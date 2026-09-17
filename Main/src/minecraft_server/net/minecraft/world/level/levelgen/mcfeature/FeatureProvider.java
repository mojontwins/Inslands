package net.minecraft.world.level.levelgen.mcfeature;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.levelgen.mcfeature.amazonvillage.FeatureAmazonVillage;
import net.minecraft.world.level.levelgen.mcfeature.fossils.FeatureFossil;
import net.minecraft.world.level.levelgen.mcfeature.icepalace.FeatureIcePalace;
import net.minecraft.world.level.levelgen.mcfeature.oceanruins.FeatureOceanRuins;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.theme.LevelThemeSettings;

public class FeatureProvider {
	/* Bounded LRU for the "already populated" failsafe. Max world size is 64x64 = 4096
	 * chunks, so 16384 slots can never evict a pending chunk in practice. */
	private static final int POPULATED_CAPACITY = 16384;
	
	public World world;
	public IChunkProvider chunkProvider; 
	
	private static final List<Class<?>> registeredFeatures;
	
	/*
	 * Phase-0 feature schedule: one deterministic pass decides, for every cell of the finite
	 * world, whether a feature is anchored there and which one. `featureMap` is a dense array
	 * indexed by WorldSize.coords2hash(x, z); a cell with no feature is null.
	 * 
	 * The schedule replaces the old per-probe sweep machinery (featureList / noFeatureChunks):
	 * the gate is evaluated exactly once per cell in a fixed, reproducible order, and feature
	 * separation (minimumSeparation, in chunks) is checked against already placed features,
	 * so the entire layout is decided up-front, before any chunk is touched.
	 * 
	 * Feature setup (schematics etc.) is deliberately NOT done here: it is deferred to the
	 * first chunk that actually draws the feature (see Feature.ensureSetup) and freed once
	 * the feature is fully consumed, so peak memory tracks the world-generation frontier.
	 */
	private Feature[] featureMap;
	
	/*
	 * Features accepted so far during scheduling, used only by the minimumSeparation test.
	 */
	private ArrayList<Feature> placedFeatures;
	
	/*
	 * Per-class ctor + long-lived probe instance, resolved once instead of reflectively
	 * per probe. Every shouldSpawn body is a pure function of (chunkProvider, world, rand,
	 * biome, chunkX, chunkZ), so the reused probe yields the byte-identical verdict a fresh
	 * allocation would.
	 */
	private Constructor<?>[] featureConstructors;
	private Feature[] probeFeatures;
	
	/*
	 * Just a small safeguard in case one of your features is not very respectful.
	 */
	private LinkedHashMap<Long, Boolean> populatedChunks;
	
	public FeatureProvider(World world, IChunkProvider chunkProvider) {
		this.world = world;
		this.chunkProvider = chunkProvider;
		this.populatedChunks = new FeatureProvider.CapacityLimitedSet(POPULATED_CAPACITY);
	}
	
	public static void registerFeature(Class<?> featureClass) {
		registeredFeatures.add(featureClass);
	}
	
	/*
	 * Resolves the (World, int, int, FeatureProvider) constructor and a long-lived probe
	 * instance for every registered class, once per provider.
	 */
	private void initializeFeatureProbes() {
		if(this.featureConstructors != null) return;
		
		int size = registeredFeatures.size();
		this.featureConstructors = new Constructor<?>[size];
		this.probeFeatures = new Feature[size];
		
		for(int i = 0; i < size; i ++) {
			Class<?> featureClass = registeredFeatures.get(i);
			if(featureClass == null) continue;
			
			try {
				Constructor<?> ctor = featureClass.getConstructor(new Class[] {
						World.class,
						Integer.TYPE,
						Integer.TYPE,
						FeatureProvider.class});
				this.featureConstructors[i] = ctor;
				this.probeFeatures[i] = (Feature)ctor.newInstance(new Object[] {this.world, 0, 0, this});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Lazily builds the whole-world feature schedule on the first feature use. On disk-loaded
	 * worlds no chunk is ever generated, so this never runs at all; after a teardown (see
	 * releaseFeatures) this stays null and the provider hands out no features.
	 */
	private void ensureFeatureMap() {
		if(this.featureMap == null) {
			this.scheduleFeatures();
		}
	}
	
	/*
	 * Decides the feature anchored at every chunk cell of the world, row-major (X outer, Z
	 * inner) - exactly the order in which generateWholeWorld provisions the chunks. Cells
	 * are independent of each other except for the minimumSeparation test against features
	 * already accepted, which makes the placement order - and result - deterministic.
	 */
	private void scheduleFeatures() {
		this.initializeFeatureProbes();
		this.featureMap = new Feature[WorldSize.getTotalChunks()];
		this.placedFeatures = new ArrayList<Feature>();
		
		for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
			for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
				Feature feature = this.scheduleFeatureAt(chunkX, chunkZ);
				if(feature != null) {
					this.featureMap[WorldSize.coords2hash(chunkX, chunkZ)] = feature;
				}
			}
		}
		
		// Give the active level theme a chance to fine-tune the schedule (add, remove or
		// replace features) before any chunk is generated from it.
		LevelThemeSettings theme = LevelThemeGlobalSettings.getTheme();
		if(theme != null) {
			theme.modifyFeatureMap(this.featureMap, this.world, this);
		}
		
		// Separation only matters while the map is being decided.
		this.placedFeatures = null;
	}
	
	/*
	 * Selects a feature (or null) for chunkX, chunkZ and registers it as placed. The gate
	 * rand is deterministic per cell; feature setup is deferred to the first consumer.
	 */
	private Feature scheduleFeatureAt(int chunkX, int chunkZ) {
		Feature feature = null;
		
		// Seed must be consistent
		long seed = this.world.getRandomSeed() + chunkX * 25117 + chunkZ * 151121;
		
		// Get a consistent random number
		final Random rand = new Random(seed);
		// Get the biome
		BiomeGenBase biome = this.world.getWorldChunkManager().getBiomeGenAt((chunkX << 4) + 8, (chunkZ << 4) + 8);
		
		// Select a feature. First feature from the list which can be spawned is selected!
		for(int i = 0; i < registeredFeatures.size(); i ++) {
			Class<?> featureClass = registeredFeatures.get(i);
			if(featureClass == null) continue;
			
			// Reuse the long-lived probe: its verdict is identical to a fresh allocation.
			Feature probe = this.probeFeatures[i];
			if(probe != null && probe.shouldFeatureSpawn(this.chunkProvider, this.world, rand, biome, chunkX, chunkZ)) {
				try {
					feature = (Feature)this.featureConstructors[i]
							.newInstance(new Object[] {this.world, chunkX, chunkZ, this});
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
		if(feature == null) return null;
		
		// Check it's not too close to other features. minimumSeparation() is declared in
		// chunks and compared here against chunk deltas (the original compared the block
		// coordinates of the centers, which made the check effectively "same cell only").
		int separation = feature.minimumSeparation();
		for(int k = 0; k < this.placedFeatures.size(); k ++) {
			Feature otherFeature = this.placedFeatures.get(k);
			if(
					Math.abs(chunkX - otherFeature.originChunkX) <= separation || 
					Math.abs(chunkZ - otherFeature.originChunkZ) <= separation
			) {
				return null;
			}
		}
		
		this.placedFeatures.add(feature);
		return feature;
	}
	
	/*
	 * Returns the feature anchored at cell (chunkX, chunkZ), or null. Range-checked: cells
	 * outside the finite world (and a torn-down provider) resolve to null.
	 */
	public Feature getFeatureAt(int chunkX, int chunkZ) {
		if(this.featureMap == null) return null;
		if(chunkX < 0 || chunkZ < 0 || chunkX >= WorldSize.xChunks || chunkZ >= WorldSize.zChunks) return null;
		return this.featureMap[WorldSize.coords2hash(chunkX, chunkZ)];
	}
	
	/*
	 * Iterates the 7x7 chunks centered on chunkX, chunkZ searching for near features.
	 * If found, their generation stage is executed.
	 * 
	 * The sweep is a single 7x7 pass (rings 0..3) visiting every cell exactly once, at the
	 * ring and row/column slot where the original quadruple-nested loops first probed it.
	 * Features are set up lazily here (first consumption) and their generation stage runs.
	 */	
	public boolean getNearestFeatures(int chunkX, int chunkZ, Chunk chunk) {
		boolean featureInChunk = false;
		if(!WorldSize.inRange(this.chunkProvider, chunkX, chunkZ)) return featureInChunk;
		this.ensureFeatureMap();
		
		ArrayList<Feature> features = new ArrayList<Feature>();
		
		for(int i = 0; i <= 3; i ++) {
			for(int x = chunkX - i; x <= chunkX + i; x ++) {
				for(int z = chunkZ - i; z <= chunkZ + i; z ++) {
					// This cell belongs to an inner ring (max(|dx|,|dz|) < i): it was already
					// probed exactly once, when its own ring ran.
					if(Math.abs(x - chunkX) < i && Math.abs(z - chunkZ) < i) continue;
					if(!WorldSize.inRange(this.chunkProvider, x, z)) continue;
					
					Feature feature = this.getFeatureAt(x, z);
					
					// i == max(|x - chunkX|, |z - chunkZ|) for this cell, exactly the ring value
					// at which the original sweep first evaluated this radius test.
					if(feature != null && feature.getFeatureRadius() >= i) {
						features.add(feature);
					}
				}
			}
		}
		
		for(int k = 0; k < features.size(); k ++) {
			Feature feature = features.get(k);
			// Materialize the feature's schematic (setup) exactly once, on first consumption.
			feature.ensureSetup();
			feature.generate(chunkX, chunkZ, chunk);
			featureInChunk = true;
		}
		
		return featureInChunk;
	}

	/* 
	 *  Populates structures for this chunk
	 */
	public void populateFeatures(World world, Random rand, int chunkX, int chunkZ) {
		if(!WorldSize.inRange(this.chunkProvider, chunkX, chunkZ)) return;
		this.ensureFeatureMap();
		
		// Failsafe (shouldn't fire, but just in case)
		long thisChunkHash = ChunkCoordIntPair.chunkXZ2Long(chunkX, chunkZ);
		if(this.populatedChunks.containsKey(thisChunkHash)) { 
			System.out.println ("Already calculated / calculating " + chunkX + " " + chunkZ); 
			return;
		}
		this.populatedChunks.put(thisChunkHash, Boolean.TRUE);
				
		// Look which nearby features would affect this chunk
		int i = 3; {
			for(int x = chunkX - i; x <= chunkX + i; x ++) {
				for(int z = chunkZ - i; z <= chunkZ + i; z ++) {
					if(!WorldSize.inRange(this.chunkProvider, x, z)) continue;			
					
					Feature feature = this.getFeatureAt(x, z);
					if(feature != null &&
							Math.abs(feature.originChunkX - x) <= feature.getFeatureRadius() &&
							Math.abs(feature.originChunkZ - z) <= feature.getFeatureRadius()
					) {
							
						feature.ensureSetup();
						feature.populate(world, rand, chunkX, chunkZ);
						
						// The feature is fully drawn once its last chunk (north-east corner of
						// its radius window) has been populated: release its schematic.
						this.releaseFeatureIfConsumed(feature, chunkX, chunkZ);
					}					
				}
			}
		}

	}
	
	/*
	 * Once the set of chunks this feature affects has been fully processed, drop its heavy
	 * structures (schematic arrays, special block lists) so peak memory tracks the frontier,
	 * not the whole world. The last consumer is the north-east cell of its radius window,
	 * i.e. the last cell of the window visited by the row-major world generation pass.
	 */
	private void releaseFeatureIfConsumed(Feature feature, int chunkX, int chunkZ) {
		int lastChunkX = feature.originChunkX + feature.getFeatureRadius();
		int lastChunkZ = feature.originChunkZ + feature.getFeatureRadius();
		if(lastChunkX >= WorldSize.xChunks) lastChunkX = WorldSize.xChunks - 1;
		if(lastChunkZ >= WorldSize.zChunks) lastChunkZ = WorldSize.zChunks - 1;
		
		if(chunkX == lastChunkX && chunkZ == lastChunkZ) {
			feature.release();
		}
	}
	
	/*
	 * Tears the provider down after the whole world has been generated: every feature's
	 * remaining structures are released and the schedule map is discarded. Called from
	 * ChunkProvider.generateWholeWorld once world generation has completed.
	 */
	public void releaseFeatures() {
		if(this.featureMap != null) {
			for(int i = 0; i < this.featureMap.length; i ++) {
				Feature feature = this.featureMap[i];
				if(feature != null) {
					feature.release();
				}
			}
		}
		this.featureMap = null;
		this.placedFeatures = null;
		this.featureConstructors = null;
		this.probeFeatures = null;
	}
	
	/*
	 * Access-order LRU map used as a bounded set.
	 */
	private static class CapacityLimitedSet extends LinkedHashMap<Long, Boolean> {
		private static final long serialVersionUID = 1L;
		private final int maxEntries;
		
		public CapacityLimitedSet(int maxEntries) {
			super(16, 0.75F, true);
			this.maxEntries = maxEntries;
		}
		
		@Override
		protected boolean removeEldestEntry(Map.Entry<Long, Boolean> eldest) {
			return this.size() > this.maxEntries;
		}
	}
	
	static {
		/*
		 * Register features here. Notice that order is important. 
		 */
		registeredFeatures = new ArrayList<Class<?>> ();
		
		registerFeature(FeatureSlimeBossLair.class);
		registerFeature(FeatureVolcano.class);
		registerFeature(FeatureAmazonVillage.class);
		registerFeature(FeatureOceanRuins.class);
		registerFeature(FeatureIcePalace.class);
		registerFeature(FeatureFossil.class);
		registerFeature(FeatureStoneArch.class);
		//registerFeature(FeatureSinkHole.class);
	}
}
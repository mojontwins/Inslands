package net.minecraft.world.level.levelgen.mcfeature;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.IChunkProvider;

public abstract class Feature {
	/*
	 * A random number between 0 and this will be generated and compared with each feature's `getSpawnChance`
	 * If it's less than getSpawnChance the feature can spawn (if `shouldSpawn` returns true)
	 */
	public static final int MAXCHANCE = 1024;
	
	public World world;
	public int originChunkX, originChunkZ;
	public int centerX, centerZ;
	public FeatureAABB featureAABB;

	/*
	 * Use a copy of the provider so features can reach out to get important stuff from elsewhere
	 */
	public FeatureProvider featureProvider;
	
	/*
	 * Whether setup() has run for this feature. Setup is deferred until the first consuming
	 * chunk actually draws the feature, so big schematic arrays are only materialized for
	 * features that end up in the world, and only when they're needed.
	 */
	private boolean isSetupComplete;
	
	/*
	 * Base constructor stores the origin chunk coordinates and calculate the feature center in blocks
	 */
	public Feature(World world, int originChunkX, int originChunkZ, FeatureProvider featureProvider) {	
		this.world = world;
		this.originChunkX = originChunkX;
		this.originChunkZ = originChunkZ;
		this.centerX = this.originChunkX * 16 + 8;
		this.centerZ = this.originChunkZ * 16 + 8;
	
		this.featureAABB = new FeatureAABB(
				(originChunkX - this.getFeatureRadius()) << 4, 
				0,
				(originChunkZ - this.getFeatureRadius()) << 4, 
				15 + ((originChunkX + this.getFeatureRadius()) << 4), 
				127,
				15 + ((originChunkZ + this.getFeatureRadius()) << 4) + 15
		);
		
		this.featureProvider = featureProvider;
	}

	/*
	 * Return this feature radius 1-3
	 */
	public abstract int getFeatureRadius();
	
	/*
	 * Return spawn chance (1-1024).
	 */
	public abstract int getSpawnChance();
	
	/*
	 * Once this feature is created and validated, this method is called to set some stuff up
	 */
	public void setup(World world, Random rand, BiomeGenBase biome, int chunkX, int chunkZ) {
	}
	
	/*
	 * Called by the feature provider. Usually you don't want to override this method.
	 */
	public boolean shouldFeatureSpawn(IChunkProvider chunkProvider, World world, Random rand, BiomeGenBase biome, int chunkX, int chunkZ) {
		return rand.nextInt(MAXCHANCE) < this.getSpawnChance() && this.shouldSpawn(chunkProvider, world, rand, biome, chunkX, chunkZ);
	}
	
	/*
	 * Return true if this feature can spawn with these chunk coordinates as a center.
	 */
	public abstract boolean shouldSpawn(IChunkProvider chunkProvider, World world, Random rand, BiomeGenBase biome, int chunkX, int chunkZ);
	
	/*
	 * Minimum amount of chunks this feature should be apart from other features
	 */
	public int minimumSeparation() {
		return this.getFeatureRadius();
	}
	
	/*
	 * Ensures this feature's setup state exists, running setup() at most once. Called lazily
	 * by the feature provider the first time a chunk consumes the feature (generation or
	 * population), so heavy per-feature data is not built during the schedule pass.
	 */
	public void ensureSetup() {
		if(this.isSetupComplete) return;
		this.isSetupComplete = true;
		
		// Deterministic per feature: the rand handed to setup must not depend on how many
		// gate probes ran before this feature won its cell, so it can be recreated reliably
		// for every rebuild (the schedule only stores the anchor, not the gate's rand).
		long seed = this.world.getRandomSeed() + this.originChunkX * 25117 + this.originChunkZ * 151121 + 51717L;
		BiomeGenBase biome = this.world.getWorldChunkManager().getBiomeGenAt((this.originChunkX << 4) + 8, (this.originChunkZ << 4) + 8);
		this.setup(this.world, new Random(seed), biome, this.originChunkX, this.originChunkZ);
	}
	
	/*
	 * Frees anything expensive this feature has built once the world knows its whole layout.
	 * Schematic features drop their arrays here; called after the feature has been fully
	 * consumed by the world generation pass, or when the provider is torn down.
	 */
	public void release() {
	}
	
	/*
	 * Called during the generation stage (in `provideChunk`). You usually want to read/write to `chunk.blocks` directly
	 */
	public abstract void generate(int chunkX, int chunkZ, Chunk chunk);
	
	/*
	 * Called during the population stage (in `populate`).
	 */
	public abstract void populate(World world, Random rand, int chunkX, int chunkZ);
}

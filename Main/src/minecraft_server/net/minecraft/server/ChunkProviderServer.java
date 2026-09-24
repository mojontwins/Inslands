package net.minecraft.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.GlobalVars;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.chunk.ChunkCoordinates;
import net.minecraft.world.level.chunk.EmptyChunk;
import net.minecraft.world.level.chunk.IChunkLoader;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

/**
 * Top-level chunk provider used by the dedicated server.
 * <p>
 * It wraps the raw terrain generator ({@link #chunkGenerator}) and adds loading, saving and
 * unloading of chunks, much like vanilla's {@code ChunkProviderServer}. For the finite Inslands
 * worlds it also offers {@link #generateWholeWorld}, which builds the entire level in one pass.
 */
public class ChunkProviderServer implements IChunkProvider {
	/** Chunk hashes that have been requested for unloading. */
	private Set<Integer> droppedChunksSet = new HashSet<Integer>();

	/** Returned when a chunk is requested but is neither loaded nor allowed to load. */
	private Chunk dummyChunk;

	/** Underlying terrain generator (normally a {@code ChunkProviderGenerate}). */
	private IChunkProvider chunkGenerator;

	/** Loads/saves chunk data from and to disk. May be null. */
	private IChunkLoader chunkLoader;

	/** When true, chunks are allowed to load even during spawn-point searches. */
	public boolean chunkLoadOverride = false;

	/** Loaded chunks keyed by {@link ChunkCoordIntPair#chunkXZ2Int}. */
	private Map<Integer, Chunk> chunksById = new HashMap<Integer, Chunk>();

	/** Loaded chunks in load order, used for iteration and saving. */
	private List<Chunk> loadedChunks = new ArrayList<Chunk>();

	/** Server world owning these chunks. */
	private WorldServer world;

	public ChunkProviderServer(WorldServer world, IChunkLoader chunkLoader, IChunkProvider chunkGenerator) {
		this.dummyChunk = new EmptyChunk(world, new byte[32768], new byte[32768], 0, 0);
		this.world = world;
		this.chunkLoader = chunkLoader;
		this.chunkGenerator = chunkGenerator;
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		return this.chunksById.containsKey(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
	}

	/**
	 * Requests that a chunk be unloaded, unless it is within the spawn-protection radius of a
	 * respawn-capable dimension.
	 */
	public void dropChunk(int chunkX, int chunkZ) {
		// Never with limited worlds
	}

	public void unloadAllChunks() {
		Iterator<Chunk> iterator = this.loadedChunks.iterator();

		while(iterator.hasNext()) {
			Chunk chunk = iterator.next();
			this.dropChunk(chunk.xPosition, chunk.zPosition);
		}
	}

	public Chunk prepareChunk(int chunkX, int chunkZ) {
		int hash = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
		this.droppedChunksSet.remove(hash);

		Chunk chunk = this.chunksById.get(hash);
		if(chunk != null) {
			return chunk;
		}

		// Prefer disk, otherwise let the generator create new terrain.
		chunk = this.loadChunkFromFile(chunkX, chunkZ);
		boolean wasGenerated = chunk == null;
		if(chunk == null) {
			chunk = this.chunkGenerator == null ? this.dummyChunk : this.chunkGenerator.provideChunk(chunkX, chunkZ);
		}

		this.chunksById.put(hash, chunk);
		this.loadedChunks.add(chunk);

		chunk.onChunkLoad();
		if(wasGenerated) {
			chunk.initLightingForRealNotJustHeightmap();
		}

		// Populate this chunk once its south-east neighbours exist, then re-check the three
		// neighbouring chunks whose own populate step needed this one to be present.
		if(!chunk.isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX + 1, chunkZ)) {
			this.populate(this, chunkX, chunkZ);
		}

		if(chunkX > 0) {
			if(this.chunkExists(chunkX - 1, chunkZ) && !this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1)) {
				this.populate(this, chunkX - 1, chunkZ);
			}
		}

		if(chunkZ > 0) {
			if(this.chunkExists(chunkX, chunkZ - 1) && !this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ - 1) && this.chunkExists(chunkX + 1, chunkZ)) {
				this.populate(this, chunkX, chunkZ - 1);
			}
		}

		if(chunkX > 0 && chunkZ > 0) {
			if(this.chunkExists(chunkX - 1, chunkZ - 1) && !this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX - 1, chunkZ)) {
				this.populate(this, chunkX - 1, chunkZ - 1);
			}
		}

		return chunk;
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		Chunk chunk = this.chunksById.get(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
		if(chunk != null) {
			return chunk;
		}
		return this.world.findingSpawnPoint || this.chunkLoadOverride ? this.prepareChunk(chunkX, chunkZ) : this.dummyChunk;
	}

	private Chunk loadChunkFromFile(int chunkX, int chunkZ) {
		if(this.chunkLoader == null) {
			return null;
		}

		try {
			Chunk chunk = this.chunkLoader.loadChunk(this.world, chunkX, chunkZ);
			if(chunk != null) {
				chunk.lastSaveTime = this.world.getWorldTime();
			}
			return chunk;
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	private void saveChunkExtraData(Chunk chunk) {
		if(this.chunkLoader == null) {
			return;
		}

		try {
			this.chunkLoader.saveExtraChunkData(this.world, chunk);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void saveChunkData(Chunk chunk) {
		if(this.chunkLoader == null) {
			return;
		}

		try {
			chunk.lastSaveTime = this.world.getWorldTime();
			this.chunkLoader.saveChunk(this.world, chunk);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
		Chunk chunk = this.provideChunk(chunkX, chunkZ);
		if(chunk.isTerrainPopulated) {
			return;
		}

		chunk.isTerrainPopulated = true;
		if(this.chunkGenerator == null) {
			return;
		}

		this.chunkGenerator.populate(chunkProvider, chunkX, chunkZ);
		chunk.setChunkModified();
	}

	public boolean saveChunks(boolean saveAll, IProgressUpdate progress) {
		int savedCount = 0;

		for(int index = 0; index < this.loadedChunks.size(); ++index) {
			Chunk chunk = this.loadedChunks.get(index);
			if(saveAll && !chunk.neverSave) {
				this.saveChunkExtraData(chunk);
			}

			if(chunk.needsSaving(saveAll)) {
				this.saveChunkData(chunk);
				chunk.isModified = false;
				++ savedCount;
				// During autosave, cap the work per call so the game does not stall.
				if(savedCount == 24 && !saveAll) {
					return false;
				}
			}
		}

		if(saveAll) {
			if(this.chunkLoader == null) {
				return true;
			}
			this.chunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return !this.world.levelSaving;
	}

	public String makeString() {
		return "ServerChunkCache: " + this.chunksById.size() + " Drop: " + this.droppedChunksSet.size();
	}

	@Override
	public Chunk justGenerateForHeight(int chunkX, int chunkZ) {
		return this.chunkGenerator.justGenerateForHeight(chunkX, chunkZ);
	}

	@Override
	public IChunkProvider getChunkProviderGenerate() {
		return this.chunkGenerator;
	}

	@Override
	public Chunk makeBlank(World ignoredWorld) {
		return this.chunkGenerator.makeBlank(ignoredWorld);
	}

	/**
	 * Generates and lights the entire finite world in one non-interactive pass.
	 * <p>
	 * The work is split into three deliberately separate phases:
	 * <ol>
	 *   <li><b>Terrain</b>: every chunk is generated and cached, but not lit.</li>
	 *   <li><b>Populate</b>: every chunk is decorated (ores, trees, structures...). Lighting
	 *       is deferred through {@link World#deferLightingUpdate}, so the thousands of block
	 *       writes a decorate step performs do not each trigger a Starlight recalculation.</li>
	 *   <li><b>Light</b>: the height maps are rebuilt and a single full-range lighting pass is
	 *       run per chunk, with Starlight propagating light across chunk borders.</li>
	 * </ol>
	 * Because every chunk exists before populate begins, features that read or write across
	 * chunk borders behave exactly as they do in the normal interactive path.
	 *
	 * @param progress optional sink advanced over {@code 3 * totalChunks} steps
	 */
	public void generateWholeWorld(IProgressUpdate progress) {
		final int totalSteps = WorldSize.getTotalChunks() * 3;
		int step = 0;

		// Phase 1: terrain only. No lighting yet so that nothing is lit twice.
		for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
			for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
				step = reportProgress(progress, step, totalSteps);

				Chunk chunk = this.chunkGenerator.provideChunk(chunkX, chunkZ);
				int hash = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
				this.chunksById.put(hash, chunk);
				this.loadedChunks.add(chunk);
				chunk.onChunkLoad();
			}
		}
		GlobalVars.didGenerateChunks = true;

		// Theme hook between terrain generation (phase 1) and population
		// (phase 2): lets a theme prepare the freshly generated chunks.
		LevelThemeGlobalSettings.getTheme().specialPrePopulation(this.world);

		// Phase 2: populate with per-block lighting disabled.
		this.world.deferLightingUpdate = true;
		try {
			for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
				for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
					step = reportProgress(progress, step, totalSteps);
					this.populate(this, chunkX, chunkZ);
				}
			}
		} finally {
			// Always restore the flag: leaving it set would permanently stop all lighting.
			this.world.deferLightingUpdate = false;
		}

		// Phase 3: one complete lighting pass over the finished world.
		for(int index = 0; index < this.loadedChunks.size(); index ++) {
			step = reportProgress(progress, step, totalSteps);

			Chunk chunk = this.loadedChunks.get(index);
			chunk.generateHeightMap();
			chunk.generateLandSurfaceHeightMap();
			chunk.initLightingForRealNotJustHeightmap(true);
		}

		// Theme-specific post generation, once the whole world is generated, populated
		// and lit.
		LevelThemeGlobalSettings.getTheme().specialPostGeneration(this.world);
	}

	/** Advances the loading progress by one step, if a progress sink was supplied. */
	private static int reportProgress(IProgressUpdate progress, int step, int totalSteps) {
		if(progress != null && totalSteps > 0) {
			progress.setLoadingProgress(step * 100 / totalSteps);
		}
		return step + 1;
	}
}

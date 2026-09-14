package net.minecraft.world.level.chunk;

import java.io.IOException;

import net.minecraft.world.GlobalVars;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;

/**
 * Chunk provider for the finite Inslands worlds.
 * <p>
 * A finite world is small enough that every chunk fits in memory at once, so this provider
 * keeps them in a flat {@link #chunkCache} indexed by {@link WorldSize#coords2hash(int, int)}
 * rather than a sliding cache. Requests outside the world bounds resolve to a shared
 * {@link #blankChunk}.
 * <p>
 * Modified, simplified version for finite worlds by na_th_an.
 */
public class ChunkProvider implements IChunkProvider {
	/** Returned for any request outside the finite world bounds. */
	private final Chunk blankChunk;

	/** The real generator that produces and populates terrain chunks. */
	private final IChunkProvider chunkGenerator;

	/** Loads/saves chunk data from and to disk. May be null for purely in-memory worlds. */
	private final IChunkLoader chunkLoader;

	/** The world this provider belongs to. */
	private final World world;

	/** Flat cache holding every chunk of the world, indexed by {@link WorldSize#coords2hash}. */
	private final Chunk[] chunkCache;

	public ChunkProvider(World world, IChunkLoader chunkLoader, IChunkProvider chunkGenerator) {
		this.world = world;
		this.chunkLoader = chunkLoader;
		this.chunkGenerator = chunkGenerator;
		this.blankChunk = chunkGenerator.makeBlank(world);
		this.chunkCache = new Chunk[WorldSize.getTotalChunks()];
	}

	public IChunkProvider getChunkProviderGenerate() {
		return this.chunkGenerator;
	}

	/**
	 * The {@link IChunkProvider} contract requires this method, but the finite world already
	 * knows its own dimensions, so the supplied world is ignored in favour of {@link #world}.
	 */
	public Chunk makeBlank(World ignoredWorld) {
		return this.chunkGenerator.makeBlank(this.world);
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		// Out-of-bounds chunks are treated as "present" so border populate checks succeed.
		if(chunkX < 0 || chunkZ < 0 || chunkX >= WorldSize.getXChunks(this.chunkGenerator) || chunkZ >= WorldSize.getZChunks(this.chunkGenerator)) {
			return true;
		}
		return this.chunkCache[WorldSize.coords2hash(chunkX, chunkZ)] != null;
	}

	public Chunk prepareChunk(int chunkX, int chunkZ) {
		if(chunkX < 0 || chunkX >= WorldSize.getXChunks(this.chunkGenerator) || chunkZ < 0 || chunkZ >= WorldSize.getZChunks(this.chunkGenerator)) {
			return this.blankChunk;
		}

		int cacheIndex = WorldSize.coords2hash(chunkX, chunkZ);
		Chunk chunk = this.chunkCache[cacheIndex];
		if(chunk != null) {
			return chunk;
		}

		// Prefer loading from disk; otherwise let the generator create fresh terrain.
		boolean wasGenerated = false;
		chunk = this.loadChunkFromFile(chunkX, chunkZ);
		if(chunk == null) {
			if(this.chunkGenerator == null) {
				chunk = this.blankChunk;
			} else {
				chunk = this.chunkGenerator.provideChunk(chunkX, chunkZ);
				wasGenerated = true;
				GlobalVars.didGenerateChunks = true;
			}
		}

		this.chunkCache[cacheIndex] = chunk;

		if(chunk != null) {
			chunk.onChunkLoad();
			if(wasGenerated) {
				chunk.initLightingForRealNotJustHeightmap();
			}
		}

		// A chunk can only be populated once its south-east neighbours exist, because features
		// may read across borders. The same condition is then re-checked for the neighbouring
		// chunks whose own populate step needed this one to be present.
		if(!chunk.isTerrainPopulated &&
				this.chunkExists(chunkX + 1, chunkZ + 1) &&
				this.chunkExists(chunkX, chunkZ + 1) &&
				this.chunkExists(chunkX + 1, chunkZ)) {
			this.populate(this, chunkX, chunkZ);
		}

		if(chunkX > 0 &&
				this.chunkExists(chunkX - 1, chunkZ) &&
				!this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated &&
				this.chunkExists(chunkX - 1, chunkZ + 1) &&
				this.chunkExists(chunkX, chunkZ + 1)) {
			this.populate(this, chunkX - 1, chunkZ);
		}

		if(chunkZ > 0 &&
				this.chunkExists(chunkX, chunkZ - 1) &&
				!this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated &&
				this.chunkExists(chunkX + 1, chunkZ - 1) &&
				this.chunkExists(chunkX + 1, chunkZ)) {
			this.populate(this, chunkX, chunkZ - 1);
		}

		if(chunkX > 0 && chunkZ > 0 &&
				this.chunkExists(chunkX - 1, chunkZ - 1) &&
				!this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated &&
				this.chunkExists(chunkX, chunkZ - 1) &&
				this.chunkExists(chunkX - 1, chunkZ)) {
			this.populate(this, chunkX - 1, chunkZ - 1);
		}

		return chunk;
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		if(chunkX < 0 || chunkX >= WorldSize.getXChunks(this.chunkGenerator) || chunkZ < 0 || chunkZ >= WorldSize.getZChunks(this.chunkGenerator)) {
			return this.blankChunk;
		}

		Chunk chunk = this.chunkCache[WorldSize.coords2hash(chunkX, chunkZ)];
		return chunk == null ? this.prepareChunk(chunkX, chunkZ) : chunk;
	}

	public Chunk justGenerateForHeight(int chunkX, int chunkZ) {
		if(chunkX >= 0 && chunkX < WorldSize.getXChunks(this.chunkGenerator) && chunkZ >= 0 && chunkZ < WorldSize.getZChunks(this.chunkGenerator)) {
			return this.chunkGenerator.justGenerateForHeight(chunkX, chunkZ);
		}
		return this.blankChunk;
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

	private void saveExtraChunkData(Chunk chunk) {
		if(this.chunkLoader == null) {
			return;
		}

		try {
			this.chunkLoader.saveExtraChunkData(this.world, chunk);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void saveChunk(Chunk chunk) {
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

		for(int cacheIndex = 0; cacheIndex < WorldSize.getTotalChunks(); cacheIndex ++) {
			Chunk chunk = this.chunkCache[cacheIndex];
			if(chunk == null) {
				continue;
			}

			if(saveAll && !chunk.neverSave) {
				this.saveExtraChunkData(chunk);
			}

			if(chunk.needsSaving(saveAll)) {
				this.saveChunk(chunk);
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

	/** Invalid for a finite world: chunks are kept in memory and never unloaded. */
	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "ServerChunkCache: " + this.chunkCache.length;
	}

	/**
	 * Generates and lights the entire finite world in one non-interactive pass.
	 * <p>
	 * The work is split into three deliberately separate phases:
	 * <ol>
	 *   <li><b>Terrain</b>: every chunk is loaded from disk if available, otherwise generated
	 *       and cached — but not lit.</li>
	 *   <li><b>Populate</b>: every chunk is decorated (ores, trees, structures...). Lighting
	 *       is deferred through {@link World#deferLightingUpdate}, so the thousands of block
	 *       writes a decorate step performs do not each trigger a Starlight recalculation.</li>
	 *   <li><b>Light</b>: the height maps are rebuilt and a single full-range lighting pass is
	 *       run per chunk, with Starlight propagating light across chunk borders.</li>
	 * </ol>
	 * Because every chunk exists before populate begins, features that read or write across
	 * chunk borders behave exactly as they do in the normal interactive path.
	 * <p>
	 * If every chunk was already on disk ({@code anyGenerated == false} after Phase 1), the
	 * populate and lighting phases are skipped entirely — the world is final as-is. This
	 * makes nether re-entry on the client (where the previous DIM-1 is saved) nearly instant.
	 *
	 * @param progress optional sink advanced over {@code 3 * totalChunks} steps
	 */
	public void generateWholeWorld(IProgressUpdate progress) {
		final int totalSteps = WorldSize.getTotalChunks() * 3;
		int step = 0;
		boolean anyGenerated = false;

		// Phase 1: prefer disk, else generate. Record whether anything was generated.
		for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
			for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
				step = reportProgress(progress, step, totalSteps);

				int cacheIndex = WorldSize.coords2hash(chunkX, chunkZ);
				Chunk chunk = this.loadChunkFromFile(chunkX, chunkZ);
				if(chunk == null) {
					chunk = this.chunkGenerator.provideChunk(chunkX, chunkZ);
					GlobalVars.didGenerateChunks = true;
					anyGenerated = true;
				}
				this.chunkCache[cacheIndex] = chunk;
				chunk.onChunkLoad();                   // NO initLighting: loaded chunks are final,
			}                                          // generated chunks are lit once in Phase 3
		}

		// Phase 2 + Phase 3 run only if something was actually created this pass.
		if(anyGenerated) {
			this.world.deferLightingUpdate = true;
			try {
				for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
					for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
						step = reportProgress(progress, step, totalSteps);
						this.populate(this, chunkX, chunkZ);   // skips isTerrainPopulated (loaded) chunks
					}
				}
			} finally {
				// Always restore the flag: leaving it set would permanently stop all lighting.
				this.world.deferLightingUpdate = false;
			}

			// Relight the WHOLE world once if any chunk was generated — never just the new
			// ones: populated features may have written across borders into loaded neighbours,
			// and a single deterministic pass kills any seam between fresh and saved light.
			for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
				for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
					step = reportProgress(progress, step, totalSteps);

					Chunk chunk = this.chunkCache[WorldSize.coords2hash(chunkX, chunkZ)];
					chunk.generateHeightMap();
					chunk.generateLandSurfaceHeightMap();
					chunk.initLightingForRealNotJustHeightmap(true);
				}
			}
		}
	}

	/** Advances the loading progress by one step, if a progress sink was supplied. */
	private static int reportProgress(IProgressUpdate progress, int step, int totalSteps) {
		if(progress != null && totalSteps > 0) {
			progress.setLoadingProgress(step * 100 / totalSteps);
		}
		return step + 1;
	}
}

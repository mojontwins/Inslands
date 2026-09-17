package net.minecraft.world.level;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;

/**
 * Bounded LRU cache for height-only chunk queries (land surface height map, ocean flag and
 * urban flag) on chunks that have not been generated yet.
 * <p>
 * The four {@link World} facades ({@code getLandSurfaceHeightValue}, {@code isOceanChunk},
 * {@code isUrbanChunk} and {@code justGenerateForHeight}) used to run a full terrain
 * generation on every miss. Callers commonly query the same not-yet-generated neighbour chunk
 * many times during one populate pass, so each query used to cost a full
 * {@code generateTerrain}. This cache memoises the cheap height-only payload instead of a
 * whole {@link Chunk}: an entry is roughly 350 bytes (a 256 byte height map plus two flags),
 * so even a full-capacity cache stays far under 100 KB. Caching whole height-only chunks
 * (2 x 32 KB block arrays each) unbounded would waste tens of MB.
 * <p>
 * The cached values are the exact values a fresh generation would produce because the
 * generator is seed-deterministic. Whenever the real chunk is generated the {@link World}
 * facades short-circuit past the cache to the live chunk, so a stale entry is never served;
 * and if that chunk later unloads, the cache returns the base pre-population height, which is
 * precisely what these queries are defined to answer.
 */
public class TerrainHeightQueryCache {
	public static final int DEFAULT_CAPACITY = 256;

	/** Lightweight per-chunk payload stored in the cache. */
	public static class TerrainHeightEntry {
		public final int chunkX;
		public final int chunkZ;
		public final byte[] landSurfaceHeightMap;
		public final boolean isOcean;
		public final boolean isUrbanChunk;

		public TerrainHeightEntry(Chunk chunk) {
			this.chunkX = chunk.xPosition;
			this.chunkZ = chunk.zPosition;
			this.landSurfaceHeightMap = chunk.landSurfaceHeightMap.clone();
			this.isOcean = chunk.isOcean;
			this.isUrbanChunk = chunk.isUrbanChunk;
		}
	}

	private final int capacity;
	private final Map<Integer, TerrainHeightEntry> cache;
	private int hitCount;
	private int missCount;

	public TerrainHeightQueryCache() {
		this(DEFAULT_CAPACITY);
	}

	public TerrainHeightQueryCache(int capacity) {
		this.capacity = capacity;
		this.cache = new LinkedHashMap<Integer, TerrainHeightEntry>(16, 0.75F, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<Integer, TerrainHeightEntry> eldest) {
				return this.size() > TerrainHeightQueryCache.this.capacity;
			}
		};
	}

	/**
	 * Returns the cached entry for the given chunk, running a fresh height-only generation
	 * through the world's chunk provider on a miss.
	 */
	public TerrainHeightEntry getOrCompute(int chunkX, int chunkZ, World world) {
		int key = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
		TerrainHeightEntry entry = this.cache.get(key);
		if(entry == null) {
			this.missCount ++;
			entry = new TerrainHeightEntry(world.chunkProvider.justGenerateForHeight(chunkX, chunkZ));
			this.cache.put(key, entry);
		} else {
			this.hitCount ++;
		}
		return entry;
	}

	/**
	 * Synthesises a real {@link Chunk} from a cached entry (no 2 x 32 KB block arrays).
	 * The height-only result never has a building, so {@code hasBuilding} stays false.
	 */
	public Chunk buildChunk(int chunkX, int chunkZ, World world, TerrainHeightEntry entry) {
		Chunk chunk = new Chunk(world, chunkX, chunkZ);
		System.arraycopy(entry.landSurfaceHeightMap, 0, chunk.landSurfaceHeightMap, 0, entry.landSurfaceHeightMap.length);
		chunk.isOcean = entry.isOcean;
		chunk.isUrbanChunk = entry.isUrbanChunk;
		chunk.hasBuilding = false;
		return chunk;
	}

	public void evict(int chunkX, int chunkZ) {
		this.cache.remove(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
	}

	public int getHitCount() {
		return this.hitCount;
	}

	public int getMissCount() {
		return this.missCount;
	}
}
# Plan: Per-Chunk Temperature / Humidity / Biome Caching

## Problem

Temperature and humidity values are recomputed from noise generators every time they're needed. The `WorldChunkManager` holds these in **reused scratch arrays** (`temperature[]`, `humidity[]`) that get overwritten on any subsequent call. Currently:

1. **`ChunkProviderGenerate.provideChunk`** calls `loadBlockGeneratorData` → populates `temperature[]/humidity[]` with processed values
2. Then calls `initializeNoiseField` → calls `getBiomesForGeneration(21×21)` → **overwrites** the scratch arrays with a different 21×21 grid
3. If `Chunk.refreshCaches()` is called later (lazy, on first `getBiomeGenAt()` after load), it calls `loadBlockGeneratorData` **again** to regenerate temperature/humidity — redundant work

The biome array (`biomeGenCache`) is already stored per-chunk but **never persisted to disk**, so it's also regenerated on every chunk load.

## Solution: 3 per-chunk caches

Add to `Chunk`:

| Field | Type | Size | Purpose |
|-------|------|------|---------|
| `temperatureCache` | `float[]` | 256 | Per-block temperature (0–1) |
| `humidityCache` | `float[]` | 256 | Per-block humidity (0–1) |
| `biomeIdCache` | `byte[]` | 256 | Per-block biome ID (byte) |

All indexed as `[x << 4 | z]` (same as existing `biomeGenCache`).

## Implementation steps

### Phase 1 — Chunk fields + Population (both trees)

1. **`Chunk.java`** (both trees): Add 3 fields after existing `biomeGenCache` (line 55):
   ```java
   public float[] temperatureCache = null;
   public float[] humidityCache = null;
   public byte[] biomeIdCache = null;
   ```

2. **`ChunkProviderGenerate.provideChunk()`** (both trees, after line 306): Clone all caches from the first `loadBlockGeneratorData` call (the only one with correct data for this chunk):
   ```java
   // After chunk.biomeGenCache = this.biomesForGeneration.clone();
   chunk.temperatureCache = new float[256];
   chunk.humidityCache = new float[256];
   chunk.biomeIdCache = new byte[256];
   for (int i = 0; i < 256; i++) {
       chunk.temperatureCache[i] = (float) this.worldObj.getWorldChunkManager().temperature[i];
       chunk.humidityCache[i] = (float) this.worldObj.getWorldChunkManager().humidity[i];
       chunk.biomeIdCache[i] = (byte) this.biomesForGeneration[i].byteId;
   }
   ```

3. **Same for `ChunkProviderHell.provideChunk()`** and **`ChunkProviderSky.provideChunk()`**.

### Phase 2 — World accessor methods (both trees)

Add to **`World.java`**:
```java
public float getTemperatureAt(int blockX, int blockZ) {
    Chunk chunk = this.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
    if (chunk.temperatureCache == null) return 0.5F;
    return chunk.temperatureCache[(blockX & 0xF) << 4 | (blockZ & 0xF)];
}

public float getHumidityAt(int blockX, int blockZ) {
    Chunk chunk = this.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4);
    if (chunk.humidityCache == null) return 0.5F;
    return chunk.humidityCache[(blockX & 0xF) << 4 | (blockZ & 0xF)];
}

public BiomeGenBase getBiomeAt(int blockX, int blockZ) {
    return this.getChunkFromChunkCoords(blockX >> 4, blockZ >> 4)
        .getBiomeGenAt(blockX & 0xF, blockZ & 0xF);
}
```

### Phase 3 — Update `Chunk.refreshCaches()` (both trees)

Rewrite the multi-biome branch (lines 919–934) to prefer chunk caches over `WorldChunkManager`:

```java
// Current: calls loadBlockGeneratorData again to recompute
// New: use cached temperature/humidity/biome if available
if (this.temperatureCache != null && this.humidityCache != null) {
    this.biomeGenCache = new BiomeGenBase[256];
    this.grassColorCache = new int[256];
    this.foliageColorCache = new int[256];
    for (int x = 0; x < 16; x++) {
        for (int z = 0; z < 16; z++) {
            int i = x << 4 | z;
            this.biomeGenCache[i] = BiomeGenBase.getBiomeFromLookup(
                this.temperatureCache[i], this.humidityCache[i]);
            this.grassColorCache[i] = ColorizerGrass.getGrassColor(
                this.temperatureCache[i], this.humidityCache[i]);
            this.foliageColorCache[i] = ColorizerFoliage.getFoliageColor(
                this.temperatureCache[i], this.humidityCache[i]);
        }
    }
} else {
    // Fallback for old chunks without cache — same as current code
    ...
}
```

### Phase 4 — NBT serialization (`ChunkLoader.java`, both trees)

**Save** (`storeChunkInCompound`): After existing tags, write:
```java
nbt.setTag("Temperature", new NBTTagByteArray(temperatureBytes));
nbt.setTag("Humidity", new NBTTagByteArray(humidityBytes));
nbt.setTag("BiomeIds", new NBTTagByteArray(chunk.biomeIdCache));
```

Conversion for float→byte: `(byte) Math.round(value * 255)` for [0,1] range.
Decompression on load: `(float)(b & 0xFF) / 255.0F`.

**Load** (`loadChunkIntoWorldFromCompound`): Read the 3 tags back, reconstruct arrays. For backward compatibility, if tags are missing → leave caches null (existing lazy `refreshCaches()` handles it).

### Phase 5 — Reroute gameplay callers (both trees)

The research shows **zero runtime callers** that read `WorldChunkManager.temperature[]` or `humidity[]` directly — the only readers are `Chunk.refreshCaches()` (handled in Phase 3). All gameplay code already goes through `World.getBiomeGenAt()` → `Chunk.getBiomeGenAt()` → `biome.t` / `biome.h`.

So no gameplay rerouting is needed — the caches are primarily for:
- Eliminating the re-computation in `refreshCaches()`
- Persisting biome data to disk
- Providing `World.getTemperatureAt()` / `getHumidityAt()` for future use

### Phase 6 — Cleanup

- Remove dead `getTemperature(int,int)` and `getTemperatures(double[],...)` methods from `WorldChunkManager` (both trees) — zero callers, now superseded
- Rename the `temperature`/`humidity` scratch fields to `temperatureScratch`/`humidityScratch` for clarity (both trees)

## Files touched

| File | Trees | Change |
|------|-------|--------|
| `Chunk.java` | Both | Add 3 cache fields |
| `ChunkProviderGenerate.java` | Both | Populate caches after first `loadBlockGeneratorData` |
| `ChunkProviderHell.java` | Both | Same |
| `ChunkProviderSky.java` | Both | Same |
| `World.java` | Both | Add `getTemperatureAt`, `getHumidityAt`, `getBiomeAt` |
| `ChunkLoader.java` | Both | Save/load 3 new NBT tags |
| `WorldChunkManager.java` | Both | Rename scratch fields, remove dead methods |

## Verification

- Compile both trees: `javac` with argfiles, exit=0, 0 errors
- `fc.exe /b` on all 7 file pairs to confirm byte-identical edits

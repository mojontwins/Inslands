Inslands is a fork of b1.7.3 with the Chunk Provider modified so all chunks are generated and stored in memory for limited size levels. Disk operations are none during gameplay, game never loads new chunks and chunks are only saved to disk on pause or exit. The idea is efficiency in systems with very slow storage and weak CPUs. 

My current plan is changing how the world is actually generated. Right now, the world is generated like in vanilla: when a chunk is needed (when entering the world, a for ensures all the chunks in the limited size world are needed and thus generated or loaded), ChunkProvider (prepareChunk) delegates to ChunkProviderGenerate which creates a new Chunk, generates terrain for it, replace the blocks based on biome, bulds some features directly on the chunk/metadata arrays, carves ravines and caves, and finally calculates initial skylight. The provider then, after calling `initLightingForRealNotJustHeightmap` (starlight system), once it ensures (x, z) + 1 chunks exist, calls populate to add trees and other features. This population uses "ingame" methods to place blocks meaning that light is recalculated for all of them.

This process is slow.

My plan is changing how the world is created. When creating a new world:

* All chunks that belong to the current limited world are created.
* The generate (ChunkProviderGenerate.provideChunk) phase is performed on every chunk. 
* The populate (ChunkProviderGenerate.populate) phase is performed on every chunk, but using special methods that don't recalculate lighting.
* Once everything is in place, lighting is calculated for every chunk, for the complete y=0..128 range just like in the nether.
* The whole level is saved to the disk for the first time.

The process of loading a world wouldn't change. The world would still be chunk based. ChunkExists always returns true. Other shortcuts may be taken.

---

# Implementation Plan

## 1. Current pipeline (as built)

Straight-line path when a new world is created:

1. `MinecraftServer.initWorld()` → `preloadWorld()` (`MinecraftServer.java:139`) or client `Minecraft.startWorld()` → `preloadWorld()` (`Minecraft.java:1489`) loop over every chunk in `WorldSize.xChunks` x `WorldSize.zChunks`, forcing generation through `world.getBlockID(x, 64, z)` / `prepareChunk(x, z)`.
2. `ChunkProviderServer.prepareChunk()` (`ChunkProviderServer.java:67`) or modded `ChunkProvider.prepareChunk()` (`ChunkProvider.java:50`) calls the raw generator's `provideChunk()`.
3. `ChunkProviderGenerate.provideChunk()` (`ChunkProviderGenerate.java:290`) generates terrain, terraforms, carves caves/ravines/mineshafts, computes height maps and a preliminary `generateSkylightMap()`.
4. Back in the provider, `chunk.initLightingForRealNotJustHeightmap()` (`Chunk.java:968`) runs **per chunk, immediately**, the moment a chunk is generated. This calls Starlight `blockLight.initBlockLight()` + `skyLight.initSkylight()` (`StarlightEngine.java:588` / `:565`).
5. Once the `(x,z+1)`, `(x+1,z)`, `(x+1,z+1)` neighbors exist, `populate()` runs for a chunk. Every block placed by populate (trees, ores, flowers, dungeons, snow, biome `populate()`, `FeatureProvider`, layered sand...) goes through `World.setBlock*()` → `Chunk.setblockIDWithMetadata()` → `Chunk.updateLight()` → **per-block Starlight recalculation** (emittance + skylight checks, `StarlightEngine.java:420`/`:463`).
6. Chunks are saved lazily (autosave / exit).

**Why it is slow:** the two most expensive lighting operations — full-column skylight init and per-block emittance propagation — are done many, many times per chunk (once per generated chunk + once per each populate block placement, with Starlight queue setup/teardown and a 5x5 chunk cache build each time).

## 2. Design decision: "defer, don't amputate"

The proposal (document lines 7–13) asks for populate during world creation using "special methods that don't recalculate lighting". Two ways to do this:

- **A. Duplicate methods** — add a shadow set of block-placers (`setBlockNoLight()`, `WorldGenThingNoLight()`, …) and repoint ~30 WorldGenerator/BiomeGen/Feature classes at them. Huge diff, two source trees, easy to miss a path.
- **B. Global lighting-suppress switch (chosen)** — a single `World` boolean that `Chunk.setblockIDWithMetadata()` / `setblockIDAndMetadataColumn()` consult before calling `updateLight()`. One switch flips the whole populate phase (ores, trees, features, biome spawn, snow, layered sand, mineshafts, mazes, dungeons) without touching any generator class. Lighting is recomputed **once per chunk** in a final closed-world pass.

B is one boolean, three guarded call sites, one light pass, and an orchestration method. It also degrades gracefully: if the flag is accidentally left on, only lighting is wrong, never block IDs.

Keep the cheap **height-map maintenance** (`Chunk.relightBlock()`, `Chunk.java:242`) active during populate even in defer mode — populate logic (`getHeightValue`, `findTopSolidBlockUsingBlockMaterial`, snow/layered-sand) relies on the live height map. Only Starlight calls (`updateLight()`) are suppressed.

## 3. New world-creation flow (target)

```
Phase 1  GENERATE  — for every chunk (row-major): generator.provideChunk(x,z)
                     → cache it, onChunkLoad(). NO lighting init.
Phase 2  POPULATE  — for every chunk (row-major): provider.populate(x,z)
                     with World.deferLighting = true (all placement skips Starlight).
Phase 3  LIGHT     — for every chunk: generateHeightMap() + generateLandSurfaceHeightMap()
                     + initLightingForRealNotJustHeightmap(forceFullRange = true).
                     Nether-style full y=0..127 scan for every chunk.
Phase 4  SAVE ONCE — world.saveWorld(true, progress). All chunks flagged modified → single
                     full write. (Client changeWorld() also saves the new world, see §5.4.)
```

Phase ordering matters: populate for chunk `(x,z)` reads/writes across chunk borders (`getHeightValue(x0±1, z0±1)`), so **all** chunks must exist before Phase 2 starts. Phase 3 must run after populate because populated blocks (trees, torches, glowstone-field light) are reflected in the final light arrays.

Determinism is preserved because `ChunkProviderGenerate.provideChunk()` reseeds `this.rand` per chunk (`ChunkProviderGenerate.java:295`) and `populate()` reseeds from `worldSeed ^ (chunkX*seed1 + chunkZ*seed2)` (`:707-710`); iteration order does not affect output, so client (SSP) and dedicated server stay byte-for-byte identical.

## 4. Code changes (edit BOTH source trees in sync — AGENTS.md rule)

### 4.1 `World` — add lighting-suppress flag
`Main/src/minecraft/.../world/level/World.java` line ~70; server tree line ~70.

Add next to `public boolean editingBlocks;`:

```java
/** When true, Chunk.setblockID* skips per-block Starlight updates (bulk world gen). */
public boolean deferLightingUpdate = false;
```

Initialize `false` in all three constructors (with the `editingBlocks = false` lines ~145/186/239). Also add a convenience on top of `World.getChunkProvider()` area (`World.java:302`) if desired later — not required.

Optionally: put the "new world" detection on `World` (`isNewWorld` already exists) so callers don't duplicate logic.

### 4.2 `Chunk` — guard the per-block light calls
`Main/src/minecraft/.../world/level/chunk/Chunk.java` (server + client).

Three edits:

1. `setblockIDWithMetadata()` (`:269`, the `updateLight(x,y,z)` call at `:312`):
   ```java
   if(!this.worldObj.deferLightingUpdate) this.updateLight(x, y, z);
   ```
   Leave `relightBlock()` (height map) as-is — it is cheap and kept correct for placement logic.

2. `setblockIDAndMetadataColumn()` (`:324`, the `updateLight(x,y,z)` at `:383`):
   ```java
   if(!this.worldObj.deferLightingUpdate) this.updateLight(x, y, z);
   ```

3. `setBlockMetadata()` (`:398`) touches neither light nor height map — no change.

Do **NOT** gate `relightBlock()`; it only updates `heightMap[]`/`heightMapMinimum` (no Starlight). Keep it live so Phase 2 snow/layered-sand/flowers placement stays correct.

### 4.3 `Chunk` — forced full-range lighting init
`Chunk.java` `initLightingForRealNotJustHeightmap()` (`:968`). Add an overload:

```java
public void initLightingForRealNotJustHeightmap() {
    this.initLightingForRealNotJustHeightmap(this.worldObj.getBiomeGenAt(this.xPosition, this.zPosition).forceBlockLightInitLikeNether);
}

public void initLightingForRealNotJustHeightmap(boolean forceFullRange) {
    this.worldObj.blockLight.initBlockLight(this.xPosition, this.zPosition, forceFullRange);
    if(!this.worldObj.worldProvider.hasNoSky) {
        this.worldObj.skyLight.initSkylight(this.xPosition, this.zPosition);
    }
}
```

`initBlockLight(..., true)` triggers the nether-style **full y=0..127 emitter scan** (`StarlightEngine.java:591-618`). This is the "complete y=0..128 range just like the nether" lighting from the proposal. Normal (false) mode only re-propagates pre-placed emitters, which is why the full-range scan is mandatory for the bulk pass.

The existing no-arg method keeps the old biome-driven behaviour for the legacy path (loading/generating existing 8-bit worlds and the nether), so no runtime behaviour changes outside the new code path.

### 4.4 Modded `ChunkProvider` — `generateWholeWorld()` orchestration
`Main/src/minecraft/.../world/level/chunk/ChunkProvider.java` (server + client; this is the provider used by client/SSP `World.getChunkProvider()`, `World.java:302`).

Add a method (uses the existing `chunkCache`, `chunkProvider`, `blankChunk`, `populate()`):

```java
/** Generates the entire finite world in one shot: terrain, populate, then a single lighting pass. */
public void generateWholeWorld(IProgressUpdate progress) {
    int total = WorldSize.getTotalChunks();
    int done = 0;

    // ---- Phase 1: terrain for every chunk (row-major, matching legacy preload order) ----
    for(int x = 0; x < WorldSize.xChunks; x ++) {
        for(int z = 0; z < WorldSize.zChunks; z ++) {
            if(progress != null) progress.setLoadingProgress(done++ * 100 / total);
            Chunk chunk = this.chunkProvider.provideChunk(x, z);
            this.chunkCache[WorldSize.coords2hash(x, z)] = chunk;
            chunk.onChunkLoad();                       // NO initLighting here
        }
    }
    GlobalVars.didGenerateChunks = true;

    // ---- Phase 2: populate everything, suppressing per-block light recalcs ----
    this.worldObj.deferLightingUpdate = true;
    try {
        for(int x = 0; x < WorldSize.xChunks; x ++) {
            for(int z = 0; z < WorldSize.zChunks; z ++) {
                if(progress != null) progress.setLoadingProgress(done++ * 100 / total);
                this.populate(this, x, z);             // sets isTerrainPopulated + isModified
            }
        }
    } finally {
        this.worldObj.deferLightingUpdate = false;     // ALWAYS reset
    }

    // ---- Phase 3: final height maps + nether-style full lighting, one pass per chunk ----
    for(int x = 0; x < WorldSize.xChunks; x ++) {
        for(int z = 0; z < WorldSize.zChunks; z ++) {
            if(progress != null) progress.setLoadingProgress(done++ * 100 / total);
            Chunk chunk = this.chunkCache[WorldSize.coords2hash(x, z)];
            chunk.generateHeightMap();                 // exact, from final blocks
            chunk.generateLandSurfaceHeightMap();
            chunk.initLightingForRealNotJustHeightmap(true);
        }
    }
}
```

Notes:
- `this.populate()` (`ChunkProvider.java:186`) already sets `isTerrainPopulated = true` and `setChunkModified()` — reusing it keeps the guarded re-entrancy (`beingDecorated`) and matches the legacy flag semantics so no double-populate happens later in-session.
- `progress.setLoadingProgress` matches what `Minecraft.preloadWorld` already does with its loading screen (`Minecraft.java:1500`); server uses `outputPercentRemaining()` + `ConvertProgressUpdater` (`MinecraftServer.java:148`).
- NO light init in Phase 1 on purpose: the goal is zero lighting work until the whole world is final.

### 4.5 `ChunkProviderServer` — same orchestration (dedicated server)
`Main/src/minecraft_server/.../server/ChunkProviderServer.java`. On the dedicated server the top-level provider is `ChunkProviderServer` (`WorldServer.getChunkProvider()`, `WorldServer.java:55`), which wraps the raw generator (`this.serverChunkGenerator`, here a `ChunkProviderGenerate`). The modded `ChunkProvider` is *not* in the dedicated-server chain, so it needs its own bulk method writing into its `id2ChunkMap` / `loadedChunksServer`:

```java
/** Dedicated-server twin of the finite-world bulk generation (see ChunkProvider). */
public void generateWholeWorld(IProgressUpdate progress) {
    int total = WorldSize.getTotalChunks();
    int done = 0;

    // Phase 1
    for(int x = 0; x < WorldSize.xChunks; x ++)
        for(int z = 0; z < WorldSize.zChunks; z ++) {
            if(progress != null) progress.setLoadingProgress(done++ * 100 / total);
            int hash = ChunkCoordIntPair.chunkXZ2Int(x, z);
            Chunk chunk = this.serverChunkGenerator.provideChunk(x, z);
            this.id2ChunkMap.put(hash, chunk);
            this.loadedChunksServer.add(chunk);
            chunk.onChunkLoad();                       // NO initLighting
        }
    GlobalVars.didGenerateChunks = true;

    // Phase 2 (populate with deferral)
    this.world.deferLightingUpdate = true;
    try {
        for(int x = 0; x < WorldSize.xChunks; x ++)
            for(int z = 0; z < WorldSize.zChunks; z ++) {
                if(progress != null) progress.setLoadingProgress(done++ * 100 / total);
                this.populate(this, x, z);             // reuse existing populate() (:161)
            }
    } finally {
        this.world.deferLightingUpdate = false;
    }

    // Phase 3
    for(Chunk chunk : this.loadedChunksServer) {
        if(progress != null) progress.setLoadingProgress(done++ * 100 / total);
        chunk.generateHeightMap();
        chunk.generateLandSurfaceHeightMap();
        chunk.initLightingForRealNotJustHeightmap(true);
    }
}
```

`ChunkProviderServer.populate()` (`:161`) already provides the `isTerrainPopulated` guard. Because Phase 1 writes into the **same** map the game loop reads (`provideChunk`, `:115`), no post-generation rebinding is needed.

### 4.6 Hook into new-world preload — dedicated server
`Main/src/minecraft_server/.../server/MinecraftServer.java` `preloadWorld()` (`:139-161`):

```java
private void preloadWorld(WorldServer world, boolean isNew) {
    if(isNew && world.worldProvider.worldType == 0) {          // surface only, for now
        this.outputPercentRemaining("Building terrain", 0);
        world.chunkProviderServer.generateWholeWorld(new ConvertProgressUpdater(this));
        // Proposal line 13: save the whole level to disk the first time.
        this.outputPercentRemaining("Saving level", 0);
        world.saveWorld(true, new ConvertProgressUpdater(this));
    } else {
        int chunksLoaded = 0;
        long prevTime = System.currentTimeMillis();
        for(int x = 0; x < WorldSize.xChunks; x ++)
            for(int z = 0 ; z < WorldSize.zChunks; z ++) {
                ... existing legacy loop unchanged ...
            }
    }

    if(isNew) {
        LevelThemeGlobalSettings.getTheme().specialPostGeneration(world);
        world.worldProvider.getInitialSpawnLocation(world);
    }
}
```

- Guard `worldType == 0` (overworld `WorldProviderSurface`) keeps the nether (`ChunkProviderHell`, worldType 1) and any sky dimension on the legacy path until the new flow is validated. `worldType` is the field checked elsewhere for nether rendering (`WorldProvider.java:245`).
- `saveWorld(true, ...)` → `chunkProvider.saveChunks(z1=true, ...)` writes **all** chunks (the 24-chunk throttle only applies to `quickSaveWorld`/`z1=false`; `ChunkProviderServer.saveChunks` `:173`), so the "save once" step really is one pass.
- `specialPostGeneration` / `getInitialSpawnLocation` run **after** the world exists and is lit — they place spawn structures (e.g. the indev house, `WorldProvider.getInitialSpawnLocation` `:108`) through normal `setBlock` with the flag already reset, and they depend on `getLandSurfaceHeightMap` being final (guaranteed by Phase 3).
- `initWorld()` (`:163`, `levelsAreOk` / re-roll loop at `:178-230`) stays untouched: `minecraftServer` re-rolls by calling `preloadWorld` again when `!levelIsValidUponWorldTheme()`, and each re-roll now bulk-regenerates cleanly.

### 4.7 Hook into new-world preload — client (SSP)
`Main/src/minecraft/.../client/Minecraft.java` `preloadWorld()` (`:1489-1519`) replaces its generation loop with the bulk call for the new-world case:

```java
private void preloadWorld(World world, String caption, boolean isNew) {
    this.loadingScreen.printText(caption);
    this.loadingScreen.displayLoadingString(isNew ? "Building terrain" : "Loading terrain");

    if(isNew) {
        // Proposal: generate everything, then light everything once.
        world.chunkProvider.generateWholeWorld(new LoadingScreenProgressWrapper(this.loadingScreen));
    } else {
        BlockFire.dontSpread = true;
        int ctr = 0;
        for(int x = 0; x < WorldSize.width; x += 16)
            for(int z = 0 ; z < WorldSize.length; z += 16) {
                this.loadingScreen.setLoadingProgress(ctr++ * 100 / WorldSize.getTotalChunks());
                world.getBlockID(x, 64, z);
            }
        BlockFire.dontSpread = false;
    }

    if(GlobalVars.didGenerateChunks) { ... specialPostGeneration as today ... }
    this.loadingScreen.displayLoadingString("Simulating world for a bit");
}
```

- `world.chunkProvider` is `IChunkProvider`; cast to `ChunkProvider` (it *is* the modded `ChunkProvider` on the client, `World.java:302`). Alternatively add `generateWholeWorld` to a small private helper. A tiny `IProgressUpdate` adapter delegating `setLoadingProgress`/`displayLoadingString` to `this.loadingScreen` is trivial (or reuse the existing save-progress screen plumbing).
- The client's per-block `BlockFire.dontSpread` guard isn't needed in bulk mode because `generateWholeWorld` → `provideChunk`/`populate` place everything before the game loop runs; keep it only on the legacy load branch.
- **Save once (client):** `Minecraft.changeWorld()` already does `world.saveWorldIndirectly(this.loadingScreen)` when `world.isNewWorld` (`Minecraft.java:1467-1470`) — that fires right after `preloadWorld`, which is exactly the "first save". No extra save needed on the client. (The server branch in §4.6 needs the explicit call because the dedicated server has no equivalent immediate save.)

### 4.8 Keep the nether path untouched
`ChunkProviderHell.provideChunk()` already writes its own light arrays during terrain gen and runs a nether-style lighting model; it is excluded by the `worldType == 0` guard. No changes to `ChunkProviderHell.java`.

### 4.9 Optional cleanup (later, not required for v1)
- Wrap the two loop pairs in `ChunkProvider`/`ChunkProviderServer` with a small `forChunkRowMajor()` helper to avoid 6 copies of the row-major loop.
- Consider moving the `world.deferLightingUpdate` guard down into `World.setBlock*` as defensive duplication (harmless, cheap) so any future caller bypassing `Chunk.setblockID*` also respects the flag.
- If Starlight artifact testing shows stale skylight at population-change columns, call `chunk.skylightMap = new NibbleArray(chunk.blocks.length)` (or expose `Chunk.clearAllLights()`, `Chunk.java:950`) before Phase 3. Expected but **not** required: `initSkylight` propagates fresh 15-sources top-down over final block states, and `initBlockLight(true)` rescans all emitters, so stale values self-correct; the deep-cave case (pre-population 0 skylight) is unchanged because populate never touches caves.

## 5. Correctness review / gotchas

1. **Populate across borders (greenfield prerequisite).** Current code defers a chunk's populate until its 3 south-east neighbours exist (`ChunkProvider.java:78-85`). The bulk path sidesteps this by guaranteeing *all* chunks exist before Phase 2 — tree placement, `getHeightValue(x0±1,z0±1)` snow pass, layered-sand blur (`ChunkProviderGenerate.java:876-915`) all see neighbours.
2. **`isTerrainPopulated` stays consistent.** Bulk populate sets it true via the existing provider `populate()` wrappers; when the player loads a chunk right after world creation, `prepareChunk` finds it cached+populated and skips re-populate; on later world loads the flag is persisted by `ChunkLoader` as today (no serialisation change).
3. **Determinism client vs. dedicated server.** Both new entry points iterate row-major and reuse the exact same `provideChunk`/`populate` seed logic, so a world generated on the client renders identically on a dedicated server with the same seed+size.
4. **First-loads of a partially-generated region are impossible** — `preloadWorld` completes `generateWholeWorld` before the loop ticks, and `chunkExists()` always returns true, so no chunk is ever requested out of bulk order.
5. **Height-map consumers.** `specialPostGeneration` (themes), `getInitialSpawnLocation` (spawn + indev house) and `getLandSurfaceHeightValue` rely on `landSurfaceHeightMap`; Phase 3 recomputes both height maps from final blocks before any of those run.
6. **Lighting reads during populate.** Only `WorldGenLakes` (`feature/WorldGenLakes.java:109`) consults skylight during populate (used by several biome `populate()`s). It checks `getSavedLightValue(Sky) > 0` at a surface column; the pre-population skylight from `provideChunk`'s `generateSkylightMap()` is already correct there, so lakes still generate. Flagged as the one call-site to watch during testing.
7. **Entity/tile-entity side effects.** Nothing about the flag changes `onBlockAdded`/`onBlockRemoval`/tile-entity creation (chests in dungeons, furnace in indev house); only Starlight calls are skipped.
8. **try/finally everywhere.** The deferral flag MUST be reset even if a theme's populate throws (mid-world re-roll loop in `initWorld` would otherwise keep the flag stuck). Both new methods use try/finally as shown.

## 6. Files to change (both trees unless noted)

| File | Change |
|------|--------|
| `minecraft/` + `minecraft_server/` `world/level/World.java` | add `deferLightingUpdate` field + init |
| `minecraft/` + `minecraft_server/` `world/level/chunk/Chunk.java` | guard `updateLight` at `:312` & `:383`; add `initLightingForRealNotJustHeightmap(boolean)` overload |
| `minecraft/` + `minecraft_server/` `world/level/chunk/ChunkProvider.java` | new `generateWholeWorld(IProgressUpdate)` |
| `minecraft_server/` `server/ChunkProviderServer.java` | new `generateWholeWorld(IProgressUpdate)` |
| `minecraft_server/` `server/MinecraftServer.java` | `preloadWorld` new-world branch + first save |
| `minecraft/` `client/Minecraft.java` | `preloadWorld` new-world branch (client save already in `changeWorld`) |

No changes to: `ChunkProviderGenerate`, `StarlightEngine`, `ChunkProviderHell`, `WorldProvider*`, `LevelTheme*`, `BiomeGenBase` (unless testing shows the `WorldGenLakes` skylight check needs a shim).

## 7. Testing checklist (manual, per AGENTS.md)

1. Client, Small (8x8), overworld theme Forest: new world → confirm progress moves through "Building terrain / populating / lighting / saving" without "Can't keep up!" warnings, then world renders fully lit (torch-lit caves, correct night/day) and trees/snow/layered sand appear.
2. Client Normal (16x16) + Paradise/Sky theme: skylight under canopies and the nether-style blocklight in `forceBlockLightInitLikeNether` biomes.
3. Save once, then exit and reload: world loads from disk with identical terrain and light; `GlobalVars.didGenerateChunks` stays false on reload so no re-generation.
4. Dedicated server: same steps for a fresh `server.properties` world; verify `MinecraftServer` logs "Building terrain / Saving level" and the `saves/` region grows by exactly one full write.
5. Verify re-roll path: a theme that fails `levelIsValidUponWorldTheme()` re-runs `generateWholeWorld` with a new seed and the deferral flag is back to false (no stuck-dark world).
6. Verify nether portal (client): nether dimension still generates snapshot-slow but correctly, and returning to overworld shows no unlit border strips.
7. Playtest: mine/place blocks after load — per-block lighting (Starlight `checkBlockEmittance`) still works, proving the flag defaults off in gameplay.
8. Time comparison (nice-to-have): log ms for `preloadWorld` before vs. after; expect the populate-phase lighting cost (the bulk of startup) to collapse to Phase 3's single pass per chunk.

## 8. Scope / future work

- v1 gates bulk generation to the overworld (`worldType == 0`). If nether bulk generation is later wanted, `ChunkProviderHell` needs the same Phase 3 force-scan and its `provideChunk` must stop pre-writing light arrays; defer.
- `WorldSize` "long" (16x128) and the nether quarter-size special cases (`WorldSize.java:39-56`) are unaffected because the loops use `WorldSize.xChunks/zChunks` directly.
- This keeps "chunked but always-present" storage (proposal line 15) fully intact — nothing about load/save serialisation changes.


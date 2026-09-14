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

### 4.8 Keep the nether path untouched (v1)
The nether is excluded from v1 by the `worldType == 0` guard in `preloadWorld`, so `ChunkProviderHell` keeps running its own chunk-by-chunk flow. No changes to `ChunkProviderHell.java` in v1. (Note: `ChunkProviderHell.provideChunk` does *not* pre-write light arrays — it only fills blocks/metadata; lighting always comes later from the wrapper's `initLightingForRealNotJustHeightmap`. See §9 for the v2 nether plan, which requires no `ChunkProviderHell` change either.)

### 4.9 Optional cleanup (later, not required for v1)
- Wrap the two loop pairs in `ChunkProvider`/`ChunkProviderServer` with a small `forChunkRowMajor()` helper to avoid 6 copies of the row-major loop.
- Consider moving the `world.deferLightingUpdate` guard down into `World.setBlock*` as defensive duplication (harmless, cheap) so any future caller bypassing `Chunk.setblockID*` also respects the flag.
- Once v2 (§9) makes the finite `ChunkProvider` phases load-aware, the "generated vs loaded" decision per chunk could be generalised to an explicit `boolean[] generatedSet` if partial-DIM-1 border-light tuning ever needs it. Today a single `anyGenerated` flag (§9.3) is enough, and the phase-tracking order (Phase 1 decides → Phases 2/3 whole-world gates) keeps the code to a handful of lines.
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

- ~~v1 gates bulk generation to the overworld (`worldType == 0`). If nether bulk generation is later wanted, `ChunkProviderHell` needs the same Phase 3 force-scan and its `provideChunk` must stop pre-writing light arrays; defer.~~ **Done in v2 (§9): the nether is bulk-generated too.** The old caveat turned out to be obsolete: `ChunkProviderHell.provideChunk` never pre-writes light arrays (the `new Chunk(...)` ctor allocates them, but they are only ever filled by the wrapper's `initLightingForRealNotJustHeightmap()`), so **no change to `ChunkProviderHell` was needed** — Phase 3's `initLightingForRealNotJustHeightmap(true)` already performs the nether-style full 0..127 emitter scan, and `hasNoSky` already skips skylight. What v2 adds instead is: (a) a client gate so first nether entry uses the bulk path, (b) a load-aware Phase 1 in the finite `ChunkProvider` so *re*-entry doesn't wipe saved DIM-1 builds, and (c) a server gate + save step for fresh worlds.
- Remaining deferrals:
  - **Sky dimension.** `WorldProviderSky` does not override `worldType`, so it *inherits* `worldType == 0` and would already match the `worldType == 0` gate — but no reachable flow today loads it through `preloadWorld`, so leave it alone. If a sky dimension flow is ever added, apply the same `anyGenerated` load-aware bulk (§9.3).
  - **Load-aware generation on the dedicated server.** Not needed: `ChunkProviderServer.generateWholeWorld` only ever runs for fresh worlds (`isNew`, DIM-1 provably empty), so its regenerate-all Phase 1 is correct and needs no disk probe.
  - **Existing pre-v2 worlds.** A world whose DIM-1 was never populated (player never built it) still takes the legacy chunk-by-chunk path on the dedicated server (`isNew == false`). Acceptable: it matches today behaviour and only the first load pays.
- `WorldSize` "long" (16x128) and the nether quarter-size special cases (`WorldSize.java:39-56`) are unaffected because the loops use `WorldSize.xChunks/zChunks` directly and `ChunkProviderHell` performs the central-quarter `inRange` clamping itself.
- This keeps "chunked but always-present" storage (proposal line 15) fully intact — nothing about load/save serialisation changes, and DIM-1 remains a normal sub-folder of the same save.

## 9. Nether bulk generation (v2 — this plan)

### 9.1 Why the nether is slow today, and what research changed

The nether is created on a completely separate path from the overworld:

- **Client (SSP)**: every portal entry builds a *fresh* nether `World` via the copy ctor `World(World, WorldProvider)` (`World.java:179`), then `usePortal` calls `preloadWorld(world7, "Entering the Nether", false)` (`Minecraft.java:1371`) → always the legacy chunk-by-chunk branch (`isNewWorld == false` for the copy ctor, `World.java:199`), so the first visit generates every chunk with **per-block Starlight during populate**, plus an immediate per-chunk full blocklight scan from the wrapper (`initLightingForRealNotJustHeightmap`) — slower than the overworld ever was.
- **Server (dedicated)**: `initWorld` always passes `isNew == false` to `preloadWorld` for slot 1 (`WorldServerMulti` → copy-ctor semantics, `MinecraftServer.java:217`), so a fresh world's DIM-1 is also generated chunk-by-chunk at startup.

Two research findings make the fix almost free:

1. **The bulk engines are already generator-agnostic.** `ChunkProvider.generateWholeWorld` (`ChunkProvider.java:271`) and `ChunkProviderServer.generateWholeWorld` (`ChunkProviderServer.java:302`) drive everything through `this.chunkGenerator` — Phase 1 calls `chunkGenerator.provideChunk`, Phase 2 routes through the provider `populate()` wrapper → `chunkGenerator.populate` (with the `deferLightingUpdate` flag already set), Phase 3 calls `initLightingForRealNotJustHeightmap(true)`. In the nether the wrapped generator is `ChunkProviderHell`, and each piece works unchanged: `provideChunk` writes no light, `populate` (lava, fire, glowstone, nether ores, biome populate) goes through `World.setBlock*` → the already-guarded `updateLight`, and Phase 3's forced full-range emitter scan is exactly "light like the nether" — for the nether itself it is simply the native behaviour, with `hasNoSky` skipping `initSkylight` for free.
2. **`ChunkProviderHell.provideChunk` never pre-writes light arrays** (`Chunk.java:105-111` allocates `skylightMap`/`blocklightMap`; `ChunkProviderHell.provideChunk` `:206-247` only fills `blocks[]`/`metadata[]`). The old §8 note ("stop pre-writing light arrays") has nothing to remove.

### 9.2 Client (SSP) hooks

1. `Minecraft.usePortal()` — entering-nether branch (`Minecraft.java:1369-1373`):
   ```java
   world7 = new World(this.theWorld, WorldProvider.getProviderForDimension(-1));
   this.preloadWorld(world7, "Entering the Nether", true);   // was: false
   ```
   (`if(world7.isNewWorld) world7.worldProvider.getInitialSpawnLocation(world7);` is dead for the nether and can stay or go.)
2. `Minecraft.preloadWorld()` gate (`Minecraft.java:1502`):
   ```java
   if(isNew && (world.isNewWorld || world.worldProvider.worldType == -1)) {
   ```
   The copy-ctor nether world has `isNewWorld == false`, so the `worldType == -1` disjunct is what lets it through. The overworld path is untouched; the sky dimension inherits `worldType == 0` but has no `preloadWorld` flow today (§8).

Route *every* nether entry through the bulk path (`isNew = true`), not just the first: with the load-aware Phase 1 (§9.3) a *re*-entry that fully loads DIM-1 does zero generation, zero populate and zero lighting — the progress bar will read "Building terrain" on re-entry, a purely cosmetic nuance.

### 9.3 Load-aware Phase 1 in `ChunkProvider` — do NOT regenerate saved DIM-1

The client nether `World`'s `saveHandler` is the *overworld's* (`World.java:209`), and `getChunkLoader` routes `WorldProviderHell` to `DIM-1` (`SaveConverterMcRegion.java:72`). So the nether **persists on disk today**: exit saves DIM-1, re-entry's legacy `prepareChunk` reloads it (that is why re-entry is already fast). If we pointed the bulk path at a regenerate-all Phase 1, every re-entry would wipe player nether builds. Fix: make the finite `ChunkProvider.generateWholeWorld` (both source trees, same change) load-aware:

```java
/** Generates the entire finite world in one shot, loading any chunks already on disk. */
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
            chunk.onChunkLoad();                    // NO initLighting: loaded chunks are final,
        }                                           // generated chunks are lit once in Phase 3
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
            this.world.deferLightingUpdate = false;
        }

        // Relight the WHOLE world once if any chunk was generated, never just the new ones:
        // populated features may have written across borders into loaded neighbours, and a
        // single deterministic pass kills any seam between fresh and saved light.
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
```

Behaviour by case:

| Scenario | Phase 1 | Phase 2 | Phase 3 |
|---|---|---|---|
| Fresh overworld (v1 path, `isNewWorld`) | DIM-0 empty → all generated (`anyGenerated`) | populate, deferred | one full relight — identical to v1 |
| First nether visit | DIM-1 empty → all generated | populate, deferred (fast!) | one full relight |
| Nether re-entry (same/fresh session) | all loaded, `anyGenerated == false` | **skipped** | **skipped** → re-entry ~instant, builds preserved |
| Partial DIM-1 (generated + loaded mix) | some generated | populate only the new ones | one world-wide relight → border-correct |

Notes:
- `loadChunkFromFile` is the private helper already used by `prepareChunk` (`ChunkProvider.java:150`); returning `null` for missing chunks is exactly the "wasGenerated" test.
- `GlobalVars.didGenerateChunks` keeps its monotone semantics (true if any chunk generated), so the client's `preloadWorld` post-generate block (`Minecraft.java:1518`) behaves exactly as it does today: first nether visit runs `specialPostGeneration`, full re-entry (fresh session) does not.
- `generateHeightMap()` sets `isModified = true`, so on a mixed partial world the loaded chunks get re-saved on quit — harmless, they came from the same seed anyway.
- `WorldGenLakes` skylight read does not matter here: the nether populate never spawns lakes, and `hasNoSky` keeps skylight untouched.

### 9.4 Server (dedicated) hooks

1. `MinecraftServer.initWorld()` — propagate the *folder* freshness to the DIM-1 world. Slot 1's `worldMngr.isNewWorld` is always `false` (copy-ctor semantics), but DIM-1 is only ever written by nether saves, so "overworld folder was just created" (slot 0's `isNewWorld`) provably implies "DIM-1 empty". Hoist a shared flag inside the loop body (it is re-computed every re-roll iteration):
   ```java
   boolean newWorld = worldMngr.isNewWorld;
   if(i == 1) newWorld = worldFolderIsNew;          // worldFolderIsNew captured at i == 0
   ...
   this.preloadWorld(worldMngr, newWorld);
   ```
2. `MinecraftServer.preloadWorld()` — three-way split, and **narrow the trailing post-gen block to the overworld** so the indev house / spawn finder (`WorldProvider.getInitialSpawnLocation`, `WorldProvider.java:60-110`) can never run on the nether:
   ```java
   private void preloadWorld(WorldServer world, boolean isNew) {
       if(isNew && world.worldProvider.worldType == 0) {
           // overworld: bulk + first save (v1, unchanged)
           this.outputPercentRemaining("Building terrain", 0);
           world.chunkProviderServer.generateWholeWorld(new ConvertProgressUpdater(this));
           this.outputPercentRemaining("Saving level", 0);
           world.saveWorld(true, new ConvertProgressUpdater(this));
       } else if(isNew && world.worldProvider.worldType == -1) {
           // nether: bulk + first save of DIM-1 (no spawn, no post-gen)
           this.outputPercentRemaining("Building nether", 0);
           world.chunkProviderServer.generateWholeWorld(new ConvertProgressUpdater(this));
           this.outputPercentRemaining("Saving nether", 0);
           world.saveWorld(true, new ConvertProgressUpdater(this));
       } else {
           // legacy chunk-by-chunk (existing worlds, sky if ever preloaded)
           ... existing loop unchanged ...
       }

       if(isNew && world.worldProvider.worldType == 0) {
           LevelThemeGlobalSettings.getTheme().specialPostGeneration(world);
           world.worldProvider.getInitialSpawnLocation(world);
       }
   }
   ```
   `ChunkProviderServer.generateWholeWorld` stays **regenerate-all**: it is only reached with `isNew == true`, i.e. a fresh DIM-1 that is provably empty, so there is nothing to load and no persistence risk.

### 9.5 Sync matrix (§6 additions — both trees unless noted)

| File | Change |
|---|---|
| `minecraft/` + `minecraft_server/` `world/level/chunk/ChunkProvider.java` | Phase 1 becomes load-aware + `anyGenerated` gates on Phases 2/3 (§9.3) |
| `minecraft/` `client/Minecraft.java` | `usePortal` passes `isNew = true` for nether entry (§9.2.1); `preloadWorld` gate adds `worldType == -1` (§9.2.2) |
| `minecraft_server/` `server/MinecraftServer.java` | `initWorld` propagates folder freshness to DIM-1 (§9.4.1); `preloadWorld` nether bulk branch + narrow post-gen to `worldType == 0` (§9.4.2) |

No changes to: `ChunkProviderHell`, `ChunkProviderServer.generateWholeWorld` body, `StarlightEngine`, `World`, `Chunk`, `WorldProvider*` (only the three call sites above move).

### 9.6 Testing additions (append to §7 checklist)

1. Client, Small world: enter the nether → first visit builds fast (bulk progress, lava/fire/glowstone correctly blocklit, skylight dark, red lightmap via `WorldProviderHell.updateLightmap`), void-wall bedrock ring intact.
2. Client: build a marked structure in the nether, exit to overworld, re-enter → structure intact (DIM-1 reloaded, no regeneration, near-instant entry), `Teleporter.setExitLocation` still links the portals.
3. Server, fresh world: startup logs "Building nether / Saving nether"; `saves/<folder>/DIM-1/` exists with one full write; restart reloads from DIM-1 with no regeneration; verify **no indev house / fortress placed** in the nether (post-gen block correctly skipped).
4. Server: degraded case unchanged — load a pre-v2 world (DIM-1 never visited) → legacy chunk-by-chunk nether on first visit, nothing breaks.
5. Client, quit-while-in-nether then reload: DIM-1 persisted (chunks were dirty after bulk populate), nether reloads from disk on return.

## 10. Remaining future work (post-v2)

- Generalise the finite `ChunkProvider` phase tracking to per-chunk `boolean[] generatedSet` if partial-DIM-1 border-light tuning ever needs finer control than the whole-world relight gate (§4.9).
- Make dedicated-server nether gen load-aware if a multi-world scenario ever leaves a partially populated DIM-1 behind (today impossible: server bulk only fires on fresh folders).
- Sky dimension: if a reachable sky `preloadWorld` flow is added, it already matches the `worldType == 0` gate and can reuse the same load-aware bulk unchanged.


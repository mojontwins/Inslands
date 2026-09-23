# Plan: Multi-world (linked DIM worlds) support on the server (§9)

> **Status: implemented.** §1-§10 landed on branch `mp-multi-world`; both source
> trees compile clean with the zulu8 javac. Not yet manually verified in-game
> (see Verification below).

## Design summary

Turn the server's fixed two-world model (`WorldServer[2]` for world 0 + nether `-1`) into a lazy `Map<worldId, WorldServer>`:

- **Startup** (`initWorld`) creates exactly world **0** and the shared nether `DIM-1` (renumbered to id **1**, replacing `-1`), generated/preloaded as today. Nothing else loads at boot.
- **Travel** to any id 2..255 creates the `WorldServer` on the fly from the reserved stub `DIM-<id>/level.dat` (`BlockWorldPortal.placeNewWorldPortal` already writes the stub, world-id incl.). Generation happens **on first entry** (mirror of the SP `Minecraft.travelToDimension` flow, `Minecraft.java:1481`).
- **Idle worlds** (zero players) stop ticking: no `tick()`, no `updateEntities()`, no time/day packets; they stay loaded and get a slow periodic save (~every 60 s), plus a full save on shutdown. Worlds 0/1 always tick (unchanged behaviour, incl. the `allow-nether` gate).
- **Travel drivers**: world-portal block hit (server dig hook) and the reworked nether walk-in (`netherReturnWorldId` → dimension 1 / back).
- **Packet9Respawn (+ Packet1Login) widened to 0..255** so all linked ids cross the wire.

Decisions resolved with the user:
- **Idle policy:** pause-but-keep-loaded (skip tick while empty; slow periodic save).
- **Respawn packet:** widen now (unsigned byte in both packet classes, both trees).

---

## Server tree (`Main/src/minecraft_server`)

### 1. `server/MinecraftServer.java`
- `worldMngr` → `public Map<Integer,WorldServer> worldMngr` (keep `worldMngr.get(0)` reachable; add `public WorldServer mainWorld` alias for the many `worldMngr[0]` usages).
- `entityTracker` → `Map<Integer,EntityTracker> entityTracker`; seed `0` and `1` (nether tracker now dimension **1**, not `-1`) in `startServer()` (lines 112-113).
- `getWorldManager(int)` (:462) → map lookup; for absent world **lazily loads** via new `loadWorld(worldId)` (below).
- New `loadWorld(int worldId)`:
  - Build `SaveOldDir(new File("."), "DIM-"+id, true)` (`SaveOldDir` ctor, as in `BlockWorldPortal.java:104`); base seed via `PortalRegistry.getBaseSeed`; `WorldSettings(deriveSeed(baseSeed, worldId), 0, generateStructures, false, false, layeredSand, DEFAULT)`.
  - `new WorldServerMulti(server, handler, WorldNameGen.getName(seed, worldId), worldId, settings, mainWorld)` — shares world-0 mapStorage.
  - **Critical:** `world.worldProvider.dimensionId = worldId;` (theme-created providers default to 0; `getSaveFolderName`/chunk routing depend on this).
  - Register: `addWorldAccess(new WorldManager(this, world))`, difficulty + mob spawns (reuse :214-215), `configManager.registerPlayerManager(world)`, `entityTracker.put(worldId, new EntityTracker(this, worldId))`, `worldMngr.put(worldId, world)`.
  - **No generation here** — generation + return portal happen in the travel/entry path (mirrors SP: `!worldInfo.isGenerated()`). (Login-resume only ever targets already-generated worlds; documented.)
- `preloadWorld` (:147) unchanged for 0/1. First-entry generation lives in a new `prepareWorldForEntry(WorldServer, int sourceId)` (see §2).
- `doTick` (:355):
  - Loop `worldMngr.values()`:
    - **Before each tick, re-apply that world's statics** so coexisting themes/sizes stay correct: `LevelThemeGlobalSettings.loadThemeById(worldInfo.getThemeId())` + `WorldSize.setSize(worldWidthChunks, worldLengthChunks)`.
    - Ticking worlds: `deathTime % 20 == 0` → `Packet4UpdateTime(w.getWorldTime())` filtered by `w.worldProvider.dimensionId` (was `worldType`); `w.tick()`; day-change → `Packet95UpdateDayOfTheYear(worldInfo.getDayOfTheYear())`.
    - **Idle worlds** (`playerEntities.isEmpty()` and id not 0/1): skip tick/entities/packets; `if(deathTime % 1200 == 0) w.saveWorld(false, null);` (autosave normally lives inside `tick()`, `World.java:2173` — idle worlds need the external cadence).
    - Nether keeps the existing `allow-nether` gate.
  - Entity-tracker loop over `entityTracker.values()`.
- `saveServerWorld` (:257) / `stopServer` (:268): iterate map values, save each once (also fixes the existing double-save quirk).
- `run()` :307 `worldMngr[0].isAllPlayersFullyAsleep()` → `getWorldManager(0)`.
- `initWorld` (:187): builds only ids 0 and 1 (`new WorldServer` for 0, `new WorldServerMulti` for 1, i.e. `i == 1` not `i == -1`), keeps the current 0/1 preload loop, but populates the maps instead of arrays.

### 2. `server/ServerConfigurationManager.java`
- `playerManagerObj` → `Map<Integer,PlayerManager>` seeded `0` and `1` in the ctor; `getPlayerManager(int)` map lookup; new `registerPlayerManager(WorldServer)` for links; `onTick` iterates values.
- `setPlayerManager` (:64) → take `WorldServer` (world 0) for `playerNBTManagerObj`.
- `s_func_28172_a` (:68) now takes the **source id** so the player is removed from the *previous* world's manager: `getPlayerManager(sourceId).removePlayer(p)` + `getPlayerManager(dimension).addPlayer(p)` + `prepareChunk`.
- **Rework `sendPlayerToOtherDimension(EntityPlayerMP)` → `sendPlayerToOtherDimension(EntityPlayerMP p, int destId)`** (the destination-driven core, mirror of SP `travelToDimension`, `Minecraft.java:1481`):
  1. `sourceId = p.dimension`; guard `destId == sourceId` / invalid id → no-op.
  2. Nether scaling: overworld↔nether only (`WorldSize` ÷2/×2 exactly as SP :1499-1516); linked↔linked 1:1.
  3. `netherReturnWorldId`: entering nether (`destId==1`) → `p.netherReturnWorldId = sourceId`; leaving nether (`sourceId==1`) → dest already resolved from the stored id (fallback 0).
  4. Remember exit position: `sourceWorld.worldInfo.setLastPosition(posX,posY,posZ,yaw,pitch)` + `sourceWorld.saveWorld(true, null)`.
  5. `destWorld = mcServer.getWorldManager(destId)`; `firstEntry = !destWorld.getWorldInfo().isGenerated()`.
  6. If `firstEntry`: `destWorld.chunkProviderServer.generateWholeWorld(...)` + `setInitialSpawnLocation` + `placeFirstWorldPortal(destWorld, sourceId)` (stub of the SP block, `Minecraft.java:1592`), set `Generated=true`, save. Else landing:
     - from nether → `(new Teleporter()).setExitLocation(destWorld, p)` (vanilla portal search);
     - world-portal re-entry → `(new Teleporter()).findWorldPortalTo(destWorld, sourceId)` then natural spawn (SP `restoreLinkedWorldPosition`, :1614); §4.2 `LastPosition` restore optional first (validate inside `WorldSize`/not solid; fall through to partner portal).
  7. Untrack from `getEntityTracker(sourceId)`, `sourceWorld.removePlayer(p)`, `p.isDead = false`, set coords, `destWorld.spawnEntityInWorld(p)`, track in `getEntityTracker(destId)`, `p.dimension = destId`, `p.currentWorldId = destId`.
  8. `s_func_28172_a(p, sourceId)`; `teleportTo(...)`; `p.setWorldHandler(destWorld)`.
  9. Send, in order: `Packet9Respawn(destId)`, `Packet93FiniteWorldSettings(destWorld.worldInfo.getThemeId(), WorldSize.getSizeId(xChunks,zChunks))` (new helper), `joinNewPlayerManager(p, destWorld)` (Packet4 time + Packet70Bed weather — already there), `Packet95UpdateDayOfTheYear(worldInfo.getDayOfTheYear())`, `s_func_30008_g(p)`.
- `joinNewPlayerManager` (:542): send the world's own day (from `worldInfo`, not the static).
- `markBlockNeedsUpdate`/`sendPacketToAllPlayersInDimension` unchanged (int filter already fine — callers now pass `dimensionId`).

### 3. `server/NetServerHandler.java`
- **Dig hook** in `handleBlockDig` (:276, right after the `worldServer2` lookup, before status dispatch): if target block is `Block.worldPortal.blockID`, `!worldProvider.isNether`, and held item is a diamond-tier tool (`Item.isDiamondTierTool`, cf. client `Minecraft.java:872`) → **cancel the dig** (return, no `blockClicked`/`blockRemoving`; `disableSpawnProtection` reset) and call `mcServer.configManager.sendPlayerToOtherDimension(player, meta)`.
- `handleRespawnPacket` (:568): dest = `world.worldProvider.canRespawnHere() ? player.dimension : player.netherReturnWorldId` (fallback 0) → `recreatePlayerEntity(player, dest)` (linked worlds keep `canRespawnHere()==true`, so dying in them stays; dying in the nether now lands in the stored return world).

### 4. `server/EntityPlayerMP.java`
- `onUpdateEntity` nether walk-in (:194-212): replace the toggle with `sendPlayerToOtherDimension(this, ...)`: current world `1` → `netherReturnWorldId` (fallback 0); otherwise → `1` (storing the return id first).

### 5. `server/WorldServer.java` + `server/WorldManager.java` + `server/ConsoleCommandHandler.java`
- `WorldServer`: all per-dimension broadcast keys use `worldProvider.dimensionId` instead of `worldProvider.worldType` — `updateWeather`/`badMoonDecide` become per-dimension (`sendPacketToAllPlayersInDimension`), `addWeatherEffect`, `newExplosion`, `playNoteAt`, `setEntityState` (via `getEntityTracker(dimensionId)`).
- `WorldManager` (:22,:26): `getEntityTracker(this.worldServer.worldProvider.dimensionId)`.
- `ConsoleCommandHandler`: replace `worldMngr.length`/`worldMngr[i]`/`worldMngr[0]` with map iteration / `getWorldManager(0)`.

### 6. Per-world day-of-year (needed: 2+ worlds tick simultaneously in one JVM)
- `world/level/WorldInfo.java`: add `dayOfTheYear` field + getter/setter; read it from NBT (:92, keep the `Seasons.dayOfTheYear =` assignment too for the client path); `updateTagCompound` writes **the field** (:204), not the static.
- `world/level/World.java`: capture `this.dayOfTheYear = worldInfo.getDayOfTheYear()` after `loadWorldInfo` (:279); when seeding a fresh day (:311) sync both; `updateDailyTasks` (:2213) does `dayOfTheYear++` then `Seasons.dayOfTheYear = dayOfTheYear; Seasons.updateSeasonCounters();` (keeps the static in step for any reader).
- **Mirror both files in the client tree** for parity (client rendering still uses the static; behaviour unchanged).

### 7. `server/NetLoginHandler.java`
- `doLogin` (:96): `Packet1Login` carries `worldServer.worldProvider.dimensionId` (not `worldType`); `Packet93`/`Packet95`/`Packet4` use the resumed world's theme/size/day (reconnect lands in the last world, matching SP resume, via lazy `getWorldManager(dimension)`).

---

## Client tree (`Main/src/minecraft`)

### 8. `network/packet/Packet9Respawn.java` + `network/packet/Packet1Login.java` (also server tree)
- Widen `dimension` to `public int`; `readPacketData` uses `readUnsignedByte()`, `writePacketData` uses `writeByte(this.dimension)`. Applied to **Packet9Respawn in both trees** and **Packet1Login in both trees** (ids 2..255 round-trip; big-value ids no longer wrap to negative → client `WorldProvider.getProviderForDimension` gets the right id).

### 9. `client/Minecraft.java` — `clickMouse` portal branch (:870-880)
- SP unchanged (`travelToDimension`).
- **MP (`isRemote`):** send `Packet14BlockDig(0, x, y, z, face, itemStack, xWithinFace, yWithinFace, zWithinFace)` via the send queue instead of `travelToDimension` (no-op remotely); still `override = true; leftClickCounter = 20;`. The server dig hook then routes travel. The client never locally breaks the block; the server's respawn/refresh keeps it in sync.

### 10. Client parity for §6 (World.java / WorldInfo.java) as noted.

---

## Files touched (both trees where marked)

| Tree | File |
|---|---|
| Server | `server/MinecraftServer.java`, `server/ServerConfigurationManager.java`, `server/NetServerHandler.java`, `server/EntityPlayerMP.java`, `server/WorldServer.java`, `server/WorldManager.java`, `server/ConsoleCommandHandler.java`, `server/NetLoginHandler.java` |
| Both | `world/level/WorldInfo.java` (per-world day), `world/level/World.java` (day capture/increment), `network/packet/Packet9Respawn.java`, `network/packet/Packet1Login.java` |
| Client | `client/Minecraft.java` (clickMouse MP branch) |

Untouched: `BlockWorldPortal`/`ItemWorldPortal`/`PortalRegistry`/`Teleporter`/`WorldProvider` (already M5-parity), `PlayerSaveData`, theme providers, world-gen.

---

## Verification (manual — no test harness)

1. Export server JAR (Eclipse → Executable JAR) + client JAR; run server; confirm log shows only DIM 0 and nether `DIM-1` building; a new save has no `DIM-2...` folders.
2. Join with client A in world 0; craft `ItemWorldPortal` (8 obsidian + wool), place it (stub `DIM-2/level.dat` appears), hit with diamond pick → enter world 2; return portal inside the spawn house sends you back; server log shows "** DIM 2" created lazily only at first entry.
3. A in world 2 while a second client B stays in world 0: confirm world 2's time day packets reach only A and world 0's only B.
4. Both players leave world 2 → its `tick()`/`updateEntities()` stop (watch CPU / console), a `Packet4UpdateTime` stops for it; auto-save still fires; re-enter → instant.
5. Test nether from a linked world: portal → enter → `DIM-1` → return lands in the same linked world (not 0). Die in the nether → respawn in the stored return world; die in a linked world → stay there.
6. Disconnect client while inside `DIM-5`, restart server, reconnect → resumes in `DIM-5` (player.dat `CurrentWorldId`-driven lazy load).
7. Stress ids 128..255: craft/place 130 portals, travel to id 130 — respawn packet must not wrap (client `getProviderForDimension(130)` → Surface, correct theme via Packet 93).
8. `save-all` / stop: every registered world (incl. idle ones) saved once.

## Known limitations (documented, match §5.12 scope)
- `LevelThemeGlobalSettings`/`WorldSize`/`Seasons` statics are re-applied **per world before each tick**, so per-world terrain/theme/size stay correct while active; anything outside a world's tick (e.g. a cross-world packet path) sees the last-awaited world's theme. Rendering is client-side and driven by per-entry Packet93/95, so unaffected.
- `GlobalVars.noiseOffsetX/Z` stay global (per §5.12 "out of scope").
- The nether remains a single save-wide hub (id 1), consistent with §3.1.
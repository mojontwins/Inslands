# Player data separation from level.dat — design & implementation plan

## Goal

`level.dat` (in every save folder: the root, `DIM-1`, `DIM-N`) should carry **world metadata
only** — seed, time, weather, spawn, theme, world id, dimensions, remembered per-world exit
position. Player state (inventory, stats, XP, health, exact position/rotation, `CurrentWorldId`)
moves to a **single** `player.dat` living in the save root folder.

This removes the cross-world coupling that produced the old `syncCanonicalPlayerToBaseWorld`
hack (a raw `level.dat` rewrite done to dodge a second `SaveHandler`'s `session.lock`) and lets
the client boot **directly** into the world the player was last in, without first loading the
base world just to find out which world that was.

## Target layout

```
<save root>/
  level.dat         world 0 metadata only
  player.dat        THE single player state (Pos, Rotation, inventory, hp, xp, CurrentWorldId, Dimension)
  DIM-1/level.dat   nether metadata only
  DIM-2/level.dat   linked world metadata only
```

Player data is never embedded in any `level.dat` again. `CurrentWorldId` in `player.dat` is what
decides which world the game boots into.

## Design decisions (locked)

1. **Greenfield fork.** Compatibility with vanilla saves or old builds of this mod is not a
   requirement. No migration/legacy fallback code is written; the pre-MCRegion old-format
   conversion path is deleted.
2. **New class `PlayerSaveData`** (mirrored in both source trees) owns all `player.dat` I/O.
3. **`WoolPortalRegistry` is renamed `PortalRegistry`** and stays scoped to portal linking,
   world-id allocation and base-directory/seed helpers.
4. **Shared `World.java` must stay byte-identical in both trees.** All client-side routing
   lives in `Minecraft.java`. The dedicated-server multiplayer path is deferred to a later
   stage; its existing `players/<username>.dat` handling is untouched.

## Implementation steps

### 1. Rename `WoolPortalRegistry` → `PortalRegistry`
Both trees: rename the file/class. Update references in `BlockWorldPortal` (both trees) and
client `Minecraft.java`.

### 2. New `PlayerSaveData` (both trees, `net.minecraft.world.level`)
- `static void startSession()` — clear the one-shot "consumed" flag.
- `static void markConsumed()` — suppress hydration for the rest of the session (used when
  `player.dat` references a world that no longer exists).
- `static File getFile(File)` / `getFile(World)` — `player.dat` under the base directory.
- `static NBTTagCompound readTag(File)` — raw read (no session flag).
- `static NBTTagCompound read(World)` — consuming read used by `World.spawnPlayerWithLoadedChunks`.
- `static void write(World, EntityPlayer)` — `player.writeToNBT(fresh)` (already carries live
  Pos/Rotation/Inventory/`CurrentWorldId`/`Dimension`), written atomically via
  `player.dat_new → player.dat_old → player.dat` rotation. Never touches `session.lock`.

### 3. `World.java` (both trees)
- `saveLevel()`: `saveHandler.saveWorldInfo(this.worldInfo)` (was `saveWorldInfoAndPlayer`);
  sync block becomes `PlayerSaveData.write(this, player)` guarded by `player.worldObj == this`
  (the old `worldId != 0 || dimensionId == 1` carve-out is gone — world 0 must also write
  `player.dat` now).
- `spawnPlayerWithLoadedChunks()`: hydrate from `PlayerSaveData.read(this)` instead of the
  `WorldInfo` player tag.
- Delete `syncCanonicalPlayerToBaseWorld`, `newDoubleNBTList`, `newFloatNBTList`.

### 4. `WorldInfo.java` (both trees)
- Remove the `playerTag` field, the `hasKey("Player")` constructor branches,
  `getPlayerNBTTagCompound`/`setPlayerNBTTagCompound`, `getNBTTagCompoundWithPlayer`, the
  copy-constructor `playerTag` line, and the `Player` `setCompoundTag` in `updateTagCompound`.

### 5. Save-handler cleanup (both trees)
- `ISaveHandler`: remove `saveWorldInfoAndPlayer`.
- `SaveHandler`: remove `saveWorldInfoAndPlayer` + now-unused `java.util.List` import.
- `SaveOldDir`: drop the `saveWorldInfoAndPlayer` override; add a `saveWorldInfo` override that
  sets `saveVersion(19132)` first (keeps the world-list "is McRegion" flag correct).
- `SaveHandlerMP` (client) and `PlayerNBTManager` (server stub): remove the method.

### 6. Strip old-format conversion (both trees)
- `ISaveFormat`: remove `isOldMapFormat` + `converMapToMCRegion`.
- Delete `SaveFormatOld`; fold its still-needed helpers (`savesDirectory`, constructor,
  `getWorldInfo`, `renameWorld`, `deleteWorldDirectory`, `deleteRecursively`) into
  `SaveConverterMcRegion`, which now `implements ISaveFormat` directly.
- Client `Minecraft.startWorld`: drop the `isOldMapFormat` branch and the private
  `converMapToMCRegion` method.
- Server `MinecraftServer.initWorld`: drop the conversion branch. `ConvertProgressUpdater` is
  KEPT — it is still used for `generateWholeWorld`/`saveWorld` progress display.

### 7. Client `Minecraft.java` — direct boot into the target world
`startWorld` now reads `player.dat` before constructing any world:

1. `changeWorld(null)`, biome lookup, `GlobalVars.initializeGameFlags()`.
2. `PlayerSaveData.startSession()`.
3. `ISaveHandler baseHandler = saveLoader.getSaveLoader(folder, false)`;
   `baseDir = baseHandler.getSaveDirectory()`.
4. `resumePlayer = PlayerSaveData.readTag(baseDir)`; `resumeId = resumePlayer.getInteger("CurrentWorldId")`.
5. Route to the target world only:
   - `resumeId >= 2` and `baseDir/DIM-<id>/level.dat` exists → load that world directly
     (`new World(handler, ...)`, `setDimension(resumeId)`); mirror the `brandNew`/preload logic
     from `travelToDimension`.
   - `resumeId == 1` → `new World(baseHandler, name, settings, WorldProvider.createNetherProvider())`
     (nether shares the base `level.dat`; chunks live in `base/DIM-1`). This also fixes the old
     behaviour where nether resume silently fell back to world 0.
   - otherwise / orphaned `player.dat` → fall back to world 0 and call `PlayerSaveData.markConsumed()`.
6. `preloadWorld`, then `changeWorld(world, caption)` — `spawnPlayerWithLoadedChunks` consumes
   `player.dat` and hydrates (position is already in the target world's frame, no nether scaling).
7. `settlePlayerOnGround(...)`.

Delete `resumeLastWorld`, `PlayerSnapshot`, `readCanonicalPlayerSnapshot`. Keep
`settlePlayerOnGround`, `travelToDimension`/`usePortal`/`restoreLinkedWorldPosition` (in-session
portal travel is unchanged).

## Pros

- `level.dat` becomes pure world metadata; each `DIM-x` folder is self-contained and stable.
- Single source of truth for SP player state; crash-safe atomic `player.dat` writes on the
  autosave cadence already used by `World.tick`.
- Direct world boot: no wasted load/generation of the base world, no boot-time save of a world
  the player never entered, no startup coordinate scaling.
- `World.java` stays identical across client/server trees with no client import.
- SP finally mirrors what the dedicated server already does (players stored apart from `level.dat`).

## Cons

- New non-vanilla file type (`player.dat`) — backup tooling must include it (same exposure as
  `portals.dat`).
- The "consume once per session" semantics must be preserved (respawn must not re-hydrate from a
  stale snapshot) — handled by the `PlayerSaveData` session flag.
- Only one `player.dat` — per-world player state is not representable; per-DIM data must live in
  `WorldInfo` (the existing `LastPosition` fields cover current needs).
- The dedicated-server player path (future stage) will need to reconcile `players/<username>.dat`
  with the root `player.dat` model.

## Verification (manual)

1. Build the client JAR in Eclipse.
2. Create a fresh world, travel into a linked world and the nether, quit and relaunch — resume in
   the last world with exact pos/rot.
3. Kill the process mid-world; reload — no loss of inventory/position.
4. Inspect save files: no `level.dat` carries a `Player` tag; `player.dat` holds the live state.
5. Death & respawn — no re-hydration from a stale snapshot.
6. World-list GUI still works (version flag preserved via `SaveOldDir`).
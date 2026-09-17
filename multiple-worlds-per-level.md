# Multiple worlds per level (linked DIM worlds) — Design draft

> **Status:** DRAFT — design settled (see §3-§5); no open decisions remain.
> Single-player first; multiplayer later.
> Mirror every change in **both** source trees (`Main/src/minecraft` and
> `Main/src/minecraft_server`) per AGENTS.md.

## 1. Goal

Inslands worlds are small and finite. Add the ability to link *several worlds
together* inside a single save, travelled between via **portal blocks**, the way
normal/dimension/hub rooms in Aether/Twilight work — but with the number of
linked worlds **decoupled from the number of level themes**.

- Worlds share **the same player data** (inventory, XP, health, stats) but each
  has **its own region files and its own `WorldInfo`** (level.dat), stored in
  vanilla-style `DIM-<n>` subfolders of the save folder (`DIM-2`, `DIM-3`, ...).
- Generation is **lazy**: placing a portal *reserves* its destination world
  (id + stub `level.dat`), and the terrain is actually generated the first time
  you enter it.
- There is always a **way back** (auto-created return portal + respawn point).
- The **vanilla nether portal is kept** and becomes a save-wide bridge: one
  shared nether (`DIM-1`) that every overworld can visit, and returning through
  it brings you back to **the exact world you left** (see §3.8).
- **World size is per linked world**: each portal-created world picks its own
  size at random (not bound to the main world's size).

## 2. Non-goals (for now)

- Multiplayer (design kept in mind, see §9, not implemented in MVP).
- 3D/volume portal linking or nether-style coordinates scaling (divide-by-8).
- Overworld ↔ linked-world entity travel for mobs (players only, like SP nether).
- The beta **Sky dimension** (a reachable dimension via provider id) is removed;
  the Sky *terrain generator* stays for the Paradise theme (`WorldType.SKY`),
  but never as a dimension you can travel to.

## 3. Core concepts

### 3.1 World id (metadata = destination)

- Every world in a save has an id 0..255.
  - **0 = the main world** (save root), "overworld". Portal blocks with
    metadata 0 link back to it. It stays at the save root (no `DIM-0` folder),
    so existing saves are untouched.
  - **1 = the nether** (folder `DIM-1`). Shared by the whole save: one nether
    regardless of how many overworlds exist. **Not** a linkable world — world
    (dimension) portals can't be built in or fired from the nether (§3.3/§3.4).
    Replaces the old beta dimension id **-1** (migration in §5.2).
  - **2..255 = linked worlds** (folders `DIM-2` .. `DIM-255`), all of which are
    "overworld". A nether portal opened from any of them goes to the same
    nether and comes back to the world you left (§3.8).
  - **The beta Sky dimension is removed.** Nothing maps to a Sky *dimension*
    anymore; `WorldType.SKY` survives only as a *terrain generator* used by the
    Paradise theme, and no longer forces `WorldInfo.dimension = 1`.
- **The portal block's 8-bit metadata IS the destination world id.** Confirmed
  the mod already stores full-byte metadata per block (`Chunk.data[]` is `byte[]`,
  `Chunk.java:36,298`), so up to 256 destinations, 0 = initial world.

### 3.2 A world identity record

Each linked world has its own `level.dat` with a `WorldInfo` that stores:

| Field | Source | Notes |
|---|---|---|
| `Dimension`/`WorldId` (root tag) | **new** | explicit world id (0=main, 1=nether, 2..255=overworld), decoupled from the embedded player tag |
| `RandomSeed` | existing | **per-world seed**, derived at reserve time from `baseSeed` (world 0's seed) + the world number via the invertible mixer `deriveSeed` (§5.13) — distinct for every world number, so two worlds of the same theme differ |
| `LevelName` | existing | friendly name from `WorldNameGen.getName(baseSeed, worldId)` (§5.13), deterministic at reserve and again at first travel |
| `generatorName` | existing (`WorldType`) | the **terrain type Y** of the link (any theme's terrain type, including Sky-style for Paradise) |
| `ThemeId` | existing | the **theme X** of the link (0..6) |
| `WidthInChunks`/`LengthInChunks` | existing | **random per world** (size id 0..4 via `WorldSize.setSizeById`), frozen in this world's level.dat at reserve time |
| `LastPositionX/Y/Z`, `LastPositionYaw/Pitch` | **new** | that world's remembered exit position/rotation; the landing spot on re-entry (§4.2, §5.11) |
| `Player` tag | existing | per-world player position + shared inventory snapshot |

`WorldInfo(nbt)` already calls `LevelThemeGlobalSettings.loadThemeById(...)` and
`WorldSize.setSize(...)` on load (`WorldInfo.java:93-107`), so switching worlds
re-applies theme + size "for free".

### 3.3 Portal block — `BlockWorldPortal` (id 169, tile 145)

- New block **`BlockWorldPortal`**, id **169** (first free id, `Block.java:248`).
  It is a **single full block**, placed with the `ItemWorldPortal` item (§3.4) —
  **no obsidian frame, no structure**.
- Its **8-bit metadata IS the destination world id** (0 .. 255, see §3.1). Only
  valid ids are ever written (0, 2..255); the nether (1) is never a target.
- **Travel = hit the block with a diamond tool or weapon** (left-click, hitting
  the block, not breaking it — see §4). No walk-in / `setInPortal` behaviour.
- **Rendering**: a **placed/active** portal shows tile **145** (`9*16+1`, confirmed
  free — planks2 owns 167, leafPile owns block id 168) as a **frame with
  transparent inner pixels**, with a 12×12×12 **glass core** inside it (glass
  tile 49, `Block.java:86`) visible through the frame, and **emits light**
  (`setLightValue`, like the vanilla portal). Portal blocks are plain metadata
  blocks — **no TileEntity** (the world id is the metadata; there is nothing else
  to store).
- **Does not function in the nether.** `onItemUse` refuses placement when
  `worldProvider.isNether`; the hit handler no-ops inside the nether. A portal
  block somehow present in `DIM-1` is inert.
- **Hardness ≥ obsidian, explosion-proof** (high `setResistance`): it cannot be
  broken by accident or by the *respawning* nether behaviour; destroying one is
  always deliberate (mined like obsidian). **Breaking it yields nothing and the
  portal block can't be reused** — no drops, no item pickup, the gate is gone
  for good. Breaking a portal destroys / frees its destination world (reference
  counting, §3.7).
- A portal with metadata **0 links back to world 0**.

### 3.4 The placement item — `ItemWorldPortal`

- New item **`ItemWorldPortal extends ItemBlock`**, registered as the block item
  for block id 169 in `Block.java`'s static initializer (same pattern as the
  other custom block items, `Block.java:904-912`:
  `new ItemWorldPortal(worldPortal.blockID - 256)`). Sub-types item with
  **damage = the wool colour** (`setHasSubtypes(true)`, `getSubItems` lists the
  16 variants; `ItemCloth.java:6-21` is the plumbing reference).
- **The unplaced item renders as the portal block** (Option A): because
  `BlockWorldPortal` uses a custom render type registered in
  `RenderBlocks.renderItemIn3d` (`RenderBlocks.java:4950-4963`), the item is
  drawn as the **3D block** everywhere (GUI, hotbar, dropped items; `itemID <
  256` → `drawItemIntoGui` uses the block render, `RenderItem.java:134`): frame
  tile **145** + a **wool core tinted by the item damage** (0-15), drawn by the
  `renderBlockOnInventory` branch (§5.6). So what you craft/hold shows tile 145
  — the portal frame — with the crafting wool colour inside.
- **Crafting recipe**: 8 obsidian surrounding a **wool block** (center) → yields
  `ItemWorldPortal` with **item damage = the wool's damage (0..15)**.
- **Placement** (`onItemUse`, overriding `ItemBlock`): right-click on a block face:
  1. No-op if `worldProvider.isNether` (portals can't be placed in the nether).
  2. **Allocate** the destination world id (sequential, §3.6) and write it as the
     placed block's metadata.
  3. Place the `BlockWorldPortal`.
  4. **Reserve the new world**: write the stub `level.dat` into `DIM-<id>/`
     (§3.5). The block and its destination are then permanent on disk.
  The item's damage is **read at placement to decide the new world's theme and
  then lost** — the placed block keeps only the world id in its metadata.
  Theme ← wool damage; terrain type and world size ← random (unless the theme
  forces one); seed ← `deriveSeed(baseSeed, worldId)` and name ←
  `WorldNameGen.getName(baseSeed, worldId)` (§5.13). All are frozen in the stub
  `level.dat` and are not changeable later.

### 3.5 Lazy generation (reserve now, generate on first travel)

- "Reserve" = the placement step in §3.4: write the stub `level.dat` in
  `DIM-<id>/` only — cheap, no terrain.
- "Generate" happens on **first entry**: load/create the `World`, run
  `preloadWorld(..., isNew=true)` (`→ generateWholeWorld` bulk pass), then
  `getInitialSpawnLocation`, then teleport the player in, then save.
- A world whose stub level.dat exists but has **no chunks** is detected by
  `world.isNewWorld` / a `GenerationFlag` tag and only built on arrival.

### 3.6 Id allocation — sequential, with gap reuse

- Starts at id **2**; ids are handed out in increasing order. On reaching
  **256**, wrap back to **2** and scan for **gaps** (ids whose `DIM-<id>` folder
  does not exist — destroyed worlds, §3.7).
- If there is **no gap at all** (all 254 slots 2..255 alive): the portal does
  NOT create a new world — it links to a **random existing world** id instead.
- Allocation state lives in a save-root registry (see §5.8).

### 3.7 Reference counting — destroying a world frees its id

- Every portal block holds a **reference on its destination world**: the placed
  portal (§3.4) references the new/existing id it carries; the auto return
  portal (§4) references the world you came from.
- The registry tracks `refCount` per world id. **Breaking a portal decrements
  the refCount of its destination**; when it reaches **0** the destination world
  is destroyed: its `DIM-<id>` folder (chunks + level.dat) is deleted and its id
  is freed for reuse (§3.6).
- Because the return portal keeps your **source** world alive, a two-gate loop
  (world A portal → B, plus B's return portal → A) survives until you physically
  break a gate. Breaking the *only* gate to a world deletes that world, cascading
  to any gates it contains (a deleted world's gates vanish with it — the registry
  reconciles counts when a folder is removed).

### 3.8 The nether portal and "return to the world I left"

The vanilla nether portal (`Block.portal`, obsidian frame + flint & steel) is
kept and becomes the save-wide bridge *between the nether and all overworlds*:

- Firing a nether portal from any overworld (0 or 2..255): **the current world
  id is stored in the player NBT** (`NetherReturnWorldId`, part of the canonical
  player snapshot, §5.11), then travel to the nether (`DIM-1`, dimension id 1).
- Coming back through the nether portal: travel to the **stored** world id. If
  the stored id is missing or its folder is gone, fall back to **0**.
- Nether-coordinate scaling stays as-is (mod's `WorldSize`-based ÷2 when
  entering, ×2 when leaving); this applies only between an overworld and the
  nether — linked-world ↔ linked-world world-portal travel maps 1:1.
- World (dimension) portals never function inside the nether (§3.3/§3.4).

## 4. Portals: the whole travel loop

Two distinct portal systems coexist:

1. **World portals** (`BlockWorldPortal`) — travel between linked overworlds
   (§3.3/§3.4). Disabled in the nether.
2. **Nether portals** (`Block.portal`) — travel between any overworld and the
   shared nether, always returning to the exact overworld you left (§3.8).

### World-portal travel

1. Player **hits** the portal block with a **diamond tool or weapon**
   (left-click attack on the block). This does **not** register a real dig — the
   click handler intercepts and triggers travel instead.
2. Hook the block-attack path: in SP the left-click reaches
   `playerController.clickBlock` (`Minecraft.java:853`); in MP the dig reaches
   the server's `NetServerHandler`. At the hook point: if the targeted block is a
   `BlockWorldPortal` and the player's held item is a **diamond-tier tool or
   weapon** (material `DIAMOND`: sword/pickaxe/axe/spade/hoe),
   - cancel the dig / don't damage the block,
   - if **not** in the nether → `mc.travelToDimension(blockMetadata)`;
   - if in the nether → portal is inert, nothing happens.
3. `Minecraft.travelToDimension(int destId)` (generalises `usePortal()`,
   `Minecraft.java:1335`):
   - Save current world (`saveWorldIndirectly`).
   - `ISaveHandler` for the target: `this.saveLoader.getSaveLoader(folderName +
     "/DIM-<id>", false)` (`SaveConverterMcRegion.getSaveLoader`, → `SaveOldDir`).
   - `World` via ctor C `new World(saveHandler, name, settings, provider)` —
     provider from `WorldProvider.getProviderForDimension(destId)`; `name` from
     `WorldNameGen.getName(baseSeed, worldId)` (§5.13).
   - If brand new / never generated → set theme/terrain/size from the stub, bulk
     `generateWholeWorld`, find spawn, save.
   - `changeWorld(newWorld, caption, player)` (`Minecraft.java:1416`), then place
     the player per the **landing rule §4.2** (remembered exit position → partner
     portal → natural spawn); the Teleporter's `findExitLocation` only provides
     the fallback.
   - **On first entry**: auto-place a **return portal** (§4.1) that links back to
     the world you came from.
4. Death: `respawn` keeps you in the current world — linked worlds keep
   `canRespawnHere() == true` (unlike the nether), so dying does **not** kick
   you out of a linked world. Nether still kicks back (via stored return id).

#### 4.1 Return portal

- Auto-placed on **first entry** into the new world, **inside the spawning
  house**, at the **spawn level** (the floor tile where the player is supposed
  to stand): a `BlockWorldPortal` with metadata = the id of the world you came
  from (plain block, no TileEntity).
- It must be **not covered**: any block directly above that new portal spot is
  cleared so the player can stand on it (and hit it) freely.
- The player **spawns ON the portal**: the landing position and the stored
  per-world spawn point are set to the tile just above the portal.
- It is a perfectly normal world portal: active the same way (diamond tool hit),
  holds a reference on the world it points to (§3.7), and breaking it yields
  nothing (like every portal block, §3.3).

#### 4.2 Where the player lands (per-world exit position)

Travelling **forwards** into a brand-new world is easy (its natural spawn is the
landing spot), but **going back** to an already-generated world needs an explicit
rule. The fix is to **remember, per world, where the player was when they left
it** — stored in that world's own `level.dat` (`WorldInfo`), so no separate file
is needed. On every `travelToDimension(destId)` landing (§5.10):

1. **First entry (stub, never generated).** Generate the world; use the theme's
   natural initial spawn (the spawning house); place the return portal there
   (§4.1) and land the player **on it**. This spot is then that world's initial
   remembered position.
2. **Existing world with a remembered position** (the normal "going backwards"
   case). Restore `LastPositionX/Y/Z` (+ `LastPositionYaw/Pitch`): the player
   reappears exactly where they stood when they last left that world. This is
   deliberately **not** partner-portal matching — it sidesteps the ambiguity of
   several portals pointing at the same world id, and "resuming where I left"
   is what the player expects.
3. **No remembered position, or it is no longer valid.** Fall back to the
   **partner portal** — the `BlockWorldPortal` in the destination whose metadata
   == the source world id (there is one in the spawning house after §4.1) — and
   otherwise to the world's natural spawn point.

Recording and safety:
- **On leaving world A** (any world-portal or nether trip out, i.e. before
  `changeWorld`): write A's current player position *and* rotation into A's
  `level.dat` root as `LastPositionX/Y/Z` + `LastPositionYaw/Pitch`. Also refresh
  it on ordinary world saves so a crash doesn't lose it.
- Before restoring, **validate** the spot (reuse the vanilla `Teleporter` safety
  search): inside `WorldSize` bounds, not inside a solid block, not over void/
  lava — else fall through to case 3.
- The recorded spot is wherever the player stood within tool reach of the portal
  (hitting a portal doesn't move the player), which is the portal area and is
  fine.
- Scoped to **world-portal** travel; the nether keeps its vanilla scaled
  partner-portal behaviour (§4, "Nether-portal travel").

### Nether-portal travel (vanilla path, reworked)

The existing `Minecraft.usePortal()` / `sendPlayerToOtherDimension` path is
retargeted:

- **Entering the nether** (any overworld → nether): store current world id in
  `player.netherReturnWorldId`, then travel to dimension 1 (`DIM-1`). ÷2
  coordinate scaling applies.
- **Leaving the nether** (nether → overworld): read `netherReturnWorldId`, travel
  to that dimension (fall back to 0 if invalid). ×2 coordinate scaling applies.
- `Packet9Respawn` carries the destination id; server and client re-apply the
  stored return world instead of toggling between -1 and 0.

## 5. Concrete code changes (per file, both trees)

### 5.1 `WorldProvider.java` (client + server)

- Add `public int dimensionId = 0;` and `public String getSaveFolderName()`:
  `null` for 0 (save root), `"DIM-1"` for 1 (nether), `"DIM-<id>"` for `>= 2`.
- Rewrite the registry `getProviderForDimension` (`WorldProvider.java:209-211`),
  dropping the Sky entry and moving the nether to id 1:
  ```java
  i0 == 0 ? new WorldProviderSurface()
  : i0 == 1 ? new WorldProviderHell()
  : /* id >= 2 → */ new WorldProviderSurface()  // set dimensionId = i0
  ```
  A plain Surface provider is enough for linked worlds: themes drive
  terrain/biome/lighting via `LevelThemeGlobalSettings` + the world's
  `WorldInfo.terrainType`.
- **Delete the Sky dimension plumbing:** `getProviderForDimension(1)` no longer
  returns `WorldProviderSky`; `WorldProviderSky.java` can be removed (client +
  server). Its `ChunkProviderSky` terrain generator is still needed by the
  Paradise theme and stays (reached via `WorldType.SKY`, never via a dimension).
- `getChunkProvider()` already delegates to
  `worldInfo.getTerrainType().getChunkGenerator(worldObj)` — nothing to change.

### 5.2 `WorldInfo.java` (client + server)

- Read a root `"WorldId"` int in the nbt ctor (`WorldInfo.java:42`); fall back to
  the embedded player tag's `Dimension` (`:88-91`) for old saves.
- Persist it in `updateTagCompound` (`:170`) next to `ThemeId`.
- Add `setDimension(int)`/`getDimension()` accessors (getter exists at `:232`).
- Add `getWorldId()` (same value) for clarity.
- Ctor B (`WorldInfo(WorldSettings, String)`, `:113`): add `setDimension(id)` for
  stub creation. **Remove** the `terrainType == WorldType.SKY → dimension = 1`
  special case (`:120`): the Paradise main world is an overworld (`0`) again.

**Migration for existing saves:**
- Legacy nether (dimension `-1`, folder `DIM-1`): treat as `1` on load and
  rewrite on save. The folder is already `DIM-1`, so nothing moves on disk.
- Legacy Paradise/sky (dimension `1` **with** `generatorName == "sky"`): treat
  as overworld `0`. Distinguish by checking `generatorName` so a real nether is
  not misread.
- The Sky *dimension* is gone: `WorldProviderSky` is removed; its terrain
  generator (`ChunkProviderSky`) stays reachable only via `WorldType.SKY`.

### 5.3 `SaveHandler.java` + `SaveOldDir.java` (client + server) — DIM routing

Replace the `instanceof WorldProviderHell` checks (`SaveHandler.java:83-90`,
`SaveOldDir.java:17-26`) with a generic folder lookup from
`worldProvider.getSaveFolderName()`:

```java
String folder = worldProvider.getSaveFolderName();
File dir = folder == null ? this.saveDirectory : new File(this.saveDirectory, folder);
dir.mkdirs();
return new ChunkLoader(dir, true);           // SaveHandler
return new McRegionChunkLoader(dir);         // SaveOldDir
```

Nether (`DIM-1`) keeps behaving identically. No change to `ISaveHandler`,
`ChunkLoader`, or `RegionFileCache`.

(Access the DIM world's `level.dat` by pointing `getSaveLoader` at
`folderName + "/DIM-<id>"` — `SaveOldDir` already writes `level.dat` into its
own `saveDirectory`, so `level.dat`, `session.lock`, and chunk files all land
inside `DIM-<id>/`.)

### 5.4 `World.java` ctor C (`:246`) — provider resolution

Replace the else-chain (`World.java:295-301`) with the new numbering:

```java
else if(this.worldInfo != null && this.worldInfo.getDimension() == 1) {
    this.worldProvider = WorldProvider.getProviderForDimension(1);   // nether
} else if(this.worldInfo != null && this.worldInfo.getDimension() >= 2) {
    this.worldProvider = WorldProvider.getProviderForDimension(this.worldInfo.getDimension());
} /* dimension 0 (and legacy -1 / legacy-sky) → GetOverworld provider */
```

The old `dimension == -1`, `dimension == 1`, and `terrainType == WorldType.SKY`
branches are collapsed into this. Legacy `-1` arrives already migrated to `1`
(§5.2); `terrainType == SKY` alone no longer picks any provider.

### 5.5 `Teleporter.java` (client + server)

- `findExitLocation` (`Teleporter.java:22`) currently matches only
  `Block.portal` (`:43`). Generalise it to match **either** a `BlockWorldPortal`
  whose metadata equals the source id, or the vanilla nether portal. For
  world-portal travel this is used only as the **fallback** landing (partner
  portal) — normally the player is placed at the destination's remembered exit
  position (§4.2). The nether keeps the current behaviour.
- `createExitLocation` (`:89`) / `Minecraft.usePortal` coordinate maths
  (`:1351-1378`): the ÷2/×2 nether scaling applies **only** between an overworld
  and the nether; linked-world ↔ linked-world world-portal travel maps 1:1
  (bounded by `WorldSize`).
- Clamp created exits inside `WorldSize` bounds.

### 5.6 New `BlockWorldPortal.java` (client + server)

**`BlockWorldPortal`** (see §3.3): a plain metadata block, **no TileEntity**,
with a **custom render type** registered in `RenderBlocks.renderItemIn3d`
(`RenderBlocks.java:4950-4963`) so the item path also knows it. The renderer
draws tile **145** (frame with transparent inner pixels) around a 12×12×12
core, and the block has `setLightValue` so placed portals emit light. The core
depends on the render context:
- **World** (placed block, `renderBlockXxx`): **glass** core (tile 49,
  `Block.java:86`).
- **Inventory / hotbar / dropped item** (`renderBlockOnInventory` custom
  branch, `RenderBlocks.java:4713-4736`; receives the stack damage as `meta`):
  **wool** core tinted by that damage 0-15 (wool tile 64 + `BlockCloth`
  colouring) — Option A item icon, §3.4.

Behaviour:
- Travel is triggered by the **block-attack hook** (§4), not `blockActivated`
  (right-click is used only by the item to *place* portals) and not
  `onEntityCollided`.
- On `breakBlock`/`onBlockDestroyedByPlayer`: decrement the destination's
  `refCount` and, if it hits 0, delete that world (§3.7, §5.8). No drops.
- Hardness ≈ obsidian, high resistance; no-op in the nether.

### 5.7 `Block.java` (client + server)

Register after the "World Overhaul" section (`Block.java:246`):

```java
public static final BlockWorldPortal worldPortal =
    (BlockWorldPortal)(new BlockWorldPortal(169, 145 /* tile: frame, transparent middle */))
        .setHardness(50.0F).setResistance(2000.0F)   // ≥ obsidian, explosion-proof
        .setStepSound(soundMetalFootstep).setBlockName("worldPortal");
```

### 5.8 `Block.java` static item assignment + new `ItemWorldPortal.java` + new `WoolPortalRegistry.java` (client + server)

In `Block.java`'s static initializer, next to the other custom block items:

```java
Item.itemsList[worldPortal.blockID] = (new ItemWorldPortal(worldPortal.blockID - 256)).setItemName("worldPortal");
```

**`ItemWorldPortal extends ItemBlock`** (`ItemBlock.java`, the `blockID = i1 + 256`
ctor pattern means we pass `blockID - 256`). Sub-types item like `ItemCloth`:
`setHasSubtypes(true)` and `getSubItems` lists the 16 variants (one per wool
colour) for creative/recipes. **Rendering uses the 3D block path** (Option A,
§3.4): `renderItemIn3d` + the `renderBlockOnInventory` branch draw frame tile
145 with the wool core tinted by the damage — no per-damage icon tiles needed.
`onItemUse` (see §3.4):
- Refuse when `worldProvider.isNether`.
- **Resolve the creation params** from the wool damage (rules below), then call
  the shared low-level API `BlockWorldPortal.placeNewWorldPortal(world, x, y, z,
  themeId, sizeId, worldTypeId)` (§5.14), which allocates the id, places the
  block with that id as metadata, does `refCount[id]++`, and writes the stub
  `level.dat`. The damage is then effectively **discarded** — placed blocks keep
  only the world id.
- Creation params for a brand-new world (frozen in the stub, §3.2):
  - **Theme** ← wool damage: 0 white = uniform random over themes with
    `isRandomWorldTheme == true` (§5.14), 14 red = Hell, 11 blue =
    Paradise, 13 dark-green = Forest, 5 light-green = Poison Island, 7 gray =
    White Forest, all others = Biomes (`LevelThemeSettings`).
  - **Terrain type** ← **the forced rule is applied here, before placement**:
    `theme.forcedWorldType` ? `theme.preferredWorldType` (Poison Island → INDEV) :
    random among creatable `WorldType`s (`WorldType.worldTypes[i] != null &&
    getCanBeCreated()`, as in `GuiCreateWorld.java:233-245`).
    `placeNewWorldPortal` itself never enforces this (§5.14).
  - **Size** ← random size id 0..4 (`WorldSize.setSizeById`).
  - **Seed** ← `deriveSeed(baseSeed, worldId)` — invertible 64-bit mixer, §5.13.
  - **Name** ← `WorldNameGen.getName(baseSeed, worldId)`, §5.13.
- Crafting: 8 obsidian around a wool block → item with damage = wool damage.

**`WoolPortalRegistry`** — save-root bookkeeping (SP: a small NBT file, e.g.
`portals.dat`, beside `level.dat`):
- `nextId` cursor, plus per world id: `refCount`. Id allocation implements §3.6
  (sequential, wrap at 256, gap scan, else random existing world).
- Kept reconcilable when a `DIM-<id>` folder is deleted (§3.7).
- No rendering data is stored here: placed portals look the same regardless of
  theme (glass core + light, §3.3); the theme lives in each world's `level.dat`.

### 5.9 `EntityPlayer.java` (client; matching stub server-side)

- Add `public int netherReturnWorldId = 0;`, saved/loaded via NBT (canonical
  player snapshot, §5.11). **Not** per-world — it always holds the overworld id
  to come back to from the nether.
- World-portal travel no longer touches the entity: it's triggered by the click
  hook (§5.10) calling `travelToDimension` directly; nothing to add in
  `EntityPlayerSP.onLivingUpdate` (walk-in portal behaviour stays only for the
  vanilla nether portal).

### 5.10 `Minecraft.java` + block-attack hook (client; matching server hook)

- New `travelToDimension(int destId)` around the existing `usePortal()`
  (`Minecraft.java:1335`) per §4. Landing position follows **§4.2**: remembered
  per-world exit position → partner portal → natural spawn.
- **Before switching**: record the current world's exit position and rotation
  into that world's `level.dat` (`LastPositionX/Y/Z` + `LastPositionYaw/Pitch`,
  §4.2/§5.11), then save.
- Rework `usePortal()` for the new numbering: on entering the nether, set
  `thePlayer.netherReturnWorldId = <current world id>` first; on leaving, travel
  to `thePlayer.netherReturnWorldId` (fallback 0).
- Add a `createReturnPortal()` step when entering a world that is **newly
  generated** (case 1 of §4.2): place a `BlockWorldPortal` with `metadata ==
  source id` inside the spawning house at the spawn-level floor tile, clear any
  block covering it, set the player's landing position and spawn point **on top
  of the portal**, and register the reference it holds on the source world
  (§3.7, §5.8).
- Insert the **block-attack hook** at the left-click dispatch
  (`Minecraft.java:853` → `playerController.clickBlock`): if the target is a
  `BlockWorldPortal` and the held item is a diamond-tier tool/weapon, cancel the
  dig and call `travelToDimension(target data)`. Server `NetServerHandler` gets
  the mirrored check for MP.
- `changeWorld` (`:1416`) and `preloadWorld` (`:1499`) already handle "new" vs
  existing worlds (`world.isNewWorld` → bulk generate); make sure the stub-detects
  "created but never generated" linked worlds through the same flag.

### 5.11 Player data sharing (SP)

b1.7.3 keeps the player inside each world's `level.dat` `Player` tag. To share
inventory/stats while remembering per-world position:

- Keep one **canonical player snapshot** in memory (survives world switches;
  also written into world 0's `Player`). It includes `netherReturnWorldId` so the
  nether return target is stable across worlds and sessions.
- On leaving world A: store A's position **and rotation** (and full player tag)
  into A's `level.dat` root as `LastPositionX/Y/Z` + `LastPositionYaw/Pitch`, then
  save (this is the data the landing rule §4.2 reads back).
- On entering world B: land per §4.2 (B's remembered position, else partner portal
  / natural spawn); build the `Player` tag for B from
  [canonical inventory/stats/XP] + [the landing position], then read it as the
  new player's data (via the usual `world.saves`/player-creation path).
- Store "last position per world" inside each linked world's `level.dat` root
  (`LastPositionX/Y/Z`, §3.2) — no separate registry file needed.

### 5.12 Theme / noise-offset care

- `GlobalVars.noiseOffsetX/Z` are **static globals** (per-session); fine for
  sequential SP switching because each `WorldInfo` ctor re-applies them on load.
  Do **not** make them per-world yet (out of scope).
- `WorldSize` statics are re-applied per load from each world's own
  `WidthInChunks`/`LengthInChunks` (`WorldInfo.java:99-107`), so **per-world
  sizes need no refactor**: set the size before creating a new linked world and
  it is already stored/restored per world.
- Per-world seeds (§3.2) already give distinct terrain between same-theme worlds,
  so shared noise offsets are acceptable.

### 5.13 New `WorldNameGen.java` + seed derivation (client + server)

Both the new world's **seed** and its **name** are deterministic functions of the
**main world's seed** (`baseSeed` = world 0's `RandomSeed`) and the **world
number** (`worldId`), recomputed identically at reserve time and at first travel
— so nothing extra needs storing beyond what is frozen in the stub `level.dat`.

**Seed — invertible 64-bit mixer.** Every step (odd-constant multiply,
xor-shift-right) is a **bijection on 64-bit integers**, so distinct world
numbers *provably* yield distinct seeds — no collisions even when two worlds
share the same theme and terrain type — while the finalizer scrambles the value
so the terrain actually differs:

```java
// long deriveSeed(long baseSeed, int worldId)
long z = baseSeed + 0x9E3779B97F4A7C15L * (long) worldId; // K odd → injective in worldId
z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;   // odd mul: bijective
z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;   // odd mul: bijective
return z ^ (z >>> 31);                        // bijective
```

- Compose it with the world number only; `worldId 0` reproduces the main world's
  own seed (`baseSeed`) exactly.
- **Do not** use `baseSeed ^ worldId` / `baseSeed + worldId` (adjacent ids →
  near-identical terrain), `new Random(worldId).nextLong()` (ignores the base
  seed), or string `hashCode` (only ~32-bit entropy): they break either the
  uniqueness or the variety guarantee.

**Name — `WorldNameGen.getName(long baseSeed, int worldId)`.** Seeded
`new Random(deriveSeed(baseSeed, worldId))` picks from adjective/noun word lists;
optionally append `" #" + worldId` to make names unique. Deterministic, so the
reserve-time stub and the eventually-generated world agree without storing the
name (it is still written to the stub's `LevelName`, §3.2).

### 5.14 `LevelThemeSettings` flags + hidden-world placement API (client + server)

**Two new flags on `LevelThemeSettings`** (default `true`; set `false` later for
secret themes). Add the fields next to the existing ones (`LevelThemeSettings.java:16-37`)
plus fluent setters in the existing style:

```java
public boolean showsOnCreation = true;    // selectable in the create-world menu
public boolean isRandomWorldTheme = true; // eligible for damage-0 random theme roll
// public LevelThemeSettings setShowsOnCreation(boolean b) { ... return this; }
// public LevelThemeSettings setIsRandomWorldTheme(boolean b) { ... return this; }
```

- **`showsOnCreation`** gates the theme button in `GuiCreateWorld` (id 8,
  `GuiCreateWorld.java:258-269`): the cycle must **skip** themes where
  `!showsOnCreation`, exactly like the existing world-type cycle skip
  (`GuiCreateWorld.java:240-245`). The initial `themeId` and the
  `preferredWorldType` sync (`:268-269`) likewise never land on a hidden theme.
  Client-only concern.
- **`isRandomWorldTheme`** narrows the **white wool (damage 0)** roll (§3.4):
  instead of "any theme", pick uniformly from the themes with
  `isRandomWorldTheme == true` (all of them for now). Rolled **at reserve time**
  and then frozen in the stub's `ThemeId`, so it is never re-derived at first
  travel.

**Hidden worlds.** A theme with `showsOnCreation == false` (and typically
`isRandomWorldTheme == false`) can never be chosen from the menu and never comes
up in a random roll — its worlds are reachable **only** via a portal placed by
the API below (special structures).

**Placement API for structure-generated portals.** A static helper on
`BlockWorldPortal` (used by structure code that holds a `World`):

```java
// Places a portal at (x,y,z) pointing at a brand-new world reserved with the
// given (already-resolved) parameters; returns the allocated world id, or -1
// if refused. Parameters are used verbatim — no forced-terrain override and no
// randomisation. Refuses in the nether (like the item).
public static int placeNewWorldPortal(World world, int x, int y, int z,
        int themeId, int sizeId, int worldTypeId)
```

Behaviour:
- Refuse (return `-1`) when `world.getWorldProvider().isNether`, mirroring
  `ItemWorldPortal` (§3.4).
- Apply the arguments **verbatim**: stub theme ← `themeId`,
  `WorldSize.setSizeById(sizeId)`, terrain ← `worldTypeId`. It does **not** apply
  `forcedWorldType` and does **not** randomise — callers resolve any defaults
  themselves first (so a structure may deliberately place *any* theme/terrain
  combination, while the item path applies the forced rule before calling in,
  §5.8).
- **Seed/name** ← `deriveSeed` / `WorldNameGen.getName(baseSeed, worldId)`, as
  for any other world (§5.13).
- Allocate the id via the registry (§3.6), place `BlockWorldPortal` at `(x,y,z)`
  with that id as metadata, `refCount[id]++`, write the stub `DIM-<id>/level.dat`
  with the frozen theme/size/terrain/seed/name (§3.5).
- The destination world is an ordinary linked world: first travel generates it,
  builds the return portal (§4.1), and travel/refcounting behave identically —
  only the *entry points* differ (no craftable item).
- The structure builder chooses `(x,y,z)` (and the portal's orientation/clearance)
  so the portal is reachable; typically a hidden-theme world's set that the
  player can't get to except through it.

## 6. Implementation milestones

**M1 — plumbing (SP).** §5.1 .. §5.4, §5.6, §5.7: DIM folders, WorldInfo `WorldId`
+ renaming/migration, generic provider, generic SaveHandler routing, new portal
block registered (not yet usable in-game). Verify: existing saves load unchanged,
legacy `-1`/Paradise worlds survive the renumbering, nether still works.

**M2 — place & travel (SP).** §5.5, §5.8, §5.9, §5.10, §5.14: ItemWorldPortal
places the block, allocates the id, writes the stub; hitting the portal with a
diamond tool reserves+generates the world, teleports the player, builds the
return portal, saves. Landing follows §4.2 (remembered exit position → partner
portal → natural spawn). Verify: place portal in world 0 → DIM-2 stub; hit it →
enter world 2; hit the return portal → back to 0 **at the spot where you left
world 0**; re-enter 2 (position restored). Hidden-world API:
`placeNewWorldPortal(world, x, y, z, theme, size, terrain)` reserves a world
with those exact params (verbatim; callers apply any `forcedWorldType`/random
resolution first, as the item path does).

**M3 — player data sharing (§5.11).** Verify: take items in world 2, return to 0,
items still there; each world's exit position/rotation is remembered and the
first-entry / invalid-position fallbacks (§4.2) land correctly.

**M4 — polish (SP).** Theme flags + create-world gating (`showsOnCreation` skips
hidden themes in the `GuiCreateWorld` theme button; `isRandomWorldTheme` narrows
the white-wool roll, §5.14). Respawn behaviour, portal rendering (placed: frame
tile 145 + glass core + light; item: same block with wool core tinted by damage,
§3.4/§5.6), world names via `WorldNameGen` (§5.13), crafting recipe, worldlist
shows nothing (linked worlds are per-save and opened only through portals),
`DIM-<id>` folders excluded from the main menu's world list.

**M5 — server compile parity.** Mirror all of the above in
`Main/src/minecraft_server` so the tree compiles (runtime MP still limited to the
main world + nether).

**M6 (later) — multiplayer.** §9.

## 7. World ids

| Id | Folder | Role |
|---|---|---|
| `0` | save root (no DIM folder) | main world / overworld (portal metadata 0 = home) |
| `1` | `DIM-1` | the nether — shared by the whole save, **not** linkable |
| `2..255` | `DIM-2` .. `DIM-255` | linked worlds, all "overworld" |

Legacy `-1` (the nether) is migrated to `1` on load (§5.2). The old Sky
*dimension* (provider id 1) is gone; id `1` now means the nether.

Consequence: **254 linkable overworlds** (0 + 2..255), plus the shared nether at
1. "Metadata = world, 0 = initial world" still holds; the nether just claims id 1
for itself.

## 8. Verification (manual, no test harness)

1. Export client jar, run, create world (any theme/size).
2. Craft `ItemWorldPortal` (8 obsidian + white wool) → place it → portal block
   appears; `saves/<name>/DIM-2/` exists with a stub `level.dat` (no region files
   yet).
3. Hit it with a diamond pick → "Generating" pass, spawn **on the return portal
   inside the spawning house** (nothing covering it); hit it → back to 0.
4. Re-enter → instant load, correct restored position; inventory shared.
5. Build an obsidian **nether** portal in world 2, light it → travel to the
   nether (`DIM-1`); step back through it → land in world 2 again, not world 0.
   Repeat from world 0 → comes back to world 0. (`NetherReturnWorldId` follows
   you.)
6. Place a second portal crafted with **dark-green wool** → world 3 is Forest;
   place one with red wool → Hell; white wool → some random theme. Same-theme
   worlds get different seeds; sizes and terrain types vary randomly.
7. Break the gate to world 3 → `DIM-3/` is deleted (refCount → 0) and the id is
   reusable. Breaking the only gate to world 2 (its return portal sits *inside*
   world 2 and points to 0, so it does not keep 2 alive) deletes world 2 too —
   any references held by portals inside the vanished folder are reconciled in
   the registry. A world with **two** gates pointing at it survives breaking one.
8. Remove `DIM-2/region/*.mcr`, re-enter → regenerates, existing `level.dat`
   theme/seed/size kept.

## 9. Multiplayer sketch (later, not MVP)

- `MinecraftServer` currently hardcodes two worlds (`worldMngr = new WorldServer[2]`,
  `MinecraftServer.java:195`, `getWorldManager` `:479-481`, `EntityTracker[3]`):
  replace with a lazy `Map<Integer, WorldServer>`; create `WorldServerMulti` per
  new id on first visit, so save folders/level.dat behave like M1.
- `ServerConfigurationManager.sendPlayerToOtherDimension` (`configManager.java:187-234`)
  becomes destination-driven: either the destination from the world-portal click
  (server dig handler sees the `BlockWorldPortal` hit and routes to its
  metadata), or the reworked nether toggle, which now stores
  `netherReturnWorldId` and travels to dimension `1` / back to the stored id
  (instead of toggling `-1`/`0`).
- `Packet9Respawn` carries a **signed byte** −128..127 (`Packet9Respawn.java`).
  After the renumbering only positive ids exist (1..255), so 1..127 work today;
  ids 128..255 need the packet widened (separate decision). No dimension uses a
  negative value anymore.
- Theme/size sync per world: today `Packet93FiniteWorldSettings` arrives once at
  login; on entering a new linked world the server must push the destination
  world's theme/size again (reuse `Packet93`).
- `Packet4UpdateTime`/`Packet95UpdateDayOfTheYear` are sent "per dimension" using
  `worldProvider.worldType` as the filter — switch the filter to the new
  `dimensionId` once ids > 1 exist.
- `NetClientHandler.handleRespawnPacket` (client, `NetClientHandler.java:695-707`)
  already rebuilds `WorldClient` from a generic dimension byte → server work only.

## 10. Resolved decisions

1. **Id assignment: automatic & sequential.** Place a portal → next sequential id
   (2..255, wrap at 256 → scan gaps from destroyed worlds → none found = link to
   a random existing world). No dialling, no copy/paste (that was the old
   key model). (§3.6)
2. **RESOLVED — theme/terrain/size of a new world (wool-driven).** Theme ← wool
   damage (0 white = uniform random over `isRandomWorldTheme` themes, red = Hell,
   blue = Paradise, dark-green = Forest, light-green = Poison Island, gray = White
   Forest, others = Biomes — small table, easy to extend for new themes). Terrain
   ← `theme.forcedWorldType` ? `preferredWorldType` : random creatable `WorldType`.
   Size ← random size id
   0..4. Seed ← `deriveSeed(baseSeed, worldId)`, name ←
   `WorldNameGen.getName(baseSeed, worldId)` (invertible mixer → distinct for
   every world number, §5.13). Frozen in the stub at placement. (§3.4)
3. **RESOLVED — portal look: frame tile 145 + core + light; no TileEntity.**
   Placed portal: tile 145 frame (transparent middle) around a **glass** core
   (tile 49) and emits light. Unplaced item (Option A): **renders as the same 3D
   block** — frame tile 145 with a **wool** core tinted by the item damage 0-15
   (custom render type in `renderItemIn3d` + a `renderBlockOnInventory` branch).
   No per-id textures. (§3.3, §3.4, §5.6)
4. **RESOLVED — no Sky dimension; the nether owns id 1.** The beta Sky
   *dimension* is dropped; `WorldType.SKY` remains a terrain generator only. The
   nether is renumbered `-1` → `1` (load-time migration, §5.2). Linked ids start
   at 2.
5. **RESOLVED — stub at placement, generate on first travel.** Reserve writes a
   stub `level.dat` now (theme/terrain/size/seed frozen); full generation happens
   on first entry. (§3.5)
6. **Dying in a linked world:** stay — linked worlds keep
   `canRespawnHere() == true`. The nether keeps the old kick-out behaviour, but
   it now lands you in the stored `netherReturnWorldId` instead of always world 0.
7. **RESOLVED — per-world random sizes.** Each portal-created world picks its own
   size id 0..4; `WorldSize` statics are re-applied per load from each world's
   `WidthInChunks`/`LengthInChunks` (`WorldInfo.java:99-107`), so no refactor of
   the globals is needed. (§3.4, §5.12)
8. **RESOLVED — return portal placement + menu visibility.** Return portal goes
   **inside the spawning house, at the spawn level, not covered**, with the
   player spawning ON the portal (§4.1). `DIM-<id>` folders **stay hidden** in
   the main-menu world list.
9. **RESOLVED — theme flags + hidden-world API.** `LevelThemeSettings` gains
   `showsOnCreation` (gate the create-world theme button) and `isRandomWorldTheme`
   (eligibility for the white-wool roll); both default `true` and are `false` only
   for future secret themes. Structures can spawn portals to reserved worlds of a
   chosen theme/size/terrain via `BlockWorldPortal.placeNewWorldPortal(...)`,
   which applies its arguments **verbatim** (no `forcedWorldType` override, no
   randomisation) and only refuses the nether. The item path enforces
   `forcedWorldType` *before* calling it. Hidden themes are unreachable except
   through such portals. (§5.8, §5.14)
10. **RESOLVED — landing on re-entry: per-world remembered exit position.**
    Each world's `level.dat` stores `LastPositionX/Y/Z` + yaw/pitch, written when
    the player leaves it. Re-entering lands the player there (resume); first
    entry lands on the freshly placed return portal, and if the remembered
    position is missing/invalid the fallback is the partner portal, then the
    natural spawn. Avoids partner-portal ambiguity when several portals point at
    the same world. (§4.2, §5.5, §5.10, §5.11)

## 11. Key file anchors (current code)

| File | Anchor |
|---|---|
| `world/level/dimension/WorldProvider.java` | `getProviderForDimension` :209-211 |
| `world/level/WorldInfo.java` | nbt ctor :42-111 (applies `WorldSize` at :99-107), `updateTagCompound` :170-202, ctor B SKY→dim :120 (remove) |
| `world/level/World.java` (ctor C) | provider branch :293-301 (nether now `1`, Sky branch removed) |
| `world/level/chunk/storage/SaveHandler.java` | `getChunkLoader` :82-90 |
| `world/level/chunk/storage/SaveOldDir.java` | `getChunkLoader` :17-26 |
| `world/level/chunk/storage/SaveConverterMcRegion.java` | `getSaveLoader` :56-57 |
| `world/level/chunk/Chunk.java` | 8-bit metadata: `byte[] data` :36, write :298 |
| `world/level/tile/Block.java` | free id **169** at :248; planks2 owns tile **167** (:302); boneBlock owns block id 167 |
| `world/level/tile/BlockCloth.java` | base tile 64, `colorMultiplier` :17/:35 — wool colouring |
| `world/item/ItemCloth.java` | sub-types + wool icon pattern :6-21 (template for `ItemWorldPortal`) |
| `world/level/tile/Block.java` (glass) | `BlockGlass(20, 49)` :86 — inner core tile |
| `world/level/tile/Block.java` (tile 145) | **free** — portal frame texture at 9×16+1 |
| `world/item/ItemBlock.java` | ctor `blockID = i1 + 256` :13-17, icon = block side-2 texture, placement metadata |
| `world/level/tile/Block.java` (static) | block-item assignment pattern :904-912; auto `ItemBlock(id-256)` loop :914-930 |
| `world/level/WorldSize.java` | static globals; `setSizeById` :103-112; sizes small/normal/big/huge/long |
| `world/level/theme/LevelThemeSettings.java` | `preferredWorldType` :32/:95, `forcedWorldType` :35/:190 (Poison → INDEV forced); add `showsOnCreation` + `isRandomWorldTheme` (~:38, §5.14) |
| `world/level/theme/LevelThemeGlobalSettings.java` | `loadThemeById` :30-55 (static active theme), `themeID` :14 |
| `world/level/WorldType.java` | creatable pool: default/sky/infdev/indev (flat is not) |
| new `WorldNameGen.java` | **TODO** — `deriveSeed(long baseSeed, int worldId)` (invertible mixer) + `getName(long baseSeed, int worldId)` (§5.13) |
| `client/gui/GuiCreateWorld.java` | random-creatable-world-type loop :233-245; theme button cycle :258-269 (gate with `showsOnCreation`) |
| `client/Minecraft.java` | `usePortal` :1335, `changeWorld` :1416, `preloadWorld` :1499, left-click dispatch :853 |
| `client/player/PlayerController*.java` | `clickBlock` (SP :52, MP :60) — portal-hit hook |
| `client/renderer/RenderBlocks.java` | `renderItemIn3d` :4950-4963 (custom render types), `renderBlockOnInventory` :4713-4736 (inventory/3D item path), `useInventoryTint` :34 |
| `client/renderer/RenderItem.java` | 3D block-items path in GUI `drawItemIntoGui` :134, dropped `doRenderItem` :51; whole-icon tint `getColorFromDamage` :144-171 (only for 2D icons — untinted for the portal) |
| `world/level/dimension/Teleporter.java` | :15/:22/:89 |
| server `MinecraftServer.java` | `worldMngr[2]` :195, `getWorldManager` :479-481 |
| server `ServerConfigurationManager.java` | `sendPlayerToOtherDimension` :187-234 |
| server net `NetServerHandler.java` | dig handler (MP world-portal hit) |
| `network/Packet9Respawn.java` | signed-byte dimension; all dimensions now positive (1..255) |
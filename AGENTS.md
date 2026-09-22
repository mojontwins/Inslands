# AGENTS.md - Inslands

## What is this

Minecraft Beta 1.7.3 mod ("Inslands" / internal name "PE_OLD"). Adds themed limited-size island worlds with custom terrain generators, mobs ported from Twilight Forest / Aether / Mo' Creatures, custom blocks/items, seasons, mazes, dungeons, and more. **No mod loader** — all code is patched directly into the decompiled vanilla source.

## Toolchain

- **RetroMCP** decompiles/deobfuscates Minecraft b1.7.3 jars into source.
  - Run `Main/retroMCP.bat` (requires Zulu JDK 8; paths hardcoded in the bat).
  - `Main/options.cfg` controls RetroMCP behavior (setup version, patching, etc.).
- **Eclipse IDE** compiles via linked-resource workspace projects (`Main/workspace/Client`, `Main/workspace/Server`).
- **No Gradle, Maven, Ant, or CI.** There are no automated tests, lint, or typecheck commands.
- JDK target: JavaSE-1.8 (classpath), JavaSE-1.6 (Idea module metadata — use 1.8).

## Build / Export

All building happens inside Eclipse:

- **Client**: Eclipse > Export > JAR file. Uncheck the `lib` folder for a standalone jar.
- **Server**: Eclipse > Export > Executable JAR.
- Output goes to `Main/bin/` (compiled classes) and `Main/reobf/` (reobfuscated jars).
- `Main/build/` contains versioned release zips (client + server + MultiMC instances).
- `.info` file in `Main/build/` is a BetaCraft launcher descriptor for running the mod.

## Source layout

There are **two independent source trees** — client and server. Shared game logic exists in both and must be kept in sync manually.

```
Main/src/
  minecraft/          # CLIENT source (decompiled + modded)
    net/minecraft/
      client/         # Minecraft.java, GUIs, renderers, models, sound
      world/          # Shared game logic: entities, blocks, items, levelgen, themes
      network/        # Packet definitions
    com/mojontwins/   # Mod author's code: WorldEdit, resources
    ca/spottedleaf/   # Starlight lighting engine
    paulscode/        # Sound system
  minecraft_server/   # SERVER source (decompiled + modded)
    net/minecraft/
      server/         # MinecraftServer.java, networking, config
      world/          # Duplicated: blocks, items, entities, themes, levelgen
    com/mojontwins/   # Server-side WorldEdit
```

## Key files

| File | Location | Purpose |
|------|----------|---------|
| `Minecraft.java` | `src/minecraft/net/minecraft/client/` | Client entry point, init, game loop |
| `MinecraftServer.java` | `src/minecraft_server/net/minecraft/server/` | Server entry point, world init |
| `Start.java` | `Main/conf/start/` | Client launcher (sets minecraftDir, calls Minecraft.main) |
| `Block.java` | Both source trees | All block definitions and registration |
| `Item.java` | Both source trees | All item definitions and registration |
| `EntityList.java` | Both source trees | Entity type registration (276 lines) |
| `LevelThemeGlobalSettings.java` | `src/minecraft/net/minecraft/world/level/theme/` | Active theme state (static holder) |
| `LevelThemeSettings.java` | Same directory | Base class + all theme definitions |
| `GlobalVars.java` | `src/minecraft_server/net/minecraft/world/` | Mod-wide global state flags |
| `Seasons.java` | Both source trees | 4-season cycle system |
| `WorldSize.java` | Both source trees | Configurable limited world sizes |
| `PlayerSaveData.java` | Both source trees | Single root `player.dat` persistence (inventory, stats, position, CurrentWorldId) |
| `PortalRegistry.java` | Both source trees | Linked-world id allocation (`portals.dat`), base save folder / seed resolution |

## Theme system

Themes are the core abstraction. Each theme controls biome, lighting, day cycle, weather, terrain gen, cave gen, structures, and spawn logic.

- `LevelThemeGlobalSettings.loadThemeById(id)` activates a theme.
- Theme IDs: 0=Normal, 1=Hell, 2=Forest, 3=Paradise, 4=Biomes, 5=Poison Island, 6=White Forest.
- Theme subclasses: `LevelThemeForest`, `LevelThemeParadise`, `LevelThemePoisonIsland`, `LevelThemeWhiteForest`.
- Server reads theme from `server.properties` and calls `loadThemeById()` during startup.
- `WorldProvider` subclasses (`WorldProviderSurface`, `WorldProviderHell`, `WorldProviderSky`) are selected per theme.

## Critical conventions

- **Always update both source trees.** If you change `Block.java`, `Item.java`, `EntityList.java`, `LevelThemeGlobalSettings.java`, or any file under `world/level/` — update the equivalent in `src/minecraft_server/` too. The two trees are independent decompilations; changes do not sync.
- **Registration is vanilla-style.** Blocks and items register via static field initialization. Entities register in `EntityList.java`'s static initializer. There are no annotations or external registries.
- **8-bit block IDs.** The mod uses 8-bit metadata for blocks — be aware of ID space limitations.
- **Named mappings.** The `.tiny` files in `Main/conf/` contain obfuscated-to-named mappings. `client.exc` / `server.exc` have exception-specific mappings.
- **Eclipse linked resources.** The workspace projects use `MCP_LOC` variable pointing to `Main/`. Do not rename or move `Main/` without updating `.project` files.

## Testing

There are no automated tests. Verification is manual:
1. Build client JAR via Eclipse export.
2. Run via BetaCraft launcher or directly (`java -jar`).
3. For server: export executable JAR, run with `java -jar`.
4. The `Main/jars/` directory serves as the runtime working directory (contains saves, resources, server.properties).

## Gotchas

- **RetroMCP paths are hardcoded** in `retroMCP.bat` to `C:\cosas\zulu8.*`. Adjust if your JDK path differs.
- **Starlight integration** is in `ca/spottedleaf/` — lighting bugs often trace back to this, not vanilla code.
- **The `.gitignore` excludes** `Main/bin/*`, `Main/reobf/*`, `Main/temp/*`, and class files. Source and build artifacts in `Main/build/` are tracked.
- **No `package.json`, no `Cargo.toml`, no build manifests.** This is a pure Java project managed by RetroMCP + Eclipse.
- **Commit messages are informal** (often single-word or casual phrases).

# Agent Configurations

## Agent Name: Big Pickle
**Profile:** High-efficiency technical assistant.

### Behavioral Instructions (System Prompt)
1. **No Flattery or Praise:** You are strictly forbidden from praising, complimenting, or flattering the user. Avoid phrases like "Excellent question!", "You are a great programmer", or "That is a brilliant idea."
2. **Direct Communication Style:** Get straight to the point. Start your response immediately with the solution, data, or code requested. Eliminate all preambles, introductory greetings, and conversational fillers.
3. **Token & Energy Efficiency:** Generate highly compact and optimized responses. If an answer can be provided in a single line of code or one short sentence, do not use additional paragraphs.

### Negative Constraints (What NEVER to do)
* DO NOT validate the user's intelligence or skill level.
* DO NOT use generic polite placeholders that consume computing resources without adding technical value.

### Interaction Examples (Few-Shot Prompting)

* **User:** "Review this Python script for errors."
* **Agent (Correct):** "The script has an incorrect indentation on line 4. Here is the corrected version: [Code]"
* **Agent (Incorrect - FORBIDDEN):** "Wow, what an excellent script you have written! You are a fantastic developer. However, I noticed a tiny detail..."

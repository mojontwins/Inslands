# LevelRenderer Optimization Plan

Client-only file: `Main/src/minecraft/net/minecraft/client/renderer/LevelRenderer.java` (1570 lines, 33+ methods).
Target: JavaSE-1.8 bytecode, decompiled b1.7.3 style. No behavioral changes — only performance + readability.

Method inventory with current line refs (from the file as read on 2026-09-15):

| Method | Lines | Priority | Why |
|--------|-------|----------|-----|
| constructor | 106-166 | cold (once) | Already bakes sky/stars into display lists — no change |
| renderStars | 168-213 | cold (once) | Compiled to `starGLCallList` — no change |
| changeWorld | 215-231 | cold | no change |
| loadRenderers | 233-313 | cold (world/distance change) | Array rebuild; no change |
| renderEntities | 315-363 | warm (per frame) | Mostly vanilla; light cleanup only |
| getDebugInfoRenders/Entities | 365-371 | cold (F3) | no change |
| markRenderersForNewPosition | 373-440 | warm (+16px) | Rotation wrap math; no perf change |
| sortAndRender | 442-611 | HOT (per frame) | **O(n) contains() per frame** + allocation site |
| checkOcclusionQueryResult | 613-635 | warm (occlusion) | Vanilla; minor rename |
| renderSortedRenderers | 637-676 | HOT (per frame) | Batched glCallLists; no change |
| renderAllRenderLists | 678-679 | no-op | no change |
| updateClouds | 681-683 | cold | no change |
| renderSky | 685-827 | HOT (per frame) | **4 useless GL calls/frame** + duplicated color |
| renderClouds | 829-888 | warm (per frame) | Vanilla; no change |
| clipRenderersByFrustrum (stub) | 890-892 | no-op | no change |
| renderCloudsFancy | 894-1035 | warm (per frame, fancy clouds) | Immediate-mode; out of scope (vertex cost, not GL state) |
| updateRenderers | 1037-1141 | HOT (per frame) | **O(n^2) remove() compaction** + double distance calls |
| drawBlockBreaking | 1143-1210 | warm (event) | no change |
| drawSelectionBox | 1212-1235 | warm (event) | no change |
| drawOutlinedBoundingBox | 1237-1263 | warm (event) | no change |
| markBlocksForUpdate | 1265-1301 | warm (block update) | no change |
| markBlockNeedsUpdate | 1303-1305 | warm | no change |
| markBlockRangeNeedsUpdate | 1307-1309 | warm | no change |
| clipRenderersByFrustum | 1311-1319 | warm (per frame) | no change |
| playRecord / playSound / showString / showChatMessage | 1321-1350 | cold (events) | no change |
| spawnParticle | 1352-1424 | HOT (particle event) | **Long if/else chain → HashMap dispatch** |
| obtainEntitySkin / releaseEntitySkin | 1426-1447 | cold | no change |
| updateAllRenderers | 1449-1459 | cold (lighting change) | no change |
| setAllRenderesVisible | 1461-1468 | cold | no change |
| doNothingWithTileEntity | 1470-1471 | no-op | no change |
| deleteDisplayLists | 1473-1475 | cold (shutdown) | no change |
| playAuxSFX | 1477-1536 | warm (events) | vanilla; no change |
| isMoving / isMovingNow / isActing / isActingNow | 1538-1565 | warm (per frame) | no change |
| renderAllSortedRenderers | 1567-1569 | cold | no change |

---

## HOT fixes (implement)

### 1. `sortAndRender` — kill the O(n) membership scan (442-451)

Every frame, when `worldRenderersToUpdate.size() < 10`, we do up to 10 `List.contains()` calls;
each is a linear scan over the (typically ~100-2000 entry) list. Fix: mirror the queue in a
`HashSet<WorldRenderer> worldRenderersToUpdateSet` and test membership against the set (O(1)).

All mutation sites must stay in sync (both trees not needed — client only):

| Site | Operation |
|------|-----------|
| `loadRenderers` (275, 293) | clear() + add() each renderer |
| `markRenderersForNewPosition` (434) | add() |
| `sortAndRender` (448) | set.add() replaces contains() gate |
| `markBlocksForUpdate` (1294) | add() |
| `updateAllRenderers` (1453) | add() |
| `updateRenderers` (1059/1065/1094/1110) | set(idx, null) → set.remove(renderer) |
| `updateRenderers` (1118) | clear() |
| `updateRenderers` compaction (1127-1136) | no set change (null compaction) |

### 2. `renderSky` — 4 useless GL calls per frame (685-827)

- Line 709: `glColor3f(f3, f4, f5)` duplicates line 705. Remove.
- Line 717: `glBlendFunc(SRC_ALPHA, ONE_MINUS_SRC_ALPHA)` is overwritten by the authoritative
  `glBlendFunc(SRC_ALPHA, ONE)` at line 767 before the sun/moon pass. Remove 717.
- Line 708+714: `glEnable(GL_FOG)` immediately followed by `glDisable(GL_FOG)` with nothing but a
  display-list call between them — net zero. Remove both.
- Line 774: `glTranslatef(0.0F, 0.0F, 0.0F)` — no-op. Remove.
- Line 775: `glRotatef(0.0F, 0.0F, 0.0F, 1.0F)` — no-op. Remove.

(That's 6 state calls removed; the "4" in the title counts the redundant color + blend + the
translate/rotate no-ops; removing the net-zero fog pair is a bonus.)

### 3. `spawnParticle` — if/else chain → O(1) dispatch (1352-1424)

21 branch conditions evaluated sequentially per spawn. Replace the known-name chain with a
`static HashMap<String, Integer>` name→caseId map and a single `switch`. The two dynamic prefixes
(`iconcrack_`, `tilecrack_`) are checked before the switch via `startsWith`.

Spawner construction still goes through the per-name `switch` case (vanilla-style `new EntityXXXFX`
calls) — behavior identical, allocation count identical (a HashMap lookup replaces ~10 string
comparisons).

### 4. `updateRenderers` — O(n^2) compaction + double distance calls (1037-1141)

- Compaction tail-removal loop (1134-1136) calls `list.remove(i)` per null entry → O(n^2).
  Replace with `this.worldRenderersToUpdate.subList(dstIndex, size).clear()` → O(n).
- `distanceToEntitySquared` is computed in the first pass (for `wrBest`), then recomputed for
  every entry in the catch-up pass (1101). Cache first-pass distances in a local
  `float[] distSqCached` sized to the queue and reuse in the catch-up pass.

---

## WARM / COLD (no change)

`renderSortedRenderers` already batches via `glCallLists(IntBuffer)` — the single biggest win in
the file is already there. `loadRenderers`, `markRenderersForNewPosition`, `renderCloudsFancy`,
`clipRenderersByFrustum` are all either infrequent or vertex-throughput-bound; leave them.

---

## Rename + comment pass

Apply to the touched methods: `sortAndRender`, `checkOcclusionQueryResult`,
`updateRenderers`, `renderSky`, `spawnParticle`, `renderEntities` — replace decompiler names
(`wr`, `d39`, `f7`, `i44`, `b41`, ...) with descriptive locals and add intent comments. No dead
method removal (public API surface; `renderAllRenderLists` stays as no-op stub).

## Verification

- `javac.exe -encoding Cp1252 -nowarn` client tree, expect exit 0 / 0 errors.
- No server-tree change (client-only feature), no fc parity needed.
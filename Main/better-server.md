# Better Server Plan — Inslands Minecraft b1.7.3

## Executive Summary
This plan resolves the core issues causing multiplayer lag, block "undo" behavior, and client-side chunk dropping in the Inslands localhost multiplayer environment. The root causes are: (1) server actively unloading chunks every tick, (2) client receiving unload packets and dropping chunks from memory, (3) memory exhaustion from GC pressure and Starlight NPE crashes, and (4) network packet instability causing desynchronization. The fix ensures all world chunks remain resident in both server and client memory at all times, matching single-player behavior for finite worlds generated once.

---

## Root Cause Analysis

### 1. Server-Chunk Unloading Loop
- `World.tick()` (World.java:2164) calls `chunkProvider.unload100OldestChunks()` every tick
- `ChunkProviderServer.unload100OldestChunks()` evicts chunks from `droppedChunksSet` and calls `onChunkUnload()` on them
- `PlayerInstance.dropChunk()` (called from `PlayerManager.updatePlayerInstances()`) actively removes chunks outside spawn protection
- This causes the "Can't keep up!" warnings seen repeatedly in `server.log`
- Unloading/reloading chunks is expensive — it triggers the tick backlog that delays block updates, causing the "undo" behavior where client-predicted state is later rolled back by the server

### 2. Client-Chunk Dropping
- `WorldClient.tick()` (WorldClient.java:73) calls `chunkProviderClient.unload100OldestChunks()` 
- `ChunkProviderClient.unloadChunk()` (line 46) removes chunks from `chunkMapping` when receiving `Packet50PreChunk(mode=false)`
- Client-side unload means blocks placed/broken can disappear when the client evicts the chunk from memory, then reappears when the chunk is re-requested from the server

### 3. Memory Exhaustion & Starlight Crashes
- Multiple `hs_err_pid*.log` files show `OutOfMemoryError: Native memory allocation failed for ChunkPool::allocate`
- 1GB heap with Compressed Oops is insufficient for the mod + Starlight lighting engine
- `StarlightEngine.initBlockLight()` NPE at line 599 crashes server during chunk preparation
- These crashes force restarts and corrupt chunk state, contributing to desync

### 4. Packet Handling Failures
- `Failed to handle packet: NullPointerException` at `Packet1Login.processPacket`
- `NetServerHandler wasn't prepared to deal with Packet107CreativeSetSlot`
- Frequent `disconnect.genericReason` disconnects indicate protocol-level issues

---

## Implementation Plan

### Phase 1: Server — Disable Chunk Unloading

**File:** `src/minecraft_server/net/minecraft/server/ChunkProviderServer.java`

#### 1.1 Override `unload100OldestChunks()`

```java
@Override
public boolean unload100OldestChunks() {
    return false;  // Never unload chunks in a finite world
}
```

**Why:** This is the primary hook that runs every server tick. Returning `false` prevents the entire eviction loop in `World.tick()`. For a finite world generated once, all chunks must remain resident.

#### 1.2 Override `dropChunk()`

```java
@Override
public void dropChunk(int chunkX, int chunkZ) {
    // No-op: finite world never drops chunks
}
```

**Why:** Prevents any code path from queuing chunks for unloading. The existing call in `PlayerInstance.removePlayer()` (line 63) is already commented out, but this override provides defense-in-depth.

#### 1.3 Verify `World.tick()` Impact

**File:** `src/minecraft/net/minecraft/world/level/World.java` (line 2164)

No code change needed. After Phase 1.1, the call `this.chunkProvider.unload100OldestChunks()` will simply return `false` and skip all unloading logic.

#### 1.4 Verify `PlayerInstance.removePlayer()`

**File:** `src/minecraft_server/net/minecraft/server/PlayerInstance.java` (line 63)

The `dropChunk` call is already commented out:
```java
//this.playerManager.getMinecraftServer().chunkProviderServer.dropChunk(this.chunkX, this.chunkZ);
```
No change needed.

---

### Phase 2: Client — Disable Chunk Unloading

**File:** `src/minecraft/net/minecraft/client/multiplayer/ChunkProviderClient.java`

#### 2.1 Override `unloadChunk()`

Change from:
```java
public void unloadChunk(int i1, int i2) {
    Chunk chunk3 = this.provideChunk(i1, i2);
    if(!chunk3.getIsChunkRendered()) {
        chunk3.onChunkUnload();
    }
    this.chunkMapping.remove(new ChunkCoordIntPair(i1, i2));
    this.chunkListing.remove(chunk3);
    this.worldObj.evictHeightQuery(i1, i2);
}
```

To:
```java
public void unloadChunk(int i1, int i2) {
    // No-op: finite world never unloads chunks on client
}
```

**Why:** This method is called when the client receives `Packet50PreChunk(mode=false)` from the server. Without this override, the client drops chunks from memory, causing the "undo" behavior where placed blocks disappear and reappear.

#### 2.2 Override `doPreChunk()` in `WorldClient`

**File:** `src/minecraft/net/minecraft/client/multiplayer/WorldClient.java` (lines 105-116)

Change from:
```java
public void doPreChunk(int i1, int i2, boolean z3) {
    if(z3) {
        this.chunkProviderClient.prepareChunk(i1, i2);
    } else {
        this.chunkProviderClient.unloadChunk(i1, i2);
    }
    if(!z3) {
        this.markBlocksDirty(i1 * 16, 0, i2 * 16, i1 * 16 + 15, 128, i2 * 16 + 15);
    }
}
```

To:
```java
public void doPreChunk(int i1, int i2, boolean z3) {
    if(z3) {
        this.chunkProviderClient.prepareChunk(i1, i2);
    }
    // Never unload chunks in finite world
}
```

**Why:** Defensive backup — even if a pre-chunk unload packet arrives from the server (which will no longer happen after Phase 1), it's ignored.

#### 2.3 Verify `unload100OldestChunks()` on Client

**File:** Same as above (lines 88-90)

Already returns `false`:
```java
public boolean unload100OldestChunks() {
    return false;
}
```
No change needed.

---

### Phase 3: Memory & Configuration Tuning

**Goal:** Ensure sufficient RAM for all world chunks + stable network flow.

#### 3.1 Calculate Required Memory

Maximum world size from `WorldSize.java.setSizeById()`:
- Size 3: 64x64 chunks = 4096 chunks
- Each chunk: ~64-128KB with lighting data (32768 bytes block + 32768 bytes metadata + sky/block light maps)
- Total world chunks: ~256-512MB
- Add entities, tile entities, Starlight engine overhead: ~1-2GB

**Action:** Set JVM heap to `-Xmx3G -Xms3G` minimum. With 3GB heap, GC pauses should be <50ms with G1GC.

#### 3.2 Increase View Distance

**File:** `server.properties`

Change:
```
view-distance=10
```
To:
```
view-distance=32
```

**Why:** For a finite world, higher view distance reduces chunk thrashing at world edges. Set to 32 to cover the entire 64x64 world with overlap. The client should also increase its render distance to match.

#### 3.3 Add G1GC Flags

**File:** Launch configuration (Eclipse run config or `Main/conf/start/Start.java`)

Add to JVM args:
```
-Xmx3G -Xms3G -XX:+UseG1GC -XX:MaxGCPauseMillis=50
```

**Why:** G1 garbage collector provides predictable pause times and is better suited for Minecraft's memory pattern than the default CMS.

#### 3.4 Verify Packet Handling Stability

**File:** `src/minecraft_server/net/minecraft/server/PlayerInstance.java` (line 126)

Confirm the send-guard is intact:
```java
if(entityPlayerMP3.listeningChunks.contains(this.currentChunk) && !entityPlayerMP3.loadedChunks.contains(this.currentChunk)) {
    entityPlayerMP3.playerNetServerHandler.sendPacket(packet1);
}
```

This prevents sending duplicate chunk data to players who already have the chunk loaded, reducing network traffic and potential desync.

---

### Phase 4: Validation & Monitoring

**Goal:** Verify the fix resolves all observed issues.

#### 4.1 Test Checklist

| # | Test | Expected Result |
|---|------|-----------------|
| 1 | Start server with 64x64 world | Server starts, no `Can't keep up!` warnings |
| 2 | Join with client | Client connects, no `disconnect.genericReason` |
| 3 | Walk to world edges | No chunks unload on client (F3 shows constant chunk count) |
| 4 | Place block | Appears immediately and persists (no "undo") |
| 5 | Break block | Disappears immediately and persists (no "undo") |
| 6 | Play 30+ minutes | Zero TPS warnings, stable memory usage |
| 7 | Save & reload world | All block states preserved |
| 8 | Monitor memory | Heap usage stabilizes at 60-70% of 3GB |

#### 4.2 Instrumentation

Add debug logging to confirm chunks are never unloaded:

**Server** (`ChunkProviderServer.java`):
```java
// Add at top of unload100OldestChunks override:
System.out.println("[DEBUG] unload100OldestChunks called but returning false — chunks remain loaded");
```

**Client** (`ChunkProviderClient.java`):
```java
// Add at top of unloadChunk override:
System.out.println("[DEBUG] unloadChunk called but no-op — chunk remains in memory");
```

#### 4.3 Success Criteria

- Zero `Can't keep up!` warnings in `server.log` for 30+ minutes
- Client F3 debug shows constant chunk count matching total world chunks
- Block placements/breaks show immediate, persistent results
- No `disconnect.genericReason` spam
- No OOM errors in `hs_err_pid*.log`
- Memory usage stable at 60-70% of allocated heap

---

## Files Modified

| File | Change |
|------|--------|
| `src/minecraft_server/net/minecraft/server/ChunkProviderServer.java` | Override `unload100OldestChunks()` → return `false`; override `dropChunk()` → no-op |
| `src/minecraft/net/minecraft/client/multiplayer/ChunkProviderClient.java` | Override `unloadChunk()` → no-op |
| `src/minecraft/net/minecraft/client/multiplayer/WorldClient.java` | Modify `doPreChunk()` → ignore `mode=false` branch |
| `server.properties` | `view-distance=32` (or higher) |
| JVM launch args | `-Xmx3G -Xms3G -XX:+UseG1GC -XX:MaxGCPauseMillis=50` |

---

## Expected Outcomes

After implementing this plan:

1. **Client never drops chunks** — all world chunks remain in memory indefinitely, matching single-player behavior
2. **Block actions persist immediately** — no "undo" behavior; placements/breaks are authoritative from the moment they occur
3. **Server maintains stable TPS** — no `Can't keep up!` warnings; tick loop stays within 20 TPS budget
4. **No OOM crashes** — heap usage stabilizes; Starlight NPE crashes eliminated (chunk lighting now complete before server tick begins)
5. **Network stability** — no packet loss/desync; connections remain stable with no spontaneous disconnects
6. **World persists correctly** — all block states saved and loaded correctly on restart

This plan directly addresses the user's core requirement: "client never drop chunks" while also resolving the observed latency and undo behaviors as primary benefits.
package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChunkProviderServer implements IChunkProvider {
	private Set droppedChunksSet = new HashSet();
	private Chunk dummyChunk;
	private IChunkProvider serverChunkGenerator;
	private IChunkLoader chunkLoader;
	public boolean chunkLoadOverride = false;
	private LongHashMap id2ChunkMap = new LongHashMap();
	private List field_727_f = new ArrayList();
	private WorldServer world;

	public ChunkProviderServer(WorldServer worldServer1, IChunkLoader iChunkLoader2, IChunkProvider iChunkProvider3) {
		this.dummyChunk = new EmptyChunk(worldServer1, 0, 0);
		this.world = worldServer1;
		this.chunkLoader = iChunkLoader2;
		this.serverChunkGenerator = iChunkProvider3;
	}

	public boolean chunkExists(int i1, int i2) {
		return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(i1, i2));
	}

	public void dropChunk(int i1, int i2) {
		if(this.world.worldProvider.canRespawnHere()) {
			ChunkCoordinates chunkCoordinates3 = this.world.getSpawnPoint();
			int i4 = i1 * 16 + 8 - chunkCoordinates3.posX;
			int i5 = i2 * 16 + 8 - chunkCoordinates3.posZ;
			short s6 = 128;
			if(i4 < -s6 || i4 > s6 || i5 < -s6 || i5 > s6) {
				this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(i1, i2));
			}
		} else {
			this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(i1, i2));
		}

	}

	public void unloadAllChunks() {
		Iterator iterator1 = this.field_727_f.iterator();

		while(iterator1.hasNext()) {
			Chunk chunk2 = (Chunk)iterator1.next();
			this.dropChunk(chunk2.xPosition, chunk2.zPosition);
		}

	}

	public Chunk loadChunk(int i1, int i2) {
		long j3 = ChunkCoordIntPair.chunkXZ2Int(i1, i2);
		this.droppedChunksSet.remove(j3);
		Chunk chunk5 = (Chunk)this.id2ChunkMap.getValueByKey(j3);
		if(chunk5 == null) {
			chunk5 = this.loadChunkFromFile(i1, i2);
			if(chunk5 == null) {
				if(this.serverChunkGenerator == null) {
					chunk5 = this.dummyChunk;
				} else {
					chunk5 = this.serverChunkGenerator.provideChunk(i1, i2);
				}
			}

			this.id2ChunkMap.add(j3, chunk5);
			this.field_727_f.add(chunk5);
			if(chunk5 != null) {
				chunk5.func_4053_c();
				chunk5.onChunkLoad();
			}

			chunk5.populateChunk(this, this, i1, i2);
		}

		return chunk5;
	}

	public Chunk provideChunk(int i1, int i2) {
		Chunk chunk3 = (Chunk)this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(i1, i2));
		return chunk3 == null ? (!this.world.findingSpawnPoint && !this.chunkLoadOverride ? this.dummyChunk : this.loadChunk(i1, i2)) : chunk3;
	}

	private Chunk loadChunkFromFile(int i1, int i2) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk chunk3 = this.chunkLoader.loadChunk(this.world, i1, i2);
				if(chunk3 != null) {
					chunk3.lastSaveTime = this.world.getWorldTime();
				}

				return chunk3;
			} catch (Exception exception4) {
				exception4.printStackTrace();
				return null;
			}
		}
	}

	private void saveChunkExtraData(Chunk chunk1) {
		if(this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.world, chunk1);
			} catch (Exception exception3) {
				exception3.printStackTrace();
			}

		}
	}

	private void saveChunkData(Chunk chunk1) {
		if(this.chunkLoader != null) {
			try {
				chunk1.lastSaveTime = this.world.getWorldTime();
				this.chunkLoader.saveChunk(this.world, chunk1);
			} catch (IOException iOException3) {
				iOException3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		Chunk chunk4 = this.provideChunk(i2, i3);
		if(!chunk4.isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			if(this.serverChunkGenerator != null) {
				this.serverChunkGenerator.populate(iChunkProvider1, i2, i3);
				chunk4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		int i3 = 0;

		for(int i4 = 0; i4 < this.field_727_f.size(); ++i4) {
			Chunk chunk5 = (Chunk)this.field_727_f.get(i4);
			if(z1) {
				this.saveChunkExtraData(chunk5);
			}

			if(chunk5.needsSaving(z1)) {
				this.saveChunkData(chunk5);
				chunk5.isModified = false;
				++i3;
				if(i3 == 24 && !z1) {
					return false;
				}
			}
		}

		if(z1) {
			if(this.chunkLoader == null) {
				return true;
			}

			this.chunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		if(!this.world.levelSaving) {
			for(int i1 = 0; i1 < 100; ++i1) {
				if(!this.droppedChunksSet.isEmpty()) {
					Long long2 = (Long)this.droppedChunksSet.iterator().next();
					Chunk chunk3 = (Chunk)this.id2ChunkMap.getValueByKey(long2.longValue());
					chunk3.onChunkUnload();
					this.saveChunkData(chunk3);
					this.saveChunkExtraData(chunk3);
					this.droppedChunksSet.remove(long2);
					this.id2ChunkMap.remove(long2.longValue());
					this.field_727_f.remove(chunk3);
				}
			}

			if(this.chunkLoader != null) {
				this.chunkLoader.chunkTick();
			}
		}

		return this.serverChunkGenerator.unload100OldestChunks();
	}

	public boolean canSave() {
		return !this.world.levelSaving;
	}

	public String func_46040_d() {
		return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
	}

	public List getPossibleCreatures(EnumCreatureType enumCreatureType1, int i2, int i3, int i4) {
		return this.serverChunkGenerator.getPossibleCreatures(enumCreatureType1, i2, i3, i4);
	}

	public ChunkPosition findClosestStructure(World world1, String string2, int i3, int i4, int i5) {
		return this.serverChunkGenerator.findClosestStructure(world1, string2, i3, i4, i5);
	}
}

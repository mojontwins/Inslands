package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChunkProvider implements IChunkProvider {
	private Set droppedChunksSet = new HashSet();
	private Chunk field_28064_b;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private Map chunkMap = new HashMap();
	private List chunkList = new ArrayList();
	private World field_28066_g;

	public ChunkProvider(World world1, IChunkLoader iChunkLoader2, IChunkProvider iChunkProvider3) {
		this.field_28064_b = new EmptyChunk(world1, new byte[32768], 0, 0);
		this.field_28066_g = world1;
		this.chunkLoader = iChunkLoader2;
		this.chunkProvider = iChunkProvider3;
	}

	public boolean chunkExists(int i1, int i2) {
		return this.chunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(i1, i2));
	}

	public Chunk prepareChunk(int i1, int i2) {
		int i3 = ChunkCoordIntPair.chunkXZ2Int(i1, i2);
		this.droppedChunksSet.remove(i3);
		Chunk chunk4 = (Chunk)this.chunkMap.get(i3);
		if(chunk4 == null) {
			chunk4 = this.loadChunkFromFile(i1, i2);
			if(chunk4 == null) {
				if(this.chunkProvider == null) {
					chunk4 = this.field_28064_b;
				} else {
					chunk4 = this.chunkProvider.provideChunk(i1, i2);
				}
			}

			this.chunkMap.put(i3, chunk4);
			this.chunkList.add(chunk4);
			if(chunk4 != null) {
				chunk4.func_4143_d();
				chunk4.onChunkLoad();
			}

			if(!chunk4.isTerrainPopulated && this.chunkExists(i1 + 1, i2 + 1) && this.chunkExists(i1, i2 + 1) && this.chunkExists(i1 + 1, i2)) {
				this.populate(this, i1, i2);
			}

			if(this.chunkExists(i1 - 1, i2) && !this.provideChunk(i1 - 1, i2).isTerrainPopulated && this.chunkExists(i1 - 1, i2 + 1) && this.chunkExists(i1, i2 + 1) && this.chunkExists(i1 - 1, i2)) {
				this.populate(this, i1 - 1, i2);
			}

			if(this.chunkExists(i1, i2 - 1) && !this.provideChunk(i1, i2 - 1).isTerrainPopulated && this.chunkExists(i1 + 1, i2 - 1) && this.chunkExists(i1, i2 - 1) && this.chunkExists(i1 + 1, i2)) {
				this.populate(this, i1, i2 - 1);
			}

			if(this.chunkExists(i1 - 1, i2 - 1) && !this.provideChunk(i1 - 1, i2 - 1).isTerrainPopulated && this.chunkExists(i1 - 1, i2 - 1) && this.chunkExists(i1, i2 - 1) && this.chunkExists(i1 - 1, i2)) {
				this.populate(this, i1 - 1, i2 - 1);
			}
		}

		return chunk4;
	}

	public Chunk provideChunk(int i1, int i2) {
		Chunk chunk3 = (Chunk)this.chunkMap.get(ChunkCoordIntPair.chunkXZ2Int(i1, i2));
		return chunk3 == null ? this.prepareChunk(i1, i2) : chunk3;
	}

	private Chunk loadChunkFromFile(int i1, int i2) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk chunk3 = this.chunkLoader.loadChunk(this.field_28066_g, i1, i2);
				if(chunk3 != null) {
					chunk3.lastSaveTime = this.field_28066_g.getWorldTime();
				}

				return chunk3;
			} catch (Exception exception4) {
				exception4.printStackTrace();
				return null;
			}
		}
	}

	private void func_28063_a(Chunk chunk1) {
		if(this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.field_28066_g, chunk1);
			} catch (Exception exception3) {
				exception3.printStackTrace();
			}

		}
	}

	private void func_28062_b(Chunk chunk1) {
		if(this.chunkLoader != null) {
			try {
				chunk1.lastSaveTime = this.field_28066_g.getWorldTime();
				this.chunkLoader.saveChunk(this.field_28066_g, chunk1);
			} catch (IOException iOException3) {
				iOException3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		Chunk chunk4 = this.provideChunk(i2, i3);
		if(!chunk4.isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(iChunkProvider1, i2, i3);
				chunk4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		int i3 = 0;

		for(int i4 = 0; i4 < this.chunkList.size(); ++i4) {
			Chunk chunk5 = (Chunk)this.chunkList.get(i4);
			if(z1 && !chunk5.neverSave) {
				this.func_28063_a(chunk5);
			}

			if(chunk5.needsSaving(z1)) {
				this.func_28062_b(chunk5);
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
		for(int i1 = 0; i1 < 100; ++i1) {
			if(!this.droppedChunksSet.isEmpty()) {
				Integer integer2 = (Integer)this.droppedChunksSet.iterator().next();
				Chunk chunk3 = (Chunk)this.chunkMap.get(integer2);
				chunk3.onChunkUnload();
				this.func_28062_b(chunk3);
				this.func_28063_a(chunk3);
				this.droppedChunksSet.remove(integer2);
				this.chunkMap.remove(integer2);
				this.chunkList.remove(chunk3);
			}
		}

		if(this.chunkLoader != null) {
			this.chunkLoader.func_814_a();
		}

		return this.chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "ServerChunkCache: " + this.chunkMap.size() + " Drop: " + this.droppedChunksSet.size();
	}
}

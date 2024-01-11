package net.minecraft.src;

import java.io.IOException;

public class ChunkProvider implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private World worldObj;
	
	private boolean debug = false;
	
	// Modified, simplified version for finite worlds

	private Chunk[] chunkCache;
	
	public ChunkProvider(World world1, IChunkLoader iChunkLoader2, IChunkProvider iChunkProvider3) {		
		this.worldObj = world1;
		this.chunkLoader = iChunkLoader2;
		this.chunkProvider = iChunkProvider3;
		this.blankChunk = this.getBlankChunk();
		this.chunkCache = new Chunk[WorldSize.getTotalChunks()];		
	}
	
	private Chunk getBlankChunk() {
		Chunk chunk = new Chunk(this.worldObj, new byte[32768], new byte[32768], 0, 0);
		chunk.neverSave = true;
		
		Block mainLiquid = Block.blocksList[LevelThemeGlobalSettings.levelThemeMainBiome.mainLiquid];
		
		if(LevelThemeGlobalSettings.worldTypeID != WorldType.SKY.getId()) {
			// Fill with water up to y = 63
			for(int x = 0; x < 16; x ++) {
				for(int z = 0; z < 16; z ++) {
					int index = x << 11 | z << 7;
					for(int y = 0; y < 64; y ++) {
						chunk.blocks[index ++] =  y < 56 ? (byte)Block.stone.blockID : (byte)mainLiquid.blockID;
					}
					
					chunk.blocklightMap.setNibble(x, 63, z, Block.lightValue[mainLiquid.blockID]);
				}
			}
		}
		
		chunk.generateSkylightMapSimple();
		
		return chunk;
	}

	public boolean chunkExists(int xChunk, int zChunk) {
		if(xChunk < 0 || zChunk < 0 || xChunk >= WorldSize.xChunks || zChunk >= WorldSize.zChunks) return true;
		return this.chunkCache[WorldSize.coords2hash(xChunk, zChunk)] != null;
	}

	public Chunk prepareChunk(int xChunk, int zChunk) {
		if(xChunk < 0 || xChunk >= WorldSize.xChunks || zChunk < 0 || zChunk >= WorldSize.zChunks) {
			return this.blankChunk;
		} else {
			int hash = WorldSize.coords2hash(xChunk, zChunk);
			Chunk chunk = this.chunkCache[hash];
			
			if(chunk == null) {
				chunk = this.loadChunkFromFile(xChunk, zChunk);
				
				if(chunk == null) {
					if(this.chunkProvider == null) {
						chunk = this.blankChunk;
					} else {
						if(this.debug) System.out.println ("PROVIDING " + xChunk + " " + zChunk);
						chunk = this.chunkProvider.provideChunk(xChunk, zChunk);
					}
				} else if(this.debug) System.out.println ("LOADED " + xChunk + " " + zChunk);
	
				this.chunkCache[hash] = chunk;
				
				if(chunk != null) {
					chunk.doesNothing();
					chunk.onChunkLoad();
				}
	
				if(!chunk.isTerrainPopulated && this.chunkExists(xChunk + 1, zChunk + 1) && this.chunkExists(xChunk, zChunk + 1) && this.chunkExists(xChunk + 1, zChunk)) {
					this.populate(this, xChunk, zChunk);
				}
	
				if(this.chunkExists(xChunk - 1, zChunk) && !this.provideChunk(xChunk - 1, zChunk).isTerrainPopulated && this.chunkExists(xChunk - 1, zChunk + 1) && this.chunkExists(xChunk, zChunk + 1) && this.chunkExists(xChunk - 1, zChunk)) {
					this.populate(this, xChunk - 1, zChunk);
				}
	
				if(this.chunkExists(xChunk, zChunk - 1) && !this.provideChunk(xChunk, zChunk - 1).isTerrainPopulated && this.chunkExists(xChunk + 1, zChunk - 1) && this.chunkExists(xChunk, zChunk - 1) && this.chunkExists(xChunk + 1, zChunk)) {
					this.populate(this, xChunk, zChunk - 1);
				}
	
				if(this.chunkExists(xChunk - 1, zChunk - 1) && !this.provideChunk(xChunk - 1, zChunk - 1).isTerrainPopulated && this.chunkExists(xChunk - 1, zChunk - 1) && this.chunkExists(xChunk, zChunk - 1) && this.chunkExists(xChunk - 1, zChunk)) {
					this.populate(this, xChunk - 1, zChunk - 1);
				}
			}
			
			return chunk;
		}	
	}

	public Chunk provideChunk(int i1, int i2) { 
		if(i1 < 0 || i1 >= WorldSize.xChunks || i2 < 0 || i2 >= WorldSize.zChunks) {
			return this.blankChunk;
		} else {
			Chunk chunk3 = this.chunkCache[WorldSize.coords2hash(i1, i2)];
			return chunk3 == null ? this.prepareChunk(i1, i2) : chunk3;
		}
	}
	
	public Chunk justGenerateForHeight(int chunkX, int chunkZ) {
		return this.chunkProvider.justGenerateForHeight(chunkX, chunkZ);
	}

	private Chunk loadChunkFromFile(int i1, int i2) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk chunk3 = this.chunkLoader.loadChunk(this.worldObj, i1, i2);
				if(chunk3 != null) {
					chunk3.lastSaveTime = this.worldObj.getWorldTime();
				}

				return chunk3;
			} catch (Exception exception4) {
				exception4.printStackTrace();
				return null;
			}
		}
	}

	private void saveExtraChunkData(Chunk chunk1) {
		if(this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.worldObj, chunk1);
			} catch (Exception exception3) {
				exception3.printStackTrace();
			}

		}
	}

	private void saveChunk(Chunk chunk1) {
		if(this.chunkLoader != null) {
			try {
				chunk1.lastSaveTime = this.worldObj.getWorldTime();
				this.chunkLoader.saveChunk(this.worldObj, chunk1);
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
			
		for(int i4 = 0; i4 < WorldSize.getTotalChunks(); i4 ++) {
			Chunk chunk5 = this.chunkCache[i4];
					
			if(chunk5 != null) {
				if(z1 && !chunk5.neverSave) {
					this.saveExtraChunkData(chunk5);
				}
	
				if(chunk5.needsSaving(z1)) {
					this.saveChunk(chunk5);
					chunk5.isModified = false;
					++i3;
					if(i3 == 24 && !z1) {
						return false;
					}
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
		return false;
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "ServerChunkCache: " + this.chunkCache.length ; 
	}
}

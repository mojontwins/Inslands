package net.minecraft.world.level.chunk;

import java.io.IOException;

import net.minecraft.src.World;
import net.minecraft.src.WorldSize;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;

public class ChunkProvider implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private World worldObj;
	
	private boolean debug = false;
	private boolean populateDebug = false;
	
	// Modified, simplified version for finite worlds by na_th_an
	// "simplified", that's what I thought XD

	private Chunk[] chunkCache;
	
	public ChunkProvider(World world1, IChunkLoader iChunkLoader2, IChunkProvider iChunkProvider3) {		
		this.worldObj = world1;
		this.chunkLoader = iChunkLoader2;
		this.chunkProvider = iChunkProvider3;
		this.blankChunk = this.getBlankChunk();
		this.chunkCache = new Chunk[WorldSize.getTotalChunks()];		
	}
	
	public IChunkProvider getChunkProviderGenerate() {
		return this.chunkProvider;
	}
	
	private Chunk getBlankChunk() {
		return this.chunkProvider.makeBlank(this.worldObj);
	}
	
	// Shouldn't be needed
	public Chunk makeBlank(World world) {
		return this.chunkProvider.makeBlank(this.worldObj);
	}

	public boolean chunkExists(int xChunk, int zChunk) {
		if(xChunk < 0 || zChunk < 0 || xChunk >= WorldSize.getXChunks(this.chunkProvider) || zChunk >= WorldSize.getZChunks(this.chunkProvider)) return true;
		return this.chunkCache[WorldSize.coords2hash(xChunk, zChunk)] != null;
	}

	public Chunk prepareChunk(int xChunk, int zChunk) {
		if(xChunk < 0 || xChunk >= WorldSize.getXChunks(this.chunkProvider) || zChunk < 0 || zChunk >= WorldSize.getZChunks(this.chunkProvider)) {
			return this.blankChunk;
		} else {
			int hash = WorldSize.coords2hash(xChunk, zChunk);
			Chunk chunk = this.chunkCache[hash];
			boolean generated = false;
			if(chunk == null) {
				chunk = this.loadChunkFromFile(xChunk, zChunk);
				
				if(chunk == null) {
					if(this.chunkProvider == null) {
						chunk = this.blankChunk;
					} else {
						if(this.debug) System.out.println ("PROVIDING " + xChunk + " " + zChunk);
						chunk = this.chunkProvider.provideChunk(xChunk, zChunk);
						generated = true;
					}
				} else if(this.debug) System.out.println ("LOADED " + xChunk + " " + zChunk);
	
				this.chunkCache[hash] = chunk;
				
				if(chunk != null) {
					chunk.onChunkLoad();
					if(generated) chunk.initLightingForRealNotJustHeightmap();
				}
	
				if(
						!chunk.isTerrainPopulated && 
						this.chunkExists(xChunk + 1, zChunk + 1) && 
						this.chunkExists(xChunk, zChunk + 1) && 
						this.chunkExists(xChunk + 1, zChunk)
				) {
					this.populate(this, xChunk, zChunk);
				}
	
				if(xChunk > 0) {
					if(
						this.chunkExists(xChunk - 1, zChunk) && 
						!this.provideChunk(xChunk - 1, zChunk).isTerrainPopulated && 
						this.chunkExists(xChunk - 1, zChunk + 1) && 
						this.chunkExists(xChunk, zChunk + 1) && 
						this.chunkExists(xChunk - 1, zChunk)
					) {
						this.populate(this, xChunk - 1, zChunk);
					}
				}
	
				if(zChunk > 0) {
					if(
							this.chunkExists(xChunk, zChunk - 1) && 
							!this.provideChunk(xChunk, zChunk - 1).isTerrainPopulated && 
							this.chunkExists(xChunk + 1, zChunk - 1) && 
							this.chunkExists(xChunk, zChunk - 1) && 
							this.chunkExists(xChunk + 1, zChunk)
					) {
						this.populate(this, xChunk, zChunk - 1);
					}
				}
	
				if(xChunk > 0 && zChunk > 0) {
					if(
							this.chunkExists(xChunk - 1, zChunk - 1) && 
							!this.provideChunk(xChunk - 1, zChunk - 1).isTerrainPopulated && 
							this.chunkExists(xChunk - 1, zChunk - 1) && 
							this.chunkExists(xChunk, zChunk - 1) && 
							this.chunkExists(xChunk - 1, zChunk)
					) {
						this.populate(this, xChunk - 1, zChunk - 1);
					}
				}
			}
			
			return chunk;
		}	
	}

	public Chunk provideChunk(int x, int z) { 
		if(x < 0 || x >= WorldSize.getXChunks(this.chunkProvider) || z < 0 || z >= WorldSize.getZChunks(this.chunkProvider)) {
			return this.blankChunk;
		} else {
			Chunk chunk = this.chunkCache[WorldSize.coords2hash(x, z)];
			return chunk == null ? this.prepareChunk(x, z) : chunk;
		}
	}
	
	public Chunk justGenerateForHeight(int chunkX, int chunkZ) {
		if(chunkX >= 0 && chunkX < WorldSize.getXChunks(this.chunkProvider) && chunkZ >= 0 && chunkZ < WorldSize.getZChunks(this.chunkProvider)) {
			return this.chunkProvider.justGenerateForHeight(chunkX, chunkZ);
		} else {
			return this.blankChunk;
		}
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

	public void populate(IChunkProvider iChunkProvider1, int x, int z) {
		Chunk chunk4 = this.provideChunk(x, z);
		if(!chunk4.isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				if(populateDebug) System.out.println ("Populating " + x + " " + z);
				this.chunkProvider.populate(iChunkProvider1, x, z);
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

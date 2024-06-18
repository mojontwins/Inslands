package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Chunk {
	public static boolean isLit;
	private ExtendedBlockStorage[] storageArrays;
	private byte[] blockBiomeArray;
	public int[] precipitationHeightMap;
	public boolean[] updateSkylightColumns;
	public boolean isChunkLoaded;
	public World worldObj;
	public int[] heightMap;
	public final int xPosition;
	public final int zPosition;
	private boolean isGapLightingUpdated;
	public Map chunkTileEntityMap;
	public List[] entityLists;
	public boolean isTerrainPopulated;
	public boolean isModified;
	public boolean hasEntities;
	public long lastSaveTime;
	public boolean field_50120_o;
	private int queuedLightChecks;
	boolean field_35846_u;

	public Chunk(World world1, int i2, int i3) {
		this.storageArrays = new ExtendedBlockStorage[16];
		this.blockBiomeArray = new byte[256];
		this.precipitationHeightMap = new int[256];
		this.updateSkylightColumns = new boolean[256];
		this.isGapLightingUpdated = false;
		this.chunkTileEntityMap = new HashMap();
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
		this.lastSaveTime = 0L;
		this.field_50120_o = false;
		this.queuedLightChecks = 4096;
		this.field_35846_u = false;
		this.entityLists = new List[16];
		this.worldObj = world1;
		this.xPosition = i2;
		this.zPosition = i3;
		this.heightMap = new int[256];

		for(int i4 = 0; i4 < this.entityLists.length; ++i4) {
			this.entityLists[i4] = new ArrayList();
		}

		Arrays.fill(this.precipitationHeightMap, -999);
		Arrays.fill(this.blockBiomeArray, (byte)-1);
	}

	public Chunk(World world1, byte[] b2, int i3, int i4) {
		this(world1, i3, i4);
		int i5 = b2.length / 256;

		for(int i6 = 0; i6 < 16; ++i6) {
			for(int i7 = 0; i7 < 16; ++i7) {
				for(int i8 = 0; i8 < i5; ++i8) {
					byte b9 = b2[i6 << 11 | i7 << 7 | i8];
					if(b9 != 0) {
						int i10 = i8 >> 4;
						if(this.storageArrays[i10] == null) {
							this.storageArrays[i10] = new ExtendedBlockStorage(i10 << 4);
						}

						this.storageArrays[i10].setExtBlockID(i6, i8 & 15, i7, b9);
					}
				}
			}
		}

	}

	public boolean isAtLocation(int i1, int i2) {
		return i1 == this.xPosition && i2 == this.zPosition;
	}

	public int getHeightValue(int i1, int i2) {
		return this.heightMap[i2 << 4 | i1];
	}

	public int getTopFilledSegment() {
		for(int i1 = this.storageArrays.length - 1; i1 >= 0; --i1) {
			if(this.storageArrays[i1] != null) {
				return this.storageArrays[i1].getYLocation();
			}
		}

		return 0;
	}

	public ExtendedBlockStorage[] getBlockStorageArray() {
		return this.storageArrays;
	}

	public void generateHeightMap() {
		int i1 = this.getTopFilledSegment();

		for(int i2 = 0; i2 < 16; ++i2) {
			for(int i3 = 0; i3 < 16; ++i3) {
				this.precipitationHeightMap[i2 + (i3 << 4)] = -999;

				for(int i4 = i1 + 16 - 1; i4 > 0; --i4) {
					int i5 = this.getBlockID(i2, i4 - 1, i3);
					if(Block.lightOpacity[i5] != 0) {
						this.heightMap[i3 << 4 | i2] = i4;
						break;
					}
				}
			}
		}

		this.isModified = true;
	}

	public void generateSkylightMap() {
		int i1 = this.getTopFilledSegment();

		int i2;
		int i3;
		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.precipitationHeightMap[i2 + (i3 << 4)] = -999;

				int i4;
				for(i4 = i1 + 16 - 1; i4 > 0; --i4) {
					if(this.getBlockLightOpacity(i2, i4 - 1, i3) != 0) {
						this.heightMap[i3 << 4 | i2] = i4;
						break;
					}
				}

				if(!this.worldObj.worldProvider.hasNoSky) {
					i4 = 15;
					int i5 = i1 + 16 - 1;

					do {
						i4 -= this.getBlockLightOpacity(i2, i5, i3);
						if(i4 > 0) {
							ExtendedBlockStorage extendedBlockStorage6 = this.storageArrays[i5 >> 4];
							if(extendedBlockStorage6 != null) {
								extendedBlockStorage6.setExtSkylightValue(i2, i5 & 15, i3, i4);
								this.worldObj.func_48464_p((this.xPosition << 4) + i2, i5, (this.zPosition << 4) + i3);
							}
						}

						--i5;
					} while(i5 > 0 && i4 > 0);
				}
			}
		}

		this.isModified = true;

		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.propagateSkylightOcclusion(i2, i3);
			}
		}

	}

	public void func_4143_d() {
	}

	private void propagateSkylightOcclusion(int i1, int i2) {
		this.updateSkylightColumns[i1 + i2 * 16] = true;
		this.isGapLightingUpdated = true;
	}

	private void updateSkylight_do() {
		Profiler.startSection("recheckGaps");
		if(this.worldObj.doChunksNearChunkExist(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8, 16)) {
			for(int i1 = 0; i1 < 16; ++i1) {
				for(int i2 = 0; i2 < 16; ++i2) {
					if(this.updateSkylightColumns[i1 + i2 * 16]) {
						this.updateSkylightColumns[i1 + i2 * 16] = false;
						int i3 = this.getHeightValue(i1, i2);
						int i4 = this.xPosition * 16 + i1;
						int i5 = this.zPosition * 16 + i2;
						int i6 = this.worldObj.getHeightValue(i4 - 1, i5);
						int i7 = this.worldObj.getHeightValue(i4 + 1, i5);
						int i8 = this.worldObj.getHeightValue(i4, i5 - 1);
						int i9 = this.worldObj.getHeightValue(i4, i5 + 1);
						if(i7 < i6) {
							i6 = i7;
						}

						if(i8 < i6) {
							i6 = i8;
						}

						if(i9 < i6) {
							i6 = i9;
						}

						this.checkSkylightNeighborHeight(i4, i5, i6);
						this.checkSkylightNeighborHeight(i4 - 1, i5, i3);
						this.checkSkylightNeighborHeight(i4 + 1, i5, i3);
						this.checkSkylightNeighborHeight(i4, i5 - 1, i3);
						this.checkSkylightNeighborHeight(i4, i5 + 1, i3);
					}
				}
			}

			this.isGapLightingUpdated = false;
		}

		Profiler.endSection();
	}

	private void checkSkylightNeighborHeight(int i1, int i2, int i3) {
		int i4 = this.worldObj.getHeightValue(i1, i2);
		if(i4 > i3) {
			this.updateSkylightNeighborHeight(i1, i2, i3, i4 + 1);
		} else if(i4 < i3) {
			this.updateSkylightNeighborHeight(i1, i2, i4, i3 + 1);
		}

	}

	private void updateSkylightNeighborHeight(int i1, int i2, int i3, int i4) {
		if(i4 > i3 && this.worldObj.doChunksNearChunkExist(i1, 0, i2, 16)) {
			for(int i5 = i3; i5 < i4; ++i5) {
				this.worldObj.updateLightByType(EnumSkyBlock.Sky, i1, i5, i2);
			}

			this.isModified = true;
		}

	}

	private void relightBlock(int i1, int i2, int i3) {
		int i4 = this.heightMap[i3 << 4 | i1] & 255;
		int i5 = i4;
		if(i2 > i4) {
			i5 = i2;
		}

		while(i5 > 0 && this.getBlockLightOpacity(i1, i5 - 1, i3) == 0) {
			--i5;
		}

		if(i5 != i4) {
			this.worldObj.markBlocksDirtyVertical(i1, i3, i5, i4);
			this.heightMap[i3 << 4 | i1] = i5;
			int i6 = this.xPosition * 16 + i1;
			int i7 = this.zPosition * 16 + i3;
			int i8;
			int i12;
			if(!this.worldObj.worldProvider.hasNoSky) {
				ExtendedBlockStorage extendedBlockStorage9;
				if(i5 < i4) {
					for(i8 = i5; i8 < i4; ++i8) {
						extendedBlockStorage9 = this.storageArrays[i8 >> 4];
						if(extendedBlockStorage9 != null) {
							extendedBlockStorage9.setExtSkylightValue(i1, i8 & 15, i3, 15);
							this.worldObj.func_48464_p((this.xPosition << 4) + i1, i8, (this.zPosition << 4) + i3);
						}
					}
				} else {
					for(i8 = i4; i8 < i5; ++i8) {
						extendedBlockStorage9 = this.storageArrays[i8 >> 4];
						if(extendedBlockStorage9 != null) {
							extendedBlockStorage9.setExtSkylightValue(i1, i8 & 15, i3, 0);
							this.worldObj.func_48464_p((this.xPosition << 4) + i1, i8, (this.zPosition << 4) + i3);
						}
					}
				}

				i8 = 15;

				while(i5 > 0 && i8 > 0) {
					--i5;
					i12 = this.getBlockLightOpacity(i1, i5, i3);
					if(i12 == 0) {
						i12 = 1;
					}

					i8 -= i12;
					if(i8 < 0) {
						i8 = 0;
					}

					ExtendedBlockStorage extendedBlockStorage10 = this.storageArrays[i5 >> 4];
					if(extendedBlockStorage10 != null) {
						extendedBlockStorage10.setExtSkylightValue(i1, i5 & 15, i3, i8);
					}
				}
			}

			i8 = this.heightMap[i3 << 4 | i1];
			i12 = i4;
			int i13 = i8;
			if(i8 < i4) {
				i12 = i8;
				i13 = i4;
			}

			if(!this.worldObj.worldProvider.hasNoSky) {
				this.updateSkylightNeighborHeight(i6 - 1, i7, i12, i13);
				this.updateSkylightNeighborHeight(i6 + 1, i7, i12, i13);
				this.updateSkylightNeighborHeight(i6, i7 - 1, i12, i13);
				this.updateSkylightNeighborHeight(i6, i7 + 1, i12, i13);
				this.updateSkylightNeighborHeight(i6, i7, i12, i13);
			}

			this.isModified = true;
		}
	}

	public int getBlockLightOpacity(int i1, int i2, int i3) {
		return Block.lightOpacity[this.getBlockID(i1, i2, i3)];
	}

	public int getBlockID(int i1, int i2, int i3) {
		if(i2 >> 4 >= this.storageArrays.length) {
			return 0;
		} else {
			ExtendedBlockStorage extendedBlockStorage4 = this.storageArrays[i2 >> 4];
			return extendedBlockStorage4 != null ? extendedBlockStorage4.getExtBlockID(i1, i2 & 15, i3) : 0;
		}
	}

	public int getBlockMetadata(int i1, int i2, int i3) {
		if(i2 >> 4 >= this.storageArrays.length) {
			return 0;
		} else {
			ExtendedBlockStorage extendedBlockStorage4 = this.storageArrays[i2 >> 4];
			return extendedBlockStorage4 != null ? extendedBlockStorage4.getExtBlockMetadata(i1, i2 & 15, i3) : 0;
		}
	}

	public boolean setBlockID(int i1, int i2, int i3, int i4) {
		return this.setBlockIDWithMetadata(i1, i2, i3, i4, 0);
	}

	public boolean setBlockIDWithMetadata(int i1, int i2, int i3, int i4, int i5) {
		int i6 = i3 << 4 | i1;
		if(i2 >= this.precipitationHeightMap[i6] - 1) {
			this.precipitationHeightMap[i6] = -999;
		}

		int i7 = this.heightMap[i6];
		int i8 = this.getBlockID(i1, i2, i3);
		if(i8 == i4 && this.getBlockMetadata(i1, i2, i3) == i5) {
			return false;
		} else {
			ExtendedBlockStorage extendedBlockStorage9 = this.storageArrays[i2 >> 4];
			boolean z10 = false;
			if(extendedBlockStorage9 == null) {
				if(i4 == 0) {
					return false;
				}

				extendedBlockStorage9 = this.storageArrays[i2 >> 4] = new ExtendedBlockStorage(i2 >> 4 << 4);
				z10 = i2 >= i7;
			}

			extendedBlockStorage9.setExtBlockID(i1, i2 & 15, i3, i4);
			int i11 = this.xPosition * 16 + i1;
			int i12 = this.zPosition * 16 + i3;
			if(i8 != 0) {
				if(!this.worldObj.isRemote) {
					Block.blocksList[i8].onBlockRemoval(this.worldObj, i11, i2, i12);
				} else if(Block.blocksList[i8] instanceof BlockContainer && i8 != i4) {
					this.worldObj.removeBlockTileEntity(i11, i2, i12);
				}
			}

			if(extendedBlockStorage9.getExtBlockID(i1, i2 & 15, i3) != i4) {
				return false;
			} else {
				extendedBlockStorage9.setExtBlockMetadata(i1, i2 & 15, i3, i5);
				if(z10) {
					this.generateSkylightMap();
				} else {
					if(Block.lightOpacity[i4 & 4095] > 0) {
						if(i2 >= i7) {
							this.relightBlock(i1, i2 + 1, i3);
						}
					} else if(i2 == i7 - 1) {
						this.relightBlock(i1, i2, i3);
					}

					this.propagateSkylightOcclusion(i1, i3);
				}

				TileEntity tileEntity13;
				if(i4 != 0) {
					if(!this.worldObj.isRemote) {
						Block.blocksList[i4].onBlockAdded(this.worldObj, i11, i2, i12);
					}

					if(Block.blocksList[i4] instanceof BlockContainer) {
						tileEntity13 = this.getChunkBlockTileEntity(i1, i2, i3);
						if(tileEntity13 == null) {
							tileEntity13 = ((BlockContainer)Block.blocksList[i4]).getBlockEntity();
							this.worldObj.setBlockTileEntity(i11, i2, i12, tileEntity13);
						}

						if(tileEntity13 != null) {
							tileEntity13.updateContainingBlockInfo();
						}
					}
				} else if(i8 > 0 && Block.blocksList[i8] instanceof BlockContainer) {
					tileEntity13 = this.getChunkBlockTileEntity(i1, i2, i3);
					if(tileEntity13 != null) {
						tileEntity13.updateContainingBlockInfo();
					}
				}

				this.isModified = true;
				return true;
			}
		}
	}

	public boolean setBlockMetadata(int i1, int i2, int i3, int i4) {
		ExtendedBlockStorage extendedBlockStorage5 = this.storageArrays[i2 >> 4];
		if(extendedBlockStorage5 == null) {
			return false;
		} else {
			int i6 = extendedBlockStorage5.getExtBlockMetadata(i1, i2 & 15, i3);
			if(i6 == i4) {
				return false;
			} else {
				this.isModified = true;
				extendedBlockStorage5.setExtBlockMetadata(i1, i2 & 15, i3, i4);
				int i7 = extendedBlockStorage5.getExtBlockID(i1, i2 & 15, i3);
				if(i7 > 0 && Block.blocksList[i7] instanceof BlockContainer) {
					TileEntity tileEntity8 = this.getChunkBlockTileEntity(i1, i2, i3);
					if(tileEntity8 != null) {
						tileEntity8.updateContainingBlockInfo();
						tileEntity8.blockMetadata = i4;
					}
				}

				return true;
			}
		}
	}

	public int getSavedLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4) {
		ExtendedBlockStorage extendedBlockStorage5 = this.storageArrays[i3 >> 4];
		return extendedBlockStorage5 == null ? enumSkyBlock1.defaultLightValue : (enumSkyBlock1 == EnumSkyBlock.Sky ? extendedBlockStorage5.getExtSkylightValue(i2, i3 & 15, i4) : (enumSkyBlock1 == EnumSkyBlock.Block ? extendedBlockStorage5.getExtBlocklightValue(i2, i3 & 15, i4) : enumSkyBlock1.defaultLightValue));
	}

	public void setLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5) {
		ExtendedBlockStorage extendedBlockStorage6 = this.storageArrays[i3 >> 4];
		if(extendedBlockStorage6 == null) {
			extendedBlockStorage6 = this.storageArrays[i3 >> 4] = new ExtendedBlockStorage(i3 >> 4 << 4);
			this.generateSkylightMap();
		}

		this.isModified = true;
		if(enumSkyBlock1 == EnumSkyBlock.Sky) {
			if(!this.worldObj.worldProvider.hasNoSky) {
				extendedBlockStorage6.setExtSkylightValue(i2, i3 & 15, i4, i5);
			}
		} else {
			if(enumSkyBlock1 != EnumSkyBlock.Block) {
				return;
			}

			extendedBlockStorage6.setExtBlocklightValue(i2, i3 & 15, i4, i5);
		}

	}

	public int getBlockLightValue(int i1, int i2, int i3, int i4) {
		ExtendedBlockStorage extendedBlockStorage5 = this.storageArrays[i2 >> 4];
		if(extendedBlockStorage5 == null) {
			return !this.worldObj.worldProvider.hasNoSky && i4 < EnumSkyBlock.Sky.defaultLightValue ? EnumSkyBlock.Sky.defaultLightValue - i4 : 0;
		} else {
			int i6 = this.worldObj.worldProvider.hasNoSky ? 0 : extendedBlockStorage5.getExtSkylightValue(i1, i2 & 15, i3);
			if(i6 > 0) {
				isLit = true;
			}

			i6 -= i4;
			int i7 = extendedBlockStorage5.getExtBlocklightValue(i1, i2 & 15, i3);
			if(i7 > i6) {
				i6 = i7;
			}

			return i6;
		}
	}

	public void addEntity(Entity entity1) {
		this.hasEntities = true;
		int i2 = MathHelper.floor_double(entity1.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity1.posZ / 16.0D);
		if(i2 != this.xPosition || i3 != this.zPosition) {
			System.out.println("Wrong location! " + entity1);
			Thread.dumpStack();
		}

		int i4 = MathHelper.floor_double(entity1.posY / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i4 >= this.entityLists.length) {
			i4 = this.entityLists.length - 1;
		}

		entity1.addedToChunk = true;
		entity1.chunkCoordX = this.xPosition;
		entity1.chunkCoordY = i4;
		entity1.chunkCoordZ = this.zPosition;
		this.entityLists[i4].add(entity1);
	}

	public void removeEntity(Entity entity1) {
		this.removeEntityAtIndex(entity1, entity1.chunkCoordY);
	}

	public void removeEntityAtIndex(Entity entity1, int i2) {
		if(i2 < 0) {
			i2 = 0;
		}

		if(i2 >= this.entityLists.length) {
			i2 = this.entityLists.length - 1;
		}

		this.entityLists[i2].remove(entity1);
	}

	public boolean canBlockSeeTheSky(int i1, int i2, int i3) {
		return i2 >= this.heightMap[i3 << 4 | i1];
	}

	public TileEntity getChunkBlockTileEntity(int i1, int i2, int i3) {
		ChunkPosition chunkPosition4 = new ChunkPosition(i1, i2, i3);
		TileEntity tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(chunkPosition4);
		if(tileEntity5 == null) {
			int i6 = this.getBlockID(i1, i2, i3);
			if(i6 <= 0 || !Block.blocksList[i6].hasTileEntity()) {
				return null;
			}

			if(tileEntity5 == null) {
				tileEntity5 = ((BlockContainer)Block.blocksList[i6]).getBlockEntity();
				this.worldObj.setBlockTileEntity(this.xPosition * 16 + i1, i2, this.zPosition * 16 + i3, tileEntity5);
			}

			tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(chunkPosition4);
		}

		if(tileEntity5 != null && tileEntity5.isInvalid()) {
			this.chunkTileEntityMap.remove(chunkPosition4);
			return null;
		} else {
			return tileEntity5;
		}
	}

	public void addTileEntity(TileEntity tileEntity1) {
		int i2 = tileEntity1.xCoord - this.xPosition * 16;
		int i3 = tileEntity1.yCoord;
		int i4 = tileEntity1.zCoord - this.zPosition * 16;
		this.setChunkBlockTileEntity(i2, i3, i4, tileEntity1);
		if(this.isChunkLoaded) {
			this.worldObj.loadedTileEntityList.add(tileEntity1);
		}

	}

	public void setChunkBlockTileEntity(int i1, int i2, int i3, TileEntity tileEntity4) {
		ChunkPosition chunkPosition5 = new ChunkPosition(i1, i2, i3);
		tileEntity4.worldObj = this.worldObj;
		tileEntity4.xCoord = this.xPosition * 16 + i1;
		tileEntity4.yCoord = i2;
		tileEntity4.zCoord = this.zPosition * 16 + i3;
		if(this.getBlockID(i1, i2, i3) != 0 && Block.blocksList[this.getBlockID(i1, i2, i3)] instanceof BlockContainer) {
			tileEntity4.validate();
			this.chunkTileEntityMap.put(chunkPosition5, tileEntity4);
		}
	}

	public void removeChunkBlockTileEntity(int i1, int i2, int i3) {
		ChunkPosition chunkPosition4 = new ChunkPosition(i1, i2, i3);
		if(this.isChunkLoaded) {
			TileEntity tileEntity5 = (TileEntity)this.chunkTileEntityMap.remove(chunkPosition4);
			if(tileEntity5 != null) {
				tileEntity5.invalidate();
			}
		}

	}

	public void onChunkLoad() {
		this.isChunkLoaded = true;
		this.worldObj.addTileEntity(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entityLists.length; ++i1) {
			this.worldObj.addLoadedEntities(this.entityLists[i1]);
		}

	}

	public void onChunkUnload() {
		this.isChunkLoaded = false;
		Iterator iterator1 = this.chunkTileEntityMap.values().iterator();

		while(iterator1.hasNext()) {
			TileEntity tileEntity2 = (TileEntity)iterator1.next();
			this.worldObj.markTileEntityForDespawn(tileEntity2);
		}

		for(int i3 = 0; i3 < this.entityLists.length; ++i3) {
			this.worldObj.unloadEntities(this.entityLists[i3]);
		}

	}

	public void setChunkModified() {
		this.isModified = true;
	}

	public void getEntitiesWithinAABBForEntity(Entity entity1, AxisAlignedBB axisAlignedBB2, List list3) {
		int i4 = MathHelper.floor_double((axisAlignedBB2.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entityLists.length) {
			i5 = this.entityLists.length - 1;
		}

		for(int i6 = i4; i6 <= i5; ++i6) {
			List list7 = this.entityLists[i6];

			for(int i8 = 0; i8 < list7.size(); ++i8) {
				Entity entity9 = (Entity)list7.get(i8);
				if(entity9 != entity1 && entity9.boundingBox.intersectsWith(axisAlignedBB2)) {
					list3.add(entity9);
					Entity[] entity10 = entity9.getParts();
					if(entity10 != null) {
						for(int i11 = 0; i11 < entity10.length; ++i11) {
							entity9 = entity10[i11];
							if(entity9 != entity1 && entity9.boundingBox.intersectsWith(axisAlignedBB2)) {
								list3.add(entity9);
							}
						}
					}
				}
			}
		}

	}

	public void getEntitiesOfTypeWithinAAAB(Class class1, AxisAlignedBB axisAlignedBB2, List list3) {
		int i4 = MathHelper.floor_double((axisAlignedBB2.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		} else if(i4 >= this.entityLists.length) {
			i4 = this.entityLists.length - 1;
		}

		if(i5 >= this.entityLists.length) {
			i5 = this.entityLists.length - 1;
		} else if(i5 < 0) {
			i5 = 0;
		}

		for(int i6 = i4; i6 <= i5; ++i6) {
			List list7 = this.entityLists[i6];

			for(int i8 = 0; i8 < list7.size(); ++i8) {
				Entity entity9 = (Entity)list7.get(i8);
				if(class1.isAssignableFrom(entity9.getClass()) && entity9.boundingBox.intersectsWith(axisAlignedBB2)) {
					list3.add(entity9);
				}
			}
		}

	}

	public boolean needsSaving(boolean z1) {
		if(z1) {
			if(this.hasEntities && this.worldObj.getWorldTime() != this.lastSaveTime) {
				return true;
			}
		} else if(this.hasEntities && this.worldObj.getWorldTime() >= this.lastSaveTime + 600L) {
			return true;
		}

		return this.isModified;
	}

	public Random getRandomWithSeed(long j1) {
		return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ j1);
	}

	public boolean isEmpty() {
		return false;
	}

	public void removeUnknownBlocks() {
		ExtendedBlockStorage[] extendedBlockStorage1 = this.storageArrays;
		int i2 = extendedBlockStorage1.length;

		for(int i3 = 0; i3 < i2; ++i3) {
			ExtendedBlockStorage extendedBlockStorage4 = extendedBlockStorage1[i3];
			if(extendedBlockStorage4 != null) {
				extendedBlockStorage4.func_48711_e();
			}
		}

	}

	public void populateChunk(IChunkProvider iChunkProvider1, IChunkProvider iChunkProvider2, int i3, int i4) {
		if(!this.isTerrainPopulated && iChunkProvider1.chunkExists(i3 + 1, i4 + 1) && iChunkProvider1.chunkExists(i3, i4 + 1) && iChunkProvider1.chunkExists(i3 + 1, i4)) {
			iChunkProvider1.populate(iChunkProvider2, i3, i4);
		}

		if(iChunkProvider1.chunkExists(i3 - 1, i4) && !iChunkProvider1.provideChunk(i3 - 1, i4).isTerrainPopulated && iChunkProvider1.chunkExists(i3 - 1, i4 + 1) && iChunkProvider1.chunkExists(i3, i4 + 1) && iChunkProvider1.chunkExists(i3 - 1, i4 + 1)) {
			iChunkProvider1.populate(iChunkProvider2, i3 - 1, i4);
		}

		if(iChunkProvider1.chunkExists(i3, i4 - 1) && !iChunkProvider1.provideChunk(i3, i4 - 1).isTerrainPopulated && iChunkProvider1.chunkExists(i3 + 1, i4 - 1) && iChunkProvider1.chunkExists(i3 + 1, i4 - 1) && iChunkProvider1.chunkExists(i3 + 1, i4)) {
			iChunkProvider1.populate(iChunkProvider2, i3, i4 - 1);
		}

		if(iChunkProvider1.chunkExists(i3 - 1, i4 - 1) && !iChunkProvider1.provideChunk(i3 - 1, i4 - 1).isTerrainPopulated && iChunkProvider1.chunkExists(i3, i4 - 1) && iChunkProvider1.chunkExists(i3 - 1, i4)) {
			iChunkProvider1.populate(iChunkProvider2, i3 - 1, i4 - 1);
		}

	}

	public int getPrecipitationHeight(int i1, int i2) {
		int i3 = i1 | i2 << 4;
		int i4 = this.precipitationHeightMap[i3];
		if(i4 == -999) {
			int i5 = this.getTopFilledSegment() + 15;
			i4 = -1;

			while(true) {
				while(i5 > 0 && i4 == -1) {
					int i6 = this.getBlockID(i1, i5, i2);
					Material material7 = i6 == 0 ? Material.air : Block.blocksList[i6].blockMaterial;
					if(!material7.blocksMovement() && !material7.isLiquid()) {
						--i5;
					} else {
						i4 = i5 + 1;
					}
				}

				this.precipitationHeightMap[i3] = i4;
				break;
			}
		}

		return i4;
	}

	public void updateSkylight() {
		if(this.isGapLightingUpdated && !this.worldObj.worldProvider.hasNoSky) {
			this.updateSkylight_do();
		}

	}

	public ChunkCoordIntPair getChunkCoordIntPair() {
		return new ChunkCoordIntPair(this.xPosition, this.zPosition);
	}

	public boolean getAreLevelsEmpty(int i1, int i2) {
		if(i1 < 0) {
			i1 = 0;
		}

		if(i2 >= 256) {
			i2 = 255;
		}

		for(int i3 = i1; i3 <= i2; i3 += 16) {
			ExtendedBlockStorage extendedBlockStorage4 = this.storageArrays[i3 >> 4];
			if(extendedBlockStorage4 != null && !extendedBlockStorage4.getIsEmpty()) {
				return false;
			}
		}

		return true;
	}

	public void setStorageArrays(ExtendedBlockStorage[] extendedBlockStorage1) {
		this.storageArrays = extendedBlockStorage1;
	}

	public void func_48494_a(byte[] b1, int i2, int i3, boolean z4) {
		int i5 = 0;

		int i6;
		for(i6 = 0; i6 < this.storageArrays.length; ++i6) {
			if((i2 & 1 << i6) != 0) {
				if(this.storageArrays[i6] == null) {
					this.storageArrays[i6] = new ExtendedBlockStorage(i6 << 4);
				}

				byte[] b7 = this.storageArrays[i6].func_48692_g();
				System.arraycopy(b1, i5, b7, 0, b7.length);
				i5 += b7.length;
			} else if(z4 && this.storageArrays[i6] != null) {
				this.storageArrays[i6] = null;
			}
		}

		NibbleArray nibbleArray8;
		for(i6 = 0; i6 < this.storageArrays.length; ++i6) {
			if((i2 & 1 << i6) != 0 && this.storageArrays[i6] != null) {
				nibbleArray8 = this.storageArrays[i6].func_48697_j();
				System.arraycopy(b1, i5, nibbleArray8.data, 0, nibbleArray8.data.length);
				i5 += nibbleArray8.data.length;
			}
		}

		for(i6 = 0; i6 < this.storageArrays.length; ++i6) {
			if((i2 & 1 << i6) != 0 && this.storageArrays[i6] != null) {
				nibbleArray8 = this.storageArrays[i6].getBlocklightArray();
				System.arraycopy(b1, i5, nibbleArray8.data, 0, nibbleArray8.data.length);
				i5 += nibbleArray8.data.length;
			}
		}

		for(i6 = 0; i6 < this.storageArrays.length; ++i6) {
			if((i2 & 1 << i6) != 0 && this.storageArrays[i6] != null) {
				nibbleArray8 = this.storageArrays[i6].getSkylightArray();
				System.arraycopy(b1, i5, nibbleArray8.data, 0, nibbleArray8.data.length);
				i5 += nibbleArray8.data.length;
			}
		}

		for(i6 = 0; i6 < this.storageArrays.length; ++i6) {
			if((i3 & 1 << i6) != 0) {
				if(this.storageArrays[i6] == null) {
					i5 += 2048;
				} else {
					nibbleArray8 = this.storageArrays[i6].getBlockMSBArray();
					if(nibbleArray8 == null) {
						nibbleArray8 = this.storageArrays[i6].createBlockMSBArray();
					}

					System.arraycopy(b1, i5, nibbleArray8.data, 0, nibbleArray8.data.length);
					i5 += nibbleArray8.data.length;
				}
			} else if(z4 && this.storageArrays[i6] != null && this.storageArrays[i6].getBlockMSBArray() != null) {
				this.storageArrays[i6].func_48715_h();
			}
		}

		if(z4) {
			System.arraycopy(b1, i5, this.blockBiomeArray, 0, this.blockBiomeArray.length);
			int i10000 = i5 + this.blockBiomeArray.length;
		}

		for(i6 = 0; i6 < this.storageArrays.length; ++i6) {
			if(this.storageArrays[i6] != null && (i2 & 1 << i6) != 0) {
				this.storageArrays[i6].func_48708_d();
			}
		}

		this.generateHeightMap();
		Iterator iterator10 = this.chunkTileEntityMap.values().iterator();

		while(iterator10.hasNext()) {
			TileEntity tileEntity9 = (TileEntity)iterator10.next();
			tileEntity9.updateContainingBlockInfo();
		}

	}

	public BiomeGenBase func_48490_a(int i1, int i2, WorldChunkManager worldChunkManager3) {
		int i4 = this.blockBiomeArray[i2 << 4 | i1] & 255;
		if(i4 == 255) {
			BiomeGenBase biomeGenBase5 = worldChunkManager3.getBiomeGenAt((this.xPosition << 4) + i1, (this.zPosition << 4) + i2);
			i4 = biomeGenBase5.biomeID;
			this.blockBiomeArray[i2 << 4 | i1] = (byte)(i4 & 255);
		}

		return BiomeGenBase.biomeList[i4] == null ? BiomeGenBase.plains : BiomeGenBase.biomeList[i4];
	}

	public byte[] getBiomeArray() {
		return this.blockBiomeArray;
	}

	public void setBiomeArray(byte[] b1) {
		this.blockBiomeArray = b1;
	}

	public void resetRelightChecks() {
		this.queuedLightChecks = 0;
	}

	public void enqueueRelightChecks() {
		for(int i1 = 0; i1 < 8; ++i1) {
			if(this.queuedLightChecks >= 4096) {
				return;
			}

			int i2 = this.queuedLightChecks % 16;
			int i3 = this.queuedLightChecks / 16 % 16;
			int i4 = this.queuedLightChecks / 256;
			++this.queuedLightChecks;
			int i5 = (this.xPosition << 4) + i3;
			int i6 = (this.zPosition << 4) + i4;

			for(int i7 = 0; i7 < 16; ++i7) {
				int i8 = (i2 << 4) + i7;
				if(this.storageArrays[i2] == null && (i7 == 0 || i7 == 15 || i3 == 0 || i3 == 15 || i4 == 0 || i4 == 15) || this.storageArrays[i2] != null && this.storageArrays[i2].getExtBlockID(i3, i7, i4) == 0) {
					if(Block.lightValue[this.worldObj.getBlockId(i5, i8 - 1, i6)] > 0) {
						this.worldObj.updateAllLightTypes(i5, i8 - 1, i6);
					}

					if(Block.lightValue[this.worldObj.getBlockId(i5, i8 + 1, i6)] > 0) {
						this.worldObj.updateAllLightTypes(i5, i8 + 1, i6);
					}

					if(Block.lightValue[this.worldObj.getBlockId(i5 - 1, i8, i6)] > 0) {
						this.worldObj.updateAllLightTypes(i5 - 1, i8, i6);
					}

					if(Block.lightValue[this.worldObj.getBlockId(i5 + 1, i8, i6)] > 0) {
						this.worldObj.updateAllLightTypes(i5 + 1, i8, i6);
					}

					if(Block.lightValue[this.worldObj.getBlockId(i5, i8, i6 - 1)] > 0) {
						this.worldObj.updateAllLightTypes(i5, i8, i6 - 1);
					}

					if(Block.lightValue[this.worldObj.getBlockId(i5, i8, i6 + 1)] > 0) {
						this.worldObj.updateAllLightTypes(i5, i8, i6 + 1);
					}

					this.worldObj.updateAllLightTypes(i5, i8, i6);
				}
			}
		}

	}
}

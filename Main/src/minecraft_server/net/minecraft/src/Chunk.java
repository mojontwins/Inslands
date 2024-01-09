package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Chunk {
	public static boolean isLit;
	public byte[] blocks;
	public boolean isChunkLoaded;
	public World worldObj;
	public NibbleArray data;
	public NibbleArray skylightMap;
	public NibbleArray blocklightMap;
	public byte[] heightMap;
	public byte[] landSurfaceHeightMap;
	public int lowestBlockHeight;
	public final int xPosition;
	public final int zPosition;
	public Map<ChunkPosition,TileEntity> chunkTileEntityMap;
	public Map<ChunkPosition, EntityBlockEntity> chunkSpecialEntityMap;
	public List<Entity>[] entities;
	public boolean isTerrainPopulated;
	public boolean isModified;
	public boolean neverSave;
	public boolean hasEntities;
	public long lastSaveTime;
	public BiomeGenBase [] biomeGenCache = null;

	@SuppressWarnings("unchecked")
	public Chunk(World world1, int i2, int i3) {
		this.chunkTileEntityMap = new HashMap<ChunkPosition, TileEntity>();
		this.chunkSpecialEntityMap = new HashMap<ChunkPosition, EntityBlockEntity>();
		this.entities = (List<Entity> []) new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
		this.lastSaveTime = 0L;
		this.worldObj = world1;
		this.xPosition = i2;
		this.zPosition = i3;
		this.heightMap = new byte[256];
		this.landSurfaceHeightMap = new byte[256];

		for(int i4 = 0; i4 < this.entities.length; ++i4) {
			this.entities[i4] = new ArrayList<Entity>();
		}

	}

	public Chunk(World world1, byte[] b2, int i3, int i4) {
		this(world1, i3, i4);
		this.blocks = b2;
		this.data = new NibbleArray(b2.length);
		this.skylightMap = new NibbleArray(b2.length);
		this.blocklightMap = new NibbleArray(b2.length);
	}

	public boolean isAtLocation(int i1, int i2) {
		return i1 == this.xPosition && i2 == this.zPosition;
	}

	public int getHeightValue(int i1, int i2) {
		return this.heightMap[i2 << 4 | i1] & 255;
	}

	public int getLandSurfaceHeightValue(int i1, int i2) {
		return this.landSurfaceHeightMap[i2 << 4 | i1] & 255;
	}
	
	public void setLandSurfaceHeightValue(int i1, int i2, int value) {
		this.landSurfaceHeightMap[i2 << 4 | i1] = (byte)value;
	}

	public void func_1014_a() {
	}

	public void generateHeightMap() {
		int i1 = 127;

		for(int i2 = 0; i2 < 16; ++i2) {
			for(int i3 = 0; i3 < 16; ++i3) {
				int i4 = 127;

				for(int i5 = i2 << 11 | i3 << 7; i4 > 0 && Block.lightOpacity[this.blocks[i5 + i4 - 1] & 255] == 0; --i4) {
				}

				this.heightMap[i3 << 4 | i2] = (byte)i4;
				if(i4 < i1) {
					i1 = i4;
				}
			}
		}

		this.lowestBlockHeight = i1;
		this.isModified = true;
	}

	public void generateLandSurfaceHeightMap() {
		int index = 0;
		for(int z = 0; z < 16; z ++) {
			for(int x = 0; x < 16; x ++) {
				int y = 127;
				int baseColumn = x << 11 | z << 7 | y;
				while(this.blocks[baseColumn] != Block.stone.blockID && y > 0) { y --; baseColumn --; }
				this.landSurfaceHeightMap[index ++] = (byte)y;
			}
		}
	}
	
	public void generateSkylightMapSimple() {
		int i1 = 127;

		int i2;
		int i3;
		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				int i4 = 127;

				int i5;
				for(i5 = i2 << 11 | i3 << 7; i4 > 0 && Block.lightOpacity[this.blocks[i5 + i4 - 1] & 255] == 0; --i4) {
				}

				this.heightMap[i3 << 4 | i2] = (byte)i4;
				if(i4 < i1) {
					i1 = i4;
				}

				if(!this.worldObj.worldProvider.hasNoSky) {
					int i6 = 15;
					int i7 = 127;

					do {
						i6 -= Block.lightOpacity[this.blocks[i5 + i7] & 255];
						if(i6 > 0) {
							this.skylightMap.setNibble(i2, i7, i3, i6);
						}

						--i7;
					} while(i7 > 0 && i6 > 0);
				}
			}
		}

		this.lowestBlockHeight = i1;
	}

	public void generateSkylightMap() {
		int i1 = 127;

		int i2;
		int i3;
		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				int i4 = 127;

				int i5;
				for(i5 = i2 << 11 | i3 << 7; i4 > 0 && Block.lightOpacity[this.blocks[i5 + i4 - 1] & 255] == 0; --i4) {
				}

				this.heightMap[i3 << 4 | i2] = (byte)i4;
				if(i4 < i1) {
					i1 = i4;
				}

				if(!this.worldObj.worldProvider.hasNoSky) {
					int i6 = 15;
					int i7 = 127;

					do {
						i6 -= Block.lightOpacity[this.blocks[i5 + i7] & 255];
						if(i6 > 0) {
							this.skylightMap.setNibble(i2, i7, i3, i6);
						}

						--i7;
					} while(i7 > 0 && i6 > 0);
				}
			}
		}

		this.lowestBlockHeight = i1;

		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.propagateSkylightOcclusion(i2, i3);
			}
		}

		this.isModified = true;
	}

	public void doesNothing() {
	}

	private void propagateSkylightOcclusion(int i1, int i2) {
		int i3 = this.getHeightValue(i1, i2);
		int i4 = this.xPosition * 16 + i1;
		int i5 = this.zPosition * 16 + i2;
		this.func_1020_f(i4 - 1, i5, i3);
		this.func_1020_f(i4 + 1, i5, i3);
		this.func_1020_f(i4, i5 - 1, i3);
		this.func_1020_f(i4, i5 + 1, i3);
	}

	private void func_1020_f(int i1, int i2, int i3) {
		int i4 = this.worldObj.getHeightValue(i1, i2);
		if(i4 > i3) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i1, i3, i2, i1, i4, i2);
			this.isModified = true;
		} else if(i4 < i3) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i1, i4, i2, i1, i3, i2);
			this.isModified = true;
		}

	}

	private void relightBlock(int i1, int i2, int i3) {
		int i4 = this.heightMap[i3 << 4 | i1] & 255;
		int i5 = i4;
		if(i2 > i4) {
			i5 = i2;
		}

		for(int i6 = i1 << 11 | i3 << 7; i5 > 0 && Block.lightOpacity[this.blocks[i6 + i5 - 1] & 255] == 0; --i5) {
		}

		if(i5 != i4) {
			this.worldObj.markBlocksDirtyVertical(i1, i3, i5, i4);
			this.heightMap[i3 << 4 | i1] = (byte)i5;
			int i7;
			int i8;
			int i9;
			if(i5 < this.lowestBlockHeight) {
				this.lowestBlockHeight = i5;
			} else {
				i7 = 127;

				for(i8 = 0; i8 < 16; ++i8) {
					for(i9 = 0; i9 < 16; ++i9) {
						if((this.heightMap[i9 << 4 | i8] & 255) < i7) {
							i7 = this.heightMap[i9 << 4 | i8] & 255;
						}
					}
				}

				this.lowestBlockHeight = i7;
			}

			i7 = this.xPosition * 16 + i1;
			i8 = this.zPosition * 16 + i3;
			if(i5 < i4) {
				for(i9 = i5; i9 < i4; ++i9) {
					this.skylightMap.setNibble(i1, i9, i3, 15);
				}
			} else {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i7, i4, i8, i7, i5, i8);

				for(i9 = i4; i9 < i5; ++i9) {
					this.skylightMap.setNibble(i1, i9, i3, 0);
				}
			}

			i9 = 15;

			int i10;
			for(i10 = i5; i5 > 0 && i9 > 0; this.skylightMap.setNibble(i1, i5, i3, i9)) {
				--i5;
				int i11 = Block.lightOpacity[this.getBlockID(i1, i5, i3)];
				if(i11 == 0) {
					i11 = 1;
				}

				i9 -= i11;
				if(i9 < 0) {
					i9 = 0;
				}
			}

			while(i5 > 0 && Block.lightOpacity[this.getBlockID(i1, i5 - 1, i3)] == 0) {
				--i5;
			}

			if(i5 != i10) {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i7 - 1, i5, i8 - 1, i7 + 1, i10, i8 + 1);
			}

			this.isModified = true;
		}
	}

	public int getBlockID(int x, int y, int z) {
		return (int) this.blocks[x << 11 | z << 7 | y] & 0xff;
	}

	public boolean setBlockIDWithMetadata(int x, int y, int z, int id, int metadata) {
		byte b6 = (byte)id;
		int i7 = this.heightMap[z << 4 | x] & 255;
		int i8 = this.blocks[x << 11 | z << 7 | y] & 255;
		if(i8 == id && this.data.getNibble(x, y, z) == metadata) {
			return false;
		} else {
			int i9 = (this.xPosition << 4) | x;
			int x0 = (this.zPosition << 4) | z;
			
			Block block = Block.blocksList[i8];
			this.blocks[x << 11 | z << 7 | y] = (byte)(b6 & 255);
			if(block != null && !this.worldObj.multiplayerWorld) {
				block.onBlockRemoval(this.worldObj, i9, y, x0);
			}

			this.data.setNibble(x, y, z, metadata);
			if(!this.worldObj.worldProvider.hasNoSky) {
				if(Block.lightOpacity[b6 & 255] != 0) {
					if(y >= i7) {
						this.relightBlock(x, y + 1, z);
					}
				} else if(y == i7 - 1) {
					this.relightBlock(x, y, z);
				}

				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i9, y, x0, i9, y, x0);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, i9, y, x0, i9, y, x0);
			this.propagateSkylightOcclusion(x, z);
			this.data.setNibble(x, y, z, metadata);
			
			block = Block.blocksList[id];
			if(block != null) {
				block.onBlockAdded(this.worldObj, i9, y, x0);
			}

			this.isModified = true;
			return true;
		}
	}

	public boolean setBlockIDColumn(int x, int y, int z, int[] id) {
		// Column is bottom to top ordered
		
		int xWorld = (this.xPosition << 4) | x;
		int zWorld = (this.zPosition << 4) | z;
		
		int height = this.heightMap[z << 4 | x] & 255;
		
		int index = x << 11 | z << 7 | y;
		int y0 = y;
		
		// Write blocks
		for(int i = 0; i < id.length; i ++) {
			int b = id[i];
			if(b >= 0) {
				this.blocks[index ++] = (byte) b;
				this.data.setNibble(x, y ++, z, 0);
			} else {
				// A negative value is the count for a run
				int c = -b;
				i ++;
				b = id[i];
				while (c -- > 0) {
					this.blocks[index ++] = (byte) b;
					this.data.setNibble(x, y ++, z, 0);
				}
			}
			
			if(y == 128) break;
		}
		
		// The topmost block
		y --;
		
		// Relight top
		if (y >= height) this.relightBlock(x, y + 1, z);
		
		this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, xWorld, y0, zWorld, xWorld, y, zWorld);
		this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, xWorld, y0, zWorld, xWorld, y, zWorld);
		this.propagateSkylightOcclusion(x, z);
		
		this.isModified = true;
		
		return true;
	}
	
	public boolean setBlockIDAndMetadataColumn(int x, int y, int z, int[] id) {
		// Column is bottom to top ordered
		// Metadata is encoded as a most significant byte

		int xWorld = (this.xPosition << 4) | x;
		int zWorld = (this.zPosition << 4) | z;
		
		int height = this.heightMap[z << 4 | x] & 255;
		
		int index = x << 11 | z << 7 | y;
		int y0 = y;
		
		// Write blocks
		for(int i = 0; i < id.length; i ++) {
			int b = id[i];
			if(b >= 0) {
				this.data.setNibble(x, y ++, z, (b >> 8) & 15);
				this.blocks[index ++] = (byte) (b & 255);
			} else {
				// A negative value is the count for a run
				int c = -b;
				i ++;
				b = id[i];
				byte m = (byte) ((b >> 8) & 15);
				byte b0 = (byte) (b & 255);
				while (c -- > 0) {
					this.data.setNibble(x, y ++, z, m);
					this.blocks[index ++] = b0;
				}
			}
			if(y == 128) break;
		}
		
		// The topmost block
		y --;
		
		// Relight top
		if (y >= height) this.relightBlock(x, y + 1, z);
		
		this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, xWorld, y0, zWorld, xWorld, y, zWorld);
		this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, xWorld, y0, zWorld, xWorld, y, zWorld);
		this.propagateSkylightOcclusion(x, z);
		
		this.isModified = true;
		
		return true;
	}

	public boolean setBlockID(int x, int y, int z, int id) {
		byte b5 = (byte)id;
		int i6 = this.heightMap[z << 4 | x] & 255;
		int i7 = this.blocks[x << 11 | z << 7 | y] & 255;
		if(i7 == id) {
			return false;
		} else {
			int i8 = (this.xPosition << 4) | x;
			int i9 = (this.zPosition << 4) | z;
			this.blocks[x << 11 | z << 7 | y] = b5;
			
			Block block = Block.blocksList[i7];
			if(block != null) {
				block.onBlockRemoval(this.worldObj, i8, y, i9);
			}

			this.data.setNibble(x, y, z, 0);
			if(Block.lightOpacity[id] != 0) {
				if(y >= i6) {
					this.relightBlock(x, y + 1, z);
				}
			} else if(y == i6 - 1) {
				this.relightBlock(x, y, z);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i8, y, i9, i8, y, i9);
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, i8, y, i9, i8, y, i9);
			this.propagateSkylightOcclusion(x, z);
			
			block = Block.blocksList[id];
			if(block != null && !this.worldObj.multiplayerWorld) {
				block.onBlockAdded(this.worldObj, i8, y, i9);
			}

			this.isModified = true;
			return true;
		}
	}

	public int getBlockMetadata(int i1, int i2, int i3) {
		return this.data.getNibble(i1, i2, i3);
	}

	public void setBlockMetadata(int i1, int i2, int i3, int i4) {
		this.isModified = true;
		this.data.setNibble(i1, i2, i3, i4);
	}

	public int getSavedLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4) {
		return enumSkyBlock1 == EnumSkyBlock.Sky ? this.skylightMap.getNibble(i2, i3, i4) : (enumSkyBlock1 == EnumSkyBlock.Block ? this.blocklightMap.getNibble(i2, i3, i4) : 0);
	}

	public void setLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5) {
		this.isModified = true;
		if(enumSkyBlock1 == EnumSkyBlock.Sky) {
			this.skylightMap.setNibble(i2, i3, i4, i5);
		} else {
			if(enumSkyBlock1 != EnumSkyBlock.Block) {
				return;
			}

			this.blocklightMap.setNibble(i2, i3, i4, i5);
		}

	}

	public int getBlockLightValue(int i1, int i2, int i3, int i4) {
		int i5 = this.skylightMap.getNibble(i1, i2, i3);
		if(i5 > 0) {
			isLit = true;
		}

		i5 -= i4;
		int i6 = this.blocklightMap.getNibble(i1, i2, i3);
		if(i6 > i5) {
			i5 = i6;
		}

		return i5;
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

		if(i4 >= this.entities.length) {
			i4 = this.entities.length - 1;
		}

		entity1.addedToChunk = true;
		entity1.chunkCoordX = this.xPosition;
		entity1.chunkCoordY = i4;
		entity1.chunkCoordZ = this.zPosition;
		this.entities[i4].add(entity1);
	}

	public void removeEntity(Entity entity1) {
		this.removeEntityAtIndex(entity1, entity1.chunkCoordY);
	}

	public void removeEntityAtIndex(Entity entity1, int i2) {
		if(i2 < 0) {
			i2 = 0;
		}

		if(i2 >= this.entities.length) {
			i2 = this.entities.length - 1;
		}

		this.entities[i2].remove(entity1);
	}

	public boolean canBlockSeeTheSky(int i1, int i2, int i3) {
		return i2 >= (this.heightMap[i3 << 4 | i1] & 255);
	}

	public TileEntity getChunkBlockTileEntity(int i1, int i2, int i3) {
		ChunkPosition chunkPosition4 = new ChunkPosition(i1, i2, i3);
		TileEntity tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(chunkPosition4);
		if(tileEntity5 == null) {
			int i6 = this.getBlockID(i1, i2, i3);
			if(!Block.isBlockContainer[i6]) {
				return null;
			}

			BlockContainer blockContainer7 = (BlockContainer)Block.blocksList[i6];
			blockContainer7.onBlockAdded(this.worldObj, this.xPosition * 16 + i1, i2, this.zPosition * 16 + i3);
			tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(chunkPosition4);
		}

		if(tileEntity5 != null && tileEntity5.isInvalid()) {
			this.chunkTileEntityMap.remove(chunkPosition4);
			return null;
		} else {
			return tileEntity5;
		}
	}

	public EntityBlockEntity getChunkBlockEntity(int x, int y, int z) {
		ChunkPosition chunkPosition = new ChunkPosition(x, y, z);
		EntityBlockEntity entity = this.chunkSpecialEntityMap.get(chunkPosition);
		if(entity == null) {
			Block block = Block.blocksList[this.getBlockID(x, y, z)];
			if(block == null || !(block instanceof BlockEntity)) {
				return null;
			}
			
			BlockEntity blockEntity = (BlockEntity)block;
			blockEntity.onBlockAdded(this.worldObj, this.xPosition * 16 + x, y, this.zPosition * 16 + z);
			entity = this.chunkSpecialEntityMap.get(chunkPosition);
		}
		
		return entity;
	}
		
	public EntityBlockEntity getChunkBlockEntityIfExists(int x, int y, int z) {
		ChunkPosition chunkPosition = new ChunkPosition(x, y, z);
		EntityBlockEntity entity = this.chunkSpecialEntityMap.get(chunkPosition);
		
		return entity;
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
	
	public void addSpecialEntity(EntityBlockEntity entity) {
		int x = entity.xTile - this.xPosition * 16;;
		int y = entity.yTile;
		int z = entity.zTile - this.zPosition * 16;;
		this.setChunkBlockEntity(x, y, z, entity);
		
		this.hasEntities = true;
		int i2 = MathHelper.floor_double(entity.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity.posZ / 16.0D);
		if(i2 != this.xPosition || i3 != this.zPosition) {
			System.out.println("Wrong location! " + entity);
		}

		int i4 = MathHelper.floor_double(entity.posY / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i4 >= this.entities.length) {
			i4 = this.entities.length - 1;
		}

		entity.addedToChunk = true;
		entity.chunkCoordX = this.xPosition;
		entity.chunkCoordY = i4;
		entity.chunkCoordZ = this.zPosition;
		this.entities[i4].add(entity);
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
		} else {
			System.out.println("Attempted to place a tile entity where there was no entity tile!");
		}
	}

	public void setChunkBlockEntity(int x, int y, int z, EntityBlockEntity entity) {
		ChunkPosition chunkPosition5 = new ChunkPosition(x, y, z); 
		entity.worldObj = this.worldObj;
		Block block = Block.blocksList[this.getBlockID(x, y, z)];
		if(block != null && block instanceof BlockEntity) {
			/*
			if(this.isChunkLoaded) {
				if(this.chunkSpecialEntityMap.get(chunkPosition5) != null) {
					this.world.loadedEntityList.remove(this.chunkSpecialEntityMap.get(chunkPosition5));
				}

				this.world.loadedEntityList.add(entity);
			}
			*/

			this.chunkSpecialEntityMap.put(chunkPosition5, entity);
		} else {
			System.out.println("Attempted to place a special entity where there was no entity tile! " + x + " " + y + " " + z + "   " + entity.getClass() + " block was " + this.getBlockID(x, y, z));
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

	public void removeChunkBlockEntity(int x, int y, int z) {
		ChunkPosition chunkPosition4 = new ChunkPosition(x, y, z);
		if(this.isChunkLoaded) {
			Entity entity = this.chunkSpecialEntityMap.remove(chunkPosition4);
			if(entity != null) {
				// System.out.println("Removing " + entity + this.worldObj.loadedEntityList.remove(entity));
				entity.setEntityDead();
			}
		}

	}
	
	public void onChunkLoad() {
		this.isChunkLoaded = true;
		this.worldObj.addTileEntity(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.addLoadedEntities(this.entities[i1]);
		}

	}

	public void onChunkUnload() {
		this.isChunkLoaded = false;
		Iterator<TileEntity> iterator1 = this.chunkTileEntityMap.values().iterator();

		while(iterator1.hasNext()) {
			TileEntity tileEntity2 = (TileEntity)iterator1.next();
			tileEntity2.invalidate();
		}

		for(int i3 = 0; i3 < this.entities.length; ++i3) {
			this.worldObj.unloadEntities(this.entities[i3]);
		}

	}

	public void setChunkModified() {
		this.isModified = true;
	}

	public void getEntitiesWithinAABBForEntity(Entity entity1, AxisAlignedBB axisAlignedBB2, List<Entity> list3) {
		int i4 = MathHelper.floor_double((axisAlignedBB2.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entities.length) {
			i5 = this.entities.length - 1;
		}

		for(int i6 = i4; i6 <= i5; ++i6) {
			List<Entity> list7 = this.entities[i6];

			for(int i8 = 0; i8 < list7.size(); ++i8) {
				Entity entity9 = (Entity)list7.get(i8);
				if(entity9 != entity1 && entity9.boundingBox.intersectsWith(axisAlignedBB2)) {
					list3.add(entity9);
				}
			}
		}

	}

	public void getEntitiesOfTypeWithinAAAB(Class<?> class1, AxisAlignedBB axisAlignedBB2, List<Entity> list3) {
		int i4 = MathHelper.floor_double((axisAlignedBB2.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entities.length) {
			i5 = this.entities.length - 1;
		}

		for(int i6 = i4; i6 <= i5; ++i6) {
			List<Entity> list7 = this.entities[i6];

			for(int i8 = 0; i8 < list7.size(); ++i8) {
				Entity entity9 = (Entity)list7.get(i8);
				if(class1.isAssignableFrom(entity9.getClass()) && entity9.boundingBox.intersectsWith(axisAlignedBB2)) {
					list3.add(entity9);
				}
			}
		}

	}

	public boolean needsSaving(boolean z1) {
		if(this.neverSave) {
			return false;
		} else {
			if(z1) {
				if(this.hasEntities && this.worldObj.getWorldTime() != this.lastSaveTime) {
					return true;
				}
			} else if(this.hasEntities && this.worldObj.getWorldTime() >= this.lastSaveTime + 600L) {
				return true;
			}

			return this.isModified;
		}
	}

	public int setChunkData(byte[] b1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
		int i9;
		int i10;
		int i11;
		int i12;
		for(i9 = i2; i9 < i5; ++i9) {
			for(i10 = i4; i10 < i7; ++i10) {
				i11 = i9 << 11 | i10 << 7 | i3;
				i12 = i6 - i3;
				System.arraycopy(b1, i8, this.blocks, i11, i12);
				i8 += i12;
			}
		}

		this.generateHeightMap();

		for(i9 = i2; i9 < i5; ++i9) {
			for(i10 = i4; i10 < i7; ++i10) {
				i11 = (i9 << 11 | i10 << 7 | i3) >> 1;
				i12 = (i6 - i3) / 2;
				System.arraycopy(b1, i8, this.data.data, i11, i12);
				i8 += i12;
			}
		}

		for(i9 = i2; i9 < i5; ++i9) {
			for(i10 = i4; i10 < i7; ++i10) {
				i11 = (i9 << 11 | i10 << 7 | i3) >> 1;
				i12 = (i6 - i3) / 2;
				System.arraycopy(b1, i8, this.blocklightMap.data, i11, i12);
				i8 += i12;
			}
		}

		for(i9 = i2; i9 < i5; ++i9) {
			for(i10 = i4; i10 < i7; ++i10) {
				i11 = (i9 << 11 | i10 << 7 | i3) >> 1;
				i12 = (i6 - i3) / 2;
				System.arraycopy(b1, i8, this.skylightMap.data, i11, i12);
				i8 += i12;
			}
		}

		return i8;
	}

	public int getChunkData(byte[] b1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
		int i9 = i5 - i2;
		int i10 = i6 - i3;
		int i11 = i7 - i4;
		if(i9 * i10 * i11 == this.blocks.length) {
			System.arraycopy(this.blocks, 0, b1, i8, this.blocks.length);
			i8 += this.blocks.length;
			System.arraycopy(this.data.data, 0, b1, i8, this.data.data.length);
			i8 += this.data.data.length;
			System.arraycopy(this.blocklightMap.data, 0, b1, i8, this.blocklightMap.data.length);
			i8 += this.blocklightMap.data.length;
			System.arraycopy(this.skylightMap.data, 0, b1, i8, this.skylightMap.data.length);
			i8 += this.skylightMap.data.length;
			return i8;
		} else {
			int i12;
			int i13;
			int i14;
			int i15;
			for(i12 = i2; i12 < i5; ++i12) {
				for(i13 = i4; i13 < i7; ++i13) {
					i14 = i12 << 11 | i13 << 7 | i3;
					i15 = i6 - i3;
					System.arraycopy(this.blocks, i14, b1, i8, i15);
					i8 += i15;
				}
			}

			for(i12 = i2; i12 < i5; ++i12) {
				for(i13 = i4; i13 < i7; ++i13) {
					i14 = (i12 << 11 | i13 << 7 | i3) >> 1;
					i15 = (i6 - i3) / 2;
					System.arraycopy(this.data.data, i14, b1, i8, i15);
					i8 += i15;
				}
			}

			for(i12 = i2; i12 < i5; ++i12) {
				for(i13 = i4; i13 < i7; ++i13) {
					i14 = (i12 << 11 | i13 << 7 | i3) >> 1;
					i15 = (i6 - i3) / 2;
					System.arraycopy(this.blocklightMap.data, i14, b1, i8, i15);
					i8 += i15;
				}
			}

			for(i12 = i2; i12 < i5; ++i12) {
				for(i13 = i4; i13 < i7; ++i13) {
					i14 = (i12 << 11 | i13 << 7 | i3) >> 1;
					i15 = (i6 - i3) / 2;
					System.arraycopy(this.skylightMap.data, i14, b1, i8, i15);
					i8 += i15;
				}
			}

			return i8;
		}
	}

	public Random getSeededRandom(long j1) {
		return new Random(this.worldObj.getRandomSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ j1);
	}

	public boolean getIsChunkRendered() {
		return false;
	}

	public void removeUnknownBlocks() {
		ChunkBlockMap.translateBlocks(this.blocks);
	}
	
	public void refreshCaches() {
		BiomeGenBase biomeGen [] = null;
		biomeGen = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomeGen, this.xPosition * 16, this.zPosition * 16, 16, 16);
		this.biomeGenCache = biomeGen.clone();
	}
	
	public BiomeGenBase getBiomeGenAt (int x, int z) {
		if (this.biomeGenCache == null) {
			// System.out.println ("Biomes for chunk " + this.xPosition + ", " + this.zPosition + " were not cached - caching now!");
			this.refreshCaches();
		}
		return this.biomeGenCache [x << 4 | z];
	}
}

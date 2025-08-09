package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.entity.block.EntityBlockEntity;

public class Chunk {
	public static boolean isLit;
	
	public byte[] blocks;
	public byte[] data;
	
	public boolean isChunkLoaded;
	public World worldObj;
	public NibbleArray skylightMap;
	public NibbleArray blocklightMap;
	public byte[] heightMap;
	public byte[] landSurfaceHeightMap;
	public int heightMapMinimum;
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
	public boolean hasBuilding = false;
	public boolean hasRoad = false;
	public boolean isOcean = false;
	public boolean hasUnderwaterRuin = false;
	public boolean hasFeature = false;
	public boolean isUrbanChunk = false;
	public int baseHeight = 0;
	public int roadVariation = 0;
	public boolean gotBlocks = false;

	// For custom features during "populate"
	public int chestX;
	public int chestY = -1;
	public int chestZ;
	
	public int specialX;
	public int specialY = -1;
	public int specialZ;
	
	Building building = null;
	
	public boolean beingDecorated = false;

	public int[] grassColorCache;
	public int[] foliageColorCache;

	@SuppressWarnings("unchecked")
	public Chunk(World world, int chunkX, int chunkZ) {
		this.chunkTileEntityMap = new HashMap<ChunkPosition, TileEntity>();
		this.chunkSpecialEntityMap = new HashMap<ChunkPosition, EntityBlockEntity>();
		this.entities = (List<Entity> []) new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
		this.lastSaveTime = 0L;
		this.worldObj = world;
		this.xPosition = chunkX;
		this.zPosition = chunkZ;
		this.heightMap = new byte[256];
		this.landSurfaceHeightMap = new byte[256];
		
		for(int i4 = 0; i4 < this.entities.length; ++i4) {
			this.entities[i4] = new ArrayList<Entity>();
		}
		
	}

	public Chunk(World world, byte[] blocks, byte[] metadata, int chunkX, int chunkZ) {
		this(world, chunkX, chunkZ);
		this.blocks = blocks;
		this.data = metadata;
		this.skylightMap = new NibbleArray(blocks.length);
		this.blocklightMap = new NibbleArray(blocks.length);
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

	public void generateHeightMap() {
		this.heightMapMinimum = 127;

		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				int height = 127;

				for(int index = x << 11 | z << 7; height > 0 && Block.lightOpacity[this.blocks[index + height - 1] & 255] == 0; --height) {
				}

				this.heightMap[z << 4 | x] = (byte)height;
				if(height < this.heightMapMinimum) {
					this.heightMapMinimum = height;
				}
			}
		}

		this.isModified = true;
	}
	
	public void generateLandSurfaceHeightMap() {
		int index = 0;
		for(int z = 0; z < 16; z ++) {
			for(int x = 0; x < 16; x ++) {
				int y = 127;
				int baseColumn = x << 11 | z << 7 | y;
				// Changed: not just air but also any non opaque block.
				while(Block.lightOpacity[this.blocks[baseColumn] & 255] < 255 && y > 0) { y --; baseColumn --; }
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

		this.heightMapMinimum = i1;
	}

	public void generateSkylightMap() {
		this.heightMapMinimum = 127;

		int x;
		int z;
		for(x = 0; x < 16; ++x) {
			for(z = 0; z < 16; ++z) {
				int height = 127;

				int index;
				for(index = x << 11 | z << 7; height > 0 && Block.lightOpacity[this.blocks[index + height - 1] & 255] == 0; --height) {
				}

				this.heightMap[z << 4 | x] = (byte)height;
				if(height < this.heightMapMinimum) {
					this.heightMapMinimum = height;
				}

				if(!this.worldObj.worldProvider.hasNoSky) {
					int lightLevel = 15;
					int y = 127;

					do {
						lightLevel -= Block.lightOpacity[this.blocks[index + y] & 255];
						if(lightLevel > 0) {
							this.skylightMap.setNibble(x, y, z, lightLevel);
						}

						--y;
					} while(y > 0 && lightLevel > 0);
				}
			}
		}

		this.isModified = true;
	}

	public void doNothing() {
	}

	/*
	 * This method relights a column from the sky after setting block at x, y, z.
	 */
	private void relightBlock(int x, int y, int z) {
		int columnHeight = this.heightMap[z << 4 | x] & 255;

		// If we just set a block higher than the cur. block height...
		int newHeight = Math.max(y, columnHeight);

		// But, if blocks beneath the new height are not opaque, lower the value until an opaque block is found
		for(int idx = x << 11 | z << 7; newHeight > 0 && Block.lightOpacity[this.blocks[idx + newHeight - 1] & 255] == 0; --newHeight) {
		}

		// newHeight is now at the topmost opaque block.
		// If the stored height and the new height are different...
		if(newHeight != columnHeight) {
			// Store new height
			this.heightMap[z << 4 | x] = (byte)newHeight;
			
			if(newHeight < this.heightMapMinimum) {
				this.heightMapMinimum = newHeight;
			}
			this.isModified = true;
		}
	}

	public int getBlockID(int x, int y, int z) {
		return (int) this.blocks[x << 11 | z << 7 | y] & 0xff;
	}

	public boolean setBlockIDWithMetadata(int x, int y, int z, int id, int metadata) {
		int height = this.heightMap[z << 4 | x] & 255;
		int index = x << 11 | z << 7 | y;
		int existingId = this.blocks[index] & 255;
		if(existingId == id && this.data[index] == metadata) {
			return false;
		} else {
			int absX = (this.xPosition << 4) | x;
			int absZ = (this.zPosition << 4) | z;
			
			Block block = Block.blocksList[existingId];
			
			// Write new block ID
			this.blocks[x << 11 | z << 7 | y] = (byte)id;
			
			// Call `onRemoval` from removed block, if applies.
			if(block != null && !this.worldObj.isRemote) {
				block.onBlockRemoval(this.worldObj, absX, y, absZ);
			}

			// Write new metadata
			this.data[index] = (byte)metadata;

			// If there's a sky, skylight may need to be recalculated.			
			if(!this.worldObj.worldProvider.hasNoSky) {

				// If block is not 100% transparent
				if(Block.lightOpacity[id] != 0) {

					// And set above current topmost block
					if(y >= height) {
						// Relight from just above this new block
						this.relightBlock(x, y + 1, z);
					}
				} else {
					// Set a 100% transparent block

					// If it is replacing the topmost block, relight from here.
					if(y == height - 1) {
						this.relightBlock(x, y, z);
					}
				}
			}
			this.updateLight(x, y, z);

			block = Block.blocksList[id];
			if(block != null) {
				block.onBlockAdded(this.worldObj, absX, y, absZ);
			}

			this.isModified = true;
			return true;
		}
	}
	
	public boolean setBlockIDAndMetadataColumn(int x, int y, int z, int[] id) {
		// Column is bottom to top ordered
		// Metadata is encoded as a most significant byte

		int absX = (this.xPosition << 4) | x;
		int absZ = (this.zPosition << 4) | z;
		
		int height = this.heightMap[z << 4 | x] & 255;
		
		int index = x << 11 | z << 7 | y;
		
		// Write blocks
		for(int i = 0; i < id.length; i ++) {
			int b = id[i];
			if(b >= 0) {
				this.data[index] = (byte)((b >> 8) & 0xff);
				
				// Call `onRemoval` from removed block, if applies.
				Block block = Block.blocksList[this.blocks[index] & 255];
				if(block != null && !this.worldObj.isRemote) {
					block.onBlockRemoval(this.worldObj, absX, y, absZ);
				}
				
				this.blocks[index ++] = (byte) (b & 255);
				y ++;
			} else if(b < -1) {
				// A negative value is the count for a run
				int c = -b;
				i ++;
				b = id[i];
				if(b == -1) {
					index += c;
				} else {
					byte m = (byte) ((b >> 8) & 255);
					byte b0 = (byte) (b & 255);
					while (c -- > 0) {
						this.data[index] = m;
						
						// Call `onRemoval` from removed block, if applies.
						Block block = Block.blocksList[this.blocks[index] & 255];
						if(block != null && !this.worldObj.isRemote) {
							block.onBlockRemoval(this.worldObj, absX, y, absZ);
						}
						
						this.blocks[index ++] = b0;
						y ++;
					}
				} 
			} else {
				y ++;
			};
			if(y == 128) break;
		}
		
		// The topmost block
		y --;
		
		// Relight top
		if (y >= height) this.relightBlock(x, y + 1, z);
		this.updateLight(x, y, z);
		
		this.isModified = true;
		
		return true;
	}

	public boolean setBlockID(int x, int y, int z, int id) {
		return this.setBlockIDWithMetadata(x, y, z, id, 0);
	}

	public int getBlockMetadata(int x, int y, int z) {
		return this.data[x << 11 | z << 7 | y] & 0xff;
	}

	public void setBlockMetadata(int x, int y, int z, int meta) {
		this.isModified = true;
		this.data[x << 11 | z << 7 | y] = (byte)meta;
	}

	public int getSavedLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4) {
		if(i3 < 0) return 0;
		if(i3 > 127) return 15;
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
	
	public EnumCreatureType getCreatureType(Entity entity) {
		EnumCreatureType[] availableCreatureTypes = EnumCreatureType.values();
		for(int i = 0; i < availableCreatureTypes.length; i ++) {
			EnumCreatureType creatureType = availableCreatureTypes[i];
			if(creatureType.getCreatureClass().isAssignableFrom(entity.getClass())) {
				return creatureType;
			}
		}
		
		return null;
	}

	public void addEntity(Entity entity1) {
		this.hasEntities = true;
		int i2 = MathHelper.floor_double(entity1.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity1.posZ / 16.0D);
		if(i2 != this.xPosition || i3 != this.zPosition) {
			System.out.println("Wrong location! " + entity1);
			System.out.println("This chunk " + this.xPosition + " " + this.zPosition + " vs attempted " + i2 + " " + i3);
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
	
	public int getCreatureTypeCounter(EnumCreatureType creatureType) {
		int count = 0;
		for(int i = 0; i < 8; i ++) {
			List<Entity> l = this.entities[i];
			for(int j = 0; j < l.size(); j ++) {
				if(creatureType.getCreatureClass().isAssignableFrom(l.get(j).getClass())) {
					count ++;
				}
			}
		}
		
		return count;
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
			blockContainer7.onBlockAdded(this.worldObj, this.xPosition << 4 | i1, i2, this.zPosition << 4 | i3);
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
			blockEntity.onBlockAdded(this.worldObj, this.xPosition << 4 | x, y, this.zPosition << 4 | z);
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
		int i2 = tileEntity1.xCoord - (this.xPosition << 4);
		int i3 = tileEntity1.yCoord;
		int i4 = tileEntity1.zCoord - (this.zPosition << 4);
		this.setChunkBlockTileEntity(i2, i3, i4, tileEntity1);
		if(this.isChunkLoaded) {
			this.worldObj.loadedTileEntityList.add(tileEntity1);
		}

	}
	
	public void addSpecialEntity(EntityBlockEntity entity) {
		int x = entity.xTile - (this.xPosition << 4);
		int y = entity.yTile;
		int z = entity.zTile - (this.zPosition << 4);
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
		tileEntity4.xCoord = this.xPosition << 4 | i1;
		tileEntity4.yCoord = i2;
		tileEntity4.zCoord = this.zPosition << 4 | i3;
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
		this.generateLandSurfaceHeightMap();

		for(i9 = i2; i9 < i5; ++i9) {
			for(i10 = i4; i10 < i7; ++i10) {
				i11 = i9 << 11 | i10 << 7 | i3;
				i12 = i6 - i3;
				System.arraycopy(b1, i8, this.data, i11, i12);
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
			System.arraycopy(this.data, 0, b1, i8, this.data.length);
			i8 += this.data.length;
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
					i14 = i12 << 11 | i13 << 7 | i3;
					i15 = i6 - i3;
					System.arraycopy(this.data, i14, b1, i8, i15);
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
		WorldChunkManager worldChunkManager = this.worldObj.getWorldChunkManager();
		BiomeGenBase biomeGen [] = null;
		biomeGen = worldChunkManager.loadBlockGeneratorData(biomeGen, this.xPosition << 4, this.zPosition << 4, 16, 16);
		this.biomeGenCache = biomeGen.clone();
		
		int biomeIndex = 0;
		this.grassColorCache = new int[256];
		this.foliageColorCache = new int[256];
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				double t = worldChunkManager.temperature[biomeIndex];
				double h = worldChunkManager.humidity[biomeIndex];
				this.grassColorCache[biomeIndex] = ColorizerGrass.getGrassColor(t, h);
				this.foliageColorCache[biomeIndex] = ColorizerFoliage.getFoliageColor(t, h);
				biomeIndex ++;
			}
		}
	}
	
	public BiomeGenBase getBiomeGenAt (int x, int z) {
		if (this.biomeGenCache == null) {
			// System.out.println ("Biomes for chunk " + this.xPosition + ", " + this.zPosition + " were not cached - caching now!");
			this.refreshCaches();
		}
		return this.biomeGenCache [x << 4 | z];
	}

	public boolean setBlockIDWithMetadataNoLights(int x, int y, int z, int id, int metadata) {
		int index = x << 11 | z << 7 | y;
		int existingId = this.blocks[index] & 255;
		if(existingId == id && this.data[index] == metadata) {
			return false;
		} else {
			int absX = (this.xPosition << 4) | x;
			int absZ = (this.zPosition << 4) | z;
			
			Block block = Block.blocksList[existingId];
			
			// Write new block ID
			this.blocks[x << 11 | z << 7 | y] = (byte)id;
			
			// Call `onRemoval` from removed block, if applies.
			if(block != null && !this.worldObj.isRemote) {
				block.onBlockRemoval(this.worldObj, absX, y, absZ);
			}

			// Write new metadata
			this.data[index] = (byte)metadata;

			block = Block.blocksList[id];
			if(block != null) {
				block.onBlockAdded(this.worldObj, absX, y, absZ);
			}

			this.isModified = true;
			return true;
		}
	}

	public void clearAllLights() {
		this.skylightMap = new NibbleArray(this.blocks.length);
	}

	public void cacheBiomes(BiomeGenBase[] biomesForGeneration) {
		for(int x = 0; x < 16; x ++) {
			for(int z = 0; z < 16; z ++) {
				this.biomeGenCache[z | x << 4] = biomesForGeneration[z + x * 21];
			}
		}
		
	}

	public void setMetadata(byte[] metadata) {
		// TODO Auto-generated method stub
		
	}
	
	public void initLightingForRealNotJustHeightmap() {
		this.worldObj.blockLight.initBlockLight(this.xPosition, this.zPosition, this.worldObj.getBiomeGenAt(this.xPosition, this.zPosition).forceBlockLightInitLikeNether);

		if (!this.worldObj.worldProvider.hasNoSky) {
			this.worldObj.skyLight.initSkylight(this.xPosition, this.zPosition);
		}
	}

	public void updateLight(int localX, int worldY, int localZ) {
		int worldX = localX | (this.xPosition << 4);
		int worldZ = localZ | (this.zPosition << 4);

		this.worldObj.blockLight.checkBlockEmittance(worldX, worldY, worldZ);
		if (!this.worldObj.worldProvider.hasNoSky) {
			this.worldObj.skyLight.checkSkyEmittance(worldX, worldY, worldZ);
		}
	}

	public int getGrassColorFromCache(int x, int z) {
		if(this.grassColorCache == null) {
			this.refreshCaches();
		} 
		
		return this.grassColorCache[x << 4 | z];
	}
	
	public int getFoliageColorFromCache(int x, int z) {
		if(this.foliageColorCache == null) {
			this.refreshCaches();
		}
		
		return this.foliageColorCache[x << 4 | z];
	}

}

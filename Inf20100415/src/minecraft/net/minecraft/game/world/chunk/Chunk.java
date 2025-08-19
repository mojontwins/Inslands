package net.minecraft.game.world.chunk;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityList;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.EnumSkyBlock;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.BlockContainer;
import net.minecraft.game.world.block.tileentity.TileEntity;

import util.MathHelper;

public final class Chunk {
	public static boolean isLit;
	private byte[] blocks;
	private World worldObj;
	private NibbleArray data;
	private NibbleArray skyLightMap;
	private NibbleArray blockLightMap;
	private byte[] heightMap;
	private int lowestBlockHeight;
	public final int xPosition;
	public final int zPosition;
	private Map chunkTileEntityMap;
	private List[] entities;
	public boolean isTerrainPopulated;
	public boolean isModified;
	private boolean hasEntities;

	private Chunk(World world1, int i2, int i3) {
		this.chunkTileEntityMap = new HashMap();
		this.entities = new List[8];
		this.isTerrainPopulated = false;
		this.isModified = false;
		this.hasEntities = false;
		this.worldObj = world1;
		this.xPosition = i2;
		this.zPosition = i3;
		this.heightMap = new byte[256];

		for(int i4 = 0; i4 < this.entities.length; ++i4) {
			this.entities[i4] = new ArrayList();
		}

	}

	public Chunk(World world1, byte[] b2, int i3, int i4) {
		this(world1, i3, i4);
		this.blocks = b2;
		this.data = new NibbleArray(b2.length);
		this.skyLightMap = new NibbleArray(b2.length);
		this.blockLightMap = new NibbleArray(b2.length);
	}

	public final int getHeightValue(int i1, int i2) {
		return this.heightMap[i2 << 4 | i1] & 255;
	}

	public final void generateHeightMap() {
		int i1 = 127;

		int i2;
		int i3;
		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.heightMap[i3 << 4 | i2] = -128;
				this.relightBlock(i2, 127, i3);
				if((this.heightMap[i3 << 4 | i2] & 255) < i1) {
					i1 = this.heightMap[i3 << 4 | i2] & 255;
				}
			}
		}

		this.lowestBlockHeight = i1;

		for(i2 = 0; i2 < 16; ++i2) {
			for(i3 = 0; i3 < 16; ++i3) {
				this.updateSkylight_do(i2, i3);
			}
		}

		this.isModified = true;
	}

	private void updateSkylight_do(int i1, int i2) {
		int i3 = this.getHeightValue(i1, i2);
		i1 += this.xPosition << 4;
		i2 += this.zPosition << 4;
		this.checkSkylightNeighborHeight(i1 - 1, i2, i3);
		this.checkSkylightNeighborHeight(i1 + 1, i2, i3);
		this.checkSkylightNeighborHeight(i1, i2 - 1, i3);
		this.checkSkylightNeighborHeight(i1, i2 + 1, i3);
	}

	private void checkSkylightNeighborHeight(int i1, int i2, int i3) {
		int i4;
		if((i4 = this.worldObj.getHeightValue(i1, i2)) > i3) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i1, i3, i2, i1, i4, i2);
		} else if(i4 < i3) {
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i1, i4, i2, i1, i3, i2);
		}

		this.isModified = true;
	}

	private void relightBlock(int i1, int i2, int i3) {
		int i4;
		int i5 = i4 = this.heightMap[i3 << 4 | i1] & 255;
		if(i2 > i4) {
			i5 = i2;
		}

		while(i5 > 0 && Block.lightOpacity[this.getBlockID(i1, i5 - 1, i3)] == 0) {
			--i5;
		}

		if(i5 != i4) {
			this.worldObj.markBlocksDirtyVertical(i1, i3, i5, i4);
			this.heightMap[i3 << 4 | i1] = (byte)i5;
			int i6;
			int i7;
			if(i5 < this.lowestBlockHeight) {
				this.lowestBlockHeight = i5;
			} else {
				i2 = 127;

				for(i6 = 0; i6 < 16; ++i6) {
					for(i7 = 0; i7 < 16; ++i7) {
						if((this.heightMap[i7 << 4 | i6] & 255) < i2) {
							i2 = this.heightMap[i7 << 4 | i6] & 255;
						}
					}
				}

				this.lowestBlockHeight = i2;
			}

			i2 = (this.xPosition << 4) + i1;
			i6 = (this.zPosition << 4) + i3;
			if(i5 < i4) {
				for(i7 = i5; i7 < i4; ++i7) {
					this.skyLightMap.set(i1, i7, i3, 15);
				}
			} else {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i2, i4, i6, i2, i5, i6);

				for(i7 = i4; i7 < i5; ++i7) {
					this.skyLightMap.set(i1, i7, i3, 0);
				}
			}

			i7 = 15;

			for(i4 = i5; i5 > 0 && i7 > 0; this.skyLightMap.set(i1, i5, i3, i7)) {
				--i5;
				int i8;
				if((i8 = Block.lightOpacity[this.getBlockID(i1, i5, i3)]) == 0) {
					i8 = 1;
				}

				if((i7 -= i8) < 0) {
					i7 = 0;
				}
			}

			while(i5 > 0 && Block.lightOpacity[this.getBlockID(i1, i5 - 1, i3)] == 0) {
				--i5;
			}

			if(i5 != i4) {
				this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i2 - 1, i5, i6 - 1, i2 + 1, i4, i6 + 1);
			}

			this.isModified = true;
		}
	}

	public final int getBlockID(int i1, int i2, int i3) {
		return this.blocks[i1 << 11 | i3 << 7 | i2];
	}

	public final boolean setBlockID(int i1, int i2, int i3, int i4) {
		byte b5 = (byte)i4;
		int i6 = this.heightMap[i3 << 4 | i1] & 255;
		int i7;
		if((i7 = this.blocks[i1 << 11 | i3 << 7 | i2] & 255) == i4) {
			return false;
		} else {
			int i8 = (this.xPosition << 4) + i1;
			int i9 = (this.zPosition << 4) + i3;
			if(i7 != 0) {
				Block.blocksList[i7].onBlockRemoval(this.worldObj, i8, i2, i9);
			}

			this.blocks[i1 << 11 | i3 << 7 | i2] = b5;
			this.data.set(i1, i2, i3, 0);
			if(Block.lightOpacity[b5] != 0) {
				if(i2 >= i6) {
					this.relightBlock(i1, i2 + 1, i3);
				}
			} else if(i2 == i6 - 1) {
				this.relightBlock(i1, i2, i3);
			}

			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Sky, i8, i2, i9, i8, i2, i9);
			this.worldObj.scheduleLightingUpdate(EnumSkyBlock.Block, i8, i2, i9, i8, i2, i9);
			this.updateSkylight_do(i1, i3);
			if(i4 != 0) {
				Block.blocksList[i4].onBlockAdded(this.worldObj, i8, i2, i9);
			}

			this.isModified = true;
			return true;
		}
	}

	public final int getBlockMetadata(int i1, int i2, int i3) {
		return this.data.get(i1, i2, i3);
	}

	public final void setBlockMetadata(int i1, int i2, int i3, int i4) {
		this.isModified = true;
		this.data.set(i1, i2, i3, i4);
	}

	public final int getSavedLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4) {
		return enumSkyBlock1 == EnumSkyBlock.Sky ? this.skyLightMap.get(i2, i3, i4) : (enumSkyBlock1 == EnumSkyBlock.Block ? this.blockLightMap.get(i2, i3, i4) : 0);
	}

	public final void setLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5) {
		this.isModified = true;
		if(enumSkyBlock1 == EnumSkyBlock.Sky) {
			this.skyLightMap.set(i2, i3, i4, i5);
		} else if(enumSkyBlock1 == EnumSkyBlock.Block) {
			this.blockLightMap.set(i2, i3, i4, i5);
		}
	}

	public final int getBlockLightValue(int i1, int i2, int i3, int i4) {
		int i5;
		if((i5 = this.skyLightMap.get(i1, i2, i3)) > 0) {
			isLit = true;
		}

		i5 -= i4;
		if((i1 = this.blockLightMap.get(i1, i2, i3)) > i5) {
			i5 = i1;
		}

		return i5;
	}

	public final void writeChunkNBTData(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setInteger("xPos", this.xPosition);
		nBTTagCompound1.setInteger("zPos", this.zPosition);
		nBTTagCompound1.setLong("LastUpdate", this.worldObj.worldTime);
		nBTTagCompound1.setByteArray("Blocks", this.blocks);
		nBTTagCompound1.setByteArray("Data", this.data.data);
		nBTTagCompound1.setByteArray("SkyLight", this.skyLightMap.data);
		nBTTagCompound1.setByteArray("BlockLight", this.blockLightMap.data);
		nBTTagCompound1.setByteArray("HeightMap", this.heightMap);
		nBTTagCompound1.setBoolean("TerrainPopulated", this.isTerrainPopulated);
		this.hasEntities = false;
		NBTTagList nBTTagList2 = new NBTTagList();

		Iterator iterator4;
		NBTTagCompound nBTTagCompound6;
		for(int i3 = 0; i3 < this.entities.length; ++i3) {
			iterator4 = this.entities[i3].iterator();

			while(iterator4.hasNext()) {
				Entity entity5 = (Entity)iterator4.next();
				nBTTagCompound6 = new NBTTagCompound();
				if(entity5.addEntityID(nBTTagCompound6)) {
					nBTTagList2.setTag(nBTTagCompound6);
					this.hasEntities = true;
				}
			}
		}

		nBTTagCompound1.setTag("Entities", nBTTagList2);
		NBTTagList nBTTagList7 = new NBTTagList();
		iterator4 = this.chunkTileEntityMap.values().iterator();

		while(iterator4.hasNext()) {
			TileEntity tileEntity8 = (TileEntity)iterator4.next();
			nBTTagCompound6 = new NBTTagCompound();
			tileEntity8.writeToNBT(nBTTagCompound6);
			nBTTagList7.setTag(nBTTagCompound6);
		}

		nBTTagCompound1.setTag("TileEntities", nBTTagList7);
	}

	public static Chunk readChunkNBTData(World world0, NBTTagCompound nBTTagCompound1) {
		int i2 = nBTTagCompound1.getInteger("xPos");
		int i3 = nBTTagCompound1.getInteger("zPos");
		Chunk chunk9;
		(chunk9 = new Chunk(world0, i2, i3)).blocks = nBTTagCompound1.getByteArray("Blocks");
		chunk9.data = new NibbleArray(nBTTagCompound1.getByteArray("Data"));
		chunk9.skyLightMap = new NibbleArray(nBTTagCompound1.getByteArray("SkyLight"));
		chunk9.blockLightMap = new NibbleArray(nBTTagCompound1.getByteArray("BlockLight"));
		chunk9.heightMap = nBTTagCompound1.getByteArray("HeightMap");
		chunk9.isTerrainPopulated = nBTTagCompound1.getBoolean("TerrainPopulated");
		if(!chunk9.data.isValid()) {
			chunk9.data = new NibbleArray(chunk9.blocks.length);
		}

		if(chunk9.heightMap == null || !chunk9.skyLightMap.isValid()) {
			chunk9.heightMap = new byte[256];
			chunk9.skyLightMap = new NibbleArray(chunk9.blocks.length);
			chunk9.generateHeightMap();
		}

		if(!chunk9.blockLightMap.isValid()) {
			chunk9.blockLightMap = new NibbleArray(chunk9.blocks.length);
		}

		chunk9.hasEntities = false;
		NBTTagList nBTTagList10;
		if((nBTTagList10 = nBTTagCompound1.getTagList("Entities")) != null) {
			for(int i4 = 0; i4 < nBTTagList10.tagCount(); ++i4) {
				Entity entity6;
				if((entity6 = EntityList.createEntityFromNBT((NBTTagCompound)nBTTagList10.tagAt(i4), world0)) != null) {
					chunk9.hasEntities = true;
					chunk9.addEntity(entity6);
				}
			}
		}

		NBTTagList nBTTagList11;
		if((nBTTagList11 = nBTTagCompound1.getTagList("TileEntities")) != null) {
			for(int i5 = 0; i5 < nBTTagList11.tagCount(); ++i5) {
				TileEntity tileEntity8;
				if((tileEntity8 = TileEntity.createAndLoadEntity((NBTTagCompound)nBTTagList11.tagAt(i5))) != null) {
					i3 = tileEntity8.xCoord - (chunk9.xPosition << 4);
					int i12 = tileEntity8.yCoord;
					int i7 = tileEntity8.zCoord - (chunk9.zPosition << 4);
					chunk9.setChunkBlockTileEntity(i3, i12, i7, tileEntity8);
				}
			}
		}

		return chunk9;
	}

	public final void addEntity(Entity entity1) {
		int i2 = MathHelper.floor_double(entity1.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity1.posZ / 16.0D);
		if(i2 != this.xPosition || i3 != this.zPosition) {
			System.out.println("Wrong location! " + entity1);
		}

		if((i2 = MathHelper.floor_double(entity1.posY / 16.0D)) < 0) {
			i2 = 0;
		}

		if(i2 >= this.entities.length) {
			i2 = this.entities.length - 1;
		}

		this.entities[i2].add(entity1);
		this.isModified = true;
	}

	public final void removeEntityAtIndex(Entity entity1, int i2) {
		if(i2 < 0) {
			i2 = 0;
		}

		if(i2 >= this.entities.length) {
			i2 = this.entities.length - 1;
		}

		if(!this.entities[i2].contains(entity1)) {
			System.out.println("There\'s no such entity to remove: " + entity1);
		}

		this.entities[i2].remove(entity1);
		this.isModified = true;
	}

	public final boolean canBlockSeeTheSky(int i1, int i2, int i3) {
		return i2 >= (this.heightMap[i3 << 4 | i1] & 255);
	}

	public final TileEntity getChunkBlockTileEntity(int i1, int i2, int i3) {
		int i4 = i1 + (i2 << 10) + (i3 << 10 << 10);
		TileEntity tileEntity5;
		if((tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(i4)) == null) {
			int i6 = this.getBlockID(i1, i2, i3);
			((BlockContainer)Block.blocksList[i6]).onBlockAdded(this.worldObj, (this.xPosition << 4) + i1, i2, (this.zPosition << 4) + i3);
			tileEntity5 = (TileEntity)this.chunkTileEntityMap.get(i4);
		}

		return tileEntity5;
	}

	public final void setChunkBlockTileEntity(int i1, int i2, int i3, TileEntity tileEntity4) {
		this.isModified = true;
		int i5 = i1 + (i2 << 10) + (i3 << 10 << 10);
		tileEntity4.worldObj = this.worldObj;
		tileEntity4.xCoord = (this.xPosition << 4) + i1;
		tileEntity4.yCoord = i2;
		tileEntity4.zCoord = (this.zPosition << 4) + i3;
		if(this.getBlockID(i1, i2, i3) != 0 && Block.blocksList[this.getBlockID(i1, i2, i3)] instanceof BlockContainer) {
			this.chunkTileEntityMap.put(i5, tileEntity4);
			this.worldObj.loadedTileEntityList.add(tileEntity4);
		} else {
			System.out.println("Attempted to place a tile entity where there was no entity tile!");
		}
	}

	public final void removeChunkBlockTileEntity(int i1, int i2, int i3) {
		this.isModified = true;
		i1 = i1 + (i2 << 10) + (i3 << 10 << 10);
		this.worldObj.loadedTileEntityList.remove(this.chunkTileEntityMap.remove(i1));
	}

	public final void loadEntities() {
		this.worldObj.loadedTileEntityList.addAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.addLoadedEntities(this.entities[i1]);
		}

	}

	public final void unloadEntities() {
		this.worldObj.loadedTileEntityList.removeAll(this.chunkTileEntityMap.values());

		for(int i1 = 0; i1 < this.entities.length; ++i1) {
			this.worldObj.unloadEntities(this.entities[i1]);
		}

	}

	public final void getEntitiesWithinAABBForEntity(Entity entity1, AxisAlignedBB axisAlignedBB2, List list3) {
		int i4 = MathHelper.floor_double((axisAlignedBB2.minY - 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.maxY + 2.0D) / 16.0D);
		if(i4 < 0) {
			i4 = 0;
		}

		if(i5 >= this.entities.length) {
			i5 = this.entities.length - 1;
		}

		for(i4 = i4; i4 <= i5; ++i4) {
			List list6 = this.entities[i4];

			for(int i7 = 0; i7 < list6.size(); ++i7) {
				Entity entity8;
				if((entity8 = (Entity)list6.get(i7)) != entity1 && entity8.boundingBox.intersectsWith(axisAlignedBB2)) {
					list3.add(entity8);
				}
			}
		}

	}

	public final int getDebugCountedEntities() {
		int i1 = 0;

		for(int i2 = 0; i2 < this.entities.length; ++i2) {
			i1 += this.entities[i2].size();
		}

		return i1;
	}

	public final boolean needsSaving(boolean z1) {
		if(this.isModified) {
			return true;
		} else {
			if(z1) {
				if(this.hasEntities) {
					return true;
				}

				for(int i2 = 0; i2 < this.entities.length; ++i2) {
					if(this.entities[i2].size() > 0) {
						return true;
					}
				}
			}

			return false;
		}
	}
}
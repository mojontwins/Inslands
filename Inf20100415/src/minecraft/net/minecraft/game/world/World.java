package net.minecraft.game.world;

import com.mojang.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.tileentity.TileEntity;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.ChunkProviderLoadOrGenerate;
import net.minecraft.game.world.chunk.IChunkProvider;
import net.minecraft.game.world.material.Material;
import net.minecraft.game.world.path.Pathfinder;
import net.minecraft.game.world.terrain.ChunkProviderGenerate;

import util.MathHelper;

public class World {
	private List lightingToUpdate = new ArrayList();
	private List loadedEntityList = new ArrayList();
	private List unloadedEntityList = new LinkedList();
	public List loadedTileEntityList = new ArrayList();
	public long worldTime = 0L;
	private long skyColor = 10079487L;
	private long fogColor = 11587839L;
	private long cloudColor = 16777215L;
	private int skylightSubtracted = 0;
	private int updateLCG = (new Random()).nextInt();
	private int DIST_HASH_MAGIC = 1013904223;
	private static float[] lightBrightnessTable = new float[16];
	public Entity playerEntity;
	public int difficultySetting;
	public final Pathfinder pathFinder = new Pathfinder(this);
	public Random rand = new Random();
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	public boolean isNewWorld = false;
	private List worldAccesses = new ArrayList();
	private IChunkProvider chunkProvider;
	private File saveDirectory;
	private long randomSeed = 0L;
	private NBTTagCompound nbtCompoundPlayer;
	public long sizeOnDisk = 0L;

	public static NBTTagCompound getWorldNBTTag(File file0, String string1) {
		file0 = new File(file0, "saves");
		if(!(file0 = new File(file0, string1)).exists()) {
			return null;
		} else {
			if((file0 = new File(file0, "level.dat")).exists()) {
				try {
					return LoadingScreenRenderer.read(new FileInputStream(file0)).getCompoundTag("Data");
				} catch (Exception exception2) {
					exception2.printStackTrace();
				}
			}

			return null;
		}
	}

	public static void deleteWorld(File file0, String string1) {
		file0 = new File(file0, "saves");
		if((file0 = new File(file0, string1)).exists()) {
			deleteFiles(file0.listFiles());
			file0.delete();
		}
	}

	private static void deleteFiles(File[] file0) {
		for(int i1 = 0; i1 < file0.length; ++i1) {
			if(file0[i1].isDirectory()) {
				deleteFiles(file0[i1].listFiles());
			}

			file0[i1].delete();
		}

	}

	public World(File file1, String string2) {
		file1.mkdirs();
		this.saveDirectory = new File(file1, string2);
		this.saveDirectory.mkdirs();
		file1 = new File(this.saveDirectory, "level.dat");
		this.isNewWorld = !file1.exists();
		if(file1.exists()) {
			try {
				NBTTagCompound nBTTagCompound4 = LoadingScreenRenderer.read(new FileInputStream(file1)).getCompoundTag("Data");
				this.randomSeed = nBTTagCompound4.getLong("RandomSeed");
				this.spawnX = nBTTagCompound4.getInteger("SpawnX");
				this.spawnY = nBTTagCompound4.getInteger("SpawnY");
				this.spawnZ = nBTTagCompound4.getInteger("SpawnZ");
				this.worldTime = nBTTagCompound4.getLong("Time");
				this.sizeOnDisk = nBTTagCompound4.getLong("SizeOnDisk");
				this.nbtCompoundPlayer = nBTTagCompound4.getCompoundTag("Player");
			} catch (Exception exception3) {
				exception3.printStackTrace();
			}
		}

		while(this.randomSeed == 0L) {
			this.randomSeed = this.rand.nextLong();
			this.spawnX = 0;
			this.spawnY = 64;
			this.spawnZ = 0;
		}

		this.chunkProvider = new ChunkProviderLoadOrGenerate(this, this.saveDirectory, new ChunkProviderGenerate(this, this.randomSeed));
		this.saveWorld(false);
	}

	public final void spawnPlayer() {
		try {
			if(this.nbtCompoundPlayer != null) {
				this.playerEntity.readFromNBT(this.nbtCompoundPlayer);
				this.nbtCompoundPlayer = null;
			}

			this.spawnEntityInWorld(this.playerEntity);
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}

	private void saveWorld(boolean z1) {
		File file2 = new File(this.saveDirectory, "level.dat");
		NBTTagCompound nBTTagCompound3;
		(nBTTagCompound3 = new NBTTagCompound()).setLong("RandomSeed", this.randomSeed);
		nBTTagCompound3.setInteger("SpawnX", this.spawnX);
		nBTTagCompound3.setInteger("SpawnY", this.spawnY);
		nBTTagCompound3.setInteger("SpawnZ", this.spawnZ);
		nBTTagCompound3.setLong("Time", this.worldTime);
		nBTTagCompound3.setLong("SizeOnDisk", this.sizeOnDisk);
		nBTTagCompound3.setLong("LastPlayed", System.currentTimeMillis());
		NBTTagCompound nBTTagCompound4;
		if(this.playerEntity != null) {
			nBTTagCompound4 = new NBTTagCompound();
			this.playerEntity.writeToNBT(nBTTagCompound4);
			nBTTagCompound3.setCompoundTag("Player", nBTTagCompound4);
		}

		(nBTTagCompound4 = new NBTTagCompound()).setTag("Data", nBTTagCompound3);

		try {
			LoadingScreenRenderer.write(nBTTagCompound4, new FileOutputStream(file2));
		} catch (Exception exception5) {
			exception5.printStackTrace();
		}

		this.chunkProvider.saveChunks(z1);
	}

	public final int getBlockId(int i1, int i2, int i3) {
		return i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000 ? (i2 <= 0 ? Block.lavaStill.blockID : (i2 >= 128 ? 0 : this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).getBlockID(i1 & 15, i2, i3 & 15))) : 0;
	}

	public final boolean blockExists(int i1, int i2, int i3) {
		return i2 >= 0 && i2 < 128 ? this.chunkExists(i1 >> 4, i3 >> 4) : false;
	}

	private boolean chunkExists(int i1, int i2) {
		return this.chunkProvider.chunkExists(i1, i2);
	}

	private Chunk getChunkFromChunkCoords(int i1, int i2) {
		return this.chunkProvider.provideChunk(i1, i2);
	}

	public final boolean setTileNoUpdate(int i1, int i2, int i3, int i4) {
		return i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000 ? (i2 < 0 ? false : (i2 >= 128 ? false : this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).setBlockID(i1 & 15, i2, i3 & 15, i4))) : false;
	}

	public final Material getBlockMaterial(int i1, int i2, int i3) {
		return (i1 = this.getBlockId(i1, i2, i3)) == 0 ? Material.air : Block.blocksList[i1].blockMaterial;
	}

	public final int getBlockMetadata(int i1, int i2, int i3) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return 0;
			} else if(i2 >= 128) {
				return 0;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				return chunk4.getBlockMetadata(i1, i2, i3);
			}
		} else {
			return 0;
		}
	}

	public final void setBlockMetadataWithNotify(int i1, int i2, int i3, int i4) {
		this.setBlockMetadata(i1, i2, i3, i4);
	}

	private boolean setBlockMetadata(int i1, int i2, int i3, int i4) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return false;
			} else if(i2 >= 128) {
				return false;
			} else {
				Chunk chunk5 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				chunk5.setBlockMetadata(i1, i2, i3, i4);
				return true;
			}
		} else {
			return false;
		}
	}

	public final boolean setBlockWithNotify(int i1, int i2, int i3, int i4) {
		if(!this.setTileNoUpdate(i1, i2, i3, i4)) {
			return false;
		} else {
			int i5 = i4;
			i4 = i3;
			i3 = i2;
			i2 = i1;
			World world7 = this;

			for(int i6 = 0; i6 < world7.worldAccesses.size(); ++i6) {
				((IWorldAccess)world7.worldAccesses.get(i6)).markBlockAndNeighborsNeedsUpdate(i2, i3, i4);
			}

			world7.notifyBlocksOfNeighborChange(i2, i3, i4, i5);
			return true;
		}
	}

	public final void markBlocksDirtyVertical(int i1, int i2, int i3, int i4) {
		int i5;
		if(i3 > i4) {
			i5 = i4;
			i4 = i3;
			i3 = i5;
		}

		int i7 = i2;
		int i6 = i4;
		i5 = i1;
		i4 = i2;
		i3 = i3;
		i2 = i1;
		World world9 = this;

		for(int i8 = 0; i8 < world9.worldAccesses.size(); ++i8) {
			((IWorldAccess)world9.worldAccesses.get(i8)).markBlockRangeNeedsUpdate(i2, i3, i4, i5, i6, i7);
		}

	}

	public final void swap(int i1, int i2, int i3, int i4, int i5, int i6) {
		int i7 = this.getBlockId(i1, i2, i3);
		int i8 = this.getBlockMetadata(i1, i2, i3);
		int i9 = this.getBlockId(i4, i5, i6);
		int i10 = this.getBlockMetadata(i4, i5, i6);
		this.setTileNoUpdate(i1, i2, i3, i9);
		this.setBlockMetadata(i1, i2, i3, i10);
		this.setTileNoUpdate(i4, i5, i6, i7);
		this.setBlockMetadata(i4, i5, i6, i8);
		this.notifyBlocksOfNeighborChange(i1, i2, i3, i9);
		this.notifyBlocksOfNeighborChange(i4, i5, i6, i7);
	}

	public final void notifyBlocksOfNeighborChange(int i1, int i2, int i3, int i4) {
		this.notifyBlockOfNeighborChange(i1 - 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1 + 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 - 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 + 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 - 1, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 + 1, i4);
	}

	private void notifyBlockOfNeighborChange(int i1, int i2, int i3, int i4) {
		Block block5;
		if((block5 = Block.blocksList[this.getBlockId(i1, i2, i3)]) != null) {
			block5.onNeighborBlockChange(this, i1, i2, i3, i4);
		}

	}

	public final boolean canBlockSeeTheSky(int i1, int i2, int i3) {
		return this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).canBlockSeeTheSky(i1 & 15, i2, i3 & 15);
	}

	public final int getBlockLightValue(int i1, int i2, int i3) {
		return this.getBlockLightValue_do(i1, i2, i3, true);
	}

	private int getBlockLightValue_do(int i1, int i2, int i3, boolean z4) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			int i8;
			if(!z4 || (i8 = this.getBlockId(i1, i2, i3)) != Block.stairSingle.blockID && i8 != Block.tilledField.blockID) {
				if(i2 < 0) {
					return 0;
				} else if(i2 >= 128) {
					if((i8 = 15 - this.skylightSubtracted) < 0) {
						i8 = 0;
					}

					return i8;
				} else {
					Chunk chunk9 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
					i1 &= 15;
					i3 &= 15;
					return chunk9.getBlockLightValue(i1, i2, i3, this.skylightSubtracted);
				}
			} else {
				i8 = this.getBlockLightValue_do(i1, i2 + 1, i3, false);
				int i5 = this.getBlockLightValue_do(i1 + 1, i2, i3, false);
				int i6 = this.getBlockLightValue_do(i1 - 1, i2, i3, false);
				int i7 = this.getBlockLightValue_do(i1, i2, i3 + 1, false);
				i1 = this.getBlockLightValue_do(i1, i2, i3 - 1, false);
				if(i5 > i8) {
					i8 = i5;
				}

				if(i6 > i8) {
					i8 = i6;
				}

				if(i7 > i8) {
					i8 = i7;
				}

				if(i1 > i8) {
					i8 = i1;
				}

				return i8;
			}
		} else {
			return 15;
		}
	}

	public final boolean canExistingBlockSeeTheSky(int i1, int i2, int i3) {
		if(i1 >= -32000000 && i3 >= -32000000 && i1 < 32000000 && i3 <= 32000000) {
			if(i2 < 0) {
				return false;
			} else if(i2 >= 128) {
				return true;
			} else if(!this.chunkExists(i1 >> 4, i3 >> 4)) {
				return false;
			} else {
				Chunk chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4);
				i1 &= 15;
				i3 &= 15;
				return chunk4.canBlockSeeTheSky(i1, i2, i3);
			}
		} else {
			return false;
		}
	}

	public final int getHeightValue(int i1, int i2) {
		return i1 >= -32000000 && i2 >= -32000000 && i1 < 32000000 && i2 <= 32000000 ? (!this.chunkExists(i1 >> 4, i2 >> 4) ? 0 : this.getChunkFromChunkCoords(i1 >> 4, i2 >> 4).getHeightValue(i1 & 15, i2 & 15)) : 0;
	}

	public final void neighborLightPropagationChanged(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5) {
		if(this.blockExists(i2, i3, i4)) {
			if(enumSkyBlock1 == EnumSkyBlock.Sky) {
				if(this.canExistingBlockSeeTheSky(i2, i3, i4)) {
					i5 = 15;
				}
			} else if(enumSkyBlock1 == EnumSkyBlock.Block) {
				int i6 = this.getBlockId(i2, i3, i4);
				if(Block.lightValue[i6] > i5) {
					i5 = Block.lightValue[i6];
				}
			}

			if(this.getSavedLightValue(enumSkyBlock1, i2, i3, i4) != i5) {
				this.scheduleLightingUpdate(enumSkyBlock1, i2, i3, i4, i2, i3, i4);
			}

		}
	}

	public final int getSavedLightValue(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4) {
		if(enumSkyBlock1 == EnumSkyBlock.Sky) {
			if(i2 < -32000000 || i4 < -32000000 || i2 >= 32000000 || i4 > 32000000) {
				return 15;
			}

			if(i3 < 0) {
				return 15;
			}

			if(i3 >= 128) {
				return 15;
			}
		} else {
			if(i2 < -32000000 || i4 < -32000000 || i2 >= 32000000 || i4 > 32000000) {
				return 0;
			}

			if(i3 < 0) {
				return 0;
			}

			if(i3 >= 128) {
				return 0;
			}
		}

		if(!this.chunkExists(i2 >> 4, i4 >> 4)) {
			return 0;
		} else {
			Chunk chunk5 = this.getChunkFromChunkCoords(i2 >> 4, i4 >> 4);
			i2 &= 15;
			i4 &= 15;
			return chunk5.getSavedLightValue(enumSkyBlock1, i2, i3, i4);
		}
	}

	public final float getBrightness(int i1, int i2, int i3) {
		return lightBrightnessTable[this.getBlockLightValue(i1, i2, i3)];
	}

	public final boolean isDaytime() {
		return this.skylightSubtracted < 8;
	}

	public final MovingObjectPosition rayTraceBlocks(Vec3D vec3D1, Vec3D vec3D2) {
		if(!Double.isNaN(vec3D1.xCoord) && !Double.isNaN(vec3D1.yCoord) && !Double.isNaN(vec3D1.zCoord)) {
			if(!Double.isNaN(vec3D2.xCoord) && !Double.isNaN(vec3D2.yCoord) && !Double.isNaN(vec3D2.zCoord)) {
				int i3 = MathHelper.floor_double(vec3D2.xCoord);
				int i4 = MathHelper.floor_double(vec3D2.yCoord);
				int i5 = MathHelper.floor_double(vec3D2.zCoord);
				int i6 = MathHelper.floor_double(vec3D1.xCoord);
				int i7 = MathHelper.floor_double(vec3D1.yCoord);
				int i8 = MathHelper.floor_double(vec3D1.zCoord);
				int i9 = 20;

				Block block11;
				int i30;
				MovingObjectPosition movingObjectPosition31;
				do {
					if(i9-- < 0) {
						return null;
					}

					if(Double.isNaN(vec3D1.xCoord) || Double.isNaN(vec3D1.yCoord) || Double.isNaN(vec3D1.zCoord)) {
						return null;
					}

					if(i6 == i3 && i7 == i4 && i8 == i5) {
						return null;
					}

					double d10 = 999.0D;
					double d12 = 999.0D;
					double d14 = 999.0D;
					if(i3 > i6) {
						d10 = (double)i6 + 1.0D;
					}

					if(i3 < i6) {
						d10 = (double)i6;
					}

					if(i4 > i7) {
						d12 = (double)i7 + 1.0D;
					}

					if(i4 < i7) {
						d12 = (double)i7;
					}

					if(i5 > i8) {
						d14 = (double)i8 + 1.0D;
					}

					if(i5 < i8) {
						d14 = (double)i8;
					}

					double d16 = 999.0D;
					double d18 = 999.0D;
					double d20 = 999.0D;
					double d22 = vec3D2.xCoord - vec3D1.xCoord;
					double d24 = vec3D2.yCoord - vec3D1.yCoord;
					double d26 = vec3D2.zCoord - vec3D1.zCoord;
					if(d10 != 999.0D) {
						d16 = (d10 - vec3D1.xCoord) / d22;
					}

					if(d12 != 999.0D) {
						d18 = (d12 - vec3D1.yCoord) / d24;
					}

					if(d14 != 999.0D) {
						d20 = (d14 - vec3D1.zCoord) / d26;
					}

					byte b28;
					if(d16 < d18 && d16 < d20) {
						if(i3 > i6) {
							b28 = 4;
						} else {
							b28 = 5;
						}

						vec3D1.xCoord = d10;
						vec3D1.yCoord += d24 * d16;
						vec3D1.zCoord += d26 * d16;
					} else if(d18 < d20) {
						if(i4 > i7) {
							b28 = 0;
						} else {
							b28 = 1;
						}

						vec3D1.xCoord += d22 * d18;
						vec3D1.yCoord = d12;
						vec3D1.zCoord += d26 * d18;
					} else {
						if(i5 > i8) {
							b28 = 2;
						} else {
							b28 = 3;
						}

						vec3D1.xCoord += d22 * d20;
						vec3D1.yCoord += d24 * d20;
						vec3D1.zCoord = d14;
					}

					Vec3D vec3D29;
					i6 = (int)((vec3D29 = new Vec3D(vec3D1.xCoord, vec3D1.yCoord, vec3D1.zCoord)).xCoord = (double)MathHelper.floor_double(vec3D1.xCoord));
					if(b28 == 5) {
						--i6;
						++vec3D29.xCoord;
					}

					i7 = (int)(vec3D29.yCoord = (double)MathHelper.floor_double(vec3D1.yCoord));
					if(b28 == 1) {
						--i7;
						++vec3D29.yCoord;
					}

					i8 = (int)(vec3D29.zCoord = (double)MathHelper.floor_double(vec3D1.zCoord));
					if(b28 == 3) {
						--i8;
						++vec3D29.zCoord;
					}

					i30 = this.getBlockId(i6, i7, i8);
					block11 = Block.blocksList[i30];
				} while(i30 <= 0 || !block11.isCollidable() || (movingObjectPosition31 = block11.collisionRayTrace(this, i6, i7, i8, vec3D1, vec3D2)) == null);

				return movingObjectPosition31;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final void playSoundAtEntity(Entity entity1, String string2, float f3, float f4) {
		for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
			float f6 = 16.0F;
			if(f3 > 1.0F) {
				f6 = 16.0F * f3;
			}

			if(this.playerEntity.getDistanceSqToEntity(entity1) < (double)(f6 * f6)) {
				((IWorldAccess)this.worldAccesses.get(i5)).playSound(string2, entity1.posX, entity1.posY - (double)entity1.yOffset, entity1.posZ, f3, f4);
			}
		}

	}

	public final void playSoundEffect(double d1, double d3, double d5, String string7, float f8, float f9) {
		try {
			for(int i10 = 0; i10 < this.worldAccesses.size(); ++i10) {
				float f11 = 16.0F;
				if(f8 > 1.0F) {
					f11 = 16.0F * f8;
				}

				double d12 = d1 - this.playerEntity.posX;
				double d14 = d3 - this.playerEntity.posY;
				double d16 = d5 - this.playerEntity.posZ;
				if(d12 * d12 + d14 * d14 + d16 * d16 < (double)(f11 * f11)) {
					((IWorldAccess)this.worldAccesses.get(i10)).playSound(string7, d1, d3, d5, f8, f9);
				}
			}

		} catch (Exception exception18) {
			exception18.printStackTrace();
		}
	}

	public final void spawnParticle(String string1, double d2, double d4, double d6, double d8, double d10, double d12) {
		for(int i14 = 0; i14 < this.worldAccesses.size(); ++i14) {
			((IWorldAccess)this.worldAccesses.get(i14)).spawnParticle(string1, d2, d4, d6, d8, d10, d12);
		}

	}

	public final void spawnEntityInWorld(Entity entity1) {
		int i2 = MathHelper.floor_double(entity1.posX / 16.0D);
		int i3 = MathHelper.floor_double(entity1.posZ / 16.0D);
		if(!this.chunkExists(i2, i3)) {
			System.out.println("Failed to add entity " + entity1);
		} else {
			this.getChunkFromChunkCoords(i2, i3).addEntity(entity1);
			this.loadedEntityList.add(entity1);

			for(i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
				((IWorldAccess)this.worldAccesses.get(i2)).obtainEntitySkin(entity1);
			}

		}
	}

	public static void setEntityDead(Entity entity0) {
		entity0.isDead = true;
	}

	public final void addWorldAccess(IWorldAccess iWorldAccess1) {
		this.worldAccesses.add(iWorldAccess1);
	}

	public final void removeWorldAccess(IWorldAccess iWorldAccess1) {
		this.worldAccesses.remove(iWorldAccess1);
	}

	public final List getCollidingBoundingBoxes(AxisAlignedBB axisAlignedBB1) {
		ArrayList arrayList2 = new ArrayList();
		int i3 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i4 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i5 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i6 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i7 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i8 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i9 = i5; i9 < i6; ++i9) {
				for(int i10 = i7; i10 < i8; ++i10) {
					Block block11;
					AxisAlignedBB axisAlignedBB12;
					if((block11 = Block.blocksList[this.getBlockId(i3, i9, i10)]) != null && (axisAlignedBB12 = block11.getCollisionBoundingBoxFromPool(i3, i9, i10)) != null && axisAlignedBB1.intersectsWith(axisAlignedBB12)) {
						arrayList2.add(axisAlignedBB12);
					}
				}
			}
		}

		return arrayList2;
	}

	public final Vec3D getSkyColor(float f1) {
		if((f1 = MathHelper.cos(this.getCelestialAngle(f1) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		float f2 = (float)(this.skyColor >> 16 & 255L) / 255.0F;
		float f3 = (float)(this.skyColor >> 8 & 255L) / 255.0F;
		float f4 = (float)(this.skyColor & 255L) / 255.0F;
		f2 *= f1;
		f3 *= f1;
		f4 *= f1;
		return new Vec3D((double)f2, (double)f3, (double)f4);
	}

	public final float getCelestialAngle(float f1) {
		int i2;
		return ((float)(i2 = (int)(this.worldTime % 24000L)) + f1) / 24000.0F - 0.15F;
	}

	public final Vec3D getCloudColor(float f1) {
		if((f1 = MathHelper.cos(this.getCelestialAngle(f1) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		float f2 = (float)(this.cloudColor >> 16 & 255L) / 255.0F;
		float f3 = (float)(this.cloudColor >> 8 & 255L) / 255.0F;
		float f4 = (float)(this.cloudColor & 255L) / 255.0F;
		f2 *= f1 * 0.9F + 0.1F;
		f3 *= f1 * 0.9F + 0.1F;
		f4 *= f1 * 0.85F + 0.15F;
		return new Vec3D((double)f2, (double)f3, (double)f4);
	}

	public final Vec3D getFogColor(float f1) {
		if((f1 = MathHelper.cos(this.getCelestialAngle(f1) * (float)Math.PI * 2.0F) * 2.0F + 0.5F) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		float f2 = (float)(this.fogColor >> 16 & 255L) / 255.0F;
		float f3 = (float)(this.fogColor >> 8 & 255L) / 255.0F;
		float f4 = (float)(this.fogColor & 255L) / 255.0F;
		f2 *= f1 * 0.94F + 0.06F;
		f3 *= f1 * 0.94F + 0.06F;
		f4 *= f1 * 0.91F + 0.09F;
		return new Vec3D((double)f2, (double)f3, (double)f4);
	}

	public final float getStarBrightness(float f1) {
		f1 = this.getCelestialAngle(f1);
		if((f1 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.75F)) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		return f1 * f1 * 0.5F;
	}

	public final void scheduleBlockUpdate(int i1, int i2, int i3, int i4) {
		NextTickListEntry nextTickListEntry5 = new NextTickListEntry(i1, i2, i3, i4);
		if(i4 > 0) {
			i3 = Block.blocksList[i4].tickRate();
			nextTickListEntry5.scheduledTime = i3;
		}

		this.unloadedEntityList.add(nextTickListEntry5);
	}

	public final void levelEntities() {
		int i1;
		for(i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
			Entity entity2;
			int i3;
			int i4;
			int i5;
			if(!(entity2 = (Entity)this.loadedEntityList.get(i1)).isDead) {
				i3 = MathHelper.floor_double(entity2.posX / 16.0D);
				i4 = MathHelper.floor_double(entity2.posY / 16.0D);
				i5 = MathHelper.floor_double(entity2.posZ / 16.0D);
				entity2.lastTickPosX = entity2.posX;
				entity2.lastTickPosY = entity2.posY;
				entity2.lastTickPosZ = entity2.posZ;
				entity2.prevRotationYaw = entity2.rotationYaw;
				entity2.prevRotationPitch = entity2.rotationPitch;
				entity2.onUpdate();
				int i6 = MathHelper.floor_double(entity2.posX / 16.0D);
				int i7 = MathHelper.floor_double(entity2.posY / 16.0D);
				int i8 = MathHelper.floor_double(entity2.posZ / 16.0D);
				if(i3 != i6 || i4 != i7 || i5 != i8) {
					if(this.chunkExists(i3, i5)) {
						this.getChunkFromChunkCoords(i3, i5).removeEntityAtIndex(entity2, i4);
					}

					if(this.chunkExists(i6, i8)) {
						this.getChunkFromChunkCoords(i6, i8).addEntity(entity2);
					} else {
						entity2.isDead = true;
					}
				}
			}

			if(entity2.isDead) {
				i3 = MathHelper.floor_double(entity2.posX / 16.0D);
				i4 = MathHelper.floor_double(entity2.posZ / 16.0D);
				if(this.chunkExists(i3, i4)) {
					this.getChunkFromChunkCoords(i3, i4).removeEntityAtIndex(entity2, MathHelper.floor_double(entity2.posY / 16.0D));
				}

				this.loadedEntityList.remove(i1--);

				for(i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
					((IWorldAccess)this.worldAccesses.get(i5)).releaseEntitySkin(entity2);
				}
			}
		}

		for(i1 = 0; i1 < this.loadedTileEntityList.size(); ++i1) {
			((TileEntity)this.loadedTileEntityList.get(i1)).updateEntity();
		}

	}

	public final boolean checkIfAABBIsClear1(AxisAlignedBB axisAlignedBB1) {
		List list3 = this.getEntitiesWithinAABBExcludingEntity((Entity)null, axisAlignedBB1);

		for(int i2 = 0; i2 < list3.size(); ++i2) {
			if(((Entity)list3.get(i2)).preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public final boolean getIsAnyLiquid(AxisAlignedBB axisAlignedBB1) {
		int i2 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i3 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i4 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i5 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i6 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i7 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);
		if(axisAlignedBB1.minX < 0.0D) {
			--i2;
		}

		if(axisAlignedBB1.minY < 0.0D) {
			--i4;
		}

		if(axisAlignedBB1.minZ < 0.0D) {
			--i6;
		}

		for(int i10 = i2; i10 < i3; ++i10) {
			for(i2 = i4; i2 < i5; ++i2) {
				for(int i8 = i6; i8 < i7; ++i8) {
					Block block9;
					if((block9 = Block.blocksList[this.getBlockId(i10, i2, i8)]) != null && block9.blockMaterial.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isBoundingBoxBurning(AxisAlignedBB axisAlignedBB1) {
		int i2 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i3 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i4 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i5 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i6 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i10 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);

		for(i2 = i2; i2 < i3; ++i2) {
			for(int i7 = i4; i7 < i5; ++i7) {
				for(int i8 = i6; i8 < i10; ++i8) {
					int i9;
					if((i9 = this.getBlockId(i2, i7, i8)) == Block.fire.blockID || i9 == Block.lavaMoving.blockID || i9 == Block.lavaStill.blockID) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean isMaterialInBB(AxisAlignedBB axisAlignedBB1, Material material2) {
		int i3 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i4 = MathHelper.floor_double(axisAlignedBB1.maxX + 1.0D);
		int i5 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i6 = MathHelper.floor_double(axisAlignedBB1.maxY + 1.0D);
		int i7 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i11 = MathHelper.floor_double(axisAlignedBB1.maxZ + 1.0D);

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i8 = i5; i8 < i6; ++i8) {
				for(int i9 = i7; i9 < i11; ++i9) {
					Block block10;
					if((block10 = Block.blocksList[this.getBlockId(i3, i8, i9)]) != null && block10.blockMaterial == material2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final void createExplosion(Entity entity1, double d2, double d4, double d6, float f8) {
		new Explosion();
		float f3 = f8;
		double d15 = d6;
		double d13 = d4;
		double d11 = d2;
		Entity entity67 = entity1;
		World world66 = this;
		this.playSoundEffect(d2, d4, d6, "random.explode", 4.0F, (1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F) * 0.7F);
		HashSet hashSet68 = new HashSet();

		int i7;
		double d32;
		double d34;
		double d36;
		int i69;
		int i71;
		for(i69 = 0; i69 < 16; ++i69) {
			for(i7 = 0; i7 < 16; ++i7) {
				for(i71 = 0; i71 < 16; ++i71) {
					if(i69 == 0 || i69 == 15 || i7 == 0 || i7 == 15 || i71 == 0 || i71 == 15) {
						double d23 = (double)((float)i69 / 15.0F * 2.0F - 1.0F);
						double d25 = (double)((float)i7 / 15.0F * 2.0F - 1.0F);
						double d27 = (double)((float)i71 / 15.0F * 2.0F - 1.0F);
						double d29 = Math.sqrt(d23 * d23 + d25 * d25 + d27 * d27);
						d23 /= d29;
						d25 /= d29;
						d27 /= d29;
						float f31 = f3 * (0.7F + world66.rand.nextFloat() * 0.6F);
						d32 = d11;
						d34 = d13;

						for(d36 = d15; f31 > 0.0F; f31 -= 0.22500001F) {
							int i39 = MathHelper.floor_double(d32);
							int i40 = MathHelper.floor_double(d34);
							int i41 = MathHelper.floor_double(d36);
							int i42;
							if((i42 = world66.getBlockId(i39, i40, i41)) > 0) {
								f31 -= (Block.blocksList[i42].getExplosionResistance() + 0.3F) * 0.3F;
							}

							if(f31 > 0.0F) {
								hashSet68.add(new ChunkPosition(i39, i40, i41));
							}

							d32 += d23 * (double)0.3F;
							d34 += d25 * (double)0.3F;
							d36 += d27 * (double)0.3F;
						}
					}
				}
			}
		}

		f3 *= 2.0F;
		i69 = MathHelper.floor_double(d11 - (double)f3 - 1.0D);
		i7 = MathHelper.floor_double(d11 + (double)f3 + 1.0D);
		i71 = MathHelper.floor_double(d13 - (double)f3 - 1.0D);
		int i72 = MathHelper.floor_double(d13 + (double)f3 + 1.0D);
		int i24 = MathHelper.floor_double(d15 - (double)f3 - 1.0D);
		int i73 = MathHelper.floor_double(d15 + (double)f3 + 1.0D);
		List list26 = world66.getEntitiesWithinAABBExcludingEntity(entity1, new AxisAlignedBB((double)i69, (double)i71, (double)i24, (double)i7, (double)i72, (double)i73));
		Vec3D vec3D74 = new Vec3D(d11, d13, d15);

		double d38;
		double d65;
		double d82;
		for(int i28 = 0; i28 < list26.size(); ++i28) {
			Entity entity70;
			Entity entity76;
			double d59 = (entity70 = entity76 = (Entity)list26.get(i28)).posX - d11;
			double d61 = entity70.posY - d13;
			double d63 = entity70.posZ - d15;
			double d30;
			if((d30 = (double)MathHelper.sqrt_double(d59 * d59 + d61 * d61 + d63 * d63) / (double)f3) <= 1.0D) {
				d32 = entity76.posX - d11;
				d34 = entity76.posY - d13;
				d36 = entity76.posZ - d15;
				d38 = (double)MathHelper.sqrt_double(d32 * d32 + d34 * d34 + d36 * d36);
				d32 /= d38;
				d34 /= d38;
				d36 /= d38;
				d82 = (double)world66.getBlockDensity(vec3D74, entity76.boundingBox);
				d65 = (1.0D - d30) * d82;
				entity76.attackEntityFrom(entity67, (int)((d65 * d65 + d65) / 2.0D * 8.0D * (double)f3 + 1.0D));
				entity76.motionX += d32 * d65;
				entity76.motionY += d34 * d65;
				entity76.motionZ += d36 * d65;
			}
		}

		f3 = f8;
		ArrayList arrayList75;
		(arrayList75 = new ArrayList()).addAll(hashSet68);

		for(int i77 = arrayList75.size() - 1; i77 >= 0; --i77) {
			ChunkPosition chunkPosition78;
			int i79 = (chunkPosition78 = (ChunkPosition)arrayList75.get(i77)).x;
			int i80 = chunkPosition78.y;
			int i33 = chunkPosition78.z;
			int i81 = world66.getBlockId(i79, i80, i33);

			for(int i35 = 0; i35 <= 0; ++i35) {
				d36 = (double)((float)i79 + world66.rand.nextFloat());
				d38 = (double)((float)i80 + world66.rand.nextFloat());
				d82 = (double)((float)i33 + world66.rand.nextFloat());
				d65 = d36 - d11;
				double d44 = d38 - d13;
				double d46 = d82 - d15;
				double d48 = (double)MathHelper.sqrt_double(d65 * d65 + d44 * d44 + d46 * d46);
				d65 /= d48;
				d44 /= d48;
				d46 /= d48;
				double d50 = (d50 = 0.5D / (d48 / (double)f3 + 0.1D)) * (double)(world66.rand.nextFloat() * world66.rand.nextFloat() + 0.3F);
				d65 *= d50;
				d44 *= d50;
				d46 *= d50;
				world66.spawnParticle("explode", (d36 + d11) / 2.0D, (d38 + d13) / 2.0D, (d82 + d15) / 2.0D, d65, d44, d46);
				world66.spawnParticle("smoke", d36, d38, d82, d65, d44, d46);
			}

			if(i81 > 0) {
				Block.blocksList[i81].dropBlockAsItemWithChance(world66, i79, i80, i33, world66.getBlockMetadata(i79, i80, i33), 0.3F);
				world66.setBlockWithNotify(i79, i80, i33, 0);
				Block.blocksList[i81].onBlockDestroyedByExplosion(world66, i79, i80, i33);
			}
		}

	}

	public final float getBlockDensity(Vec3D vec3D1, AxisAlignedBB axisAlignedBB2) {
		double d3 = 1.0D / ((axisAlignedBB2.maxX - axisAlignedBB2.minX) * 2.0D + 1.0D);
		double d5 = 1.0D / ((axisAlignedBB2.maxY - axisAlignedBB2.minY) * 2.0D + 1.0D);
		double d7 = 1.0D / ((axisAlignedBB2.maxZ - axisAlignedBB2.minZ) * 2.0D + 1.0D);
		int i9 = 0;
		int i10 = 0;

		for(float f11 = 0.0F; f11 <= 1.0F; f11 = (float)((double)f11 + d3)) {
			for(float f12 = 0.0F; f12 <= 1.0F; f12 = (float)((double)f12 + d5)) {
				for(float f13 = 0.0F; f13 <= 1.0F; f13 = (float)((double)f13 + d7)) {
					double d14 = axisAlignedBB2.minX + (axisAlignedBB2.maxX - axisAlignedBB2.minX) * (double)f11;
					double d16 = axisAlignedBB2.minY + (axisAlignedBB2.maxY - axisAlignedBB2.minY) * (double)f12;
					double d18 = axisAlignedBB2.minZ + (axisAlignedBB2.maxZ - axisAlignedBB2.minZ) * (double)f13;
					if(this.rayTraceBlocks(new Vec3D(d14, d16, d18), vec3D1) == null) {
						++i9;
					}

					++i10;
				}
			}
		}

		return (float)i9 / (float)i10;
	}

	public final void extinguishFire(int i1, int i2, int i3, int i4) {
		if(i4 == 0) {
			--i2;
		}

		if(i4 == 1) {
			++i2;
		}

		if(i4 == 2) {
			--i3;
		}

		if(i4 == 3) {
			++i3;
		}

		if(i4 == 4) {
			--i1;
		}

		if(i4 == 5) {
			++i1;
		}

		if(this.getBlockId(i1, i2, i3) == Block.fire.blockID) {
			this.playSoundEffect((double)((float)i1 + 0.5F), (double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);
			this.setBlockWithNotify(i1, i2, i3, 0);
		}

	}

	public final String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size() + "/ Counted: " + this.getDebugCountedEntities();
	}

	private int getDebugCountedEntities() {
		int i1 = MathHelper.floor_double(this.playerEntity.posX / 16.0D);
		int i2 = MathHelper.floor_double(this.playerEntity.posZ / 16.0D);
		int i3 = 0;

		for(int i4 = i1 - 32; i4 <= i1 + 32; ++i4) {
			for(int i5 = i2 - 32; i5 <= i2 + 32; ++i5) {
				if(this.chunkExists(i4, i5)) {
					i3 += this.getChunkFromChunkCoords(i4, i5).getDebugCountedEntities();
				}
			}
		}

		return i3;
	}

	public final Entity getPlayerEntity() {
		return this.playerEntity;
	}

	public final TileEntity getBlockTileEntity(int i1, int i2, int i3) {
		Chunk chunk4;
		return (chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4)) != null ? chunk4.getChunkBlockTileEntity(i1 & 15, i2, i3 & 15) : null;
	}

	public final void setBlockTileEntity(int i1, int i2, int i3, TileEntity tileEntity4) {
		Chunk chunk5;
		if((chunk5 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4)) != null) {
			chunk5.setChunkBlockTileEntity(i1 & 15, i2, i3 & 15, tileEntity4);
		}

	}

	public final void removeBlockTileEntity(int i1, int i2, int i3) {
		Chunk chunk4;
		if((chunk4 = this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4)) != null) {
			chunk4.removeChunkBlockTileEntity(i1 & 15, i2, i3 & 15);
		}

	}

	public final boolean isSolid(int i1, int i2, int i3) {
		Block block4;
		return (block4 = Block.blocksList[this.getBlockId(i1, i2, i3)]) == null ? false : block4.isOpaqueCube();
	}

	public final void saveWorldIndirectly() {
		this.saveWorld(true);
	}

	public final int lightUpdatesNeeded() {
		return this.lightingToUpdate.size();
	}

	public final boolean updatingLighting() {
		int i1 = 100000;

		while(this.lightingToUpdate.size() > 0) {
			--i1;
			if(i1 <= 0) {
				return true;
			}

			MetadataChunkBlock metadataChunkBlock10000 = (MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1);
			World world3 = this;
			MetadataChunkBlock metadataChunkBlock2 = metadataChunkBlock10000;

			for(int i4 = metadataChunkBlock10000.x; i4 <= metadataChunkBlock2.maxX; ++i4) {
				for(int i5 = metadataChunkBlock2.z; i5 <= metadataChunkBlock2.maxZ; ++i5) {
					if(world3.blockExists(i4, 0, i5)) {
						for(int i6 = metadataChunkBlock2.y; i6 <= metadataChunkBlock2.maxY; ++i6) {
							if(i6 >= 0 && i6 < 128) {
								int i7 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6, i5);
								int i8 = world3.getBlockId(i4, i6, i5);
								int i9;
								if((i9 = Block.lightOpacity[i8]) == 0) {
									i9 = 1;
								}

								int i10 = 0;
								if(metadataChunkBlock2.skyBlock == EnumSkyBlock.Sky) {
									if(world3.canExistingBlockSeeTheSky(i4, i6, i5)) {
										i10 = 15;
									}
								} else if(metadataChunkBlock2.skyBlock == EnumSkyBlock.Block) {
									i10 = Block.lightValue[i8];
								}

								int i11;
								int i12;
								if(i9 >= 15 && i10 == 0) {
									i8 = 0;
								} else {
									i8 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4 - 1, i6, i5);
									i11 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4 + 1, i6, i5);
									i12 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6 - 1, i5);
									int i13 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6 + 1, i5);
									int i14 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6, i5 - 1);
									int i15 = world3.getSavedLightValue(metadataChunkBlock2.skyBlock, i4, i6, i5 + 1);
									i8 = i8;
									if(i11 > i8) {
										i8 = i11;
									}

									if(i12 > i8) {
										i8 = i12;
									}

									if(i13 > i8) {
										i8 = i13;
									}

									if(i14 > i8) {
										i8 = i14;
									}

									if(i15 > i8) {
										i8 = i15;
									}

									if((i8 -= i9) < 0) {
										i8 = 0;
									}

									if(i10 > i8) {
										i8 = i10;
									}
								}

								if(i7 != i8) {
									i12 = i5;
									i11 = i6;
									i10 = i4;
									EnumSkyBlock enumSkyBlock17 = metadataChunkBlock2.skyBlock;
									World world16 = world3;
									if(i4 >= -32000000 && i5 >= -32000000 && i4 < 32000000 && i5 <= 32000000 && i6 >= 0 && i6 < 128 && world3.chunkExists(i4 >> 4, i5 >> 4)) {
										world3.getChunkFromChunkCoords(i4 >> 4, i5 >> 4).setLightValue(enumSkyBlock17, i4 & 15, i6, i5 & 15, i8);

										for(i9 = 0; i9 < world16.worldAccesses.size(); ++i9) {
											((IWorldAccess)world16.worldAccesses.get(i9)).markBlockAndNeighborsNeedsUpdate(i10, i11, i12);
										}
									}

									if(--i8 < 0) {
										i8 = 0;
									}

									world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4 - 1, i6, i5, i8);
									world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6 - 1, i5, i8);
									world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6, i5 - 1, i8);
									if(i4 + 1 >= metadataChunkBlock2.maxX) {
										world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4 + 1, i6, i5, i8);
									}

									if(i6 + 1 >= metadataChunkBlock2.maxY) {
										world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6 + 1, i5, i8);
									}

									if(i5 + 1 >= metadataChunkBlock2.maxZ) {
										world3.neighborLightPropagationChanged(metadataChunkBlock2.skyBlock, i4, i6, i5 + 1, i8);
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public final void scheduleLightingUpdate(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5, int i6, int i7) {
		int i8 = this.lightingToUpdate.size();
		int i9 = 4;
		if(4 > i8) {
			i9 = i8;
		}

		for(i8 = 0; i8 < i9; ++i8) {
			MetadataChunkBlock metadataChunkBlock10;
			if((metadataChunkBlock10 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - i8 - 1)).skyBlock == enumSkyBlock1) {
				boolean z10000;
				if(i2 >= metadataChunkBlock10.x && i3 >= metadataChunkBlock10.y && i4 >= metadataChunkBlock10.z && i5 <= metadataChunkBlock10.maxX && i6 <= metadataChunkBlock10.maxY && i7 <= metadataChunkBlock10.maxZ) {
					z10000 = true;
				} else if(i2 >= metadataChunkBlock10.x - 1 && i3 >= metadataChunkBlock10.y - 1 && i4 >= metadataChunkBlock10.z - 1 && i5 <= metadataChunkBlock10.maxX + 1 && i6 <= metadataChunkBlock10.maxY + 1 && i7 <= metadataChunkBlock10.maxZ + 1) {
					if(i2 < metadataChunkBlock10.x) {
						metadataChunkBlock10.x = i2;
					}

					if(i3 < metadataChunkBlock10.y) {
						metadataChunkBlock10.y = i3;
					}

					if(i4 < metadataChunkBlock10.z) {
						metadataChunkBlock10.z = i4;
					}

					if(i5 > metadataChunkBlock10.maxX) {
						metadataChunkBlock10.maxX = i5;
					}

					if(i6 > metadataChunkBlock10.maxY) {
						metadataChunkBlock10.maxY = i6;
					}

					if(i7 > metadataChunkBlock10.maxZ) {
						metadataChunkBlock10.maxZ = i7;
					}

					z10000 = true;
				} else {
					z10000 = false;
				}

				if(z10000) {
					return;
				}
			}
		}

		this.lightingToUpdate.add(new MetadataChunkBlock(enumSkyBlock1, i2, i3, i4, i5, i6, i7));
		if(this.lightingToUpdate.size() > 1000000) {
			while(this.lightingToUpdate.size() > 500000) {
				this.updatingLighting();
			}
		}

	}

	public final void restartTimeOfDay() {
		if(!this.loadedEntityList.contains(this.playerEntity)) {
			this.spawnEntityInWorld(this.playerEntity);
		}

		float f1 = 1.0F;
		f1 = this.getCelestialAngle(1.0F);
		if((f1 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F)) < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		int i8;
		if((i8 = (int)(f1 * 13.0F)) != this.skylightSubtracted) {
			this.skylightSubtracted = i8;

			for(i8 = 0; i8 < this.worldAccesses.size(); ++i8) {
				((IWorldAccess)this.worldAccesses.get(i8)).updateAllRenderers();
			}
		}

		++this.worldTime;
		if(this.worldTime % 200L == 0L) {
			this.saveWorld(false);
		}

		if((i8 = this.unloadedEntityList.size()) > 200) {
			i8 = 200;
		}

		int i2;
		int i4;
		for(i2 = 0; i2 < i8; ++i2) {
			NextTickListEntry nextTickListEntry3;
			if((nextTickListEntry3 = (NextTickListEntry)this.unloadedEntityList.remove(0)).scheduledTime > 0) {
				--nextTickListEntry3.scheduledTime;
				this.unloadedEntityList.add(nextTickListEntry3);
			} else if(this.blockExists(nextTickListEntry3.xCoord, nextTickListEntry3.yCoord, nextTickListEntry3.zCoord) && (i4 = this.getBlockId(nextTickListEntry3.xCoord, nextTickListEntry3.yCoord, nextTickListEntry3.zCoord)) == nextTickListEntry3.blockID && i4 > 0) {
				Block.blocksList[i4].updateTick(this, nextTickListEntry3.xCoord, nextTickListEntry3.yCoord, nextTickListEntry3.zCoord, this.rand);
			}
		}

		i8 = MathHelper.floor_double(this.playerEntity.posX);
		i2 = MathHelper.floor_double(this.playerEntity.posZ);

		for(int i9 = 0; i9 < 32000; ++i9) {
			this.updateLCG = this.updateLCG * 3 + this.DIST_HASH_MAGIC;
			int i5 = ((i4 = this.updateLCG >> 2) & 255) - 128 + i8;
			int i6 = (i4 >> 8 & 255) - 128 + i2;
			i4 = i4 >> 16 & 127;
			int i7 = this.getBlockId(i5, i4, i6);
			if(Block.tickOnLoad[i7]) {
				Block.blocksList[i7].updateTick(this, i5, i4, i6, this.rand);
			}
		}

	}

	public final void randomDisplayUpdates(int i1, int i2, int i3) {
		Random random4 = new Random();

		for(int i5 = 0; i5 < 1000; ++i5) {
			int i6 = i1 + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i7 = i2 + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i8 = i3 + this.rand.nextInt(16) - this.rand.nextInt(16);
			int i9;
			if((i9 = this.getBlockId(i6, i7, i8)) > 0) {
				Block.blocksList[i9].randomDisplayTick(this, i6, i7, i8, random4);
			}
		}

	}

	public final List getEntitiesWithinAABBExcludingEntity(Entity entity1, AxisAlignedBB axisAlignedBB2) {
		int i3 = MathHelper.floor_double((axisAlignedBB2.minX - 2.0D) / 16.0D);
		int i4 = MathHelper.floor_double((axisAlignedBB2.maxX + 2.0D) / 16.0D);
		int i5 = MathHelper.floor_double((axisAlignedBB2.minZ - 2.0D) / 16.0D);
		int i6 = MathHelper.floor_double((axisAlignedBB2.maxZ + 2.0D) / 16.0D);
		ArrayList arrayList7 = new ArrayList();

		for(i3 = i3; i3 <= i4; ++i3) {
			for(int i8 = i5; i8 <= i6; ++i8) {
				if(this.chunkExists(i3, i8)) {
					this.getChunkFromChunkCoords(i3, i8).getEntitiesWithinAABBForEntity(entity1, axisAlignedBB2, arrayList7);
				}
			}
		}

		return arrayList7;
	}

	public final List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public final void updateTileEntityChunkAndDoNothing(int i1, int i2, int i3) {
		if(this.blockExists(i1, i2, i3)) {
			this.getChunkFromChunkCoords(i1 >> 4, i3 >> 4).isModified = true;
		}

	}

	public final int countEntities(Class class1) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.loadedEntityList.size(); ++i3) {
			Entity entity4 = (Entity)this.loadedEntityList.get(i3);
			if(class1.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	public final void addLoadedEntities(List list1) {
		this.loadedEntityList.addAll(list1);

		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			IWorldAccess iWorldAccess3 = (IWorldAccess)this.worldAccesses.get(i2);

			for(int i4 = 0; i4 < list1.size(); ++i4) {
				iWorldAccess3.obtainEntitySkin((Entity)list1.get(i4));
			}
		}

	}

	public final void unloadEntities(List list1) {
		this.loadedEntityList.removeAll(list1);

		for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
			IWorldAccess iWorldAccess3 = (IWorldAccess)this.worldAccesses.get(i2);

			for(int i4 = 0; i4 < list1.size(); ++i4) {
				iWorldAccess3.releaseEntitySkin((Entity)list1.get(i4));
			}
		}

	}

	static {
		for(int i0 = 0; i0 <= 15; ++i0) {
			float f1 = 1.0F - (float)i0 / 15.0F;
			lightBrightnessTable[i0] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.95F + 0.05F;
		}

	}
}
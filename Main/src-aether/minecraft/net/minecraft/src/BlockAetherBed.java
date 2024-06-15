package net.minecraft.src;

import java.util.Iterator;
import java.util.Random;

public class BlockAetherBed extends Block {
	public static final int[][] headBlockToFootBlockMap = new int[][]{{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

	public BlockAetherBed(int i) {
		super(i, 134, Material.cloth);
		this.setBounds();
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if(world.multiplayerWorld) {
			return true;
		} else {
			int l = world.getBlockMetadata(i, j, k);
			if(!isBlockFootOfBed(l)) {
				int enumstatus = getDirectionFromMetadata(l);
				i += headBlockToFootBlockMap[enumstatus][0];
				k += headBlockToFootBlockMap[enumstatus][1];
				if(world.getBlockId(i, j, k) != this.blockID) {
					return true;
				}

				l = world.getBlockMetadata(i, j, k);
			}

			if(isBedOccupied(l)) {
				EntityPlayer enumstatus1 = null;
				Iterator iterator = world.playerEntities.iterator();

				while(iterator.hasNext()) {
					EntityPlayer entityplayer2 = (EntityPlayer)iterator.next();
					if(entityplayer2.isPlayerSleeping()) {
						ChunkCoordinates chunkcoordinates = entityplayer2.bedChunkCoordinates;
						if(chunkcoordinates.x == i && chunkcoordinates.y == j && chunkcoordinates.z == k) {
							enumstatus1 = entityplayer2;
						}
					}
				}

				if(enumstatus1 != null) {
					entityplayer.addChatMessage("tile.bed.occupied");
					return true;
				}

				setBedOccupied(world, i, j, k, false);
			}

			EnumStatus enumstatus2 = this.sleepInBedAt(entityplayer, i, j, k);
			if(enumstatus2 == EnumStatus.OK) {
				setBedOccupied(world, i, j, k, true);
				return true;
			} else {
				if(enumstatus2 == EnumStatus.NOT_POSSIBLE_NOW) {
					entityplayer.addChatMessage("tile.bed.noSleep");
				}

				return true;
			}
		}
	}

	public EnumStatus sleepInBedAt(EntityPlayer player, int i, int j, int k) {
		World worldObj = player.worldObj;
		if(!worldObj.multiplayerWorld) {
			label49: {
				if(!player.isPlayerSleeping() && player.isEntityAlive()) {
					if(worldObj.isDaytime()) {
						return EnumStatus.NOT_POSSIBLE_NOW;
					}

					if(Math.abs(player.posX - (double)i) <= 3.0D && Math.abs(player.posY - (double)j) <= 2.0D && Math.abs(player.posZ - (double)k) <= 3.0D) {
						break label49;
					}

					return EnumStatus.TOO_FAR_AWAY;
				}

				return EnumStatus.OTHER_PROBLEM;
			}
		}

		player.setSize(0.2F, 0.2F);
		player.yOffset = 0.2F;
		if(worldObj.blockExists(i, j, k)) {
			int l = worldObj.getBlockMetadata(i, j, k);
			int i1 = BlockBed.getDirectionFromMetadata(l);
			float f = 0.5F;
			float f1 = 0.5F;
			switch(i1) {
			case 0:
				f1 = 0.9F;
				break;
			case 1:
				f = 0.1F;
				break;
			case 2:
				f1 = 0.1F;
				break;
			case 3:
				f = 0.9F;
			}

			this.func_22052_e(player, i1);
			player.setPosition((double)((float)i + f), (double)((float)j + 0.9375F), (double)((float)k + f1));
		} else {
			player.setPosition((double)((float)i + 0.5F), (double)((float)j + 0.9375F), (double)((float)k + 0.5F));
		}

		player.sleeping = true;
		player.motionX = player.motionZ = player.motionY = 0.0D;
		if(!worldObj.multiplayerWorld) {
			worldObj.updateAllPlayersSleepingFlag();
		}

		return EnumStatus.OK;
	}

	private void func_22052_e(EntityPlayer player, int i) {
		player.field_22063_x = 0.0F;
		player.field_22061_z = 0.0F;
		switch(i) {
		case 0:
			player.field_22061_z = -1.8F;
			break;
		case 1:
			player.field_22063_x = 1.8F;
			break;
		case 2:
			player.field_22061_z = 1.8F;
			break;
		case 3:
			player.field_22063_x = -1.8F;
		}

	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if(i == 0) {
			return Block.planks.blockIndexInTexture;
		} else {
			int k = getDirectionFromMetadata(j);
			int l = ModelBed.bedDirection[k][i];
			return isBlockFootOfBed(j) ? (l == 2 ? this.blockIndexInTexture + 2 + 16 : (l != 5 && l != 4 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture + 1 + 16)) : (l == 3 ? this.blockIndexInTexture - 1 + 16 : (l != 5 && l != 4 ? this.blockIndexInTexture : this.blockIndexInTexture + 16));
		}
	}

	public int getRenderType() {
		return 14;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		this.setBounds();
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMetadata(i, j, k);
		int j1 = getDirectionFromMetadata(i1);
		if(isBlockFootOfBed(i1)) {
			if(world.getBlockId(i - headBlockToFootBlockMap[j1][0], j, k - headBlockToFootBlockMap[j1][1]) != this.blockID) {
				world.setBlockWithNotify(i, j, k, 0);
			}
		} else if(world.getBlockId(i + headBlockToFootBlockMap[j1][0], j, k + headBlockToFootBlockMap[j1][1]) != this.blockID) {
			world.setBlockWithNotify(i, j, k, 0);
			if(!world.multiplayerWorld) {
				this.dropBlockAsItem(world, i, j, k, i1);
			}
		}

	}

	public int idDropped(int i, Random random) {
		return isBlockFootOfBed(i) ? 0 : Item.bed.shiftedIndex;
	}

	private void setBounds() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
	}

	public static int getDirectionFromMetadata(int i) {
		return i & 3;
	}

	public static boolean isBlockFootOfBed(int i) {
		return (i & 8) != 0;
	}

	public static boolean isBedOccupied(int i) {
		return (i & 4) != 0;
	}

	public static void setBedOccupied(World world, int i, int j, int k, boolean flag) {
		int l = world.getBlockMetadata(i, j, k);
		if(flag) {
			l |= 4;
		} else {
			l &= -5;
		}

		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	public static ChunkCoordinates getNearestEmptyChunkCoordinates(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMetadata(i, j, k);
		int j1 = getDirectionFromMetadata(i1);

		for(int k1 = 0; k1 <= 1; ++k1) {
			int l1 = i - headBlockToFootBlockMap[j1][0] * k1 - 1;
			int i2 = k - headBlockToFootBlockMap[j1][1] * k1 - 1;
			int j2 = l1 + 2;
			int k2 = i2 + 2;

			for(int l2 = l1; l2 <= j2; ++l2) {
				for(int i3 = i2; i3 <= k2; ++i3) {
					if(world.isBlockNormalCube(l2, j - 1, i3) && world.isAirBlock(l2, j, i3) && world.isAirBlock(l2, j + 1, i3)) {
						if(l <= 0) {
							return new ChunkCoordinates(l2, j, i3);
						}

						--l;
					}
				}
			}
		}

		return null;
	}

	public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f) {
		if(!isBlockFootOfBed(l)) {
			super.dropBlockAsItemWithChance(world, i, j, k, l, f);
		}

	}

	public int getMobilityFlag() {
		return 1;
	}
}

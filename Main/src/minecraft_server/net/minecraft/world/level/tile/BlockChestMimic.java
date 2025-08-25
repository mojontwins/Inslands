package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.world.entity.monster.EntityMimic;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.material.Material;

public class BlockChestMimic extends Block {
	public BlockChestMimic(int i) {
		super(i, Material.wood);
		this.blockIndexInTexture = 26;
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(l == 1) {
			return this.blockIndexInTexture - 1;
		} else if(l == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int i1 = iblockaccess.getblockID(i, j, k - 1);
			int j1 = iblockaccess.getblockID(i, j, k + 1);
			int k1 = iblockaccess.getblockID(i - 1, j, k);
			int l1 = iblockaccess.getblockID(i + 1, j, k);
			int byte0;
			int l2;
			int j3;
			byte byte2;
			if(i1 != this.blockID && j1 != this.blockID) {
				if(k1 != this.blockID && l1 != this.blockID) {
					byte byte01 = 3;
					if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[j1]) {
						byte01 = 3;
					}

					if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[i1]) {
						byte01 = 2;
					}

					if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[l1]) {
						byte01 = 5;
					}

					if(Block.opaqueCubeLookup[l1] && !Block.opaqueCubeLookup[k1]) {
						byte01 = 4;
					}

					return l != byte01 ? this.blockIndexInTexture : this.blockIndexInTexture + 1;
				} else if(l != 4 && l != 5) {
					byte0 = 0;
					if(k1 == this.blockID) {
						byte0 = -1;
					}

					l2 = iblockaccess.getblockID(k1 != this.blockID ? i + 1 : i - 1, j, k - 1);
					j3 = iblockaccess.getblockID(k1 != this.blockID ? i + 1 : i - 1, j, k + 1);
					if(l == 3) {
						byte0 = -1 - byte0;
					}

					byte2 = 3;
					if((Block.opaqueCubeLookup[i1] || Block.opaqueCubeLookup[l2]) && !Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[j3]) {
						byte2 = 3;
					}

					if((Block.opaqueCubeLookup[j1] || Block.opaqueCubeLookup[j3]) && !Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l2]) {
						byte2 = 2;
					}

					return (l != byte2 ? this.blockIndexInTexture + 32 : this.blockIndexInTexture + 16) + byte0;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(l != 2 && l != 3) {
				byte0 = 0;
				if(i1 == this.blockID) {
					byte0 = -1;
				}

				l2 = iblockaccess.getblockID(i - 1, j, i1 != this.blockID ? k + 1 : k - 1);
				j3 = iblockaccess.getblockID(i + 1, j, i1 != this.blockID ? k + 1 : k - 1);
				if(l == 4) {
					byte0 = -1 - byte0;
				}

				byte2 = 5;
				if((Block.opaqueCubeLookup[k1] || Block.opaqueCubeLookup[l2]) && !Block.opaqueCubeLookup[l1] && !Block.opaqueCubeLookup[j3]) {
					byte2 = 5;
				}

				if((Block.opaqueCubeLookup[l1] || Block.opaqueCubeLookup[j3]) && !Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[l2]) {
					byte2 = 4;
				}

				return (l != byte2 ? this.blockIndexInTexture + 32 : this.blockIndexInTexture + 16) + byte0;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public int getBlockTextureFromSide(int i) {
		return i == 1 ? this.blockIndexInTexture - 1 : (i == 0 ? this.blockIndexInTexture - 1 : (i == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		int l = 0;
		if(world.getblockID(i - 1, j, k) == this.blockID) {
			++l;
		}

		if(world.getblockID(i + 1, j, k) == this.blockID) {
			++l;
		}

		if(world.getblockID(i, j, k - 1) == this.blockID) {
			++l;
		}

		if(world.getblockID(i, j, k + 1) == this.blockID) {
			++l;
		}

		return l > 1 ? false : (this.isThereANeighborChest(world, i - 1, j, k) ? false : (this.isThereANeighborChest(world, i + 1, j, k) ? false : (this.isThereANeighborChest(world, i, j, k - 1) ? false : !this.isThereANeighborChest(world, i, j, k + 1))));
	}

	private boolean isThereANeighborChest(World world, int i, int j, int k) {
		return world.getblockID(i, j, k) != this.blockID ? false : (world.getblockID(i - 1, j, k) == this.blockID ? true : (world.getblockID(i + 1, j, k) == this.blockID ? true : (world.getblockID(i, j, k - 1) == this.blockID ? true : world.getblockID(i, j, k + 1) == this.blockID)));
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		world.setBlockWithNotify(i, j, k, 0);
		EntityMimic mimic = new EntityMimic(world);
		mimic.setPosition((double)i + 0.5D, (double)j + 1.5D, (double)k + 0.5D);
		world.spawnEntityInWorld(mimic);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		world.setBlockWithNotify(i, j, k, 0);
		return true;
	}

	public int quantityDropped(Random random) {
		return 0;
	}
}

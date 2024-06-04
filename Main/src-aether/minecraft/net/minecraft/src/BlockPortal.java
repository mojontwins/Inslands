package net.minecraft.src;

import java.util.Random;

public class BlockPortal extends BlockBreakable {
	public BlockPortal(int i, int j) {
		super(i, j, Material.portal, false);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		float f1;
		float f3;
		if(iblockaccess.getBlockId(i - 1, j, k) != this.blockID && iblockaccess.getBlockId(i + 1, j, k) != this.blockID) {
			f1 = 0.125F;
			f3 = 0.5F;
			this.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f3, 0.5F + f1, 1.0F, 0.5F + f3);
		} else {
			f1 = 0.5F;
			f3 = 0.125F;
			this.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f3, 0.5F + f1, 1.0F, 0.5F + f3);
		}

	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean tryToCreatePortal(World world, int i, int j, int k) {
		byte l = 0;
		byte i1 = 0;
		if(world.getBlockId(i - 1, j, k) == Block.obsidian.blockID || world.getBlockId(i + 1, j, k) == Block.obsidian.blockID) {
			l = 1;
		}

		if(world.getBlockId(i, j, k - 1) == Block.obsidian.blockID || world.getBlockId(i, j, k + 1) == Block.obsidian.blockID) {
			i1 = 1;
		}

		if(l == i1) {
			return false;
		} else {
			if(world.getBlockId(i - l, j, k - i1) == 0) {
				i -= l;
				k -= i1;
			}

			int i2;
			int k1;
			for(i2 = -1; i2 <= 2; ++i2) {
				for(k1 = -1; k1 <= 3; ++k1) {
					boolean flag = i2 == -1 || i2 == 2 || k1 == -1 || k1 == 3;
					if(i2 != -1 && i2 != 2 || k1 != -1 && k1 != 3) {
						int j2 = world.getBlockId(i + l * i2, j + k1, k + i1 * i2);
						if(flag) {
							if(j2 != Block.obsidian.blockID) {
								return false;
							}
						} else if(j2 != 0 && j2 != Block.fire.blockID) {
							return false;
						}
					}
				}
			}

			world.editingBlocks = true;

			for(i2 = 0; i2 < 2; ++i2) {
				for(k1 = 0; k1 < 3; ++k1) {
					world.setBlockWithNotify(i + l * i2, j + k1, k + i1 * i2, Block.portal.blockID);
				}
			}

			world.editingBlocks = false;
			return true;
		}
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		byte i1 = 0;
		byte j1 = 1;
		if(world.getBlockId(i - 1, j, k) == this.blockID || world.getBlockId(i + 1, j, k) == this.blockID) {
			i1 = 1;
			j1 = 0;
		}

		int k1;
		for(k1 = j; world.getBlockId(i, k1 - 1, k) == this.blockID; --k1) {
		}

		if(world.getBlockId(i, k1 - 1, k) != Block.obsidian.blockID) {
			world.setBlockWithNotify(i, j, k, 0);
		} else {
			int l1;
			for(l1 = 1; l1 < 4 && world.getBlockId(i, k1 + l1, k) == this.blockID; ++l1) {
			}

			if(l1 == 3 && world.getBlockId(i, k1 + l1, k) == Block.obsidian.blockID) {
				boolean flag = world.getBlockId(i - 1, j, k) == this.blockID || world.getBlockId(i + 1, j, k) == this.blockID;
				boolean flag1 = world.getBlockId(i, j, k - 1) == this.blockID || world.getBlockId(i, j, k + 1) == this.blockID;
				if(flag && flag1) {
					world.setBlockWithNotify(i, j, k, 0);
				} else if((world.getBlockId(i + i1, j, k + j1) != Block.obsidian.blockID || world.getBlockId(i - i1, j, k - j1) != this.blockID) && (world.getBlockId(i - i1, j, k - j1) != Block.obsidian.blockID || world.getBlockId(i + i1, j, k + j1) != this.blockID)) {
					world.setBlockWithNotify(i, j, k, 0);
				}
			} else {
				world.setBlockWithNotify(i, j, k, 0);
			}
		}
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if(iblockaccess.getBlockId(i, j, k) == this.blockID) {
			return false;
		} else {
			boolean flag = iblockaccess.getBlockId(i - 1, j, k) == this.blockID && iblockaccess.getBlockId(i - 2, j, k) != this.blockID;
			boolean flag1 = iblockaccess.getBlockId(i + 1, j, k) == this.blockID && iblockaccess.getBlockId(i + 2, j, k) != this.blockID;
			boolean flag2 = iblockaccess.getBlockId(i, j, k - 1) == this.blockID && iblockaccess.getBlockId(i, j, k - 2) != this.blockID;
			boolean flag3 = iblockaccess.getBlockId(i, j, k + 1) == this.blockID && iblockaccess.getBlockId(i, j, k + 2) != this.blockID;
			boolean flag4 = flag || flag1;
			boolean flag5 = flag2 || flag3;
			return flag4 && l == 4 ? true : (flag4 && l == 5 ? true : (flag5 && l == 2 ? true : flag5 && l == 3));
		}
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		if(entity.ridingEntity == null && entity.riddenByEntity == null) {
			if(entity instanceof EntityPlayerSP) {
				EntityPlayerSP entityplayersp = (EntityPlayerSP)entity;
				((PlayerBaseSAPI)((PlayerBaseSAPI)PlayerAPI.getPlayerBase(entityplayersp, PlayerBaseSAPI.class))).portal = this.getDimNumber();
			}

			entity.setInPortal();
		}

	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if(random.nextInt(100) == 0) {
			world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "portal.portal", 1.0F, random.nextFloat() * 0.4F + 0.8F);
		}

		for(int l = 0; l < 4; ++l) {
			double d = (double)((float)i + random.nextFloat());
			double d1 = (double)((float)j + random.nextFloat());
			double d2 = (double)((float)k + random.nextFloat());
			double d3 = 0.0D;
			double d4 = 0.0D;
			double d5 = 0.0D;
			int i1 = random.nextInt(2) * 2 - 1;
			d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
			d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
			d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
			if(world.getBlockId(i - 1, j, k) != this.blockID && world.getBlockId(i + 1, j, k) != this.blockID) {
				d = (double)i + 0.5D + 0.25D * (double)i1;
				d3 = (double)(random.nextFloat() * 2.0F * (float)i1);
			} else {
				d2 = (double)k + 0.5D + 0.25D * (double)i1;
				d5 = (double)(random.nextFloat() * 2.0F * (float)i1);
			}

			world.spawnParticle("portal", d, d1, d2, d3, d4, d5);
		}

	}

	public int getDimNumber() {
		return -1;
	}
}

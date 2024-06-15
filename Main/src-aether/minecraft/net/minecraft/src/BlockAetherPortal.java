package net.minecraft.src;

import java.util.Random;

public class BlockAetherPortal extends BlockPortal {
	public BlockAetherPortal(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/Portal.png"));
	}

	public boolean tryToCreatePortal(World world, int i, int j, int k) {
		byte l = 0;
		byte i1 = 0;
		if(world.getBlockId(i - 1, j, k) == Block.glowStone.blockID || world.getBlockId(i + 1, j, k) == Block.glowStone.blockID) {
			l = 1;
		}

		if(world.getBlockId(i, j, k - 1) == Block.glowStone.blockID || world.getBlockId(i, j, k + 1) == Block.glowStone.blockID) {
			i1 = 1;
		}

		if(l == i1) {
			return false;
		} else {
			if(world.getBlockId(i - l, j, k - i1) == 0) {
				i -= l;
				k -= i1;
			}

			int k1;
			int i2;
			for(k1 = -1; k1 <= 2; ++k1) {
				for(i2 = -1; i2 <= 3; ++i2) {
					boolean flag = k1 == -1 || k1 == 2 || i2 == -1 || i2 == 3;
					if(k1 != -1 && k1 != 2 || i2 != -1 && i2 != 3) {
						int j2 = world.getBlockId(i + l * k1, j + i2, k + i1 * k1);
						if(flag) {
							if(j2 != Block.glowStone.blockID) {
								return false;
							}
						} else if(j2 != 0 && j2 != Block.waterMoving.blockID) {
							return false;
						}
					}
				}
			}

			world.editingBlocks = true;

			for(k1 = 0; k1 < 2; ++k1) {
				for(i2 = 0; i2 < 3; ++i2) {
					world.setBlockWithNotify(i + l * k1, j + i2, k + i1 * k1, this.blockID);
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

		if(world.getBlockId(i, k1 - 1, k) != Block.glowStone.blockID) {
			world.setBlockWithNotify(i, j, k, 0);
		} else {
			int l1;
			for(l1 = 1; l1 < 4 && world.getBlockId(i, k1 + l1, k) == this.blockID; ++l1) {
			}

			if(l1 == 3 && world.getBlockId(i, k1 + l1, k) == Block.glowStone.blockID) {
				boolean flag = world.getBlockId(i - 1, j, k) == this.blockID || world.getBlockId(i + 1, j, k) == this.blockID;
				boolean flag1 = world.getBlockId(i, j, k - 1) == this.blockID || world.getBlockId(i, j, k + 1) == this.blockID;
				if(flag && flag1) {
					world.setBlockWithNotify(i, j, k, 0);
				} else if((world.getBlockId(i + i1, j, k + j1) != Block.glowStone.blockID || world.getBlockId(i - i1, j, k - j1) != this.blockID) && (world.getBlockId(i - i1, j, k - j1) != Block.glowStone.blockID || world.getBlockId(i + i1, j, k + j1) != this.blockID)) {
					world.setBlockWithNotify(i, j, k, 0);
				}
			} else {
				world.setBlockWithNotify(i, j, k, 0);
			}
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

			EntityAetherPortalFX obj = new EntityAetherPortalFX(world, d, d1, d2, d3, d4, d5);
			ModLoader.getMinecraftInstance().effectRenderer.addEffect(obj);
		}

	}

	public int getDimNumber() {
		return 3;
	}
}

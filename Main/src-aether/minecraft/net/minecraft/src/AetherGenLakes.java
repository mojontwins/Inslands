package net.minecraft.src;

import java.util.Random;

public class AetherGenLakes extends WorldGenerator {
	private int field_15235_a;

	public AetherGenLakes(int i) {
		this.field_15235_a = i;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		i -= 8;

		for(k -= 8; j > 0 && world.isAirBlock(i, j, k); --j) {
		}

		j -= 4;
		boolean[] aflag = new boolean[2048];
		int l = random.nextInt(4) + 4;

		int i2;
		for(i2 = 0; i2 < l; ++i2) {
			double i3 = random.nextDouble() * 6.0D + 3.0D;
			double flag1 = random.nextDouble() * 4.0D + 2.0D;
			double d2 = random.nextDouble() * 6.0D + 3.0D;
			double d3 = random.nextDouble() * (16.0D - i3 - 2.0D) + 1.0D + i3 / 2.0D;
			double d4 = random.nextDouble() * (8.0D - flag1 - 4.0D) + 2.0D + flag1 / 2.0D;
			double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

			for(int j4 = 1; j4 < 15; ++j4) {
				for(int k4 = 1; k4 < 15; ++k4) {
					for(int l4 = 1; l4 < 7; ++l4) {
						double d6 = ((double)j4 - d3) / (i3 / 2.0D);
						double d7 = ((double)l4 - d4) / (flag1 / 2.0D);
						double d8 = ((double)k4 - d5) / (d2 / 2.0D);
						double d9 = d6 * d6 + d7 * d7 + d8 * d8;
						if(d9 < 1.0D) {
							aflag[(j4 * 16 + k4) * 8 + l4] = true;
						}
					}
				}
			}
		}

		int i4;
		int i32;
		boolean z33;
		for(i2 = 0; i2 < 16; ++i2) {
			for(i32 = 0; i32 < 16; ++i32) {
				for(i4 = 0; i4 < 8; ++i4) {
					z33 = !aflag[(i2 * 16 + i32) * 8 + i4] && (i2 < 15 && aflag[((i2 + 1) * 16 + i32) * 8 + i4] || i2 > 0 && aflag[((i2 - 1) * 16 + i32) * 8 + i4] || i32 < 15 && aflag[(i2 * 16 + i32 + 1) * 8 + i4] || i32 > 0 && aflag[(i2 * 16 + (i32 - 1)) * 8 + i4] || i4 < 7 && aflag[(i2 * 16 + i32) * 8 + i4 + 1] || i4 > 0 && aflag[(i2 * 16 + i32) * 8 + (i4 - 1)]);
					if(z33) {
						Material material = world.getBlockMaterial(i + i2, j + i4, k + i32);
						if(i4 >= 4 && material.getIsLiquid()) {
							return false;
						}

						if(i4 < 4 && !material.isSolid() && world.getBlockId(i + i2, j + i4, k + i32) != this.field_15235_a) {
							return false;
						}
					}
				}
			}
		}

		for(i2 = 0; i2 < 16; ++i2) {
			for(i32 = 0; i32 < 16; ++i32) {
				for(i4 = 0; i4 < 8; ++i4) {
					if(aflag[(i2 * 16 + i32) * 8 + i4]) {
						world.setBlock(i + i2, j + i4, k + i32, i4 < 4 ? this.field_15235_a : 0);
					}
				}
			}
		}

		for(i2 = 0; i2 < 16; ++i2) {
			for(i32 = 0; i32 < 16; ++i32) {
				for(i4 = 4; i4 < 8; ++i4) {
					if(aflag[(i2 * 16 + i32) * 8 + i4] && world.getBlockId(i + i2, j + i4 - 1, k + i32) == AetherBlocks.Dirt.blockID && world.getSavedLightValue(EnumSkyBlock.Sky, i + i2, j + i4, k + i32) > 0) {
						world.setBlock(i + i2, j + i4 - 1, k + i32, AetherBlocks.Grass.blockID);
					}
				}
			}
		}

		if(Block.blocksList[this.field_15235_a].blockMaterial == Material.lava) {
			for(i2 = 0; i2 < 16; ++i2) {
				for(i32 = 0; i32 < 16; ++i32) {
					for(i4 = 0; i4 < 8; ++i4) {
						z33 = !aflag[(i2 * 16 + i32) * 8 + i4] && (i2 < 15 && aflag[((i2 + 1) * 16 + i32) * 8 + i4] || i2 > 0 && aflag[((i2 - 1) * 16 + i32) * 8 + i4] || i32 < 15 && aflag[(i2 * 16 + i32 + 1) * 8 + i4] || i32 > 0 && aflag[(i2 * 16 + (i32 - 1)) * 8 + i4] || i4 < 7 && aflag[(i2 * 16 + i32) * 8 + i4 + 1] || i4 > 0 && aflag[(i2 * 16 + i32) * 8 + (i4 - 1)]);
						if(z33 && (i4 < 4 || random.nextInt(2) != 0) && world.getBlockMaterial(i + i2, j + i4, k + i32).isSolid()) {
							world.setBlockAndMetadata(i + i2, j + i4, k + i32, AetherBlocks.Holystone.blockID, 0);
						}
					}
				}
			}
		}

		return true;
	}
}

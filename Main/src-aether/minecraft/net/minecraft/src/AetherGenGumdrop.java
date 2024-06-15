package net.minecraft.src;

import java.util.Random;

public class AetherGenGumdrop extends WorldGenerator {
	public boolean generate(World world, Random random, int i, int j, int k) {
		return this.func_100009_a(world, random, i, j, k, 24);
	}

	public boolean func_100009_a(World world, Random random, int i, int j, int k, int l) {
		if(j - l <= 0) {
			j = l + 1;
		}

		if(j + l >= 116) {
			j = 116 - l - 1;
		}

		int i1 = 0;
		int j1 = MathHelper.floor_double((double)l * 0.875D);

		int j2;
		int f1;
		for(int f = -j1; f <= j1; ++f) {
			for(j2 = l; j2 >= -j1; --j2) {
				for(f1 = -j1; f1 <= j1; ++f1) {
					if(!AetherBlocks.isGood(world.getBlockId(f + i, j2 + j, f1 + k), world.getBlockMetadata(f + i, j2 + j, f1 + k))) {
						++i1;
						if(i1 > l / 2) {
							return false;
						}
					}
				}
			}
		}

		float f18 = 0.8F;

		int flag;
		int l3;
		int j4;
		int i5;
		int l5;
		for(j2 = -l; j2 <= l; ++j2) {
			for(f1 = l; f1 >= -l; --f1) {
				for(flag = -l; flag <= l; ++flag) {
					l3 = MathHelper.floor_double((double)j2 * (1.0D + (double)f1 / ((double)l * 10.0D)) / (double)f18);
					j4 = f1;
					if((double)f1 > (double)l * 0.625D) {
						j4 = MathHelper.floor_double((double)f1 * 1.375D);
						j4 -= MathHelper.floor_double((double)l * 0.25D);
					} else if((double)f1 < (double)l * -0.625D) {
						j4 = MathHelper.floor_double((double)f1 * 1.350000023841858D);
						j4 += MathHelper.floor_double((double)l * 0.25D);
					}

					i5 = MathHelper.floor_double((double)flag * (1.0D + (double)f1 / ((double)l * 10.0D)) / (double)f18);
					if(Math.sqrt((double)(l3 * l3 + j4 * j4 + i5 * i5)) <= (double)l) {
						if(AetherBlocks.isGood(world.getBlockId(j2 + i, f1 + j + 1, flag + k), world.getBlockMetadata(j2 + i, f1 + j + 1, flag + k)) && (double)f1 > (double)MathHelper.floor_double((double)l / 5.0D)) {
							world.setBlock(j2 + i, f1 + j, flag + k, AetherBlocks.Grass.blockID);
							world.setBlock(j2 + i, f1 + j - 1, flag + k, AetherBlocks.Dirt.blockID);
							world.setBlock(j2 + i, f1 + j - (1 + random.nextInt(2)), flag + k, AetherBlocks.Dirt.blockID);
							if(f1 >= l / 2) {
								l5 = random.nextInt(48);
								if(l5 < 2) {
									(new AetherGenGoldenOak()).generate(world, random, j2 + i, f1 + j + 1, flag + k);
								} else if(l5 == 3) {
									if(random.nextInt(2) == 0) {
										(new WorldGenLakes(Block.waterStill.blockID)).generate(world, random, j2 + i + random.nextInt(3) - random.nextInt(3), f1 + j, flag + k + random.nextInt(3) - random.nextInt(3));
									}
								} else if(l5 == 4) {
									if(random.nextInt(2) == 0) {
										(new WorldGenFlowers(Block.plantYellow.blockID)).generate(world, random, j2 + i + random.nextInt(3) - random.nextInt(3), f1 + j + 1, flag + k + random.nextInt(3) - random.nextInt(3));
									} else {
										(new WorldGenFlowers(Block.plantRed.blockID)).generate(world, random, j2 + i + random.nextInt(3) - random.nextInt(3), f1 + j + 1, flag + k + random.nextInt(3) - random.nextInt(3));
									}
								}
							}
						} else if(AetherBlocks.isGood(world.getBlockId(j2 + i, f1 + j, flag + k), world.getBlockMetadata(j2 + i, f1 + j, flag + k))) {
							world.setBlockAndMetadata(j2 + i, f1 + j, flag + k, AetherBlocks.Holystone.blockID, 0);
						}
					}
				}
			}
		}

		j2 = 8 + random.nextInt(5);
		float f19 = 0.01745329F;

		int j6;
		for(flag = 0; flag < j2; ++flag) {
			float f21 = random.nextFloat() * 360.0F;
			float f22 = (random.nextFloat() * 0.125F + 0.7F) * (float)l;
			i5 = i + MathHelper.floor_double(Math.cos((double)(f19 * f21)) * (double)f22);
			l5 = j - MathHelper.floor_double((double)l * (double)random.nextFloat() * 0.3D);
			j6 = k + MathHelper.floor_double(-Math.sin((double)(f19 * f21)) * (double)f22);
			this.func_100008_b(world, random, i5, l5, j6, MathHelper.floor_double((double)l / 3.0D), true);
		}

		boolean z20 = false;
		(new AetherGenDungeon()).generate(world, random, i, j, k, j1);
		l3 = MathHelper.floor_double((double)l * 0.75D);

		for(j4 = 0; j4 < l3; ++j4) {
			i5 = i + random.nextInt(l) - random.nextInt(l);
			l5 = j + random.nextInt(l) - random.nextInt(l);
			j6 = k + random.nextInt(l) - random.nextInt(l);
			(new AetherGenGumdropCaves(0, 24 + l3 / 3)).generate(world, random, i5, l5, j6);
		}

		return true;
	}

	public boolean func_100008_b(World world, Random random, int i, int j, int k, int l, boolean flag) {
		if(j - l <= 0) {
			j = l + 1;
		}

		if(j + l >= 127) {
			j = 127 - l - 1;
		}

		float f = 1.0F;

		int j1;
		int l1;
		int j2;
		int l2;
		int j3;
		for(j1 = -l; j1 <= l; ++j1) {
			for(l1 = l; l1 >= -l; --l1) {
				for(j2 = -l; j2 <= l; ++j2) {
					l2 = MathHelper.floor_double((double)j1 / (double)f);
					j3 = l1;
					if((double)l1 > (double)l * 0.625D) {
						j3 = MathHelper.floor_double((double)l1 * 1.375D);
						j3 -= MathHelper.floor_double((double)l * 0.25D);
					} else if((double)l1 < (double)l * -0.625D) {
						j3 = MathHelper.floor_double((double)l1 * 1.350000023841858D);
						j3 += MathHelper.floor_double((double)l * 0.25D);
					}

					int k3 = MathHelper.floor_double((double)j2 / (double)f);
					if(Math.sqrt((double)(l2 * l2 + j3 * j3 + k3 * k3)) <= (double)l) {
						if(AetherBlocks.isGood(world.getBlockId(j1 + i, l1 + j + 1, j2 + k), world.getBlockMetadata(j1 + i, l1 + j + 1, j2 + k)) && (double)l1 > (double)MathHelper.floor_double((double)l / 5.0D)) {
							world.setBlock(j1 + i, l1 + j, j2 + k, AetherBlocks.Grass.blockID);
							world.setBlock(j1 + i, l1 + j - 1, j2 + k, AetherBlocks.Dirt.blockID);
							world.setBlock(j1 + i, l1 + j - (1 + random.nextInt(2)), j2 + k, AetherBlocks.Dirt.blockID);
							if(l1 >= l / 2) {
								int l3 = random.nextInt(64);
								if(l3 == 0) {
									(new AetherGenGoldenOak()).generate(world, random, j1 + i, l1 + j + 1, j2 + k);
								} else if(l3 == 5 && random.nextInt(3) == 0) {
									(new WorldGenLakes(Block.waterStill.blockID)).generate(world, random, j1 + i + random.nextInt(3) - random.nextInt(3), l1 + j, j2 + k + random.nextInt(3) - random.nextInt(3));
								}
							}
						} else if(AetherBlocks.isGood(world.getBlockId(j1 + i, l1 + j, j2 + k), world.getBlockMetadata(j1 + i, l1 + j, j2 + k))) {
							world.setBlockAndMetadata(j1 + i, l1 + j, j2 + k, AetherBlocks.Holystone.blockID, 0);
						}
					}
				}
			}
		}

		if(!flag) {
			j1 = MathHelper.floor_double((double)l * 1.25D);

			for(l1 = 0; l1 < j1; ++l1) {
				j2 = i + random.nextInt(l) - random.nextInt(l);
				l2 = j + random.nextInt(l) - random.nextInt(l);
				j3 = k + random.nextInt(l) - random.nextInt(l);
				(new AetherGenGumdropCaves(0, 16 + j1 / 3)).generate(world, random, j2, l2, j3);
			}
		}

		return true;
	}
}

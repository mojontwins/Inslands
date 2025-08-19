package net.minecraft.game.world.terrain;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.chunk.Chunk;
import net.minecraft.game.world.chunk.IChunkProvider;
import net.minecraft.game.world.terrain.generate.WorldGenBigTree;
import net.minecraft.game.world.terrain.generate.WorldGenMinable;
import net.minecraft.game.world.terrain.noise.NoiseGeneratorOctaves;

public final class ChunkProviderGenerate implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	private NoiseGeneratorOctaves noiseGen5;
	private NoiseGeneratorOctaves mobSpawnerNoise;
	private World worldObj;

	public ChunkProviderGenerate(World world1, long j2) {
		this.worldObj = world1;
		this.rand = new Random(j2);
		new Random(j2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 4);
		new NoiseGeneratorOctaves(this.rand, 5);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 5);
	}

	public final Chunk provideChunk(int i1, int i2) {
		this.rand.setSeed((long)i1 * 341873128712L + (long)i2 * 132897987541L);
		byte[] b3 = new byte[32768];
		Chunk chunk4 = new Chunk(this.worldObj, b3, i1, i2);

		int i5;
		int i6;
		double d50;
		for(i5 = 0; i5 < 4; ++i5) {
			for(i6 = 0; i6 < 4; ++i6) {
				double[][] d7 = new double[33][4];
				int i8 = (i1 << 2) + i5;
				int i9 = (i2 << 2) + i6;

				for(int i10 = 0; i10 < d7.length; ++i10) {
					d7[i10][0] = this.initializeNoiseField((double)i8, (double)i10, (double)i9);
					d7[i10][1] = this.initializeNoiseField((double)i8, (double)i10, (double)(i9 + 1));
					d7[i10][2] = this.initializeNoiseField((double)(i8 + 1), (double)i10, (double)i9);
					d7[i10][3] = this.initializeNoiseField((double)(i8 + 1), (double)i10, (double)(i9 + 1));
				}

				for(i8 = 0; i8 < 32; ++i8) {
					d50 = d7[i8][0];
					double d11 = d7[i8][1];
					double d13 = d7[i8][2];
					double d15 = d7[i8][3];
					double d17 = d7[i8 + 1][0];
					double d19 = d7[i8 + 1][1];
					double d21 = d7[i8 + 1][2];
					double d23 = d7[i8 + 1][3];

					for(int i25 = 0; i25 < 4; ++i25) {
						double d26 = (double)i25 / 4.0D;
						double d28 = d50 + (d17 - d50) * d26;
						double d30 = d11 + (d19 - d11) * d26;
						double d32 = d13 + (d21 - d13) * d26;
						double d34 = d15 + (d23 - d15) * d26;

						for(int i55 = 0; i55 < 4; ++i55) {
							double d37 = (double)i55 / 4.0D;
							double d39 = d28 + (d32 - d28) * d37;
							double d41 = d30 + (d34 - d30) * d37;
							int i27 = i55 + (i5 << 2) << 11 | 0 + (i6 << 2) << 7 | (i8 << 2) + i25;

							for(int i36 = 0; i36 < 4; ++i36) {
								double d45 = (double)i36 / 4.0D;
								double d47 = d39 + (d41 - d39) * d45;
								int i56 = 0;
								if((i8 << 2) + i25 < 64) {
									i56 = Block.waterStill.blockID;
								}

								if(d47 > 0.0D) {
									i56 = Block.stone.blockID;
								}

								b3[i27] = (byte)i56;
								i27 += 128;
							}
						}
					}
				}
			}
		}

		for(i5 = 0; i5 < 16; ++i5) {
			for(i6 = 0; i6 < 16; ++i6) {
				double d49 = (double)((i1 << 4) + i5);
				d50 = (double)((i2 << 4) + i6);
				boolean z51 = this.noiseGen4.generateNoiseOctaves(d49 * 8.0D / 256D, d50 * 8.0D / 256D, 0.0D) + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean z14 = this.noiseGen4.generateNoiseOctaves(d50 * 8.0D / 256D, 109.0134D, d49 * 8.0D / 256D) + this.rand.nextDouble() * 0.2D > 3.0D;
				int i52 = (int)(this.noiseGen5.noiseGenerator(d49 * 8.0D / 256D * 2.0D, d50 * 8.0D / 256D * 2.0D) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i16 = i5 << 11 | i6 << 7 | 127;
				int i53 = -1;
				int i18 = Block.grass.blockID;
				int i54 = Block.dirt.blockID;

				for(int i20 = 127; i20 >= 0; --i20) {
					if(b3[i16] == 0) {
						i53 = -1;
					} else if(b3[i16] == Block.stone.blockID) {
						if(i53 == -1) {
							if(i52 <= 0) {
								i18 = 0;
								i54 = (byte)Block.stone.blockID;
							} else if(i20 >= 60 && i20 <= 65) {
								i18 = Block.grass.blockID;
								i54 = Block.dirt.blockID;
								if(z14) {
									i18 = 0;
								}

								if(z14) {
									i54 = Block.gravel.blockID;
								}

								if(z51) {
									i18 = Block.sand.blockID;
								}

								if(z51) {
									i54 = Block.sand.blockID;
								}
							}

							if(i20 < 64 && i18 == 0) {
								i18 = Block.waterStill.blockID;
							}

							i53 = i52;
							if(i20 >= 63) {
								b3[i16] = (byte)i18;
							} else {
								b3[i16] = (byte)i54;
							}
						} else if(i53 > 0) {
							--i53;
							b3[i16] = (byte)i54;
						}
					}

					--i16;
				}
			}
		}

		chunk4.generateHeightMap();
		return chunk4;
	}

	private double initializeNoiseField(double d1, double d3, double d5) {
		double d7;
		if((d7 = d3 * 4.0D - 64.0D) < 0.0D) {
			d7 *= 3.0D;
		}

		double d9;
		double d13;
		if((d9 = this.noiseGen3.generateNoiseOctaves(d1 * 684.412D / 80.0D, d3 * 684.412D / 400.0D, d5 * 684.412D / 80.0D) / 2.0D) < -1.0D) {
			if((d13 = this.noiseGen1.generateNoiseOctaves(d1 * 684.412D, d3 * 984.412D, d5 * 684.412D) / 512.0D - d7) < -10.0D) {
				d13 = -10.0D;
			}

			if(d13 > 10.0D) {
				d13 = 10.0D;
			}
		} else if(d9 > 1.0D) {
			if((d13 = this.noiseGen2.generateNoiseOctaves(d1 * 684.412D, d3 * 984.412D, d5 * 684.412D) / 512.0D - d7) < -10.0D) {
				d13 = -10.0D;
			}

			if(d13 > 10.0D) {
				d13 = 10.0D;
			}
		} else {
			double d15 = this.noiseGen1.generateNoiseOctaves(d1 * 684.412D, d3 * 984.412D, d5 * 684.412D) / 512.0D - d7;
			double d17 = this.noiseGen2.generateNoiseOctaves(d1 * 684.412D, d3 * 984.412D, d5 * 684.412D) / 512.0D - d7;
			if(d15 < -10.0D) {
				d15 = -10.0D;
			}

			if(d15 > 10.0D) {
				d15 = 10.0D;
			}

			if(d17 < -10.0D) {
				d17 = -10.0D;
			}

			if(d17 > 10.0D) {
				d17 = 10.0D;
			}

			double d19 = (d9 + 1.0D) / 2.0D;
			d13 = d15 + (d17 - d15) * d19;
		}

		return d13;
	}

	public final boolean chunkExists(int i1, int i2) {
		return true;
	}

	public final void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		this.rand.setSeed((long)i2 * 318279123L + (long)i3 * 919871212L);
		int i8 = i2 << 4;
		i2 = i3 << 4;

		int i4;
		int i5;
		int i6;
		for(i3 = 0; i3 < 20; ++i3) {
			i4 = i8 + this.rand.nextInt(16);
			i5 = this.rand.nextInt(128);
			i6 = i2 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreCoal.blockID)).generate(this.worldObj, this.rand, i4, i5, i6);
		}

		for(i3 = 0; i3 < 10; ++i3) {
			i4 = i8 + this.rand.nextInt(16);
			i5 = this.rand.nextInt(64);
			i6 = i2 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID)).generate(this.worldObj, this.rand, i4, i5, i6);
		}

		if(this.rand.nextInt(2) == 0) {
			i3 = i8 + this.rand.nextInt(16);
			i4 = this.rand.nextInt(32);
			i5 = i2 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID)).generate(this.worldObj, this.rand, i3, i4, i5);
		}

		if(this.rand.nextInt(8) == 0) {
			i3 = i8 + this.rand.nextInt(16);
			i4 = this.rand.nextInt(16);
			i5 = i2 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreDiamond.blockID)).generate(this.worldObj, this.rand, i3, i4, i5);
		}

		i3 = (int)this.mobSpawnerNoise.noiseGenerator((double)i8 * 0.25D, (double)i2 * 0.25D) << 3;
		WorldGenBigTree worldGenBigTree9 = new WorldGenBigTree();

		for(i5 = 0; i5 < i3; ++i5) {
			i6 = i8 + this.rand.nextInt(16) + 8;
			int i7 = i2 + this.rand.nextInt(16) + 8;
			worldGenBigTree9.setScale(1.0D, 1.0D, 1.0D);
			worldGenBigTree9.generate(this.worldObj, this.rand, i6, this.worldObj.getHeightValue(i6, i7), i7);
		}

	}

	public final void saveChunks(boolean z1) {
	}
}
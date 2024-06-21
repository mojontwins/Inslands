package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderEnd implements IChunkProvider {
	private Random endRNG;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	public NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;
	private World endWorld;
	private double[] densities;
	private BiomeGenBase[] biomesForGeneration;
	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;
	int[][] field_40200_h = new int[32][32];

	public ChunkProviderEnd(World world1, long j2) {
		this.endWorld = world1;
		this.endRNG = new Random(j2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
	}

	public void func_40184_a(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4) {
		byte b5 = 2;
		int i6 = b5 + 1;
		byte b7 = 33;
		int i8 = b5 + 1;
		this.densities = this.func_40186_a(this.densities, i1 * b5, 0, i2 * b5, i6, b7, i8);

		for(int i9 = 0; i9 < b5; ++i9) {
			for(int i10 = 0; i10 < b5; ++i10) {
				for(int i11 = 0; i11 < 32; ++i11) {
					double d12 = 0.25D;
					double d14 = this.densities[((i9 + 0) * i8 + i10 + 0) * b7 + i11 + 0];
					double d16 = this.densities[((i9 + 0) * i8 + i10 + 1) * b7 + i11 + 0];
					double d18 = this.densities[((i9 + 1) * i8 + i10 + 0) * b7 + i11 + 0];
					double d20 = this.densities[((i9 + 1) * i8 + i10 + 1) * b7 + i11 + 0];
					double d22 = (this.densities[((i9 + 0) * i8 + i10 + 0) * b7 + i11 + 1] - d14) * d12;
					double d24 = (this.densities[((i9 + 0) * i8 + i10 + 1) * b7 + i11 + 1] - d16) * d12;
					double d26 = (this.densities[((i9 + 1) * i8 + i10 + 0) * b7 + i11 + 1] - d18) * d12;
					double d28 = (this.densities[((i9 + 1) * i8 + i10 + 1) * b7 + i11 + 1] - d20) * d12;

					for(int i30 = 0; i30 < 4; ++i30) {
						double d31 = 0.125D;
						double d33 = d14;
						double d35 = d16;
						double d37 = (d18 - d14) * d31;
						double d39 = (d20 - d16) * d31;

						for(int i41 = 0; i41 < 8; ++i41) {
							int i42 = i41 + i9 * 8 << 11 | 0 + i10 * 8 << 7 | i11 * 4 + i30;
							short s43 = 128;
							double d44 = 0.125D;
							double d46 = d33;
							double d48 = (d35 - d33) * d44;

							for(int i50 = 0; i50 < 8; ++i50) {
								int i51 = 0;
								if(d46 > 0.0D) {
									i51 = Block.whiteStone.blockID;
								}

								b3[i42] = (byte)i51;
								i42 += s43;
								d46 += d48;
							}

							d33 += d37;
							d35 += d39;
						}

						d14 += d22;
						d16 += d24;
						d18 += d26;
						d20 += d28;
					}
				}
			}
		}

	}

	public void func_40185_b(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4) {
		for(int i5 = 0; i5 < 16; ++i5) {
			for(int i6 = 0; i6 < 16; ++i6) {
				byte b7 = 1;
				int i8 = -1;
				byte b9 = (byte)Block.whiteStone.blockID;
				byte b10 = (byte)Block.whiteStone.blockID;

				for(int i11 = 127; i11 >= 0; --i11) {
					int i12 = (i6 * 16 + i5) * 128 + i11;
					byte b13 = b3[i12];
					if(b13 == 0) {
						i8 = -1;
					} else if(b13 == Block.stone.blockID) {
						if(i8 == -1) {
							if(b7 <= 0) {
								b9 = 0;
								b10 = (byte)Block.whiteStone.blockID;
							}

							i8 = b7;
							if(i11 >= 0) {
								b3[i12] = b9;
							} else {
								b3[i12] = b10;
							}
						} else if(i8 > 0) {
							--i8;
							b3[i12] = b10;
						}
					}
				}
			}
		}

	}

	public Chunk loadChunk(int i1, int i2) {
		return this.provideChunk(i1, i2);
	}

	public Chunk provideChunk(int i1, int i2) {
		this.endRNG.setSeed((long)i1 * 341873128712L + (long)i2 * 132897987541L);
		byte[] b3 = new byte[32768];
		this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, i1 * 16, i2 * 16, 16, 16);
		this.func_40184_a(i1, i2, b3, this.biomesForGeneration);
		this.func_40185_b(i1, i2, b3, this.biomesForGeneration);
		Chunk chunk4 = new Chunk(this.endWorld, b3, i1, i2);
		byte[] b5 = chunk4.getBiomeArray();

		for(int i6 = 0; i6 < b5.length; ++i6) {
			b5[i6] = (byte)this.biomesForGeneration[i6].biomeID;
		}

		chunk4.generateSkylightMap();
		return chunk4;
	}

	private double[] func_40186_a(double[] d1, int i2, int i3, int i4, int i5, int i6, int i7) {
		if(d1 == null) {
			d1 = new double[i5 * i6 * i7];
		}

		double d8 = 684.412D;
		double d10 = 684.412D;
		this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, i2, i4, i5, i7, 1.121D, 1.121D, 0.5D);
		this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, i2, i4, i5, i7, 200.0D, 200.0D, 0.5D);
		d8 *= 2.0D;
		this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, i2, i3, i4, i5, i6, i7, d8 / 80.0D, d10 / 160.0D, d8 / 80.0D);
		this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, i2, i3, i4, i5, i6, i7, d8, d10, d8);
		this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, i2, i3, i4, i5, i6, i7, d8, d10, d8);
		int i12 = 0;
		int i13 = 0;

		for(int i14 = 0; i14 < i5; ++i14) {
			for(int i15 = 0; i15 < i7; ++i15) {
				double d16 = (this.noiseData4[i13] + 256.0D) / 512.0D;
				if(d16 > 1.0D) {
					d16 = 1.0D;
				}

				double d18 = this.noiseData5[i13] / 8000.0D;
				if(d18 < 0.0D) {
					d18 = -d18 * 0.3D;
				}

				d18 = d18 * 3.0D - 2.0D;
				float f20 = (float)(i14 + i2 - 0) / 1.0F;
				float f21 = (float)(i15 + i4 - 0) / 1.0F;
				float f22 = 100.0F - MathHelper.sqrt_float(f20 * f20 + f21 * f21) * 8.0F;
				if(f22 > 80.0F) {
					f22 = 80.0F;
				}

				if(f22 < -100.0F) {
					f22 = -100.0F;
				}

				if(d18 > 1.0D) {
					d18 = 1.0D;
				}

				d18 /= 8.0D;
				d18 = 0.0D;
				if(d16 < 0.0D) {
					d16 = 0.0D;
				}

				d16 += 0.5D;
				d18 = d18 * (double)i6 / 16.0D;
				++i13;
				double d23 = (double)i6 / 2.0D;

				for(int i25 = 0; i25 < i6; ++i25) {
					double d26 = 0.0D;
					double d28 = ((double)i25 - d23) * 8.0D / d16;
					if(d28 < 0.0D) {
						d28 *= -1.0D;
					}

					double d30 = this.noiseData2[i12] / 512.0D;
					double d32 = this.noiseData3[i12] / 512.0D;
					double d34 = (this.noiseData1[i12] / 10.0D + 1.0D) / 2.0D;
					if(d34 < 0.0D) {
						d26 = d30;
					} else if(d34 > 1.0D) {
						d26 = d32;
					} else {
						d26 = d30 + (d32 - d30) * d34;
					}

					d26 -= 8.0D;
					d26 += (double)f22;
					byte b36 = 2;
					double d37;
					if(i25 > i6 / 2 - b36) {
						d37 = (double)((float)(i25 - (i6 / 2 - b36)) / 64.0F);
						if(d37 < 0.0D) {
							d37 = 0.0D;
						}

						if(d37 > 1.0D) {
							d37 = 1.0D;
						}

						d26 = d26 * (1.0D - d37) + -3000.0D * d37;
					}

					b36 = 8;
					if(i25 < b36) {
						d37 = (double)((float)(b36 - i25) / ((float)b36 - 1.0F));
						d26 = d26 * (1.0D - d37) + -30.0D * d37;
					}

					d1[i12] = d26;
					++i12;
				}
			}
		}

		return d1;
	}

	public boolean chunkExists(int i1, int i2) {
		return true;
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		BlockSand.fallInstantly = true;
		int i4 = i2 * 16;
		int i5 = i3 * 16;
		BiomeGenBase biomeGenBase6 = this.endWorld.getBiomeGenForCoords(i4 + 16, i5 + 16);
		biomeGenBase6.decorate(this.endWorld, this.endWorld.rand, i4, i5);
		BlockSand.fallInstantly = false;
	}

	public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return true;
	}

	public List getPossibleCreatures(EnumCreatureType enumCreatureType1, int i2, int i3, int i4) {
		BiomeGenBase biomeGenBase5 = this.endWorld.getBiomeGenForCoords(i2, i4);
		return biomeGenBase5 == null ? null : biomeGenBase5.getSpawnableList(enumCreatureType1);
	}

	public ChunkPosition findClosestStructure(World world1, String string2, int i3, int i4, int i5) {
		return null;
	}
}

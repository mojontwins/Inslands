package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderGenerate implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	private World worldObj;
	private final boolean mapFeaturesEnabled;
	private double[] noiseArray;
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCaves();
	private MapGenStronghold strongholdGenerator = new MapGenStronghold();
	private MapGenVillage villageGenerator = new MapGenVillage(0);
	private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
	private MapGenBase ravineGenerator = new MapGenRavine();
	private BiomeGenBase[] biomesForGeneration;
	double[] noise3;
	double[] noise1;
	double[] noise2;
	double[] noise5;
	double[] noise6;
	float[] field_35388_l;
	int[][] field_914_i = new int[32][32];

	public ChunkProviderGenerate(World world1, long j2, boolean z4) {
		this.worldObj = world1;
		this.mapFeaturesEnabled = z4;
		this.rand = new Random(j2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
	}

	public void generateTerrain(int i1, int i2, byte[] b3) {
		byte b4 = 4;
		byte b5 = 16;
		byte b6 = 63;
		int i7 = b4 + 1;
		byte b8 = 17;
		int i9 = b4 + 1;
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, i1 * 4 - 2, i2 * 4 - 2, i7 + 5, i9 + 5);
		this.noiseArray = this.initializeNoiseField(this.noiseArray, i1 * b4, 0, i2 * b4, i7, b8, i9);

		for(int i10 = 0; i10 < b4; ++i10) {
			for(int i11 = 0; i11 < b4; ++i11) {
				for(int i12 = 0; i12 < b5; ++i12) {
					double d13 = 0.125D;
					double d15 = this.noiseArray[((i10 + 0) * i9 + i11 + 0) * b8 + i12 + 0];
					double d17 = this.noiseArray[((i10 + 0) * i9 + i11 + 1) * b8 + i12 + 0];
					double d19 = this.noiseArray[((i10 + 1) * i9 + i11 + 0) * b8 + i12 + 0];
					double d21 = this.noiseArray[((i10 + 1) * i9 + i11 + 1) * b8 + i12 + 0];
					double d23 = (this.noiseArray[((i10 + 0) * i9 + i11 + 0) * b8 + i12 + 1] - d15) * d13;
					double d25 = (this.noiseArray[((i10 + 0) * i9 + i11 + 1) * b8 + i12 + 1] - d17) * d13;
					double d27 = (this.noiseArray[((i10 + 1) * i9 + i11 + 0) * b8 + i12 + 1] - d19) * d13;
					double d29 = (this.noiseArray[((i10 + 1) * i9 + i11 + 1) * b8 + i12 + 1] - d21) * d13;

					for(int i31 = 0; i31 < 8; ++i31) {
						double d32 = 0.25D;
						double d34 = d15;
						double d36 = d17;
						double d38 = (d19 - d15) * d32;
						double d40 = (d21 - d17) * d32;

						for(int i42 = 0; i42 < 4; ++i42) {
							int i43 = i42 + i10 * 4 << 11 | 0 + i11 * 4 << 7 | i12 * 8 + i31;
							short s44 = 128;
							i43 -= s44;
							double d45 = 0.25D;
							double d49 = (d36 - d34) * d45;
							double d47 = d34 - d49;

							for(int i51 = 0; i51 < 4; ++i51) {
								if((d47 += d49) > 0.0D) {
									b3[i43 += s44] = (byte)Block.stone.blockID;
								} else if(i12 * 8 + i31 < b6) {
									b3[i43 += s44] = (byte)Block.waterStill.blockID;
								} else {
									b3[i43 += s44] = 0;
								}
							}

							d34 += d38;
							d36 += d40;
						}

						d15 += d23;
						d17 += d25;
						d19 += d27;
						d21 += d29;
					}
				}
			}
		}

	}

	public void replaceBlocksForBiome(int i1, int i2, byte[] b3, BiomeGenBase[] biomeGenBase4) {
		byte b5 = 63;
		double d6 = 8.0D / 256D;
		this.stoneNoise = this.noiseGen4.generateNoiseOctaves(this.stoneNoise, i1 * 16, i2 * 16, 0, 16, 16, 1, d6 * 2.0D, d6 * 2.0D, d6 * 2.0D);

		for(int i8 = 0; i8 < 16; ++i8) {
			for(int i9 = 0; i9 < 16; ++i9) {
				BiomeGenBase biomeGenBase10 = biomeGenBase4[i9 + i8 * 16];
				float f11 = biomeGenBase10.getFloatTemperature();
				int i12 = (int)(this.stoneNoise[i8 + i9 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i13 = -1;
				byte b14 = biomeGenBase10.topBlock;
				byte b15 = biomeGenBase10.fillerBlock;

				for(int i16 = 127; i16 >= 0; --i16) {
					int i17 = (i9 * 16 + i8) * 128 + i16;
					if(i16 <= 0 + this.rand.nextInt(5)) {
						b3[i17] = (byte)Block.bedrock.blockID;
					} else {
						byte b18 = b3[i17];
						if(b18 == 0) {
							i13 = -1;
						} else if(b18 == Block.stone.blockID) {
							if(i13 == -1) {
								if(i12 <= 0) {
									b14 = 0;
									b15 = (byte)Block.stone.blockID;
								} else if(i16 >= b5 - 4 && i16 <= b5 + 1) {
									b14 = biomeGenBase10.topBlock;
									b15 = biomeGenBase10.fillerBlock;
								}

								if(i16 < b5 && b14 == 0) {
									if(f11 < 0.15F) {
										b14 = (byte)Block.ice.blockID;
									} else {
										b14 = (byte)Block.waterStill.blockID;
									}
								}

								i13 = i12;
								if(i16 >= b5 - 1) {
									b3[i17] = b14;
								} else {
									b3[i17] = b15;
								}
							} else if(i13 > 0) {
								--i13;
								b3[i17] = b15;
								if(i13 == 0 && b15 == Block.sand.blockID) {
									i13 = this.rand.nextInt(4);
									b15 = (byte)Block.sandStone.blockID;
								}
							}
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
		this.rand.setSeed((long)i1 * 341873128712L + (long)i2 * 132897987541L);
		byte[] b3 = new byte[32768];
		this.generateTerrain(i1, i2, b3);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, i1 * 16, i2 * 16, 16, 16);
		this.replaceBlocksForBiome(i1, i2, b3, this.biomesForGeneration);
		this.caveGenerator.generate(this, this.worldObj, i1, i2, b3);
		this.ravineGenerator.generate(this, this.worldObj, i1, i2, b3);
		if(this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generate(this, this.worldObj, i1, i2, b3);
			this.villageGenerator.generate(this, this.worldObj, i1, i2, b3);
			this.strongholdGenerator.generate(this, this.worldObj, i1, i2, b3);
		}

		Chunk chunk4 = new Chunk(this.worldObj, b3, i1, i2);
		byte[] b5 = chunk4.getBiomeArray();

		for(int i6 = 0; i6 < b5.length; ++i6) {
			b5[i6] = (byte)this.biomesForGeneration[i6].biomeID;
		}

		chunk4.generateSkylightMap();
		return chunk4;
	}

	private double[] initializeNoiseField(double[] d1, int i2, int i3, int i4, int i5, int i6, int i7) {
		if(d1 == null) {
			d1 = new double[i5 * i6 * i7];
		}

		if(this.field_35388_l == null) {
			this.field_35388_l = new float[25];

			for(int i8 = -2; i8 <= 2; ++i8) {
				for(int i9 = -2; i9 <= 2; ++i9) {
					float f10 = 10.0F / MathHelper.sqrt_float((float)(i8 * i8 + i9 * i9) + 0.2F);
					this.field_35388_l[i8 + 2 + (i9 + 2) * 5] = f10;
				}
			}
		}

		double d44 = 684.412D;
		double d45 = 684.412D;
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, i2, i4, i5, i7, 1.121D, 1.121D, 0.5D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, i2, i4, i5, i7, 200.0D, 200.0D, 0.5D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, i2, i3, i4, i5, i6, i7, d44 / 80.0D, d45 / 160.0D, d44 / 80.0D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, i2, i3, i4, i5, i6, i7, d44, d45, d44);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, i2, i3, i4, i5, i6, i7, d44, d45, d44);
		boolean z43 = false;
		boolean z42 = false;
		int i12 = 0;
		int i13 = 0;

		for(int i14 = 0; i14 < i5; ++i14) {
			for(int i15 = 0; i15 < i7; ++i15) {
				float f16 = 0.0F;
				float f17 = 0.0F;
				float f18 = 0.0F;
				byte b19 = 2;
				BiomeGenBase biomeGenBase20 = this.biomesForGeneration[i14 + 2 + (i15 + 2) * (i5 + 5)];

				for(int i21 = -b19; i21 <= b19; ++i21) {
					for(int i22 = -b19; i22 <= b19; ++i22) {
						BiomeGenBase biomeGenBase23 = this.biomesForGeneration[i14 + i21 + 2 + (i15 + i22 + 2) * (i5 + 5)];
						float f24 = this.field_35388_l[i21 + 2 + (i22 + 2) * 5] / (biomeGenBase23.minHeight + 2.0F);
						if(biomeGenBase23.minHeight > biomeGenBase20.minHeight) {
							f24 /= 2.0F;
						}

						f16 += biomeGenBase23.maxHeight * f24;
						f17 += biomeGenBase23.minHeight * f24;
						f18 += f24;
					}
				}

				f16 /= f18;
				f17 /= f18;
				f16 = f16 * 0.9F + 0.1F;
				f17 = (f17 * 4.0F - 1.0F) / 8.0F;
				double d46 = this.noise6[i13] / 8000.0D;
				if(d46 < 0.0D) {
					d46 = -d46 * 0.3D;
				}

				d46 = d46 * 3.0D - 2.0D;
				if(d46 < 0.0D) {
					d46 /= 2.0D;
					if(d46 < -1.0D) {
						d46 = -1.0D;
					}

					d46 /= 1.4D;
					d46 /= 2.0D;
				} else {
					if(d46 > 1.0D) {
						d46 = 1.0D;
					}

					d46 /= 8.0D;
				}

				++i13;

				for(int i47 = 0; i47 < i6; ++i47) {
					double d48 = (double)f17;
					double d26 = (double)f16;
					d48 += d46 * 0.2D;
					d48 = d48 * (double)i6 / 16.0D;
					double d28 = (double)i6 / 2.0D + d48 * 4.0D;
					double d30 = 0.0D;
					double d32 = ((double)i47 - d28) * 12.0D * 128.0D / 128.0D / d26;
					if(d32 < 0.0D) {
						d32 *= 4.0D;
					}

					double d34 = this.noise1[i12] / 512.0D;
					double d36 = this.noise2[i12] / 512.0D;
					double d38 = (this.noise3[i12] / 10.0D + 1.0D) / 2.0D;
					if(d38 < 0.0D) {
						d30 = d34;
					} else if(d38 > 1.0D) {
						d30 = d36;
					} else {
						d30 = d34 + (d36 - d34) * d38;
					}

					d30 -= d32;
					if(i47 > i6 - 4) {
						double d40 = (double)((float)(i47 - (i6 - 4)) / 3.0F);
						d30 = d30 * (1.0D - d40) + -10.0D * d40;
					}

					d1[i12] = d30;
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
		BiomeGenBase biomeGenBase6 = this.worldObj.getBiomeGenForCoords(i4 + 16, i5 + 16);
		this.rand.setSeed(this.worldObj.getSeed());
		long j7 = this.rand.nextLong() / 2L * 2L + 1L;
		long j9 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)i2 * j7 + (long)i3 * j9 ^ this.worldObj.getSeed());
		boolean z11 = false;
		if(this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, i2, i3);
			z11 = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.rand, i2, i3);
			this.strongholdGenerator.generateStructuresInChunk(this.worldObj, this.rand, i2, i3);
		}

		int i12;
		int i13;
		int i14;
		if(!z11 && this.rand.nextInt(4) == 0) {
			i12 = i4 + this.rand.nextInt(16) + 8;
			i13 = this.rand.nextInt(128);
			i14 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenLakes(Block.waterStill.blockID)).generate(this.worldObj, this.rand, i12, i13, i14);
		}

		if(!z11 && this.rand.nextInt(8) == 0) {
			i12 = i4 + this.rand.nextInt(16) + 8;
			i13 = this.rand.nextInt(this.rand.nextInt(120) + 8);
			i14 = i5 + this.rand.nextInt(16) + 8;
			if(i13 < 63 || this.rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.lavaStill.blockID)).generate(this.worldObj, this.rand, i12, i13, i14);
			}
		}

		for(i12 = 0; i12 < 8; ++i12) {
			i13 = i4 + this.rand.nextInt(16) + 8;
			i14 = this.rand.nextInt(128);
			int i15 = i5 + this.rand.nextInt(16) + 8;
			if((new WorldGenDungeons()).generate(this.worldObj, this.rand, i13, i14, i15)) {
				;
			}
		}

		biomeGenBase6.decorate(this.worldObj, this.rand, i4, i5);
		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomeGenBase6, i4 + 8, i5 + 8, 16, 16, this.rand);
		i4 += 8;
		i5 += 8;

		for(i12 = 0; i12 < 16; ++i12) {
			for(i13 = 0; i13 < 16; ++i13) {
				i14 = this.worldObj.getPrecipitationHeight(i4 + i12, i5 + i13);
				if(this.worldObj.isBlockHydratedDirectly(i12 + i4, i14 - 1, i13 + i5)) {
					this.worldObj.setBlockWithNotify(i12 + i4, i14 - 1, i13 + i5, Block.ice.blockID);
				}

				if(this.worldObj.canSnowAt(i12 + i4, i14, i13 + i5)) {
					this.worldObj.setBlockWithNotify(i12 + i4, i14, i13 + i5, Block.snow.blockID);
				}
			}
		}

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

	public String makeString() {
		return "RandomLevelSource";
	}

	public List getPossibleCreatures(EnumCreatureType enumCreatureType1, int i2, int i3, int i4) {
		BiomeGenBase biomeGenBase5 = this.worldObj.getBiomeGenForCoords(i2, i4);
		return biomeGenBase5 == null ? null : biomeGenBase5.getSpawnableList(enumCreatureType1);
	}

	public ChunkPosition findClosestStructure(World world1, String string2, int i3, int i4, int i5) {
		return "Stronghold".equals(string2) && this.strongholdGenerator != null ? this.strongholdGenerator.getNearestInstance(world1, i3, i4, i5) : null;
	}
}

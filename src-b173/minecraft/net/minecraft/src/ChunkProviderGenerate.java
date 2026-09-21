package net.minecraft.src;

import java.util.Random;

public class ChunkProviderGenerate implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGenSandOrGravel;
	private NoiseGeneratorOctaves noiseStone;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	private World worldObj;
	private double[] terrainNoise;
	private double[] sandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCaves();
	private BiomeGenBase[] biomesForGeneration;
	double[] noise3;
	double[] noise1;
	double[] noise2;
	double[] noise5;
	double[] noise6;
	int[][] unused = new int[32][32];
	private double[] generatedTemperatures;

	public ChunkProviderGenerate(World world1, long j2) {
		this.worldObj = world1;
		this.rand = new Random(j2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGenSandOrGravel = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseStone = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
	}

	public void generateTerrain(int chunkX, int chunkZ, byte[] blocks, BiomeGenBase[] biomes, double[] temperatures) {
		final double noiseScale = 0.125D;
		final double scalingFactor = 0.25D;
		final double densityVariationSpeed = 0.25D;
		
		final byte quadrantSize = 4;
		final byte seaLevel = 64;
		final int cellSize = quadrantSize + 1;
		final byte columnSize = 17;
		final int cellSize2 = quadrantSize + 1;
		final short chunkHeight = 128;

		this.terrainNoise = this.initializeNoiseField(this.terrainNoise, chunkX * quadrantSize, 0, chunkZ * quadrantSize, cellSize, columnSize, cellSize2);

		for(int xSection = 0; xSection < quadrantSize; ++xSection) {
			for(int zSection = 0; zSection < quadrantSize; ++zSection) {
				for(int ySection = 0; ySection < 16; ++ySection) {
					
					double noiseA = this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 0) * columnSize + ySection + 0];
					double noiseB = this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 1) * columnSize + ySection + 0];
					double noiseC = this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 0) * columnSize + ySection + 0];
					double noiseD = this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 1) * columnSize + ySection + 0];
					double noiseAinc = (this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 0) * columnSize + ySection + 1] - noiseA) * noiseScale;
					double noiseBinc = (this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 1) * columnSize + ySection + 1] - noiseB) * noiseScale;
					double noiseCinc = (this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 0) * columnSize + ySection + 1] - noiseC) * noiseScale;
					double noiseDinc = (this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 1) * columnSize + ySection + 1] - noiseD) * noiseScale;

					for(int y = 0; y < 8; ++y) {
						double curNoiseA = noiseA;
						double curNoiseB = noiseB;
						double curNoiseAinc = (noiseC - noiseA) * scalingFactor;
						double curNoiseBinc = (noiseD - noiseB) * scalingFactor;

						int yy = ySection * 8 + y;

						for(int x = 0; x < 4; ++x) {
							int indexInBlockArray = (x + (xSection << 2)) << 11 | (0 + (zSection << 2)) << 7 | (ySection << 3) + y;
					
							double density = curNoiseA;
							double densityIncrement = (curNoiseB - curNoiseA) * densityVariationSpeed;

							for(int z = 0; z < 4; ++z) {
								int biomeIndex = (x + (xSection << 2)) << 4 | (z + (zSection << 2));
								double temperature = temperatures[biomeIndex];

								int blockID = 0;
								if(yy < seaLevel) {
									if(temperature < 0.5D && yy >= seaLevel - 1) {
										blockID = Block.ice.blockID;
									} else {
										blockID = Block.waterStill.blockID;
									}
								}

								if(density > 0.0D) {
									blockID = Block.stone.blockID;
								}

								blocks[indexInBlockArray] = (byte)blockID;
								indexInBlockArray += chunkHeight;
								density += densityIncrement;
							}

							curNoiseA += curNoiseAinc;
							curNoiseB += curNoiseBinc;
						}

						noiseA += noiseAinc;
						noiseB += noiseBinc;
						noiseC += noiseCinc;
						noiseD += noiseDinc;
					}
				}
			}
		}

	}

	public void replaceBlocksForBiome(int chunkX, int chunkZ, byte[] blocks, BiomeGenBase[] biomes) {
		byte seaLevel = 64;
		double d6 = 8.0D / 256D;
		this.sandNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.sandNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, d6, d6, 1.0D);
		this.gravelNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.gravelNoise, (double)(chunkX * 16), 109.0134D, (double)(chunkZ * 16), 16, 1, 16, d6, 1.0D, d6);
		this.stoneNoise = this.noiseStone.generateNoiseOctaves(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, d6 * 2.0D, d6 * 2.0D, d6 * 2.0D);

		int biomeIndex = 0;
		BiomeGenBase biomeGen;

		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				biomeGen = biomes[biomeIndex ++];

				int noiseIndex = x + (z << 4);
				boolean generateSand = this.sandNoise[noiseIndex] + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean generateGravel = this.gravelNoise[noiseIndex] + this.rand.nextDouble() * 0.2D > 3.0D;
				int i13 = (int)(this.stoneNoise[noiseIndex] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i14 = -1;
				byte topBlock = biomeGen.topBlock;
				byte fillerBlock = biomeGen.fillerBlock;

				for(int y = 127; y >= 0; --y) {
					int index = x << 11 | z << 7 | y; // (x * 16 + z) * 128 + y
					if(y <= 0 + this.rand.nextInt(5)) {
						blocks[index] = (byte)Block.bedrock.blockID;
					} else {
						byte blockID = blocks[index];
						if(blockID == 0) {
							i14 = -1;
						} else if(blockID == Block.stone.blockID) {
							if(i14 == -1) {
								if(i13 <= 0) {
									topBlock = 0;
									fillerBlock = (byte)Block.stone.blockID;
								} else if(y >= seaLevel - 4 && y <= seaLevel + 1) {
									topBlock = biomeGen.topBlock;
									fillerBlock = biomeGen.fillerBlock;
									if(generateGravel) {
										topBlock = 0;
										fillerBlock = (byte)Block.gravel.blockID;
									}

									if(generateSand) {
										topBlock = (byte)Block.sand.blockID;
										fillerBlock = (byte)Block.sand.blockID;
									}
								}

								if(y < seaLevel && topBlock == 0) {
									topBlock = (byte)Block.waterStill.blockID;
								}

								i14 = i13;
								if(y >= seaLevel - 1) {
									blocks[index] = topBlock;
								} else {
									blocks[index] = fillerBlock;
								}
							} else if(i14 > 0) {
								--i14;
								blocks[index] = fillerBlock;
								if(i14 == 0 && fillerBlock == Block.sand.blockID) {
									i14 = this.rand.nextInt(4);
									fillerBlock = (byte)Block.sandStone.blockID;
								}
							}
						}
					}
				}
			}
		}

	}

	public Chunk prepareChunk(int i1, int i2) {
		return this.provideChunk(i1, i2);
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		
		// Empty block array & new Chunk
		byte[] blockArray = new byte[32768];
		Chunk chunk = new Chunk(this.worldObj, blockArray, chunkX, chunkZ);

		// Calculate biomes & temperatures for this chunk
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
		double[] temperatures = this.worldObj.getWorldChunkManager().temperature;

		// Generate terrain for this chunk
		this.generateTerrain(chunkX, chunkZ, blockArray, this.biomesForGeneration, temperatures);
		
		// Replace blocks
		this.replaceBlocksForBiome(chunkX, chunkZ, blockArray, this.biomesForGeneration);

		// Generate caves
		this.caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);

		// Calculate lights
		chunk.generateSkylightMap();

		// Done
		return chunk;
	}

	private double[] initializeNoiseField(double[] d1, int i2, int i3, int i4, int i5, int i6, int i7) {
	
		// The noise field makes Alpha and Beta terrain generate differently.
		
		if(d1 == null) {
			d1 = new double[i5 * i6 * i7];
		}

		double d8 = 684.412D;
		double d10 = 684.412D;
		double[] d12 = this.worldObj.getWorldChunkManager().temperature;
		double[] d13 = this.worldObj.getWorldChunkManager().humidity;
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, i2, i4, i5, i7, 1.121D, 1.121D, 0.5D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, i2, i4, i5, i7, 200.0D, 200.0D, 0.5D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8 / 80.0D, d10 / 160.0D, d8 / 80.0D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8, d10, d8);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8, d10, d8);
		int i14 = 0;
		int i15 = 0;
		int i16 = 16 / i5;

		for(int i17 = 0; i17 < i5; ++i17) {
			int i18 = i17 * i16 + i16 / 2;

			for(int i19 = 0; i19 < i7; ++i19) {
				int i20 = i19 * i16 + i16 / 2;
				double d21 = d12[i18 * 16 + i20];
				double d23 = d13[i18 * 16 + i20] * d21;
				double d25 = 1.0D - d23;
				d25 *= d25;
				d25 *= d25;
				d25 = 1.0D - d25;
				double d27 = (this.noise5[i15] + 256.0D) / 512.0D;
				d27 *= d25;
				if(d27 > 1.0D) {
					d27 = 1.0D;
				}

				double d29 = this.noise6[i15] / 8000.0D;
				if(d29 < 0.0D) {
					d29 = -d29 * 0.3D;
				}

				d29 = d29 * 3.0D - 2.0D;
				if(d29 < 0.0D) {
					d29 /= 2.0D;
					if(d29 < -1.0D) {
						d29 = -1.0D;
					}

					d29 /= 1.4D;
					d29 /= 2.0D;
					d27 = 0.0D;
				} else {
					if(d29 > 1.0D) {
						d29 = 1.0D;
					}

					d29 /= 8.0D;
				}

				if(d27 < 0.0D) {
					d27 = 0.0D;
				}

				d27 += 0.5D;
				d29 = d29 * (double)i6 / 16.0D;
				double d31 = (double)i6 / 2.0D + d29 * 4.0D;
				++i15;

				for(int i33 = 0; i33 < i6; ++i33) {
					double d34 = 0.0D;
					double d36 = ((double)i33 - d31) * 12.0D / d27;
					if(d36 < 0.0D) {
						d36 *= 4.0D;
					}

					double d38 = this.noise1[i14] / 512.0D;
					double d40 = this.noise2[i14] / 512.0D;
					double d42 = (this.noise3[i14] / 10.0D + 1.0D) / 2.0D;
					if(d42 < 0.0D) {
						d34 = d38;
					} else if(d42 > 1.0D) {
						d34 = d40;
					} else {
						d34 = d38 + (d40 - d38) * d42;
					}

					d34 -= d36;
					if(i33 > i6 - 4) {
						double d44 = (double)((float)(i33 - (i6 - 4)) / 3.0F);
						d34 = d34 * (1.0D - d44) + -10.0D * d44;
					}

					d1[i14] = d34;
					++i14;
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
		BiomeGenBase biomeGenBase6 = this.worldObj.getWorldChunkManager().getBiomeGenAt(i4 + 16, i5 + 16);
		this.rand.setSeed(this.worldObj.getRandomSeed());
		long j7 = this.rand.nextLong() / 2L * 2L + 1L;
		long j9 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)i2 * j7 + (long)i3 * j9 ^ this.worldObj.getRandomSeed());
		double d11 = 0.25D;
		int i13;
		int i14;
		int i15;
		if(this.rand.nextInt(4) == 0) {
			i13 = i4 + this.rand.nextInt(16) + 8;
			i14 = this.rand.nextInt(128);
			i15 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenLakes(Block.waterStill.blockID)).generate(this.worldObj, this.rand, i13, i14, i15);
		}

		if(this.rand.nextInt(8) == 0) {
			i13 = i4 + this.rand.nextInt(16) + 8;
			i14 = this.rand.nextInt(this.rand.nextInt(120) + 8);
			i15 = i5 + this.rand.nextInt(16) + 8;
			if(i14 < 64 || this.rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.lavaStill.blockID)).generate(this.worldObj, this.rand, i13, i14, i15);
			}
		}

		int i16;
		for(i13 = 0; i13 < 8; ++i13) {
			i14 = i4 + this.rand.nextInt(16) + 8;
			i15 = this.rand.nextInt(128);
			i16 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenDungeons()).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 10; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(128);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenClay(32)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 20; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(128);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.dirt.blockID, 32)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 10; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(128);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.gravel.blockID, 32)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 20; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(128);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 20; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(64);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 2; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(32);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 8; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(16);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 1; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(16);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		for(i13 = 0; i13 < 1; ++i13) {
			i14 = i4 + this.rand.nextInt(16);
			i15 = this.rand.nextInt(16) + this.rand.nextInt(16);
			i16 = i5 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreLapis.blockID, 6)).generate(this.worldObj, this.rand, i14, i15, i16);
		}

		d11 = 0.5D;
		i13 = (int)((this.mobSpawnerNoise.func_806_a((double)i4 * d11, (double)i5 * d11) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
		i14 = 0;
		if(this.rand.nextInt(10) == 0) {
			++i14;
		}

		if(biomeGenBase6 == BiomeGenBase.forest) {
			i14 += i13 + 5;
		}

		if(biomeGenBase6 == BiomeGenBase.rainforest) {
			i14 += i13 + 5;
		}

		if(biomeGenBase6 == BiomeGenBase.seasonalForest) {
			i14 += i13 + 2;
		}

		if(biomeGenBase6 == BiomeGenBase.taiga) {
			i14 += i13 + 5;
		}

		if(biomeGenBase6 == BiomeGenBase.desert) {
			i14 -= 20;
		}

		if(biomeGenBase6 == BiomeGenBase.tundra) {
			i14 -= 20;
		}

		if(biomeGenBase6 == BiomeGenBase.plains) {
			i14 -= 20;
		}

		int i17;
		for(i15 = 0; i15 < i14; ++i15) {
			i16 = i4 + this.rand.nextInt(16) + 8;
			i17 = i5 + this.rand.nextInt(16) + 8;
			WorldGenerator worldGenerator18 = biomeGenBase6.getRandomWorldGenForTrees(this.rand);
			worldGenerator18.setScale(1.0D, 1.0D, 1.0D);
			worldGenerator18.generate(this.worldObj, this.rand, i16, this.worldObj.getHeightValue(i16, i17), i17);
		}

		byte b27 = 0;
		if(biomeGenBase6 == BiomeGenBase.forest) {
			b27 = 2;
		}

		if(biomeGenBase6 == BiomeGenBase.seasonalForest) {
			b27 = 4;
		}

		if(biomeGenBase6 == BiomeGenBase.taiga) {
			b27 = 2;
		}

		if(biomeGenBase6 == BiomeGenBase.plains) {
			b27 = 3;
		}

		int i19;
		int i25;
		for(i16 = 0; i16 < b27; ++i16) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i25 = this.rand.nextInt(128);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, i17, i25, i19);
		}

		byte b28 = 0;
		if(biomeGenBase6 == BiomeGenBase.forest) {
			b28 = 2;
		}

		if(biomeGenBase6 == BiomeGenBase.rainforest) {
			b28 = 10;
		}

		if(biomeGenBase6 == BiomeGenBase.seasonalForest) {
			b28 = 2;
		}

		if(biomeGenBase6 == BiomeGenBase.taiga) {
			b28 = 1;
		}

		if(biomeGenBase6 == BiomeGenBase.plains) {
			b28 = 10;
		}

		int i20;
		int i21;
		for(i17 = 0; i17 < b28; ++i17) {
			byte b26 = 1;
			if(biomeGenBase6 == BiomeGenBase.rainforest && this.rand.nextInt(3) != 0) {
				b26 = 2;
			}

			i19 = i4 + this.rand.nextInt(16) + 8;
			i20 = this.rand.nextInt(128);
			i21 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenTallGrass(Block.tallGrass.blockID, b26)).generate(this.worldObj, this.rand, i19, i20, i21);
		}

		b28 = 0;
		if(biomeGenBase6 == BiomeGenBase.desert) {
			b28 = 2;
		}

		for(i17 = 0; i17 < b28; ++i17) {
			i25 = i4 + this.rand.nextInt(16) + 8;
			i19 = this.rand.nextInt(128);
			i20 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenDeadBush(Block.deadBush.blockID)).generate(this.worldObj, this.rand, i25, i19, i20);
		}

		if(this.rand.nextInt(2) == 0) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i25 = this.rand.nextInt(128);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, i17, i25, i19);
		}

		if(this.rand.nextInt(4) == 0) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i25 = this.rand.nextInt(128);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, i17, i25, i19);
		}

		if(this.rand.nextInt(8) == 0) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i25 = this.rand.nextInt(128);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, i17, i25, i19);
		}

		for(i17 = 0; i17 < 10; ++i17) {
			i25 = i4 + this.rand.nextInt(16) + 8;
			i19 = this.rand.nextInt(128);
			i20 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenReed()).generate(this.worldObj, this.rand, i25, i19, i20);
		}

		if(this.rand.nextInt(32) == 0) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i25 = this.rand.nextInt(128);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenPumpkin()).generate(this.worldObj, this.rand, i17, i25, i19);
		}

		i17 = 0;
		if(biomeGenBase6 == BiomeGenBase.desert) {
			i17 += 10;
		}

		for(i25 = 0; i25 < i17; ++i25) {
			i19 = i4 + this.rand.nextInt(16) + 8;
			i20 = this.rand.nextInt(128);
			i21 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenCactus()).generate(this.worldObj, this.rand, i19, i20, i21);
		}

		for(i25 = 0; i25 < 50; ++i25) {
			i19 = i4 + this.rand.nextInt(16) + 8;
			i20 = this.rand.nextInt(this.rand.nextInt(120) + 8);
			i21 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, i19, i20, i21);
		}

		for(i25 = 0; i25 < 20; ++i25) {
			i19 = i4 + this.rand.nextInt(16) + 8;
			i20 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
			i21 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, i19, i20, i21);
		}

		this.generatedTemperatures = this.worldObj.getWorldChunkManager().getTemperatures(this.generatedTemperatures, i4 + 8, i5 + 8, 16, 16);

		for(i25 = i4 + 8; i25 < i4 + 8 + 16; ++i25) {
			for(i19 = i5 + 8; i19 < i5 + 8 + 16; ++i19) {
				i20 = i25 - (i4 + 8);
				i21 = i19 - (i5 + 8);
				int i22 = this.worldObj.findTopSolidBlockUsingBlockMaterial(i25, i19);
				double d23 = this.generatedTemperatures[i20 * 16 + i21] - (double)(i22 - 64) / 64.0D * 0.3D;
				if(d23 < 0.5D && i22 > 0 && i22 < 128 && this.worldObj.isAirBlock(i25, i22, i19) && this.worldObj.getBlockMaterial(i25, i22 - 1, i19).getIsSolid() && this.worldObj.getBlockMaterial(i25, i22 - 1, i19) != Material.ice) {
					this.worldObj.setBlockWithNotify(i25, i22, i19, Block.snow.blockID);
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
}

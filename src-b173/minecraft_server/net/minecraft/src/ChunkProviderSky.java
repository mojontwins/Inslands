package net.minecraft.src;

import java.util.Random;

public class ChunkProviderSky implements IChunkProvider {
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

	public ChunkProviderSky(World world1, long j2) {
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
		double noiseScale = 0.25D;
		double yscalingFactor = 0.125D;
		double densityVariationSpeed = 0.125D;

		byte quadrantSize = 2;

		int cellSize = quadrantSize + 1;
		byte columnSize = 33;
		int cellSize2 = quadrantSize + 1;
		short chunkHeight = 128;

		this.terrainNoise = this.initializeNoiseField(this.terrainNoise, chunkX * quadrantSize, 0, chunkZ * quadrantSize, cellSize, columnSize, cellSize2);

		for(int xSection = 0; xSection < quadrantSize; ++xSection) {
			for(int zSection = 0; zSection < quadrantSize; ++zSection) {
				for(int ySection = 0; ySection < 32; ++ySection) {
					
					double noiseA = this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 0) * columnSize + ySection + 0];
					double noiseB = this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 1) * columnSize + ySection + 0];
					double noiseC = this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 0) * columnSize + ySection + 0];
					double noiseD = this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 1) * columnSize + ySection + 0];
					double noiseAinc = (this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 0) * columnSize + ySection + 1] - noiseA) * noiseScale;
					double noiseBinc = (this.terrainNoise[((xSection + 0) * cellSize2 + zSection + 1) * columnSize + ySection + 1] - noiseB) * noiseScale;
					double noiseCinc = (this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 0) * columnSize + ySection + 1] - noiseC) * noiseScale;
					double noiseDinc = (this.terrainNoise[((xSection + 1) * cellSize2 + zSection + 1) * columnSize + ySection + 1] - noiseD) * noiseScale;

					for(int y = 0; y < 4; ++y) {
						
						double curNoiseA = noiseA;
						double curNoiseB = noiseB;
						double curNoiseAinc = (noiseC - noiseA) * yscalingFactor;
						double curNoiseBinc = (noiseD - noiseB) * yscalingFactor;

						for(int x = 0; x < 8; ++x) {
							int indexInBlockArray = (x + (xSection << 3)) << 11 | (0 + (zSection << 3)) << 7 | (ySection << 2) + y;
														
							double density = curNoiseA;
							double densityIncrement = (curNoiseB - curNoiseA) * densityVariationSpeed;

							for(int z = 0; z < 8; ++z) {
								int blockID = 0;
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
		double d5 = 8.0D / 256D;
		this.sandNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.sandNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, d5, d5, 1.0D);
		this.gravelNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.gravelNoise, (double)(chunkX * 16), 109.0134D, (double)(chunkZ * 16), 16, 1, 16, d5, 1.0D, d5);
		this.stoneNoise = this.noiseStone.generateNoiseOctaves(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, d5 * 2.0D, d5 * 2.0D, d5 * 2.0D);

		int biomeIndex = 0;
		BiomeGenBase biomeGen;

		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				biomeGen = biomes[biomeIndex ++];
				
				int noiseIndex = x + (z << 4);
				int i10 = (int)(this.stoneNoise[noiseIndex] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i11 = -1;
				byte topBlock = biomeGen.topBlock;
				byte fillerBlock = biomeGen.fillerBlock;

				for(int y = 127; y >= 0; --y) {
					int index = x << 11 | z << 7 | y; // (x * 16 + z) * 128 + y
					byte blockID = blocks[index];
					if(blockID == 0) {
						i11 = -1;
					} else if(blockID == Block.stone.blockID) {
						if(i11 == -1) {
							if(i10 <= 0) {
								topBlock = 0;
								fillerBlock = (byte)Block.stone.blockID;
							}

							i11 = i10;
							if(y >= 0) {
								blocks[index] = topBlock;
							} else {
								blocks[index] = fillerBlock;
							}
						} else if(i11 > 0) {
							--i11;
							blocks[index] = fillerBlock;
							if(i11 == 0 && fillerBlock == Block.sand.blockID) {
								i11 = this.rand.nextInt(4);
								fillerBlock = (byte)Block.sandStone.blockID;
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
		if(d1 == null) {
			d1 = new double[i5 * i6 * i7];
		}

		double d8 = 684.412D;
		double d10 = 684.412D;
		double[] d12 = this.worldObj.getWorldChunkManager().temperature;
		double[] d13 = this.worldObj.getWorldChunkManager().humidity;
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, i2, i4, i5, i7, 1.121D, 1.121D, 0.5D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, i2, i4, i5, i7, 200.0D, 200.0D, 0.5D);
		d8 *= 2.0D; 	// This makes the difference between overworld & sky dimension
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
				if(d29 > 1.0D) {
					d29 = 1.0D;
				}

				d29 /= 8.0D;
				d29 = 0.0D;
				if(d27 < 0.0D) {
					d27 = 0.0D;
				}

				d27 += 0.5D;
				d29 = d29 * (double)i6 / 16.0D;
				++i15;
				double d31 = (double)i6 / 2.0D;

				for(int i33 = 0; i33 < i6; ++i33) {
					double d34 = 0.0D;
					double d36 = ((double)i33 - d31) * 8.0D / d27;
					if(d36 < 0.0D) {
						d36 *= -1.0D;
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

					d34 -= 8.0D;
					byte b44 = 32;
					double d45;
					if(i33 > i6 - b44) {
						d45 = (double)((float)(i33 - (i6 - b44)) / ((float)b44 - 1.0F));
						d34 = d34 * (1.0D - d45) + -30.0D * d45;
					}

					b44 = 8;
					if(i33 < b44) {
						d45 = (double)((float)(b44 - i33) / ((float)b44 - 1.0F));
						d34 = d34 * (1.0D - d45) + -30.0D * d45;
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

		int i23;
		for(i15 = 0; i15 < 2; ++i15) {
			i16 = i4 + this.rand.nextInt(16) + 8;
			i17 = this.rand.nextInt(128);
			i23 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, i16, i17, i23);
		}

		if(this.rand.nextInt(2) == 0) {
			i15 = i4 + this.rand.nextInt(16) + 8;
			i16 = this.rand.nextInt(128);
			i17 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, i15, i16, i17);
		}

		if(this.rand.nextInt(4) == 0) {
			i15 = i4 + this.rand.nextInt(16) + 8;
			i16 = this.rand.nextInt(128);
			i17 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, i15, i16, i17);
		}

		if(this.rand.nextInt(8) == 0) {
			i15 = i4 + this.rand.nextInt(16) + 8;
			i16 = this.rand.nextInt(128);
			i17 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, i15, i16, i17);
		}

		for(i15 = 0; i15 < 10; ++i15) {
			i16 = i4 + this.rand.nextInt(16) + 8;
			i17 = this.rand.nextInt(128);
			i23 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenReed()).generate(this.worldObj, this.rand, i16, i17, i23);
		}

		if(this.rand.nextInt(32) == 0) {
			i15 = i4 + this.rand.nextInt(16) + 8;
			i16 = this.rand.nextInt(128);
			i17 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenPumpkin()).generate(this.worldObj, this.rand, i15, i16, i17);
		}

		i15 = 0;
		if(biomeGenBase6 == BiomeGenBase.desert) {
			i15 += 10;
		}

		int i19;
		for(i16 = 0; i16 < i15; ++i16) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i23 = this.rand.nextInt(128);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenCactus()).generate(this.worldObj, this.rand, i17, i23, i19);
		}

		for(i16 = 0; i16 < 50; ++i16) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i23 = this.rand.nextInt(this.rand.nextInt(120) + 8);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, i17, i23, i19);
		}

		for(i16 = 0; i16 < 20; ++i16) {
			i17 = i4 + this.rand.nextInt(16) + 8;
			i23 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
			i19 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, i17, i23, i19);
		}

		this.generatedTemperatures = this.worldObj.getWorldChunkManager().getTemperatures(this.generatedTemperatures, i4 + 8, i5 + 8, 16, 16);

		for(i16 = i4 + 8; i16 < i4 + 8 + 16; ++i16) {
			for(i17 = i5 + 8; i17 < i5 + 8 + 16; ++i17) {
				i23 = i16 - (i4 + 8);
				i19 = i17 - (i5 + 8);
				int i20 = this.worldObj.findTopSolidBlockUsingBlockMaterial(i16, i17);
				double d21 = this.generatedTemperatures[i23 * 16 + i19] - (double)(i20 - 64) / 64.0D * 0.3D;
				if(d21 < 0.5D && i20 > 0 && i20 < 128 && this.worldObj.isAirBlock(i16, i20, i17) && this.worldObj.getBlockMaterial(i16, i20 - 1, i17).getIsSolid() && this.worldObj.getBlockMaterial(i16, i20 - 1, i17) != Material.ice) {
					this.worldObj.setBlockWithNotify(i16, i20, i17, Block.snow.blockID);
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

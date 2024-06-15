package net.minecraft.src;

import java.util.Random;

import com.mojang.minecraft.structure.MapGenMineshaft;

public class ChunkProviderGenerate implements IChunkProvider {
	protected Random rand;
	protected NoiseGeneratorOctaves noiseGen1;
	protected NoiseGeneratorOctaves noiseGen2;
	protected NoiseGeneratorOctaves noiseGen3;
	protected NoiseGeneratorOctaves noiseGenSandOrGravel;
	protected NoiseGeneratorOctaves noiseStone;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	public NoiseGeneratorOctavesIndev noiseIslandGen;
	protected World worldObj;
	public final boolean mapFeaturesEnabled;
	protected double[] terrainNoise;
	protected double[] sandNoise = new double[256];
	protected double[] gravelNoise = new double[256];
	protected double[] stoneNoise = new double[256];
	protected MapGenBase caveGenerator = new MapGenCaves();
	protected MapGenBase ravineGenerator = new MapGenRavine();
	protected MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
	protected BiomeGenBase[] biomesForGeneration;
	double[] noise3;
	double[] noise1;
	double[] noise2;
	double[] noise5;
	double[] noise6;
	int[][] unused = new int[32][32];
	public double[] generatedTemperatures;

	public ChunkProviderGenerate(World world1, long j2) {
		this(world1, j2, true);
	}

	public ChunkProviderGenerate(World world1, long j2, boolean z4) {
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
		this.noiseIslandGen = new NoiseGeneratorOctavesIndev(this.rand, 2);
		this.mapFeaturesEnabled = z4;
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
									byte liquidID = (byte)(biomes[biomeIndex].mainLiquid);
									
									if(liquidID == Block.waterStill.blockID) {
										if(temperature < 0.5D && yy >= seaLevel - 1) {
											blockID = Block.ice.blockID;
										} else {
											blockID = Block.waterStill.blockID;
										}
									} else {
										blockID = liquidID;
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
				
				int noiseIndex = x + z * 16;
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
									topBlock = (byte)biomeGen.mainLiquid; //Block.waterStill.blockID;
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

	public void terraform(int chunkX, int chunkZ, Chunk chunk, BiomeGenBase[] biomes) {
		// fade world height to the edges of map. This uses formulae not dissimilar to those found in indev,
		// albeit adapted to work with 3D perlin terrain divided in chunks! 
		
		byte[] blocks = chunk.blocks;
		
		BiomeGenBase biome;
		int biomeIndex = 0;
		
		int xx = chunkX << 4;
		for(int x = 0; x < 16; x ++) {
			double dx = Math.abs(((double)xx / (double)(WorldSize.width - 1) - 0.5D) * 2.0D);
			
			int zz = chunkZ << 4;
			for(int z = 0; z < 16; z ++) {
				biome = biomes[biomeIndex ++];
				
				double dz = Math.abs(((double)zz / (double)(WorldSize.length - 1) - 0.5D) * 2.0D);
				
				// Get a weighted 2D distance to the center of sorts. This is a cone centered on the whole map area
				double d = Math.sqrt(dx * dx + dz * dz) * 1.2D;
				
				// Get noise
				double noise = this.noiseIslandGen.generateNoise(xx * 0.05D, zz * 0.05D) / 4.0D + 1.0D;
				
				// Weird a bit with those values (noise and d) to get a nice fried agg shape
				double factor = Math.max(Math.min(d, noise), Math.min(dx, dz));
				
				if(factor > 1.0D) factor = 1.0D;
				if(factor < 0.0D) factor = 0.0D;
				
				// Curve a bit
				factor *= factor;
				
				// Land height map, that's what we are adjusting:
				int height = chunk.getLandSurfaceHeightValue(x, z);
				
				// Adjust by factor, centered at 64
				double normalizedHeight = (double)height - 64.0D;
				normalizedHeight = normalizedHeight * (1.0D - factor) - factor * 10.0D + 5.0D;
				
				// Deepen oceans
				if(normalizedHeight < 0.0D) {
					normalizedHeight -= normalizedHeight * normalizedHeight * 0.2D;
				}
				
				int newHeight = 64 + (int)normalizedHeight;
				
				// Erode / raise
				int columnIndex = x << 11 | z << 7;
				
				if(newHeight < height) {
					for(int y = newHeight + 1; y <= height; y ++) {
						blocks[columnIndex + y] =  y < 64 ? (byte)biome.mainLiquid : 0;
					}
				} else if(newHeight > height) {
					for(int y = height + 1; y <= newHeight; y ++) {
						blocks[columnIndex + y] = (byte)Block.stone.blockID;
					}
				}
								
				// write back height
				chunk.setLandSurfaceHeightValue(x, z, newHeight);
				
				zz ++;
			}
			xx ++;
		}
		// 
	}
	
	public Chunk prepareChunk(int i1, int i2) {
		return this.provideChunk(i1, i2);
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		if(chunkX < 0 || chunkX >= WorldSize.xChunks || chunkZ < 0 || chunkZ >= WorldSize.zChunks) {
			return new EmptyChunk(this.worldObj, new byte[32768], 0, 0);
		}
		
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		
		// Empty block array & new Chunk
		byte[] blockArray = new byte[32768];
		Chunk chunk = new Chunk(this.worldObj, blockArray, chunkX, chunkZ);

		// Calculate biomes & temperatures for this chunk
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
		double[] temperatures = this.generatedTemperatures = this.worldObj.getWorldChunkManager().temperature;
		
		// Cache biomes in chunk
		chunk.biomeGenCache = this.biomesForGeneration.clone();

		// Generate terrain for this chunk
		this.generateTerrain(chunkX, chunkZ, blockArray, this.biomesForGeneration, temperatures);

		// Calculate height maps for this chunk
		chunk.generateLandSurfaceHeightMap();
		
		// Terraform
		this.terraform(chunkX, chunkZ, chunk, this.biomesForGeneration);
		
		// Replace blocks
		this.replaceBlocksForBiome(chunkX, chunkZ, blockArray, this.biomesForGeneration);

		// Generate caves, ravines & mineshafts
		this.caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);

		if (this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);
		}
		
		this.ravineGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);

		// Calculate lights
		chunk.generateSkylightMap();

		// Done
		return chunk;
	}

	private double[] initializeNoiseField(double[] d1, int i2, int i3, int i4, int i5, int i6, int i7) {
	
		// The noise field makes Alpha and Beta terrain generate differently.
		
		// Alpha noise:
		
		if(d1 == null) {
			d1 = new double[i5 * i6 * i7];
		}

		double d8 = 684.412D;
		double d10 = 684.412D;
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, (double)i2, (double)i3, (double)i4, i5, 1, i7, 1.0D, 0.0D, 1.0D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, (double)i2, (double)i3, (double)i4, i5, 1, i7, 100.0D, 0.0D, 100.0D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8 / 80.0D, d10 / 160.0D, d8 / 80.0D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8, d10, d8);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8, d10, d8);
		int i12 = 0;
		int i13 = 0;

		for(int i14 = 0; i14 < i5; ++i14) {
			for(int i15 = 0; i15 < i7; ++i15) {
				double d16 = (this.noise5[i13] + 256.0D) / 512.0D;
				if(d16 > 1.0D) {
					d16 = 1.0D;
				}

				double d18 = 0.0D;
				double d20 = this.noise6[i13] / 8000.0D;
				if(d20 < 0.0D) {
					d20 = -d20;
				}

				d20 = d20 * 3.0D - 3.0D;
				if(d20 < 0.0D) {
					d20 /= 2.0D;
					if(d20 < -1.0D) {
						d20 = -1.0D;
					}

					d20 /= 1.4D;
					d20 /= 2.0D;
					d16 = 0.0D;
				} else {
					if(d20 > 1.0D) {
						d20 = 1.0D;
					}

					d20 /= 6.0D;
				}

				d16 += 0.5D;
				d20 = d20 * (double)i6 / 16.0D;
				double d22 = (double)i6 / 2.0D + d20 * 4.0D;
				++i13;

				for(int i24 = 0; i24 < i6; ++i24) {
					double d25 = 0.0D;
					double d27 = ((double)i24 - d22) * 12.0D / d16;
					if(d27 < 0.0D) {
						d27 *= 4.0D;
					}

					double d29 = this.noise1[i12] / 512.0D;
					double d31 = this.noise2[i12] / 512.0D;
					double d33 = (this.noise3[i12] / 10.0D + 1.0D) / 2.0D;
					if(d33 < 0.0D) {
						d25 = d29;
					} else if(d33 > 1.0D) {
						d25 = d31;
					} else {
						d25 = d29 + (d31 - d29) * d33;
					}

					d25 -= d27;
					double d35;
					if(i24 > i6 - 4) {
						d35 = (double)((float)(i24 - (i6 - 4)) / 3.0F);
						d25 = d25 * (1.0D - d35) + -10.0D * d35;
					}

					if((double)i24 < d18) {
						d35 = (d18 - (double)i24) / 4.0D;
						if(d35 < 0.0D) {
							d35 = 0.0D;
						}

						if(d35 > 1.0D) {
							d35 = 1.0D;
						}

						d25 = d25 * (1.0D - d35) + -10.0D * d35;
					}

					d1[i12] = d25;
					++i12;
				}
			}
		}

		return d1;
		
		// Beta noise: 
		/*
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
		*/
	}

	public boolean chunkExists(int i1, int i2) {
		return true;
	}

	public void populateOres(int x0, int z0, BiomeGenBase biomeGen) {
		int i, x, y, z;
		
		for(i = 0; i < biomeGen.coalLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.glowLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(48);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGlow.blockID, 6)).generate(this.worldObj, this.rand, x, y, z);
		}
				
		for(i = 0; i < biomeGen.ironLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(64);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.goldLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(biomeGen.goldLumpMaxHeight);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.redstoneLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(biomeGen.redstoneLumpMaxHeight);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.diamondLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(biomeGen.diamondLumpMaxHeight);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(this.worldObj, this.rand, x, y, z);
		}
	}

	public void generateMapFeatures(int chunkX, int chunkZ) {
		if (this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, chunkX, chunkZ, false);
		}
	}
	
	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
		BlockSand.fallInstantly = true;
		int x0 = chunkX * 16;
		int z0 = chunkZ * 16;
		
		// We need the chunk
		Chunk thisChunk = chunkProvider.provideChunk(chunkX, chunkZ);	
		
		// Find which biome
		BiomeGenBase biomeGen = thisChunk.getBiomeGenAt(8, 8);
				
		this.rand.setSeed(this.worldObj.getRandomSeed());
		long seed1 = this.rand.nextLong() / 2L * 2L + 1L;
		long seed2 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)chunkX * seed1 + (long)chunkZ * seed2 ^ this.worldObj.getRandomSeed());

		biomeGen.prePopulate(this.worldObj, this.rand, x0, z0);
		
		// Vanilla map features (mineshafts)
		this.generateMapFeatures(chunkX, chunkZ);
		
		int i, x, y, z;
		
		int maxDungeonHeight = 128;
		
		for(i = 0; i < biomeGen.dungeonAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(maxDungeonHeight);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenDungeons(biomeGen)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.clayAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenClay(32)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.dirtLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.dirt.blockID, 32)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.gravelLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.gravel.blockID, 32)).generate(this.worldObj, this.rand, x, y, z);
		}

		this.populateOres(x0, z0, biomeGen);

		double noiseScaler = 0.5D;
		int treeBaseAttempts = (int)((this.mobSpawnerNoise.generateNoiseOctaves((double)x0 * noiseScaler, (double)z0 * noiseScaler) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
		if(treeBaseAttempts < 0) {
			treeBaseAttempts = 0;
		}

		if(this.rand.nextInt(10) == 0) {
			++treeBaseAttempts;
		}
		
		treeBaseAttempts += biomeGen.treeBaseAttemptsModifier;

		for(i = 0; i < treeBaseAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			z = z0 + this.rand.nextInt(16) + 8;
			y = this.worldObj.getHeightValue(x, z);
			
			WorldGenerator treeGen = biomeGen.genTreeTryFirst(this.rand);
			
			if(treeGen == null || treeGen.generate(this.worldObj, this.rand, x, y, z) == false) {	
				if (this.rand.nextInt(10) < biomeGen.bigTreesEach10Trees) {
					treeGen = biomeGen.getBigTreeGen(this.rand);
				} else {
					treeGen = biomeGen.getTreeGen(this.rand);
				}
				
				if(treeGen != null) treeGen.generate(this.worldObj, this.rand, x, y, z);
			}
		}

		for(i = 0; i < biomeGen.yellowFlowersAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.redFlowersAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		if(this.rand.nextInt(biomeGen.mushroomBrownChance) == 0) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		if(this.rand.nextInt(biomeGen.mushroomRedChance) == 0) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.reedAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenReed()).generate(this.worldObj, this.rand, x, y, z);
		}
		
		if(biomeGen.pumpkinChance > 0 && this.rand.nextInt(biomeGen.pumpkinChance) == 0) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenPumpkin()).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.cactusAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenCactus()).generate(this.worldObj, this.rand, x, y, z);
		}
		
		// Generate tall grass
		for(i = 0; i < biomeGen.tallGrassAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(64) + 64;
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.tallGrass.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}
		
		// Generate dead bushes
		for(i = 0; i < biomeGen.deadBushAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			z = z0 + this.rand.nextInt(16) + 8;
			y = this.worldObj.getHeightValue(x, z);
			
			Block block = Block.blocksList[this.worldObj.getBlockId(x, y - 1, z)];
			if (block == Block.sand || block == Block.grass || block == Block.dirt) 
				this.worldObj.setBlock(x, y, z, Block.deadBush.blockID);
		}

		for(i = 0; i < biomeGen.waterFallAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(this.rand.nextInt(biomeGen.waterFallMaxHeight - biomeGen.waterFallMinHeight) + biomeGen.waterFallMinHeight);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.lavaAttempts; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		// Layered sand
		// 1st step = build float "blurred" height map
		float[][] floatHeightMap = new float[18][18];
		for (x = -1; x < 17; x ++) {
			for (z = -1; z < 17; z ++) {
				floatHeightMap[x + 1][z + 1] = (float)this.worldObj.getHeightValue(x0 + x, z0 + z);
			}
		}
		
		float[][] blurredHeightMap = new float[16][16];
		for (x = 0; x < 16; x ++) {
			for (z = 0; z < 16; z ++) {
				int xx0 = x + 1; 
				int zz0 = z + 1;
				float sum = 0.0F;
				for (int xx = xx0 - 1; xx <= xx0 + 1; xx ++) {
					for (int zz = zz0 - 1; zz <= zz0 + 1; zz ++) {
						sum += floatHeightMap[xx][zz];
					}
				}
				blurredHeightMap[x][z] = sum / 9.0F;
			}
		}

		// Now fill "decimal" part of height value over sand blocks with layered sand
		for (x = 0; x < 16; x ++) {
			for (z = 0; z < 16; z ++) {
				float f = blurredHeightMap[x][z];
				y = (int)f - 1;
				if(y >= 63 && this.worldObj.getBlockId(x0 + x, y, z0 + z) == Block.sand.blockID) {
					int yy = (int)f;
					int meta = (int)(16.0F * (f - Math.floor(f))) - 1;
					if (this.worldObj.getBlockId(x0 + x, yy + 1, z0 + z) == 0 && meta > 0) {
						this.worldObj.setBlockAndMetadata(x0 + x, yy , z0 + z, Block.layeredSand.blockID, meta);
					}
				}
			}
		}		

		// Biome based population
		biomeGen.populate(this.worldObj, this.rand, x0, z0);
		
		// Cover with snow:
		for(x = x0; x < x0 + 16; x ++) {
			for(z = z0; z < z0 + 16; z ++) {
				y = this.worldObj.findTopSolidBlockUsingBlockMaterial(x, z);
				Material material = this.worldObj.getBlockMaterial(x, y - 1, z);
				Block blockBeneath = Block.blocksList[this.worldObj.getBlockId(x, y - 1, z)];
				if(
					this.worldObj.getBiomeGenAt(x, z).weather == Weather.cold && 
					y > 0 && y < 128 && 
					this.worldObj.getBlockId(x, y, z) == 0 && 
					material.getIsSolid() && material != Material.ice && 
					material != Material.sand && 
					blockBeneath.isOpaqueCube()
				) {
					this.worldObj.setBlockAndMetadata(x, y, z, Block.snow.blockID, rand.nextInt(5) + 1);
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

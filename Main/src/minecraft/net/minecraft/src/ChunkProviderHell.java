package net.minecraft.src;

import java.util.Random;

public class ChunkProviderHell implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGenSandOrGravel;
	private NoiseGeneratorOctaves noiseStone;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	private World worldObj;
	private double[] terrainNoise;
	private double[] sandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCavesHell();
	
	double[] noise3;
	double[] noise1;
	double[] noise2;
	double[] noise5;
	double[] noise6;

	public ChunkProviderHell(World world1, long j2) {
		this.worldObj = world1;
		this.rand = new Random(j2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGenSandOrGravel = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseStone = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
	}
	
	public IChunkProvider getChunkProviderGenerate() {
		return this;
	}

	public void generateTerrain(int chunkX, int chunkZ, byte[] blocks) {
		double noiseScale = 0.125D;
		double scalingFactor = 0.25D;
		double densityVariationSpeed = 0.25D;
						
		byte quadrantSize = 4;
		byte lavaLevel = 32;
		int cellSize = quadrantSize + 1;
		byte columnSize = 17;
		int cellSize2 = quadrantSize + 1;
		short chunkHeight = 128;

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
								int blockID = 0;
								if(yy < lavaLevel) {
									blockID = Block.lavaStill.blockID;
								}

								if(density > 0.0D) {
									blockID = Block.bloodStone.blockID;
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

	public void replaceBlocksForBiome(int chunkX, int chunkZ, byte[] blocks) {
		byte lavaLevel = 64;
		double d5 = 8.0D / 256D;
		this.sandNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.sandNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, d5, d5, 1.0D);
		this.gravelNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.gravelNoise, (double)(chunkX * 16), 109.0134D, (double)(chunkZ * 16), 16, 1, 16, d5, 1.0D, d5);
		this.stoneNoise = this.noiseStone.generateNoiseOctaves(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, d5 * 2.0D, d5 * 2.0D, d5 * 2.0D);

		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				boolean generateSand = this.sandNoise[x + z * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean generateGravel = this.gravelNoise[x + z * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
				int i11 = (int)(this.stoneNoise[x + z * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int i12 = -1;
				byte topBlock = (byte)Block.bloodStone.blockID;
				byte fillerBlock = (byte)Block.bloodStone.blockID;

				for(int y = 127; y >= 0; --y) {
					int index = x << 11 | z << 7 | y; // (x * 16 + z) * 128 + y
					if(y >= 127 - this.rand.nextInt(5)) {
						blocks[index] = (byte)Block.bedrock.blockID;
					} else if(y <= 0 + this.rand.nextInt(5)) {
						blocks[index] = (byte)Block.bedrock.blockID;
					} else {
						byte blockID = blocks[index];
						if(blockID == 0) {
							i12 = -1;
						} else if(blockID == Block.bloodStone.blockID) {
							if(i12 == -1) {
								if(i11 <= 0) {
									topBlock = 0;
									fillerBlock = (byte)Block.bloodStone.blockID;
								} else if(y >= lavaLevel - 4 && y <= lavaLevel + 1) {
									topBlock = (byte)Block.bloodStone.blockID;
									fillerBlock = (byte)Block.bloodStone.blockID;
									if(generateGravel) {
										topBlock = (byte)Block.gravel.blockID;
										fillerBlock = (byte)Block.bloodStone.blockID;
									}

									if(generateSand) {
										topBlock = (byte)Block.slowSand.blockID;
										fillerBlock = (byte)Block.slowSand.blockID;
									}
								}

								if(y < lavaLevel && topBlock == 0) {
									topBlock = (byte)Block.lavaStill.blockID;
								}

								i12 = i11;
								if(y >= lavaLevel - 1) {
									blocks[index] = topBlock;
								} else {
									blocks[index] = fillerBlock;
								}
							} else if(i12 > 0) {
								--i12;
								blocks[index] = fillerBlock;
							}
						}
					}
				}
			}
		}

	}

	public void terraform(int chunkX, int chunkZ, Chunk chunk, BiomeGenBase[] biomes) {
		// TODO::Add nether walls
	}

	public Chunk prepareChunk(int i1, int i2) {
		return this.provideChunk(i1, i2);
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		
		// Empty block array & new Chunk
		byte[] blockArray = new byte[32768];
		byte[] metadata = new byte[32768];
		Chunk chunk = new Chunk(this.worldObj, blockArray, metadata, chunkX, chunkZ);

		// Generate terrain for this chunk
		this.generateTerrain(chunkX, chunkZ, blockArray);
		
		// Replace blocks
		this.replaceBlocksForBiome(chunkX, chunkZ, blockArray);

		// Generate caves
		this.caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);

		// Done
		return chunk;	
	}
	
	public Chunk justGenerateForHeight(int chunkX, int chunkZ) {
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		
		// Empty block array & new Chunk
		byte[] blockArray = new byte[32768];
		byte[] metadata = new byte[32768];
		Chunk chunk = new Chunk(this.worldObj, blockArray, metadata, chunkX, chunkZ);
		this.generateTerrain(chunkX, chunkZ, blockArray);
		this.caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);
		
		return chunk;
	}

	private double[] initializeNoiseField(double[] d1, int i2, int i3, int i4, int i5, int i6, int i7) {
		if(d1 == null) {
			d1 = new double[i5 * i6 * i7];
		}

		double d8 = 684.412D;
		double d10 = 2053.236D;
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, (double)i2, (double)i3, (double)i4, i5, 1, i7, 1.0D, 0.0D, 1.0D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, (double)i2, (double)i3, (double)i4, i5, 1, i7, 100.0D, 0.0D, 100.0D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8 / 80.0D, d10 / 60.0D, d8 / 80.0D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8, d10, d8);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, (double)i2, (double)i3, (double)i4, i5, i6, i7, d8, d10, d8);
		int i12 = 0;
		int i13 = 0;
		double[] d14 = new double[i6];

		int i15;
		for(i15 = 0; i15 < i6; ++i15) {
			d14[i15] = Math.cos((double)i15 * Math.PI * 6.0D / (double)i6) * 2.0D;
			double d16 = (double)i15;
			if(i15 > i6 / 2) {
				d16 = (double)(i6 - 1 - i15);
			}

			if(d16 < 4.0D) {
				d16 = 4.0D - d16;
				d14[i15] -= d16 * d16 * d16 * 10.0D;
			}
		}

		for(i15 = 0; i15 < i5; ++i15) {
			for(int i36 = 0; i36 < i7; ++i36) {
				double d17 = (this.noise5[i13] + 256.0D) / 512.0D;
				if(d17 > 1.0D) {
					d17 = 1.0D;
				}

				double d19 = 0.0D;
				double d21 = this.noise6[i13] / 8000.0D;
				if(d21 < 0.0D) {
					d21 = -d21;
				}

				d21 = d21 * 3.0D - 3.0D;
				if(d21 < 0.0D) {
					d21 /= 2.0D;
					if(d21 < -1.0D) {
						d21 = -1.0D;
					}

					d21 /= 1.4D;
					d21 /= 2.0D;
					d17 = 0.0D;
				} else {
					if(d21 > 1.0D) {
						d21 = 1.0D;
					}

					d21 /= 6.0D;
				}

				d17 += 0.5D;
				d21 = d21 * (double)i6 / 16.0D;
				++i13;

				for(int i23 = 0; i23 < i6; ++i23) {
					double d24 = 0.0D;
					double d26 = d14[i23];
					double d28 = this.noise1[i12] / 512.0D;
					double d30 = this.noise2[i12] / 512.0D;
					double d32 = (this.noise3[i12] / 10.0D + 1.0D) / 2.0D;
					if(d32 < 0.0D) {
						d24 = d28;
					} else if(d32 > 1.0D) {
						d24 = d30;
					} else {
						d24 = d28 + (d30 - d28) * d32;
					}

					d24 -= d26;
					double d34;
					if(i23 > i6 - 4) {
						d34 = (double)((float)(i23 - (i6 - 4)) / 3.0F);
						d24 = d24 * (1.0D - d34) + -10.0D * d34;
					}

					if((double)i23 < d19) {
						d34 = (d19 - (double)i23) / 4.0D;
						if(d34 < 0.0D) {
							d34 = 0.0D;
						}

						if(d34 > 1.0D) {
							d34 = 1.0D;
						}

						d24 = d24 * (1.0D - d34) + -10.0D * d34;
					}

					d1[i12] = d24;
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

		int i6;
		int i7;
		int i8;
		int i9;
		for(i6 = 0; i6 < 8; ++i6) {
			i7 = i4 + this.rand.nextInt(16) + 8;
			i8 = this.rand.nextInt(120) + 4;
			i9 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenHellLava(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, i7, i8, i9);
		}

		i6 = this.rand.nextInt(this.rand.nextInt(10) + 1) + 1;

		int i10;
		for(i7 = 0; i7 < i6; ++i7) {
			i8 = i4 + this.rand.nextInt(16) + 8;
			i9 = this.rand.nextInt(120) + 4;
			i10 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFire()).generate(this.worldObj, this.rand, i8, i9, i10);
		}

		i6 = this.rand.nextInt(this.rand.nextInt(10) + 1);

		for(i7 = 0; i7 < i6; ++i7) {
			i8 = i4 + this.rand.nextInt(16) + 8;
			i9 = this.rand.nextInt(120) + 4;
			i10 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenGlowStone1()).generate(this.worldObj, this.rand, i8, i9, i10);
		}

		for(i7 = 0; i7 < 10; ++i7) {
			i8 = i4 + this.rand.nextInt(16) + 8;
			i9 = this.rand.nextInt(128);
			i10 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenGlowStone2()).generate(this.worldObj, this.rand, i8, i9, i10);
		}

		if(this.rand.nextInt(1) == 0) {
			i7 = i4 + this.rand.nextInt(16) + 8;
			i8 = this.rand.nextInt(128);
			i9 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, i7, i8, i9);
		}

		if(this.rand.nextInt(1) == 0) {
			i7 = i4 + this.rand.nextInt(16) + 8;
			i8 = this.rand.nextInt(128);
			i9 = i5 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, i7, i8, i9);
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
		return "HellRandomLevelSource";
	}
	
	public Chunk makeBlank(World world) {
		Chunk chunk = new Chunk(world, new byte[32768], new byte[32768], 0, 0);
		chunk.neverSave = true;
		
		// Fill with fancy bedrock
		for(int x = 0; x < 16; x ++) {
			for(int z = 0; z < 16; z ++) {
				int index = x << 11 | z << 7;
				for(int y = 0; y < 128; y ++) {
					byte blockID = (byte)(((x < 3 || x > 12 || z < 3 || z > 12) && rand.nextBoolean()) ? 0 : Block.bedrock.blockID);
					chunk.blocks[index ++] =  blockID;
				}
			}
		}
		
		chunk.isTerrainPopulated = true;
		
		return chunk;
	}
}

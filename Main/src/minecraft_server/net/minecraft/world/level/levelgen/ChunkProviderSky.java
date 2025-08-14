package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.EmptyChunk;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;
import net.minecraft.world.level.levelgen.feature.WorldGenMinable;
import net.minecraft.world.level.tile.Block;

public class ChunkProviderSky extends ChunkProviderGenerate implements IChunkProvider {
	
	public ChunkProviderSky(World world1, long j2) {
		this(world1, j2, true);
	}
	
	public ChunkProviderSky(World world1, long j2, boolean b) {
		super(world1, j2, b);
	}

	public void generateTerrain(int chunkX, int chunkZ, byte[] blocks) {
		double noiseScale = 0.25D;
		double yscalingFactor = 0.125D;
		double densityVariationSpeed = 0.125D;
		
		byte quadrantSize = 2;
		
		int cellSize = quadrantSize + 1;
		byte columnSize = 33;
		int cellSize2 = quadrantSize + 1;
		short chunkHeight = 128;
		
		this.terrainNoise = this.initializeNoiseField(this.terrainNoise, chunkX * quadrantSize, 0, chunkZ * quadrantSize, cellSize, columnSize, cellSize2, chunkX, chunkZ);

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

	public void replaceBlocksForBiome(int chunkX, int chunkZ, byte[] blocks, byte[] metadata, BiomeGenBase[] biomes) {
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
				byte topBlock = biomeGen.getTopBlock(this.rand);
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

	public double extend_center(double d) {
		// Assumes d = 0.0 -> 1.0.
		if(d < 0.25D) return d * 2;
		if(d >= 0.75D) return (d - 0.75D) * 2 + 0.5D;
		return 0.5D;
	}
	
	public void terraform(int chunkX, int chunkZ, Chunk chunk, BiomeGenBase[] biomes) {
		// fade world height to the edges of map. This uses formulae not dissimilar to those found in indev,
		// albeit adapted to work with 3D terrain divided in chunks! 
		
		byte[] blocks = chunk.blocks;
		
		int xx = chunkX << 4;
		for(int x = 0; x < 16; x ++) {
			double dx = Math.abs(( extend_center((double)xx / (double)(WorldSize.width - 1)) - 0.5D) * 2.0D);
			
			int zz = chunkZ << 4;
			for(int z = 0; z < 16; z ++) {
				double dz = Math.abs(( extend_center ((double)zz / (double)(WorldSize.length - 1)) - 0.5D) * 2.0D);
				
				// Get a weighted 2D distance to the center of sorts. This is a cone centered on the whole map area
				double d = Math.sqrt(dx * dx + dz * dz) * 1.2D;
				
				// Get noise
				double noise = this.noiseIslandGen.generateNoise(xx * 0.05D, zz * 0.05D) / 4.0D + 1.0D;
				
				// Weird a bit with those values (noise and d) to get a nice fried egg shape
				double factor = Math.max(Math.min(d, noise), Math.min(dx, dz));
				
				if(factor > 1.0D) factor = 1.0D;
				if(factor < 0.0D) factor = 0.0D;
				
				// Curve a bit
				factor *= factor;
			
				//factor *= 2;				
				
				// Land height map, that's what we are adjusting:
				int height = chunk.getLandSurfaceHeightValue(x, z);
				if(height > 1) {	
					// Adjust by factor
					double normalizedHeight = (double)height;
					normalizedHeight = normalizedHeight * (1.0D - factor) - factor * 10.0D + 5.0D;
					
					// Deepen oceans
					if(normalizedHeight < 0.0D) {
						normalizedHeight -= normalizedHeight * normalizedHeight * 0.2D;
					}
					
					int newHeight = (int)normalizedHeight;
					if(newHeight < 0) newHeight = 0;
					if(newHeight > 127) newHeight = 127;
					
					// Erode / raise
					int columnIndex = x << 11 | z << 7;
					
					if(newHeight < height) {
						for(int y = newHeight + 1; y <= height; y ++) {
							blocks[columnIndex + y] = 0;
						}
					} else if(newHeight > height) {
						for(int y = height + 1; y <= newHeight; y ++) {
							blocks[columnIndex + y] = (byte)Block.stone.blockID;
						}
					}
									
					// write back height
					chunk.setLandSurfaceHeightValue(x, z, newHeight);
				}
				
				zz ++;
			}
			xx ++;
		}
		// 
	}
	
	public Chunk provideChunk(int chunkX, int chunkZ) {
		if(chunkX < 0 || chunkX >= WorldSize.xChunks || chunkZ < 0 || chunkZ >= WorldSize.zChunks) {
			return new EmptyChunk(this.worldObj, new byte[32768], new byte[32768], 0, 0);
		}
		
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		
		// Empty block array & new Chunk
		byte[] blockArray = new byte[32768];
		byte[] metadata = new byte[32768];
		Chunk chunk = new Chunk(this.worldObj, blockArray, metadata, chunkX, chunkZ);

		// Calculate biomes & temperatures for this chunk
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
		
		// Cache biomes in chunk
		chunk.biomeGenCache = this.biomesForGeneration.clone();

		// Generate terrain for this chunk
		this.generateTerrain(chunkX, chunkZ, blockArray);

		// Calculate height maps for this chunk
		chunk.generateLandSurfaceHeightMap();
		
		// Terraform
		this.terraform(chunkX, chunkZ, chunk, this.biomesForGeneration);

		// Features system
		if (this.mapFeaturesEnabled) {
			this.featureProvider.getNearestFeatures(chunkX, chunkZ, chunk);
		} 
		
		// Replace blocks
		this.replaceBlocksForBiome(chunkX, chunkZ, blockArray, metadata, this.biomesForGeneration);

		// Caves
		this.caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);
		
		// Mineshafts
		if (this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);
			this.strongholdGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);
		}		

		// Calculate lights
		chunk.generateHeightMap();
		chunk.generateSkylightMap();

		// Done
		return chunk;
	}
	
	public Chunk prepareChunk(int i1, int i2) {
		return this.provideChunk(i1, i2);
	}

	private double[] initializeNoiseField(double[] densityMapArray, int x, int y, int z, int xSize, int ySize, int zSize, int chunkX, int chunkZ) {
		if(densityMapArray == null) {
			densityMapArray = new double[xSize * ySize * zSize];
		}

		double d8 = 684.412D;
		double densityMapArray0 = 684.412D;
		double[] densityMapArray2 = this.worldObj.getWorldChunkManager().temperature;
		double[] densityMapArray3 = this.worldObj.getWorldChunkManager().humidity;
		this.scaleArray = this.scaleNoise.generateNoiseOctaves(this.scaleArray, x, z, xSize, zSize, 1.121D, 1.121D, 0.5D);
		this.depthArray = this.depthNoise.generateNoiseOctaves(this.depthArray, x, z, xSize, zSize, 200.0D, 200.0D, 0.5D);
		d8 *= 2.0D; 	// This makes the difference between overworld & sky dimension
		this.mainArray = this.mainNoise.generateNoiseOctaves(this.mainArray, (double)x, (double)y, (double)z, xSize, ySize, zSize, d8 / 80.0D, densityMapArray0 / 160.0D, d8 / 80.0D);
		this.minLimitArray = this.minLimitNoise.generateNoiseOctaves(this.minLimitArray, (double)x, (double)y, (double)z, xSize, ySize, zSize, d8, densityMapArray0, d8);
		this.maxLimitArray = this.maxLimitNoise.generateNoiseOctaves(this.maxLimitArray, (double)x, (double)y, (double)z, xSize, ySize, zSize, d8, densityMapArray0, d8);
		int i14 = 0;
		int i15 = 0;
		int i16 = 16 / xSize;

		for(int i17 = 0; i17 < xSize; ++i17) {
			int i18 = i17 * i16 + i16 / 2;

			for(int i19 = 0; i19 < zSize; ++i19) {
				int x0 = i19 * i16 + i16 / 2;
				double d21 = densityMapArray2[i18 * 16 + x0];
				double d23 = densityMapArray3[i18 * 16 + x0] * d21;
				double d25 = 1.0D - d23;
				d25 *= d25;
				d25 *= d25;
				d25 = 1.0D - d25;
				double d27 = (this.scaleArray[i15] + 256.0D) / 512.0D;
				d27 *= d25;
				if(d27 > 1.0D) {
					d27 = 1.0D;
				}

				double d29 = this.depthArray[i15] / 8000.0D;
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
				d29 = d29 * (double)ySize / 16.0D;
				++i15;
				double d31 = (double)ySize / 2.0D;

				for(int y3 = 0; y3 < ySize; ++y3) {
					double d34 = 0.0D;
					double d36 = ((double)y3 - d31) * 8.0D / d27;
					if(d36 < 0.0D) {
						d36 *= -1.0D;
					}

					double d38 = this.minLimitArray[i14] / 512.0D;
					double d40 = this.maxLimitArray[i14] / 512.0D;
					double d42 = (this.mainArray[i14] / 10.0D + 1.0D) / 2.0D;
					if(d42 < 0.0D) {
						d34 = d38;
					} else if(d42 > 1.0D) {
						d34 = d40;
					} else {
						d34 = d38 + (d40 - d38) * d42;
					}

					d34 -= 7.0D; // WAS 8!
					byte b44 = 32;
					double d45;
					if(y3 > ySize - b44) {
						d45 = (double)((float)(y3 - (ySize - b44)) / ((float)b44 - 1.0F));
						d34 = d34 * (1.0D - d45) + -30.0D * d45;
					}

					b44 = 8;
					if(y3 < b44) {
						d45 = (double)((float)(b44 - y3) / ((float)b44 - 1.0F));
						d34 = d34 * (1.0D - d45) + -30.0D * d45;
					}

					densityMapArray[i14] = d34;
					++i14;
				}
			}
		}

		return densityMapArray;
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
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGlow.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}
				
		for(i = 0; i < 2*biomeGen.ironLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < 4*biomeGen.goldLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < 6*biomeGen.redstoneLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < 6*biomeGen.diamondLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(this.worldObj, this.rand, x, y, z);
		}
	}
	
	public void generateMapFeatures(int chunkX, int chunkZ) {
		if (this.mapFeaturesEnabled) {
			this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, chunkX, chunkZ, true);
		}
	}

	public boolean chunkExists(int i1, int i2) {
		return true;
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
	
	@Override
	protected MapGenBase getCaveGenerator() {
		return new MapGenCavesSky();
	}

}

package net.minecraft.world.level.levelgen;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldChunkManager;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;
import net.minecraft.world.level.levelgen.feature.WorldGenFire;
import net.minecraft.world.level.levelgen.feature.WorldGenFlowers;
import net.minecraft.world.level.levelgen.feature.WorldGenGlowStone1;
import net.minecraft.world.level.levelgen.feature.WorldGenGlowStone2;
import net.minecraft.world.level.levelgen.feature.WorldGenHellLava;
import net.minecraft.world.level.levelgen.feature.WorldGenNetherMinable;
import net.minecraft.world.level.levelgen.synth.NoiseGeneratorOctaves;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockSand;

public class ChunkProviderHell implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves minLimitNoise;
	private NoiseGeneratorOctaves maxLimitNoise;
	private NoiseGeneratorOctaves mainNoise;
	private NoiseGeneratorOctaves noiseGenSandOrGravel;
	private NoiseGeneratorOctaves noiseStone;
	public NoiseGeneratorOctaves scaleNoise;
	public NoiseGeneratorOctaves depthNoise;
	private World worldObj;
	private double[] terrainNoise;
	private double[] sandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCavesHell();
	
	double[] mainArray;
	double[] minLimitArray;
	double[] maxLimitArray;
	double[] scaleArray;
	double[] depthArray;
	private BiomeGenBase[] biomesForGeneration;

	public ChunkProviderHell(World world, long seed) {
		this.worldObj = world;
		this.rand = new Random(seed);
		this.minLimitNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.maxLimitNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.mainNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGenSandOrGravel = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseStone = new NoiseGeneratorOctaves(this.rand, 4);
		this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
		this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
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
		int xSize = quadrantSize + 1;
		byte ySize = 17;
		int zSize = quadrantSize + 1;
		short chunkHeight = 128;

		this.terrainNoise = this.initializeNoiseField(this.terrainNoise, chunkX * quadrantSize, 0, chunkZ * quadrantSize, xSize, ySize, zSize);

		for(int xSection = 0; xSection < quadrantSize; ++xSection) {
			for(int zSection = 0; zSection < quadrantSize; ++zSection) {
				for(int ySection = 0; ySection < 16; ++ySection) {
					
					double densityMinXMinYMinZ = this.terrainNoise[((xSection + 0) * zSize + zSection + 0) * ySize + ySection + 0];
					double densityMinXMinYMaxZ = this.terrainNoise[((xSection + 0) * zSize + zSection + 1) * ySize + ySection + 0];
					double densityMaxXMinYMinZ = this.terrainNoise[((xSection + 1) * zSize + zSection + 0) * ySize + ySection + 0];
					double densityMaxXMinYMaxZ = this.terrainNoise[((xSection + 1) * zSize + zSection + 1) * ySize + ySection + 0];
					double yLerpAmountMinXMinZ = (this.terrainNoise[((xSection + 0) * zSize + zSection + 0) * ySize + ySection + 1] - densityMinXMinYMinZ) * noiseScale;
					double yLerpAmountMinXMaxZ = (this.terrainNoise[((xSection + 0) * zSize + zSection + 1) * ySize + ySection + 1] - densityMinXMinYMaxZ) * noiseScale;
					double yLerpAmountMaxXMinZ = (this.terrainNoise[((xSection + 1) * zSize + zSection + 0) * ySize + ySection + 1] - densityMaxXMinYMinZ) * noiseScale;
					double yLerpAmountMaxXMaxZ = (this.terrainNoise[((xSection + 1) * zSize + zSection + 1) * ySize + ySection + 1] - densityMaxXMinYMaxZ) * noiseScale;

					for(int y = 0; y < 8; ++y) {
						double curDensityMinXMinYMinZ = densityMinXMinYMinZ;
						double curDensityMinXMinYMaxZ = densityMinXMinYMaxZ;
						double xLerpAmountMinZ = (densityMaxXMinYMinZ - densityMinXMinYMinZ) * scalingFactor;
						double xLerpAmountMaxZ = (densityMaxXMinYMaxZ - densityMinXMinYMaxZ) * scalingFactor;

						int yy = ySection * 8 + y;

						for(int x = 0; x < 4; ++x) {
							int indexInBlockArray = (x + (xSection << 2)) << 11 | (0 + (zSection << 2)) << 7 | (ySection << 3) + y;
							
							double density = curDensityMinXMinYMinZ;
							double densityIncrement = (curDensityMinXMinYMaxZ - curDensityMinXMinYMinZ) * densityVariationSpeed;

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

							curDensityMinXMinYMinZ += xLerpAmountMinZ;
							curDensityMinXMinYMaxZ += xLerpAmountMaxZ;
						}

						densityMinXMinYMinZ += yLerpAmountMinXMinZ;
						densityMinXMinYMaxZ += yLerpAmountMinXMaxZ;
						densityMaxXMinYMinZ += yLerpAmountMaxXMinZ;
						densityMaxXMinYMaxZ += yLerpAmountMaxXMaxZ;
					}
				}
			}
		}

	}

	public void replaceBlocksForBiome(int chunkX, int chunkZ, byte[] blocks) {
		byte lavaSeaLevel = 64;
		double noiseScale = 8.0D / 256D;
		this.sandNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.sandNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, noiseScale, noiseScale, 1.0D);
		this.gravelNoise = this.noiseGenSandOrGravel.generateNoiseOctaves(this.gravelNoise, (double)(chunkX * 16), 109.0134D, (double)(chunkZ * 16), 16, 1, 16, noiseScale, 1.0D, noiseScale);
		this.stoneNoise = this.noiseStone.generateNoiseOctaves(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0D, 16, 16, 1, noiseScale * 2.0D, noiseScale * 2.0D, noiseScale * 2.0D);

		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				boolean generateSand = this.sandNoise[x + z * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean generateGravel = this.gravelNoise[x + z * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
				int soilDepth = (int)(this.stoneNoise[x + z * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int soilDepthRemaining = -1;
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
							soilDepthRemaining = -1;
						} else if(blockID == Block.bloodStone.blockID) {
							if(soilDepthRemaining == -1) {
								if(soilDepth <= 0) {
									topBlock = 0;
									fillerBlock = (byte)Block.bloodStone.blockID;
								} else if(y >= lavaSeaLevel - 4 && y <= lavaSeaLevel + 1) {
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

								if(y < lavaSeaLevel && topBlock == 0) {
									topBlock = (byte)Block.lavaStill.blockID;
								}

								soilDepthRemaining = soilDepth;
								if(y >= lavaSeaLevel - 1) {
									blocks[index] = topBlock;
								} else {
									blocks[index] = fillerBlock;
								}
							} else if(soilDepthRemaining > 0) {
								--soilDepthRemaining;
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

	public Chunk prepareChunk(int chunkX, int chunkZ) {
		return this.provideChunk(chunkX, chunkZ);
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
		
		// Empty block array & new Chunk
		byte[] blockArray = new byte[32768];
		byte[] metadata = new byte[32768];
		
		Chunk chunk = new Chunk(this.worldObj, blockArray, metadata, chunkX, chunkZ);
		
		if(WorldSize.inRange(this, chunkX, chunkZ)) {
			// Calculate biomes & temperatures for this chunk
			this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
			
			// Cache biomes in chunk
			chunk.biomeGenCache = this.biomesForGeneration.clone();
			
			// Capture the post-processed temperature, humidity and biome codes for this
			// chunk right now (see ChunkProviderGenerate.provideChunk for details).
			WorldChunkManager worldChunkManager = this.worldObj.getWorldChunkManager();
			chunk.temperatureCache = new float[256];
			chunk.humidityCache = new float[256];
			chunk.biomeIdCache = new byte[256];
			for(int cacheIndex = 0; cacheIndex < 256; cacheIndex ++) {
				chunk.temperatureCache[cacheIndex] = (float)worldChunkManager.temperatureScratch[cacheIndex];
				chunk.humidityCache[cacheIndex] = (float)worldChunkManager.humidityScratch[cacheIndex];
				chunk.biomeIdCache[cacheIndex] = (byte)this.biomesForGeneration[cacheIndex].biomeCode;
			}
			
			// Generate terrain for this chunk
			this.generateTerrain(chunkX, chunkZ, blockArray);
			
			// Replace blocks
			this.replaceBlocksForBiome(chunkX, chunkZ, blockArray);
	
			// Generate caves
			this.caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, blockArray);
		} else {
			// Fill with fancy bedrock
			for(int x = 0; x < 16; x ++) {
				for(int z = 0; z < 16; z ++) {
					int index = x << 11 | z << 7;
					for(int y = 0; y < 128; y ++) {
						byte blockID = (byte)(((x < 3 || x > 12 || z < 3 || z > 12) && rand.nextBoolean()) ? 0 : Block.bedrock.blockID);
						blockArray[index ++] =  blockID;
					}
				}
			}
			
			chunk.isTerrainPopulated = true;
		}

		// Done
		return chunk;	
	}
	
	public Chunk justGenerateForHeight(int chunkX, int chunkZ) {
		// The nether has no surface height to answer for: the old height pass left the land
		// surface height map zeroed (no generateLandSurfaceHeightMap call), so a blank chunk
		// is behaviorally identical and drops the wasteful full-dense 2 x 32 KB terrain scan.
		return new Chunk(this.worldObj, chunkX, chunkZ);
	}

	private double[] initializeNoiseField(double[] densityMapArray, int x, int y, int z, int xSize, int ySize, int zSize) {
		if(densityMapArray == null) {
			densityMapArray = new double[xSize * ySize * zSize];
		}

		double scaleXZ = 684.412D;
		double scaleY = 2053.236D;
		this.scaleArray = this.scaleNoise.generateNoiseOctaves(this.scaleArray, (double)x, (double)y, (double)z, xSize, 1, zSize, 1.0D, 0.0D, 1.0D);
		this.depthArray = this.depthNoise.generateNoiseOctaves(this.depthArray, (double)x, (double)y, (double)z, xSize, 1, zSize, 100.0D, 0.0D, 100.0D);
		this.mainArray = this.mainNoise.generateNoiseOctaves(this.mainArray, (double)x, (double)y, (double)z, xSize, ySize, zSize, scaleXZ / 80.0D, scaleY / 60.0D, scaleXZ / 80.0D);
		this.minLimitArray = this.minLimitNoise.generateNoiseOctaves(this.minLimitArray, (double)x, (double)y, (double)z, xSize, ySize, zSize, scaleXZ, scaleY, scaleXZ);
		this.maxLimitArray = this.maxLimitNoise.generateNoiseOctaves(this.maxLimitArray, (double)x, (double)y, (double)z, xSize, ySize, zSize, scaleXZ, scaleY, scaleXZ);
		int mainIndex = 0;
		int depthScaleIndex = 0;
		double[] heightCurve = new double[ySize];

		for(int curveIndex = 0; curveIndex < ySize; ++curveIndex) {
			heightCurve[curveIndex] = Math.cos((double)curveIndex * Math.PI * 6.0D / (double)ySize) * 2.0D;
			double edgeDistance = (double)curveIndex;
			if(curveIndex > ySize / 2) {
				edgeDistance = (double)(ySize - 1 - curveIndex);
			}

			if(edgeDistance < 4.0D) {
				edgeDistance = 4.0D - edgeDistance;
				heightCurve[curveIndex] -= edgeDistance * edgeDistance * edgeDistance * 10.0D;
			}
		}

		for(int dx = 0; dx < xSize; ++dx) {
			for(int dz = 0; dz < zSize; ++dz) {
				double scale = (this.scaleArray[depthScaleIndex] + 256.0D) / 512.0D;
				if(scale > 1.0D) {
					scale = 1.0D;
				}

				double offsetY = 0.0D;
				double depth = this.depthArray[depthScaleIndex] / 8000.0D;
				if(depth < 0.0D) {
					depth = -depth;
				}

				depth = depth * 3.0D - 3.0D;
				if(depth < 0.0D) {
					depth /= 2.0D;
					if(depth < -1.0D) {
						depth = -1.0D;
					}

					depth /= 1.4D;
					depth /= 2.0D;
					scale = 0.0D;
				} else {
					if(depth > 1.0D) {
						depth = 1.0D;
					}

					depth /= 6.0D;
				}

				scale += 0.5D;
				depth = depth * (double)ySize / 16.0D;
				++depthScaleIndex;

				for(int dy = 0; dy < ySize; ++dy) {
					double density = 0.0D;
					double curveValue = heightCurve[dy];
					double minDensity = this.minLimitArray[mainIndex] / 512.0D;
					double maxDensity = this.maxLimitArray[mainIndex] / 512.0D;
					double mainDensity = (this.mainArray[mainIndex] / 10.0D + 1.0D) / 2.0D;
					if(mainDensity < 0.0D) {
						density = minDensity;
					} else if(mainDensity > 1.0D) {
						density = maxDensity;
					} else {
						density = minDensity + (maxDensity - minDensity) * mainDensity;
					}

					density -= curveValue;
					double lerpFactor;
					if(dy > ySize - 4) {
						lerpFactor = (double)((float)(dy - (ySize - 4)) / 3.0F);
						density = density * (1.0D - lerpFactor) + -10.0D * lerpFactor;
					}

					if((double)dy < offsetY) {
						lerpFactor = (offsetY - (double)dy) / 4.0D;
						if(lerpFactor < 0.0D) {
							lerpFactor = 0.0D;
						}

						if(lerpFactor > 1.0D) {
							lerpFactor = 1.0D;
						}

						density = density * (1.0D - lerpFactor) + -10.0D * lerpFactor;
					}

					densityMapArray[mainIndex] = density;
					++mainIndex;
				}
			}
		}

		return densityMapArray;
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		return true;
	}

	public void populateOres(int x0, int z0, BiomeGenBase biomeGen) {
		int i, x, y, z;
		
		for(i = 0; i < 128; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenNetherMinable(Block.oreQuartz.blockID, 16)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < 32; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenNetherMinable(Block.oreNetherGold.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < 8; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenNetherMinable(Block.oreNetherDiamond.blockID, 7)).generate(this.worldObj, this.rand, x, y, z);
		}
	}

	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
		BlockSand.fallInstantly = true;
		int x0 = chunkX * 16;
		int z0 = chunkZ * 16;

		// We need the chunk
		Chunk thisChunk = chunkProvider.provideChunk(chunkX, chunkZ);	
		if(thisChunk.beingDecorated) {
			System.out.println ("Being decorated " + chunkX + " " + chunkZ);
			return;
		}
		thisChunk.beingDecorated = true;
		
		BiomeGenBase biomeGen = thisChunk.getBiomeGenAt(8, 8);
		
		this.rand.setSeed(this.worldObj.getRandomSeed());
		long seed1 = this.rand.nextLong() / 2L * 2L + 1L;
		long seed2 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)chunkX * seed1 + (long)chunkZ * seed2 ^ this.worldObj.getRandomSeed());
		
		biomeGen.prePopulate(this.worldObj, this.rand, x0, z0);
		
		int i, x, y, z;

		for(i = 0; i < 8 + biomeGen.hellLavaExtraAttemtps; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(120) + 4;
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenHellLava(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		int fireAttempts = biomeGen.hellFireExtraAttempts + this.rand.nextInt(this.rand.nextInt(10) + 1) + 1;

		for(i = 0; i < fireAttempts*2; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(120) + 4;
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFire()).generate(this.worldObj, this.rand, x, y, z);
		}

		i = biomeGen.hellGlowstoneExtraAttempts + this.rand.nextInt(this.rand.nextInt(10) + 1);

		for(i = 0; i < i; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(120) + 4;
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenGlowStone1()).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < 10; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenGlowStone2()).generate(this.worldObj, this.rand, x, y, z);
		}

		if(this.rand.nextInt(1) == 0) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}

		if(this.rand.nextInt(1) == 0) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, x, y, z);
		}
		
		this.populateOres(x0, z0, biomeGen);

		// Biome based population
		biomeGen.populate(this.worldObj, this.rand, x0, z0);
		
		BlockSand.fallInstantly = false;
	}

	public boolean saveChunks(boolean onlyNecessary, IProgressUpdate progressUpdate) {
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
		chunk.isTerrainPopulated = true;
		
		return chunk;
	}
}

package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.levelgen.feature.WorldGenClay;
import net.minecraft.world.level.levelgen.feature.WorldGenDungeons;
import net.minecraft.world.level.levelgen.feature.WorldGenFlowers;
import net.minecraft.world.level.levelgen.feature.WorldGenGlowStone1;
import net.minecraft.world.level.levelgen.feature.WorldGenGlowStone2;
import net.minecraft.world.level.levelgen.feature.WorldGenLiquids;
import net.minecraft.world.level.levelgen.feature.WorldGenMinable;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockSand;
import net.minecraft.world.level.tile.BlockTerracotta;

public class ChunkProviderCaves extends ChunkProviderGenerate {
	public static byte SEA_LEVEL = 32;
	
	// Generates cavey world, derived from the Nether nose & caves, but different fill / population
	
	public ChunkProviderCaves(World world, long seed) {
		this(world, seed, true, true);
	}
	
	public ChunkProviderCaves(World world, long seed, boolean mapFeaturesEnabled, boolean layeredSand) {
		super(world, seed, mapFeaturesEnabled, layeredSand);
	}

	@Override
	public void generateTerrain(int chunkX, int chunkZ, byte[] blocks) {
		final double noiseScale = 0.125D;
		final double scalingFactor = 0.25D;
		final double densityVariationSpeed = 0.25D;
		
		final byte quadrantSize = 4;
		final byte seaLevel = SEA_LEVEL;
		final int xSize = quadrantSize + 1;
		final byte ySize = 17;
		final int zSize = quadrantSize + 1;
		final short chunkHeight = 128;

		this.terrainNoise = this.initializeNoiseField(this.terrainNoise, chunkX * quadrantSize, 0, chunkZ * quadrantSize, xSize, ySize, zSize);
		this.isOcean = true;
		
		// Split in 4x16x4 sections
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
								int biomeIndex = (x + (xSection << 2)) << 4 | (z + (zSection << 2));
								
								int blockID;

								// World density positive: fill with block. Otherwise, fill with water or air.
								if(density > 0.0D) {
									blockID = Block.stone.blockID;
								} else if(yy < seaLevel) {
									blockID = this.biomesForGeneration[biomeIndex].mainLiquid;
								} else {
									blockID = 0;
								}

								blocks[indexInBlockArray] = (byte)blockID;
								
								// Next Z
								indexInBlockArray += chunkHeight;
								density += densityIncrement;
								
								// Ocean detector
								if(yy == seaLevel - 1) this.isOcean &= (blockID != Block.stone.blockID);
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
	
	/*
	 * Nether's noise field generator:
	 */
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

	@Override
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
			(new WorldGenMinable(Block.oreGlow.blockID, 6)).generate(this.worldObj, this.rand, x, y, z);
		}
				
		for(i = 0; i < biomeGen.ironLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.goldLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(32);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.redstoneLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(32);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < biomeGen.diamondLumpAttempts; ++i) {
			x = x0 + this.rand.nextInt(16);
			y = this.rand.nextInt(16);
			z = z0 + this.rand.nextInt(16);
			(new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(this.worldObj, this.rand, x, y, z);
		}
	}
	
	@Override
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
		
		// Find which biome
		BiomeGenBase biomeGen = thisChunk.getBiomeGenAt(8, 8);
				
		this.rand.setSeed(this.worldObj.getRandomSeed());
		long seed1 = this.rand.nextLong() / 2L * 2L + 1L;
		long seed2 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)chunkX * seed1 + (long)chunkZ * seed2 ^ this.worldObj.getRandomSeed());

		biomeGen.prePopulate(this.worldObj, this.rand, x0, z0);
		
		int i, x, y, z;

		// Features
		if(this.mapFeaturesEnabled) {
			// Vanilla map features (mineshafts)
			this.generateMapFeatures(chunkX, chunkZ);
			
			// Custom features
			this.featureProvider.populateFeatures(worldObj, rand, chunkX, chunkZ);
		}
		
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
					treeGen = biomeGen.getBigTreeGen(this.worldObj, this.rand, chunkX, chunkZ);
				} else {
					treeGen = biomeGen.getTreeGen(this.worldObj, this.rand, chunkX, chunkZ);
				}
				
				if(treeGen != null) treeGen.generate(this.worldObj, this.rand, x, y, z);
			}
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
			
			Block block = Block.blocksList[this.worldObj.getBlockID(x, y - 1, z)];
			if (block == Block.sand || block == Block.grass || block == Block.dirt || (block instanceof BlockTerracotta)) 
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
		
		i = this.rand.nextInt(this.rand.nextInt(5) + 1);

		for(i = 0; i < i; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(120) + 4;
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenGlowStone1()).generate(this.worldObj, this.rand, x, y, z);
		}

		for(i = 0; i < 5; ++i) {
			x = x0 + this.rand.nextInt(16) + 8;
			y = this.rand.nextInt(128);
			z = z0 + this.rand.nextInt(16) + 8;
			(new WorldGenGlowStone2()).generate(this.worldObj, this.rand, x, y, z);
		}
		
		if(this.mapFeaturesEnabled) {
			((MapGenUnderwater)this.underwaterGenerator).populate(this.worldObj, this.rand, chunkX, chunkZ, thisChunk);
		}

		// Layered sand
		// 1st step = build float "blurred" height map
		if(this.layeredSand) {
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
					if(y >= 63 && this.worldObj.getBlockID(x0 + x, y, z0 + z) == Block.sand.blockID) {
						int yy = (int)f;
						int meta = (int)(16.0F * (f - Math.floor(f))) - 1 + (this.rand.nextInt(2) << 1) - 1;
						if(meta < 0) meta = 0;
						if(meta > 15) meta = 15;
						if (this.worldObj.getBlockID(x0 + x, yy + 1, z0 + z) == 0 && meta > 0) {
							this.worldObj.setBlockAndMetadata(x0 + x, yy , z0 + z, Block.layeredSand.blockID, meta);
						}
					}
				}
			}		
		}

		// Biome based population
		biomeGen.populate(this.worldObj, this.rand, x0, z0);
				
		BlockSand.fallInstantly = false;
		thisChunk.beingDecorated = false;
	}
	
}

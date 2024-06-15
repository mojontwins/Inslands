package net.minecraft.src;

import java.util.Random;

public class BiomeGenThemeHell extends BiomeGenBase {

	public BiomeGenThemeHell() {
		super();
		this.overrideSkyColor = 0x100400;
		this.overrideFogColor = 0x100400;
		this.overrideCloudColor = 0x210800;
		
		this.bigTreesEach10Trees = 1;
		this.mushroomBrownChance = 1;
		this.mushroomRedChance = 2;
		this.lavaAttempts = 50;
		this.waterFallAttempts = 0;
		this.mainLiquid = Block.lavaStill.blockID;
		this.deadBushAttempts = 8;
		this.topBlock = (byte)Block.dirt.blockID;
		
		this.weather = Weather.desert;
		this.foliageColorizer = 1;
		
		this.forceBlockLightInitLikeNether = true;
	}
	
	public WorldGenerator getTreeGen(Random rand) {
		return new WorldGenTreesDead();
	}
	
	public WorldGenerator getBigTreeGen(Random rand) {
		return rand.nextBoolean() ? new WorldGenTreesDead() : new WorldGenTrees();
	}
	
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z, i;
		
		int amount = rand.nextInt(3);
		for(i = 0; i < amount; i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.getHeightValue(x, z);
			(new WorldGenBigMushroom(rand.nextInt(3) == 0 ? 0 : 1)).generate(world, rand, x, y, z);
		}
		
		if(rand.nextInt(8) == 0) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.getHeightValue(x, z);
			(new WorldGenBigMushroom(2)).generate(world, rand, x, y, z);
		}
		
		if (rand.nextInt(32) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			WorldGenRockBoulder worldGenRockBoulder = new WorldGenRockBoulder(true);
			worldGenRockBoulder.generate(world, rand, x, world.getLandSurfaceHeightValue(x, z) + 1, z);
		}
		
		for(i = 0; i < 10; ++i) {
			x = chunkX + rand.nextInt(16);
			y = rand.nextInt(64) + 48;
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.regolith.blockID, 32)).generate(world, rand, x, y, z);
		}	
		
		if(rand.nextInt(64) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(rand.nextInt(120) + 8);
			z = chunkZ + rand.nextInt(16) + 8;
			if(y < 56 || rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.lavaMoving.blockID)).generate(world, rand, x, y, z);
			}
		}
		
		int j = (rand.nextInt(rand.nextInt(10) + 1) + 1) / 2;

		for(i = 0; i < j; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(60) + 64;
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFireWithSet()).generate(world, rand, x, y, z);
		}
	}
	
	public void replaceBlocksForBiome(IChunkProvider generator, World world, Random rand, int chunkX, int chunkZ, int x, int z, byte[] blocks, byte[] metadata, int seaLevel, double sandNoise, double gravelNoise, double stoneNoise) {
		boolean generateSand = sandNoise + rand.nextDouble() * 0.2D > 0.0D;
		boolean generateGravel = gravelNoise + rand.nextDouble() * 0.2D > 3.0D;
		boolean generateSoulSand = gravelNoise + rand.nextDouble() * 0.3 > 2.0D;
		int height = (int)(stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);

		int stoneHeight = -1;
		byte topBlock = this.getTopBlock(rand);
		byte fillerBlock = this.fillerBlock;

		for(int y = 127; y >= 0; --y) {
			int index = x << 11 | z << 7 | y; // (x * 16 + z) * 128 + y
			
			if(y <= 0 + rand.nextInt(5) && !(generator instanceof ChunkProviderSky)) {
				blocks[index] = (byte)Block.bedrock.blockID;
			} else {
				byte blockID = blocks[index];
				byte meta = metadata[index];
				if(blockID == 0) {
					stoneHeight = -1;
				} else if(blockID == Block.stone.blockID && meta == 0) {
					if(stoneHeight == -1) {
						if(height <= 0) {
							topBlock = 0;
							fillerBlock = (byte)Block.stone.blockID;
						} else if(y >= seaLevel - 4 && y <= seaLevel + 1 && !(generator instanceof ChunkProviderSky)) {
							topBlock = this.topBlock;
							fillerBlock = this.fillerBlock;
							if(generateGravel) {
								topBlock = 0;
							}
							
							if(this.genBeaches) {
								if(generateGravel) {
									fillerBlock = (byte)Block.gravel.blockID;
								}

								if(generateSand) {
									topBlock = (byte)Block.sand.blockID;
									fillerBlock = (byte)Block.sand.blockID;
								}
								
								if(generateSoulSand) {
									topBlock = (byte)Block.slowSand.blockID;
									fillerBlock = (byte)Block.slowSand.blockID;
								}
							}
						}

						if(y < seaLevel && topBlock == 0 && !(generator instanceof ChunkProviderSky)) {
							if(this.weather == Weather.cold) {
								topBlock = (byte)Block.ice.blockID;
							} else {
								topBlock = (byte)this.mainLiquid;
							}
						}

						stoneHeight = height;
						if(y >= seaLevel - 1 || generator instanceof ChunkProviderSky) {
							blocks[index] = topBlock;
						} else {
							blocks[index] = fillerBlock;
						}
					} else if(stoneHeight > 0) {
						--stoneHeight;
						blocks[index] = fillerBlock;
						if(stoneHeight == 0 && fillerBlock == this.sandstoneGenTriggerer()) {
							stoneHeight = rand.nextInt(4);
							fillerBlock = this.sandstoneGenBlock();
						}
					}
				} else if(blockID == Block.waterStill.blockID && y == seaLevel - 1 && this.weather == Weather.cold) {
					blocks[index] = (byte)Block.ice.blockID;
				}
			}
		}
	}
}


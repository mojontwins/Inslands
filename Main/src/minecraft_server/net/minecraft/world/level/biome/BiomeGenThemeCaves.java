package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.entity.mob.spider.EntityTFHedgeSpider;
import net.minecraft.world.entity.mob.spider.EntityTFSwarmSpiderCaves;
import net.minecraft.world.entity.mob.twilight.EntityTFKobold;
import net.minecraft.world.entity.mob.twilight.EntityTFRedcap;
import net.minecraft.world.entity.mob.undead.EntityGhoul;
import net.minecraft.world.level.SpawnListEntry;
import net.minecraft.world.level.Weather;
import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.levelgen.ChunkProviderSky;
import net.minecraft.world.level.levelgen.feature.WorldGenCaveVines;
import net.minecraft.world.level.levelgen.feature.WorldGenFlowers;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenBigMushroom;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenBog1;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenSwamp;
import net.minecraft.world.level.tile.Block;

public class BiomeGenThemeCaves extends BiomeGenBase {
	public BiomeGenThemeCaves(int id) {
		super(id);
		
		this.overrideFogColor = 0x444488;
		this.weather = Weather.hot;
		
		this.bigTreesEach10Trees = 0;
		this.mushroomBrownChance = 1;
		this.mushroomRedChance = 1;
		
		this.diamondLumpAttempts = 8;
		this.goldLumpAttempts = 32;
		this.redstoneLumpAttempts = 16;
		this.ironLumpAttempts = 32;
		this.coalLumpAttempts = 64;
		
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityGhoul.class, 10));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFSwarmSpiderCaves.class, 5));		
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFKobold.class, 10));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFRedcap.class, 5));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFHedgeSpider.class, 5));
	}
	
	@Override
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if(rand.nextInt(64) == 0) {
			return rand.nextBoolean() ? 
					new WorldGenBog1() 
				:
					new WorldGenSwamp();
		}
		return new WorldGenBigMushroom(rand.nextInt(3));
	}
	
	@Override
	public void populate(World world, Random rand, int chunkX, int chunkZ) {	
		super.populate(world, rand, chunkX, chunkZ);
		int x, y, z;
		
		// Cave vines
		for (int i = 0; i < 32; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(96);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenCaveVines()).generate(world, rand, x, y, z);
		}		
		
		// Glowshrooms
		for(int i = 0; i < 4; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(64);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.glowshroom.blockID)).generate(world, rand, x, y, z);
		}
	}
	
	@Override
	public void replaceBlocksForBiome(IChunkProvider generator, World world, Random rand, int chunkX, int chunkZ, int x, int z, byte[] blocks, byte[] metadata, int seaLevel, double sandNoise, double gravelNoise, double stoneNoise) {
		boolean generateSand = sandNoise + rand.nextDouble() * 0.2D > 0.0D;
		boolean generateGravel = gravelNoise + rand.nextDouble() * 0.2D > 3.0D;
		int height = (int)(stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);

		int stoneHeight = -1;
		byte topBlock = this.getTopBlock(rand);
		byte fillerBlock = this.fillerBlock;

		for(int y = 127; y >= 0; --y) {
			int index = x << 11 | z << 7 | y; // (x * 16 + z) * 128 + y
			
			if((y <= rand.nextInt(5) || y >= 128 -rand.nextInt(5)) && !(generator instanceof ChunkProviderSky)) {
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

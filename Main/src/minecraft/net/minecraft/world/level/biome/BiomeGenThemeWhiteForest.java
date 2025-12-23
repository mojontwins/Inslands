package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.entity.animal.EntityColdCow;
import net.minecraft.world.entity.animal.EntityTwilightBoar;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.monster.EntityGhoul;
import net.minecraft.world.entity.monster.EntityTFWraith;
import net.minecraft.world.entity.monster.EntityZombieAlex;
import net.minecraft.world.level.SpawnListEntry;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.levelgen.feature.WorldGenCaveVines;
import net.minecraft.world.level.levelgen.feature.WorldGenFlowers;
import net.minecraft.world.level.levelgen.feature.WorldGenHollowLogs;
import net.minecraft.world.level.levelgen.feature.WorldGenRuinedHouse;
import net.minecraft.world.level.levelgen.feature.WorldGenVines;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenAlder;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenAspen;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenEucalyptusBig;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenForest;
import net.minecraft.world.level.tile.Block;

public class BiomeGenThemeWhiteForest extends BiomeGenBase {
	public BiomeGenThemeWhiteForest() {
		super();
		this.overrideSkyColor = 0xCCCCCC;
		this.overrideFogColor = 0xEEEEEE;
		this.overrideCloudColor = 0xEEEEFF;
		
		this.bigTreesEach10Trees = 5;
		this.treeBaseAttemptsModifier = 30;
		this.tallGrassAttempts = 128;
		this.redFlowersAttempts = 16;
		this.yellowFlowersAttempts = 24;
		
		this.gravelLumpAttempts = 20;
		this.waterFallAttempts = 100;
		
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityColdCow.class, 8));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityTwilightBoar.class, 8));
		
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombieAlex.class, 10));
		
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFWraith.class, 1));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityGhoul.class, 10));
	}
	
	@Override
	public void prePopulate(World world, Random rand, int x0, int z0) {
		if(x0 > 0 && x0 < WorldSize.width-16 && z0 > 0 && z0 < WorldSize.length - 16) {
			if(rand.nextInt(16) == 0) {
				(new WorldGenRuinedHouse()).generate(world, rand, x0 + rand.nextInt(16), 64, z0 + rand.nextInt(16));
			}
		}
	}
	
	@Override
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		switch(rand.nextInt(4)) {
		case 0:	return new WorldGenAlder(3 + rand.nextInt(3), 4, 3).setRelaxedBoundaries();
		case 1: return new WorldGenAspen(3 + rand.nextInt(3)).setRelaxedBoundaries();
		default: return new WorldGenForest().setRelaxedBoundaries();
		}
	};
	
	@Override
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		switch(rand.nextInt(3)) {
		case 0:	return new WorldGenAlder(6 + rand.nextInt(3), 5, 4).setRelaxedBoundaries();
		case 1: return new WorldGenAspen(8 + rand.nextInt(4)).setRelaxedBoundaries();
		default: return new WorldGenEucalyptusBig(true).setRelaxedBoundaries();
		}
	};
	
	public int getNetherVinesPerChunk() {
		return 32;
	}
	
	@Override
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z;
		
		for(int i = 0; i < rand.nextInt(4); i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.getHeightValue(x, z);
			(new WorldGenHollowLogs ()).generate(world, rand, x, y, z);
		}
		
		// Glowshroom
		for(int i = 0; i < 8; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.glowshroom.blockID)).generate(world, rand, x, y, z);
		}
		
		// Cave vines
		for (int i = 0; i < this.getNetherVinesPerChunk(); ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(96);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenCaveVines()).generate(world, rand, x, y, z);
		}
		
		// Generate vines
		for(int i = 0; i < 150; i++) {
			x = chunkX + rand.nextInt(16) + 8;
			y = 32;
			z = chunkZ + rand.nextInt(16) + 8;
			
			(new WorldGenVines()).generate(world, rand, x, y, z);
		}
	}
}

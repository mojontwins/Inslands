package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntityDrowned;
import net.minecraft.world.entity.monster.EntityElementalCreeper;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.level.SpawnListEntry;
import net.minecraft.world.level.Weather;
import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenFlowers;
import net.minecraft.world.level.levelgen.feature.WorldGenHollowLogs;
import net.minecraft.world.level.levelgen.feature.WorldGenLakes;
import net.minecraft.world.level.levelgen.feature.WorldGenLilypad;
import net.minecraft.world.level.levelgen.feature.WorldGenVines;
import net.minecraft.world.level.levelgen.feature.WorldGenWitchHut;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenBigTree;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenSwamp;
import net.minecraft.world.level.tile.Block;

public class BiomeGenSwampLand extends BiomeGenBeta {
	public BiomeGenSwampLand () {
		super();
		
		this.biomeColor = 522674;
		
		this.treeBaseAttemptsModifier = 1;
		this.reedAttempts = 20;
		this.mushroomBrownChance = 3;
		this.redFlowersAttempts = 4;
		this.bigTreesEach10Trees = 8;
		this.tallGrassAttempts = 32;
		this.deadBushAttempts = 4;
		this.waterFallAttempts = 8;
		this.pumpkinChance = 16;
		this.genBeaches = false;
		
		this.weather = Weather.hot;
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10));
		
		this.spawnableMonsterList.add(new SpawnListEntry(EntityDrowned.class, 30));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityElementalCreeper.class, 1));
		
		this.minHeight = -0.1F;
		this.maxHeight = 0.125F;
	}
	
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return rand.nextInt(10) == 0 ? new WorldGenBigTree() : new WorldGenSwamp();
	}
	
	
	public void prePopulate(World world, Random rand, int x0, int z0) {
		int x, y, z;
		
		// Mangrove is very wet. After squishing terrain I must add tons of water.
		
		// Generate lakes
		for(int i = 0; i < 6; i++) {
			x = x0 + rand.nextInt(16) + 8;			
			z = z0 + rand.nextInt(16) + 8;
			y = world.getHeightValue(x, z);
			(new WorldGenLakes(Block.waterMoving.blockID)).generate(world, rand, x, y, z);
		}		
	}
	
	public void populate (World world, Random rand, int x0, int z0) {
		int x, y, z, i;
		
		// Witch hut
		if(rand.nextInt(32) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			z = z0 + rand.nextInt(16) + 8;
			y = 64 + rand.nextInt(4);
			(new WorldGenWitchHut()).generate(world, rand, x, y, z);
		}
		
		// Hollow logs
		for(i = 0; i < rand.nextInt(4); i ++) {
			x = x0 + rand.nextInt(16);
			z = z0 + rand.nextInt(16);
			y = world.getHeightValue(x, z);
			(new WorldGenHollowLogs ()).generate(world, rand, x, y, z);
		}
		
		// Generate vines
		for(i = 0; i < 150; i++) {
			x = x0 + rand.nextInt(16) + 8;
			y = 32;
			z = z0 + rand.nextInt(16) + 8;
			
			(new WorldGenVines()).generate(world, rand, x, y, z);
		}
		
		// Generate Lilypads
		for(i = 0; i < 8; i ++) {
			x = x0 + rand.nextInt(16) + 8;
			z = z0 + rand.nextInt(16) + 8;
			
			for(y = rand.nextInt(128); y > 0 && world.getBlockId(x, y - 1, z) == 0; y --) {}
			
			(new WorldGenLilypad()).generate(world, rand, x, y, z);
		}
		
		// Blue Flowers
		for(i = 0; i < 10; ++i) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = z0 + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.blueFlower.blockID)).generate(world, rand, x, y, z);
		}
		
		// Glowshrooms
		for(i = 0; i < 8; i ++) {
			x = x0 + rand.nextInt(16) + 8;
			y = 64 + rand.nextInt(64);
			z = z0 + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.glowshroom.blockID)).generate(world, rand, x, y, z);
		}
		
		// Surface schrooms
		for(i = 0; i < 8; i ++) {
			x = x0 + rand.nextInt(16) + 8;
			z = z0 + rand.nextInt(16) + 8;
			y = world.getHeightValue(x, z);
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(world, rand, x, y, z);
		}
		
		for(i = 0; i < 4; i ++) {
			x = x0 + rand.nextInt(16) + 8;
			z = z0 + rand.nextInt(16) + 8;
			y = world.getHeightValue(x, z);
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(world, rand, x, y, z);
		}
	}	
	
	@Override
	public boolean isHumid() {
		return true;
	}
}

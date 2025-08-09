package com.mojang.minecraft.betabiomes;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBigTreeDead;
import net.minecraft.src.WorldGenDesertFlowers;
import net.minecraft.src.WorldGenLakes;
import net.minecraft.src.WorldGenMinable;
import net.minecraft.src.WorldGenerator;
import net.minecraft.world.entity.animal.EntityChickenBlack;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntityHusk;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.entity.monster.EntitySpider;

public class BiomeGenBetaDesert extends BiomeGenBeta {

	public BiomeGenBetaDesert() {
		super();
		
		this.biomeColor = 16421912;
		this.treeBaseAttemptsModifier = -20;
		this.cactusAttempts = 10;
		this.topBlock = (byte) Block.sand.blockID;
		this.fillerBlock = (byte) Block.sand.blockID;
		this.deadBushAttempts = 8;
		this.weather = Weather.desert;
		this.dungeonAttempts = 10;
		this.clayAttempts = 20;
		this.dirtLumpAttempts = 10;
		this.diamondLumpAttempts = 4;
		this.diamondLumpMaxHeight = 24;
		this.lavaAttempts = 40;
		this.glowLumpAttempts = 25;
		
		this.foliageColorizer = 1;
		this.spawnableCreatureList.clear();
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChickenBlack.class, 10));
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityHusk.class, 25));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10));
		
	}
	
	public String getPreferedSpawner() {
		return "Husk";
	}
	
	public int getPreferedSpawnerChance() {
		return 4;
	}
	
	public int getPreferedSpawnerChanceOffset() {
		return 3;
	}
	
	public WorldGenerator getBigTreeGen() {
		return new WorldGenBigTreeDead();
	}
	
	public void prePopulate(World world, Random rand, int x0, int z0) {
		int x, y, z;
		
		if(rand.nextInt(8) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(rand.nextInt(120) + 8);
			z = z0 + rand.nextInt(16) + 8;
			if(y < 64 || rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.waterMoving.blockID)).generate(world, rand, x, y, z);
			}
		}
	}
	
	public void populate(World world, Random rand, int x0, int z0) {
		int x, y, z, i;
		
		for(i = 0; i < 2; ++i) {
			x = x0 + rand.nextInt(16);
			y = 12 + rand.nextInt(48);
			z = z0 + rand.nextInt(16);
			(new WorldGenMinable(Block.oreRuby.blockID, 7)).generate(world, rand, x, y, z);
		}
		
		for(i = 0; i < 2; ++i) {
			x = x0 + rand.nextInt(16);
			y = 12 + rand.nextInt(48);
			z = z0 + rand.nextInt(16);
			(new WorldGenMinable(Block.oreEmerald.blockID, 7)).generate(world, rand, x, y, z);
		}
		
		// Generate tall grass on high ground
		for(i = 0; i < 16; ++i) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(32) + 96;
			z = z0 + rand.nextInt(16) + 8;
			(new WorldGenDesertFlowers(Block.tallGrass.blockID, 0x10, 0x90)).generate(world, rand, x, y, z);
		}
	}

}

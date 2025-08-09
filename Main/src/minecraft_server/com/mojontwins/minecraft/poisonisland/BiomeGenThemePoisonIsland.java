package com.mojontwins.minecraft.poisonisland;

import java.util.Random;

import com.benimatic.twilightforest.EntityTFSwarmSpiderCaves;
import com.gw.dm.EntityGhoul;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBigMushroom;
import net.minecraft.src.WorldGenBog1;
import net.minecraft.src.WorldGenCaveVines;
import net.minecraft.src.WorldGenSwamp;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.WorldGenerator;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.entity.monster.EntityZombie;

public class BiomeGenThemePoisonIsland extends BiomeGenBase {

	WorldGenerator treeGen = new WorldGenTrees();
	
	public BiomeGenThemePoisonIsland() {
		super();
		this.overrideSkyColor = 0x0b0c33;
		this.overrideFogColor = 0x3ab14e;
		this.overrideCloudColor = 0x4d4fa0;

		this.mainLiquid = Block.acidStill.blockID;
		this.weather = Weather.hot;
		
		this.topBlock = (byte)Block.podzol.blockID;
	
		this.bigTreesEach10Trees = 1;
		this.mushroomBrownChance = 1;
		this.mushroomRedChance = 2;
		this.lavaAttempts = 50;
		this.waterFallAttempts = 0;
		this.deadBushAttempts = 8;
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityPoisonSkeleton.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10));
		
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityGhoul.class, 10));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFSwarmSpiderCaves.class, 5));
	
	}
	
	@Override
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if(rand.nextInt(64) == 0) {
			return rand.nextBoolean() ? 
					new WorldGenBog1() 
				:
					new WorldGenSwamp();
		}
		return new WorldGenBigMushroom(rand.nextInt(2));
	}
	
	@Override
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenBigMushroom(2);
	}

	@Override
	public void populate(World world, Random rand, int chunkX, int chunkZ) {	
		super.populate(world, rand, chunkX, chunkZ);
		int x, y, z;
		
		// Trees only on mesas
		for(int i = 0; i < 4; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			y = world.getHeightValue(x, z);
			
			if(y > 80) {
				this.treeGen.generate(world, rand, x, y, z);
			}
		}
		
		// Cave vines
		for (int i = 0; i < 32; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(96);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenCaveVines()).generate(world, rand, x, y, z);
		}
		
	}

	
}

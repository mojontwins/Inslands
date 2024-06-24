package com.mojang.minecraft.betabiomes;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenIceSpike;
import net.minecraft.src.WorldGenIgloos;
import net.minecraft.src.WorldGenMinable;

public class BiomeGenIceDesert extends BiomeGenBetaDesert {

	public BiomeGenIceDesert() {
		super();
		
		this.weather = Weather.cold;
		this.cactusAttempts = 2;
		
	}

	public boolean isPermaFrost() {
		return true;
	}

	public void populate (World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z;
		
		// Generate IceSpike
		for(int i = 0; i < 8; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			WorldGenIceSpike worldgenIceSpike = new WorldGenIceSpike();
			worldgenIceSpike.generate(world, rand, x, world.getHeightValue(x, z) + 1, z);
		}
	
		// Generate igloos
		if (rand.nextInt(64) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			WorldGenIgloos worldgenIgloos = new WorldGenIgloos();
			worldgenIgloos.generate(world, rand, x, world.getHeightValue(x, z), z);
		}
		
		// Generate snow pools
		for(int i = 0; i < 20; ++i) {
			x = chunkX + rand.nextInt(16);
			y = rand.nextInt(128);
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.blockSnow.blockID, 32)).generate(world, rand, x, y, z);
		}
	}
}

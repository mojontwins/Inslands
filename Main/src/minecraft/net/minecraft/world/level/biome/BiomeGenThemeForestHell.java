package net.minecraft.world.level.biome;

import java.util.Random;

import com.mojontwins.minecraft.nether.WorldGenBloodTree;
import com.mojontwins.minecraft.nether.WorldGenHellWillow;
import com.mojontwins.minecraft.worldgen.WorldGenPopsicleshroom;

import net.minecraft.src.World;
import net.minecraft.src.WorldGenCaveVines;

public class BiomeGenThemeForestHell extends BiomeGenHell {

	public BiomeGenThemeForestHell() {
		super();
	}

	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z, i;
		
		int amount = rand.nextInt(12);
		for(i = 0; i < amount; i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.findSurfaceInCave(x, 32 + rand.nextInt(96), z);
			(new WorldGenPopsicleshroom(false)).generate(world, rand, x, y, z);
		}
		
		amount = rand.nextInt(12);
		for(i = 0; i < amount; i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = 32 + rand.nextInt(96);
			(new WorldGenBloodTree()).generate(world, rand, x, y, z);
		}
		
		amount = rand.nextInt(4);
		for(i = 0; i < amount; i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.findSurfaceInCave(x, 32 + rand.nextInt(96), z);
			(new WorldGenHellWillow(4 + rand.nextInt(4))).generate(world, rand, x, y, z);
		}
		
		// Cave vines
		for (i = 0; i < 32; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = 64 + rand.nextInt(64);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenCaveVines()).generate(world, rand, x, y, z);
		}
	}
}

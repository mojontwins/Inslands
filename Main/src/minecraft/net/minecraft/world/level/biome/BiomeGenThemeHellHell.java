package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenCaveVines;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenBloodTree;

public class BiomeGenThemeHellHell extends BiomeGenHell {

	public BiomeGenThemeHellHell() {
		super();
		this.hellFireExtraAttempts = 4;
		this.hellLavaExtraAttemtps = 4;
	}

	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z, i;
		
		int amount = 4 + rand.nextInt(12);
		for(i = 0; i < amount; i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = 32 + rand.nextInt(96);
			(new WorldGenBloodTree()).generate(world, rand, x, y, z);
		}
		
		// Cave vines
		for (i = 0; i < 4; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = 64 + rand.nextInt(64);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenCaveVines()).generate(world, rand, x, y, z);
		}

	}	

}

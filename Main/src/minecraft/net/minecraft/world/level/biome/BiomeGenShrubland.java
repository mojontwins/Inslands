package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.src.World;
import net.minecraft.src.WorldGenShrub;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.WorldGenWillow;
import net.minecraft.src.WorldGenerator;

public class BiomeGenShrubland extends BiomeGenBetaForest {

	public BiomeGenShrubland() {
		super();
		
		this.deadBushAttempts = 4;
		this.tallGrassAttempts = 8;
	}

	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if (rand.nextInt(6) != 0) {
			return new WorldGenShrub();
		} else
			return new WorldGenTrees();
	}

	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenWillow(4 + rand.nextInt(4));
	}
}

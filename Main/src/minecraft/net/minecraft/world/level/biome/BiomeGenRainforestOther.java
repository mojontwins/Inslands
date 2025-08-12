package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenBigTree;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenTrees;

public class BiomeGenRainforestOther extends BiomeGenBase {
	public WorldGenerator getRandomWorldGenForTrees(Random random1) {
		return (WorldGenerator)(random1.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
	}
}

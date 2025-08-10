package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.src.WorldGenBigTree;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.WorldGenerator;

public class BiomeGenRainforestOther extends BiomeGenBase {
	public WorldGenerator getRandomWorldGenForTrees(Random random1) {
		return (WorldGenerator)(random1.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
	}
}

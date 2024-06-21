package net.minecraft.src;

import java.util.Random;

public class BiomeGenTaiga extends BiomeGenBase {
	public BiomeGenTaiga(int i1) {
		super(i1);
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 8, 4, 4));
		this.biomeDecorator.treesPerChunk = 10;
		this.biomeDecorator.grassPerChunk = 1;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random1) {
		return (WorldGenerator)(random1.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2(false));
	}
}

package net.minecraft.src;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
	public BiomeGenForest(int i1) {
		super(i1);
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
		this.biomeDecorator.treesPerChunk = 10;
		this.biomeDecorator.grassPerChunk = 2;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random1) {
		return (WorldGenerator)(random1.nextInt(5) == 0 ? this.worldGenForest : (random1.nextInt(10) == 0 ? this.worldGenBigTree : this.worldGenTrees));
	}
}

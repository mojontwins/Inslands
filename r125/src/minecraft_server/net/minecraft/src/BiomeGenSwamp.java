package net.minecraft.src;

import java.util.Random;

public class BiomeGenSwamp extends BiomeGenBase {
	protected BiomeGenSwamp(int i1) {
		super(i1);
		this.biomeDecorator.treesPerChunk = 2;
		this.biomeDecorator.flowersPerChunk = -999;
		this.biomeDecorator.deadBushPerChunk = 1;
		this.biomeDecorator.mushroomsPerChunk = 8;
		this.biomeDecorator.reedsPerChunk = 10;
		this.biomeDecorator.clayPerChunk = 1;
		this.biomeDecorator.waterlilyPerChunk = 4;
		this.waterColorMultiplier = 14745518;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random1) {
		return this.worldGenSwamp;
	}
}

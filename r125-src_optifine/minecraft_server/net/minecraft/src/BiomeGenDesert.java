package net.minecraft.src;

import java.util.Random;

public class BiomeGenDesert extends BiomeGenBase {
	public BiomeGenDesert(int i1) {
		super(i1);
		this.spawnableCreatureList.clear();
		this.topBlock = (byte)Block.sand.blockID;
		this.fillerBlock = (byte)Block.sand.blockID;
		this.biomeDecorator.treesPerChunk = -999;
		this.biomeDecorator.deadBushPerChunk = 2;
		this.biomeDecorator.reedsPerChunk = 50;
		this.biomeDecorator.cactiPerChunk = 10;
	}

	public void decorate(World world1, Random random2, int i3, int i4) {
		super.decorate(world1, random2, i3, i4);
		if(random2.nextInt(1000) == 0) {
			int i5 = i3 + random2.nextInt(16) + 8;
			int i6 = i4 + random2.nextInt(16) + 8;
			WorldGenDesertWells worldGenDesertWells7 = new WorldGenDesertWells();
			worldGenDesertWells7.generate(world1, random2, i5, world1.getHeightValue(i5, i6) + 1, i6);
		}

	}
}

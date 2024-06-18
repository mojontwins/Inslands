package net.minecraft.src;

import java.util.Random;

public class BiomeGenJungle extends BiomeGenBase {
	public BiomeGenJungle(int i1) {
		super(i1);
		this.biomeDecorator.treesPerChunk = 50;
		this.biomeDecorator.grassPerChunk = 25;
		this.biomeDecorator.flowersPerChunk = 4;
		this.spawnableMonsterList.add(new SpawnListEntry(EntityOcelot.class, 2, 1, 1));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random1) {
		return (WorldGenerator)(random1.nextInt(10) == 0 ? this.worldGenBigTree : (random1.nextInt(2) == 0 ? new WorldGenShrub(3, 0) : (random1.nextInt(3) == 0 ? new WorldGenHugeTrees(false, 10 + random1.nextInt(20), 3, 3) : new WorldGenTrees(false, 4 + random1.nextInt(7), 3, 3, true))));
	}

	public WorldGenerator func_48440_b(Random random1) {
		return random1.nextInt(4) == 0 ? new WorldGenTallGrass(Block.tallGrass.blockID, 2) : new WorldGenTallGrass(Block.tallGrass.blockID, 1);
	}

	public void decorate(World world1, Random random2, int i3, int i4) {
		super.decorate(world1, random2, i3, i4);
		WorldGenVines worldGenVines5 = new WorldGenVines();

		for(int i6 = 0; i6 < 50; ++i6) {
			int i7 = i3 + random2.nextInt(16) + 8;
			byte b8 = 64;
			int i9 = i4 + random2.nextInt(16) + 8;
			worldGenVines5.generate(world1, random2, i7, b8, i9);
		}

	}
}

package net.minecraft.src;

import java.util.Random;

public class AetherGenFlowers extends WorldGenerator {
	private int plantBlockId;

	public AetherGenFlowers(int i) {
		this.plantBlockId = i;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		for(int l = 0; l < 64; ++l) {
			int i1 = i + random.nextInt(8) - random.nextInt(8);
			int j1 = j + random.nextInt(4) - random.nextInt(4);
			int k1 = k + random.nextInt(8) - random.nextInt(8);
			if(world.isAirBlock(i1, j1, k1) && ((BlockAetherFlower)Block.blocksList[this.plantBlockId]).canBlockStay(world, i1, j1, k1)) {
				world.setBlock(i1, j1, k1, this.plantBlockId);
			}
		}

		return true;
	}
}

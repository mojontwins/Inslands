package net.minecraft.src;

import java.util.Random;

public class WorldGenFlowers extends WorldGenerator {
	private int plantBlockId;
	private Block plantBlock;

	public WorldGenFlowers(int i1) {
		this.plantBlockId = i1;
		this.plantBlock = Block.blocksList[i1];
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int i6 = 0; i6 < 64; ++i6) {
			int i7 = x + rand.nextInt(8) - rand.nextInt(8);
			int i8 = y + rand.nextInt(4) - rand.nextInt(4);
			int i9 = z + rand.nextInt(8) - rand.nextInt(8);
			if(world.isAirBlock(i7, i8, i9) && ((BlockFlower)this.plantBlock).canBlockStay(world, i7, i8, i9)) {

				// Random big mushroom
				if(this.plantBlock instanceof BlockMushroom && rand.nextInt(3) == 0 && i8 < 64) {
					if (((BlockMushroom)this.plantBlock).growBigMushroom(world, i7, i8, i9, rand)) {
						continue;
					}
				}
				
				world.setBlock(i7, i8, i9, this.plantBlockId);
			}
		}

		return true;
	}
}

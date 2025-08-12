package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class WorldGenFireWithSet extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int i6 = 0; i6 < 64; ++i6) {
			int i7 = x + rand.nextInt(8) - rand.nextInt(8);
			int i8 = y + rand.nextInt(4) - rand.nextInt(4);
			int i9 = z + rand.nextInt(8) - rand.nextInt(8);
			if(world.isAirBlock(i7, i8, i9) && world.getBlockId(i7, i8 - 1, i9) == Block.dirt.blockID) {
				world.setBlockWithNotify(i7, i8, i9, Block.fire.blockID);
				world.setBlockWithNotify(i7 - 1, i8 - 1, i9, Block.bloodStone.blockID);
				world.setBlockWithNotify(i7 + 1, i8 - 1, i9, Block.bloodStone.blockID);
				world.setBlockWithNotify(i7, i8 - 1, i9 - 1, Block.bloodStone.blockID);
				world.setBlockWithNotify(i7, i8 - 1, i9 + 1, Block.bloodStone.blockID);
				world.setBlockWithNotify(i7, i8 - 1, i9, Block.bloodStone.blockID);
			}
		}

		return true;
	}

}

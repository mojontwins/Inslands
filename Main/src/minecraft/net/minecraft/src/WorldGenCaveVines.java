package net.minecraft.src;

import java.util.Random;

public class WorldGenCaveVines extends WorldGenerator {
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(world.getBlockId(x, y, z) != Block.stone.blockID) return false;
		
		while(!world.isAirBlock(x, y, z) && y > 2) {
			y --;
		}
		
		if(y <= 2) return false;
		
		if (world.isAirBlock(x - 1, y, z)) {
			world.setBlock(x - 1, y, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 1, z)) {
			world.setBlock(x - 1, y - 1, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 2, z)) {
			world.setBlock(x - 1, y - 2, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 3, z)) {
			world.setBlock(x - 1, y - 3, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 4, z)) {
			world.setBlock(x - 1, y - 4, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 5, z)) {
			world.setBlock(x - 1, y - 5, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 6, z)) {
			world.setBlock(x - 1, y - 6, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 7, z)) {
			world.setBlock(x - 1, y - 7, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 8, z)) {
			world.setBlock(x - 1, y - 8, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 9, z)) {
			world.setBlock(x - 1, y - 9, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 10, z)) {
			world.setBlock(x - 1, y - 10, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 11, z)) {
			world.setBlock(x - 1, y - 11, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 12, z)) {
			world.setBlock(x - 1, y - 12, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 13, z)) {
			world.setBlock(x - 1, y - 13, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 14, z)) {
			world.setBlock(x - 1, y - 14, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x - 1, y - 15, z)) {
			world.setBlock(x - 1, y - 15, z, Block.caveVine.blockID);
		}

		if (world.isAirBlock(x + 1, y, z)) {
			world.setBlock(x + 1, y, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 1, z)) {
			world.setBlock(x + 1, y - 1, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 2, z)) {
			world.setBlock(x + 1, y - 2, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 3, z)) {
			world.setBlock(x + 1, y - 3, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 4, z)) {
			world.setBlock(x + 1, y - 4, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 5, z)) {
			world.setBlock(x + 1, y - 5, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 6, z)) {
			world.setBlock(x + 1, y - 6, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 7, z)) {
			world.setBlock(x + 1, y - 7, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 8, z)) {
			world.setBlock(x + 1, y - 8, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 9, z)) {
			world.setBlock(x + 1, y - 9, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 10, z)) {
			world.setBlock(x + 1, y - 10, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 11, z)) {
			world.setBlock(x + 1, y - 11, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 12, z)) {
			world.setBlock(x + 1, y - 12, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 13, z)) {
			world.setBlock(x + 1, y - 13, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 14, z)) {
			world.setBlock(x + 1, y - 14, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 15, z)) {
			world.setBlock(x + 1, y - 15, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 16, z)) {
			world.setBlock(x + 1, y - 16, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 17, z)) {
			world.setBlock(x + 1, y - 17, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 18, z)) {
			world.setBlock(x + 1, y - 18, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 19, z)) {
			world.setBlock(x + 1, y - 19, z, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x + 1, y - 20, z)) {
			world.setBlock(x + 1, y - 20, z, Block.caveVine.blockID);
		}

		if (world.isAirBlock(x, y, z - 1)) {
			world.setBlock(x, y, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 1, z - 1)) {
			world.setBlock(x, y - 1, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 2, z - 1)) {
			world.setBlock(x, y - 2, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 3, z - 1)) {
			world.setBlock(x, y - 3, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 4, z - 1)) {
			world.setBlock(x, y - 4, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 5, z - 1)) {
			world.setBlock(x, y - 5, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 6, z - 1)) {
			world.setBlock(x, y - 6, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 7, z - 1)) {
			world.setBlock(x, y - 7, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 8, z - 1)) {
			world.setBlock(x, y - 8, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 9, z - 1)) {
			world.setBlock(x, y - 9, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 10, z - 1)) {
			world.setBlock(x, y - 10, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 11, z - 1)) {
			world.setBlock(x, y - 11, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 12, z - 1)) {
			world.setBlock(x, y - 12, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 13, z - 1)) {
			world.setBlock(x, y - 13, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 14, z - 1)) {
			world.setBlock(x, y - 14, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 15, z - 1)) {
			world.setBlock(x, y - 15, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 16, z - 1)) {
			world.setBlock(x, y - 16, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 17, z - 1)) {
			world.setBlock(x, y - 17, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 18, z - 1)) {
			world.setBlock(x, y - 18, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 19, z - 1)) {
			world.setBlock(x, y - 19, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 20, z - 1)) {
			world.setBlock(x, y - 20, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 21, z - 1)) {
			world.setBlock(x, y - 21, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 22, z - 1)) {
			world.setBlock(x, y - 22, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 23, z - 1)) {
			world.setBlock(x, y - 23, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 24, z - 1)) {
			world.setBlock(x, y - 24, z - 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 25, z - 1)) {
			world.setBlock(x, y - 25, z - 1, Block.caveVine.blockID);
		}

		if (world.isAirBlock(x, y, z + 1)) {
			world.setBlock(x, y, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 1, z + 1)) {
			world.setBlock(x, y - 1, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 2, z + 1)) {
			world.setBlock(x, y - 2, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 3, z + 1)) {
			world.setBlock(x, y - 3, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 4, z + 1)) {
			world.setBlock(x, y - 4, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 5, z + 1)) {
			world.setBlock(x, y - 5, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 6, z + 1)) {
			world.setBlock(x, y - 6, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 7, z + 1)) {
			world.setBlock(x, y - 7, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 8, z + 1)) {
			world.setBlock(x, y - 8, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 9, z + 1)) {
			world.setBlock(x, y - 9, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 10, z + 1)) {
			world.setBlock(x, y - 10, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 11, z + 1)) {
			world.setBlock(x, y - 11, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 12, z + 1)) {
			world.setBlock(x, y - 12, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 13, z + 1)) {
			world.setBlock(x, y - 13, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 14, z + 1)) {
			world.setBlock(x, y - 14, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 15, z + 1)) {
			world.setBlock(x, y - 15, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 16, z + 1)) {
			world.setBlock(x, y - 16, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 17, z + 1)) {
			world.setBlock(x, y - 17, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 18, z + 1)) {
			world.setBlock(x, y - 18, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 19, z + 1)) {
			world.setBlock(x, y - 19, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 20, z + 1)) {
			world.setBlock(x, y - 20, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 21, z + 1)) {
			world.setBlock(x, y - 21, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 22, z + 1)) {
			world.setBlock(x, y - 22, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 23, z + 1)) {
			world.setBlock(x, y - 23, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 24, z + 1)) {
			world.setBlock(x, y - 24, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 25, z + 1)) {
			world.setBlock(x, y - 25, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 26, z + 1)) {
			world.setBlock(x, y - 26, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 27, z + 1)) {
			world.setBlock(x, y - 27, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 28, z + 1)) {
			world.setBlock(x, y - 28, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 29, z + 1)) {
			world.setBlock(x, y - 29, z + 1, Block.caveVine.blockID);
		}
		if (world.isAirBlock(x, y - 30, z + 1)) {
			world.setBlock(x, y - 30, z + 1, Block.caveVine.blockID);
		}

		return true;
	}
}

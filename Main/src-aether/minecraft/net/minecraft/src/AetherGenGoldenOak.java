package net.minecraft.src;

import java.util.Random;

public class AetherGenGoldenOak extends WorldGenerator {
	public boolean branch(World world, Random random, int i, int j, int k, int slant) {
		int directionX = random.nextInt(3) - 1;
		int directionY = slant;
		int directionZ = random.nextInt(3) - 1;

		for(int n = 0; n < random.nextInt(2) + 1; ++n) {
			i += directionX;
			j += directionY;
			k += directionZ;
			if(world.getBlockId(i, j, k) == AetherBlocks.GoldenOakLeaves.blockID) {
				world.setBlockAndMetadata(i, j, k, AetherBlocks.Log.blockID, 2);
			}
		}

		return true;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		if(world.getBlockId(i, j - 1, k) != AetherBlocks.Grass.blockID && world.getBlockId(i, j - 1, k) != AetherBlocks.Dirt.blockID) {
			return false;
		} else {
			int height = random.nextInt(5) + 6;

			int n;
			for(n = i - 3; n < i + 4; ++n) {
				for(int y = j + 5; y < j + 12; ++y) {
					for(int z = k - 3; z < k + 4; ++z) {
						if((n - i) * (n - i) + (y - j - 8) * (y - j - 8) + (z - k) * (z - k) < 12 + random.nextInt(5) && world.getBlockId(n, y, z) == 0) {
							world.setBlock(n, y, z, AetherBlocks.GoldenOakLeaves.blockID);
						}
					}
				}
			}

			for(n = 0; n < height; ++n) {
				if(n > 4 && random.nextInt(3) > 0) {
					this.branch(world, random, i, j + n, k, n / 4 - 1);
				}

				world.setBlockAndMetadata(i, j + n, k, AetherBlocks.Log.blockID, 2);
			}

			return true;
		}
	}
}

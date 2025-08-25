package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.World;

public class AetherGenClouds extends WorldGenerator {
	private int cloudblockID;
	private int meta;
	private int numberOfBlocks;
	private boolean flat;

	public AetherGenClouds(int i, int meta, int j, boolean flag) {
		this.cloudblockID = i;
		this.meta = meta;
		this.numberOfBlocks = j;
		this.flat = flag;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		int x = i;
		int y = j;
		int z = k;
		int xTendency = random.nextInt(3) - 1;
		int zTendency = random.nextInt(3) - 1;

		for(int n = 0; n < this.numberOfBlocks; ++n) {
			x += random.nextInt(3) - 1 + xTendency;
			if(random.nextBoolean() && !this.flat || this.flat && random.nextInt(10) == 0) {
				y += random.nextInt(3) - 1;
			}

			z += random.nextInt(3) - 1 + zTendency;

			for(int x1 = x; x1 < x + random.nextInt(4) + 3 * (this.flat ? 3 : 1); ++x1) {
				for(int y1 = y; y1 < y + random.nextInt(1) + 2; ++y1) {
					for(int z1 = z; z1 < z + random.nextInt(4) + 3 * (this.flat ? 3 : 1); ++z1) {
						if(world.getblockID(x1, y1, z1) == 0 && Math.abs(x1 - x) + Math.abs(y1 - y) + Math.abs(z1 - z) < 4 * (this.flat ? 3 : 1) + random.nextInt(2)) {
							world.setBlockAndMetadataWithNotify(x1, y1, z1, this.cloudblockID, this.meta);
						}
					}
				}
			}
		}

		return true;
	}
}

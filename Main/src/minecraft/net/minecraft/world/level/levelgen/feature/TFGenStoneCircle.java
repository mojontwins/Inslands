package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class TFGenStoneCircle extends TFGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		this.worldObj = world;

		if(!isAreaMostlyClear(world, rand, x - 3, y, z - 3, 7, 4, 7, 80)) return false;
		
		int cy;
		/*
		for(cy = -3; cy <= 3; ++cy) {
			for(int cz = -3; cz <= 3; ++cz) {
				for(int cy1 = 0; cy1 <= 4; ++cy1) {
					if(!world.getBlockMaterial(x + cy, y - 1, z + cz).isSolid()) {
						return false;
					}

					if(!world.isAirBlock(x + cy, y + cy1, z + cz)) {
						return false;
					}
				}
			}
		}
		*/

		for(cy = 0; cy <= 2; ++cy) {
			this.putBlock(x - 3, y + cy, z, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 3, y + cy, z, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x, y + cy, z - 3, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x, y + cy, z + 3, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x - 2, y + cy, z - 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 2, y + cy, z - 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x - 2, y + cy, z + 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 2, y + cy, z + 2, Block.cobblestoneMossy.blockID, true);
		}

		return true;
	}
}

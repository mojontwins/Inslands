package com.benimatic.twilightforest;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class TFGenFoundation extends TFGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		this.worldObj = world;
		int sx = 5 + rand.nextInt(5);
		int sz = 5 + rand.nextInt(5);
		if(!this.isAreaMostlyClear(world, rand, x, y, z, sx, 4, sz, 80)) {
			return false;
		} else {
			for(int cx = 0; cx <= sx; ++cx) {
				for(int cz = 0; cz <= sz; ++cz) {
					if(cx != 0 && cx != sx && cz != 0 && cz != sz) {
						if(rand.nextInt(3) != 0) {
							this.putBlock(x + cx, y - 1, z + cz, (byte)Block.planks.blockID, true);
						}
					} else {
						int ht = rand.nextInt(4) + 1;

						for(int cy = 0; cy <= ht; ++cy) {
							this.putBlock(x + cx, y + cy - 1, z + cz, this.randStone(rand, cy + 1), true);
						}
					}
				}
			}

			return true;
		}
	}
}

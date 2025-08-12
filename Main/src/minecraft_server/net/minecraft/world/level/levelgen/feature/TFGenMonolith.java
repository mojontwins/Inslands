package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class TFGenMonolith extends TFGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		this.worldObj = world;
		int ht = rand.nextInt(10) + 10;
		int dir = rand.nextInt(4);
		if(!this.isAreaMostlyClear(world, rand, x, y, z, 2, ht, 2, 90)) {
			return false;
		} else {
			int h0;
			int h1;
			int h2;
			int h3;
			switch(dir) {
			case 0:
				h0 = ht;
				h1 = (int)((double)ht * 0.75D);
				h2 = (int)((double)ht * 0.75D);
				h3 = (int)((double)ht * 0.5D);
				break;
			case 1:
				h0 = (int)((double)ht * 0.5D);
				h1 = ht;
				h2 = (int)((double)ht * 0.75D);
				h3 = (int)((double)ht * 0.75D);
				break;
			case 2:
				h0 = (int)((double)ht * 0.75D);
				h1 = (int)((double)ht * 0.5D);
				h2 = ht;
				h3 = (int)((double)ht * 0.75D);
				break;
			case 3:
			default:
				h0 = (int)((double)ht * 0.75D);
				h1 = (int)((double)ht * 0.75D);
				h2 = (int)((double)ht * 0.5D);
				h3 = ht;
			}

			int cy;
			for(cy = 0; cy <= h0; ++cy) {
				this.putBlock(x + 0, y + cy - 1, z + 0, cy == ht ? (byte)Block.blockLapis.blockID : (byte)Block.obsidian.blockID, true);
			}

			for(cy = 0; cy <= h1; ++cy) {
				this.putBlock(x + 1, y + cy - 1, z + 0, cy == ht ? (byte)Block.blockLapis.blockID : (byte)Block.obsidian.blockID, true);
			}

			for(cy = 0; cy <= h2; ++cy) {
				this.putBlock(x + 0, y + cy - 1, z + 1, cy == ht ? (byte)Block.blockLapis.blockID : (byte)Block.obsidian.blockID, true);
			}

			for(cy = 0; cy <= h3; ++cy) {
				this.putBlock(x + 1, y + cy - 1, z + 1, cy == ht ? (byte)Block.blockLapis.blockID : (byte)Block.obsidian.blockID, true);
			}

			return true;
		}
	}
}

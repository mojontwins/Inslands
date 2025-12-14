package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.world.level.World;

public class WorldGenRuinedHouse extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, int x0, int y0, int z0) {
		int width = 5 + rand.nextInt(5); if ((width & 1) == 0) width ++;
		int length = 5 + rand.nextInt(5); if ((length & 1) == 0) length ++;
		
		// Base height
		int h = 0;
		int min = 128; int max = 0;
		for(int x = x0; x < x0 + width; x ++) {
			for(int z = z0; z < z0 + length; z ++) {
				int height = world.getLandSurfaceHeightValue(x, z);
				if (height < 64) {
					return false;
				}
				
				if (height > max) max = height;
				if (height < min) min = height;
				h += height;
			}
		}
		h = (int) ((float)h / (float)(width * length));
		
		// Too much variation: fail
		if (Math.abs(max - min) > 8) {
			return false;
		}
		
		// Carve / raise & floor
		for(int x = x0; x < x0 + width; x ++) {
			for(int z = z0; z < z0 + length; z ++) {
				int height = world.getLandSurfaceHeightValue(x, z);
				if (height > h) for (int y = h; y < height; y ++) world.setBlock(x, y, z, 0);
				if (height < h) for (int y = height; y < h; y ++) world.setBlock(x, y, z, Block.gravel.blockID);
				
				world.setBlock(x, h, z, Block.planks.blockID);
			}
		}
		
		// Broken walls
		for(int x = x0; x < x0 + width; x ++) {
			for(int z = z0; z < z0 + length; z ++) {
				if ((x == x0 || x == x0 + width - 1) || (z == z0 || z == z0 + length - 1)) {
					//if(((x - x0) & 1) == 1 || ((z - z0) & 1) == 1) {
						int hh = h + 1 + rand.nextInt (4);
						for(int y = h; y < hh; y ++) world.setBlock(x, y, z, Block.cobblestone.blockID);
					//}
				}
			}
		}
		
		// Windows
		for(int x = x0 + 1; x < x0 + width - 1; x ++) {
			if(((x - x0) & 1) == 1) {
				for(int z = z0; z <= z0 + length - 1; z += (length - 1)) {
					if(world.isAirBlock(x, h + 3, z) ||
							!world.isAirBlock(x - 1, h + 3, z) ||
							!world.isAirBlock(x + 1, h + 3, z)
					) {
						world.setBlock(x, h + 2, z, 0);
					}
				}
			}
		}
		
		for(int z = z0 + 1; z < z0 + length - 1; z ++) {
			if(((z - z0) & 1) == 1) {
				for(int x = x0; x <= x0 + width - 1; x += (width - 1)) {
					if(world.isAirBlock(x, h + 3, z) ||
							!world.isAirBlock(x, h + 3, z - 1) ||
							!world.isAirBlock(x, h + 3, z + 1)
					) {
						world.setBlock(x, h + 2, z, 0);
					}
				}
			}
		}
		
		// Door
		int w = rand.nextInt(4);
		int x = x0, z = z0;
		switch(w) {
		case 0:
		case 2:
			x = w == 0 ? x0 : x0 + width - 1;
			z = x0 + 1 + rand.nextInt(length - 2);
			break;
		case 1:
		case 3:
			x = x0 + 1 + rand.nextInt(width- 2);
			z = w == 0 ? z0 : z0 + length - 1;
			break;
		}
		
		world.setBlock(x, h + 1, z, 0);
		world.setBlock(x, h + 2, z, 0);
		
		System.out.println("Ruin success @ " + x + " " + h + " " + z);
		
		return true;
	}

}

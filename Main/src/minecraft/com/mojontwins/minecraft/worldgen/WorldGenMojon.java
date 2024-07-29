package com.mojontwins.minecraft.worldgen;

import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public abstract class WorldGenMojon extends WorldGenerator {
	boolean withNotify = false;
	
	public WorldGenMojon(boolean withNotify) {
		super();
	}
	
	public void setBlockWithMetadata(World world, int x, int y, int z, int blockID, int meta) {
		if (this.withNotify) {
			world.setBlockAndMetadataWithNotify(x, y, z, blockID, meta);
		} else {
			world.setBlockAndMetadata(x, y, z, blockID, meta);
		}
	}

	public void roundedShape(World world, int x0, int y0, int z0, int r, int blockID, int meta, boolean withBottomHole) {
		int x1 = x0 - r, x2 = x0 + r;
		int y1 = y0 - r, y2 = y0 + r;
		int z1 = z0 - r, z2 = z0 + r;
		
		int bottom = y1; if (withBottomHole) bottom ++;
		
		for(int x = x1; x <= x2; x ++) {
			for(int y = bottom; y <= y2; y ++) {
				for(int z = z1; z <= z2; z ++) {
					if(
							(x == x1 || x == x2 || z == z1 || z == z2 || y == y1 || y == y2) &&
							!(x == x1 && z == z1 && y == y1) &&
							!(x == x2 && z == z1 && y == y1) &&
							!(x == x1 && z == z2 && y == y1) &&
							!(x == x2 && z == z2 && y == y1) &&
							!(x == x1 && z == z1 && y == y2) &&
							!(x == x2 && z == z1 && y == y2) &&
							!(x == x1 && z == z2 && y == y2) &&
							!(x == x2 && z == z2 && y == y2)
					) {
						this.setBlockWithMetadata(world, x, y, z, blockID, meta);
					}
				}
			}
		}
	}
	
}

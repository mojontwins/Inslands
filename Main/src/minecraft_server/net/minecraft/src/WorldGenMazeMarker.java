package net.minecraft.src;

import java.util.Random;

public class WorldGenMazeMarker extends WorldGenerator {

	// If true, carve up until air (for floating / cave levels with no real landsurfaceHeightmap)
	boolean carver = false;
	
	public WorldGenMazeMarker(boolean carver) {
		this.carver = carver;
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {

		if (this.carver) {
			while (world.isBlockOpaqueCube(x, y, z)) y ++;
		}
		
		for(int i = 0; i < 5; i ++) {
			world.setBlock(x, y++, z, Block.stoneBricks.blockID);
		}
		world.setBlock(x, y++, z, Block.blockLapis.blockID);
		world.setBlock(x, y, z, Block.shinyGlass.blockID);
		
		return false;
	}

}

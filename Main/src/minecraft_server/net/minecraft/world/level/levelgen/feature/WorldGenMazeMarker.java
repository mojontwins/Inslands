package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

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

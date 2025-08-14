package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.tile.Block;

public class WorldGenCypress extends WorldGenerator {

	// Code adapted from Enhanced Biomes mod
	// so it works in a1.2.6 with vanilla blocks!
	// https://github.com/SMEZ1234/EnhancedBiomes/
	
	private static final int leavesId = Block.leaves.blockID;
	private static final int trunkId = Block.wood.blockID;
	
	private int height;
	
	public WorldGenCypress (int height) {
		this.height = height;
	}
	
	public void setBlockIfEmpty (World world, int x, int y, int z, int blockID) {
		if (0 == world.getBlockId(x, y, z)) world.setBlock(x, y, z, blockID);
	}
	
	public void setBlockIfEmpty (World world, int x, int y, int z, int blockID, int meta) {
		if (0 == world.getBlockId(x, y, z)) world.setBlockAndMetadata(x, y, z, blockID, meta);
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if (y + height > 126) return false;
		
		Block soilBlock = Block.blocksList[world.getBlockId(x, y - 1, z)];
		if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;

		for(int i = 0; i < this.height * 2; i++) {
			for(int k = 0; k < 5; k++) {
				for(int j = 0; j < 5; j++) {
					if(world.getBlockId(x - 2 + k, y + 3 + i, z - 2 + j) != 0) {
						return false;
					}
				}
			}
		}

		for(int xx = 0; xx < height; xx++) {
			int largeLayer = xx * 2 + 3;
			int smallLayer = xx * 2 + 4;

			//Large Layer
			for(int yy = -2; yy < 3; yy++) {
				for(int zz = -2; zz < 3; zz++) {
					if(!((yy == -2 || yy == 2) && (zz == -2 || zz == 2))) {
						setBlockIfEmpty(world, x + yy, y + largeLayer, z + zz, leavesId);
					}
				}
			}

			//Small Layer
			for(int yy = -1; yy < 2; yy++) {
				for(int zz = -1; zz < 2; zz++) {
					if(!((yy == -1 || yy == 1) && (zz == -1 || zz == 1))) {
						setBlockIfEmpty(world, x + yy, y + smallLayer, z + zz, leavesId);
					}
				}
			}
		}

		for(int i = 0; i < (height * 2) + 3; i++) {
			world.setBlock(x, y + i, z, trunkId);
		}

		setBlockIfEmpty(world, x, y + (this.height * 2) + 3, z, leavesId);

		return true;
	}

}

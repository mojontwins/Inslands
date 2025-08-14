package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.tile.Block;

public class WorldGenBaobab extends WorldGenerator {

	// Code adapted from Enhanced Biomes mod
	// so it works in a1.2.6 with vanilla blocks!
	// https://github.com/SMEZ1234/EnhancedBiomes/

	private static final int leavesId = Block.leaves.blockID;
	private static final int trunkId = Block.wood.blockID;
	
	private int height;
	
	public WorldGenBaobab (int height) {
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

		soilBlock = Block.blocksList[world.getBlockId(x, y - 1, z + 1)];
		if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;

		soilBlock = Block.blocksList[world.getBlockId(x + 1, y - 1, z)];
		if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;
		
		soilBlock = Block.blocksList[world.getBlockId(x + 1, y - 1, z + 1)];
		if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;

		for(int i = 0; i < height; i++) {
			if(
				world.getBlockId(x, y + i, z) != 0 || 
				world.getBlockId(x + 1, y + i, z) != 0 || 
				world.getBlockId(x, y + i, z + 1) != 0 || 
				world.getBlockId(x + 1, y + i, z + 1) != 0
			) {
				return false;
			}
		}

		//setBlockIfEmpty(world, x, y + height + 10, z, trunkId);

		for(int i = 0; i < height; i++) {
			world.setBlock(x, y + i, z, trunkId);
			world.setBlock(x + 1, y + i, z, trunkId);
			world.setBlock(x, y + i, z + 1, trunkId);
			world.setBlock(x + 1, y + i, z + 1, trunkId);
		}

		int b1 = rand.nextInt(2);
		world.setBlock(x - 1, y + height - 1, z + b1, trunkId);
		world.setBlock(x - 2, y + height - 1, z + b1, trunkId);
		world.setBlock(x - 3, y + height, z + b1, trunkId);
		setBlockIfEmpty(world, x - 3, y + height + 1, z + b1, leavesId);
		setBlockIfEmpty(world, x - 2, y + height + 1, z + b1, leavesId);
		setBlockIfEmpty(world, x - 4, y + height + 1, z + b1, leavesId);
		setBlockIfEmpty(world, x - 3, y + height + 1, z + b1 - 1, leavesId);
		setBlockIfEmpty(world, x - 3, y + height + 1, z + b1 + 1, leavesId);

		int b2 = rand.nextInt(2);
		world.setBlock(x + 2, y + height - 1, z + b2, trunkId);
		world.setBlock(x + 3, y + height - 1, z + b2, trunkId);
		world.setBlock(x + 4, y + height, z + b2, trunkId);
		setBlockIfEmpty(world, x + 4, y + height + 1, z + b2, leavesId);
		setBlockIfEmpty(world, x + 3, y + height + 1, z + b2, leavesId);
		setBlockIfEmpty(world, x + 5, y + height + 1, z + b2, leavesId);
		setBlockIfEmpty(world, x + 4, y + height + 1, z + b2 - 1, leavesId);
		setBlockIfEmpty(world, x + 4, y + height + 1, z + b2 + 1, leavesId);

		int b3 = rand.nextInt(2);
		world.setBlock(x + b3, y + height - 1, z - 1, trunkId);
		world.setBlock(x + b3, y + height - 1, z - 2, trunkId);
		world.setBlock(x + b3, y + height, z - 3, trunkId);
		setBlockIfEmpty(world, x + b3, y + height + 1, z - 3, leavesId);
		setBlockIfEmpty(world, x + b3, y + height + 1, z - 2, leavesId);
		setBlockIfEmpty(world, x + b3, y + height + 1, z - 4, leavesId);
		setBlockIfEmpty(world, x + b3 + 1, y + height + 1, z - 3, leavesId);
		setBlockIfEmpty(world, x + b3 - 1, y + height + 1, z - 3, leavesId);

		int b4 = rand.nextInt(2);
		world.setBlock(x + b4, y + height - 1, z + 2, trunkId);
		world.setBlock(x + b4, y + height - 1, z + 3, trunkId);
		world.setBlock(x + b4, y + height, z + 4, trunkId);
		setBlockIfEmpty(world, x + b4, y + height + 1, z + 4, leavesId);
		setBlockIfEmpty(world, x + b4, y + height + 1, z + 3, leavesId);
		setBlockIfEmpty(world, x + b4, y + height + 1, z + 5, leavesId);
		setBlockIfEmpty(world, x + b4 + 1, y + height + 1, z + 4, leavesId);
		setBlockIfEmpty(world, x + b4 - 1, y + height + 1, z + 4, leavesId);

		int x1;
		int y1;
		int z1;

		int x2;
		int y2;
		int z2;

		if(rand.nextInt(2) == 0) {
			world.setBlock(x + 1, y + height, z, trunkId);
			if(rand.nextInt(2) == 0) {
				world.setBlock(x + 2, y + height + 1, z, trunkId);
				x1 = x + 2;
				y1 = y + height + 2;
				z1 = z + 0;
			}
			else {
				world.setBlock(x + 1, y + height + 1, z - 1, trunkId);
				x1 = x + 1;
				y1 = y + height + 2;
				z1 = z - 1;
			}

			world.setBlock(x, y + height, z + 1, trunkId);
			if(rand.nextInt(2) == 0) {
				world.setBlock(x - 1, y + height + 1, z + 1, trunkId);
				x2 = x - 1;
				y2 = y + height + 2;
				z2 = z + 1;
			}
			else {
				world.setBlock(x, y + height + 1, z + 2, trunkId);
				x2 = x + 0;
				y2 = y + height + 2;
				z2 = z + 2;
			}
		}
		else {
			world.setBlock(x, y + height, z, trunkId);
			if(rand.nextInt(2) == 0) {
				world.setBlock(x - 1, y + height + 1, z, trunkId);
				x1 = x - 1;
				y1 = y + height + 2;
				z1 = z + 1;
			}
			else {
				world.setBlock(x, y + height + 1, z - 1, trunkId);
				x1 = x + 0;
				y1 = y + height + 2;
				z1 = z - 1;
			}

			world.setBlock(x + 1, y + height, z + 1, trunkId);
			if(rand.nextInt(2) == 0) {
				world.setBlock(x + 2, y + height + 1, z + 1, trunkId);
				x2 = x + 2;
				y2 = y + height + 2;
				z2 = z + 1;
			}
			else {
				world.setBlock(x + 1, y + height + 1, z + 2, trunkId);
				x2 = x + 1;
				y2 = y + height + 2;
				z2 = z + 2;
			}
		}

		setBlockIfEmpty(world, x1, y1, z1, leavesId);
		setBlockIfEmpty(world, x1 + 1, y1, z1, leavesId);
		setBlockIfEmpty(world, x1, y1, z1 + 1, leavesId);
		setBlockIfEmpty(world, x1 - 1, y1, z1, leavesId);
		setBlockIfEmpty(world, x1, y1, z1 - 1, leavesId);

		setBlockIfEmpty(world, x2, y2, z2, leavesId);
		setBlockIfEmpty(world, x2 + 1, y2, z2, leavesId);
		setBlockIfEmpty(world, x2, y2, z2 + 1, leavesId);
		setBlockIfEmpty(world, x2 - 1, y2, z2, leavesId);
		setBlockIfEmpty(world, x2, y2, z2 - 1, leavesId);

		return true;
	}

}

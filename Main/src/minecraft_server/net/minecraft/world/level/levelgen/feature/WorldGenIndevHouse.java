package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class WorldGenIndevHouse extends WorldGenerator {
	int wallID = Block.planks.blockID;
	
	public WorldGenIndevHouse() {		
	}
	
	public WorldGenIndevHouse(int wallID) {
		this.wallID = wallID;
	}
	
	public boolean generate(World world, Random rand, int x0, int y0, int z0) {
		// Main body
		for(int x = x0 - 3; x <= x0 + 3; ++x) {
			for(int y = y0 - 2; y <= y0 + 2; ++y) {
				for(int z = z0 - 3; z <= z0 + 3; ++z) {
					int blockID = 0;
					
					if(y == y0 - 2) {
						blockID = rand.nextBoolean() ? Block.cobblestoneMossy.blockID : Block.stone.blockID;
					} else if(x == x0 - 3 || x == x0 + 3 || z == z0 - 3 || z == z0 + 3 || y == y0 + 2) {
						blockID = this.wallID;
					}				
					
					world.setBlock(x, y, z, blockID);
				}
			}
		}
		
		// Door
		world.setBlock(x0, y0, z0 - 3, 0);
		world.setBlock(x0, y0 - 1, z0 - 3, 0);
		
		// Make room in front
		for(int x = x0 - 3; x <= x0 + 3; ++x) {
			for(int y = y0 - 2; y <= y0 + 2; ++y) {
				for(int z = z0 - 6; z <= z0 - 4; ++z) {
					world.setBlock(x, y, z, (y == y0 - 2) ? (rand.nextBoolean() ? Block.cobblestoneMossy.blockID : Block.stone.blockID) : 0);
				}
			}
		}
		
		// Raise floor
		for(int x = x0 - 3; x <= x0 + 3; ++x) {
			for(int z = z0 - 6; z <= z0 + 3; ++z) {
				int y = y0 - 3;
				while (y > 0 && notGroundOrStone(world.getblockID(x, y, z))) {
					world.setBlock(x, y, z, Block.cobblestone.blockID);
					y --;
				}
			}
		}     

		// Torches
		world.setBlockWithNotify(x0 - 3 + 1, y0, z0, Block.torchWood.blockID);
		world.setBlockWithNotify(x0 + 3 - 1, y0, z0, Block.torchWood.blockID);
		
		return true;
	}

	public boolean notGroundOrStone(int blockID) {
		return blockID != Block.stone.blockID && blockID != Block.dirt.blockID && blockID != Block.sand.blockID && blockID != Block.grass.blockID && blockID != Block.gravel.blockID;
	}
}

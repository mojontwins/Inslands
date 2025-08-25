package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class WorldGenLiquids extends WorldGenerator {
	private int liquidblockID;

	public WorldGenLiquids(int liquidblockID) {
		this.liquidblockID = liquidblockID;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(world.getblockID(x, y + 1, z) != Block.stone.blockID) {
			return false;
		} else if(world.getblockID(x, y - 1, z) != Block.stone.blockID) {
			return false;
		} else if(world.getblockID(x, y, z) != 0 && world.getblockID(x, y, z) != Block.stone.blockID) {
			return false;
		} else {
			int i6 = 0;
			if(world.getblockID(x - 1, y, z) == Block.stone.blockID) {
				++i6;
			}

			if(world.getblockID(x + 1, y, z) == Block.stone.blockID) {
				++i6;
			}

			if(world.getblockID(x, y, z - 1) == Block.stone.blockID) {
				++i6;
			}

			if(world.getblockID(x, y, z + 1) == Block.stone.blockID) {
				++i6;
			}

			int i7 = 0;
			if(world.isAirBlock(x - 1, y, z)) {
				++i7;
			}

			if(world.isAirBlock(x + 1, y, z)) {
				++i7;
			}

			if(world.isAirBlock(x, y, z - 1)) {
				++i7;
			}
			
			if(world.isAirBlock(x, y, z + 1)) {
				++i7;
			}

			if(i6 == 3 && i7 == 1) {
				world.setBlockWithNotify(x, y, z, this.liquidblockID);
				world.scheduledUpdatesAreImmediate = true;
				Block.blocksList[this.liquidblockID].updateTick(world, x, y, z, rand);
				world.scheduledUpdatesAreImmediate = false;
			}

			return true;
		}
	}
}

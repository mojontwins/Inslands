package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class WorldGenHollowLogs extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		// Select orientation & length
		int length = 3 + rand.nextInt(4);
		boolean axisX = rand.nextBoolean();
		
		for(int i = 0; i < length; i ++) {
			int xx = axisX ? x + i : x;
			int zz = axisX ? z : z + i;
			
			Block block = Block.blocksList[world.getblockID(xx, y, zz)];
			Block blockBeneath = Block.blocksList[world.getblockID(xx, y - 1, zz)];
			
			if((block == null || !block.isOpaqueCube()) && blockBeneath != null && blockBeneath.isOpaqueCube() && blockBeneath != Block.leaves) {
				world.setBlockAndMetadata(xx, y, zz, Block.hollowLog.blockID, axisX ? 4 : 2);
			} else break;
		}
		
		return false;
	}

}

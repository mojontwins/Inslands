package net.minecraft.src;

import java.util.Random;

public abstract class WorldGenerator {
	public abstract boolean generate(World world, Random rand, int x, int y, int z);

	public void setScale(double scaleX, double scaleY, double scaleZ) {
	}
	
	public boolean blockIsCube(World world, int x, int y, int z) {
		if(world.isBlockOpaqueCube(x, y, z)) return true;
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		return (block instanceof BlockIce) || (block instanceof BlockFluid);
	}
}

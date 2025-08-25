package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockFluid;
import net.minecraft.world.level.tile.BlockIce;

public abstract class WorldGenerator {
	public abstract boolean generate(World world, Random rand, int x, int y, int z);

	public void setScale(double scaleX, double scaleY, double scaleZ) {
	}
	
	public boolean blockIsCube(World world, int x, int y, int z) {
		if(world.isBlockOpaqueCube(x, y, z)) return true;
		Block block = Block.blocksList[world.getblockID(x, y, z)];
		return (block instanceof BlockIce) || (block instanceof BlockFluid);
	}
}

package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class WorldGenLeafPile extends WorldGenerator {

	public WorldGenLeafPile() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(world.isUnderLeaves(x, y + 1, z) && Block.leafPile.canBlockStay(world, x, y, z)) {
			world.setBlock(x, y + 1, z, Block.leafPile.blockID);
			return true;
		}
		return false;
	}

}

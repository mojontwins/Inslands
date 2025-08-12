package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class WorldGenStreetLight extends WorldGenerator {
	private boolean broken;
	
	public WorldGenStreetLight(boolean broken) {
		this.broken = broken;
	}
	
	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		y--;
		world.setBlock(x, y ++, z, Block.stone.blockID);
		world.setBlock(x, y ++, z, Block.streetLanternFence.blockID);
		world.setBlock(x, y ++, z, Block.streetLanternFence.blockID);
		world.setBlock(x, y ++, z, Block.streetLanternFence.blockID);
		world.setBlock(x, y, z, this.broken ? Block.streetLanternBroken.blockID : Block.streetLantern.blockID);
		return true;
	}

}

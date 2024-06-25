package com.benimatic.twilightforest;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class TFGenOutsideStalagmite extends TFGenCaveStalactite {
	public TFGenOutsideStalagmite() {
		super((byte)Block.stone.blockID, 1.0D, false);
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		this.worldObj = world;
		int length = rand.nextInt(10) + 5;
		return !this.isAreaMostlyClear(world, rand, x, y, z, 1, length, 1, 90) ? false : this.makeSpike(rand, x, y - 1, z, length);
	}
}

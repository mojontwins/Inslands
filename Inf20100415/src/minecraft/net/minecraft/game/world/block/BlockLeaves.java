package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.material.Material;

public final class BlockLeaves extends BlockLeavesBase {
	protected BlockLeaves(int i1, int i2) {
		super(18, 52, Material.leaves, true);
		this.setTickOnLoad(true);
	}

	public final int quantityDropped(Random random1) {
		return random1.nextInt(10) == 0 ? 1 : 0;
	}

	public final int idDropped(int i1, Random random2) {
		return Block.sapling.blockID;
	}
}
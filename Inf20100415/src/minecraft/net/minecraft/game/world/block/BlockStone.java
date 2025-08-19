package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.material.Material;

public final class BlockStone extends Block {
	public BlockStone(int i1, int i2) {
		super(i1, i2, Material.rock);
	}

	public final int idDropped(int i1, Random random2) {
		return Block.cobblestone.blockID;
	}
}
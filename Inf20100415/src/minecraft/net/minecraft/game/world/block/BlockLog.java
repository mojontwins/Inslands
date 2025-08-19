package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.material.Material;

public final class BlockLog extends Block {
	protected BlockLog(int i1) {
		super(17, Material.wood);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(Random random1) {
		return 1;
	}

	public final int idDropped(int i1, Random random2) {
		return Block.wood.blockID;
	}

	public final int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? 21 : (i1 == 0 ? 21 : 20);
	}
}
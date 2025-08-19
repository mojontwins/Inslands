package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.material.Material;

public final class BlockBookshelf extends Block {
	public BlockBookshelf(int i1, int i2) {
		super(47, 35, Material.wood);
	}

	public final int getBlockTextureFromSide(int i1) {
		return i1 <= 1 ? 4 : this.blockIndexInTexture;
	}

	public final int quantityDropped(Random random1) {
		return 0;
	}
}
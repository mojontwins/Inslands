package net.minecraft.game.world.block;

import net.minecraft.game.world.material.Material;

public final class BlockOreStorage extends Block {
	public BlockOreStorage(int i1, int i2) {
		super(i1, Material.iron);
		this.blockIndexInTexture = i2;
	}

	public final int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? this.blockIndexInTexture - 16 : (i1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
}
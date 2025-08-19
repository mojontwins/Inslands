package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.item.Item;

public final class BlockGravel extends BlockSand {
	public BlockGravel(int i1, int i2) {
		super(13, 19);
	}

	public final int idDropped(int i1, Random random2) {
		return random2.nextInt(10) == 0 ? Item.flint.shiftedIndex : this.blockID;
	}
}
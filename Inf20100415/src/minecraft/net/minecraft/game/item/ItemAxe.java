package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;

public final class ItemAxe extends ItemTool {
	private static Block[] damageReduceAmount = new Block[]{Block.planks, Block.bookshelf, Block.wood, Block.chest};

	public ItemAxe(int i1, int i2) {
		super(i1, 3, i2, damageReduceAmount);
	}
}
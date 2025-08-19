package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;

public final class ItemSpade extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel};

	public ItemSpade(int i1, int i2) {
		super(i1, 1, i2, blocksEffectiveAgainst);
	}
}
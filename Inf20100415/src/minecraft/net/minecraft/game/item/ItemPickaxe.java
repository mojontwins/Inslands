package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

public final class ItemPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{Block.cobblestone, Block.stairDouble, Block.stairSingle, Block.stone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond};
	private int harvestLevel;

	public ItemPickaxe(int i1, int i2) {
		super(i1, 2, i2, blocksEffectiveAgainst);
		this.harvestLevel = i2;
	}

	public final boolean canHarvestBlock(Block block1) {
		return block1 == Block.obsidian ? this.harvestLevel == 3 : (block1 != Block.blockDiamond && block1 != Block.oreDiamond ? (block1 != Block.blockGold && block1 != Block.oreGold ? (block1 != Block.blockSteel && block1 != Block.oreIron ? (block1.blockMaterial == Material.rock ? true : block1.blockMaterial == Material.iron) : this.harvestLevel > 0) : this.harvestLevel >= 2) : this.harvestLevel >= 2);
	}
}
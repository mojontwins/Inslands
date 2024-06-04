package net.minecraft.src;

import java.util.Random;

public class ItemSkyrootPickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Holystone, AetherBlocks.AmbrosiumOre, AetherBlocks.Freezer, AetherBlocks.QuicksoilGlass, AetherBlocks.Incubator};
	private static Random random = new Random();

	protected ItemSkyrootPickaxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 2, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public boolean canHarvestBlock(Block block) {
		for(int i = 0; i < blocksEffectiveAgainst.length; ++i) {
			if(blocksEffectiveAgainst[i].blockID == block.blockID) {
				return true;
			}
		}

		return false;
	}

	public ToolBase getToolBase() {
		return ToolBase.Pickaxe;
	}
}

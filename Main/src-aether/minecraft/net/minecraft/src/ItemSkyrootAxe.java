package net.minecraft.src;

import java.util.Random;

public class ItemSkyrootAxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Plank, AetherBlocks.Log};
	private static Random random = new Random();

	protected ItemSkyrootAxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 3, enumtoolmaterial, blocksEffectiveAgainst);
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
		return ToolBase.Axe;
	}
}

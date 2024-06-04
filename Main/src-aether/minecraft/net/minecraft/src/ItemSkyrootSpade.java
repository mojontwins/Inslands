package net.minecraft.src;

import java.util.Random;

public class ItemSkyrootSpade extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Quicksoil, AetherBlocks.Dirt, AetherBlocks.Grass, AetherBlocks.Aercloud};
	private static Random random = new Random();

	public ItemSkyrootSpade(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 1, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public ToolBase getToolBase() {
		return ToolBase.Shovel;
	}

	public boolean canHarvestBlock(Block block) {
		for(int i = 0; i < blocksEffectiveAgainst.length; ++i) {
			if(blocksEffectiveAgainst[i].blockID == block.blockID) {
				return true;
			}
		}

		return false;
	}
}

package net.minecraft.src;

import java.util.Random;

public class ItemValkyrieAxe extends ItemTool implements IReach {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Plank, AetherBlocks.Log};
	private static Random random = new Random();

	protected ItemValkyrieAxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 3, enumtoolmaterial, blocksEffectiveAgainst);
		SAPI.reachAdd(this);
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

	public boolean reachItemMatches(ItemStack itemstack) {
		return itemstack == null ? false : itemstack.itemID == AetherItems.AxeValkyrie.shiftedIndex;
	}

	public float getReach(ItemStack itemstack) {
		return 10.0F;
	}
}

package net.minecraft.src;

import java.util.Random;

public class ItemValkyrieSpade extends ItemTool implements IReach {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Quicksoil, AetherBlocks.Dirt, AetherBlocks.Grass, AetherBlocks.Aercloud};
	private static Random random = new Random();

	public ItemValkyrieSpade(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 1, enumtoolmaterial, blocksEffectiveAgainst);
		SAPI.reachAdd(this);
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

	public boolean reachItemMatches(ItemStack itemstack) {
		return itemstack == null ? false : itemstack.itemID == AetherItems.ShovelValkyrie.shiftedIndex;
	}

	public float getReach(ItemStack itemstack) {
		return 10.0F;
	}
}

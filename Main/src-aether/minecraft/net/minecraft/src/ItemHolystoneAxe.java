package net.minecraft.src;

import java.util.Random;

public class ItemHolystoneAxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Plank, AetherBlocks.Log};
	private static Random random = new Random();

	protected ItemHolystoneAxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 3, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
		if(random.nextInt(50) == 0) {
			entityliving.dropItemWithOffset(AetherItems.AmbrosiumShard.shiftedIndex, 1, 0.0F);
		}

		return super.onBlockDestroyed(itemstack, i, j, k, l, entityliving);
	}

	public ToolBase getToolBase() {
		return ToolBase.Axe;
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

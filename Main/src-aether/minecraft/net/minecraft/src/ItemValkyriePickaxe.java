package net.minecraft.src;

import java.util.Random;

public class ItemValkyriePickaxe extends ItemTool implements IReach {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Holystone, AetherBlocks.Icestone, AetherBlocks.EnchantedGravitite, AetherBlocks.GravititeOre, AetherBlocks.ZaniteOre, AetherBlocks.AmbrosiumOre, AetherBlocks.LightDungeonStone, AetherBlocks.DungeonStone, AetherBlocks.Pillar, AetherBlocks.Aerogel, AetherBlocks.Enchanter, AetherBlocks.Incubator, AetherBlocks.ZaniteBlock, AetherBlocks.Freezer, AetherBlocks.QuicksoilGlass};
	private static Random random = new Random();

	protected ItemValkyriePickaxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 2, enumtoolmaterial, blocksEffectiveAgainst);
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
		return ToolBase.Pickaxe;
	}

	public boolean reachItemMatches(ItemStack itemstack) {
		return itemstack == null ? false : itemstack.itemID == AetherItems.PickValkyrie.shiftedIndex;
	}

	public float getReach(ItemStack itemstack) {
		return 10.0F;
	}
}

package net.minecraft.src;

public class ItemZanitePickaxe extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Holystone, AetherBlocks.Icestone, AetherBlocks.EnchantedGravitite, AetherBlocks.GravititeOre, AetherBlocks.ZaniteOre, AetherBlocks.AmbrosiumOre, AetherBlocks.LightDungeonStone, AetherBlocks.DungeonStone, AetherBlocks.Pillar, AetherBlocks.Enchanter, AetherBlocks.Incubator, AetherBlocks.ZaniteBlock, AetherBlocks.Freezer, AetherBlocks.QuicksoilGlass};

	protected ItemZanitePickaxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 2, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return super.getStrVsBlock(itemstack, block) * (2.0F * (float)itemstack.getItemDamage() / (float)itemstack.getItem().getMaxDamage() + 0.5F);
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

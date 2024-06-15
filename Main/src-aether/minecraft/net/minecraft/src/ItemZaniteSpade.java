package net.minecraft.src;

public class ItemZaniteSpade extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Quicksoil, AetherBlocks.Dirt, AetherBlocks.Grass, AetherBlocks.Aercloud};

	public ItemZaniteSpade(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 1, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return super.getStrVsBlock(itemstack, block) * ((float)(itemstack.getItemDamage() / itemstack.getItem().getMaxDamage()) + 0.5F);
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

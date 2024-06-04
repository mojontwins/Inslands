package net.minecraft.src;

public class ItemSwordZanite extends ItemSword {
	public ItemSwordZanite(int itemID, EnumToolMaterial mat) {
		super(itemID, mat);
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return super.getStrVsBlock(itemstack, block) * (2.0F * (float)itemstack.getItemDamage() / (float)itemstack.getItem().getMaxDamage() + 0.5F);
	}
}

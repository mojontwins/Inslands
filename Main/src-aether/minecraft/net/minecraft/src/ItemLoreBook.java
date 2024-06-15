package net.minecraft.src;

public class ItemLoreBook extends Item {
	public ItemLoreBook(int i) {
		super(i);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public int getColorFromDamage(int i) {
		return i == 0 ? 8388479 : (i == 1 ? 16744319 : 8355839);
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		ModLoader.OpenGUI(entityplayer, new GuiLore(entityplayer.inventory, itemstack.getItemDamage()));
		return itemstack;
	}

	public String getItemNameIS(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		if(i > 2) {
			i = 2;
		}

		return super.getItemName() + "." + i;
	}
}

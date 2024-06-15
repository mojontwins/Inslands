package net.minecraft.src;

public class ItemLifeShard extends Item {
	public ItemLifeShard(int i) {
		super(i);
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		--itemstack.stackSize;
		mod_Aether.getPlayer(entityplayer).increaseMaxHP(2);
		return itemstack;
	}
}

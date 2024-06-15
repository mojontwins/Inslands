package net.minecraft.src;

public class ItemAmbrosium extends Item {
	private int healAmount;

	public ItemAmbrosium(int i, int j) {
		super(i);
		this.healAmount = j;
		this.maxStackSize = 64;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		--itemstack.stackSize;
		entityplayer.heal(this.healAmount);
		return itemstack;
	}

	public int getHealAmount() {
		return this.healAmount;
	}
}

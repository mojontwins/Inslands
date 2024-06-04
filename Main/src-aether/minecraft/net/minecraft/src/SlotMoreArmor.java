package net.minecraft.src;

class SlotMoreArmor extends Slot {
	final int armorType;
	final ContainerAether inventory;

	SlotMoreArmor(ContainerAether containerplayer, IInventory iinventory, int i, int j, int k, int l) {
		super(iinventory, i, j, k);
		this.inventory = containerplayer;
		this.armorType = l;
	}

	public int getSlotStackLimit() {
		return 1;
	}

	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.getItem() instanceof ItemMoreArmor ? ((ItemMoreArmor)itemstack.getItem()).isTypeValid(this.armorType) : false;
	}
}

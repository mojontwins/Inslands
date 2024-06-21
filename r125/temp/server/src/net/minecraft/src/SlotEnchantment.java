package net.minecraft.src;

class SlotEnchantment extends Slot {
	final ContainerEnchantment container;

	SlotEnchantment(ContainerEnchantment containerEnchantment1, IInventory iInventory2, int i3, int i4, int i5) {
		super(iInventory2, i3, i4, i5);
		this.container = containerEnchantment1;
	}

	public boolean isItemValid(ItemStack itemStack1) {
		return true;
	}
}

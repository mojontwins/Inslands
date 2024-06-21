package net.minecraft.src;

class SlotBrewingStandIngredient extends Slot {
	final ContainerBrewingStand container;

	public SlotBrewingStandIngredient(ContainerBrewingStand containerBrewingStand1, IInventory iInventory2, int i3, int i4, int i5) {
		super(iInventory2, i3, i4, i5);
		this.container = containerBrewingStand1;
	}

	public boolean isItemValid(ItemStack itemStack1) {
		return itemStack1 != null ? Item.itemsList[itemStack1.itemID].isPotionIngredient() : false;
	}

	public int getSlotStackLimit() {
		return 64;
	}
}

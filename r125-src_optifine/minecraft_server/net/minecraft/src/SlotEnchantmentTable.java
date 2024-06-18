package net.minecraft.src;

class SlotEnchantmentTable extends InventoryBasic {
	final ContainerEnchantment container;

	SlotEnchantmentTable(ContainerEnchantment containerEnchantment1, String string2, int i3) {
		super(string2, i3);
		this.container = containerEnchantment1;
	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public void onInventoryChanged() {
		super.onInventoryChanged();
		this.container.onCraftMatrixChanged(this);
	}
}

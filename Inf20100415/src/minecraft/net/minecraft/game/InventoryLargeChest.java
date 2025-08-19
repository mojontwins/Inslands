package net.minecraft.game;

import net.minecraft.game.item.ItemStack;

public final class InventoryLargeChest implements IInventory {
	private String name;
	private IInventory upperChest;
	private IInventory lowerChest;

	public InventoryLargeChest(String string1, IInventory iInventory2, IInventory iInventory3) {
		this.name = string1;
		this.upperChest = iInventory2;
		this.lowerChest = iInventory3;
	}

	public final int getInventorySize() {
		return this.upperChest.getInventorySize() + this.lowerChest.getInventorySize();
	}

	public final String getInvName() {
		return this.name;
	}

	public final ItemStack getStackInSlot(int i1) {
		return i1 >= this.upperChest.getInventorySize() ? this.lowerChest.getStackInSlot(i1 - this.upperChest.getInventorySize()) : this.upperChest.getStackInSlot(i1);
	}

	public final ItemStack decrStackSize(int i1, int i2) {
		return i1 >= this.upperChest.getInventorySize() ? this.lowerChest.decrStackSize(i1 - this.upperChest.getInventorySize(), i2) : this.upperChest.decrStackSize(i1, i2);
	}

	public final void setInventorySlotContents(int i1, ItemStack itemStack2) {
		if(i1 >= this.upperChest.getInventorySize()) {
			this.lowerChest.setInventorySlotContents(i1 - this.upperChest.getInventorySize(), itemStack2);
		} else {
			this.upperChest.setInventorySlotContents(i1, itemStack2);
		}
	}

	public final int getInventoryStackLimit() {
		return this.upperChest.getInventoryStackLimit();
	}

	public final void onInventoryChanged() {
		this.upperChest.onInventoryChanged();
		this.lowerChest.onInventoryChanged();
	}
}
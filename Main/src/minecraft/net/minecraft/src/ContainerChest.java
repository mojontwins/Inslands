package net.minecraft.src;

import net.minecraft.world.entity.player.EntityPlayer;

public class ContainerChest extends Container {
	private IInventory lowerChestInventory;
	private int numRows;

	public ContainerChest(IInventory iInventory1, IInventory iInventory2) {
		this.lowerChestInventory = iInventory2;
		this.numRows = iInventory2.getSizeInventory() / 9;
		int i3 = (this.numRows - 4) * 18;

		int i4;
		int i5;
		for(i4 = 0; i4 < this.numRows; ++i4) {
			for(i5 = 0; i5 < 9; ++i5) {
				this.addSlot(new Slot(iInventory2, i5 + i4 * 9, 8 + i5 * 18, 18 + i4 * 18));
			}
		}

		for(i4 = 0; i4 < 3; ++i4) {
			for(i5 = 0; i5 < 9; ++i5) {
				this.addSlot(new Slot(iInventory1, i5 + i4 * 9 + 9, 8 + i5 * 18, 103 + i4 * 18 + i3));
			}
		}

		for(i4 = 0; i4 < 9; ++i4) {
			this.addSlot(new Slot(iInventory1, i4, 8 + i4 * 18, 161 + i3));
		}

	}

	public boolean canInteractWith(EntityPlayer entityPlayer1) {
		return this.lowerChestInventory.canInteractWith(entityPlayer1);
	}

	public ItemStack getStackInSlot(int i1) {
		ItemStack itemStack2 = null;
		Slot slot3 = (Slot)this.inventorySlots.get(i1);
		if(slot3 != null && slot3.getHasStack()) {
			ItemStack itemStack4 = slot3.getStack();
			itemStack2 = itemStack4.copy();
			if(i1 < this.numRows * 9) {
				this.mergeItemStack(itemStack4, this.numRows * 9, this.inventorySlots.size(), true);
			} else {
				this.mergeItemStack(itemStack4, 0, this.numRows * 9, false);
			}

			if(itemStack4.stackSize == 0) {
				slot3.putStack((ItemStack)null);
			} else {
				slot3.onSlotChanged();
			}
		}

		return itemStack2;
	}
}

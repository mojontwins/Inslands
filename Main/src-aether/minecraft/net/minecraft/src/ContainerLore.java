package net.minecraft.src;

public class ContainerLore extends Container {
	public IInventory loreSlot = new InventoryBasic("Lore Item", 1);

	public ContainerLore(InventoryPlayer inventoryplayer) {
		this.addSlot(new Slot(this.loreSlot, 0, 82, 66));

		int j1;
		for(j1 = 0; j1 < 3; ++j1) {
			for(int l1 = 0; l1 < 9; ++l1) {
				this.addSlot(new Slot(inventoryplayer, l1 + j1 * 9 + 9, 48 + l1 * 18, 113 + j1 * 18));
			}
		}

		for(j1 = 0; j1 < 9; ++j1) {
			this.addSlot(new Slot(inventoryplayer, j1, 48 + j1 * 18, 171));
		}

	}

	protected void func_28125_a(ItemStack itemstack, int i, int j, boolean flag) {
	}

	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		super.onCraftGuiClosed(entityplayer);
		ItemStack itemstack = this.loreSlot.getStackInSlot(0);
		if(itemstack != null) {
			entityplayer.dropPlayerItem(itemstack);
		}

	}

	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	public ItemStack getStackInSlot(int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.slots.get(i);
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(i == 0) {
				this.func_28125_a(itemstack1, 10, 46, true);
			} else if(i >= 10 && i < 37) {
				this.func_28125_a(itemstack1, 37, 46, false);
			} else if(i >= 37 && i < 46) {
				this.func_28125_a(itemstack1, 10, 37, false);
			} else {
				this.func_28125_a(itemstack1, 10, 46, false);
			}

			if(itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			} else {
				slot.onSlotChanged();
			}

			if(itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(itemstack1);
		}

		return itemstack;
	}
}

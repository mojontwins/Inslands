package net.minecraft.src;

public class ContainerIncubator extends Container {
	private TileEntityIncubator Incubator;
	private int cookTime = 0;
	private int burnTime = 0;
	private int itemBurnTime = 0;

	public ContainerIncubator(InventoryPlayer inventoryplayer, TileEntityIncubator tileentityIncubator) {
		this.Incubator = tileentityIncubator;
		this.addSlot(new TileEntityIncubatorSlot(tileentityIncubator, 1, 73, 17));
		this.addSlot(new Slot(tileentityIncubator, 0, 73, 53));

		int j;
		for(j = 0; j < 3; ++j) {
			for(int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventoryplayer, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
			}
		}

		for(j = 0; j < 9; ++j) {
			this.addSlot(new Slot(inventoryplayer, j, 8 + j * 18, 142));
		}

	}

	protected void func_28125_a(ItemStack itemstack, int i, int j, boolean flag) {
	}

	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return this.Incubator.canInteractWith(entityplayer);
	}

	public ItemStack getStackInSlot(int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.slots.get(i);
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(i == 2) {
				this.func_28125_a(itemstack1, 3, 39, true);
			} else if(i >= 3 && i < 30) {
				this.func_28125_a(itemstack1, 30, 39, false);
			} else if(i >= 30 && i < 39) {
				this.func_28125_a(itemstack1, 3, 30, false);
			} else {
				this.func_28125_a(itemstack1, 3, 39, false);
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

package net.minecraft.src;

public class ContainerBrewingStand extends Container {
	private TileEntityBrewingStand tileBrewingStand;
	private int brewTime = 0;

	public ContainerBrewingStand(InventoryPlayer inventoryPlayer1, TileEntityBrewingStand tileEntityBrewingStand2) {
		this.tileBrewingStand = tileEntityBrewingStand2;
		this.addSlot(new SlotBrewingStandPotion(this, inventoryPlayer1.player, tileEntityBrewingStand2, 0, 56, 46));
		this.addSlot(new SlotBrewingStandPotion(this, inventoryPlayer1.player, tileEntityBrewingStand2, 1, 79, 53));
		this.addSlot(new SlotBrewingStandPotion(this, inventoryPlayer1.player, tileEntityBrewingStand2, 2, 102, 46));
		this.addSlot(new SlotBrewingStandIngredient(this, tileEntityBrewingStand2, 3, 79, 17));

		int i3;
		for(i3 = 0; i3 < 3; ++i3) {
			for(int i4 = 0; i4 < 9; ++i4) {
				this.addSlot(new Slot(inventoryPlayer1, i4 + i3 * 9 + 9, 8 + i4 * 18, 84 + i3 * 18));
			}
		}

		for(i3 = 0; i3 < 9; ++i3) {
			this.addSlot(new Slot(inventoryPlayer1, i3, 8 + i3 * 18, 142));
		}

	}

	public void onCraftGuiOpened(ICrafting iCrafting1) {
		super.onCraftGuiOpened(iCrafting1);
		iCrafting1.updateCraftingInventoryInfo(this, 0, this.tileBrewingStand.getBrewTime());
	}

	public void updateCraftingResults() {
		super.updateCraftingResults();

		for(int i1 = 0; i1 < this.crafters.size(); ++i1) {
			ICrafting iCrafting2 = (ICrafting)this.crafters.get(i1);
			if(this.brewTime != this.tileBrewingStand.getBrewTime()) {
				iCrafting2.updateCraftingInventoryInfo(this, 0, this.tileBrewingStand.getBrewTime());
			}
		}

		this.brewTime = this.tileBrewingStand.getBrewTime();
	}

	public boolean canInteractWith(EntityPlayer entityPlayer1) {
		return this.tileBrewingStand.isUseableByPlayer(entityPlayer1);
	}

	public ItemStack transferStackInSlot(int i1) {
		ItemStack itemStack2 = null;
		Slot slot3 = (Slot)this.inventorySlots.get(i1);
		if(slot3 != null && slot3.getHasStack()) {
			ItemStack itemStack4 = slot3.getStack();
			itemStack2 = itemStack4.copy();
			if((i1 < 0 || i1 > 2) && i1 != 3) {
				if(i1 >= 4 && i1 < 31) {
					if(!this.mergeItemStack(itemStack4, 31, 40, false)) {
						return null;
					}
				} else if(i1 >= 31 && i1 < 40) {
					if(!this.mergeItemStack(itemStack4, 4, 31, false)) {
						return null;
					}
				} else if(!this.mergeItemStack(itemStack4, 4, 40, false)) {
					return null;
				}
			} else {
				if(!this.mergeItemStack(itemStack4, 4, 40, true)) {
					return null;
				}

				slot3.func_48417_a(itemStack4, itemStack2);
			}

			if(itemStack4.stackSize == 0) {
				slot3.putStack((ItemStack)null);
			} else {
				slot3.onSlotChanged();
			}

			if(itemStack4.stackSize == itemStack2.stackSize) {
				return null;
			}

			slot3.onPickupFromSlot(itemStack4);
		}

		return itemStack2;
	}
}

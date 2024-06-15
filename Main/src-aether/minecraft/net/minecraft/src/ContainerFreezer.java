package net.minecraft.src;

public class ContainerFreezer extends Container {
	private TileEntityFreezer freezer;
	private int cookTime = 0;
	private int burnTime = 0;
	private int itemBurnTime = 0;

	public ContainerFreezer(InventoryPlayer inventoryplayer, TileEntityFreezer tileentityfreezer) {
		this.freezer = tileentityfreezer;
		this.addSlot(new Slot(tileentityfreezer, 0, 56, 17));
		this.addSlot(new Slot(tileentityfreezer, 1, 56, 53));
		this.addSlot(new SlotFurnace(inventoryplayer.player, tileentityfreezer, 2, 116, 35));

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

	public void updateCraftingResults() {
		super.updateCraftingResults();

		for(int i = 0; i < this.field_20121_g.size(); ++i) {
			ICrafting icrafting = (ICrafting)this.field_20121_g.get(i);
			if(this.cookTime != this.freezer.frozenTimeForItem) {
				icrafting.func_20158_a(this, 0, this.freezer.frozenTimeForItem);
			}

			if(this.burnTime != this.freezer.frozenProgress) {
				icrafting.func_20158_a(this, 1, this.freezer.frozenProgress);
			}

			if(this.itemBurnTime != this.freezer.frozenPowerRemaining) {
				icrafting.func_20158_a(this, 2, this.freezer.frozenPowerRemaining);
			}
		}

		this.cookTime = this.freezer.frozenTimeForItem;
		this.burnTime = this.freezer.frozenProgress;
		this.itemBurnTime = this.freezer.frozenPowerRemaining;
	}

	public void func_20112_a(int i, int j) {
		if(i == 0) {
			this.freezer.frozenTimeForItem = j;
		}

		if(i == 1) {
			this.freezer.frozenProgress = j;
		}

		if(i == 2) {
			this.freezer.frozenPowerRemaining = j;
		}

	}

	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return this.freezer.canInteractWith(entityplayer);
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

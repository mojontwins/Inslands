package net.minecraft.src;

public class ContainerEnchanter extends Container {
	private TileEntityEnchanter enchanter;
	private int cookTime = 0;
	private int burnTime = 0;
	private int itemBurnTime = 0;

	public ContainerEnchanter(InventoryPlayer inventoryplayer, TileEntityEnchanter tileentityenchanter) {
		this.enchanter = tileentityenchanter;
		this.addSlot(new Slot(tileentityenchanter, 0, 56, 17));
		this.addSlot(new Slot(tileentityenchanter, 1, 56, 53));
		this.addSlot(new SlotFurnace(inventoryplayer.player, tileentityenchanter, 2, 116, 35));

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
			if(this.cookTime != this.enchanter.enchantTimeForItem) {
				icrafting.func_20158_a(this, 0, this.enchanter.enchantTimeForItem);
			}

			if(this.burnTime != this.enchanter.enchantProgress) {
				icrafting.func_20158_a(this, 1, this.enchanter.enchantProgress);
			}

			if(this.itemBurnTime != this.enchanter.enchantPowerRemaining) {
				icrafting.func_20158_a(this, 2, this.enchanter.enchantPowerRemaining);
			}
		}

		this.cookTime = this.enchanter.enchantTimeForItem;
		this.burnTime = this.enchanter.enchantProgress;
		this.itemBurnTime = this.enchanter.enchantPowerRemaining;
	}

	public void func_20112_a(int i, int j) {
		if(i == 0) {
			this.enchanter.enchantTimeForItem = j;
		}

		if(i == 1) {
			this.enchanter.enchantProgress = j;
		}

		if(i == 2) {
			this.enchanter.enchantPowerRemaining = j;
		}

	}

	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return this.enchanter.canInteractWith(entityplayer);
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

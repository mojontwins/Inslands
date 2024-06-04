package net.minecraft.src;

public class InventoryAether implements IInventory {
	public ItemStack[] slots;
	public EntityPlayer player;

	public InventoryAether(EntityPlayer entityplayer) {
		this.player = entityplayer;
		this.slots = new ItemStack[8];
	}

	public void readFromNBT(NBTTagList nbttaglist) {
		this.slots = new ItemStack[8];

		for(int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			ItemStack itemstack = new ItemStack(nbttagcompound);
			if(j > 8 || !(itemstack.getItem() instanceof ItemMoreArmor)) {
				this.readOldFile(nbttaglist);
				return;
			}

			if(itemstack.getItem() != null && j < this.slots.length) {
				this.slots[j] = itemstack;
			}
		}

	}

	public void readOldFile(NBTTagList nbttaglist) {
		for(int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			ItemStack itemstack = new ItemStack(nbttagcompound);
			if(itemstack.getItem() != null && j >= 104 && j < 112) {
				this.slots[j - 104] = itemstack;
			}
		}

	}

	public NBTTagList writeToNBT(NBTTagList nbttaglist) {
		for(int j = 0; j < this.slots.length; ++j) {
			if(this.slots[j] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)j);
				this.slots[j].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		return nbttaglist;
	}

	public ItemStack getStackInSlot(int i) {
		return this.slots[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		if(this.slots[i] != null) {
			ItemStack itemstack1;
			if(this.slots[i].stackSize <= j) {
				itemstack1 = this.slots[i];
				this.slots[i] = null;
				this.onInventoryChanged();
				return itemstack1;
			} else {
				itemstack1 = this.slots[i].splitStack(j);
				if(this.slots[i].stackSize == 0) {
					this.slots[i] = null;
				}

				this.onInventoryChanged();
				return itemstack1;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.slots[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public int getSizeInventory() {
		return 8;
	}

	public String getInvName() {
		return "Aether Slots";
	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public void onInventoryChanged() {
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	public int getTotalArmorValue() {
		int i = 0;
		int j = 0;
		int k = 0;

		for(int l = 0; l < this.slots.length; ++l) {
			if(this.slots[l] != null && this.slots[l].getItem() instanceof ItemMoreArmor) {
				int i1 = this.slots[l].getMaxDamage();
				int j1 = this.slots[l].getItemDamageForDisplay();
				int k1 = i1 - j1;
				j += k1;
				k += i1;
				int l1 = ((ItemMoreArmor)this.slots[l].getItem()).damageReduceAmount;
				i += l1;
			}
		}

		if(k == 0) {
			return 0;
		} else {
			return (i - 1) * j / k + 1;
		}
	}

	public void damageArmor(int i) {
		for(int j = 0; j < this.slots.length; ++j) {
			if(this.slots[j] != null && this.slots[j].getItem() instanceof ItemMoreArmor) {
				this.slots[j].damageItem(i, this.player);
				if(this.slots[j].stackSize == 0) {
					this.slots[j].func_1097_a(this.player);
					this.slots[j] = null;
				}
			}
		}

	}

	public void dropAllItems() {
		for(int j = 0; j < this.slots.length; ++j) {
			if(this.slots[j] != null) {
				this.player.dropPlayerItemWithRandomChoice(this.slots[j], true);
				this.slots[j] = null;
			}
		}

	}
}

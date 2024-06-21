package net.minecraft.src;

import java.util.List;

public class TileEntityBrewingStand extends TileEntity implements IInventory {
	private ItemStack[] brewingItemStacks = new ItemStack[4];
	private int brewTime;
	private int filledSlots;
	private int ingredientID;

	public String getInvName() {
		return "container.brewing";
	}

	public int getSizeInventory() {
		return this.brewingItemStacks.length;
	}

	public void updateEntity() {
		if(this.brewTime > 0) {
			--this.brewTime;
			if(this.brewTime == 0) {
				this.brewPotions();
				this.onInventoryChanged();
			} else if(!this.canBrew()) {
				this.brewTime = 0;
				this.onInventoryChanged();
			} else if(this.ingredientID != this.brewingItemStacks[3].itemID) {
				this.brewTime = 0;
				this.onInventoryChanged();
			}
		} else if(this.canBrew()) {
			this.brewTime = 400;
			this.ingredientID = this.brewingItemStacks[3].itemID;
		}

		int i1 = this.getFilledSlots();
		if(i1 != this.filledSlots) {
			this.filledSlots = i1;
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, i1);
		}

		super.updateEntity();
	}

	public int getBrewTime() {
		return this.brewTime;
	}

	private boolean canBrew() {
		if(this.brewingItemStacks[3] != null && this.brewingItemStacks[3].stackSize > 0) {
			ItemStack itemStack1 = this.brewingItemStacks[3];
			if(!Item.itemsList[itemStack1.itemID].isPotionIngredient()) {
				return false;
			} else {
				boolean z2 = false;

				for(int i3 = 0; i3 < 3; ++i3) {
					if(this.brewingItemStacks[i3] != null && this.brewingItemStacks[i3].itemID == Item.potion.shiftedIndex) {
						int i4 = this.brewingItemStacks[i3].getItemDamage();
						int i5 = this.getPotionResult(i4, itemStack1);
						if(!ItemPotion.isSplash(i4) && ItemPotion.isSplash(i5)) {
							z2 = true;
							break;
						}

						List list6 = Item.potion.getEffects(i4);
						List list7 = Item.potion.getEffects(i5);
						if((i4 <= 0 || list6 != list7) && (list6 == null || !list6.equals(list7) && list7 != null) && i4 != i5) {
							z2 = true;
							break;
						}
					}
				}

				return z2;
			}
		} else {
			return false;
		}
	}

	private void brewPotions() {
		if(this.canBrew()) {
			ItemStack itemStack1 = this.brewingItemStacks[3];

			for(int i2 = 0; i2 < 3; ++i2) {
				if(this.brewingItemStacks[i2] != null && this.brewingItemStacks[i2].itemID == Item.potion.shiftedIndex) {
					int i3 = this.brewingItemStacks[i2].getItemDamage();
					int i4 = this.getPotionResult(i3, itemStack1);
					List list5 = Item.potion.getEffects(i3);
					List list6 = Item.potion.getEffects(i4);
					if((i3 <= 0 || list5 != list6) && (list5 == null || !list5.equals(list6) && list6 != null)) {
						if(i3 != i4) {
							this.brewingItemStacks[i2].setItemDamage(i4);
						}
					} else if(!ItemPotion.isSplash(i3) && ItemPotion.isSplash(i4)) {
						this.brewingItemStacks[i2].setItemDamage(i4);
					}
				}
			}

			if(Item.itemsList[itemStack1.itemID].hasContainerItem()) {
				this.brewingItemStacks[3] = new ItemStack(Item.itemsList[itemStack1.itemID].getContainerItem());
			} else {
				--this.brewingItemStacks[3].stackSize;
				if(this.brewingItemStacks[3].stackSize <= 0) {
					this.brewingItemStacks[3] = null;
				}
			}

		}
	}

	private int getPotionResult(int i1, ItemStack itemStack2) {
		return itemStack2 == null ? i1 : (Item.itemsList[itemStack2.itemID].isPotionIngredient() ? PotionHelper.applyIngredient(i1, Item.itemsList[itemStack2.itemID].getPotionEffect()) : i1);
	}

	public void readFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readFromNBT(nBTTagCompound1);
		NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("Items");
		this.brewingItemStacks = new ItemStack[this.getSizeInventory()];

		for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
			NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3);
			byte b5 = nBTTagCompound4.getByte("Slot");
			if(b5 >= 0 && b5 < this.brewingItemStacks.length) {
				this.brewingItemStacks[b5] = ItemStack.loadItemStackFromNBT(nBTTagCompound4);
			}
		}

		this.brewTime = nBTTagCompound1.getShort("BrewTime");
	}

	public void writeToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeToNBT(nBTTagCompound1);
		nBTTagCompound1.setShort("BrewTime", (short)this.brewTime);
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.brewingItemStacks.length; ++i3) {
			if(this.brewingItemStacks[i3] != null) {
				NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
				nBTTagCompound4.setByte("Slot", (byte)i3);
				this.brewingItemStacks[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.appendTag(nBTTagCompound4);
			}
		}

		nBTTagCompound1.setTag("Items", nBTTagList2);
	}

	public ItemStack getStackInSlot(int i1) {
		return i1 >= 0 && i1 < this.brewingItemStacks.length ? this.brewingItemStacks[i1] : null;
	}

	public ItemStack decrStackSize(int i1, int i2) {
		if(i1 >= 0 && i1 < this.brewingItemStacks.length) {
			ItemStack itemStack3 = this.brewingItemStacks[i1];
			this.brewingItemStacks[i1] = null;
			return itemStack3;
		} else {
			return null;
		}
	}

	public ItemStack getStackInSlotOnClosing(int i1) {
		if(i1 >= 0 && i1 < this.brewingItemStacks.length) {
			ItemStack itemStack2 = this.brewingItemStacks[i1];
			this.brewingItemStacks[i1] = null;
			return itemStack2;
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i1, ItemStack itemStack2) {
		if(i1 >= 0 && i1 < this.brewingItemStacks.length) {
			this.brewingItemStacks[i1] = itemStack2;
		}

	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public boolean isUseableByPlayer(EntityPlayer entityPlayer1) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityPlayer1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	public void openChest() {
	}

	public void closeChest() {
	}

	public void setBrewTime(int i1) {
		this.brewTime = i1;
	}

	public int getFilledSlots() {
		int i1 = 0;

		for(int i2 = 0; i2 < 3; ++i2) {
			if(this.brewingItemStacks[i2] != null) {
				i1 |= 1 << i2;
			}
		}

		return i1;
	}
}

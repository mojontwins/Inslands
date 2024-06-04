package net.minecraft.src;

public class TileEntityIncubator extends TileEntity implements IInventory {
	private ItemStack[] IncubatorItemStacks = new ItemStack[2];
	public int torchPower;
	public int progress = 0;

	public int getSizeInventory() {
		return this.IncubatorItemStacks.length;
	}

	public ItemStack getStackInSlot(int i) {
		return this.IncubatorItemStacks[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		if(this.IncubatorItemStacks[i] != null) {
			ItemStack itemstack1;
			if(this.IncubatorItemStacks[i].stackSize <= j) {
				itemstack1 = this.IncubatorItemStacks[i];
				this.IncubatorItemStacks[i] = null;
				return itemstack1;
			} else {
				itemstack1 = this.IncubatorItemStacks[i].splitStack(j);
				if(this.IncubatorItemStacks[i].stackSize == 0) {
					this.IncubatorItemStacks[i] = null;
				}

				return itemstack1;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.IncubatorItemStacks[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

	}

	public String getInvName() {
		return "Incubator";
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		this.IncubatorItemStacks = new ItemStack[this.getSizeInventory()];

		for(int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < this.IncubatorItemStacks.length) {
				this.IncubatorItemStacks[byte0] = new ItemStack(nbttagcompound1);
			}
		}

		this.progress = nbttagcompound.getShort("BurnTime");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("BurnTime", (short)this.progress);
		NBTTagList nbttaglist = new NBTTagList();

		for(int i = 0; i < this.IncubatorItemStacks.length; ++i) {
			if(this.IncubatorItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.IncubatorItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public int getCookProgressScaled(int i) {
		return this.progress * i / 6000;
	}

	public int getBurnTimeRemainingScaled(int i) {
		return this.torchPower * i / 500;
	}

	public boolean isBurning() {
		return this.torchPower > 0;
	}

	public void updateEntity() {
		if(this.torchPower > 0) {
			--this.torchPower;
			if(this.getStackInSlot(1) != null) {
				++this.progress;
			}
		}

		if(this.IncubatorItemStacks[1] == null || this.IncubatorItemStacks[1].itemID != AetherItems.MoaEgg.shiftedIndex) {
			this.progress = 0;
		}

		if(this.progress >= 6000) {
			if(this.IncubatorItemStacks[1] != null) {
				EntityMoa moa = new EntityMoa(this.worldObj, true, false, false, MoaColour.getColour(this.IncubatorItemStacks[1].getItemDamage()));
				moa.setPosition((double)this.xCoord + 0.5D, (double)this.yCoord + 1.5D, (double)this.zCoord + 0.5D);
				this.worldObj.entityJoinedWorld(moa);
			}

			mod_Aether.giveAchievement(AetherAchievements.incubator);
			this.decrStackSize(1, 1);
			this.progress = 0;
		}

		if(this.torchPower <= 0 && this.IncubatorItemStacks[1] != null && this.IncubatorItemStacks[1].itemID == AetherItems.MoaEgg.shiftedIndex && this.getStackInSlot(0) != null && this.getStackInSlot(0).itemID == AetherBlocks.AmbrosiumTorch.blockID) {
			this.torchPower += 1000;
			this.decrStackSize(0, 1);
		}

	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
}

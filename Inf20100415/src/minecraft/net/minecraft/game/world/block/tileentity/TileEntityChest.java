package net.minecraft.game.world.block.tileentity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.game.IInventory;
import net.minecraft.game.item.ItemStack;

public class TileEntityChest extends TileEntity implements IInventory {
	private ItemStack[] chestContents = new ItemStack[36];

	public final int getInventorySize() {
		return 27;
	}

	public final ItemStack getStackInSlot(int i1) {
		return this.chestContents[i1];
	}

	public final ItemStack decrStackSize(int i1, int i2) {
		if(this.chestContents[i1] != null) {
			ItemStack itemStack3;
			if(this.chestContents[i1].stackSize <= i2) {
				itemStack3 = this.chestContents[i1];
				this.chestContents[i1] = null;
				return itemStack3;
			} else {
				itemStack3 = this.chestContents[i1].splitStack(i2);
				if(this.chestContents[i1].stackSize == 0) {
					this.chestContents[i1] = null;
				}

				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public final void setInventorySlotContents(int i1, ItemStack itemStack2) {
		this.chestContents[i1] = itemStack2;
		if(itemStack2 != null && itemStack2.stackSize > 64) {
			itemStack2.stackSize = 64;
		}

	}

	public final String getInvName() {
		return "Chest";
	}

	public final void readFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readFromNBT(nBTTagCompound1);
		NBTTagList nBTTagList5 = nBTTagCompound1.getTagList("Items");
		this.chestContents = new ItemStack[27];

		for(int i2 = 0; i2 < nBTTagList5.tagCount(); ++i2) {
			NBTTagCompound nBTTagCompound3;
			int i4;
			if((i4 = (nBTTagCompound3 = (NBTTagCompound)nBTTagList5.tagAt(i2)).getByte("Slot") & 255) >= 0 && i4 < this.chestContents.length) {
				this.chestContents[i4] = new ItemStack(nBTTagCompound3);
			}
		}

	}

	public final void writeToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeToNBT(nBTTagCompound1);
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.chestContents.length; ++i3) {
			if(this.chestContents[i3] != null) {
				NBTTagCompound nBTTagCompound4;
				(nBTTagCompound4 = new NBTTagCompound()).setByte("Slot", (byte)i3);
				this.chestContents[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		nBTTagCompound1.setTag("Items", nBTTagList2);
	}

	public final int getInventoryStackLimit() {
		return 64;
	}

	public final void onInventoryChanged() {
		this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord);
	}
}
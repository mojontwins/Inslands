package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFreezer extends TileEntity implements IInventory {
	private static List frozen = new ArrayList();
	private ItemStack[] frozenItemStacks = new ItemStack[3];
	public int frozenProgress = 0;
	public int frozenPowerRemaining = 0;
	public int frozenTimeForItem = 0;
	private Frozen currentFrozen;

	public int getSizeInventory() {
		return this.frozenItemStacks.length;
	}

	public ItemStack getStackInSlot(int i) {
		return this.frozenItemStacks[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		if(this.frozenItemStacks[i] != null) {
			ItemStack itemstack1;
			if(this.frozenItemStacks[i].stackSize <= j) {
				itemstack1 = this.frozenItemStacks[i];
				this.frozenItemStacks[i] = null;
				return itemstack1;
			} else {
				itemstack1 = this.frozenItemStacks[i].splitStack(j);
				if(this.frozenItemStacks[i].stackSize == 0) {
					this.frozenItemStacks[i] = null;
				}

				return itemstack1;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.frozenItemStacks[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

	}

	public String getInvName() {
		return "Freezer";
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		this.frozenItemStacks = new ItemStack[this.getSizeInventory()];

		for(int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < this.frozenItemStacks.length) {
				this.frozenItemStacks[byte0] = new ItemStack(nbttagcompound1);
			}
		}

		this.frozenProgress = nbttagcompound.getShort("BurnTime");
		this.frozenTimeForItem = nbttagcompound.getShort("CookTime");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("BurnTime", (short)this.frozenProgress);
		nbttagcompound.setShort("CookTime", (short)this.frozenTimeForItem);
		NBTTagList nbttaglist = new NBTTagList();

		for(int i = 0; i < this.frozenItemStacks.length; ++i) {
			if(this.frozenItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.frozenItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.setTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public int getCookProgressScaled(int i) {
		return this.frozenTimeForItem == 0 ? 0 : this.frozenProgress * i / this.frozenTimeForItem;
	}

	public int getBurnTimeRemainingScaled(int i) {
		return this.frozenPowerRemaining * i / 500;
	}

	public boolean isBurning() {
		return this.frozenPowerRemaining > 0;
	}

	public void updateEntity() {
		if(this.frozenPowerRemaining > 0) {
			--this.frozenPowerRemaining;
			if(this.currentFrozen != null) {
				++this.frozenProgress;
			}
		}

		if(this.currentFrozen != null && (this.frozenItemStacks[0] == null || this.frozenItemStacks[0].itemID != this.currentFrozen.frozenFrom.itemID)) {
			this.currentFrozen = null;
			this.frozenProgress = 0;
		}

		if(this.currentFrozen != null && this.frozenProgress >= this.currentFrozen.frozenPowerNeeded) {
			if(this.frozenItemStacks[2] == null) {
				this.setInventorySlotContents(2, new ItemStack(this.currentFrozen.frozenTo.getItem(), 1, this.currentFrozen.frozenTo.getItemDamage()));
			} else {
				this.setInventorySlotContents(2, new ItemStack(this.currentFrozen.frozenTo.getItem(), this.getStackInSlot(2).stackSize + 1, this.currentFrozen.frozenTo.getItemDamage()));
			}

			if(this.getStackInSlot(0).itemID != Item.bucketWater.shiftedIndex && this.getStackInSlot(0).itemID != Item.bucketLava.shiftedIndex) {
				if(this.getStackInSlot(0).itemID == AetherItems.Bucket.shiftedIndex) {
					this.setInventorySlotContents(0, new ItemStack(AetherItems.Bucket));
				} else {
					this.decrStackSize(0, 1);
				}
			} else {
				this.setInventorySlotContents(0, new ItemStack(Item.bucketEmpty));
			}

			this.frozenProgress = 0;
			this.currentFrozen = null;
			this.frozenTimeForItem = 0;
		}

		if(this.frozenPowerRemaining <= 0 && this.currentFrozen != null && this.getStackInSlot(1) != null && this.getStackInSlot(1).itemID == AetherBlocks.Icestone.blockID) {
			this.frozenPowerRemaining += 500;
			this.decrStackSize(1, 1);
		}

		if(this.currentFrozen == null) {
			ItemStack itemstack = this.getStackInSlot(0);

			for(int i = 0; i < frozen.size(); ++i) {
				if(itemstack != null && frozen.get(i) != null && itemstack.itemID == ((Frozen)frozen.get(i)).frozenFrom.itemID && itemstack.getItemDamage() == ((Frozen)frozen.get(i)).frozenFrom.getItemDamage()) {
					if(this.frozenItemStacks[2] == null) {
						this.currentFrozen = (Frozen)frozen.get(i);
						this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
					} else if(this.frozenItemStacks[2].itemID == ((Frozen)frozen.get(i)).frozenTo.itemID && ((Frozen)frozen.get(i)).frozenTo.getItem().getItemStackLimit() > this.frozenItemStacks[2].stackSize) {
						this.currentFrozen = (Frozen)frozen.get(i);
						this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
					}
				}
			}
		}

	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	public static void addFrozen(ItemStack from, ItemStack to, int i) {
		frozen.add(new Frozen(from, to, i));
	}

	static {
		addFrozen(new ItemStack(Item.bucketWater, 1), new ItemStack(Block.ice, 5), 500);
		addFrozen(new ItemStack(AetherItems.Bucket, 1, 8), new ItemStack(Block.ice, 5), 500);
		addFrozen(new ItemStack(Item.bucketLava, 1), new ItemStack(Block.obsidian, 2), 500);
		addFrozen(new ItemStack(AetherBlocks.Aercloud, 1, 0), new ItemStack(AetherBlocks.Aercloud, 1, 1), 50);
		addFrozen(new ItemStack(AetherItems.GoldPendant, 1), new ItemStack(AetherItems.IcePendant, 1), 2500);
		addFrozen(new ItemStack(AetherItems.GoldRing, 1), new ItemStack(AetherItems.IceRing, 1), 1500);
		addFrozen(new ItemStack(AetherItems.IronRing, 1), new ItemStack(AetherItems.IceRing, 1), 1500);
		addFrozen(new ItemStack(AetherItems.IronPendant, 1), new ItemStack(AetherItems.IcePendant, 1), 2500);
	}
}

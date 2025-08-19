package net.minecraft.game.item;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.world.block.Block;

public final class ItemStack {
	public int stackSize;
	public int animationsToGo;
	public int itemID;
	public int itemDamage;

	public ItemStack(Block block1) {
		this((Block)block1, 1);
	}

	public ItemStack(Block block1, int i2) {
		this(block1.blockID, i2);
	}

	public ItemStack(Item item1) {
		this((Item)item1, 1);
	}

	public ItemStack(Item item1, int i2) {
		this(item1.shiftedIndex, i2);
	}

	public ItemStack(int i1) {
		this(i1, 1);
	}

	public ItemStack(int i1, int i2) {
		this.stackSize = 0;
		this.itemID = i1;
		this.stackSize = i2;
	}

	public ItemStack(int i1, int i2, int i3) {
		this.stackSize = 0;
		this.itemID = i1;
		this.stackSize = i2;
		this.itemDamage = i3;
	}

	public ItemStack(NBTTagCompound nBTTagCompound1) {
		this.stackSize = 0;
		this.itemID = nBTTagCompound1.getShort("id");
		this.stackSize = nBTTagCompound1.getByte("Count");
		this.itemDamage = nBTTagCompound1.getShort("Damage");
	}

	public final ItemStack splitStack(int i1) {
		this.stackSize -= i1;
		return new ItemStack(this.itemID, i1, this.itemDamage);
	}

	public final Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public final NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("id", (short)this.itemID);
		nBTTagCompound1.setByte("Count", (byte)this.stackSize);
		nBTTagCompound1.setShort("Damage", (short)this.itemDamage);
		return nBTTagCompound1;
	}

	public final int getMaxDamage() {
		return Item.itemsList[this.itemID].getMaxDamage();
	}

	public final void damageItem(int i1) {
		this.itemDamage += i1;
		if(this.itemDamage > this.getMaxDamage()) {
			--this.stackSize;
			if(this.stackSize < 0) {
				this.stackSize = 0;
			}

			this.itemDamage = 0;
		}

	}
}
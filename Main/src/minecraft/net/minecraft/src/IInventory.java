package net.minecraft.src;

import net.minecraft.world.entity.player.EntityPlayer;

public interface IInventory {
	int getSizeInventory();

	ItemStack getStackInSlot(int i1);

	ItemStack decrStackSize(int i1, int i2);

	void setInventorySlotContents(int i1, ItemStack itemStack2);

	String getInvName();

	int getInventoryStackLimit();

	void onInventoryChanged();

	boolean canInteractWith(EntityPlayer entityPlayer1);
}

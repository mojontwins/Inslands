package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ContainerEnchantment extends Container {
	public IInventory tableInventory = new SlotEnchantmentTable(this, "Enchant", 1);
	private World worldPointer;
	private int posX;
	private int posY;
	private int posZ;
	private Random rand = new Random();
	public long nameSeed;
	public int[] enchantLevels = new int[3];

	public ContainerEnchantment(InventoryPlayer inventoryPlayer1, World world2, int i3, int i4, int i5) {
		this.worldPointer = world2;
		this.posX = i3;
		this.posY = i4;
		this.posZ = i5;
		this.addSlot(new SlotEnchantment(this, this.tableInventory, 0, 25, 47));

		int i6;
		for(i6 = 0; i6 < 3; ++i6) {
			for(int i7 = 0; i7 < 9; ++i7) {
				this.addSlot(new Slot(inventoryPlayer1, i7 + i6 * 9 + 9, 8 + i7 * 18, 84 + i6 * 18));
			}
		}

		for(i6 = 0; i6 < 9; ++i6) {
			this.addSlot(new Slot(inventoryPlayer1, i6, 8 + i6 * 18, 142));
		}

	}

	public void onCraftGuiOpened(ICrafting iCrafting1) {
		super.onCraftGuiOpened(iCrafting1);
		iCrafting1.updateCraftingInventoryInfo(this, 0, this.enchantLevels[0]);
		iCrafting1.updateCraftingInventoryInfo(this, 1, this.enchantLevels[1]);
		iCrafting1.updateCraftingInventoryInfo(this, 2, this.enchantLevels[2]);
	}

	public void updateCraftingResults() {
		super.updateCraftingResults();

		for(int i1 = 0; i1 < this.crafters.size(); ++i1) {
			ICrafting iCrafting2 = (ICrafting)this.crafters.get(i1);
			iCrafting2.updateCraftingInventoryInfo(this, 0, this.enchantLevels[0]);
			iCrafting2.updateCraftingInventoryInfo(this, 1, this.enchantLevels[1]);
			iCrafting2.updateCraftingInventoryInfo(this, 2, this.enchantLevels[2]);
		}

	}

	public void onCraftMatrixChanged(IInventory iInventory1) {
		if(iInventory1 == this.tableInventory) {
			ItemStack itemStack2 = iInventory1.getStackInSlot(0);
			int i3;
			if(itemStack2 != null && itemStack2.isItemEnchantable()) {
				this.nameSeed = this.rand.nextLong();
				if(!this.worldPointer.isRemote) {
					i3 = 0;

					int i4;
					for(i4 = -1; i4 <= 1; ++i4) {
						for(int i5 = -1; i5 <= 1; ++i5) {
							if((i4 != 0 || i5 != 0) && this.worldPointer.isAirBlock(this.posX + i5, this.posY, this.posZ + i4) && this.worldPointer.isAirBlock(this.posX + i5, this.posY + 1, this.posZ + i4)) {
								if(this.worldPointer.getBlockId(this.posX + i5 * 2, this.posY, this.posZ + i4 * 2) == Block.bookShelf.blockID) {
									++i3;
								}

								if(this.worldPointer.getBlockId(this.posX + i5 * 2, this.posY + 1, this.posZ + i4 * 2) == Block.bookShelf.blockID) {
									++i3;
								}

								if(i5 != 0 && i4 != 0) {
									if(this.worldPointer.getBlockId(this.posX + i5 * 2, this.posY, this.posZ + i4) == Block.bookShelf.blockID) {
										++i3;
									}

									if(this.worldPointer.getBlockId(this.posX + i5 * 2, this.posY + 1, this.posZ + i4) == Block.bookShelf.blockID) {
										++i3;
									}

									if(this.worldPointer.getBlockId(this.posX + i5, this.posY, this.posZ + i4 * 2) == Block.bookShelf.blockID) {
										++i3;
									}

									if(this.worldPointer.getBlockId(this.posX + i5, this.posY + 1, this.posZ + i4 * 2) == Block.bookShelf.blockID) {
										++i3;
									}
								}
							}
						}
					}

					for(i4 = 0; i4 < 3; ++i4) {
						this.enchantLevels[i4] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i4, i3, itemStack2);
					}

					this.updateCraftingResults();
				}
			} else {
				for(i3 = 0; i3 < 3; ++i3) {
					this.enchantLevels[i3] = 0;
				}
			}
		}

	}

	public boolean enchantItem(EntityPlayer entityPlayer1, int i2) {
		ItemStack itemStack3 = this.tableInventory.getStackInSlot(0);
		if(this.enchantLevels[i2] <= 0 || itemStack3 == null || entityPlayer1.experienceLevel < this.enchantLevels[i2] && !entityPlayer1.capabilities.isCreativeMode) {
			return false;
		} else {
			if(!this.worldPointer.isRemote) {
				List list4 = EnchantmentHelper.buildEnchantmentList(this.rand, itemStack3, this.enchantLevels[i2]);
				if(list4 != null) {
					entityPlayer1.removeExperience(this.enchantLevels[i2]);
					Iterator iterator5 = list4.iterator();

					while(iterator5.hasNext()) {
						EnchantmentData enchantmentData6 = (EnchantmentData)iterator5.next();
						itemStack3.addEnchantment(enchantmentData6.enchantmentobj, enchantmentData6.enchantmentLevel);
					}

					this.onCraftMatrixChanged(this.tableInventory);
				}
			}

			return true;
		}
	}

	public void onCraftGuiClosed(EntityPlayer entityPlayer1) {
		super.onCraftGuiClosed(entityPlayer1);
		if(!this.worldPointer.isRemote) {
			ItemStack itemStack2 = this.tableInventory.getStackInSlotOnClosing(0);
			if(itemStack2 != null) {
				entityPlayer1.dropPlayerItem(itemStack2);
			}

		}
	}

	public boolean canInteractWith(EntityPlayer entityPlayer1) {
		return this.worldPointer.getBlockId(this.posX, this.posY, this.posZ) != Block.enchantmentTable.blockID ? false : entityPlayer1.getDistanceSq((double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
	}

	public ItemStack transferStackInSlot(int i1) {
		ItemStack itemStack2 = null;
		Slot slot3 = (Slot)this.inventorySlots.get(i1);
		if(slot3 != null && slot3.getHasStack()) {
			ItemStack itemStack4 = slot3.getStack();
			itemStack2 = itemStack4.copy();
			if(i1 != 0) {
				return null;
			}

			if(!this.mergeItemStack(itemStack4, 1, 37, true)) {
				return null;
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

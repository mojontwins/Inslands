package net.minecraft.src;

public class SlotCrafting extends Slot {
	private final IInventory craftMatrix;
	private EntityPlayer thePlayer;
	private int field_48418_g;

	public SlotCrafting(EntityPlayer entityPlayer1, IInventory iInventory2, IInventory iInventory3, int i4, int i5, int i6) {
		super(iInventory3, i4, i5, i6);
		this.thePlayer = entityPlayer1;
		this.craftMatrix = iInventory2;
	}

	public boolean isItemValid(ItemStack itemStack1) {
		return false;
	}

	public ItemStack decrStackSize(int i1) {
		if(this.getHasStack()) {
			this.field_48418_g += Math.min(i1, this.getStack().stackSize);
		}

		return super.decrStackSize(i1);
	}

	protected void func_48415_a(ItemStack itemStack1, int i2) {
		this.field_48418_g += i2;
		this.func_48416_b(itemStack1);
	}

	protected void func_48416_b(ItemStack itemStack1) {
		itemStack1.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_48418_g);
		this.field_48418_g = 0;
		if(itemStack1.itemID == Block.workbench.blockID) {
			this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
		} else if(itemStack1.itemID == Item.pickaxeWood.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
		} else if(itemStack1.itemID == Block.stoneOvenIdle.blockID) {
			this.thePlayer.addStat(AchievementList.buildFurnace, 1);
		} else if(itemStack1.itemID == Item.hoeWood.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildHoe, 1);
		} else if(itemStack1.itemID == Item.bread.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.makeBread, 1);
		} else if(itemStack1.itemID == Item.cake.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.bakeCake, 1);
		} else if(itemStack1.itemID == Item.pickaxeStone.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		} else if(itemStack1.itemID == Item.swordWood.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildSword, 1);
		} else if(itemStack1.itemID == Block.enchantmentTable.blockID) {
			this.thePlayer.addStat(AchievementList.enchantments, 1);
		} else if(itemStack1.itemID == Block.bookShelf.blockID) {
			this.thePlayer.addStat(AchievementList.bookcase, 1);
		}

	}

	public void onPickupFromSlot(ItemStack itemStack1) {
		this.func_48416_b(itemStack1);

		for(int i2 = 0; i2 < this.craftMatrix.getSizeInventory(); ++i2) {
			ItemStack itemStack3 = this.craftMatrix.getStackInSlot(i2);
			if(itemStack3 != null) {
				this.craftMatrix.decrStackSize(i2, 1);
				if(itemStack3.getItem().hasContainerItem()) {
					ItemStack itemStack4 = new ItemStack(itemStack3.getItem().getContainerItem());
					if(!itemStack3.getItem().doesContainerItemLeaveCraftingGrid(itemStack3) || !this.thePlayer.inventory.addItemStackToInventory(itemStack4)) {
						if(this.craftMatrix.getStackInSlot(i2) == null) {
							this.craftMatrix.setInventorySlotContents(i2, itemStack4);
						} else {
							this.thePlayer.dropPlayerItem(itemStack4);
						}
					}
				}
			}
		}

	}
}

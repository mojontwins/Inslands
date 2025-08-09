package net.minecraft.src;

import net.minecraft.world.entity.player.EntityPlayer;

public class SlotCrafting extends Slot {
	private final IInventory craftMatrix;
	private EntityPlayer thePlayer;

	public SlotCrafting(EntityPlayer entityPlayer1, IInventory iInventory2, IInventory iInventory3, int i4, int i5, int i6) {
		super(iInventory3, i4, i5, i6);
		this.thePlayer = entityPlayer1;
		this.craftMatrix = iInventory2;
	}

	public boolean isItemValid(ItemStack itemStack1) {
		return false;
	}

	public void onPickupFromSlot(ItemStack pickedStack) {
		pickedStack.onCrafting(this.thePlayer.worldObj, this.thePlayer);
		if(pickedStack.itemID == Block.workbench.blockID) {
			this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
		} else if(pickedStack.itemID == Item.pickaxeWood.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
		} else if(pickedStack.itemID == Block.stoneOvenIdle.blockID) {
			this.thePlayer.addStat(AchievementList.buildFurnace, 1);
		} else if(pickedStack.itemID == Item.hoeWood.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildHoe, 1);
		} else if(pickedStack.itemID == Item.bread.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.makeBread, 1);
		} else if(pickedStack.itemID == Item.cake.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.bakeCake, 1);
		} else if(pickedStack.itemID == Item.pickaxeStone.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		} else if(pickedStack.itemID == Item.swordWood.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.buildSword, 1);
		} else if(pickedStack.itemID == Item.bootsLeather.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.bootsOfLeather, 1);
		} else if(pickedStack.itemID == Item.slingshot.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.bangBang, 1);
		} else if(pickedStack.itemID == Item.pirateCrown.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.pirateCrown, 1);
		} else if(pickedStack.itemID == Block.fleshBlock.blockID) {
			this.thePlayer.addStat(AchievementList.meatBlock, 1);
		} else if(pickedStack.itemID == Item.boat_iron.shiftedIndex) {
			this.thePlayer.addStat(AchievementList.ironBoat, 1);
		}

		for(int slotIdx = 0; slotIdx < this.craftMatrix.getSizeInventory(); ++slotIdx) {
			ItemStack stackInSlot = this.craftMatrix.getStackInSlot(slotIdx);
			if(stackInSlot != null) {
				this.craftMatrix.decrStackSize(slotIdx, 1);
				if(stackInSlot.getItem().hasContainerItem()) {
					this.craftMatrix.setInventorySlotContents(slotIdx, new ItemStack(stackInSlot.getItem().getContainerItem()));
				}
			}
		}

	}
}

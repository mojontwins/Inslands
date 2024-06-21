package net.minecraft.src;

class SlotBrewingStandPotion extends Slot {
	private EntityPlayer player;
	final ContainerBrewingStand container;

	public SlotBrewingStandPotion(ContainerBrewingStand containerBrewingStand1, EntityPlayer entityPlayer2, IInventory iInventory3, int i4, int i5, int i6) {
		super(iInventory3, i4, i5, i6);
		this.container = containerBrewingStand1;
		this.player = entityPlayer2;
	}

	public boolean isItemValid(ItemStack itemStack1) {
		return itemStack1 != null && (itemStack1.itemID == Item.potion.shiftedIndex || itemStack1.itemID == Item.glassBottle.shiftedIndex);
	}

	public int getSlotStackLimit() {
		return 1;
	}

	public void onPickupFromSlot(ItemStack itemStack1) {
		if(itemStack1.itemID == Item.potion.shiftedIndex && itemStack1.getItemDamage() > 0) {
			this.player.addStat(AchievementList.potion, 1);
		}

		super.onPickupFromSlot(itemStack1);
	}
}

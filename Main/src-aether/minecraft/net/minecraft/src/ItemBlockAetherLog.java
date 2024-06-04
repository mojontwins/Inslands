package net.minecraft.src;

public class ItemBlockAetherLog extends ItemBlock {
	public ItemBlockAetherLog(int itemID) {
		super(itemID);
		this.setItemName("BlockAetherLog");
		this.setHasSubtypes(true);
	}

	public String getItemNameIS(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		if(i > 2) {
			i = 2;
		}

		if(i == 1) {
			i = 0;
		}

		return this.getItemName() + i;
	}

	public int getPlacedBlockMetadata(int damage) {
		return damage <= 1 ? 1 : 3;
	}
}

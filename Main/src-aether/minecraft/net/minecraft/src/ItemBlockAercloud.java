package net.minecraft.src;

public class ItemBlockAercloud extends ItemBlock {
	public ItemBlockAercloud(int itemID) {
		super(itemID);
		this.setItemName("BlockAercloud");
		this.setHasSubtypes(true);
	}

	public String getItemNameIS(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		if(i > 2) {
			i = 2;
		}

		return this.getItemName() + i;
	}

	public int getColorFromDamage(int damage) {
		return damage == 1 ? -7829249 : (damage == 2 ? -120 : -1);
	}

	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}
}

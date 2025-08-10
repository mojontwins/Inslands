package net.minecraft.world.item;

public class ItemAetherKey extends Item {
	public ItemAetherKey(int itemID) {
		super(itemID);
		this.setIconIndex(13*16+2);
		this.setItemName("AetherKey");
		this.setHasSubtypes(true);
		this.maxStackSize = 1;
	}

	public String getItemNameIS(ItemStack stack) {
		int i = stack.getItemDamage();
		if(i > 2) {
			i = 2;
		}

		return this.getItemName() + i;
	}

	public int getColorFromDamage(int damage) {
		return damage == 1 ? -6710887 : (damage == 2 ? -13312 : -7638187);
	}
}

package net.minecraft.src;

public class ItemAetherKey extends Item {
	protected ItemAetherKey(int itemID) {
		super(itemID);
		this.setIconIndex(ModLoader.addOverride("/gui/items.png", "/aether/items/Key.png"));
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

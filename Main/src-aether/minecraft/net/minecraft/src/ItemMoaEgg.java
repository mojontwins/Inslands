package net.minecraft.src;

public class ItemMoaEgg extends Item {
	protected ItemMoaEgg(int itemID) {
		super(itemID);
		this.setIconIndex(ModLoader.addOverride("/gui/items.png", "/aether/items/MoaEgg.png"));
		this.setHasSubtypes(true);
	}

	public int getColorFromDamage(int damage) {
		return MoaColour.getColour(damage).colour;
	}

	public String getItemNameIS(ItemStack stack) {
		int i = stack.getItemDamage();
		if(i > MoaColour.colours.size() - 1) {
			i = MoaColour.colours.size() - 1;
		}

		return this.getItemName() + i;
	}
}

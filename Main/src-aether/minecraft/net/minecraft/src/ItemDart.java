package net.minecraft.src;

public class ItemDart extends Item {
	public static int sprGolden = ModLoader.addOverride("/gui/items.png", "/aether/items/DartGolden.png");
	public static int sprEnchanted = ModLoader.addOverride("/gui/items.png", "/aether/items/DartEnchanted.png");
	public static int sprPoison = ModLoader.addOverride("/gui/items.png", "/aether/items/DartPoison.png");

	protected ItemDart(int itemID) {
		super(itemID);
		this.setHasSubtypes(true);
	}

	public int getIconFromDamage(int damage) {
		return damage == 0 ? sprGolden : (damage == 1 ? sprPoison : (damage == 2 ? sprEnchanted : sprGolden));
	}

	public String getItemNameIS(ItemStack stack) {
		int i = stack.getItemDamage();
		if(i > 2) {
			i = 2;
		}

		return this.getItemName() + i;
	}
}

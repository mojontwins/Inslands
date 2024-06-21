package net.minecraft.src;

public class EnchantmentDurability extends Enchantment {
	protected EnchantmentDurability(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.digger);
		this.setName("durability");
	}

	public int getMinEnchantability(int i1) {
		return 5 + (i1 - 1) * 10;
	}

	public int getMaxEnchantability(int i1) {
		return super.getMinEnchantability(i1) + 50;
	}

	public int getMaxLevel() {
		return 3;
	}
}

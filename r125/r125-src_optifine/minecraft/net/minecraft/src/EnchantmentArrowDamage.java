package net.minecraft.src;

public class EnchantmentArrowDamage extends Enchantment {
	public EnchantmentArrowDamage(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.bow);
		this.setName("arrowDamage");
	}

	public int getMinEnchantability(int i1) {
		return 1 + (i1 - 1) * 10;
	}

	public int getMaxEnchantability(int i1) {
		return this.getMinEnchantability(i1) + 15;
	}

	public int getMaxLevel() {
		return 5;
	}
}

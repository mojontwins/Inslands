package net.minecraft.src;

public class EnchantmentArrowKnockback extends Enchantment {
	public EnchantmentArrowKnockback(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.bow);
		this.setName("arrowKnockback");
	}

	public int getMinEnchantability(int i1) {
		return 12 + (i1 - 1) * 20;
	}

	public int getMaxEnchantability(int i1) {
		return this.getMinEnchantability(i1) + 25;
	}

	public int getMaxLevel() {
		return 2;
	}
}

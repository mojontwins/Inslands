package net.minecraft.src;

public class EnchantmentArrowFire extends Enchantment {
	public EnchantmentArrowFire(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.bow);
		this.setName("arrowFire");
	}

	public int getMinEnchantability(int i1) {
		return 20;
	}

	public int getMaxEnchantability(int i1) {
		return 50;
	}

	public int getMaxLevel() {
		return 1;
	}
}

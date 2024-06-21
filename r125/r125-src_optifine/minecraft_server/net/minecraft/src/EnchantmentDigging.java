package net.minecraft.src;

public class EnchantmentDigging extends Enchantment {
	protected EnchantmentDigging(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.digger);
		this.setName("digging");
	}

	public int getMinEnchantability(int i1) {
		return 1 + 15 * (i1 - 1);
	}

	public int getMaxEnchantability(int i1) {
		return super.getMinEnchantability(i1) + 50;
	}

	public int getMaxLevel() {
		return 5;
	}
}

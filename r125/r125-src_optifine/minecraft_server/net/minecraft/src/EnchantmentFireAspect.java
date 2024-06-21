package net.minecraft.src;

public class EnchantmentFireAspect extends Enchantment {
	protected EnchantmentFireAspect(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.weapon);
		this.setName("fire");
	}

	public int getMinEnchantability(int i1) {
		return 10 + 20 * (i1 - 1);
	}

	public int getMaxEnchantability(int i1) {
		return super.getMinEnchantability(i1) + 50;
	}

	public int getMaxLevel() {
		return 2;
	}
}

package net.minecraft.src;

public class EnchantmentKnockback extends Enchantment {
	protected EnchantmentKnockback(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.weapon);
		this.setName("knockback");
	}

	public int getMinEnchantability(int i1) {
		return 5 + 20 * (i1 - 1);
	}

	public int getMaxEnchantability(int i1) {
		return super.getMinEnchantability(i1) + 50;
	}

	public int getMaxLevel() {
		return 2;
	}
}

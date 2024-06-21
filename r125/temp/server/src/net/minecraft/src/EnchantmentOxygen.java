package net.minecraft.src;

public class EnchantmentOxygen extends Enchantment {
	public EnchantmentOxygen(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.armor_head);
		this.setName("oxygen");
	}

	public int getMinEnchantability(int i1) {
		return 10 * i1;
	}

	public int getMaxEnchantability(int i1) {
		return this.getMinEnchantability(i1) + 30;
	}

	public int getMaxLevel() {
		return 3;
	}
}

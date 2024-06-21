package net.minecraft.src;

public class EnchantmentWaterWorker extends Enchantment {
	public EnchantmentWaterWorker(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.armor_head);
		this.setName("waterWorker");
	}

	public int getMinEnchantability(int i1) {
		return 1;
	}

	public int getMaxEnchantability(int i1) {
		return this.getMinEnchantability(i1) + 40;
	}

	public int getMaxLevel() {
		return 1;
	}
}

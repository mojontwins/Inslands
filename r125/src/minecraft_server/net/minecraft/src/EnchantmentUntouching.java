package net.minecraft.src;

public class EnchantmentUntouching extends Enchantment {
	protected EnchantmentUntouching(int i1, int i2) {
		super(i1, i2, EnumEnchantmentType.digger);
		this.setName("untouching");
	}

	public int getMinEnchantability(int i1) {
		return 25;
	}

	public int getMaxEnchantability(int i1) {
		return super.getMinEnchantability(i1) + 50;
	}

	public int getMaxLevel() {
		return 1;
	}

	public boolean canApplyTogether(Enchantment enchantment1) {
		return super.canApplyTogether(enchantment1) && enchantment1.effectId != fortune.effectId;
	}
}

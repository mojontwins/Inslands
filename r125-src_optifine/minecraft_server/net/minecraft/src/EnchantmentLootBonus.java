package net.minecraft.src;

public class EnchantmentLootBonus extends Enchantment {
	protected EnchantmentLootBonus(int i1, int i2, EnumEnchantmentType enumEnchantmentType3) {
		super(i1, i2, enumEnchantmentType3);
		this.setName("lootBonus");
		if(enumEnchantmentType3 == EnumEnchantmentType.digger) {
			this.setName("lootBonusDigger");
		}

	}

	public int getMinEnchantability(int i1) {
		return 20 + (i1 - 1) * 12;
	}

	public int getMaxEnchantability(int i1) {
		return super.getMinEnchantability(i1) + 50;
	}

	public int getMaxLevel() {
		return 3;
	}

	public boolean canApplyTogether(Enchantment enchantment1) {
		return super.canApplyTogether(enchantment1) && enchantment1.effectId != silkTouch.effectId;
	}
}

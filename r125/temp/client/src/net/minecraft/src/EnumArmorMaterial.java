package net.minecraft.src;

public enum EnumArmorMaterial {
	CLOTH(5, new int[]{1, 3, 2, 1}, 15),
	CHAIN(15, new int[]{2, 5, 4, 1}, 12),
	IRON(15, new int[]{2, 6, 5, 2}, 9),
	GOLD(7, new int[]{2, 5, 3, 1}, 25),
	DIAMOND(33, new int[]{3, 8, 6, 3}, 10);

	private int maxDamageFactor;
	private int[] damageReductionAmountArray;
	private int enchantability;

	private EnumArmorMaterial(int i3, int[] i4, int i5) {
		this.maxDamageFactor = i3;
		this.damageReductionAmountArray = i4;
		this.enchantability = i5;
	}

	public int getDurability(int i1) {
		return ItemArmor.getMaxDamageArray()[i1] * this.maxDamageFactor;
	}

	public int getDamageReductionAmount(int i1) {
		return this.damageReductionAmountArray[i1];
	}

	public int getEnchantability() {
		return this.enchantability;
	}
}

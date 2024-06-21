package net.minecraft.src;

public class EnchantmentDamage extends Enchantment {
	private static final String[] protectionName = new String[]{"all", "undead", "arthropods"};
	private static final int[] baseEnchantability = new int[]{1, 5, 5};
	private static final int[] levelEnchantability = new int[]{16, 8, 8};
	private static final int[] thresholdEnchantability = new int[]{20, 20, 20};
	public final int damageType;

	public EnchantmentDamage(int i1, int i2, int i3) {
		super(i1, i2, EnumEnchantmentType.weapon);
		this.damageType = i3;
	}

	public int getMinEnchantability(int i1) {
		return baseEnchantability[this.damageType] + (i1 - 1) * levelEnchantability[this.damageType];
	}

	public int getMaxEnchantability(int i1) {
		return this.getMinEnchantability(i1) + thresholdEnchantability[this.damageType];
	}

	public int getMaxLevel() {
		return 5;
	}

	public int calcModifierLiving(int i1, EntityLiving entityLiving2) {
		return this.damageType == 0 ? i1 * 3 : (this.damageType == 1 && entityLiving2.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD ? i1 * 4 : (this.damageType == 2 && entityLiving2.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD ? i1 * 4 : 0));
	}

	public boolean canApplyTogether(Enchantment enchantment1) {
		return !(enchantment1 instanceof EnchantmentDamage);
	}
}

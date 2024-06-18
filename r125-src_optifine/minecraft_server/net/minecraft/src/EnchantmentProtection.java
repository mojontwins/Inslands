package net.minecraft.src;

public class EnchantmentProtection extends Enchantment {
	private static final String[] protectionName = new String[]{"all", "fire", "fall", "explosion", "projectile"};
	private static final int[] baseEnchantability = new int[]{1, 10, 5, 5, 3};
	private static final int[] levelEnchantability = new int[]{16, 8, 6, 8, 6};
	private static final int[] thresholdEnchantability = new int[]{20, 12, 10, 12, 15};
	public final int protectionType;

	public EnchantmentProtection(int i1, int i2, int i3) {
		super(i1, i2, EnumEnchantmentType.armor);
		this.protectionType = i3;
		if(i3 == 2) {
			this.type = EnumEnchantmentType.armor_feet;
		}

	}

	public int getMinEnchantability(int i1) {
		return baseEnchantability[this.protectionType] + (i1 - 1) * levelEnchantability[this.protectionType];
	}

	public int getMaxEnchantability(int i1) {
		return this.getMinEnchantability(i1) + thresholdEnchantability[this.protectionType];
	}

	public int getMaxLevel() {
		return 4;
	}

	public int calcModifierDamage(int i1, DamageSource damageSource2) {
		if(damageSource2.canHarmInCreative()) {
			return 0;
		} else {
			int i3 = (6 + i1 * i1) / 2;
			return this.protectionType == 0 ? i3 : (this.protectionType == 1 && damageSource2.fireDamage() ? i3 : (this.protectionType == 2 && damageSource2 == DamageSource.fall ? i3 * 2 : (this.protectionType == 3 && damageSource2 == DamageSource.explosion ? i3 : (this.protectionType == 4 && damageSource2.isProjectile() ? i3 : 0))));
		}
	}

	public boolean canApplyTogether(Enchantment enchantment1) {
		if(enchantment1 instanceof EnchantmentProtection) {
			EnchantmentProtection enchantmentProtection2 = (EnchantmentProtection)enchantment1;
			return enchantmentProtection2.protectionType == this.protectionType ? false : this.protectionType == 2 || enchantmentProtection2.protectionType == 2;
		} else {
			return super.canApplyTogether(enchantment1);
		}
	}
}

package net.minecraft.src;

final class EnchantmentModifierDamage implements IEnchantmentModifier {
	public int damageModifier;
	public DamageSource damageSource;

	private EnchantmentModifierDamage() {
	}

	public void calculateModifier(Enchantment enchantment1, int i2) {
		this.damageModifier += enchantment1.calcModifierDamage(i2, this.damageSource);
	}

	EnchantmentModifierDamage(Empty3 empty31) {
		this();
	}
}

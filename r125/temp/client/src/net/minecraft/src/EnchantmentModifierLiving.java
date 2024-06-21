package net.minecraft.src;

final class EnchantmentModifierLiving implements IEnchantmentModifier {
	public int livingModifier;
	public EntityLiving entityLiving;

	private EnchantmentModifierLiving() {
	}

	public void calculateModifier(Enchantment enchantment1, int i2) {
		this.livingModifier += enchantment1.calcModifierLiving(i2, this.entityLiving);
	}

	EnchantmentModifierLiving(Empty3 empty31) {
		this();
	}
}

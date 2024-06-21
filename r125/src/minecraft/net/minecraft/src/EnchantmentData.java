package net.minecraft.src;

public class EnchantmentData extends WeightedRandomChoice {
	public final Enchantment enchantmentobj;
	public final int enchantmentLevel;

	public EnchantmentData(Enchantment enchantment1, int i2) {
		super(enchantment1.getWeight());
		this.enchantmentobj = enchantment1;
		this.enchantmentLevel = i2;
	}
}

package net.minecraft.src;

public class Enchantment {
	public ItemStack enchantFrom;
	public ItemStack enchantTo;
	public int enchantPowerNeeded;

	public Enchantment(ItemStack from, ItemStack to, int i) {
		this.enchantFrom = from;
		this.enchantTo = to;
		this.enchantPowerNeeded = i;
	}
}

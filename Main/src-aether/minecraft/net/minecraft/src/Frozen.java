package net.minecraft.src;

public class Frozen {
	public ItemStack frozenFrom;
	public ItemStack frozenTo;
	public int frozenPowerNeeded;

	public Frozen(ItemStack from, ItemStack to, int i) {
		this.frozenFrom = from;
		this.frozenTo = to;
		this.frozenPowerNeeded = i;
	}
}

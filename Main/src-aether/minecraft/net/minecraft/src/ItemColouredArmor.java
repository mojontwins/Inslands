package net.minecraft.src;

public class ItemColouredArmor extends ItemArmor {
	private int colour;

	public ItemColouredArmor(int i, int j, int k, int l, int col) {
		super(i, j, k, l);
		this.colour = col;
	}

	public int getColorFromDamage(int i) {
		return this.colour;
	}
}

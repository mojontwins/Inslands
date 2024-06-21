package net.minecraft.src;

public class StatCrafting extends StatBase {
	private final int itemID;

	public StatCrafting(int i1, String string2, int i3) {
		super(i1, string2);
		this.itemID = i3;
	}

	public int getItemID() {
		return this.itemID;
	}
}

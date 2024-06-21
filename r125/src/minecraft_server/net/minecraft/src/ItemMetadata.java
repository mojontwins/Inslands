package net.minecraft.src;

public class ItemMetadata extends ItemBlock {
	private Block blockObj;

	public ItemMetadata(int i1, Block block2) {
		super(i1);
		this.blockObj = block2;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getMetadata(int i1) {
		return i1;
	}
}

package net.minecraft.src;

public class ItemGrass extends ItemBlock {

	public ItemGrass(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getIconFromDamage(int i1) {
		return i1 == 0 ? 3 : 0;
	}

	public int getPlacedBlockMetadata(int i1) {
		return i1;
	}
}

package net.minecraft.world.item;

import net.minecraft.src.Block;

public class ItemIce extends ItemBlock {

	public ItemIce(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getIconFromDamage(int i1) {
		return Block.ice.blockIndexInTexture;
	}

	public int getPlacedBlockMetadata(int i1) {
		return i1;
	}
}

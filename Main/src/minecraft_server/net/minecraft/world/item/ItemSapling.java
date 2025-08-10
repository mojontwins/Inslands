package net.minecraft.world.item;

import net.minecraft.src.Block;

public class ItemSapling extends ItemBlock {
	public ItemSapling(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getPlacedBlockMetadata(int i1) {
		return i1;
	}

	public int getIconFromDamage(int i1) {
		return Block.sapling.getBlockTextureFromSideAndMetadata(0, i1);
	}
}

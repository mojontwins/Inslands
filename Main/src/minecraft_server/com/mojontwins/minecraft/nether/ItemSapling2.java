package com.mojontwins.minecraft.nether;

import net.minecraft.src.Block;
import net.minecraft.world.item.ItemBlock;

public class ItemSapling2 extends ItemBlock {

	public ItemSapling2(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getPlacedBlockMetadata(int i1) {
		return i1;
	}

	public int getIconFromDamage(int i1) {
		return Block.sapling2.getBlockTextureFromSideAndMetadata(0, i1);
	}

}

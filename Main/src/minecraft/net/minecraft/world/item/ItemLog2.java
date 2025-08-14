package net.minecraft.world.item;

import net.minecraft.world.level.tile.Block;

public class ItemLog2 extends ItemBlock {

	public ItemLog2(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getIconFromDamage(int i1) {
		return Block.wood2.getBlockTextureFromSideAndMetadata(2, i1);
	}

	public int getPlacedBlockMetadata(int i1) {
		return i1;
	}

}

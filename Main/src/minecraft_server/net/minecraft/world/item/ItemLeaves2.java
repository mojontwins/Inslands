package net.minecraft.world.item;

import net.minecraft.world.level.tile.Block;

public class ItemLeaves2 extends ItemBlock {

	public ItemLeaves2(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getPlacedBlockMetadata(int i1) {
		return i1 | 8;
	}

	public int getIconFromDamage(int i1) {
		return Block.leaves2.getBlockTextureFromSideAndMetadata(0, i1);
	}

}

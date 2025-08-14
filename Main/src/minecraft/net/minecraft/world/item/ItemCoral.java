package net.minecraft.world.item;

import net.minecraft.world.level.tile.Block;

public class ItemCoral extends ItemBlock {
	public ItemCoral(int var1) {
		super(var1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	public int getIconFromDamage(int damage) {
		return Block.coral.getBlockTextureFromSideAndMetadata(2, damage);
	}

	public int getPlacedBlockMetadata(int var1) {
		return var1;
	}

	public String getItemNameIS(ItemStack var1) {
		switch(var1.itemDamage) {
			case 0: return "Pink Coral";
			case 1: return "Yellow Coral";
			case 2: return "Blue Coral";
			default: return "Coral";
		}
	}
}

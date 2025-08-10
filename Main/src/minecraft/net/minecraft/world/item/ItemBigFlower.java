package net.minecraft.world.item;

import net.minecraft.src.Block;

public class ItemBigFlower extends ItemBlock {

	public ItemBigFlower(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	public int getIconFromDamage(int damage) {
		return Block.bigFlower.getBlockTextureFromSideAndMetadata(2, damage);
	}

	public int getPlacedBlockMetadata(int var1) {
		return var1;
	}

	public String getItemNameIS(ItemStack var1) {
		switch(var1.itemDamage) {
			case 0: return "tile.redFlowerPetal";
			case 1: return "tile.yellowFlowerPetal";
			case 2: return "tile.stem";
			default: return "tile.bigFlower";
		}
	}
}

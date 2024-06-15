package net.minecraft.src;

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
		System.out.println("placed block metadata " + var1);
		return var1;
	}

	public String getItemNameIS(ItemStack var1) {
		switch(var1.itemDamage) {
			case 0: return "Red flower petal";
			case 1: return "Yellow flower petal";
			case 2: return "Stem";
			default: return "Big Flower";
		}
	}
}

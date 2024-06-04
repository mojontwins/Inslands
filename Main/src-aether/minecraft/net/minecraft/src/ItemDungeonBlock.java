package net.minecraft.src;

public class ItemDungeonBlock extends ItemBlock {
	public ItemDungeonBlock(int i) {
		super(i);
		this.setHasSubtypes(true);
	}

	public int getPlacedBlockMetadata(int i) {
		return i;
	}

	public String getItemNameIS(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		if(i > 2) {
			i = 2;
		}

		return this.getItemName() + i;
	}
}

package net.minecraft.world.item;

import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;

public class ItemCloth extends ItemBlock {
	public ItemCloth(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getIconFromDamage(int i1) {
		return Block.cloth.blockIndexInTexture;
	}

	@Override
	public int getPlacedBlockMetadata(int i1) {
		return i1;
	}

	@Override
	public String getItemNameIS(ItemStack itemStack1) {
		return super.getItemName() + "." + ItemDye.dyeColorNames[BlockCloth.getBlockFromDye(itemStack1.getItemDamage())];
	}
}

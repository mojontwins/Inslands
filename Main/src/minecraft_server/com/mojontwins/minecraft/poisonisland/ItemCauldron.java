package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemCauldron extends ItemBlock {

	private static String[] contentsNames = new String[] {
		"empty",
		"water",
		"acid",
		"soup",
		"goo",
		"poison"
	};

	public ItemCauldron(int id) {
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack itemStack) {
		return super.getItemName() + "." + ItemCauldron.contentsNames[itemStack.getItemDamage() >> 2];
	}
}

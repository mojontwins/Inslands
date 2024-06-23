package com.mojang.minecraft.creative;

import net.minecraft.src.Item;

final class CreativeTabMisc extends CreativeTabs {
	CreativeTabMisc(int par1, String par2Str) {
		super(par1, par2Str);
	}

	/**
	 * the itemID for the item to be displayed on the tab
	 */
	public int getTabIconItemIndex() {
		return Item.bucketLava.shiftedIndex;
	}
}

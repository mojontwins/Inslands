package com.mojontwins.minecraft.worldedit;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemMagicWand extends Item {

	protected ItemMagicWand(int i1) {
		super(i1);
	}
	
	// Right click: Set corner 2
	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7, float xWithinFace, float yWithinFace, float zWithinFace) {
		return true;
	}

}

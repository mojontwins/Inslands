package com.mojontwins.minecraft.worldedit;

import net.minecraft.src.World;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemMagicWand extends Item {

	public ItemMagicWand(int i1) {
		super(i1);
	}
	
	// Left click: Set corner 1
	public boolean onItemLeftClick(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int face, float xWithinFace, float yWithinFace, float zWithinFace) {
		WorldEdit.setCorner1(x, y, z);
		world.getWorldAccess(0).showChatMessage("1st point set to " + x + ", " + y + ", " + z);
		return true;
	}
	
	// Right click: Set corner 2
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int face, float xWithinFace, float yWithinFace, float zWithinFace) {
		WorldEdit.setCorner2(x, y, z);
		world.getWorldAccess(0).showChatMessage("2nd point set to " + x + ", " + y + ", " + z);
		return true;
	}

}

package com.benimatic.twilightforest;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemTool;
import net.minecraft.world.level.material.Material;

public class BlockTFMazestone extends Block {
	static int[] mimicIDs = new int[]{Block.stoneBricks.blockID, Block.cobblestone.blockID, Block.cobblestoneMossy.blockID};

	public BlockTFMazestone(int id, int texture) {
		super(id, texture, Material.rock);
	}

	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		Block mimic = Block.blocksList[mimicIDs[meta]];
		return mimic != null && mimic.isOpaqueCube() ? mimic.getBlockTextureFromSide(side) : super.getBlockTextureFromSideAndMetadata(side, meta);
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta) {
		ItemStack cei = entityplayer.getCurrentEquippedItem();
		if(cei != null && Item.itemsList[cei.itemID] instanceof ItemTool) {
			cei.damageItem(8, entityplayer);
		}

		Block mimic = Block.blocksList[mimicIDs[meta]];
		if(mimic != null) {
			mimic.harvestBlock(world, entityplayer, x, y, z, 0);
		} else {
			super.harvestBlock(world, entityplayer, x, y, z, meta);
		}

	}
}

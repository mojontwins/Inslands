package com.benimatic.twilightforest;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemTool;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.world.entity.player.EntityPlayer;

public class BlockTFMazestone2 extends Block {
	static int[] mimicIDs = new int[]{Block.stone.blockID, Block.stoneBricks.blockID, Block.stairDouble.blockID, Block.brick.blockID, Block.cobblestone.blockID, Block.cobblestoneMossy.blockID, Block.grass.blockID, Block.planks.blockID};

	public BlockTFMazestone2(int id, int texture) {
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

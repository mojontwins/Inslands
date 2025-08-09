package com.mojontwins.minecraft.poisonisland;

import java.util.ArrayList;
import java.util.List;

import com.mojang.minecraft.creative.CreativeTabs;
import com.mojontwins.minecraft.blockmodels.BlockModel;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;

public class BlockCauldron extends Block {
	BlockModel blockModel;
	public static int[] cauldronContents = new int[] {
		-1, 			// empty  0
		12 * 16 + 13, 	// water  1
		9 * 16 + 6, 	// acid   2
		9 * 16 + 11, 	// soup   3
		9 * 16 + 7, 	// goo    4
		9 * 16 + 10 	// poison 5
	};
	
	public BlockCauldron(int blockID, int blockIndexInTexture) {
		super(blockID, blockIndexInTexture, Material.wood);
		this.blockModel = new BlockModel("/resources/blocks/models/blockCauldron.json");
		
		// System.out.println ("BlockCauldron " + this.blockModel);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
		int meta = world.getBlockMetadata(x, y, z);
        world.setBlockMetadata(x, y, z, (meta & 0xFC) | BlockModel.angleToMeta((MathHelper.floor_double((double)((entityLiving.rotationYaw * 4F) / 360F) + .5D) + 2) & 3));

	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, ItemStack itemStack) {
		// Doing this here so I don't have to modify vanilla classes
		if(itemStack == null) return false;
		
		int meta = world.getBlockMetadata(x, y, z);
		int contents = meta >> 2;
		int newContents = contents;
System.out.println ("Stack = " + itemStack.getItem() + " meta = " + meta + " contents = " + contents);		
		switch(contents) {
		case 1:		
			// Water
			if(itemStack.itemID == Block.mushroomCapBrown.blockID ||
			itemStack.itemID == Item.chickenRaw.shiftedIndex) {
				// + capBrown = soup;
				// + chicken = soup;

				newContents = 3;
			}
			break;
			
		case 2:
			// Acid
			if(itemStack.itemID == Block.mushroomCapBrown.blockID) {
				// + capBrown = water;
				
				newContents = 1; 
			} else if(itemStack.itemID == Block.mushroomCapGreen.blockID) {
				// + capGreen = 50% chance poison / goo
				
				newContents = world.rand.nextInt(4) == 0 ? 4 : 5;
			} else {
				// + anything else: goo
				
				newContents = 4;
			}
			break;
			
		}
		
		if(newContents != contents) {
			meta = (meta & 3) | (newContents << 2);
			
			world.setBlockAndMetadataWithNotify(x, y, z, this.blockID, meta);
			if(!entityPlayer.isCreative ) entityPlayer.getHeldItem().stackSize --;
			world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.splash", world.rand.nextFloat() * 0.25F + 0.75F,  world.rand.nextFloat() + 0.5F);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public BlockModel getBlockModel() {
		return this.blockModel;
	}
	
	@Override
	public int getRenderType() {
		return 250;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override 
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList<AxisAlignedBB> collidingBoundingBoxes) { 
		this.blockModel.getCollidingBoundingBoxes(world, x, y, z, aabb, collidingBoundingBoxes);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return this.blockModel.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	
	@Override
	protected int damageDropped(int meta) {
		// Just clear orientation bits
		
		return meta & 0xFC;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return cauldronContents[meta >> 2];
	}
	
	@Override
	public void getSubBlocks(int id, CreativeTabs creativeTabs, List<ItemStack> list) {
		for(int i = 0; i < cauldronContents.length; i ++) {
			list.add(new ItemStack(id, 1, i << 2));
		}
	}
}

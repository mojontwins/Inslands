package com.mojontwins.minecraft.poisonisland;

import java.util.ArrayList;

import com.mojontwins.minecraft.blockmodels.BlockModel;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class BlockSmallHead extends Block {
	BlockModel blockModel;
	
	public BlockSmallHead(int blockID, int blockIndexInTexture) {
		super(blockID, blockIndexInTexture, Material.wood);
		this.blockModel = new BlockModel("/resources/blocks/models/blockSkeletonSkull.json");
		
		// System.out.println ("BlockSmallHead " + this.blockModel);
	}	

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving) {
        int facing = (MathHelper.floor_double((double)((entityLiving.rotationYaw * 4F) / 360F) + .5D) + 2) & 3;
        world.setBlockMetadata(x, y, z, facing);

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
}

package com.mojontwins.minecraft.poisonisland;

import java.util.ArrayList;

import com.mojontwins.minecraft.blockmodels.BlockModel;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
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
        world.setBlockMetadata(x, y, z, BlockModel.angleToMeta((MathHelper.floor_double((double)((entityLiving.rotationYaw * 4F) / 360F) + .5D) + 2) & 3));

	}
	

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		// Detect if it's on top of two diamond blocks
		// And there's 1 block surrounding to each direction
		
		for (int i = x - 1; i <= x + 1; i ++) {
			for (int j = y - 2; j <= y; j ++) {
				for (int k = z - 1; k <= z + 1; k ++)  {
					int blockID = world.getBlockId(i, j, k);
					if (j < y && i == x && k == z) {
						if (blockID != Block.blockDiamond.blockID) return;
					} else if ((i != x || k != z) && (blockID != 0)) return;    				
				}
			}
		}
		
		// If we get to this point the condition is fulfilled.
		
		// Destroy the blocks
		for (int j = y - 2; j <= y; j ++) {
			world.spawnParticle("largesmoke", 
					(double)x + world.rand.nextDouble (), 
					(double)j + world.rand.nextDouble (),
					(double)z + world.rand.nextDouble (),
					0.0, 0.0, 0.0);
			world.setBlockWithNotify(x, j, z, 0);
		}
		
		// Add the entity
		Entity entityDiamondSkeleton = new EntityDiamondSkeleton (world);
		entityDiamondSkeleton.setPositionAndRotation (x, y, z, 0.0F, 0.0F);
		world.playSoundAtEntity(entityDiamondSkeleton, "random.explode", 0.5F, 1.0F);
		world.spawnEntityInWorld(entityDiamondSkeleton);
		
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

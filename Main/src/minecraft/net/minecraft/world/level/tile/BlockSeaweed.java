package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AxisAlignedBB;

public class BlockSeaweed extends Block {

	public BlockSeaweed(int id, int blockIndex) {
		super(id, blockIndex, Material.water);
		this.setTickOnLoad(true);
		
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}

	// This block can be placed if:
	// - replacing a water block
	// - a water block on top
	// - a valid block beneath
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getblockID(x, y, z) == Block.waterStill.blockID 
        		&& world.getblockID(x, y + 1, z) == Block.waterStill.blockID 
        		&& canThisPlantGrowOnThisblockID(world.getblockID(x, y - 1, z));
    }
    
    protected boolean canThisPlantGrowOnThisblockID(int par1) {
        return par1 == this.blockID || par1 == Block.dirt.blockID 
        		|| par1 == Block.sand.blockID
        		|| par1 == Block.stone.blockID;
    }    
    
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborblockID) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, Block.waterStill.blockID);
        }
    }
    
    public void updateTick(World world, int x, int y, int z, Random rand) {
    	if (rand.nextInt (32) == 0) {
	    	if (world.getblockID(x, y + 1, z) == Block.waterStill.blockID && world.getblockID(x, y + 2, z) == Block.waterStill.blockID ) {
	    		world.setBlockWithNotify(x, y + 1, z, blockID);
	    	}
    	}
    }
    
    public boolean canBlockStay(World world, int x, int y, int z) {
    	// This block can stay if
    	// - there's water or same on top
    	// - there's valid beneath
    	int blockOnTop = world.getblockID(x, y + 1, z);
    	if(! (blockOnTop == this.blockID || blockOnTop == Block.waterStill.blockID || blockOnTop == Block.waterMoving.blockID)) return false;
    	return canThisPlantGrowOnThisblockID(world.getblockID(x, y - 1, z));
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int i) {
        return null;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public int getRenderType() {
        return 1;
    }
    
	public int idDropped(int par1, Random par2Random)	{
		return Item.kelp.shiftedIndex;
	}
}

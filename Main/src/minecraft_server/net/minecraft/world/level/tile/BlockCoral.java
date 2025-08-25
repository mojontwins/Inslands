package net.minecraft.world.level.tile;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AxisAlignedBB;

public class BlockCoral extends Block implements IBlockWithSubtypes {
	
	public static String[] coralNames = new String[] {
			"pink", "yello", "blue"
	};
	
	/*
	 * Coral metadata will be 8, 9, 10 to make it compatible with flowing water.
	 */
	public BlockCoral(int id, int blockIndex) {
		super(id, blockIndex, Material.water); 

		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}
	
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getblockID(x, y, z) == Block.waterStill.blockID 
        		&& world.getblockID(x, y + 1, z) == Block.waterStill.blockID 
        		&& canThisPlantGrowOnThisblockID(world.getblockID(x, y - 1, z));
    }
    
    protected boolean canThisPlantGrowOnThisblockID(int par1) {
    	return Block.opaqueCubeLookup[par1];
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborblockID) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, Block.waterStill.blockID);
        }
    }
    
    public boolean canBlockStay(World world, int x, int y, int z) {
    	int blockOnTop = world.getblockID(x, y + 1, z);
    	if(! (blockOnTop == Block.waterStill.blockID || blockOnTop == Block.waterMoving.blockID)) return false;
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
    
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
    	return this.blockIndexInTexture + (meta & 7);
    }
    
	protected int damageDropped(int var1) {
		return var1;
	}
	
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 3; i ++) {
			par3List.add(new ItemStack(par1, 1, 8 + i));
		}
	}

	@Override
	public int getItemblockID() {
		return this.blockID - 256;
	}

	@Override
	public String getNameFromMeta(int meta) {
		if(meta < 8 || meta > 10) return null;
		return "coral." + BlockCoral.coralNames [meta - 8];
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return this.blockIndexInTexture + (meta & 7);
	}
}

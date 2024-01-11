package net.minecraft.src;

public class BlockCoral extends Block {
	/*
	 * Coral metadata will be 8, 9, 10 to make it compatible with flowing water.
	 */
	public BlockCoral(int id, int blockIndex) {
		super(id, blockIndex, Material.water); 
	}
	
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlockId(x, y, z) == Block.waterStill.blockID 
        		&& world.getBlockId(x, y + 1, z) == Block.waterStill.blockID 
        		&& canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
    }
    
    protected boolean canThisPlantGrowOnThisBlockID(int par1) {
    	return Block.opaqueCubeLookup[par1];
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, Block.waterStill.blockID);
        }
    }
    
    public boolean canBlockStay(World world, int x, int y, int z) {
    	int blockOnTop = world.getBlockId(x, y + 1, z);
    	if(! (blockOnTop == Block.waterStill.blockID || blockOnTop == Block.waterMoving.blockID)) return false;
    	return canThisPlantGrowOnThisBlockID(world.getBlockId(x, y - 1, z));
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
}

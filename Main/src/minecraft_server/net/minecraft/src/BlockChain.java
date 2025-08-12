package net.minecraft.src;

import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockChain extends Block {
	public BlockChain(int i1, int i2, Material material3) {
		super(i1, i2, material3);
		float f3 = 0.2F;
		this.setTickOnLoad(false);
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, 1.0F, 0.5F + f3);
		
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}
	
	@Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return this.canBlockStay(world, x, y, z);
    }

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		Block above = Block.blocksList[world.getBlockId(x, y + 1, z)];
		return above != null && (above.isOpaqueCube() || above.blockID == Block.chain.blockID || (above instanceof BlockFence));
	}
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
        if (!canBlockStay(world, x, y, z)) {
        	world.playAuxSFX(2001, x, y, z, blockID);
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
            world.setBlockWithNotify(x, y, z, 0);
        }
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
	public int getRenderType() {
		return 110;
	}
	
	public boolean seeThrough() {
		return true; 
	}
}

package net.minecraft.src;

public class BlockIcePacked extends BlockIce {
	public BlockIcePacked(int blockID, int blockIndex) {
		super(blockID, blockIndex);
		this.setTickOnLoad(false);
	}

	@Override
	public int getRenderBlockPass() {
		return 0;
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, int x, int y, int z, int side) {
	}
	
	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getRenderColor(0);
	}
	
	@Override
	public int getRenderColor(int meta) {
		return 0xEEEEFF;
	}
}

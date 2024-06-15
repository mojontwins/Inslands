package net.minecraft.src;

public class BlockZanite extends Block {
	protected BlockZanite(int blockID, int j) {
		super(blockID, j, Material.rock);
	}

	public int getRenderColor(int i) {
		return 10066431;
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return this.getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
	}
}

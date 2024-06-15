package net.minecraft.src;

public class BlockEnchantedGravitite extends BlockFloating {
	public BlockEnchantedGravitite(int i, int j, boolean bool) {
		super(i, j, bool);
	}

	public int getRenderColor(int i) {
		return 16755455;
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return this.getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
	}
}

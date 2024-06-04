package net.minecraft.src;

import java.util.Random;

public class BlockQuicksoilGlass extends BlockBreakable {
	public BlockQuicksoilGlass(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/QuicksoilGlass.png"), Material.glass, false);
		this.slipperiness = 1.05F;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
	}

	public int getRenderColor(int i) {
		return 16776960;
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return this.getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
	}
}

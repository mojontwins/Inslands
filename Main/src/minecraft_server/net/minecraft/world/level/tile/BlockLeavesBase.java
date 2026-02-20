package net.minecraft.world.level.tile;

import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.material.Material;

public class BlockLeavesBase extends Block {
	protected static boolean graphicsLevel;

	protected BlockLeavesBase(int id, int blockIndex, Material material, boolean agraphicsLevel) {
		super(id, blockIndex, material);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int i6 = blockAccess.getBlockID(x, y, z);
		return !graphicsLevel && i6 == this.blockID ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}

	public static void setGraphicsLevel(boolean flag) {
		graphicsLevel = flag;
	}
}

package net.minecraft.src;

import java.util.Random;

public class BlockStone extends Block {
	public static final int[] stoneColor = new int [] {
		0xFFFFFF,
		0xCCCCCC
	};
	
	public BlockStone(int id, int blockIndex) {
		super(id, blockIndex, Material.rock);
	}

	public int idDropped(int metadata, Random rand) {
		return Block.cobblestone.blockID;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getRenderColor(blockAccess.getBlockMetadata(x, y, z));
	}

	@Override
	public int getRenderColor(int meta) {
		return stoneColor[(meta >> 4) & 7];
	}
}

package net.minecraft.src;

import java.util.Random;

public class BlockStone extends Block {
	public BlockStone(int id, int blockIndex) {
		super(id, blockIndex, Material.rock);
	}

	public int idDropped(int metadata, Random rand) {
		return Block.cobblestone.blockID;
	}
}

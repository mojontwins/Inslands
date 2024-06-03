package net.minecraft.src;

import java.util.Random;

public class BlockClay extends Block {
	public BlockClay(int id, int blockIndex) {
		super(id, blockIndex, Material.clay);
	}

	public int idDropped(int metadata, Random rand) {
		return Item.clay.shiftedIndex;
	}

	public int quantityDropped(Random rand) {
		return 4;
	}
}

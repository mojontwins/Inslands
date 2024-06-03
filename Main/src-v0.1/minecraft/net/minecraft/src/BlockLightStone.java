package net.minecraft.src;

import java.util.Random;

public class BlockLightStone extends Block {
	public BlockLightStone(int i, int j, Material material) {
		super(i, j, material);
	}

	public int quantityDropped(Random Random) {
		return 2 + Random.nextInt(3);
	}

	public int idDropped(int i, Random Random) {
		return Item.lightStoneDust.shiftedIndex;
	}
}

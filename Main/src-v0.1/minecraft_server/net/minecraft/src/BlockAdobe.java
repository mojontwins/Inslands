package net.minecraft.src;

import java.util.Random;

public class BlockAdobe extends Block {
	protected BlockAdobe(int id, int blockIndexInTexture) {
		super(id, blockIndexInTexture, Material.grass);
	}

	public int idDropped(int metadata, Random rand) {
		return Block.dirt.blockID;
	}
}

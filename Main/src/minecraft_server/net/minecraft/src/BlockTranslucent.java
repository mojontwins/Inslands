package net.minecraft.src;

import net.minecraft.world.level.material.Material;

public class BlockTranslucent extends Block {

	public BlockTranslucent(int blockID, int textureID, Material material) {
		super(blockID, textureID, material);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}
}

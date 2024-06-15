package net.minecraft.src;

import java.util.Random;

public class BlockZaniteOre extends Block {
	protected BlockZaniteOre(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/ZaniteOre.png"), Material.rock);
	}

	public int idDropped(int i, Random random) {
		return AetherItems.Zanite.shiftedIndex;
	}
}

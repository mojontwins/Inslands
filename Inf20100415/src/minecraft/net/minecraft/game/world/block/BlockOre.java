package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.item.Item;
import net.minecraft.game.world.material.Material;

public final class BlockOre extends Block {
	public BlockOre(int i1, int i2) {
		super(i1, i2, Material.rock);
	}

	public final int idDropped(int i1, Random random2) {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamod.shiftedIndex : this.blockID);
	}

	public final int quantityDropped(Random random1) {
		return 1;
	}
}
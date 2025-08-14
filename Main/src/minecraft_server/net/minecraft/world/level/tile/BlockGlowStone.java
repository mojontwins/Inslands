package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockGlowStone extends Block {
	public BlockGlowStone(int i1, int i2, Material material3) {
		super(i1, i2, material3);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int quantityDropped(Random random1) {
		return 2 + random1.nextInt(3);
	}

	public int idDropped(int i1, Random random2) {
		return Item.lightStoneDust.shiftedIndex;
	}
}

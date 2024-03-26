package net.minecraft.src;

import java.util.Random;

import com.mojang.minecraft.creative.CreativeTabs;

public class BlockLightStone extends Block {
	public BlockLightStone(int i, int j, Material material) {
		super(i, j, material);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int quantityDropped(Random Random) {
		return 2 + Random.nextInt(3);
	}

	public int idDropped(int i, Random Random) {
		return Item.lightStoneDust.shiftedIndex;
	}
}

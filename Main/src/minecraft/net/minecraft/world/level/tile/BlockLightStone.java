package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

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

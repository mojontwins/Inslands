package net.minecraft.src;

import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockClay extends Block {
	public BlockClay(int id, int blockIndex) {
		super(id, blockIndex, Material.clay);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int idDropped(int metadata, Random rand) {
		return Item.clay.shiftedIndex;
	}

	public int quantityDropped(Random rand) {
		return 4;
	}
}

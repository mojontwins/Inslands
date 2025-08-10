package net.minecraft.src;

import java.util.Random;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.world.item.Item;

public class BlockRegolith extends Block {
	public BlockRegolith(int id, int blockIndex) {
		super(id, blockIndex, Material.grass);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
	
	public int quantityDropped(Random rand) {
		return 1 + rand.nextInt(4);
	}

	public int idDropped(int metadata, Random rand) {
		return Item.pebble.shiftedIndex;
	}
	
	public boolean canGrowPlants() {
		return true;
	}
}

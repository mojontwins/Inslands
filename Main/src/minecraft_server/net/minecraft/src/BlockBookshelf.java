package net.minecraft.src;

import java.util.Random;

import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockBookshelf extends Block {
	public BlockBookshelf(int id, int blockIndex) {
		super(id, blockIndex, Material.wood);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int getBlockTextureFromSide(int side) {
		return side <= 1 ? 4 : this.blockIndexInTexture;
	}

	public int quantityDropped(Random rand) {
		return 0;
	}
}

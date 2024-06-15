package net.minecraft.src;

import java.util.Random;

import com.mojang.minecraft.creative.CreativeTabs;

public class BlockAdobe extends Block {
	protected BlockAdobe(int id, int blockIndexInTexture) {
		super(id, blockIndexInTexture, Material.grass);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int idDropped(int metadata, Random rand) {
		return Block.dirt.blockID;
	}
}

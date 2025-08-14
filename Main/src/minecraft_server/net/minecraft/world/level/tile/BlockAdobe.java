package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockAdobe extends Block {
	protected BlockAdobe(int id, int blockIndexInTexture) {
		super(id, blockIndexInTexture, Material.grass);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int idDropped(int metadata, Random rand) {
		return Block.dirt.blockID;
	}
}

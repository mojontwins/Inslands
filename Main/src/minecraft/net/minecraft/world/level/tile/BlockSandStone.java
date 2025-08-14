package net.minecraft.world.level.tile;

import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockSandStone extends Block {
	public BlockSandStone(int i1) {
		super(i1, 192, Material.rock);

		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	/*
	public int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? this.blockIndexInTexture - 16 : (i1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
	*/
}

package net.minecraft.src;

import com.mojang.minecraft.creative.CreativeTabs;

public class BlockNetherrack extends Block {
	public BlockNetherrack(int i1, int i2) {
		super(i1, i2, Material.rock);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
}

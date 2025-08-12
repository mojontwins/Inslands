package net.minecraft.src;

import net.minecraft.world.level.creative.CreativeTabs;

public class BlockNetherrack extends Block {
	public BlockNetherrack(int i1, int i2) {
		super(i1, i2, Material.rock);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
}

package net.minecraft.src;

import java.util.Random;

import net.minecraft.world.level.creative.CreativeTabs;

public class BlockGlass extends BlockBreakable {
	public BlockGlass(int i1, int i2, Material material3, boolean z4) {
		super(i1, i2, material3, z4);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public int getRenderBlockPass() {
		return 0;
	}
	
	public boolean seeThrough() {
		return true; 
	}
}

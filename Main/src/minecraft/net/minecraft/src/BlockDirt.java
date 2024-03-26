package net.minecraft.src;

import com.mojang.minecraft.creative.CreativeTabs;

public class BlockDirt extends Block {
	protected BlockDirt(int i1, int i2) {
		super(i1, i2, Material.ground);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
	
	public boolean canGrowPlants() {
		return true;
	}	
}

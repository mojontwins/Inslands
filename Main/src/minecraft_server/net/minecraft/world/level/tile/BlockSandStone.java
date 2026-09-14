package net.minecraft.world.level.tile;

import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockSandStone extends Block implements IGroundSubstitute {
	public BlockSandStone(int i1) {
		super(i1, 192, Material.rock);

		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
}


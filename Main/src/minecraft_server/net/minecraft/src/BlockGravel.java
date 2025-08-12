package net.minecraft.src;

import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockGravel extends BlockSand {
	public BlockGravel(int i1, int i2) {
		super(i1, i2);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int idDropped(int metadata, Random rand) {
		return rand.nextInt(10) == 0 ? Item.flint.shiftedIndex : this.blockID;
	}
}

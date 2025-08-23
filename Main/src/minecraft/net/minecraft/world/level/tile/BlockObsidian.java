package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockObsidian extends BlockStone {
	public BlockObsidian(int i1, int i2) {
		super(i1, i2);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int quantityDropped(Random rand) {
		return 1;
	}

	public int idDropped(int metadata, Random rand) {
		return Block.obsidian.blockID;
	}
	
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
}

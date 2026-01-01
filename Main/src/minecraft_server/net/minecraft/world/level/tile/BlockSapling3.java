package net.minecraft.world.level.tile;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockSapling3 extends BlockSapling {

	public BlockSapling3(int i1) {
		super(i1, 0);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return 80 + super.getBlockTextureFromSideAndMetadata(side, meta);
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 3; i ++) {
			par3List.add(new ItemStack(par1, 1, i<<4));
		}
	}
    
}

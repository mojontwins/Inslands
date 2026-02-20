package net.minecraft.world.level.tile;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockLog3 extends BlockLog {

	// New version
	// Wood meta >> 4 means wood type.
	// Logs will use 64 + what superclass texture indexes return.
	
	public BlockLog3(int id) {
		super(id);
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

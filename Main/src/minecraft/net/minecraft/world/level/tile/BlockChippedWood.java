package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockChippedWood extends BlockLog {
	protected BlockChippedWood(int id, int blockIndex) {
		super(id);
		this.blockIndexInTexture = blockIndex;
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
	
    public int getRenderType() {
    	return 105;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
	public int idDropped(int i1, Random random2) {
		return Block.chippedWood.blockID;
	} 
	
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
	
}

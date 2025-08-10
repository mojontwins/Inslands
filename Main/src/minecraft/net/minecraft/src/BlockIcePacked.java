package net.minecraft.src;

import java.util.List;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.ItemStack;

public class BlockIcePacked extends BlockIce {
	public BlockIcePacked(int blockID, int blockIndex) {
		super(blockID, blockIndex);
		this.setTickOnLoad(false);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	@Override
	public int getRenderBlockPass() {
		return 0;
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, int x, int y, int z, int side) {
	}
	
	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getRenderColor(0);
	}
	
	@Override
	public int getRenderColor(int meta) {
		return 0xEEEEFF;
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
}

package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockStone extends Block implements IBlockWithSubtypes {
	public static final int[] stoneColor = new int [] {
		0xFFFFFF,
		0xCCCCCC
	};
	
	public BlockStone(int id, int blockIndex) {
		super(id, blockIndex, Material.rock);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int idDropped(int metadata, Random rand) {
		return Block.cobblestone.blockID;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getRenderColor(blockAccess.getBlockMetadata(x, y, z));
	}

	@Override
	public int getRenderColor(int meta) {
		return stoneColor[(meta >> 4) & 7];
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 2; i ++) {
			par3List.add(new ItemStack(par1, 1, i << 4));
		}
	}

	@Override
	public int getItemBlockId() {
		return this.blockID - 256;
	}

	@Override
	public String getNameFromMeta(int meta) {
		return "stone";
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return this.blockIndexInTexture;
	}
}

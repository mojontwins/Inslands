package net.minecraft.src;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockCloth extends Block {
	public static final int clothColors[] = {
		0x252121, 0xBC342F, 0x40591C, 0x633B20, 0x2D3BB2, 0x944DD2, 0x2D86AC, 0xB8BDBD,
		0x4C4C4C, 0xE5A8B9, 0x4ACF3E, 0xDECF2A, 0x8EA8DF, 0xCB6BD4, 0xED8F4E, 0xFFFFFF
	};
	
	public BlockCloth() {
		super(35, 64, Material.cloth);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	// Removed. We are using a colorizer for this job.
	/*
	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		if(i2 == 0) {
			return this.blockIndexInTexture;
		} else {
			i2 = ~(i2 & 15);
			return 113 + ((i2 & 8) >> 3) + (i2 & 7) * 16;
		}
	}
	*/

	protected int damageDropped(int i1) {
		return i1;
	}

	public static int getBlockFromDye(int i0) {
		return ~i0 & 15;
	}

	public static int getDyeFromBlock(int i0) {
		return ~i0 & 15;
	}
	
	// Use this and save 15 textures in the texture atlas. Note how metadata-color is reversed
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getRenderColor(blockAccess.getBlockMetadata(x, y, z));
	}
	
	public int getRenderColor(int meta) {
		return clothColors[15 - meta];
	}
	
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 16; i ++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
}

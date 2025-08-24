package net.minecraft.world.level.tile;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockBigFlower extends Block implements IBlockWithSubtypes {
	// Uses metadata to represent three different blocks:
	// meta 0 is red petal block
	// meta 1 is yellow petal block
	// meta 2 is stem.

	public static String[] bigFlowerParts = new String[] {
			"redFlowerPetal",
			"yellowFlowerPetal",
			"stem"
	};

	protected BlockBigFlower(int id, int blockIndexInTexture, Material material) {
		super(id, blockIndexInTexture, material);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		switch(metadata) {
			case 0: return 11 * 16 + 13;
			case 1: return 11 * 16 + 14;
			case 2:
				if(side < 2) return 304;
				return 288;
			default: return this.blockIndexInTexture;
		}
	}

	@Override
	public Material getBlockMaterialBasedOnmetaData(int meta) {
		return meta == 2 ? Material.wood : Material.leaves;
	}
	
	@Override
	protected int damageDropped(int i1) {
		return i1;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getRenderColor(blockAccess.getBlockMetadata(x, y, z));
	}

	@Override
	public int getRenderColor(int meta) {
		return meta == 2 ? 0x88FF88 : 0xFFFFFF;
	}
	
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 3; i ++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public int getItemBlockId() {
		return this.blockID - 256;
	}

	@Override
	public String getNameFromMeta(int meta) {
		return BlockBigFlower.bigFlowerParts  [meta];
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return this.getBlockTextureFromSideAndMetadata(2, meta);
	}
}

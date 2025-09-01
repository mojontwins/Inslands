package net.minecraft.world.level.tile;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockFlowerWithSubtypes extends BlockFlower implements IBlockWithSubtypes {
	private String[] flowerNames = new String[] {
			"flower", 
			"rose",
			"paeonia",
			"blueFlower"
	};
	
	private int[] flowerTextures = new int[] {
			13, 12, 14*16 + 2, 14*16 + 0
	};
	
	protected BlockFlowerWithSubtypes(int id) {
		super(id, 13);
		this.setMyBlockBounds();
	}
	
	@Override
	public void setMyBlockBounds() {
		float var3 = 0.2F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.8F, 0.5F + var3);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		int type = this.flowerType(meta);
		return type < flowerTextures.length ? flowerTextures[type] : this.blockIndexInTexture;
	}
	
	private int flowerType(int meta) {
		return meta >> 4;
	}

	@Override
	public String getNameFromMeta(int meta) {
		int type = this.flowerType(meta);
		return type < flowerNames.length ? flowerNames[type] : "flower";
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return this.getBlockTextureFromSideAndMetadata(0, meta);
	}
	
	@Override
	public void getSubBlocks(int blockID, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < this.flowerNames.length; i ++) {
			list.add(new ItemStack(blockID, 1, i<<4));
		}
	}

}

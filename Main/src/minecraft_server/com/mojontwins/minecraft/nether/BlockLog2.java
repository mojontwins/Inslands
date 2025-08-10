package com.mojontwins.minecraft.nether;

import java.util.List;
import java.util.Random;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLog;
import net.minecraft.src.World;
import net.minecraft.world.item.ItemStack;

public class BlockLog2 extends BlockLog {
	public int textureBark[] = new int[] { 7 * 16 + 5, 0, 0, 0 };
	public int textureHeart[] = new int [] { 166, 0, 0, 0 };

	// Meta = 15 is full bark
	
	public BlockLog2(int i1) {
		super(i1);
		this.blockIndexInTexture = textureHeart [0];
	}

	@Override
	public int idDropped(int i1, Random random2) {
		return Block.wood2.blockID;
	}
	
	@Override
	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		byte b5 = 4;
		int i6 = b5 + 1;
		if(world1.checkChunksExist(i2 - i6, i3 - i6, i4 - i6, i2 + i6, i3 + i6, i4 + i6)) {
			for(int i7 = -b5; i7 <= b5; ++i7) {
				for(int i8 = -b5; i8 <= b5; ++i8) {
					for(int i9 = -b5; i9 <= b5; ++i9) {
						int i10 = world1.getBlockId(i2 + i7, i3 + i8, i4 + i9);
						if(i10 == Block.leaves2.blockID) {
							int i11 = world1.getBlockMetadata(i2 + i7, i3 + i8, i4 + i9);
							if((i11 & 8) == 0) {
								world1.setBlockMetadata(i2 + i7, i3 + i8, i4 + i9, i11 | 8);
							}
						}
					}
				}
			}
		}

	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		int woodType = meta & 3;
		
		if((meta & 12) == 8) return textureBark[woodType];
		
		boolean horizontal = (meta & 4) != 0;

		int outTextureIndex;
		int endTextureIndex;

		outTextureIndex = textureBark[woodType];
		endTextureIndex = textureHeart[woodType];
		
		if (!horizontal) {
			// Vanilla logs:
			return side <= 1 ? endTextureIndex : outTextureIndex;
		} else {
			// Horizontal logs:
			if(side <= 1) return outTextureIndex;
			
			if((meta & 8) != 0) {
				return (side == 4 || side == 5) ? endTextureIndex : outTextureIndex;
			} else {
				return (side == 2 || side == 3) ? endTextureIndex : outTextureIndex;
			}
		}
		
	}
	
	@Override
	public int getBreakTexture(int metadata) {
		return this.textureHeart[metadata & 3];
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
}

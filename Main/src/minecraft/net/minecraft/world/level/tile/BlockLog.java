package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.levelgen.feature.trees.EnumTreeType;
import net.minecraft.world.level.material.Material;

public class BlockLog extends Block implements IBlockWithSubtypes {
	// New version
	// Wood meta >> 4 means wood type.
	// Logs will use 288+type for bark and 304+type for ends.
	
	protected BlockLog(int i1) {
		super(i1, Material.wood);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}
	
	protected BlockLog(int i1, Material m) {
		super(i1, m);
	}

	public int quantityDropped(Random random1) {
		return 1;
	}

	public void harvestBlock(World world1, EntityPlayer entityPlayer2, int i3, int i4, int i5, int i6) {
		super.harvestBlock(world1, entityPlayer2, i3, i4, i5, i6);
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		byte b5 = 4;
		int i6 = b5 + 1;
		if(world1.checkChunksExist(i2 - i6, i3 - i6, i4 - i6, i2 + i6, i3 + i6, i4 + i6)) {
			for(int i7 = -b5; i7 <= b5; ++i7) {
				for(int i8 = -b5; i8 <= b5; ++i8) {
					for(int i9 = -b5; i9 <= b5; ++i9) {
						int i10 = world1.getblockID(i2 + i7, i3 + i8, i4 + i9);
						if(i10 == Block.leaves.blockID) {
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

	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		int woodType = meta >> 4;
		boolean horizontal = (meta & 4) != 0;

		int outTextureIndex = 288 + woodType;
		int endTextureIndex = 304 + woodType;
		
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
	
	public int getBlockTextureFromSide(int side) {
		return this.getBlockTextureFromSideAndMetadata(side, 0);
	}

	protected int damageDropped(int i1) {
		return i1 & 0xf0;
	}
	
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		// Make this compatible with beta wood types:
		int meta = world.getBlockMetadata(x, y, z);
		
		// Make horizontal & oriented?
		if (side > 1) {
			meta |= 4;
			if (side > 3) {
				meta |= 8;
			}
		}
		
		world.setBlockMetadata(x, y, z, meta);
	}
    
	public boolean renderAsNormalBlock() {
		return true;
	}
	
	public boolean isOpaqueCube() {
		return true;
	}

	public int getRenderType() {
		return 108;
	}
	
	public boolean canGrowMushrooms() {
		return true;
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 15; i ++) {
			par3List.add(new ItemStack(par1, 1, i<<4));
		}
	}

	@Override
	public int getItemblockID() {
		return this.blockID - 256;
	}

	@Override
	public String getNameFromMeta(int meta) {
		return "log." + EnumTreeType.findTreeTypeFromWood(new BlockState(this, meta)).name;
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return 2;
	}
}

package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.EnumTreeType;

public class BlockSapling extends BlockFlower implements IBlockWithSubtypes {
	private static final int GROWING_BIT = 16;

	protected BlockSapling(int i1, int i2) {
		super(i1, i2);
		this.setMyBlockBounds();
	}
	
	public void setMyBlockBounds() {
		float f3 = 0.4F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 2.0F, 0.5F + f3);

		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}
	
	public void updateTick(World world, int i2, int i3, int i4, Random random5) {
		if(!world.isRemote) {
			super.updateTick(world, i2, i3, i4, random5);
			if(world.getBlockLightValue(i2, i3 + 1, i4) >= 9 && random5.nextInt(30) == 0) {
				int i6 = world.getBlockMetadata(i2, i3, i4);
				if((i6 & GROWING_BIT) == 0) {
					world.setBlockMetadataWithNotify(i2, i3, i4, i6 | 8);
				} else {
					this.growTree(world, i2, i3, i4, random5);
				}
			}

		}
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return 320 + (meta >> 4);
	}

	public void growTree(World world, int x, int y, int z, Random rand) {
		int meta = world.getBlockMetadata(x, y, z) & 0xf0;
		
		EnumTreeType tree = EnumTreeType.findTreeTypeFromSapling(new BlockState(Block.sapling, meta));
		WorldGenerator worldGen = tree.getGen(rand);
		
		if (tree.needsFourSaplings) {
			// Check for 4 saplings
			for(int dx = 0; dx >= -1; dx --) {
				for(int dz = 0; dz >= -1; dz --) {
					if(
						this.sameSapling(world, x + dx, y, z + dz, meta) &&
						this.sameSapling(world, x + dx + 1, y, z + dz, meta) &&
						this.sameSapling(world, x + dx, y, z + dz + 1, meta) && 
						this.sameSapling(world, x + dx + 1, y, z + dz + 1, meta)) {
						
						world.setBlock(x + dx, y, z + dz, 0);
						world.setBlock(x + dx + 1, y, z + dz, 0);
						world.setBlock(x + dx, y, z + dz + 1, 0);
						world.setBlock(x + dx + 1, y, z + dz + 1, 0);
						
						if(worldGen == null || !worldGen.generate(world, rand, x + dx, y, z + dz)) {
							world.setBlockAndMetadata(x + dx, y, z + dz, this.blockID, meta);
							world.setBlockAndMetadata(x + dx + 1, y, z + dz, this.blockID, meta);
							world.setBlockAndMetadata(x + dx, y, z + dz + 1, this.blockID, meta);
							world.setBlockAndMetadata(x + dx + 1, y, z + dz + 1, this.blockID, meta);
						}
						
						break;
					}
				}
			}
		} else {
			world.setBlock(x, y, z, 0);
			if(worldGen == null || !worldGen.generate(world, rand, x, y, z)) {
				world.setBlockAndMetadata(x, y, z, this.blockID, meta);
			}
		}

	}

	public boolean sameSapling(World world, int x, int y, int z, int meta) {
		return world.getBlockId(x, y, z) == this.blockID &&
				world.getBlockMetadata(x, y, z) == meta;
	}
	
	@Override
	protected int damageDropped(int i1) {
		return i1 & 0xf0;
	}
	
	@Override
	public int getRenderType() {
		return 111;
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 15; i ++) {
			par3List.add(new ItemStack(par1, 1, i<<4));
		}
	}
    
	@Override
	public int getItemBlockId() {
		return this.blockID - 256;
	}

	@Override
	public String getNameFromMeta(int meta) {
		return "sapling." + EnumTreeType.values()[meta >> 4].name;
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return 320 + (meta >> 4);
	}
	
}

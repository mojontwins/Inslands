package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.Seasons;
import net.minecraft.world.level.World;
import net.minecraft.world.level.colorizer.ColorizerFoliage;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class BlockGrass extends Block {
	protected BlockGrass(int blockID) {
		super(blockID, Material.grass);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		
		if(meta == 1) {
		
			switch(side) {
				case 0: return 2;
				case 1: return LevelThemeGlobalSettings.colorizedPlants ? 253 : 0;
				default: 
					Block block = Block.blocksList[blockAccess.getBlockId(x, y + 1, z)];
					
					if(block != null && (
							(block.blockMaterial == Material.snow || block.blockMaterial == Material.builtSnow) ||
							(block.getRenderType() == 111 && blockAccess.getBlockMetadata(x, y + 1, z) > 0)
						)
					) {
						return 14 * 16 + 1;
					}
					
					return 0;
			}
		}
		
		switch(side) {
			case 0: return 2;
			case 1: return 0;
			default: 
				Block block = Block.blocksList[blockAccess.getBlockId(x, y + 1, z)];
				
				if(block == null) return 3;
				
				if(block.blockMaterial == Material.snow || block.blockMaterial == Material.builtSnow) return 68;
				if(block.getRenderType() == 111 && blockAccess.getBlockMetadata(x, y + 1, z) > 0) return 68;
				
				return 3;
		}
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		if(meta == 1) return 0; 
		return this.getBlockTextureFromSide(side);
	}
	
	@Override
	public int getBlockTextureFromSide(int side) {
		switch(side) {
			case 0: return 2;
			case 1: return 0;
			default: return 3;
		}
	}
	
	// Softlocked for a1.1.2
	/*
	public int colorMultiplier(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		iBlockAccess1.getWorldChunkManager().getBiomesForGeneration(i2, i4, 1, 1);
		double d5 = iBlockAccess1.getWorldChunkManager().temperature[0];
		double d7 = iBlockAccess1.getWorldChunkManager().humidity[0];
		return ColorizerGrass.getGrassColor(d5, d7);
	}
	*/
	
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		if(LevelThemeGlobalSettings.colorizedPlants) {
			return world.getGrassColorFromCache(x, z);
		} else return 0xffffff;
	}
	
	public int getRenderColor(int meta) {
		if(LevelThemeGlobalSettings.colorizedPlants) {
			return ColorizerFoliage.getFoliageColorBasic();
		} else return 0xffffff;
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if(!world.isRemote) {
			int meta = world.getBlockMetadata(x, y, z);
			int blockIDAbove = world.getBlockId(x, y + 1, z);
			
			if(meta == 1) {
				int blockBelow = world.getBlockId(x, y - 1, z);
				if(blockBelow == Block.dirt.blockID) {
					world.setBlock(x, y - 1, z, Block.grass.blockID);
				} else if (blockBelow == 0) {
					world.setBlockMetadata(x, y, z, 0);
				}
			}
			
			if(world.getBlockLightValue(x, y + 1, z) < 4 && Block.lightOpacity[blockIDAbove] > 2) {
				
				if(blockIDAbove == Block.grass.blockID && world.getBlockMetadata(x, y + 1, z) == 1) {
					return;
				}
				
				if(rand.nextInt(4) != 0) {
					return;
				}

				world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
			} else if(world.getBlockLightValue(x, y + 1, z) >= 9) {
				int xx = x + rand.nextInt(3) - 1;
				int yy = y + rand.nextInt(5) - 3;
				int zz = z + rand.nextInt(3) - 1;
				int blockID = world.getBlockId(xx, yy + 1, zz);
				if(world.getBlockId(xx, yy, zz) == Block.dirt.blockID && world.getBlockLightValue(xx, yy + 1, zz) >= 4 && Block.lightOpacity[blockID] <= 2) {
					world.setBlockAndMetadataWithNotify(xx, yy, zz, Block.grass.blockID, meta);
				}
			}

			if(Seasons.currentSeason == Seasons.AUTUMN && world.isAirBlock(x, y + 1, z) && world.isUnderLeaves(x, y + 1, z)) {
				world.setBlock(x, y + 1, z, Block.leafPile.blockID);
			}
		}
	}

	public int idDropped(int i1, Random random2) {
		return Block.dirt.idDropped(0, random2);
	}
	
	public boolean canGrowPlants() {
		return true;
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 2; i ++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
}

package net.minecraft.world.level.tile;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.colorizer.ColorizerFoliage;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class BlockLeaves3 extends BlockLeaves {

	public BlockLeaves3(int id) {
		super(id, 0);
	}
	
	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		if(LevelThemeGlobalSettings.colorizedPlants && (meta & 0xf0) < 0x30) {
			return world.getFoliageColorFromCache(x, z);
		} else return getRenderColor(meta);
	}

	@Override
	public int getRenderColor(int meta) {
		if((meta & 0xf0) < 0x30) {
			if(LevelThemeGlobalSettings.colorizedPlants) {
				return ColorizerFoliage.getFoliageColorBasic();
			} else {
				return 0x5BFB3B;
			}
		} else return 0xFFFFFF;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return 80 + super.getBlockTextureFromSideAndMetadata(side, meta);
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 3; i ++) {
			par3List.add(new ItemStack(par1, 1, i<<4));
		}
	}
    
}

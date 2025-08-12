package net.minecraft.src;

import java.util.List;
import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockTallGrass extends BlockFlower {
	public static int[] tallGrassColor = new int [] {
		0xFFFFFF,
		0xFAF3A4, 
		0xA76E1F
	};
	
	protected BlockTallGrass(int var1, int var2) {
		super(var1, var2);
		this.setMyBlockBounds();
	}

	@Override 
	public void setMyBlockBounds() {
		float var3 = 0.4F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.8F, 0.5F + var3);
	}

	@Override
	public int idDropped(int meta, Random random2) {
		meta >>= 4;		// Bits 4-7  
		meta &= 7; 		// Ignore bit 7 (which marks "double height"
		if(meta == 0) {
			return random2.nextInt(8) == 0 ? Item.seeds.shiftedIndex : -1;
		} else if(meta < 3) {
			return random2.nextInt(3) > 0 ? Block.tallGrass.blockID : -1;
		} else {
			return -1;
		}
	}
	
	@Override
	protected int damageDropped(int meta) {
		meta >>= 4;		// Bits 4-7  
		meta &= 7; 		// Ignore bit 7 (which marks "double height"
		if(meta == 1 || meta == 2) return 16;
		return 0;
	}
	
	@Override
	public int getRenderType() {
		return 111;
	}
	
	public boolean seeThrough() {
		return true; 
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return ((meta >> 4) & 7) == 0 && !LevelThemeGlobalSettings.colorizedPlants ? 13 * 16 + 13 : 15 * 16 + 10;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		if (((meta >> 4) & 7) == 0 && LevelThemeGlobalSettings.colorizedPlants) return blockAccess.getFoliageColorFromCache(x, z);
		return this.getRenderColor(meta);
	}

	@Override
	public int getRenderColor(int meta) {
		return tallGrassColor[(meta >> 4) & 7];
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		int grassType = (world.getBlockMetadata(x, y, z) >> 4) & 7;
		
		switch(grassType) {
		case 0: 
			return super.canBlockStay(world, x, y, z);
			
		default:
			int blockID = world.getBlockId(x, y - 1, z);
			Block block = Block.blocksList[blockID];
			
			if(block != null && block.canGrowPlants()) return true;
			if(blockID == Block.sand.blockID || blockID == Block.terracotta.blockID || blockID == Block.stainedTerracotta.blockID) return true;
			
			return false;
		}
	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 2; i ++) {
			par3List.add(new ItemStack(par1, 1, i << 4));
		}
		
		for(int i = 0; i < 2; i ++) {
			par3List.add(new ItemStack(par1, 1, (i & 8) << 4));
		}
	}
}

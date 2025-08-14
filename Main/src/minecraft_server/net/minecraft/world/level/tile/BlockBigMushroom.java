package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockBigMushroom extends Block {
	public int mushroomType;
	
	public int textureStem = 8*16+13;
	public int textureCap;
	
	public BlockBigMushroom(int id, int type) {
		super(id, Material.wood);
		mushroomType = type;
		blockIndexInTexture = 8*16+14;
		
		switch (mushroomType) {
			case 0: textureCap = 7*16+13; break;
			case 1: textureCap = 7*16+14; break;
			case 2: textureCap = 12*16+9; break;
		}
		
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		// meta = 10: stem, par > 1: sides
		if (par2 == 10 && par1 > 1) {
			return textureStem;
		}

		// Bottom
		if (par2 >= 1 && par2 <= 9 && par1 == 1) {
			return textureCap;
		}

		// Side 2
		if (par2 >= 1 && par2 <= 3 && par1 == 2) {
			return textureCap;
		}

		// Side 3
		if (par2 >= 7 && par2 <= 9 && par1 == 3) {
			return textureCap;
		}

		// Side 4
		if ((par2 == 1 || par2 == 4 || par2 == 7) && par1 == 4) {
			return textureCap;
		}

		// Side 5
		if ((par2 == 3 || par2 == 6 || par2 == 9) && par1 == 5) {
			return textureCap;
		}

		// Whole
		if (par2 == 14) {
			return textureCap;
		}

		// All trunk
		if (par2 == 15) {
			return textureStem;
		}

		// Inside
		return blockIndexInTexture;
	}

	@Override
	public int quantityDropped(Random rand) {
		if (rand.nextInt(3) != 0) return 1;
		else return 0;
	}

	@Override
	public int idDropped(int par1, Random par2Random) {
		return blockID;
	}

	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for (int i = 0; i < 2; i++) {
			par3List.add(new ItemStack(par1, 1, 14 + i));
		}
	}
}

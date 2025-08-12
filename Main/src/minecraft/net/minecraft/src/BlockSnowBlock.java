package net.minecraft.src;

import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;

public class BlockSnowBlock extends Block {
	protected BlockSnowBlock(int id, int blockIndex) {
		super(id, blockIndex, Material.builtSnow);
		this.setTickOnLoad(true);
		this.displayOnCreativeTab = CreativeTabs.tabBlock;
	}

	public int idDropped(int metadata, Random rand) {
		return Item.snowball.shiftedIndex;
	}

	public int quantityDropped(Random rand) {
		return 4;
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if(world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}

	}
}

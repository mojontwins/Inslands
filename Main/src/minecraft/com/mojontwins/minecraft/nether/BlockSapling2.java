package com.mojontwins.minecraft.nether;

import java.util.List;
import java.util.Random;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.src.Block;
import net.minecraft.src.BlockSapling;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class BlockSapling2 extends BlockSapling {
	public int blockIndexForMeta[] = new int[] { 11*16+1, 0, 0, 0 };

	public BlockSapling2(int blockID) {
		super(blockID, 11*16+1);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y + 1, z));
	}
	
	@Override
	protected boolean canThisPlantGrowOnThisBlockID(int blockID) {
		return Block.opaqueCubeLookup[blockID];
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return this.canThisPlantGrowOnThisBlockID(world.getBlockId(x, y + 1, z));
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if(!world.isRemote) {
			super.updateTick(world, x, y, z, rand);
			if(world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(30) == 0) {
				int meta = world.getBlockMetadata(x, y, z);
				if((meta & 8) == 0) {
					world.setBlockMetadataWithNotify(x, y, z, meta | 8);
				} else {
					this.growTree(world, x, y, z, rand);
				}
			}
		}
		
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return blockIndexForMeta[meta & 3];
	}
	
	@Override
	public void growTree(World world, int x, int y, int z, Random random5) {
		int meta = world.getBlockMetadata(x, y, z) & 3;
		world.setBlock(x, y, z, 0);
		
		WorldGenerator treeGen = null;
		switch(meta) {
		default: treeGen = new WorldGenBloodTree();
		}

		if(!((WorldGenerator)treeGen).generate(world, random5, x, y, z)) {
			world.setBlockAndMetadata(x, y, z, this.blockID, meta);
		}

	}
	
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
	
}

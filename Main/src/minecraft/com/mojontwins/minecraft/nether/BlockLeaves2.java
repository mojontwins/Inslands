package com.mojontwins.minecraft.nether;

import java.util.List;
import java.util.Random;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLeaves;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BlockLeaves2 extends BlockLeaves {
	public static int blockIndexForMeta[] = new int[] { 168, 168, 0, 0 };
	public static int colorForMeta[] = new int[] { 0xFFFFFF, 0xDDBBFF, 0xFFFFFF, 0xFFFFFF };
	
	public BlockLeaves2(int id) {
		super(id, 168);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return blockIndexForMeta [meta & 3];
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		// Small optimization: When replaced with leaves or wood, surrounding leaves are
		// NOT affected
		if (blockID == Block.wood2.blockID || blockID == Block.leaves2.blockID)
			return;
		
		this.onBlockRemovalDo(world, x, y, z);
	}
	
	@Override
	public int idDropped(int i, Random Random) {
		return Random.nextInt(50) == 0 ? Item.rottenFlesh.shiftedIndex : Block.sapling2.blockID;
	}
	
	@Override
	protected int damageDropped(int meta) {
		return meta & 7;
	}
	
	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		int blockID = world.getBlockId(x, y, z);

		// Small optimization: When replaced with leaves or wood, surrounding leaves are
		// NOT affected
		if (blockID == Block.wood2.blockID || blockID == Block.leaves2.blockID)
			return;
		
		this.onBlockRemovalDo(world, x, y, z);
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return this.getRenderColor(world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public int getRenderColor(int meta) {
		return colorForMeta[meta & 3];
	}
		
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
	}
		
}

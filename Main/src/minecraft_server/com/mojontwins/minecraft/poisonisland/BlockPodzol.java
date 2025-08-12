package com.mojontwins.minecraft.poisonisland;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.world.level.material.Material;

public class BlockPodzol extends Block {

	public BlockPodzol(int id) {
		super(id, Material.grass);
		this.blockIndexInTexture = 9 * 16 + 9;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		if (side == 1) {
			return this.blockIndexInTexture;
		}

		return side != 0 ? this.blockIndexInTexture - 1 : 2;
	}
	
	@Override
	public int idDropped(int par1, Random par2Random)	{
		return Block.dirt.idDropped(0, par2Random);
	}
	
	@Override
	public boolean canGrowMushrooms() {
		return true;
	}

	@Override
	public boolean canGrowPlants() {
		return true;
	}
}

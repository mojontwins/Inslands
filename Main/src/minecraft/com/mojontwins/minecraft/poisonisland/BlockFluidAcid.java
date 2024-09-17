package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.BlockFluid;
import net.minecraft.src.Material;

public class BlockFluidAcid extends BlockFluid {

	public BlockFluidAcid(int i1, int blockIndexInTexture) {
		super(i1, Material.water);
		this.blockIndexInTexture = blockIndexInTexture;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public int getBlockTextureFromSide(int side) {
		return this.blockIndexInTexture;
	}
	
	@Override
	public int tickRate() {
		return 10;
	}
	
}

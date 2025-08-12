package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.BlockFluid;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Material;

public class BlockFluidAcid extends BlockFluid {

	public BlockFluidAcid(int i1, int blockIndexInTexture) {
		super(i1, Material.acid);
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
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.attackEntityFrom((Entity)null, 1);
	}
	
}

package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.BlockStationary;
import net.minecraft.src.Material;

public class BlockStationaryAcid extends BlockStationary {

	public BlockStationaryAcid(int id, int blockIndexInTexture) {
		super(id, Material.acid);
		this.blockIndexInTexture = blockIndexInTexture;
		this.setTickOnLoad(true);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public int getBlockTextureFromSide(int side) {
		return this.blockIndexInTexture;
	}
	

}

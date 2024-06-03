package net.minecraft.src;

import java.util.Random;

public class BlockMobSpawner extends BlockContainer {
	public boolean oneShot = false; 
	
	protected BlockMobSpawner(int id, int blockIndex, boolean oneShot) {
		super(id, blockIndex, Material.rock);
		this.oneShot = oneShot;
	}

	protected TileEntity getBlockEntity() {
		if(this.oneShot) {
			return new TileEntityMobSpawnerOneshot();
		} else {
			return new TileEntityMobSpawner();
		}
	}

	public int idDropped(int metadata, Random rand) {
		return 0;
	}

	public int quantityDropped(Random rand) {
		return 0;
	}

	public boolean isOpaqueCube() {
		return false;
	}
	
	public boolean seeThrough() {
		return true; 
	}
}

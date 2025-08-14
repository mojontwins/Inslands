package net.minecraft.world.level.tile;

import net.minecraft.src.TileEntity;
import net.minecraft.world.level.World;
import net.minecraft.world.level.material.Material;

public abstract class BlockContainer extends Block {
	protected BlockContainer(int i1, Material material2) {
		super(i1, material2);
		isBlockContainer[i1] = true;
	}

	protected BlockContainer(int i1, int i2, Material material3) {
		super(i1, i2, material3);
		isBlockContainer[i1] = true;
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		world.setBlockTileEntity(x, y, z, this.getBlockEntity());
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		world.removeBlockTileEntity(x, y, z);
	}

	protected abstract TileEntity getBlockEntity();
}

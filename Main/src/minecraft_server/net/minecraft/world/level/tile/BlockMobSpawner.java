package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.entity.TileEntity;
import net.minecraft.world.level.tile.entity.TileEntityMobSpawner;
import net.minecraft.world.level.tile.entity.TileEntityMobSpawnerOneshot;
import net.minecraft.world.phys.AxisAlignedBB;

public class BlockMobSpawner extends BlockContainer {
	public boolean oneShot = false; 
	
	protected BlockMobSpawner(int id, int blockIndex, boolean oneShot) {
		super(id, blockIndex, Material.rock);
		this.oneShot = oneShot;
		//if(oneShot) this.blockIndexInTexture = 253;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		if(this.oneShot) return null;
		return super.getCollisionBoundingBoxFromPool(world1, i2, i3, i4);
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

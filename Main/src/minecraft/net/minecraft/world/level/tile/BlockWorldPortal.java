package net.minecraft.world.level.tile;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.material.Material;

public class BlockWorldPortal extends Block {

	public BlockWorldPortal(int id, int textureIndex) {
		super(id, textureIndex, Material.rock);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int idDropped(int meta, Random random) {
		return 0;
	}

	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
	}
}
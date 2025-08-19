package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockSource extends Block {
	private int fluid;

	protected BlockSource(int i1, int i2) {
		super(i1, Block.blocksList[i2].blockIndexInTexture, Material.water);
		this.fluid = i2;
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		super.onBlockAdded(world1, i2, i3, i4);
		if(world1.getBlockId(i2 - 1, i3, i4) == 0) {
			world1.setBlockWithNotify(i2 - 1, i3, i4, this.fluid);
		}

		if(world1.getBlockId(i2 + 1, i3, i4) == 0) {
			world1.setBlockWithNotify(i2 + 1, i3, i4, this.fluid);
		}

		if(world1.getBlockId(i2, i3, i4 - 1) == 0) {
			world1.setBlockWithNotify(i2, i3, i4 - 1, this.fluid);
		}

		if(world1.getBlockId(i2, i3, i4 + 1) == 0) {
			world1.setBlockWithNotify(i2, i3, i4 + 1, this.fluid);
		}

	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		super.updateTick(world1, i2, i3, i4, random5);
		if(world1.getBlockId(i2 - 1, i3, i4) == 0) {
			world1.setBlockWithNotify(i2 - 1, i3, i4, this.fluid);
		}

		if(world1.getBlockId(i2 + 1, i3, i4) == 0) {
			world1.setBlockWithNotify(i2 + 1, i3, i4, this.fluid);
		}

		if(world1.getBlockId(i2, i3, i4 - 1) == 0) {
			world1.setBlockWithNotify(i2, i3, i4 - 1, this.fluid);
		}

		if(world1.getBlockId(i2, i3, i4 + 1) == 0) {
			world1.setBlockWithNotify(i2, i3, i4 + 1, this.fluid);
		}

	}
}
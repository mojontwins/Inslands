package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockStep extends Block {
	private boolean blockType;

	public BlockStep(int i1, boolean z2) {
		super(i1, 6, Material.rock);
		this.blockType = z2;
		if(!z2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		this.setLightOpacity(255);
	}

	public final int getBlockTextureFromSide(int i1) {
		return i1 <= 1 ? 6 : 5;
	}

	public final boolean isOpaqueCube() {
		return this.blockType;
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(this == Block.stairSingle) {
			;
		}
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(this != Block.stairSingle) {
			super.onBlockAdded(world1, i2, i3, i4);
		}

		if(world1.getBlockId(i2, i3 - 1, i4) == stairSingle.blockID) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
			world1.setBlockWithNotify(i2, i3 - 1, i4, Block.stairDouble.blockID);
		}

	}

	public final int idDropped(int i1, Random random2) {
		return Block.stairSingle.blockID;
	}

	public final boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public final boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		if(this != Block.stairSingle) {
			super.shouldSideBeRendered(world1, i2, i3, i4, i5);
		}

		return i5 == 1 ? true : (!super.shouldSideBeRendered(world1, i2, i3, i4, i5) ? false : (i5 == 0 ? true : world1.getBlockId(i2, i3, i4) != this.blockID));
	}
}
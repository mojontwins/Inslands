package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFlower extends Block {
	protected BlockFlower(int i1, int i2) {
		super(i1, Material.plants);
		this.blockIndexInTexture = i2;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
	}

	public final boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return this.canThisPlantGrowOnThisBlockID(world1.getBlockId(i2, i3 - 1, i4));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i1) {
		return i1 == Block.grass.blockID || i1 == Block.dirt.blockID || i1 == Block.tilledField.blockID;
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		super.onNeighborBlockChange(world1, i2, i3, i4, i5);
		this.checkFlowerChange(world1, i2, i3, i4);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		this.checkFlowerChange(world1, i2, i3, i4);
	}

	private void checkFlowerChange(World world1, int i2, int i3, int i4) {
		if(!this.canBlockStay(world1, i2, i3, i4)) {
			this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}

	}

	public boolean canBlockStay(World world1, int i2, int i3, int i4) {
		return (world1.getBlockLightValue(i2, i3, i4) >= 8 || world1.canBlockSeeTheSky(i2, i3, i4)) && this.canThisPlantGrowOnThisBlockID(world1.getBlockId(i2, i3 - 1, i4));
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 1;
	}
}
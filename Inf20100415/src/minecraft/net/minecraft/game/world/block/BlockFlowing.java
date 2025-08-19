package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFlowing extends BlockFluid {
	private int stillId1;
	private int movingId1;

	protected BlockFlowing(int i1, Material material2) {
		super(i1, material2);
		new Random();
		int[] i10000 = new int[]{0, 1, 2, 3};
		this.blockIndexInTexture = 14;
		if(material2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockContainer[i1] = true;
		this.movingId1 = i1;
		this.stillId1 = i1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		world1.scheduleBlockUpdate(i2, i3, i4, this.movingId1);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		boolean z6 = false;
		boolean z10000 = false;
	}

	public final boolean update(World world1, int i2, int i3, int i4, int i5) {
		return false;
	}

	public final boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		int i6;
		return (i6 = world1.getBlockId(i2, i3, i4)) != this.movingId1 && i6 != this.stillId1 ? (i5 != 1 || world1.getBlockId(i2 - 1, i3, i4) != 0 && world1.getBlockId(i2 + 1, i3, i4) != 0 && world1.getBlockId(i2, i3, i4 - 1) != 0 && world1.getBlockId(i2, i3, i4 + 1) != 0 ? super.shouldSideBeRendered(world1, i2, i3, i4, i5) : true) : false;
	}

	public final boolean isCollidable() {
		return false;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
	}

	public final int tickRate() {
		return this.blockMaterial == Material.lava ? 25 : 5;
	}

	public final int quantityDropped(Random random1) {
		return 0;
	}

	public final int getRenderBlockPass() {
		return this.blockMaterial == Material.water ? 1 : 0;
	}
}
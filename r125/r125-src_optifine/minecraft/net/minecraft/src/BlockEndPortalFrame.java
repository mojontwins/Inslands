package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockEndPortalFrame extends Block {
	public BlockEndPortalFrame(int i1) {
		super(i1, 159, Material.glass);
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return i1 == 1 ? this.blockIndexInTexture - 1 : (i1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 26;
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
	}

	public void getCollidingBoundingBoxes(World world1, int i2, int i3, int i4, AxisAlignedBB axisAlignedBB5, ArrayList arrayList6) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		int i7 = world1.getBlockMetadata(i2, i3, i4);
		if(isEnderEyeInserted(i7)) {
			this.setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
			super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		}

		this.setBlockBoundsForItemRender();
	}

	public static boolean isEnderEyeInserted(int i0) {
		return (i0 & 4) != 0;
	}

	public int idDropped(int i1, Random random2, int i3) {
		return 0;
	}

	public void onBlockPlacedBy(World world1, int i2, int i3, int i4, EntityLiving entityLiving5) {
		int i6 = ((MathHelper.floor_double((double)(entityLiving5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
		world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
	}
}

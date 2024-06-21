package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockEndPortal extends BlockContainer {
	public static boolean bossDefeated = false;

	protected BlockEndPortal(int i1, Material material2) {
		super(i1, 0, material2);
		this.setLightValue(1.0F);
	}

	public TileEntity getBlockEntity() {
		return new TileEntityEndPortal();
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		float f5 = 0.0625F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f5, 1.0F);
	}

	public void getCollidingBoundingBoxes(World world1, int i2, int i3, int i4, AxisAlignedBB axisAlignedBB5, ArrayList arrayList6) {
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public void onEntityCollidedWithBlock(World world1, int i2, int i3, int i4, Entity entity5) {
		if(entity5.ridingEntity == null && entity5.riddenByEntity == null && entity5 instanceof EntityPlayer && !world1.isRemote) {
			((EntityPlayer)entity5).travelToTheEnd(1);
		}

	}

	public int getRenderType() {
		return -1;
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(!bossDefeated) {
			if(world1.worldProvider.worldType != 0) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}
		}
	}
}

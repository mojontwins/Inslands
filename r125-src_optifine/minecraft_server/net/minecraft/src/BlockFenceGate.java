package net.minecraft.src;

public class BlockFenceGate extends BlockDirectional {
	public BlockFenceGate(int i1, int i2) {
		super(i1, i2, Material.wood);
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return !world1.getBlockMaterial(i2, i3 - 1, i4).isSolid() ? false : super.canPlaceBlockAt(world1, i2, i3, i4);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockMetadata(i2, i3, i4);
		return isFenceGateOpen(i5) ? null : (i5 != 2 && i5 != 0 ? AxisAlignedBB.getBoundingBoxFromPool((double)((float)i2 + 0.375F), (double)i3, (double)i4, (double)((float)i2 + 0.625F), (double)((float)i3 + 1.5F), (double)(i4 + 1)) : AxisAlignedBB.getBoundingBoxFromPool((double)i2, (double)i3, (double)((float)i4 + 0.375F), (double)(i2 + 1), (double)((float)i3 + 1.5F), (double)((float)i4 + 0.625F)));
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		int i5 = getDirection(iBlockAccess1.getBlockMetadata(i2, i3, i4));
		if(i5 != 2 && i5 != 0) {
			this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		}

	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean getBlocksMovement(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		return isFenceGateOpen(iBlockAccess1.getBlockMetadata(i2, i3, i4));
	}

	public int getRenderType() {
		return 21;
	}

	public void onBlockPlacedBy(World world1, int i2, int i3, int i4, EntityLiving entityLiving5) {
		int i6 = (MathHelper.floor_double((double)(entityLiving5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) % 4;
		world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if(isFenceGateOpen(i6)) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, i6 & -5);
		} else {
			int i7 = (MathHelper.floor_double((double)(entityPlayer5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) % 4;
			int i8 = getDirection(i6);
			if(i8 == (i7 + 2) % 4) {
				i6 = i7;
			}

			world1.setBlockMetadataWithNotify(i2, i3, i4, i6 | 4);
		}

		world1.playAuxSFXAtEntity(entityPlayer5, 1003, i2, i3, i4, 0);
		return true;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(!world1.isRemote) {
			int i6 = world1.getBlockMetadata(i2, i3, i4);
			boolean z7 = world1.isBlockIndirectlyGettingPowered(i2, i3, i4);
			if(z7 || i5 > 0 && Block.blocksList[i5].canProvidePower() || i5 == 0) {
				if(z7 && !isFenceGateOpen(i6)) {
					world1.setBlockMetadataWithNotify(i2, i3, i4, i6 | 4);
					world1.playAuxSFXAtEntity((EntityPlayer)null, 1003, i2, i3, i4, 0);
				} else if(!z7 && isFenceGateOpen(i6)) {
					world1.setBlockMetadataWithNotify(i2, i3, i4, i6 & -5);
					world1.playAuxSFXAtEntity((EntityPlayer)null, 1003, i2, i3, i4, 0);
				}
			}

		}
	}

	public static boolean isFenceGateOpen(int i0) {
		return (i0 & 4) != 0;
	}
}

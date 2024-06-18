package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockStep extends Block {
	public static final String[] blockStepTypes = new String[]{"stone", "sand", "wood", "cobble", "brick", "smoothStoneBrick"};
	private boolean blockType;

	public BlockStep(int i1, boolean z2) {
		super(i1, 6, Material.rock);
		this.blockType = z2;
		if(!z2) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		} else {
			opaqueCubeLookup[i1] = true;
		}

		this.setLightOpacity(255);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		if(this.blockType) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			boolean z5 = (iBlockAccess1.getBlockMetadata(i2, i3, i4) & 8) != 0;
			if(z5) {
				this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else {
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			}
		}

	}

	public void setBlockBoundsForItemRender() {
		if(this.blockType) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

	}

	public void getCollidingBoundingBoxes(World world1, int i2, int i3, int i4, AxisAlignedBB axisAlignedBB5, ArrayList arrayList6) {
		this.setBlockBoundsBasedOnState(world1, i2, i3, i4);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		int i3 = i2 & 7;
		return i3 == 0 ? (i1 <= 1 ? 6 : 5) : (i3 == 1 ? (i1 == 0 ? 208 : (i1 == 1 ? 176 : 192)) : (i3 == 2 ? 4 : (i3 == 3 ? 16 : (i3 == 4 ? Block.brick.blockIndexInTexture : (i3 == 5 ? Block.stoneBrick.blockIndexInTexture : 6)))));
	}

	public int getBlockTextureFromSide(int i1) {
		return this.getBlockTextureFromSideAndMetadata(i1, 0);
	}

	public boolean isOpaqueCube() {
		return this.blockType;
	}

	public void onBlockPlaced(World world1, int i2, int i3, int i4, int i5) {
		if(i5 == 0 && !this.blockType) {
			int i6 = world1.getBlockMetadata(i2, i3, i4) & 7;
			world1.setBlockMetadataWithNotify(i2, i3, i4, i6 | 8);
		}

	}

	public int idDropped(int i1, Random random2, int i3) {
		return Block.stairSingle.blockID;
	}

	public int quantityDropped(Random random1) {
		return this.blockType ? 2 : 1;
	}

	protected int damageDropped(int i1) {
		return i1 & 7;
	}

	public boolean renderAsNormalBlock() {
		return this.blockType;
	}

	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess1, int i2, int i3, int i4, int i5) {
		if(this.blockType) {
			super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5);
		}

		if(i5 != 1 && i5 != 0 && !super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5)) {
			return false;
		} else {
			int i6 = i2 + Facing.offsetsXForSide[Facing.faceToSide[i5]];
			int i7 = i3 + Facing.offsetsYForSide[Facing.faceToSide[i5]];
			int i8 = i4 + Facing.offsetsZForSide[Facing.faceToSide[i5]];
			boolean z9 = (iBlockAccess1.getBlockMetadata(i6, i7, i8) & 8) != 0;
			return !z9 ? (i5 == 1 ? true : (i5 == 0 && super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5) ? true : iBlockAccess1.getBlockId(i2, i3, i4) != this.blockID || (iBlockAccess1.getBlockMetadata(i2, i3, i4) & 8) != 0)) : (i5 == 0 ? true : (i5 == 1 && super.shouldSideBeRendered(iBlockAccess1, i2, i3, i4, i5) ? true : iBlockAccess1.getBlockId(i2, i3, i4) != this.blockID || (iBlockAccess1.getBlockMetadata(i2, i3, i4) & 8) == 0));
		}
	}

	protected ItemStack createStackedBlock(int i1) {
		return new ItemStack(Block.stairSingle.blockID, 1, i1 & 7);
	}
}

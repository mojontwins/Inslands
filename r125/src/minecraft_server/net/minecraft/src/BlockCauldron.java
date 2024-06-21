package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockCauldron extends Block {
	public BlockCauldron(int i1) {
		super(i1, Material.iron);
		this.blockIndexInTexture = 154;
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return i1 == 1 ? 138 : (i1 == 0 ? 155 : 154);
	}

	public void getCollidingBoundingBoxes(World world1, int i2, int i3, int i4, AxisAlignedBB axisAlignedBB5, ArrayList arrayList6) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		float f7 = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f7, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f7);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		this.setBlockBounds(1.0F - f7, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f7, 1.0F, 1.0F, 1.0F);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		this.setBlockBoundsForItemRender();
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 24;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		if(world1.isRemote) {
			return true;
		} else {
			ItemStack itemStack6 = entityPlayer5.inventory.getCurrentItem();
			if(itemStack6 == null) {
				return true;
			} else {
				int i7 = world1.getBlockMetadata(i2, i3, i4);
				if(itemStack6.itemID == Item.bucketWater.shiftedIndex) {
					if(i7 < 3) {
						if(!entityPlayer5.capabilities.isCreativeMode) {
							entityPlayer5.inventory.setInventorySlotContents(entityPlayer5.inventory.currentItem, new ItemStack(Item.bucketEmpty));
						}

						world1.setBlockMetadataWithNotify(i2, i3, i4, 3);
					}

					return true;
				} else {
					if(itemStack6.itemID == Item.glassBottle.shiftedIndex && i7 > 0) {
						ItemStack itemStack8 = new ItemStack(Item.potion, 1, 0);
						if(!entityPlayer5.inventory.addItemStackToInventory(itemStack8)) {
							world1.spawnEntityInWorld(new EntityItem(world1, (double)i2 + 0.5D, (double)i3 + 1.5D, (double)i4 + 0.5D, itemStack8));
						}

						--itemStack6.stackSize;
						if(itemStack6.stackSize <= 0) {
							entityPlayer5.inventory.setInventorySlotContents(entityPlayer5.inventory.currentItem, (ItemStack)null);
						}

						world1.setBlockMetadataWithNotify(i2, i3, i4, i7 - 1);
					}

					return true;
				}
			}
		}
	}

	public int idDropped(int i1, Random random2, int i3) {
		return Item.cauldron.shiftedIndex;
	}
}

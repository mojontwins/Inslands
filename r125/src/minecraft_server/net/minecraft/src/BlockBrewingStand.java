package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockBrewingStand extends BlockContainer {
	private Random rand = new Random();

	public BlockBrewingStand(int i1) {
		super(i1, Material.iron);
		this.blockIndexInTexture = 157;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 25;
	}

	public TileEntity getBlockEntity() {
		return new TileEntityBrewingStand();
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public void getCollidingBoundingBoxes(World world1, int i2, int i3, int i4, AxisAlignedBB axisAlignedBB5, ArrayList arrayList6) {
		this.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
		this.setBlockBoundsForItemRender();
		super.getCollidingBoundingBoxes(world1, i2, i3, i4, axisAlignedBB5, arrayList6);
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		if(world1.isRemote) {
			return true;
		} else {
			TileEntityBrewingStand tileEntityBrewingStand6 = (TileEntityBrewingStand)world1.getBlockTileEntity(i2, i3, i4);
			if(tileEntityBrewingStand6 != null) {
				entityPlayer5.displayGUIBrewingStand(tileEntityBrewingStand6);
			}

			return true;
		}
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		TileEntity tileEntity5 = world1.getBlockTileEntity(i2, i3, i4);
		if(tileEntity5 != null && tileEntity5 instanceof TileEntityBrewingStand) {
			TileEntityBrewingStand tileEntityBrewingStand6 = (TileEntityBrewingStand)tileEntity5;

			for(int i7 = 0; i7 < tileEntityBrewingStand6.getSizeInventory(); ++i7) {
				ItemStack itemStack8 = tileEntityBrewingStand6.getStackInSlot(i7);
				if(itemStack8 != null) {
					float f9 = this.rand.nextFloat() * 0.8F + 0.1F;
					float f10 = this.rand.nextFloat() * 0.8F + 0.1F;
					float f11 = this.rand.nextFloat() * 0.8F + 0.1F;

					while(itemStack8.stackSize > 0) {
						int i12 = this.rand.nextInt(21) + 10;
						if(i12 > itemStack8.stackSize) {
							i12 = itemStack8.stackSize;
						}

						itemStack8.stackSize -= i12;
						EntityItem entityItem13 = new EntityItem(world1, (double)((float)i2 + f9), (double)((float)i3 + f10), (double)((float)i4 + f11), new ItemStack(itemStack8.itemID, i12, itemStack8.getItemDamage()));
						float f14 = 0.05F;
						entityItem13.motionX = (double)((float)this.rand.nextGaussian() * f14);
						entityItem13.motionY = (double)((float)this.rand.nextGaussian() * f14 + 0.2F);
						entityItem13.motionZ = (double)((float)this.rand.nextGaussian() * f14);
						world1.spawnEntityInWorld(entityItem13);
					}
				}
			}
		}

		super.onBlockRemoval(world1, i2, i3, i4);
	}

	public int idDropped(int i1, Random random2, int i3) {
		return Item.brewingStand.shiftedIndex;
	}
}

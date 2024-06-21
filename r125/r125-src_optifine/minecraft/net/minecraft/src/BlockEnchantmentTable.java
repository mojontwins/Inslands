package net.minecraft.src;

import java.util.Random;

public class BlockEnchantmentTable extends BlockContainer {
	protected BlockEnchantmentTable(int i1) {
		super(i1, 166, Material.rock);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		this.setLightOpacity(0);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		super.randomDisplayTick(world1, i2, i3, i4, random5);

		for(int i6 = i2 - 2; i6 <= i2 + 2; ++i6) {
			for(int i7 = i4 - 2; i7 <= i4 + 2; ++i7) {
				if(i6 > i2 - 2 && i6 < i2 + 2 && i7 == i4 - 1) {
					i7 = i4 + 2;
				}

				if(random5.nextInt(16) == 0) {
					for(int i8 = i3; i8 <= i3 + 1; ++i8) {
						if(world1.getBlockId(i6, i8, i7) == Block.bookShelf.blockID) {
							if(!world1.isAirBlock((i6 - i2) / 2 + i2, i8, (i7 - i4) / 2 + i4)) {
								break;
							}

							world1.spawnParticle("enchantmenttable", (double)i2 + 0.5D, (double)i3 + 2.0D, (double)i4 + 0.5D, (double)((float)(i6 - i2) + random5.nextFloat()) - 0.5D, (double)((float)(i8 - i3) - random5.nextFloat() - 1.0F), (double)((float)(i7 - i4) + random5.nextFloat()) - 0.5D);
						}
					}
				}
			}
		}

	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return this.getBlockTextureFromSide(i1);
	}

	public int getBlockTextureFromSide(int i1) {
		return i1 == 0 ? this.blockIndexInTexture + 17 : (i1 == 1 ? this.blockIndexInTexture : this.blockIndexInTexture + 16);
	}

	public TileEntity getBlockEntity() {
		return new TileEntityEnchantmentTable();
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		if(world1.isRemote) {
			return true;
		} else {
			entityPlayer5.displayGUIEnchantment(i2, i3, i4);
			return true;
		}
	}
}

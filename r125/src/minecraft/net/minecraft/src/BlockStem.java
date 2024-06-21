package net.minecraft.src;

import java.util.Random;

public class BlockStem extends BlockFlower {
	private Block fruitType;

	protected BlockStem(int i1, Block block2) {
		super(i1, 111);
		this.fruitType = block2;
		this.setTickRandomly(true);
		float f3 = 0.125F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, 0.25F, 0.5F + f3);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i1) {
		return i1 == Block.tilledField.blockID;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		super.updateTick(world1, i2, i3, i4, random5);
		if(world1.getBlockLightValue(i2, i3 + 1, i4) >= 9) {
			float f6 = this.getGrowthModifier(world1, i2, i3, i4);
			if(random5.nextInt((int)(25.0F / f6) + 1) == 0) {
				int i7 = world1.getBlockMetadata(i2, i3, i4);
				if(i7 < 7) {
					++i7;
					world1.setBlockMetadataWithNotify(i2, i3, i4, i7);
				} else {
					if(world1.getBlockId(i2 - 1, i3, i4) == this.fruitType.blockID) {
						return;
					}

					if(world1.getBlockId(i2 + 1, i3, i4) == this.fruitType.blockID) {
						return;
					}

					if(world1.getBlockId(i2, i3, i4 - 1) == this.fruitType.blockID) {
						return;
					}

					if(world1.getBlockId(i2, i3, i4 + 1) == this.fruitType.blockID) {
						return;
					}

					int i8 = random5.nextInt(4);
					int i9 = i2;
					int i10 = i4;
					if(i8 == 0) {
						i9 = i2 - 1;
					}

					if(i8 == 1) {
						++i9;
					}

					if(i8 == 2) {
						i10 = i4 - 1;
					}

					if(i8 == 3) {
						++i10;
					}

					int i11 = world1.getBlockId(i9, i3 - 1, i10);
					if(world1.getBlockId(i9, i3, i10) == 0 && (i11 == Block.tilledField.blockID || i11 == Block.dirt.blockID || i11 == Block.grass.blockID)) {
						world1.setBlockWithNotify(i9, i3, i10, this.fruitType.blockID);
					}
				}
			}
		}

	}

	public void fertilizeStem(World world1, int i2, int i3, int i4) {
		world1.setBlockMetadataWithNotify(i2, i3, i4, 7);
	}

	private float getGrowthModifier(World world1, int i2, int i3, int i4) {
		float f5 = 1.0F;
		int i6 = world1.getBlockId(i2, i3, i4 - 1);
		int i7 = world1.getBlockId(i2, i3, i4 + 1);
		int i8 = world1.getBlockId(i2 - 1, i3, i4);
		int i9 = world1.getBlockId(i2 + 1, i3, i4);
		int i10 = world1.getBlockId(i2 - 1, i3, i4 - 1);
		int i11 = world1.getBlockId(i2 + 1, i3, i4 - 1);
		int i12 = world1.getBlockId(i2 + 1, i3, i4 + 1);
		int i13 = world1.getBlockId(i2 - 1, i3, i4 + 1);
		boolean z14 = i8 == this.blockID || i9 == this.blockID;
		boolean z15 = i6 == this.blockID || i7 == this.blockID;
		boolean z16 = i10 == this.blockID || i11 == this.blockID || i12 == this.blockID || i13 == this.blockID;

		for(int i17 = i2 - 1; i17 <= i2 + 1; ++i17) {
			for(int i18 = i4 - 1; i18 <= i4 + 1; ++i18) {
				int i19 = world1.getBlockId(i17, i3 - 1, i18);
				float f20 = 0.0F;
				if(i19 == Block.tilledField.blockID) {
					f20 = 1.0F;
					if(world1.getBlockMetadata(i17, i3 - 1, i18) > 0) {
						f20 = 3.0F;
					}
				}

				if(i17 != i2 || i18 != i4) {
					f20 /= 4.0F;
				}

				f5 += f20;
			}
		}

		if(z16 || z14 && z15) {
			f5 /= 2.0F;
		}

		return f5;
	}

	public int getRenderColor(int i1) {
		int i2 = i1 * 32;
		int i3 = 255 - i1 * 8;
		int i4 = i1 * 4;
		return i2 << 16 | i3 << 8 | i4;
	}

	public int colorMultiplier(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		return this.getRenderColor(iBlockAccess1.getBlockMetadata(i2, i3, i4));
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return this.blockIndexInTexture;
	}

	public void setBlockBoundsForItemRender() {
		float f1 = 0.125F;
		this.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, 0.25F, 0.5F + f1);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		this.maxY = (double)((float)(iBlockAccess1.getBlockMetadata(i2, i3, i4) * 2 + 2) / 16.0F);
		float f5 = 0.125F;
		this.setBlockBounds(0.5F - f5, 0.0F, 0.5F - f5, 0.5F + f5, (float)this.maxY, 0.5F + f5);
	}

	public int getRenderType() {
		return 19;
	}

	public int func_35296_f(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		int i5 = iBlockAccess1.getBlockMetadata(i2, i3, i4);
		return i5 < 7 ? -1 : (iBlockAccess1.getBlockId(i2 - 1, i3, i4) == this.fruitType.blockID ? 0 : (iBlockAccess1.getBlockId(i2 + 1, i3, i4) == this.fruitType.blockID ? 1 : (iBlockAccess1.getBlockId(i2, i3, i4 - 1) == this.fruitType.blockID ? 2 : (iBlockAccess1.getBlockId(i2, i3, i4 + 1) == this.fruitType.blockID ? 3 : -1))));
	}

	public void dropBlockAsItemWithChance(World world1, int i2, int i3, int i4, int i5, float f6, int i7) {
		super.dropBlockAsItemWithChance(world1, i2, i3, i4, i5, f6, i7);
		if(!world1.isRemote) {
			Item item8 = null;
			if(this.fruitType == Block.pumpkin) {
				item8 = Item.pumpkinSeeds;
			}

			if(this.fruitType == Block.melon) {
				item8 = Item.melonSeeds;
			}

			for(int i9 = 0; i9 < 3; ++i9) {
				if(world1.rand.nextInt(15) <= i5) {
					float f10 = 0.7F;
					float f11 = world1.rand.nextFloat() * f10 + (1.0F - f10) * 0.5F;
					float f12 = world1.rand.nextFloat() * f10 + (1.0F - f10) * 0.5F;
					float f13 = world1.rand.nextFloat() * f10 + (1.0F - f10) * 0.5F;
					EntityItem entityItem14 = new EntityItem(world1, (double)((float)i2 + f11), (double)((float)i3 + f12), (double)((float)i4 + f13), new ItemStack(item8));
					entityItem14.delayBeforeCanPickup = 10;
					world1.spawnEntityInWorld(entityItem14);
				}
			}

		}
	}

	public int idDropped(int i1, Random random2, int i3) {
		if(i1 == 7) {
			;
		}

		return -1;
	}

	public int quantityDropped(Random random1) {
		return 1;
	}
}

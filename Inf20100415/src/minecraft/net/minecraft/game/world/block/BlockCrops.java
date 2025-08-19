package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.world.World;

public final class BlockCrops extends BlockFlower {
	protected BlockCrops(int i1, int i2) {
		super(59, 88);
		this.blockIndexInTexture = 88;
		this.setTickOnLoad(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	protected final boolean canThisPlantGrowOnThisBlockID(int i1) {
		return i1 == Block.tilledField.blockID;
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		super.updateTick(world1, i2, i3, i4, random5);
		int i6;
		if(world1.getBlockLightValue(i2, i3 + 1, i4) >= 9 && (i6 = world1.getBlockMetadata(i2, i3, i4)) < 7) {
			int i11 = i4;
			int i10 = i3;
			int i9 = i2;
			World world8 = world1;
			float f12 = 1.0F;
			int i13 = world1.getBlockId(i2, i3, i4 - 1);
			int i14 = world1.getBlockId(i2, i3, i4 + 1);
			int i15 = world1.getBlockId(i2 - 1, i3, i4);
			int i16 = world1.getBlockId(i2 + 1, i3, i4);
			int i17 = world1.getBlockId(i2 - 1, i3, i4 - 1);
			int i18 = world1.getBlockId(i2 + 1, i3, i4 - 1);
			int i19 = world1.getBlockId(i2 + 1, i3, i4 + 1);
			int i20 = world1.getBlockId(i2 - 1, i3, i4 + 1);
			boolean z22 = i15 == this.blockID || i16 == this.blockID;
			boolean z21 = i13 == this.blockID || i14 == this.blockID;
			boolean z7 = i17 == this.blockID || i18 == this.blockID || i19 == this.blockID || i20 == this.blockID;

			for(i14 = i2 - 1; i14 <= i9 + 1; ++i14) {
				for(i16 = i11 - 1; i16 <= i11 + 1; ++i16) {
					i17 = world8.getBlockId(i14, i10 - 1, i16);
					float f23 = 0.0F;
					if(i17 == Block.tilledField.blockID) {
						f23 = 1.0F;
						if(world8.getBlockMetadata(i14, i10 - 1, i16) > 0) {
							f23 = 3.0F;
						}
					}

					if(i14 != i9 || i16 != i11) {
						f23 /= 4.0F;
					}

					f12 += f23;
				}
			}

			if(z7 || z22 && z21) {
				f12 /= 2.0F;
			}

			if(random5.nextInt((int)(100.0F / f12)) == 0) {
				++i6;
				world1.setBlockMetadataWithNotify(i2, i3, i4, i6);
			}
		}

	}

	public final int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		if(i2 < 0) {
			i2 = 7;
		}

		return this.blockIndexInTexture + i2;
	}

	public final int getRenderType() {
		return 6;
	}

	public final void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
		super.onBlockDestroyedByPlayer(world1, i2, i3, i4, i5);

		for(int i6 = 0; i6 < 3; ++i6) {
			if(world1.rand.nextInt(15) <= i5) {
				float f7 = world1.rand.nextFloat() * 0.7F + 0.15F;
				float f8 = world1.rand.nextFloat() * 0.7F + 0.15F;
				float f9 = world1.rand.nextFloat() * 0.7F + 0.15F;
				EntityItem entityItem10;
				(entityItem10 = new EntityItem(world1, (double)((float)i2 + f7), (double)((float)i3 + f8), (double)((float)i4 + f9), new ItemStack(Item.seeds))).delayBeforeCanPickup = 10;
				world1.spawnEntityInWorld(entityItem10);
			}
		}

	}

	public final int idDropped(int i1, Random random2) {
		System.out.println("Get resource: " + i1);
		return i1 == 7 ? Item.wheat.shiftedIndex : -1;
	}

	public final int quantityDropped(Random random1) {
		return 1;
	}
}
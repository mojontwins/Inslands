package net.minecraft.src;

public class ItemSlab extends ItemBlock {
	public ItemSlab(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getMetadata(int i1) {
		return i1;
	}

	public String getItemNameIS(ItemStack itemStack1) {
		int i2 = itemStack1.getItemDamage();
		if(i2 < 0 || i2 >= BlockStep.blockStepTypes.length) {
			i2 = 0;
		}

		return super.getItemName() + "." + BlockStep.blockStepTypes[i2];
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		if(itemStack1.stackSize == 0) {
			return false;
		} else if(!entityPlayer2.canPlayerEdit(i4, i5, i6)) {
			return false;
		} else {
			int i8 = world3.getBlockId(i4, i5, i6);
			int i9 = world3.getBlockMetadata(i4, i5, i6);
			int i10 = i9 & 7;
			boolean z11 = (i9 & 8) != 0;
			if((i7 == 1 && !z11 || i7 == 0 && z11) && i8 == Block.stairSingle.blockID && i10 == itemStack1.getItemDamage()) {
				if(world3.checkIfAABBIsClear(Block.stairDouble.getCollisionBoundingBoxFromPool(world3, i4, i5, i6)) && world3.setBlockAndMetadataWithNotify(i4, i5, i6, Block.stairDouble.blockID, i10)) {
					world3.playSoundEffect((double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), (double)((float)i6 + 0.5F), Block.stairDouble.stepSound.getStepSound(), (Block.stairDouble.stepSound.getVolume() + 1.0F) / 2.0F, Block.stairDouble.stepSound.getPitch() * 0.8F);
					--itemStack1.stackSize;
				}

				return true;
			} else {
				return func_50020_b(itemStack1, entityPlayer2, world3, i4, i5, i6, i7) ? true : super.onItemUse(itemStack1, entityPlayer2, world3, i4, i5, i6, i7);
			}
		}
	}

	private static boolean func_50020_b(ItemStack itemStack0, EntityPlayer entityPlayer1, World world2, int i3, int i4, int i5, int i6) {
		if(i6 == 0) {
			--i4;
		}

		if(i6 == 1) {
			++i4;
		}

		if(i6 == 2) {
			--i5;
		}

		if(i6 == 3) {
			++i5;
		}

		if(i6 == 4) {
			--i3;
		}

		if(i6 == 5) {
			++i3;
		}

		int i7 = world2.getBlockId(i3, i4, i5);
		int i8 = world2.getBlockMetadata(i3, i4, i5);
		int i9 = i8 & 7;
		if(i7 == Block.stairSingle.blockID && i9 == itemStack0.getItemDamage()) {
			if(world2.checkIfAABBIsClear(Block.stairDouble.getCollisionBoundingBoxFromPool(world2, i3, i4, i5)) && world2.setBlockAndMetadataWithNotify(i3, i4, i5, Block.stairDouble.blockID, i9)) {
				world2.playSoundEffect((double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), Block.stairDouble.stepSound.getStepSound(), (Block.stairDouble.stepSound.getVolume() + 1.0F) / 2.0F, Block.stairDouble.stepSound.getPitch() * 0.8F);
				--itemStack0.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}
}

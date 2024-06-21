package net.minecraft.src;

public class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int i1) {
		super(i1);
		this.blockID = i1 + 256;
		this.setIconIndex(Block.blocksList[i1 + 256].getBlockTextureFromSide(2));
	}

	public int getBlockID() {
		return this.blockID;
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		int i8 = world3.getBlockId(i4, i5, i6);
		if(i8 == Block.snow.blockID) {
			i7 = 1;
		} else if(i8 != Block.vine.blockID && i8 != Block.tallGrass.blockID && i8 != Block.deadBush.blockID) {
			if(i7 == 0) {
				--i5;
			}

			if(i7 == 1) {
				++i5;
			}

			if(i7 == 2) {
				--i6;
			}

			if(i7 == 3) {
				++i6;
			}

			if(i7 == 4) {
				--i4;
			}

			if(i7 == 5) {
				++i4;
			}
		}

		if(itemStack1.stackSize == 0) {
			return false;
		} else if(!entityPlayer2.canPlayerEdit(i4, i5, i6)) {
			return false;
		} else if(i5 == 255 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		} else if(world3.canBlockBePlacedAt(this.blockID, i4, i5, i6, false, i7)) {
			Block block9 = Block.blocksList[this.blockID];
			if(world3.setBlockAndMetadataWithNotify(i4, i5, i6, this.blockID, this.getMetadata(itemStack1.getItemDamage()))) {
				if(world3.getBlockId(i4, i5, i6) == this.blockID) {
					Block.blocksList[this.blockID].onBlockPlaced(world3, i4, i5, i6, i7);
					Block.blocksList[this.blockID].onBlockPlacedBy(world3, i4, i5, i6, entityPlayer2);
				}

				world3.playSoundEffect((double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), (double)((float)i6 + 0.5F), block9.stepSound.getStepSound(), (block9.stepSound.getVolume() + 1.0F) / 2.0F, block9.stepSound.getPitch() * 0.8F);
				--itemStack1.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	public String getItemNameIS(ItemStack itemStack1) {
		return Block.blocksList[this.blockID].getBlockName();
	}

	public String getItemName() {
		return Block.blocksList[this.blockID].getBlockName();
	}
}

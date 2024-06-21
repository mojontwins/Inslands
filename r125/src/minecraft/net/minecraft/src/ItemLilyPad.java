package net.minecraft.src;

public class ItemLilyPad extends ItemColored {
	public ItemLilyPad(int i1) {
		super(i1, false);
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		MovingObjectPosition movingObjectPosition4 = this.getMovingObjectPositionFromPlayer(world2, entityPlayer3, true);
		if(movingObjectPosition4 == null) {
			return itemStack1;
		} else {
			if(movingObjectPosition4.typeOfHit == EnumMovingObjectType.TILE) {
				int i5 = movingObjectPosition4.blockX;
				int i6 = movingObjectPosition4.blockY;
				int i7 = movingObjectPosition4.blockZ;
				if(!world2.canMineBlock(entityPlayer3, i5, i6, i7)) {
					return itemStack1;
				}

				if(!entityPlayer3.canPlayerEdit(i5, i6, i7)) {
					return itemStack1;
				}

				if(world2.getBlockMaterial(i5, i6, i7) == Material.water && world2.getBlockMetadata(i5, i6, i7) == 0 && world2.isAirBlock(i5, i6 + 1, i7)) {
					world2.setBlockWithNotify(i5, i6 + 1, i7, Block.waterlily.blockID);
					if(!entityPlayer3.capabilities.isCreativeMode) {
						--itemStack1.stackSize;
					}
				}
			}

			return itemStack1;
		}
	}

	public int getColorFromDamage(int i1, int i2) {
		return Block.waterlily.getRenderColor(i1);
	}
}

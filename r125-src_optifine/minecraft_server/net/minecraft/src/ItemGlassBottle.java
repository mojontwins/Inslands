package net.minecraft.src;

public class ItemGlassBottle extends Item {
	public ItemGlassBottle(int i1) {
		super(i1);
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

				if(world2.getBlockMaterial(i5, i6, i7) == Material.water) {
					--itemStack1.stackSize;
					if(itemStack1.stackSize <= 0) {
						return new ItemStack(Item.potion);
					}

					if(!entityPlayer3.inventory.addItemStackToInventory(new ItemStack(Item.potion))) {
						entityPlayer3.dropPlayerItem(new ItemStack(Item.potion.shiftedIndex, 1, 0));
					}
				}
			}

			return itemStack1;
		}
	}
}

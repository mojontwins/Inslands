package net.minecraft.game.item;

import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.world.World;

public final class ItemPainting extends Item {
	public ItemPainting(int i1) {
		super(65);
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack itemStack1, World world2, int i3, int i4, int i5, int i6) {
		if(i6 == 0) {
			return false;
		} else if(i6 == 1) {
			return false;
		} else {
			byte b7 = 0;
			if(i6 == 4) {
				b7 = 1;
			}

			if(i6 == 3) {
				b7 = 2;
			}

			if(i6 == 5) {
				b7 = 3;
			}

			EntityPainting entityPainting8;
			if((entityPainting8 = new EntityPainting(world2, i3, i4, i5, b7)).onValidSurface()) {
				world2.spawnEntityInWorld(entityPainting8);
				--itemStack1.stackSize;
			}

			return true;
		}
	}
}
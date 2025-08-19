package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.world.World;

public final class ItemBow extends Item {
	public ItemBow(int i1) {
		super(5);
		this.maxStackSize = 1;
	}

	public final ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(entityPlayer3.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			world2.playSoundAtEntity(entityPlayer3, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
			world2.spawnEntityInWorld(new EntityArrow(world2, entityPlayer3));
		}

		return itemStack1;
	}
}
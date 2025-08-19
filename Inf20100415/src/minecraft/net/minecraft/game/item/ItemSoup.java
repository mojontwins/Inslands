package net.minecraft.game.item;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;

public final class ItemSoup extends ItemFood {
	public ItemSoup(int i1, int i2) {
		super(26, 10);
	}

	public final ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		super.onItemRightClick(itemStack1, world2, entityPlayer3);
		return new ItemStack(Item.bowlEmpty);
	}
}
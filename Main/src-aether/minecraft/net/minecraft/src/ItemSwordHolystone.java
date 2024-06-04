package net.minecraft.src;

import java.util.Random;

public class ItemSwordHolystone extends ItemSword {
	public ItemSwordHolystone(int itemID, EnumToolMaterial mat) {
		super(itemID, mat);
	}

	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		if((new Random()).nextInt(25) == 0 && entityliving1 != null && entityliving1 instanceof EntityPlayer && (entityliving.hurtTime > 0 || entityliving.deathTime > 0)) {
			entityliving.dropItemWithOffset(AetherItems.AmbrosiumShard.shiftedIndex, 1, 0.0F);
			itemstack.damageItem(1, entityliving1);
		}

		itemstack.damageItem(1, entityliving1);
		return true;
	}
}

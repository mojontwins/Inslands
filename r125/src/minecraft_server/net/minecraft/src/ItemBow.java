package net.minecraft.src;

public class ItemBow extends Item {
	public ItemBow(int i1) {
		super(i1);
		this.maxStackSize = 1;
		this.setMaxDamage(384);
	}

	public void onPlayerStoppedUsing(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3, int i4) {
		boolean z5 = entityPlayer3.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemStack1) > 0;
		if(z5 || entityPlayer3.inventory.hasItem(Item.arrow.shiftedIndex)) {
			int i6 = this.getMaxItemUseDuration(itemStack1) - i4;
			float f7 = (float)i6 / 20.0F;
			f7 = (f7 * f7 + f7 * 2.0F) / 3.0F;
			if((double)f7 < 0.1D) {
				return;
			}

			if(f7 > 1.0F) {
				f7 = 1.0F;
			}

			EntityArrow entityArrow8 = new EntityArrow(world2, entityPlayer3, f7 * 2.0F);
			if(f7 == 1.0F) {
				entityArrow8.arrowCritical = true;
			}

			int i9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack1);
			if(i9 > 0) {
				entityArrow8.setDamage(entityArrow8.getDamage() + (double)i9 * 0.5D + 0.5D);
			}

			int i10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack1);
			if(i10 > 0) {
				entityArrow8.func_46007_b(i10);
			}

			if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack1) > 0) {
				entityArrow8.setFire(100);
			}

			itemStack1.damageItem(1, entityPlayer3);
			world2.playSoundAtEntity(entityPlayer3, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f7 * 0.5F);
			if(!z5) {
				entityPlayer3.inventory.consumeInventoryItem(Item.arrow.shiftedIndex);
			} else {
				entityArrow8.doesArrowBelongToPlayer = false;
			}

			if(!world2.isRemote) {
				world2.spawnEntityInWorld(entityArrow8);
			}
		}

	}

	public ItemStack onFoodEaten(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		return itemStack1;
	}

	public int getMaxItemUseDuration(ItemStack itemStack1) {
		return 72000;
	}

	public EnumAction getItemUseAction(ItemStack itemStack1) {
		return EnumAction.bow;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(entityPlayer3.capabilities.isCreativeMode || entityPlayer3.inventory.hasItem(Item.arrow.shiftedIndex)) {
			entityPlayer3.setItemInUse(itemStack1, this.getMaxItemUseDuration(itemStack1));
		}

		return itemStack1;
	}

	public int getItemEnchantability() {
		return 1;
	}
}

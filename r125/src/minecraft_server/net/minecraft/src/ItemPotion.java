package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ItemPotion extends Item {
	private HashMap effectCache = new HashMap();

	public ItemPotion(int i1) {
		super(i1);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public List getEffects(ItemStack itemStack1) {
		return this.getEffects(itemStack1.getItemDamage());
	}

	public List getEffects(int i1) {
		List list2 = (List)this.effectCache.get(i1);
		if(list2 == null) {
			list2 = PotionHelper.getPotionEffects(i1, false);
			this.effectCache.put(i1, list2);
		}

		return list2;
	}

	public ItemStack onFoodEaten(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		--itemStack1.stackSize;
		if(!world2.isRemote) {
			List list4 = this.getEffects(itemStack1);
			if(list4 != null) {
				Iterator iterator5 = list4.iterator();

				while(iterator5.hasNext()) {
					PotionEffect potionEffect6 = (PotionEffect)iterator5.next();
					entityPlayer3.addPotionEffect(new PotionEffect(potionEffect6));
				}
			}
		}

		if(itemStack1.stackSize <= 0) {
			return new ItemStack(Item.glassBottle);
		} else {
			entityPlayer3.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
			return itemStack1;
		}
	}

	public int getMaxItemUseDuration(ItemStack itemStack1) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemStack1) {
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(isSplash(itemStack1.getItemDamage())) {
			--itemStack1.stackSize;
			world2.playSoundAtEntity(entityPlayer3, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if(!world2.isRemote) {
				world2.spawnEntityInWorld(new EntityPotion(world2, entityPlayer3, itemStack1.getItemDamage()));
			}

			return itemStack1;
		} else {
			entityPlayer3.setItemInUse(itemStack1, this.getMaxItemUseDuration(itemStack1));
			return itemStack1;
		}
	}

	public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
		return false;
	}

	public static boolean isSplash(int i0) {
		return (i0 & 16384) != 0;
	}
}

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

	public int getIconFromDamage(int i1) {
		return isSplash(i1) ? 154 : 140;
	}

	public int func_46057_a(int i1, int i2) {
		return i2 == 0 ? 141 : super.func_46057_a(i1, i2);
	}

	public static boolean isSplash(int i0) {
		return (i0 & 16384) != 0;
	}

	public int getColorFromDamage(int i1, int i2) {
		return i2 > 0 ? 0xFFFFFF : PotionHelper.func_40358_a(i1, false);
	}

	public boolean func_46058_c() {
		return true;
	}

	public boolean isEffectInstant(int i1) {
		List list2 = this.getEffects(i1);
		if(list2 != null && !list2.isEmpty()) {
			Iterator iterator3 = list2.iterator();

			PotionEffect potionEffect4;
			do {
				if(!iterator3.hasNext()) {
					return false;
				}

				potionEffect4 = (PotionEffect)iterator3.next();
			} while(!Potion.potionTypes[potionEffect4.getPotionID()].isInstant());

			return true;
		} else {
			return false;
		}
	}

	public String getItemDisplayName(ItemStack itemStack1) {
		if(itemStack1.getItemDamage() == 0) {
			return StatCollector.translateToLocal("item.emptyPotion.name").trim();
		} else {
			String string2 = "";
			if(isSplash(itemStack1.getItemDamage())) {
				string2 = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
			}

			List list3 = Item.potion.getEffects(itemStack1);
			String string4;
			if(list3 != null && !list3.isEmpty()) {
				string4 = ((PotionEffect)list3.get(0)).getEffectName();
				string4 = string4 + ".postfix";
				return string2 + StatCollector.translateToLocal(string4).trim();
			} else {
				string4 = PotionHelper.func_40359_b(itemStack1.getItemDamage());
				return StatCollector.translateToLocal(string4).trim() + " " + super.getItemDisplayName(itemStack1);
			}
		}
	}

	public void addInformation(ItemStack itemStack1, List list2) {
		if(itemStack1.getItemDamage() != 0) {
			List list3 = Item.potion.getEffects(itemStack1);
			if(list3 != null && !list3.isEmpty()) {
				Iterator iterator7 = list3.iterator();

				while(iterator7.hasNext()) {
					PotionEffect potionEffect5 = (PotionEffect)iterator7.next();
					String string6 = StatCollector.translateToLocal(potionEffect5.getEffectName()).trim();
					if(potionEffect5.getAmplifier() > 0) {
						string6 = string6 + " " + StatCollector.translateToLocal("potion.potency." + potionEffect5.getAmplifier()).trim();
					}

					if(potionEffect5.getDuration() > 20) {
						string6 = string6 + " (" + Potion.getDurationString(potionEffect5) + ")";
					}

					if(Potion.potionTypes[potionEffect5.getPotionID()].isBadEffect()) {
						list2.add("\u00a7c" + string6);
					} else {
						list2.add("\u00a77" + string6);
					}
				}
			} else {
				String string4 = StatCollector.translateToLocal("potion.empty").trim();
				list2.add("\u00a77" + string4);
			}

		}
	}

	public boolean hasEffect(ItemStack itemStack1) {
		List list2 = this.getEffects(itemStack1);
		return list2 != null && !list2.isEmpty();
	}
}

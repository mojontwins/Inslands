package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

public class ItemSwordElemental extends ItemSword {
	public static ArrayList undead = new ArrayList();
	private int weaponDamage;
	private int holyDamage;
	private EnumElement element;
	private int colour;

	public ItemSwordElemental(int i, EnumElement element, int colour) {
		super(i, EnumToolMaterial.EMERALD);
		this.setIconIndex(AetherItems.ElementalSwordIcon);
		this.maxStackSize = 1;
		this.setMaxDamage(element == EnumElement.Holy ? 128 : 32);
		this.weaponDamage = 4;
		this.holyDamage = 20;
		this.element = element;
		this.colour = colour;
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return 1.5F;
	}

	public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
		itemstack.damageItem(2, entityliving);
		return true;
	}

	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		if(this.element == EnumElement.Fire) {
			entityliving.fire = 600;
		} else if(this.element == EnumElement.Lightning) {
			double d10004 = (double)((int)entityliving.posX);
			double d10005 = (double)((int)entityliving.posY);
			ModLoader.getMinecraftInstance().theWorld.entityJoinedWorld(new EntityAetherLightning(ModLoader.getMinecraftInstance().theWorld, d10004, d10005, (double)((int)entityliving.posZ)));
		}

		itemstack.damageItem(1, entityliving1);
		return true;
	}

	public int getDamageVsEntity(Entity entity) {
		if(this.element == EnumElement.Holy && entity instanceof EntityLiving) {
			EntityLiving living = (EntityLiving)entity;
			Iterator i$ = undead.iterator();

			while(i$.hasNext()) {
				Class cls = (Class)i$.next();
				if(living.getClass().isAssignableFrom(cls)) {
					return this.holyDamage;
				}
			}
		}

		return this.weaponDamage;
	}

	public int getColorFromDamage(int i) {
		return this.colour;
	}

	public boolean isFull3D() {
		return true;
	}

	static {
		undead.add(EntityZombie.class);
		undead.add(EntitySkeleton.class);
		undead.add(EntityPigZombie.class);
	}
}

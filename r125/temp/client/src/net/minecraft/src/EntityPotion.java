package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityPotion extends EntityThrowable {
	private int potionDamage;

	public EntityPotion(World world1) {
		super(world1);
	}

	public EntityPotion(World world1, EntityLiving entityLiving2, int i3) {
		super(world1, entityLiving2);
		this.potionDamage = i3;
	}

	public EntityPotion(World world1, double d2, double d4, double d6, int i8) {
		super(world1, d2, d4, d6);
		this.potionDamage = i8;
	}

	protected float func_40075_e() {
		return 0.05F;
	}

	protected float func_40077_c() {
		return 0.5F;
	}

	protected float func_40074_d() {
		return -20.0F;
	}

	public int getPotionDamage() {
		return this.potionDamage;
	}

	protected void onImpact(MovingObjectPosition movingObjectPosition1) {
		if(!this.worldObj.isRemote) {
			List list2 = Item.potion.getEffects(this.potionDamage);
			if(list2 != null && !list2.isEmpty()) {
				AxisAlignedBB axisAlignedBB3 = this.boundingBox.expand(4.0D, 2.0D, 4.0D);
				List list4 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, axisAlignedBB3);
				if(list4 != null && !list4.isEmpty()) {
					Iterator iterator5 = list4.iterator();

					label48:
					while(true) {
						Entity entity6;
						double d7;
						do {
							if(!iterator5.hasNext()) {
								break label48;
							}

							entity6 = (Entity)iterator5.next();
							d7 = this.getDistanceSqToEntity(entity6);
						} while(d7 >= 16.0D);

						double d9 = 1.0D - Math.sqrt(d7) / 4.0D;
						if(entity6 == movingObjectPosition1.entityHit) {
							d9 = 1.0D;
						}

						Iterator iterator11 = list2.iterator();

						while(iterator11.hasNext()) {
							PotionEffect potionEffect12 = (PotionEffect)iterator11.next();
							int i13 = potionEffect12.getPotionID();
							if(Potion.potionTypes[i13].isInstant()) {
								Potion.potionTypes[i13].affectEntity(this.thrower, (EntityLiving)entity6, potionEffect12.getAmplifier(), d9);
							} else {
								int i14 = (int)(d9 * (double)potionEffect12.getDuration() + 0.5D);
								if(i14 > 20) {
									((EntityLiving)entity6).addPotionEffect(new PotionEffect(i13, i14, potionEffect12.getAmplifier()));
								}
							}
						}
					}
				}
			}

			this.worldObj.playAuxSFX(2002, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), this.potionDamage);
			this.setDead();
		}

	}
}

package com.mojontwins.minecraft.icepalace;

import com.chocolatin.betterdungeons.EntityPirateArcher;
import com.mojontwins.minecraft.entity.status.Status;
import com.mojontwins.minecraft.entity.status.StatusEffect;

import net.minecraft.src.World;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityArrowWithEffect;

public class EntityIceArcher extends EntityPirateArcher implements ISentient {

	public EntityIceArcher(World world) {
		super(world);
		this.texture = "/mob/ice_archer.png";	
	}

	public EntityArrow selectArrow() {
		switch (this.rand.nextInt(4)) {
		case 1: 
			return new EntityArrowWithEffect(this.worldObj, this)
					.withStatusEffect(new StatusEffect(Status.statusPoisoned.id, 100, 1));
		case 2: 
			return new EntityArrowWithEffect(this.worldObj, this)
					.withStatusEffect(new StatusEffect(Status.statusSlowness.id, 100, 1));
		default:
			return new EntityArrow(this.worldObj, this);
		}
	}
	
	@Override
	protected String getHurtSound() {
		return "mob.ice.hurt";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.ice.hurt";
	}
	
	@Override
	protected String getLivingSound() {
		return "mob.ice.idle";
	}
}

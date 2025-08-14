package net.minecraft.world.entity.sentient;

import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.monster.EntityPirateArcher;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityArrowWithEffect;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.entity.status.StatusEffect;
import net.minecraft.world.level.World;

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

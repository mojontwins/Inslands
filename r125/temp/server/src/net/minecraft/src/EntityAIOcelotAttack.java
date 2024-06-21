package net.minecraft.src;

public class EntityAIOcelotAttack extends EntityAIBase {
	World theWorld;
	EntityLiving theEntity;
	EntityLiving field_48170_c;
	int field_48168_d = 0;

	public EntityAIOcelotAttack(EntityLiving entityLiving1) {
		this.theEntity = entityLiving1;
		this.theWorld = entityLiving1.worldObj;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		EntityLiving entityLiving1 = this.theEntity.getAttackTarget();
		if(entityLiving1 == null) {
			return false;
		} else {
			this.field_48170_c = entityLiving1;
			return true;
		}
	}

	public boolean continueExecuting() {
		return !this.field_48170_c.isEntityAlive() ? false : (this.theEntity.getDistanceSqToEntity(this.field_48170_c) > 225.0D ? false : !this.theEntity.getNavigator().noPath() || this.shouldExecute());
	}

	public void resetTask() {
		this.field_48170_c = null;
		this.theEntity.getNavigator().clearPathEntity();
	}

	public void updateTask() {
		this.theEntity.getLookHelper().setLookPositionWithEntity(this.field_48170_c, 30.0F, 30.0F);
		double d1 = (double)(this.theEntity.width * 2.0F * this.theEntity.width * 2.0F);
		double d3 = this.theEntity.getDistanceSq(this.field_48170_c.posX, this.field_48170_c.boundingBox.minY, this.field_48170_c.posZ);
		float f5 = 0.23F;
		if(d3 > d1 && d3 < 16.0D) {
			f5 = 0.4F;
		} else if(d3 < 225.0D) {
			f5 = 0.18F;
		}

		this.theEntity.getNavigator().func_48652_a(this.field_48170_c, f5);
		this.field_48168_d = Math.max(this.field_48168_d - 1, 0);
		if(d3 <= d1) {
			if(this.field_48168_d <= 0) {
				this.field_48168_d = 20;
				this.theEntity.attackEntityAsMob(this.field_48170_c);
			}
		}
	}
}

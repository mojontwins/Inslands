package net.minecraft.world.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;

public class EntityAILeapAtTarget extends EntityAIBase {
	EntityLiving leaper;
	EntityLiving leapTarget;
	float leapMotionY;

	public EntityAILeapAtTarget(EntityLiving entityLiving, float leapMotionY) {
		this.leaper = entityLiving;
		this.leapMotionY = leapMotionY;
		this.setMutexBits(5);
	}

	public boolean shouldExecute() {
		this.leapTarget = this.leaper.getAttackTarget();
		if (this.leapTarget == null) {
			return false;
		} else {
			double distance = this.leaper.getDistanceSqToEntity(this.leapTarget);
			return distance >= 4.0D && distance <= 16.0D ? (!this.leaper.onGround ? false : this.leaper.getRNG().nextInt(5) == 0) : false;
		}
	}

	public boolean continueExecuting() {
		return !this.leaper.onGround;
	}

	public void startExecuting() {
		double dX = this.leapTarget.posX - this.leaper.posX;
		double dZ = this.leapTarget.posZ - this.leaper.posZ;
		float distance = MathHelper.sqrt_double(dX * dX + dZ * dZ);
		this.leaper.motionX += dX / (double) distance * 0.5D * (double) 0.8F + this.leaper.motionX * (double) 0.2F;
		this.leaper.motionZ += dZ / (double) distance * 0.5D * (double) 0.8F + this.leaper.motionZ * (double) 0.2F;
		this.leaper.motionY = (double) this.leapMotionY;
	}
}

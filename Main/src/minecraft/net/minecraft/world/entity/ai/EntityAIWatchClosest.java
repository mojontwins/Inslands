package net.minecraft.world.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;

public class EntityAIWatchClosest extends EntityAIBase {
	private EntityLiving theEntity;
	private Entity closestEntity;
	private float maxDistance;
	private int watchTicks;
	private float chance;
	private Class<?> targetClass;

	public EntityAIWatchClosest(EntityLiving entityLiving, Class<?> targetClass, float maxDistance) {
		this.theEntity = entityLiving;
		this.targetClass = targetClass;
		this.maxDistance = maxDistance;
		this.chance = 0.02F;
		this.setMutexBits(2);
	}

	public EntityAIWatchClosest(EntityLiving entityLiving, Class<?> targetClass, float maxDistance, float chance) {
		this.theEntity = entityLiving;
		this.targetClass = targetClass;
		this.maxDistance = maxDistance;
		this.chance = chance;
		this.setMutexBits(2);
	}

	public boolean shouldExecute() {
		if (this.theEntity.getRNG().nextFloat() >= this.chance) {
			return false;
		} else {
			if (this.targetClass == EntityPlayer.class) {
				this.closestEntity = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, (double) this.maxDistance);
			} else {
				this.closestEntity = this.theEntity.worldObj.findNearestEntityWithinAABB(this.targetClass, this.theEntity.boundingBox.expand((double) this.maxDistance, 3.0D, (double) this.maxDistance), this.theEntity);
			}

			return this.closestEntity != null;
		}
	}

	public boolean continueExecuting() {
		return !this.closestEntity.isEntityAlive() ? false : (this.theEntity.getDistanceSqToEntity(this.closestEntity) > (double) (this.maxDistance * this.maxDistance) ? false : this.watchTicks > 0);
	}

	public void startExecuting() {
		this.watchTicks = 40 + this.theEntity.getRNG().nextInt(40);
	}

	public void resetTask() {
		this.closestEntity = null;
	}

	public void updateTask() {
		this.theEntity.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + (double) this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0F, (float) this.theEntity.getVerticalFaceSpeed());
		--this.watchTicks;
	}
}

package net.minecraft.world.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.mob.EntityMob;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.World;

public class EntityAIAttackOnCollide extends EntityAIBase {
	World worldObj;
	EntityLiving attacker;
	EntityLiving entityTarget;
	int attackDelay;
	float movementSpeed;
	boolean movementSensitive;
	PathEntity pathEntity;
	Class<?> classTarget;
	private int pathUpdateCooldown;

	public EntityAIAttackOnCollide(EntityLiving entityLiving, Class<?> targetClass, float movementSpeed, boolean movementSensitive) {
		this(entityLiving, movementSpeed, movementSensitive);
		this.classTarget = targetClass;
	}

	public EntityAIAttackOnCollide(EntityLiving entityLiving, float movementSpeed, boolean movementSensitive) {
		this.attackDelay = 0;
		this.attacker = entityLiving;
		this.worldObj = entityLiving.worldObj;
		this.movementSpeed = movementSpeed;
		this.movementSensitive = movementSensitive;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		EntityLiving target = this.attacker.getAttackTarget();
		if (target == null) {
			return false;
		} else if (this.classTarget != null && !this.classTarget.isAssignableFrom(target.getClass())) {
			return false;
		} else {
			this.entityTarget = target;
			this.pathEntity = this.attacker.getNavigator().getPathToEntity(this.entityTarget);
			return this.pathEntity != null;
		}
	}

	public boolean continueExecuting() {
		EntityLiving target = this.attacker.getAttackTarget();
		return target == null ? false : (!this.entityTarget.isEntityAlive() ? false : (!this.movementSensitive ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistance(MathHelper.floor_double(this.entityTarget.posX), MathHelper.floor_double(this.entityTarget.posY), MathHelper.floor_double(this.entityTarget.posZ))));
	}

	public void startExecuting() {
		this.attacker.getNavigator().setPath(this.pathEntity, this.movementSpeed);
		this.pathUpdateCooldown = 0;
	}

	public void resetTask() {
		this.entityTarget = null;
		this.attacker.getNavigator().clearPathEntity();
	}

	public void updateTask() {
		this.attacker.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
		if ((this.movementSensitive || this.attacker.getEntitySenses().canSee(this.entityTarget)) && --this.pathUpdateCooldown <= 0) {
			this.pathUpdateCooldown = 4 + this.attacker.getRNG().nextInt(7);
			this.attacker.getNavigator().getPathToEntityWithSpeed(this.entityTarget, this.movementSpeed);
		}

		this.attackDelay = Math.max(this.attackDelay - 1, 0);
		double attackRange = (double) (this.attacker.width * 2.0F * this.attacker.width * 2.0F);
		if (this.attacker.getDistanceSq(this.entityTarget.posX, this.entityTarget.boundingBox.minY, this.entityTarget.posZ) <= attackRange) {
			if (this.attackDelay <= 0) {
				this.attackDelay = 20;
				if (this.attacker instanceof EntityMob) {
					((EntityMob) this.attacker).attackEntityAsMob(this.entityTarget);
				}
			}
		}
	}
}

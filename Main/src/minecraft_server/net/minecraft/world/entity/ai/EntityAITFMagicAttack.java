package net.minecraft.world.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.projectile.EntityTFNatureBolt;
import net.minecraft.world.level.World;

public class EntityAITFMagicAttack extends EntityAIBase {
	World worldObj;
	EntityLiving entityHost;
	EntityLiving attackTarget;
	int rangedAttackTime = 0;
	float movementSpeed;
	int canSeeTickCount = 0;
	int rangedAttackID;
	int maxRangedAttackTime;

	public EntityAITFMagicAttack(EntityLiving entityLiving, float movementSpeed, int rangedAttackID, int maxRangedAttackTime) {
		this.entityHost = entityLiving;
		this.worldObj = entityLiving.worldObj;
		this.movementSpeed = movementSpeed;
		this.rangedAttackID = rangedAttackID;
		this.maxRangedAttackTime = maxRangedAttackTime;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		EntityLiving target = this.entityHost.getAttackTarget();
		if (target == null) {
			return false;
		} else {
			this.attackTarget = target;
			return true;
		}
	}

	public boolean continueExecuting() {
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	public void resetTask() {
		this.attackTarget = null;
	}

	public void updateTask() {
		double attackRange = 100.0D;
		double distance = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
		boolean lineOfSight = this.entityHost.getEntitySenses().canSee(this.attackTarget);
		if (lineOfSight) {
			++this.canSeeTickCount;
		} else {
			this.canSeeTickCount = 0;
		}

		if (distance <= attackRange && this.canSeeTickCount >= 20) {
			this.entityHost.getNavigator().clearPathEntity();
		} else {
			this.entityHost.getNavigator().getPathToEntityWithSpeed(this.attackTarget, this.movementSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
		if (this.rangedAttackTime <= 0 && distance <= attackRange && lineOfSight) {
			this.doRangedAttack();
			this.rangedAttackTime = this.maxRangedAttackTime;
		}
	}

	protected void doRangedAttack() {
		if (this.rangedAttackID == 1) {
			EntityTFNatureBolt projectile = new EntityTFNatureBolt(this.worldObj, this.entityHost);
			double tx = this.attackTarget.posX - this.entityHost.posX;
			double ty = this.attackTarget.posY + (double) this.attackTarget.getEyeHeight() - 1.100000023841858D - projectile.posY;
			double tz = this.attackTarget.posZ - this.entityHost.posZ;
			float heightOffset = MathHelper.sqrt_double(tx * tx + tz * tz) * 0.2F;
			projectile.setThrowableHeading(tx, ty + (double) heightOffset, tz, 0.6F, 6.0F);
			this.worldObj.playSoundAtEntity(this.entityHost, "mob.ghast.fireball", 1.0F, 1.0F / (this.entityHost.getRNG().nextFloat() * 0.4F + 0.8F));
			this.worldObj.spawnEntityInWorld(projectile);
		}
	}
}

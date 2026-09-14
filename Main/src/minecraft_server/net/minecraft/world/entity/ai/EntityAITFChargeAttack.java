package net.minecraft.world.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.mob.EntityMob;
import net.minecraft.world.entity.mob.twilight.EntityTFMinotaur;
import net.minecraft.world.phys.Vec3D;

public class EntityAITFChargeAttack extends EntityAIBase {
	protected static final double MIN_RANGE_SQ = 16.0D;
	protected static final double MAX_RANGE_SQ = 64.0D;
	protected static final int FREQ = 1;
	protected EntityLiving charger;
	protected EntityLiving chargeTarget;
	protected double chargeX;
	protected double chargeY;
	protected double chargeZ;
	protected float speed;
	protected int windup;
	protected boolean hasAttacked;

	public EntityAITFChargeAttack(EntityLiving entityLiving, float speed) {
		this.charger = entityLiving;
		this.speed = speed;
		this.windup = 0;
		this.hasAttacked = false;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		this.chargeTarget = this.charger.getAttackTarget();

		if (this.chargeTarget == null) {
			return false;
		} else {
			double distance = this.charger.getDistanceSqToEntity(this.chargeTarget);

			if (distance >= 16.0D && distance <= 64.0D) {
				if (!this.charger.onGround) {
					return false;
				} else {
					Vec3D chargePoint = this.findChargePoint(this.charger, this.chargeTarget, 2.1D);
					//boolean lineOfSight = this.chargeTarget.worldObj.rayTraceBlocks(Vec3.getVec3Pool().getVecFromPool(this.chargeTarget.posX, this.chargeTarget.posY + (double) this.chargeTarget.getEyeHeight(), this.chargeTarget.posZ), chargePoint) == null;

					boolean lineOfSight = this.chargeTarget.worldObj.rayTraceBlocks(Vec3D.createVector(this.chargeTarget.posX, this.chargeTarget.posY + (double) this.chargeTarget.getEyeHeight(),
							this.chargeTarget.posZ), chargePoint) == null;

					if (chargePoint != null && lineOfSight) {
						this.chargeX = chargePoint.xCoord;
						this.chargeY = chargePoint.yCoord;
						this.chargeZ = chargePoint.zCoord;
						return this.charger.getRNG().nextInt(1) == 0;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.windup > 0 || !this.charger.getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.windup = 15 + this.charger.getRNG().nextInt(30);
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		this.charger.getLookHelper().setLookPosition(this.chargeX, this.chargeY - 1.0D, this.chargeZ, 10.0F,
				(float) this.charger.getVerticalFaceSpeed());

		if (this.windup > 0) {
			if (--this.windup == 0) {
				this.charger.getNavigator().tryMoveToXYZ(this.chargeX, this.chargeY, this.chargeZ,
						this.speed);
			} else {
				this.charger.limbYaw = (float) ((double) this.charger.limbYaw + 0.8D);

				if (this.charger instanceof EntityTFMinotaur) {
					((EntityTFMinotaur) this.charger).setCharging(true);
				}
			}
		}

		double attackRange = (double) (this.charger.width * 2.1F * this.charger.width * 2.1F);

		if (this.charger.getDistanceSq(this.chargeTarget.posX, this.chargeTarget.boundingBox.minY,
				this.chargeTarget.posZ) <= attackRange && !this.hasAttacked) {
			this.hasAttacked = true;
			if (this.charger instanceof EntityMob) {
				((EntityMob) this.charger).attackEntityAsMob(this.chargeTarget);
			}
		}
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.windup = 0;
		this.chargeTarget = null;
		this.hasAttacked = false;

		if (this.charger instanceof EntityTFMinotaur) {
			((EntityTFMinotaur) this.charger).setCharging(false);
		}
	}

	protected Vec3D findChargePoint(Entity charger, Entity target, double extraDistance) {
		double dX = target.posX - charger.posX;
		double dZ = target.posZ - charger.posZ;
		float yawAngle = (float) Math.atan2(dZ, dX);
		double distance = (double) MathHelper.sqrt_double(dX * dX + dZ * dZ);
		double offsetX = (double) MathHelper.cos(yawAngle) * (distance + extraDistance);
		double offsetZ = (double) MathHelper.sin(yawAngle) * (distance + extraDistance);
		//return Vec3.getVec3Pool().getVecFromPool(charger.posX + offsetX, target.posY, charger.posZ + offsetZ);
		return Vec3D.createVector(charger.posX + offsetX, target.posY, charger.posZ + offsetZ);
	}
}

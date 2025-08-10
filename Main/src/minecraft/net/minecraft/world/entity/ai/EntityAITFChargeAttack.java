package net.minecraft.world.entity.ai;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntityMob;
import net.minecraft.world.entity.monster.EntityTFMinotaur;

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

	public EntityAITFChargeAttack(EntityLiving var1, float var2) {
		this.charger = var1;
		this.speed = var2;
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
			double var1 = this.charger.getDistanceSqToEntity(this.chargeTarget);

			if (var1 >= 16.0D && var1 <= 64.0D) {
				if (!this.charger.onGround) {
					return false;
				} else {
					Vec3D var3 = this.findChargePoint(this.charger, this.chargeTarget, 2.1D);
					//boolean var4 = this.chargeTarget.worldObj.rayTraceBlocks(Vec3.getVec3Pool().getVecFromPool(this.chargeTarget.posX, this.chargeTarget.posY + (double) this.chargeTarget.getEyeHeight(), this.chargeTarget.posZ), var3) == null;

					boolean var4 = this.chargeTarget.worldObj.rayTraceBlocks(Vec3D.createVector(this.chargeTarget.posX, this.chargeTarget.posY + (double) this.chargeTarget.getEyeHeight(),
							this.chargeTarget.posZ), var3) == null;
					
					if (var3 != null && var4) {
						this.chargeX = var3.xCoord;
						this.chargeY = var3.yCoord;
						this.chargeZ = var3.zCoord;
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

		double var3 = (double) (this.charger.width * 2.1F * this.charger.width * 2.1F);

		if (this.charger.getDistanceSq(this.chargeTarget.posX, this.chargeTarget.boundingBox.minY,
				this.chargeTarget.posZ) <= var3 && !this.hasAttacked) {
			this.hasAttacked = true;
			if(this.charger instanceof EntityMob) {
				((EntityMob)this.charger).attackEntityAsMob(this.chargeTarget);
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

	protected Vec3D findChargePoint(Entity var1, Entity var2, double var3) {
		double var5 = var2.posX - var1.posX;
		double var7 = var2.posZ - var1.posZ;
		float var9 = (float) Math.atan2(var7, var5);
		double var10 = (double) MathHelper.sqrt_double(var5 * var5 + var7 * var7);
		double var12 = (double) MathHelper.cos(var9) * (var10 + var3);
		double var14 = (double) MathHelper.sin(var9) * (var10 + var3);
		//return Vec3.getVec3Pool().getVecFromPool(var1.posX + var12, var2.posY, var1.posZ + var14);
		return Vec3D.createVector(var1.posX + var12, var2.posY, var1.posZ + var14);
	}
}

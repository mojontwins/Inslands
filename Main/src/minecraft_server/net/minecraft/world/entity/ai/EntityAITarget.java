package net.minecraft.world.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.animal.EntityTameable;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.pathfinder.PathPoint;

public abstract class EntityAITarget extends EntityAIBase {
	protected EntityLiving taskOwner;
	protected float targetDistance;
	protected boolean shouldCheckSight;
	private boolean needsPathCheck;
	private int pathCheckResult;
	private int pathCheckCooldown;
	private int ticksWithoutVisibility;

	public EntityAITarget(EntityLiving entityLiving, float distance, boolean shouldCheckSight) {
		this(entityLiving, distance, shouldCheckSight, false);
	}

	public EntityAITarget(EntityLiving entityLiving, float distance, boolean shouldCheckSight, boolean needsPathCheck) {
		this.pathCheckResult = 0;
		this.pathCheckCooldown = 0;
		this.ticksWithoutVisibility = 0;
		this.taskOwner = entityLiving;
		this.targetDistance = distance;
		this.shouldCheckSight = shouldCheckSight;
		this.needsPathCheck = needsPathCheck;
	}

	public boolean continueExecuting() {
		EntityLiving target = this.taskOwner.getAttackTarget();
		if (target == null) {
			return false;
		} else if (!target.isEntityAlive()) {
			return false;
		} else if (this.taskOwner.getDistanceSqToEntity(target) > (double) (this.targetDistance * this.targetDistance)) {
			return false;
		} else {
			if (this.shouldCheckSight) {
				if (!this.taskOwner.getEntitySenses().canSee(target)) {
					if (++this.ticksWithoutVisibility > 60) {
						return false;
					}
				} else {
					this.ticksWithoutVisibility = 0;
				}
			}

			return true;
		}
	}

	public void startExecuting() {
		this.pathCheckResult = 0;
		this.pathCheckCooldown = 0;
		this.ticksWithoutVisibility = 0;
	}

	public void resetTask() {
		this.taskOwner.setAttackTarget(null);
	}

	protected boolean isSuitableTarget(EntityLiving target, boolean allowPlayers) {
		if (target == null) {
			return false;
		} else if (target == this.taskOwner) {
			return false;
		} else if (!target.isEntityAlive()) {
			return false;
		} else if (target.boundingBox.maxY > this.taskOwner.boundingBox.minY && target.boundingBox.minY < this.taskOwner.boundingBox.maxY) {
			if (!this.taskOwner.func_48100_a(target.getClass())) {
				return false;
			} else {
				if (this.taskOwner instanceof EntityTameable && ((EntityTameable) this.taskOwner).isTamed()) {
					if (target instanceof EntityTameable && ((EntityTameable) target).isTamed()) {
						return false;
					}

					if (target == ((EntityTameable) this.taskOwner).getOwner()) {
						return false;
					}
				} else if (target instanceof EntityPlayer && !allowPlayers && ((EntityPlayer) target).isCreative) {
					return false;
				}

				if (!this.taskOwner.isWithinHomeDistance(MathHelper.floor_double(target.posX), MathHelper.floor_double(target.posY), MathHelper.floor_double(target.posZ))) {
					return false;
				} else if (this.shouldCheckSight && !this.taskOwner.getEntitySenses().canSee(target)) {
					return false;
				} else {
					if (this.needsPathCheck) {
						if (--this.pathCheckCooldown <= 0) {
							this.pathCheckResult = 0;
						}

						if (this.pathCheckResult == 0) {
							this.pathCheckResult = this.hasPathToTarget(target) ? 1 : 2;
						}

						if (this.pathCheckResult == 2) {
							return false;
						}
					}

					return true;
				}
			}
		} else {
			return false;
		}
	}

	private boolean hasPathToTarget(EntityLiving target) {
		this.pathCheckCooldown = 10 + this.taskOwner.getRNG().nextInt(5);
		PathEntity path = this.taskOwner.getNavigator().getPathToEntity(target);
		if (path == null) {
			return false;
		} else {
			PathPoint endPoint = path.getFinalPathPoint();
			if (endPoint == null) {
				return false;
			} else {
				int dX = endPoint.xCoord - MathHelper.floor_double(target.posX);
				int dZ = endPoint.zCoord - MathHelper.floor_double(target.posZ);
				return (double) (dX * dX + dZ * dZ) <= 2.25D;
			}
		}
	}
}

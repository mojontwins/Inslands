package net.minecraft.world.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.mob.twilight.EntityTFKobold;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.stats.AchievementList;

public class EntityAITFPanicOnFlockDeath extends EntityAIBase {
	private EntityCreature flockCreature;
	private float speed;
	private double fleeX;
	private double fleeY;
	private double fleeZ;
	int fleeTimer;

	public EntityAITFPanicOnFlockDeath(EntityCreature entityCreature, float speed) {
		this.flockCreature = entityCreature;
		this.speed = speed;
		this.setMutexBits(1);
		this.fleeTimer = 0;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		boolean panicking = this.fleeTimer > 0;
		List<Entity> nearbyCreatures = this.flockCreature.worldObj.getEntitiesWithinAABB(
				this.flockCreature.getClass(),
				this.flockCreature.boundingBox.expand(4.0D, 2.0D, 4.0D)
			);
		Iterator<Entity> iter = nearbyCreatures.iterator();

		while (iter.hasNext()) {
			EntityLiving nearbyCreature = (EntityLiving) iter.next();

			if (nearbyCreature.deathTime > 0) {
				panicking = true;
				if (nearbyCreature.lastAttackingEntity instanceof EntityPlayer) {
					((EntityPlayer) nearbyCreature.lastAttackingEntity).triggerAchievement(AchievementList.scareKobold);
				}
				break;
			}
		}

		if (!panicking) {
			return false;
		} else {
			Vec3D fleeTarget = RandomPositionGenerator.findRandomTarget(this.flockCreature, 5, 4);

			if (fleeTarget == null) {
				return false;
			} else {
				this.fleeX = fleeTarget.xCoord;
				this.fleeY = fleeTarget.yCoord;
				this.fleeZ = fleeTarget.zCoord;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.fleeTimer = 40;
		this.flockCreature.getNavigator().tryMoveToXYZ(this.fleeX, this.fleeY, this.fleeZ, this.speed);

		if (this.flockCreature instanceof EntityTFKobold) {
			((EntityTFKobold) this.flockCreature).setPanicked(true);
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.fleeTimer > 0 && !this.flockCreature.getNavigator().noPath();
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		--this.fleeTimer;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.fleeTimer -= 20;

		if (this.flockCreature instanceof EntityTFKobold) {
			((EntityTFKobold) this.flockCreature).setPanicked(false);
		}
	}
}

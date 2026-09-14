package net.minecraft.world.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.phys.Vec3D;

public class EntityAITFFlockToSameKind extends EntityAIBase {
	private static final double MAX_DIST = 256.0D;
	private static final double MIN_DIST = 25.0D;
	EntityLiving flockCreature;
	Vec3D flockPosition;
	float speed;
	private int moveTimer;

	public EntityAITFFlockToSameKind(EntityLiving entityLiving, float speed) {
		this.flockCreature = entityLiving;
		this.speed = speed;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.flockCreature.getRNG().nextInt(40) != 0) {
			return false;
		} else {
			List<Entity> nearbyCreatures = this.flockCreature.worldObj.getEntitiesWithinAABB(
					this.flockCreature.getClass(),
					this.flockCreature.boundingBox.expand(16.0D, 4.0D, 16.0D)
				);
			int count = 0;
			double sumX = 0.0D;
			double sumY = 0.0D;
			double sumZ = 0.0D;
			EntityLiving creature;

			for (Iterator<Entity> iter = nearbyCreatures.iterator(); iter.hasNext(); sumZ += creature.posZ) {
				creature = (EntityLiving) iter.next();
				++count;
				sumX += creature.posX;
				sumY += creature.posY;
			}

			sumX /= (double) count;
			sumY /= (double) count;
			sumZ /= (double) count;

			if (this.flockCreature.getDistanceSq(sumX, sumY, sumZ) < MIN_DIST) {
				return false;
			} else {
				this.flockPosition = Vec3D.createVector(sumX, sumY, sumZ);
				return true;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		if (this.flockPosition == null) {
			return false;
		} else {
			double distance = this.flockCreature.getDistanceSq(this.flockPosition.xCoord, this.flockPosition.yCoord,
					this.flockPosition.zCoord);
			return distance >= MIN_DIST && distance <= MAX_DIST;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.moveTimer = 0;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.flockPosition = null;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		if (--this.moveTimer <= 0) {
			this.moveTimer = 10;
			this.flockCreature.getNavigator().tryMoveToXYZ(this.flockPosition.xCoord, this.flockPosition.yCoord,
					this.flockPosition.zCoord, this.speed);
		}
	}
}

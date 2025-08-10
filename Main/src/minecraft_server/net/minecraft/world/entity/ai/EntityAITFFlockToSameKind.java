package net.minecraft.world.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.src.Vec3D;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;

public class EntityAITFFlockToSameKind extends EntityAIBase {
	private static final double MAX_DIST = 256.0D;
	private static final double MIN_DIST = 25.0D;
	EntityLiving flockCreature;
	Vec3D flockPosition;
	float speed;
	private int moveTimer;

	public EntityAITFFlockToSameKind(EntityLiving var1, float var2) {
		this.flockCreature = var1;
		this.speed = var2;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.flockCreature.getRNG().nextInt(40) != 0) {
			return false;
		} else {
			List<Entity> var1 = this.flockCreature.worldObj.getEntitiesWithinAABB(
					this.flockCreature.getClass(),
					this.flockCreature.boundingBox.expand(16.0D, 4.0D, 16.0D)
				);
			int var2 = 0;
			double var3 = 0.0D;
			double var5 = 0.0D;
			double var7 = 0.0D;
			EntityLiving var10;

			for (Iterator<Entity> var9 = var1.iterator(); var9.hasNext(); var7 += var10.posZ) {
				var10 = (EntityLiving) var9.next();
				++var2;
				var3 += var10.posX;
				var5 += var10.posY;
			}

			var3 /= (double) var2;
			var5 /= (double) var2;
			var7 /= (double) var2;

			if (this.flockCreature.getDistanceSq(var3, var5, var7) < MIN_DIST) {
				return false;
			} else {
				this.flockPosition = Vec3D.createVector(var3, var5, var7);
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
			double var1 = this.flockCreature.getDistanceSq(this.flockPosition.xCoord, this.flockPosition.yCoord,
					this.flockPosition.zCoord);
			return var1 >= MIN_DIST && var1 <= MAX_DIST;
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

package com.benimatic.twilightforest;

import java.util.Iterator;
import java.util.List;

import com.mojang.minecraft.modernAI.EntityAIBase;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RandomPositionGenerator;
import net.minecraft.src.Vec3D;

public class EntityAITFPanicOnFlockDeath extends EntityAIBase {
	private EntityCreature flockCreature;
	private float speed;
	private double fleeX;
	private double fleeY;
	private double fleeZ;
	int fleeTimer;

	public EntityAITFPanicOnFlockDeath(EntityCreature var1, float var2) {
		this.flockCreature = var1;
		this.speed = var2;
		this.setMutexBits(1);
		this.fleeTimer = 0;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		boolean var1 = this.fleeTimer > 0;
		List<Entity> var2 = this.flockCreature.worldObj.getEntitiesWithinAABB(
				this.flockCreature.getClass(),
				this.flockCreature.boundingBox.expand(4.0D, 2.0D, 4.0D)
			);
		Iterator<Entity> var3 = var2.iterator();

		while (var3.hasNext()) {
			EntityLiving var4 = (EntityLiving) var3.next();

			if (var4.deathTime > 0) {
				var1 = true;
				break;
			}
		}

		if (!var1) {
			return false;
		} else {
			Vec3D var5 = RandomPositionGenerator.findRandomTarget(this.flockCreature, 5, 4);

			if (var5 == null) {
				return false;
			} else {
				this.fleeX = var5.xCoord;
				this.fleeY = var5.yCoord;
				this.fleeZ = var5.zCoord;
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

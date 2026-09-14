package net.minecraft.world.entity.ai;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.phys.Vec3D;

public class EntityAIWander extends EntityAIBase {
	private EntityCreature entity;
	private double wanderTargetX;
	private double wanderTargetY;
	private double wanderTargetZ;
	private float movementSpeed;

	public EntityAIWander(EntityCreature entityCreature, float movementSpeed) {
		this.entity = entityCreature;
		this.movementSpeed = movementSpeed;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		/*if(this.entity.getAge() >= 100) {
			return false;
		} else */if (this.entity.getRNG().nextInt(120) != 0) {
			return false;
		} else {
			Vec3D target = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
			if (target == null) {
				return false;
			} else {
				this.wanderTargetX = target.xCoord;
				this.wanderTargetY = target.yCoord;
				this.wanderTargetZ = target.zCoord;
				return true;
			}
		}
	}

	public boolean continueExecuting() {
		return !this.entity.getNavigator().noPath();
	}

	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(this.wanderTargetX, this.wanderTargetY, this.wanderTargetZ, this.movementSpeed);
	}
}

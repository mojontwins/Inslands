package net.minecraft.world.entity.helper;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;

public class EntityMoveHelper {
	private EntityLiving entity;
	private double posX;
	private double posY;
	private double posZ;
	private float speed;
	private boolean isUpdating = false;

	public EntityMoveHelper(EntityLiving entityLiving) {
		this.entity = entityLiving;
		this.posX = entityLiving.posX;
		this.posY = entityLiving.posY;
		this.posZ = entityLiving.posZ;
	}

	public boolean isUpdating() {
		return this.isUpdating;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setMoveTo(double x, double y, double z, float speed) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.speed = speed;
		this.isUpdating = true;
	}

	public void onUpdateMoveHelper() {
		this.entity.setMoveForward(0.0F);
		if (this.isUpdating) {
			this.isUpdating = false;
			int entityY = MathHelper.floor_double(this.entity.boundingBox.minY + 0.5D);
			double dX = this.posX - this.entity.posX;
			double dZ = this.posZ - this.entity.posZ;
			double dY = this.posY - (double) entityY;
			double distanceSq = dX * dX + dY * dY + dZ * dZ;
			if (distanceSq >= 2.500000277905201E-7D) {
				float yaw = (float) (Math.atan2(dZ, dX) * 180.0D / (double) (float) Math.PI) - 90.0F;
				this.entity.rotationYaw = this.updateRotationYawWithLimit(this.entity.rotationYaw, yaw, 30.0F);
				this.entity.setAIMoveSpeed(this.speed);
				if (dY > 0.0D && dX * dX + dZ * dZ < 1.0D) {
					this.entity.getJumpHelper().setJumping();
				}
			}
		}
	}

	private float updateRotationYawWithLimit(float current, float target, float maxChange) {
		float result;
		for (result = target - current; result < -180.0F; result += 360.0F) {
		}

		while (result >= 180.0F) {
			result -= 360.0F;
		}

		if (result > maxChange) {
			result = maxChange;
		}

		if (result < -maxChange) {
			result = -maxChange;
		}

		return current + result;
	}
}

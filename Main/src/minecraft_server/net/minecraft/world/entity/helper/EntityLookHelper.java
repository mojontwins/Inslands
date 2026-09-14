package net.minecraft.world.entity.helper;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;

public class EntityLookHelper {
	private EntityLiving entity;
	private float maxYawChange;
	private float maxPitchChange;
	private boolean isLooking = false;
	private double posX;
	private double posY;
	private double posZ;

	public EntityLookHelper(EntityLiving entityLiving) {
		this.entity = entityLiving;
	}

	public void setLookPositionWithEntity(Entity target, float maxYaw, float maxPitch) {
		this.posX = target.posX;
		if (target instanceof EntityLiving) {
			this.posY = target.posY + (double) ((EntityLiving) target).getEyeHeight();
		} else {
			this.posY = (target.boundingBox.minY + target.boundingBox.maxY) / 2.0D;
		}

		this.posZ = target.posZ;
		this.maxYawChange = maxYaw;
		this.maxPitchChange = maxPitch;
		this.isLooking = true;
	}

	public void setLookPosition(double x, double y, double z, float maxYaw, float maxPitch) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.maxYawChange = maxYaw;
		this.maxPitchChange = maxPitch;
		this.isLooking = true;
	}

	public void onUpdateLook() {
		this.entity.rotationPitch = 0.0F;
		if (this.isLooking) {
			this.isLooking = false;
			double dX = this.posX - this.entity.posX;
			double dY = this.posY - (this.entity.posY + (double) this.entity.getEyeHeight());
			double dZ = this.posZ - this.entity.posZ;
			double distance = (double) MathHelper.sqrt_double(dX * dX + dZ * dZ);
			float yaw = (float) (Math.atan2(dZ, dX) * 180.0D / (double) (float) Math.PI) - 90.0F;
			float pitch = (float) (-(Math.atan2(dY, distance) * 180.0D / (double) (float) Math.PI));
			this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, pitch, this.maxPitchChange);
			this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, yaw, this.maxYawChange);
		} else {
			this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
		}

		float yawOffsetDiff;
		for (yawOffsetDiff = this.entity.rotationYawHead - this.entity.renderYawOffset; yawOffsetDiff < -180.0F; yawOffsetDiff += 360.0F) {
		}

		while (yawOffsetDiff >= 180.0F) {
			yawOffsetDiff -= 360.0F;
		}

		if (!this.entity.getNavigator().noPath()) {
			if (yawOffsetDiff < -75.0F) {
				this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
			}

			if (yawOffsetDiff > 75.0F) {
				this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
			}
		}
	}

	private float updateRotation(float current, float target, float maxChange) {
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

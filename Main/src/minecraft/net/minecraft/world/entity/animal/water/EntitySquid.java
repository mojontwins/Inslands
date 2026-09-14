package net.minecraft.world.entity.animal.water;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.animal.EntityWaterMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.material.Material;

public class EntitySquid extends EntityWaterMob {
	public float squidPitch = 0.0F;
	public float prevSquidPitch = 0.0F;
	public float squidYaw = 0.0F;
	public float prevSquidYaw = 0.0F;
	public float squidRotation = 0.0F;
	public float prevSquidRotation = 0.0F;
	public float tentacleAngle = 0.0F;
	public float lastTentacleAngle = 0.0F;
	private float randomMotionSpeed = 0.0F;
	private float randomRotationSpeed = 0.0F;
	private float yawMotion = 0.0F;
	private float randomMotionVecX = 0.0F;
	private float randomMotionVecY = 0.0F;
	private float randomMotionVecZ = 0.0F;

	public EntitySquid(World world) {
		super(world);
		this.texture = "/mob/squid.png";
		this.setSize(0.95F, 0.95F);
		this.randomRotationSpeed = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return null;
	}

	protected String getDeathSound() {
		return null;
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		return 0;
	}

	protected void dropFewItems() {
		int count = this.rand.nextInt(3) + 1;

		for (int i = 0; i < count; ++i) {
			this.entityDropItem(new ItemStack(Item.dyePowder, 1, 0), 0.0F);
		}
	}

	public boolean isInWater() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.prevSquidPitch = this.squidPitch;
		this.prevSquidYaw = this.squidYaw;
		this.prevSquidRotation = this.squidRotation;
		this.lastTentacleAngle = this.tentacleAngle;
		this.squidRotation += this.randomRotationSpeed;
		if (this.squidRotation > 6.2831855F) {
			this.squidRotation -= 6.2831855F;
			if (this.rand.nextInt(10) == 0) {
				this.randomRotationSpeed = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
			}
		}

		if (this.isInWater()) {
			float progress;
			if (this.squidRotation < (float) Math.PI) {
				progress = this.squidRotation / (float) Math.PI;
				this.tentacleAngle = MathHelper.sin(progress * progress * (float) Math.PI) * (float) Math.PI * 0.25F;
				if ((double) progress > 0.75D) {
					this.randomMotionSpeed = 1.0F;
					this.yawMotion = 1.0F;
				} else {
					this.yawMotion *= 0.8F;
				}
			} else {
				this.tentacleAngle = 0.0F;
				this.randomMotionSpeed *= 0.9F;
				this.yawMotion *= 0.99F;
			}

			if (!this.isMultiplayerEntity) {
				this.motionX = (double) (this.randomMotionVecX * this.randomMotionSpeed);
				this.motionY = (double) (this.randomMotionVecY * this.randomMotionSpeed);
				this.motionZ = (double) (this.randomMotionVecZ * this.randomMotionSpeed);
			}

			progress = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.renderYawOffset += (-((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float) Math.PI - this.renderYawOffset) * 0.1F;
			this.rotationYaw = this.renderYawOffset;
			this.squidYaw += (float) Math.PI * this.yawMotion * 1.5F;
			this.squidPitch += (-((float) Math.atan2((double) progress, this.motionY)) * 180.0F / (float) Math.PI - this.squidPitch) * 0.1F;
		} else {
			this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float) Math.PI * 0.25F;
			if (!this.isMultiplayerEntity) {
				this.motionX = 0.0D;
				this.motionY -= 0.08D;
				this.motionY *= (double) 0.98F;
				this.motionZ = 0.0D;
			}

			this.squidPitch = (float) ((double) this.squidPitch + (double) (-90.0F - this.squidPitch) * 0.02D);
		}
	}

	public void moveEntityWithHeading(float strafe, float forward) {
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

	protected void updateEntityActionState() {
		if (this.rand.nextInt(50) == 0 || !this.inWater || this.randomMotionVecX == 0.0F && this.randomMotionVecY == 0.0F && this.randomMotionVecZ == 0.0F) {
			float angle = this.rand.nextFloat() * (float) Math.PI * 2.0F;
			this.randomMotionVecX = MathHelper.cos(angle) * 0.2F;
			this.randomMotionVecY = -0.1F + this.rand.nextFloat() * 0.2F;
			this.randomMotionVecZ = MathHelper.sin(angle) * 0.2F;
		}

		this.despawnEntity();
	}
}

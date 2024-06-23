package com.benimatic.twilightforest;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFlying;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class EntityTFWraith extends EntityFlying {
	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity;
	private int aggroCooldown;
	public int prevAttackCounter;
	public int attackCounter;
	int attackStrength;

	public EntityTFWraith(World world) {
		super(world);
		this.texture = "/mob/ghost-crown.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
	}

	public EntityTFWraith(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	public int getFullHealth() {
		return 20;
	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime()) {
			float f = this.getEntityBrightness(1.0F);
			if(f > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
				;
			}
		}

		super.onLivingUpdate();
	}

	public boolean canTriggerWalking() {
		return false;
	}

	protected void updateEntityActionState() {
		if(!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

		this.despawnEntity();
		this.prevAttackCounter = this.attackCounter;
		double d = this.waypointX - this.posX;
		double d1 = this.waypointY - this.posY;
		double d2 = this.waypointZ - this.posZ;
		double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
		if(d3 < 1.0D || d3 > 60.0D) {
			this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if(this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			if(this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3)) {
				this.motionX += d / d3 * 0.1D;
				this.motionY += d1 / d3 * 0.1D;
				this.motionZ += d2 / d3 * 0.1D;
			} else {
				this.waypointX = this.posX;
				this.waypointY = this.posY;
				this.waypointZ = this.posZ;
				this.targetedEntity = null;
			}
		}

		if(this.targetedEntity != null && this.targetedEntity.isDead) {
			this.targetedEntity = null;
		}

		if(this.targetedEntity != null && this.aggroCooldown-- > 0) {
			float d4 = this.targetedEntity.getDistanceToEntity(this);
			if(this.canEntityBeSeen(this.targetedEntity)) {
				this.attackEntity(this.targetedEntity, d4);
			} else {
				this.attackBlockedEntity(this.targetedEntity, d4);
			}
		} else {
			this.targetedEntity = this.findPlayerToAttack();
			if(this.targetedEntity != null) {
				this.aggroCooldown = 20;
			}
		}

		double d17 = 64.0D;
		if(this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < d17 * d17) {
			double d5 = this.targetedEntity.posX - this.posX;
			double d7 = this.targetedEntity.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / 3.141593F;
			if(this.canEntityBeSeen(this.targetedEntity)) {
				if(this.attackCounter == 10) {
					;
				}

				++this.attackCounter;
				if(this.attackCounter == 20) {
					this.waypointX = this.targetedEntity.posX;
					this.waypointY = this.targetedEntity.posY - (double)this.targetedEntity.height + 0.5D;
					this.waypointZ = this.targetedEntity.posZ;
					this.attackCounter = -40;
				}
			} else if(this.attackCounter > 0) {
				--this.attackCounter;
			}
		} else {
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.141593F;
			if(this.attackCounter > 0) {
				--this.attackCounter;
			}
		}

	}

	protected void attackEntity(Entity entity, float f) {
		if(this.attackTime <= 0 && f < 2.0F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			entity.attackEntityFrom(this, this.attackStrength);
		}

	}

	protected void attackBlockedEntity(Entity entity, float f) {
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(super.attackEntityFrom(entity, i)) {
			if(this.riddenByEntity != entity && this.ridingEntity != entity) {
				if(entity != this) {
					this.targetedEntity = entity;
				}

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	protected Entity findPlayerToAttack() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
	}

	private boolean isCourseTraversable(double d, double d1, double d2, double d3) {
		double d4 = (this.waypointX - this.posX) / d3;
		double d5 = (this.waypointY - this.posY) / d3;
		double d6 = (this.waypointZ - this.posZ) / d3;
		AxisAlignedBB axisalignedbb = this.boundingBox.copy();

		for(int i = 1; (double)i < d3; ++i) {
			axisalignedbb.offset(d4, d5, d6);
			if(this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).size() > 0) {
				return false;
			}
		}

		return true;
	}

	protected String getLivingSound() {
		return "mob.wraith";
	}

	protected String getHurtSound() {
		return "mob.wraith";
	}

	protected String getDeathSound() {
		return "mob.wraith";
	}

	protected int getDropItemId() {
		return Item.lightStoneDust.shiftedIndex;
	}
}

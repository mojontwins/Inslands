package net.minecraft.src;

public class EntityFlyingCow extends EntityAetherAnimal {
	public boolean getSaddled = false;
	public float wingFold;
	public float wingAngle;
	private float aimingForFold;
	public int jumps;
	public int jrem;
	private boolean jpress;
	private int ticks;

	public EntityFlyingCow(World world) {
		super(world);
		this.texture = "/aether/mobs/Mob_FlyingCowBase.png";
		this.setSize(0.9F, 1.3F);
		this.jrem = 0;
		this.jumps = 1;
		this.ticks = 0;
		this.stepHeight = 1.0F;
		this.ignoreFrustumCheck = true;
	}

	protected boolean canDespawn() {
		return !this.getSaddled;
	}

	protected boolean canTriggerWalking() {
		return this.onGround;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(16, (byte)0);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("Jumps", (short)this.jumps);
		nbttagcompound.setShort("Remaining", (short)this.jrem);
		nbttagcompound.setBoolean("getSaddled", this.getSaddled);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.jumps = nbttagcompound.getShort("Jumps");
		this.jrem = nbttagcompound.getShort("Remaining");
		this.getSaddled = nbttagcompound.getBoolean("getSaddled");
		if(this.getSaddled) {
			this.texture = "/aether/mobs/Mob_FlyingCowSaddle.png";
		}

	}

	protected void jump() {
		this.motionY = 0.6D;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.onGround) {
			this.wingAngle *= 0.8F;
			this.aimingForFold = 0.1F;
			this.jpress = false;
			this.jrem = this.jumps;
		} else {
			this.aimingForFold = 1.0F;
		}

		++this.ticks;
		this.wingAngle = this.wingFold * (float)Math.sin((double)((float)this.ticks / 31.830988F));
		this.wingFold += (this.aimingForFold - this.wingFold) / 5.0F;
		this.fallDistance = 0.0F;
		if(this.motionY < -0.2D) {
			this.motionY = -0.2D;
		}

	}

	public void updatePlayerActionState() {
		if(!this.worldObj.multiplayerWorld) {
			if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityLiving) {
				this.moveForward = 0.0F;
				this.moveStrafing = 0.0F;
				this.isJumping = false;
				this.riddenByEntity.fallDistance = 0.0F;
				this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
				this.prevRotationPitch = this.rotationPitch = this.riddenByEntity.rotationPitch;
				EntityLiving entityliving = (EntityLiving)this.riddenByEntity;
				float f = 3.141593F;
				float f1 = f / 180.0F;
				float d;
				if(entityliving.moveForward > 0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveForward * -Math.sin((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveForward * Math.cos((double)d) * 0.17499999701976776D;
				} else if(entityliving.moveForward < -0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveForward * -Math.sin((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveForward * Math.cos((double)d) * 0.17499999701976776D;
				}

				if(entityliving.moveStrafing > 0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveStrafing * Math.cos((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)d) * 0.17499999701976776D;
				} else if(entityliving.moveStrafing < -0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveStrafing * Math.cos((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)d) * 0.17499999701976776D;
				}

				if(this.onGround && entityliving.isJumping) {
					this.onGround = false;
					this.motionY = 1.4D;
					this.jpress = true;
					--this.jrem;
				} else if(this.handleWaterMovement() && entityliving.isJumping) {
					this.motionY = 0.5D;
					this.jpress = true;
					--this.jrem;
				} else if(this.jrem > 0 && !this.jpress && entityliving.isJumping) {
					this.motionY = 1.2D;
					this.jpress = true;
					--this.jrem;
				}

				if(this.jpress && !entityliving.isJumping) {
					this.jpress = false;
				}

				double d8 = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));
				if(d8 > 0.375D) {
					double d1 = 0.375D / d8;
					this.motionX *= d1;
					this.motionZ *= d1;
				}

			} else {
				super.updatePlayerActionState();
			}
		}
	}

	protected String getLivingSound() {
		return "mob.cow";
	}

	protected String getHurtSound() {
		return "mob.cowhurt";
	}

	protected String getDeathSound() {
		return "mob.cowhurt";
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	public boolean interact(EntityPlayer entityplayer) {
		if(!this.getSaddled && entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Item.saddle.shiftedIndex) {
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
			this.getSaddled = true;
			this.texture = "/aether/mobs/Mob_FlyingCowSaddle.png";
			return true;
		} else if(!this.getSaddled || this.worldObj.multiplayerWorld || this.riddenByEntity != null && this.riddenByEntity != entityplayer) {
			return false;
		} else {
			entityplayer.mountEntity(this);
			return true;
		}
	}

	protected void dropFewItems() {
		if(mod_Aether.equippedSkyrootSword()) {
			this.dropItem(Item.leather.shiftedIndex, 4);
		} else {
			this.dropItem(Item.leather.shiftedIndex, 2);
		}

	}
}

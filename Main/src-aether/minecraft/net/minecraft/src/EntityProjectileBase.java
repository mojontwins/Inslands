package net.minecraft.src;

import java.util.List;

public abstract class EntityProjectileBase extends Entity {
	public float speed;
	public float slowdown;
	public float curvature;
	public float precision;
	public float hitBox;
	public int dmg;
	public ItemStack item;
	public int ttlInGround;
	public int xTile;
	public int yTile;
	public int zTile;
	public int inTile;
	public int inData;
	public boolean inGround;
	public int arrowShake;
	public EntityLiving shooter;
	public int ticksInGround;
	public int ticksFlying;
	public boolean shotByPlayer;

	public EntityProjectileBase(World world) {
		super(world);
	}

	public EntityProjectileBase(World world, double d, double d1, double d2) {
		this(world);
		this.setPositionAndRotation(d, d1, d2, this.rotationYaw, this.rotationPitch);
	}

	public EntityProjectileBase(World world, EntityLiving entityliving) {
		this(world);
		this.shooter = entityliving;
		this.shotByPlayer = entityliving instanceof EntityPlayer;
		this.setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * 0.16F);
		this.posY -= (double)0.1F;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * 0.16F);
		this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F));
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, this.speed, this.precision);
	}

	protected void entityInit() {
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.inTile = 0;
		this.inGround = false;
		this.arrowShake = 0;
		this.ticksFlying = 0;
		this.setSize(0.5F, 0.5F);
		this.yOffset = 0.0F;
		this.hitBox = 0.3F;
		this.speed = 1.0F;
		this.slowdown = 0.99F;
		this.curvature = 0.03F;
		this.dmg = 4;
		this.precision = 1.0F;
		this.ttlInGround = 1200;
		this.item = null;
	}

	public void setEntityDead() {
		this.shooter = null;
		super.setEntityDead();
	}

	public void setArrowHeading(double d, double d1, double d2, float f, float f1) {
		float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
		d /= (double)f2;
		d1 /= (double)f2;
		d2 /= (double)f2;
		d += this.rand.nextGaussian() * (double)0.0075F * (double)f1;
		d1 += this.rand.nextGaussian() * (double)0.0075F * (double)f1;
		d2 += this.rand.nextGaussian() * (double)0.0075F * (double)f1;
		d *= (double)f;
		d1 *= (double)f;
		d2 *= (double)f;
		this.motionX = d;
		this.motionY = d1;
		this.motionZ = d2;
		float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / (double)(float)Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, (double)f3) * 180.0D / (double)(float)Math.PI);
		this.ticksInGround = 0;
	}

	public void setVelocity(double d, double d1, double d2) {
		this.motionX = d;
		this.motionY = d1;
		this.motionZ = d2;
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(d * d + d2 * d2);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0D / (double)(float)Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, (double)f) * 180.0D / (double)(float)Math.PI);
		}

	}

	public void onUpdate() {
		super.onUpdate();
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float vec3d = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)vec3d) * 180.0D / (double)(float)Math.PI);
		}

		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inGround) {
			int i15 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			int vec3d1 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
			if(i15 == this.inTile && vec3d1 == this.inData) {
				++this.ticksInGround;
				this.tickInGround();
				if(this.ticksInGround == this.ttlInGround) {
					this.setEntityDead();
				}

				return;
			}

			this.inGround = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
			this.ticksInGround = 0;
			this.ticksFlying = 0;
		} else {
			++this.ticksFlying;
		}

		this.tickFlying();
		Vec3D vec3D16 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		Vec3D vec3D17 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3D16, vec3D17);
		vec3D16 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		vec3D17 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(movingobjectposition != null) {
			vec3D17 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}

		Entity entity = null;
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d = 0.0D;

		for(int f2 = 0; f2 < list.size(); ++f2) {
			Entity f1 = (Entity)list.get(f2);
			if(this.canBeShot(f1)) {
				float f4 = this.hitBox;
				AxisAlignedBB axisalignedbb = f1.boundingBox.expand((double)f4, (double)f4, (double)f4);
				MovingObjectPosition movingobjectposition1 = axisalignedbb.func_1169_a(vec3D16, vec3D17);
				if(movingobjectposition1 != null) {
					double d1 = vec3D16.distanceTo(movingobjectposition1.hitVec);
					if(d1 < d || d == 0.0D) {
						entity = f1;
						d = d1;
					}
				}
			}
		}

		if(entity != null) {
			movingobjectposition = new MovingObjectPosition(entity);
		}

		if(movingobjectposition != null && this.onHit()) {
			Entity entity18 = movingobjectposition.entityHit;
			if(entity18 != null) {
				if(this.onHitTarget(entity18)) {
					if(entity18 instanceof EntityLiving && !(entity18 instanceof EntityPlayer)) {
						((EntityLiving)entity18).field_9346_af = 0;
					}

					entity18.attackEntityFrom(this.shooter, this.dmg);
					this.setEntityDead();
				}
			} else {
				this.xTile = movingobjectposition.blockX;
				this.yTile = movingobjectposition.blockY;
				this.zTile = movingobjectposition.blockZ;
				this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
				this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
				if(this.onHitBlock(movingobjectposition)) {
					this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
					this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
					this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
					float f20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / (double)f20 * (double)0.05F;
					this.posY -= this.motionY / (double)f20 * (double)0.05F;
					this.posZ -= this.motionZ / (double)f20 * (double)0.05F;
					this.inGround = true;
					this.arrowShake = 7;
				} else {
					this.inTile = 0;
					this.inData = 0;
				}
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		this.handleMotionUpdate();
		float f19 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f19) * 180.0D / (double)(float)Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
	}

	public void handleMotionUpdate() {
		float slow = this.slowdown;
		if(this.handleWaterMovement()) {
			for(int k = 0; k < 4; ++k) {
				float f6 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f6, this.posY - this.motionY * (double)f6, this.posZ - this.motionZ * (double)f6, this.motionX, this.motionY, this.motionZ);
			}

			slow *= 0.8F;
		}

		this.motionX *= (double)slow;
		this.motionY *= (double)slow;
		this.motionZ *= (double)slow;
		this.motionY -= (double)this.curvature;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short)this.xTile);
		nbttagcompound.setShort("yTile", (short)this.yTile);
		nbttagcompound.setShort("zTile", (short)this.zTile);
		nbttagcompound.setByte("inTile", (byte)this.inTile);
		nbttagcompound.setByte("inData", (byte)this.inData);
		nbttagcompound.setByte("shake", (byte)this.arrowShake);
		nbttagcompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		nbttagcompound.setBoolean("player", this.shotByPlayer);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.xTile = nbttagcompound.getShort("xTile");
		this.yTile = nbttagcompound.getShort("yTile");
		this.zTile = nbttagcompound.getShort("zTile");
		this.inTile = nbttagcompound.getByte("inTile") & 255;
		this.inData = nbttagcompound.getByte("inData") & 255;
		this.arrowShake = nbttagcompound.getByte("shake") & 255;
		this.inGround = nbttagcompound.getByte("inGround") == 1;
		this.shotByPlayer = nbttagcompound.getBoolean("player");
	}

	public void onCollideWithPlayer(EntityPlayer entityplayer) {
		if(this.item != null) {
			if(!this.worldObj.multiplayerWorld) {
				if(this.inGround && this.shotByPlayer && this.arrowShake <= 0 && entityplayer.inventory.addItemStackToInventory(this.item.copy())) {
					this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
					entityplayer.onItemPickup(this, 1);
					this.setEntityDead();
				}

			}
		}
	}

	public boolean canBeShot(Entity ent) {
		return ent.canBeCollidedWith() && (ent != this.shooter || this.ticksFlying >= 5) && (!(ent instanceof EntityLiving) || ((EntityLiving)ent).deathTime <= 0);
	}

	public boolean onHit() {
		return true;
	}

	public boolean onHitTarget(Entity target) {
		this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		return true;
	}

	public void tickFlying() {
	}

	public void tickInGround() {
	}

	public boolean onHitBlock(MovingObjectPosition mop) {
		return this.onHitBlock();
	}

	public boolean onHitBlock() {
		this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		return true;
	}

	public float getShadowSize() {
		return 0.0F;
	}
}

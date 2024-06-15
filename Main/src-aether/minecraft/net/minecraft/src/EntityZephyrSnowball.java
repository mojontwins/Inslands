package net.minecraft.src;

import java.util.List;

public class EntityZephyrSnowball extends Entity {
	private int field_9402_e = -1;
	private int field_9401_f = -1;
	private int field_9400_g = -1;
	private int field_9399_h = 0;
	private boolean field_9398_i = false;
	public int field_9406_a = 0;
	private EntityLiving field_9397_j;
	private int field_9396_k;
	private int field_9395_l = 0;
	public double field_9405_b;
	public double field_9404_c;
	public double field_9403_d;

	public EntityZephyrSnowball(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
	}

	protected void entityInit() {
	}

	public boolean isInRangeToRenderDist(double d) {
		double d1 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		d1 *= 64.0D;
		return d < d1 * d1;
	}

	public EntityZephyrSnowball(World world, EntityLiving entityliving, double d, double d1, double d2) {
		super(world);
		this.field_9397_j = entityliving;
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		this.yOffset = 0.0F;
		this.motionX = this.motionY = this.motionZ = 0.0D;
		d += this.rand.nextGaussian() * 0.4D;
		d1 += this.rand.nextGaussian() * 0.4D;
		d2 += this.rand.nextGaussian() * 0.4D;
		double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
		this.field_9405_b = d / d3 * 0.1D;
		this.field_9404_c = d1 / d3 * 0.1D;
		this.field_9403_d = d2 / d3 * 0.1D;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.field_9406_a > 0) {
			--this.field_9406_a;
		}

		if(this.field_9398_i) {
			int vec3d = this.worldObj.getBlockId(this.field_9402_e, this.field_9401_f, this.field_9400_g);
			if(vec3d == this.field_9399_h) {
				++this.field_9396_k;
				if(this.field_9396_k == 1200) {
					this.setEntityDead();
				}

				return;
			}

			this.field_9398_i = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
			this.field_9396_k = 0;
			this.field_9395_l = 0;
		} else {
			++this.field_9395_l;
		}

		Vec3D vec3D15 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		Vec3D vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3D15, vec3d1);
		vec3D15 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(movingobjectposition != null) {
			vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}

		Entity entity = null;
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d = 0.0D;

		for(int f = 0; f < list.size(); ++f) {
			Entity f1 = (Entity)list.get(f);
			if(f1.canBeCollidedWith() && (f1 != this.field_9397_j || this.field_9395_l >= 25)) {
				float k = 0.3F;
				AxisAlignedBB f3 = f1.boundingBox.expand((double)k, (double)k, (double)k);
				MovingObjectPosition movingobjectposition1 = f3.func_1169_a(vec3D15, vec3d1);
				if(movingobjectposition1 != null) {
					double d1 = vec3D15.distanceTo(movingobjectposition1.hitVec);
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

		if(movingobjectposition != null) {
			if(movingobjectposition.entityHit != null) {
				if(!movingobjectposition.entityHit.attackEntityFrom(this.field_9397_j, 0)) {
					;
				}

				movingobjectposition.entityHit.motionX += this.motionX;
				movingobjectposition.entityHit.motionY += 0.2D;
				movingobjectposition.entityHit.motionZ += this.motionZ;
			}

			this.setEntityDead();
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f16) * 180.0D / (double)(float)Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
		float f17 = 0.95F;
		if(this.handleWaterMovement()) {
			for(int i18 = 0; i18 < 4; ++i18) {
				float f19 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f19, this.posY - this.motionY * (double)f19, this.posZ - this.motionZ * (double)f19, this.motionX, this.motionY, this.motionZ);
			}

			f17 = 0.8F;
		}

		this.motionX += this.field_9405_b;
		this.motionY += this.field_9404_c;
		this.motionZ += this.field_9403_d;
		this.motionX *= (double)f17;
		this.motionY *= (double)f17;
		this.motionZ *= (double)f17;
		this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		this.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short)this.field_9402_e);
		nbttagcompound.setShort("yTile", (short)this.field_9401_f);
		nbttagcompound.setShort("zTile", (short)this.field_9400_g);
		nbttagcompound.setByte("inTile", (byte)this.field_9399_h);
		nbttagcompound.setByte("shake", (byte)this.field_9406_a);
		nbttagcompound.setByte("inGround", (byte)(this.field_9398_i ? 1 : 0));
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.field_9402_e = nbttagcompound.getShort("xTile");
		this.field_9401_f = nbttagcompound.getShort("yTile");
		this.field_9400_g = nbttagcompound.getShort("zTile");
		this.field_9399_h = nbttagcompound.getByte("inTile") & 255;
		this.field_9406_a = nbttagcompound.getByte("shake") & 255;
		this.field_9398_i = nbttagcompound.getByte("inGround") == 1;
	}

	public float getCollisionBorderSize() {
		return 1.0F;
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		this.setBeenAttacked();
		if(entity != null) {
			Vec3D vec3d = entity.getLookVec();
			if(vec3d != null) {
				this.motionX = vec3d.xCoord;
				this.motionY = vec3d.yCoord;
				this.motionZ = vec3d.zCoord;
				this.field_9405_b = this.motionX * 0.1D;
				this.field_9404_c = this.motionY * 0.1D;
				this.field_9403_d = this.motionZ * 0.1D;
			}

			return true;
		} else {
			return false;
		}
	}

	public float getShadowSize() {
		return 0.0F;
	}
}

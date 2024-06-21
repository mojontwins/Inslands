package net.minecraft.src;

public class EntityEnderEye extends Entity {
	public int field_40096_a = 0;
	private double field_40094_b;
	private double field_40095_c;
	private double field_40091_d;
	private int despawnTimer;
	private boolean shatterOrDrop;

	public EntityEnderEye(World world1) {
		super(world1);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit() {
	}

	public boolean isInRangeToRenderDist(double d1) {
		double d3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		d3 *= 64.0D;
		return d1 < d3 * d3;
	}

	public EntityEnderEye(World world1, double d2, double d4, double d6) {
		super(world1);
		this.despawnTimer = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(d2, d4, d6);
		this.yOffset = 0.0F;
	}

	public void func_40090_a(double d1, int i3, double d4) {
		double d6 = d1 - this.posX;
		double d8 = d4 - this.posZ;
		float f10 = MathHelper.sqrt_double(d6 * d6 + d8 * d8);
		if(f10 > 12.0F) {
			this.field_40094_b = this.posX + d6 / (double)f10 * 12.0D;
			this.field_40091_d = this.posZ + d8 / (double)f10 * 12.0D;
			this.field_40095_c = this.posY + 8.0D;
		} else {
			this.field_40094_b = d1;
			this.field_40095_c = (double)i3;
			this.field_40091_d = d4;
		}

		this.despawnTimer = 0;
		this.shatterOrDrop = this.rand.nextInt(5) > 0;
	}

	public void setVelocity(double d1, double d3, double d5) {
		this.motionX = d1;
		this.motionY = d3;
		this.motionZ = d5;
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f7 = MathHelper.sqrt_double(d1 * d1 + d5 * d5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d1, d5) * 180.0D / (double)(float)Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d3, (double)f7) * 180.0D / (double)(float)Math.PI);
		}

	}

	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f1) * 180.0D / (double)(float)Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
		if(!this.worldObj.isRemote) {
			double d2 = this.field_40094_b - this.posX;
			double d4 = this.field_40091_d - this.posZ;
			float f6 = (float)Math.sqrt(d2 * d2 + d4 * d4);
			float f7 = (float)Math.atan2(d4, d2);
			double d8 = (double)f1 + (double)(f6 - f1) * 0.0025D;
			if(f6 < 1.0F) {
				d8 *= 0.8D;
				this.motionY *= 0.8D;
			}

			this.motionX = Math.cos((double)f7) * d8;
			this.motionZ = Math.sin((double)f7) * d8;
			if(this.posY < this.field_40095_c) {
				this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
			} else {
				this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
			}
		}

		float f10 = 0.25F;
		if(this.isInWater()) {
			for(int i3 = 0; i3 < 4; ++i3) {
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f10, this.posY - this.motionY * (double)f10, this.posZ - this.motionZ * (double)f10, this.motionX, this.motionY, this.motionZ);
			}
		} else {
			this.worldObj.spawnParticle("portal", this.posX - this.motionX * (double)f10 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * (double)f10 - 0.5D, this.posZ - this.motionZ * (double)f10 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ);
		}

		if(!this.worldObj.isRemote) {
			this.setPosition(this.posX, this.posY, this.posZ);
			++this.despawnTimer;
			if(this.despawnTimer > 80 && !this.worldObj.isRemote) {
				this.setDead();
				if(this.shatterOrDrop) {
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.eyeOfEnder)));
				} else {
					this.worldObj.playAuxSFX(2003, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 0);
				}
			}
		}

	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer1) {
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public float getBrightness(float f1) {
		return 1.0F;
	}

	public int getBrightnessForRender(float f1) {
		return 15728880;
	}

	public boolean canAttackWithItem() {
		return false;
	}
}

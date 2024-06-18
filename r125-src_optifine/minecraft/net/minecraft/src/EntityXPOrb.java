package net.minecraft.src;

public class EntityXPOrb extends Entity {
	public int xpColor;
	public int xpOrbAge = 0;
	public int field_35126_c;
	private int xpOrbHealth = 5;
	private int xpValue;

	public EntityXPOrb(World world1, double d2, double d4, double d6, int i8) {
		super(world1);
		this.setSize(0.5F, 0.5F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(d2, d4, d6);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * (double)0.2F - (double)0.1F) * 2.0F);
		this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
		this.motionZ = (double)((float)(Math.random() * (double)0.2F - (double)0.1F) * 2.0F);
		this.xpValue = i8;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public EntityXPOrb(World world1) {
		super(world1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
	}

	protected void entityInit() {
	}

	public int getBrightnessForRender(float f1) {
		float f2 = 0.5F;
		if(f2 < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		int i3 = super.getBrightnessForRender(f1);
		int i4 = i3 & 255;
		int i5 = i3 >> 16 & 255;
		i4 += (int)(f2 * 15.0F * 16.0F);
		if(i4 > 240) {
			i4 = 240;
		}

		return i4 | i5 << 16;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.field_35126_c > 0) {
			--this.field_35126_c;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.03F;
		if(this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava) {
			this.motionY = (double)0.2F;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		double d1 = 8.0D;
		EntityPlayer entityPlayer3 = this.worldObj.getClosestPlayerToEntity(this, d1);
		if(entityPlayer3 != null) {
			double d4 = (entityPlayer3.posX - this.posX) / d1;
			double d6 = (entityPlayer3.posY + (double)entityPlayer3.getEyeHeight() - this.posY) / d1;
			double d8 = (entityPlayer3.posZ - this.posZ) / d1;
			double d10 = Math.sqrt(d4 * d4 + d6 * d6 + d8 * d8);
			double d12 = 1.0D - d10;
			if(d12 > 0.0D) {
				d12 *= d12;
				this.motionX += d4 / d10 * d12 * 0.1D;
				this.motionY += d6 / d10 * d12 * 0.1D;
				this.motionZ += d8 / d10 * d12 * 0.1D;
			}
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float f14 = 0.98F;
		if(this.onGround) {
			f14 = 0.58800006F;
			int i5 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
			if(i5 > 0) {
				f14 = Block.blocksList[i5].slipperiness * 0.98F;
			}
		}

		this.motionX *= (double)f14;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)f14;
		if(this.onGround) {
			this.motionY *= -0.8999999761581421D;
		}

		++this.xpColor;
		++this.xpOrbAge;
		if(this.xpOrbAge >= 6000) {
			this.setDead();
		}

	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
	}

	protected void dealFireDamage(int i1) {
		this.attackEntityFrom(DamageSource.inFire, i1);
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		this.setBeenAttacked();
		this.xpOrbHealth -= i2;
		if(this.xpOrbHealth <= 0) {
			this.setDead();
		}

		return false;
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("Health", (short)((byte)this.xpOrbHealth));
		nBTTagCompound1.setShort("Age", (short)this.xpOrbAge);
		nBTTagCompound1.setShort("Value", (short)this.xpValue);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.xpOrbHealth = nBTTagCompound1.getShort("Health") & 255;
		this.xpOrbAge = nBTTagCompound1.getShort("Age");
		this.xpValue = nBTTagCompound1.getShort("Value");
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer1) {
		if(!this.worldObj.isRemote) {
			if(this.field_35126_c == 0 && entityPlayer1.xpCooldown == 0) {
				entityPlayer1.xpCooldown = 2;
				this.worldObj.playSoundAtEntity(this, "random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
				entityPlayer1.onItemPickup(this, 1);
				entityPlayer1.addExperience(this.xpValue);
				this.setDead();
			}

		}
	}

	public int getXpValue() {
		return this.xpValue;
	}

	public int getTextureByXP() {
		return this.xpValue >= 2477 ? 10 : (this.xpValue >= 1237 ? 9 : (this.xpValue >= 617 ? 8 : (this.xpValue >= 307 ? 7 : (this.xpValue >= 149 ? 6 : (this.xpValue >= 73 ? 5 : (this.xpValue >= 37 ? 4 : (this.xpValue >= 17 ? 3 : (this.xpValue >= 7 ? 2 : (this.xpValue >= 3 ? 1 : 0)))))))));
	}

	public static int getXPSplit(int i0) {
		return i0 >= 2477 ? 2477 : (i0 >= 1237 ? 1237 : (i0 >= 617 ? 617 : (i0 >= 307 ? 307 : (i0 >= 149 ? 149 : (i0 >= 73 ? 73 : (i0 >= 37 ? 37 : (i0 >= 17 ? 17 : (i0 >= 7 ? 7 : (i0 >= 3 ? 3 : 1)))))))));
	}

	public boolean canAttackWithItem() {
		return false;
	}
}

package net.minecraft.src;

public class EntityBlaze extends EntityMob {
	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;
	private int field_40152_d;

	public EntityBlaze(World world1) {
		super(world1);
		this.texture = "/mob/fire.png";
		this.isImmuneToFire = true;
		this.attackStrength = 6;
		this.experienceValue = 10;
	}

	public int getMaxHealth() {
		return 20;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}

	protected String getLivingSound() {
		return "mob.blaze.breathe";
	}

	protected String getHurtSound() {
		return "mob.blaze.hit";
	}

	protected String getDeathSound() {
		return "mob.blaze.death";
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		return super.attackEntityFrom(damageSource1, i2);
	}

	public void onDeath(DamageSource damageSource1) {
		super.onDeath(damageSource1);
	}

	public int getBrightnessForRender(float f1) {
		return 15728880;
	}

	public float getBrightness(float f1) {
		return 1.0F;
	}

	public void onLivingUpdate() {
		if(!this.worldObj.isRemote) {
			if(this.isWet()) {
				this.attackEntityFrom(DamageSource.drown, 1);
			}

			--this.heightOffsetUpdateTime;
			if(this.heightOffsetUpdateTime <= 0) {
				this.heightOffsetUpdateTime = 100;
				this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
			}

			if(this.getEntityToAttack() != null && this.getEntityToAttack().posY + (double)this.getEntityToAttack().getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset) {
				this.motionY += ((double)0.3F - this.motionY) * (double)0.3F;
			}
		}

		if(this.rand.nextInt(24) == 0) {
			this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F);
		}

		if(!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		for(int i1 = 0; i1 < 2; ++i1) {
			this.worldObj.spawnParticle("largesmoke", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
		}

		super.onLivingUpdate();
	}

	protected void attackEntity(Entity entity1, float f2) {
		if(this.attackTime <= 0 && f2 < 2.0F && entity1.boundingBox.maxY > this.boundingBox.minY && entity1.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.attackEntityAsMob(entity1);
		} else if(f2 < 30.0F) {
			double d3 = entity1.posX - this.posX;
			double d5 = entity1.boundingBox.minY + (double)(entity1.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
			double d7 = entity1.posZ - this.posZ;
			if(this.attackTime == 0) {
				++this.field_40152_d;
				if(this.field_40152_d == 1) {
					this.attackTime = 60;
					this.func_40150_a(true);
				} else if(this.field_40152_d <= 4) {
					this.attackTime = 6;
				} else {
					this.attackTime = 100;
					this.field_40152_d = 0;
					this.func_40150_a(false);
				}

				if(this.field_40152_d > 1) {
					float f9 = MathHelper.sqrt_float(f2) * 0.5F;
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

					for(int i10 = 0; i10 < 1; ++i10) {
						EntitySmallFireball entitySmallFireball11 = new EntitySmallFireball(this.worldObj, this, d3 + this.rand.nextGaussian() * (double)f9, d5, d7 + this.rand.nextGaussian() * (double)f9);
						entitySmallFireball11.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
						this.worldObj.spawnEntityInWorld(entitySmallFireball11);
					}
				}
			}

			this.rotationYaw = (float)(Math.atan2(d7, d3) * 180.0D / (double)(float)Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

	}

	protected void fall(float f1) {
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	protected int getDropItemId() {
		return Item.blazeRod.shiftedIndex;
	}

	public boolean isBurning() {
		return this.func_40151_ac();
	}

	protected void dropFewItems(boolean z1, int i2) {
		if(z1) {
			int i3 = this.rand.nextInt(2 + i2);

			for(int i4 = 0; i4 < i3; ++i4) {
				this.dropItem(Item.blazeRod.shiftedIndex, 1);
			}
		}

	}

	public boolean func_40151_ac() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_40150_a(boolean z1) {
		byte b2 = this.dataWatcher.getWatchableObjectByte(16);
		if(z1) {
			b2 = (byte)(b2 | 1);
		} else {
			b2 &= -2;
		}

		this.dataWatcher.updateObject(16, b2);
	}

	protected boolean isValidLightLevel() {
		return true;
	}
}

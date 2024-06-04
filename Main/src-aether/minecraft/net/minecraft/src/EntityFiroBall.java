package net.minecraft.src;

public class EntityFiroBall extends EntityFlying {
	public float[] sinage;
	public double smotionX;
	public double smotionY;
	public double smotionZ;
	public int life;
	public int lifeSpan;
	public boolean frosty;
	public boolean smacked;
	public boolean fromCloud;
	private static final double topSpeed = 0.125D;
	private static final float sponge = 57.295773F;

	public EntityFiroBall(World world) {
		super(world);
		this.texture = "/aether/mobs/firoball.png";
		this.lifeSpan = 300;
		this.life = this.lifeSpan;
		this.setSize(0.9F, 0.9F);
		this.sinage = new float[3];
		this.isImmuneToFire = true;

		for(int i = 0; i < 3; ++i) {
			this.sinage[i] = this.rand.nextFloat() * 6.0F;
		}

	}

	public EntityFiroBall(World world, double x, double y, double z, boolean flag) {
		super(world);
		this.texture = "/aether/mobs/firoball.png";
		this.lifeSpan = 300;
		this.life = this.lifeSpan;
		this.setSize(0.9F, 0.9F);
		this.setPositionAndRotation(x, y, z, this.rotationYaw, this.rotationPitch);
		this.sinage = new float[3];
		this.isImmuneToFire = true;

		for(int i = 0; i < 3; ++i) {
			this.sinage[i] = this.rand.nextFloat() * 6.0F;
		}

		this.smotionX = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
		this.smotionY = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
		this.smotionZ = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
		if(flag) {
			this.frosty = true;
			this.texture = "/aether/mobs/iceyball.png";
			this.smotionX /= 3.0D;
			this.smotionY = 0.0D;
			this.smotionZ /= 3.0D;
		}

	}

	public EntityFiroBall(World world, double x, double y, double z, boolean flag, boolean flag2) {
		super(world);
		this.texture = "/aether/mobs/firoball.png";
		this.lifeSpan = 300;
		this.life = this.lifeSpan;
		this.setSize(0.9F, 0.9F);
		this.setPositionAndRotation(x, y, z, this.rotationYaw, this.rotationPitch);
		this.sinage = new float[3];
		this.isImmuneToFire = true;

		for(int i = 0; i < 3; ++i) {
			this.sinage[i] = this.rand.nextFloat() * 6.0F;
		}

		this.smotionX = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
		this.smotionY = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
		this.smotionZ = (0.2D + (double)this.rand.nextFloat() * 0.15D) * (this.rand.nextInt(2) == 0 ? 1.0D : -1.0D);
		if(flag) {
			this.frosty = true;
			this.texture = "/aether/mobs/iceyball.png";
			this.smotionX /= 3.0D;
			this.smotionY = 0.0D;
			this.smotionZ /= 3.0D;
		}

		this.fromCloud = flag2;
	}

	public void onUpdate() {
		super.onUpdate();
		--this.life;
		if(this.life <= 0) {
			this.fizzle();
			this.isDead = true;
		}

	}

	public void fizzle() {
		if(this.frosty) {
			this.worldObj.playSoundAtEntity(this, "random.glass", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
		} else {
			this.worldObj.playSoundAtEntity(this, "random.fizz", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
		}

		for(int j = 0; j < 16; ++j) {
			double a = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
			double b = this.posY + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
			double c = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.25D;
			if(!this.frosty) {
				this.worldObj.spawnParticle("largesmoke", a, b, c, 0.0D, 0.0D, 0.0D);
			}
		}

	}

	public void splode() {
		if(this.frosty) {
			this.worldObj.playSoundAtEntity(this, "random.glass", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
		} else {
			this.worldObj.playSoundAtEntity(this, "random.explode", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.2F);
		}

		for(int j = 0; j < 40; ++j) {
			double a = (double)((this.rand.nextFloat() - 0.5F) * 0.5F);
			double b = (double)((this.rand.nextFloat() - 0.5F) * 0.5F);
			double c = (double)((this.rand.nextFloat() - 0.5F) * 0.5F);
			if(this.frosty) {
				a *= 0.5D;
				b *= 0.5D;
				c *= 0.5D;
				this.worldObj.spawnParticle("snowshovel", this.posX, this.posY, this.posZ, a, b + 0.125D, c);
			} else {
				this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, a, b, c);
			}
		}

	}

	public void updateAnims() {
		if(!this.frosty) {
			for(int i = 0; i < 3; ++i) {
				this.sinage[i] += 0.3F + (float)i * 0.13F;
				if(this.sinage[i] > 6.283186F) {
					this.sinage[i] -= 6.283186F;
				}
			}
		}

	}

	public void updatePlayerActionState() {
		this.motionX = this.smotionX;
		this.motionY = this.smotionY;
		this.motionZ = this.smotionZ;
		if(this.isCollided) {
			if(this.frosty && this.smacked) {
				this.splode();
				this.fizzle();
				this.isDead = true;
			} else {
				int i = MathHelper.floor_double(this.posX);
				int j = MathHelper.floor_double(this.boundingBox.minY);
				int k = MathHelper.floor_double(this.posZ);
				if(this.smotionX > 0.0D && this.worldObj.getBlockId(i + 1, j, k) != 0) {
					this.motionX = this.smotionX = -this.smotionX;
				} else if(this.smotionX < 0.0D && this.worldObj.getBlockId(i - 1, j, k) != 0) {
					this.motionX = this.smotionX = -this.smotionX;
				}

				if(this.smotionY > 0.0D && this.worldObj.getBlockId(i, j + 1, k) != 0) {
					this.motionY = this.smotionY = -this.smotionY;
				} else if(this.smotionY < 0.0D && this.worldObj.getBlockId(i, j - 1, k) != 0) {
					this.motionY = this.smotionY = -this.smotionY;
				}

				if(this.smotionZ > 0.0D && this.worldObj.getBlockId(i, j, k + 1) != 0) {
					this.motionZ = this.smotionZ = -this.smotionZ;
				} else if(this.smotionZ < 0.0D && this.worldObj.getBlockId(i, j, k - 1) != 0) {
					this.motionZ = this.smotionZ = -this.smotionZ;
				}
			}
		}

		this.updateAnims();
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("LifeLeft", (short)this.life);
		nbttagcompound.setTag("SeriousKingVampire", this.newDoubleNBTList(new double[]{this.smotionX, this.smotionY, this.smotionZ}));
		nbttagcompound.setBoolean("Frosty", this.frosty);
		nbttagcompound.setBoolean("FromCloud", this.fromCloud);
		nbttagcompound.setBoolean("Smacked", this.smacked);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.life = nbttagcompound.getShort("LifeLeft");
		this.frosty = nbttagcompound.getBoolean("Frosty");
		this.fromCloud = nbttagcompound.getBoolean("FromCloud");
		if(this.frosty) {
			this.texture = "/aether/mobs/iceyball.png";
		}

		this.smacked = nbttagcompound.getBoolean("Smacked");
		NBTTagList nbttaglist = nbttagcompound.getTagList("SeriousKingVampire");
		this.smotionX = (double)((float)((NBTTagDouble)nbttaglist.tagAt(0)).doubleValue);
		this.smotionY = (double)((float)((NBTTagDouble)nbttaglist.tagAt(1)).doubleValue);
		this.smotionZ = (double)((float)((NBTTagDouble)nbttaglist.tagAt(2)).doubleValue);
	}

	public void applyEntityCollision(Entity entity) {
		super.applyEntityCollision(entity);
		boolean flag = false;
		if(entity != null && entity instanceof EntityLiving && !(entity instanceof EntityFiroBall)) {
			if(this.frosty && (!(entity instanceof EntityFireMonster) || this.smacked && !this.fromCloud) && !(entity instanceof EntityFireMinion)) {
				flag = entity.attackEntityFrom(this, 5);
			} else if(!this.frosty && !(entity instanceof EntityFireMonster) && !(entity instanceof EntityFireMinion)) {
				flag = entity.attackEntityFrom(this, 5);
				if(flag) {
					entity.fire = 100;
				}
			}
		}

		if(flag) {
			this.splode();
			this.fizzle();
			this.isDead = true;
		}

	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(entity != null) {
			Vec3D vec3d = entity.getLookVec();
			if(vec3d != null) {
				this.smotionX = vec3d.xCoord;
				this.smotionY = vec3d.yCoord;
				this.smotionZ = vec3d.zCoord;
			}

			this.smacked = true;
			return true;
		} else {
			return false;
		}
	}
}

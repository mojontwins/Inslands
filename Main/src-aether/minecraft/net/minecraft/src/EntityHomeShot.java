package net.minecraft.src;

public class EntityHomeShot extends EntityFlying {
	public float[] sinage;
	public EntityLiving target;
	public boolean firstRun;
	public int life;
	public int lifeSpan;
	private static final double topSpeed = 0.125D;
	private static final float sponge = 57.295773F;

	public EntityHomeShot(World world) {
		super(world);
		this.texture = "/aether/mobs/electroball.png";
		this.lifeSpan = 200;
		this.life = this.lifeSpan;
		this.setSize(0.7F, 0.7F);
		this.firstRun = true;
		this.sinage = new float[3];
		this.isImmuneToFire = true;

		for(int i = 0; i < 3; ++i) {
			this.sinage[i] = this.rand.nextFloat() * 6.0F;
		}

	}

	public EntityHomeShot(World world, double x, double y, double z, EntityLiving ep) {
		super(world);
		this.texture = "/aether/mobs/electroball.png";
		this.lifeSpan = 200;
		this.life = this.lifeSpan;
		this.setSize(0.7F, 0.7F);
		this.setPosition(x, y, z);
		this.target = ep;
		this.sinage = new float[3];
		this.isImmuneToFire = true;

		for(int i = 0; i < 3; ++i) {
			this.sinage[i] = this.rand.nextFloat() * 6.0F;
		}

	}

	public void onUpdate() {
		super.onUpdate();
		--this.life;
		if(this.firstRun && this.target == null) {
			this.target = (EntityLiving)this.findPlayerToAttack();
			this.firstRun = false;
		}

		if(this.target != null && !this.target.isDead && this.target.health > 0) {
			if(this.life <= 0) {
				EntityLightningBolt thunder = new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ);
				this.worldObj.entityJoinedWorld(thunder);
				this.isDead = true;
			} else {
				this.updateAnims();
				this.faceIt();
				this.moveIt(this.target, 0.02D);
			}
		} else {
			this.isDead = true;
		}

	}

	public void moveIt(Entity e1, double sped) {
		double angle1 = (double)(this.rotationYaw / 57.295773F);
		this.motionX -= Math.sin(angle1) * sped;
		this.motionZ += Math.cos(angle1) * sped;
		double a = e1.posY - 0.75D;
		if(a < this.boundingBox.minY - 0.5D) {
			this.motionY -= sped / 2.0D;
		} else if(a > this.boundingBox.minY + 0.5D) {
			this.motionY += sped / 2.0D;
		} else {
			this.motionY += (a - this.boundingBox.minY) * (sped / 2.0D);
		}

		if(this.onGround) {
			this.onGround = false;
			this.motionY = (double)0.1F;
		}

	}

	public void faceIt() {
		this.faceEntity(this.target, 10.0F, 10.0F);
	}

	public void updateAnims() {
		for(int i = 0; i < 3; ++i) {
			this.sinage[i] += 0.3F + (float)i * 0.13F;
			if(this.sinage[i] > 6.283186F) {
				this.sinage[i] -= 6.283186F;
			}
		}

	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("LifeLeft", (short)this.life);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.life = nbttagcompound.getShort("LifeLeft");
	}

	public void checkOverLimit() {
		double a = this.target.posX - this.posX;
		double b = this.target.posY - this.posY;
		double c = this.target.posZ - this.posZ;
		double d = Math.sqrt(a * a + b * b + c * c);
		if(d > 0.125D) {
			double e = 0.125D / d;
			this.motionX *= e;
			this.motionY *= e;
			this.motionZ *= e;
		}

	}

	public Entity findPlayerToAttack() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
	}

	public void applyEntityCollision(Entity entity) {
		super.applyEntityCollision(entity);
		if(entity != null && this.target != null && entity == this.target) {
			boolean flag = entity.attackEntityFrom(this, 1);
			if(flag) {
				this.moveIt(entity, -0.1D);
			}
		}

	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(entity != null) {
			this.moveIt(entity, -0.15D - (double)i / 8.0D);
			return true;
		} else {
			return false;
		}
	}
}

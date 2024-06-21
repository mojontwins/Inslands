package net.minecraft.src;

public class EntityEnderCrystal extends Entity {
	public int innerRotation;
	public int health;

	public EntityEnderCrystal(World world1) {
		super(world1);
		this.innerRotation = 0;
		this.preventEntitySpawning = true;
		this.setSize(2.0F, 2.0F);
		this.yOffset = this.height / 2.0F;
		this.health = 5;
		this.innerRotation = this.rand.nextInt(100000);
	}

	public EntityEnderCrystal(World world1, double d2, double d4, double d6) {
		this(world1);
		this.setPosition(d2, d4, d6);
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(8, this.health);
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		++this.innerRotation;
		this.dataWatcher.updateObject(8, this.health);
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.posY);
		int i3 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.getBlockId(i1, i2, i3) != Block.fire.blockID) {
			this.worldObj.setBlockWithNotify(i1, i2, i3, Block.fire.blockID);
		}

	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		if(!this.isDead && !this.worldObj.isRemote) {
			this.health = 0;
			if(this.health <= 0) {
				if(!this.worldObj.isRemote) {
					this.setDead();
					this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 6.0F);
				} else {
					this.setDead();
				}
			}
		}

		return true;
	}
}

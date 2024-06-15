package net.minecraft.src;

public class EntitySentry extends EntityDungeonMob {
	public float field_100021_a;
	public float field_100020_b;
	private int jcount;
	public int size;
	public int counter;
	public int lostyou;
	public boolean active;

	public EntitySentry(World world) {
		super(world);
		this.texture = "/aether/mobs/Sentry.png";
		this.size = 2;
		this.yOffset = 0.0F;
		this.moveSpeed = 1.0F;
		this.field_100021_a = 1.0F;
		this.field_100020_b = 1.0F;
		this.jcount = this.rand.nextInt(20) + 10;
		this.func_100019_e(this.size);
	}

	public EntitySentry(World world, double x, double y, double z) {
		super(world);
		this.texture = "/aether/mobs/Sentry.png";
		this.size = 2;
		this.yOffset = 0.0F;
		this.moveSpeed = 1.0F;
		this.field_100021_a = 1.0F;
		this.field_100020_b = 1.0F;
		this.jcount = this.rand.nextInt(20) + 10;
		this.func_100019_e(this.size);
		this.rotationYaw = (float)this.rand.nextInt(4) * 1.5707965F;
		this.setPosition(x, y, z);
	}

	public void func_100019_e(int i) {
		this.health = 10;
		this.width = 0.85F;
		this.height = 0.85F;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("Size", this.size - 1);
		nbttagcompound.setInteger("LostYou", this.lostyou);
		nbttagcompound.setInteger("Counter", this.counter);
		nbttagcompound.setBoolean("Active", this.active);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.size = nbttagcompound.getInteger("Size") + 1;
		this.lostyou = nbttagcompound.getInteger("LostYou");
		this.counter = nbttagcompound.getInteger("Counter");
		this.active = nbttagcompound.getBoolean("Active");
	}

	public void onUpdate() {
		boolean flag = this.onGround;
		super.onUpdate();
		if(this.onGround && !flag) {
			this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
		} else if(!this.onGround && flag && this.playerToAttack != null) {
			this.motionX *= 3.0D;
			this.motionZ *= 3.0D;
		}

		if(this.playerToAttack != null && this.playerToAttack.isDead) {
			this.playerToAttack = null;
		}

	}

	public void setEntityDead() {
		if(this.health <= 0 || this.isDead) {
			super.setEntityDead();
		}

	}

	public boolean attackEntityFrom(Entity entity, int i) {
		boolean flag = super.attackEntityFrom(entity, i);
		if(flag && entity instanceof EntityLiving) {
			this.active = true;
			this.lostyou = 0;
			this.playerToAttack = entity;
			this.texture = "/aether/mobs/SentryLit.png";
		}

		return flag;
	}

	public void shutdown() {
		this.counter = -64;
		this.active = false;
		this.playerToAttack = null;
		this.texture = "/aether/mobs/Sentry.png";
		this.setPathToEntity((PathEntity)null);
		this.moveStrafing = 0.0F;
		this.moveForward = 0.0F;
		this.isJumping = false;
		this.motionX = 0.0D;
		this.motionZ = 0.0D;
	}

	public void applyEntityCollision(Entity entity) {
		if(!this.isDead && this.playerToAttack != null && entity != null && this.playerToAttack == entity) {
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.1F);
			entity.attackEntityFrom((Entity)null, 2);
			if(entity instanceof EntityLiving) {
				EntityLiving f = (EntityLiving)entity;
				double i = f.posX - this.posX;

				double d2;
				for(d2 = f.posZ - this.posZ; i * i + d2 * d2 < 1.0E-4D; d2 = (Math.random() - Math.random()) * 0.01D) {
					i = (Math.random() - Math.random()) * 0.01D;
				}

				f.knockBack(this, 5, -i, -d2);
				f.motionX *= 4.0D;
				f.motionY *= 4.0D;
				f.motionZ *= 4.0D;
			}

			float f11 = 0.01745329F;

			for(int i12 = 0; i12 < 40; ++i12) {
				double d1 = (double)((float)this.posX + this.rand.nextFloat() * 0.25F);
				double d3 = (double)((float)this.posY + 0.5F);
				double d4 = (double)((float)this.posZ + this.rand.nextFloat() * 0.25F);
				float f1 = this.rand.nextFloat() * 360.0F;
				this.worldObj.spawnParticle("explode", d1, d3, d4, -Math.sin((double)(f11 * f1)) * 0.75D, 0.125D, Math.cos((double)(f11 * f1)) * 0.75D);
			}

			this.health = 0;
			this.setEntityDead();
		}

	}

	protected void updatePlayerActionState() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);
		if(!this.active && this.counter >= 8) {
			if(entityplayer != null && this.canEntityBeSeen(entityplayer)) {
				this.faceEntity(entityplayer, 10.0F, 10.0F);
				this.playerToAttack = entityplayer;
				this.active = true;
				this.lostyou = 0;
				this.texture = "/aether/mobs/SentryLit.png";
			}

			this.counter = 0;
		} else if(this.active && this.counter >= 8) {
			if(this.playerToAttack == null) {
				if(entityplayer != null && this.canEntityBeSeen(entityplayer)) {
					this.playerToAttack = entityplayer;
					this.active = true;
					this.lostyou = 0;
				} else {
					++this.lostyou;
					if(this.lostyou >= 4) {
						this.shutdown();
					}
				}
			} else if(this.canEntityBeSeen(this.playerToAttack) && this.getDistanceToEntity(this.playerToAttack) < 16.0F) {
				this.lostyou = 0;
			} else {
				++this.lostyou;
				if(this.lostyou >= 4) {
					this.shutdown();
				}
			}

			this.counter = 0;
		} else {
			++this.counter;
		}

		if(this.active) {
			if(this.playerToAttack != null) {
				this.faceEntity(this.playerToAttack, 10.0F, 10.0F);
			}

			if(this.onGround && this.jcount-- <= 0) {
				this.jcount = this.rand.nextInt(20) + 10;
				this.isJumping = true;
				this.moveStrafing = 0.5F - this.rand.nextFloat();
				this.moveForward = 1.0F;
				this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
				if(this.playerToAttack != null) {
					this.jcount /= 2;
					this.moveForward = 1.0F;
				}
			} else {
				this.isJumping = false;
				if(this.onGround) {
					this.moveStrafing = this.moveForward = 0.0F;
				}
			}

		}
	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere();
	}

	protected float getSoundVolume() {
		return 0.6F;
	}

	protected int getDropItemId() {
		return this.rand.nextInt(5) == 0 ? AetherBlocks.LightDungeonStone.blockID : AetherBlocks.DungeonStone.blockID;
	}
}

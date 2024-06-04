package net.minecraft.src;

import java.util.List;

public class EntityAerbunny extends EntityAetherAnimal {
	public int age;
	public int mate;
	public boolean grab;
	public boolean fear;
	public boolean gotrider;
	public Entity runFrom;
	public float puffiness;

	public EntityAerbunny(World world) {
		super(world);
		this.moveSpeed = 2.5F;
		this.texture = "/aether/mobs/aerbunny.png";
		this.yOffset = -0.16F;
		this.setSize(0.4F, 0.4F);
		this.health = 6;
		if(this.renderDistanceWeight < 5.0D) {
			this.renderDistanceWeight = 5.0D;
		}

		this.age = this.rand.nextInt(64);
		this.mate = 0;
	}

	public void onUpdate() {
		if(this.gotrider) {
			this.gotrider = false;
			if(this.ridingEntity == null) {
				EntityPlayer i = (EntityPlayer)this.findPlayerToRunFrom();
				if(i != null && this.getDistanceToEntity(i) < 2.0F && i.riddenByEntity == null) {
					this.mountEntity(i);
				}
			}
		}

		if(this.age < 1023) {
			++this.age;
		} else if(this.mate < 127) {
			++this.mate;
		} else {
			int i9 = 0;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(16.0D, 16.0D, 16.0D));

			for(int list1 = 0; list1 < list.size(); ++list1) {
				Entity flag = (Entity)list.get(list1);
				if(flag instanceof EntityAerbunny) {
					++i9;
				}
			}

			if(i9 > 12) {
				this.proceed();
				return;
			}

			List list10 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 1.0D, 1.0D));
			boolean z11 = false;

			for(int k = 0; k < list.size(); ++k) {
				Entity entity1 = (Entity)list10.get(k);
				if(entity1 instanceof EntityAerbunny && entity1 != this) {
					EntityAerbunny entitybunny = (EntityAerbunny)entity1;
					if(entitybunny.ridingEntity == null && entitybunny.age >= 1023) {
						EntityAerbunny entitybunny1 = new EntityAerbunny(this.worldObj);
						entitybunny1.setPosition(this.posX, this.posY, this.posZ);
						this.worldObj.entityJoinedWorld(entitybunny1);
						this.worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
						this.proceed();
						entitybunny.proceed();
						z11 = true;
						break;
					}
				}
			}

			if(!z11) {
				this.mate = this.rand.nextInt(16);
			}
		}

		if(this.puffiness > 0.0F) {
			this.puffiness -= 0.1F;
		} else {
			this.puffiness = 0.0F;
		}

		super.onUpdate();
	}

	protected void fall(float f) {
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Fear", this.fear);
		if(this.riddenByEntity != null) {
			this.gotrider = true;
		}

		nbttagcompound.setBoolean("GotRider", this.gotrider);
		nbttagcompound.setShort("RepAge", (short)this.age);
		nbttagcompound.setShort("RepMate", (short)this.mate);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.fear = nbttagcompound.getBoolean("Fear");
		this.gotrider = nbttagcompound.getBoolean("GotRider");
		this.age = nbttagcompound.getShort("RepAge");
		this.mate = nbttagcompound.getShort("RepMate");
	}

	protected void updatePlayerActionState() {
		int i;
		if(this.onGround) {
			if(this.moveForward != 0.0F) {
				this.jump();
			}
		} else if(this.ridingEntity != null) {
			if(this.ridingEntity.isDead) {
				this.mountEntity(this.ridingEntity);
			} else if(!this.ridingEntity.onGround && !this.ridingEntity.handleWaterMovement()) {
				this.ridingEntity.fallDistance = 0.0F;
				this.ridingEntity.motionY += (double)0.05F;
				if(this.ridingEntity.motionY < -0.22499999403953552D && this.ridingEntity instanceof EntityLiving && ((EntityLiving)this.ridingEntity).isJumping) {
					this.ridingEntity.motionY = 0.125D;
					this.cloudPoop();
					this.puffiness = 1.15F;
				}
			}
		} else if(!this.grab) {
			if(this.moveForward != 0.0F) {
				int list = MathHelper.floor_double(this.posX);
				i = MathHelper.floor_double(this.boundingBox.minY);
				int entity = MathHelper.floor_double(this.boundingBox.minY - 0.5D);
				int entitymobs = MathHelper.floor_double(this.posZ);
				if((this.worldObj.getBlockId(list, i - 1, entitymobs) != 0 || this.worldObj.getBlockId(list, entity - 1, entitymobs) != 0) && this.worldObj.getBlockId(list, i + 2, entitymobs) == 0 && this.worldObj.getBlockId(list, i + 1, entitymobs) == 0) {
					if(this.motionY < 0.0D) {
						this.cloudPoop();
						this.puffiness = 0.9F;
					}

					this.motionY = 0.2D;
				}
			}

			if(this.motionY < -0.1D) {
				this.motionY = -0.1D;
			}
		}

		if(!this.grab) {
			super.updatePlayerActionState();
			if(this.fear && this.rand.nextInt(4) == 0) {
				if(this.runFrom != null) {
					this.runLikeHell();
					this.worldObj.spawnParticle("splash", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
					if(!this.hasPath()) {
						this.faceEntity(this.runFrom, 30.0F, 30.0F);
					}

					if(this.runFrom.isDead || this.getDistanceToEntity(this.runFrom) > 16.0F) {
						this.runFrom = null;
					}
				} else {
					this.runFrom = this.findPlayerToRunFrom();
				}
			}
		} else if(this.onGround) {
			this.grab = false;
			this.worldObj.playSoundAtEntity(this, "aether.sound.mobs.aerbunny.aerbunnyLand", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			List list5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(12.0D, 12.0D, 12.0D));

			for(i = 0; i < list5.size(); ++i) {
				Entity entity6 = (Entity)list5.get(i);
				if(entity6 instanceof EntityMob) {
					EntityMob entityMob7 = (EntityMob)entity6;
					entityMob7.setTarget(this);
				}
			}
		}

		if(this.handleWaterMovement()) {
			this.jump();
		}

	}

	public void cloudPoop() {
		double a = (double)(this.rand.nextFloat() - 0.5F);
		double c = (double)(this.rand.nextFloat() - 0.5F);
		double d = this.posX + a * (double)0.4F;
		double e = this.boundingBox.minY;
		double f = this.posZ + a * (double)0.4F;
		this.worldObj.spawnParticle("explode", d, e, f, 0.0D, -0.07500000298023224D, 0.0D);
	}

	public boolean attackEntityFrom(Entity e, int i) {
		boolean flag = super.attackEntityFrom(e, i);
		if(flag && e instanceof EntityPlayer) {
			this.fear = true;
		}

		return flag;
	}

	public boolean isOnLadder() {
		return this.moveForward != 0.0F;
	}

	protected Entity findPlayerToRunFrom() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 12.0D);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
	}

	public void runLikeHell() {
		double a = this.posX - this.runFrom.posX;
		double b = this.posZ - this.runFrom.posZ;
		double crazy = Math.atan2(a, b);
		crazy += (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.75D;
		double c = this.posX + Math.sin(crazy) * 8.0D;
		double d = this.posZ + Math.cos(crazy) * 8.0D;
		int x = MathHelper.floor_double(c);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(d);

		for(int q = 0; q < 16; ++q) {
			int i = x + this.rand.nextInt(4) - this.rand.nextInt(4);
			int j = y + this.rand.nextInt(4) - this.rand.nextInt(4) - 1;
			int k = z + this.rand.nextInt(4) - this.rand.nextInt(4);
			if(j > 4 && (this.worldObj.getBlockId(i, j, k) == 0 || this.worldObj.getBlockId(i, j, k) == Block.snow.blockID) && this.worldObj.getBlockId(i, j - 1, k) != 0) {
				PathEntity dogs = this.worldObj.getEntityPathToXYZ(this, i, j, k, 16.0F);
				this.setPathToEntity(dogs);
				break;
			}
		}

	}

	public boolean interact(EntityPlayer entityplayer) {
		this.rotationYaw = entityplayer.rotationYaw;
		if(this.ridingEntity != null) {
			this.renderYawOffset = this.ridingEntity.rotationYaw;
			this.rotationYaw = this.ridingEntity.rotationYaw;
		}

		this.mountEntity(entityplayer);
		if(this.ridingEntity == null) {
			this.grab = true;
		} else {
			this.worldObj.playSoundAtEntity(this, "aether.sound.mobs.aerbunny.aerbunnyLift", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		}

		this.isJumping = false;
		this.moveForward = 0.0F;
		this.moveStrafing = 0.0F;
		this.setPathToEntity((PathEntity)null);
		this.motionX = entityplayer.motionX * 5.0D;
		this.motionY = entityplayer.motionY / 2.0D + 0.5D;
		this.motionZ = entityplayer.motionZ * 5.0D;
		return true;
	}

	public double getYOffset() {
		return this.ridingEntity instanceof EntityPlayer ? (double)(this.yOffset - 1.15F) : (double)this.yOffset;
	}

	protected String getLivingSound() {
		return null;
	}

	protected void dropFewItems() {
		if(mod_Aether.equippedSkyrootSword()) {
			this.dropItem(Item.silk.shiftedIndex, 2);
		} else {
			this.dropItem(Item.silk.shiftedIndex, 1);
		}

	}

	public void proceed() {
		this.mate = 0;
		this.age = this.rand.nextInt(64);
	}

	protected boolean canTriggerWalking() {
		return this.onGround;
	}

	protected String getHurtSound() {
		return "aether.sound.mobs.aerbunny.aerbunnyHurt";
	}

	protected String getDeathSound() {
		return "aether.sound.mobs.aerbunny.aerbunnyDeath";
	}

	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere();
	}
}

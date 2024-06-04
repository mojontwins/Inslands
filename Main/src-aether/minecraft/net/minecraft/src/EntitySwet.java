package net.minecraft.src;

import java.util.List;

public class EntitySwet extends EntityAetherAnimal {
	public int ticker;
	public int flutter;
	public int hops;
	public int textureNum;
	public boolean textureSet;
	public boolean gotrider;
	public boolean kickoff;
	public boolean friendly;

	public EntitySwet(World world) {
		super(world);
		this.health = 25;
		if(!this.textureSet) {
			if(this.rand.nextInt(2) == 0) {
				this.textureNum = 2;
				this.textureSet = true;
			} else {
				this.textureNum = 1;
				this.textureSet = true;
			}
		}

		if(this.textureNum == 1) {
			this.texture = "/aether/mobs/swets.png";
			this.moveSpeed = 1.5F;
		} else {
			this.texture = "/aether/mobs/goldswets.png";
			this.moveSpeed = 3.0F;
		}

		this.setSize(0.8F, 0.8F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.hops = 0;
		this.gotrider = false;
		this.flutter = 0;
		this.ticker = 0;
	}

	public void updateRidden() {
		super.updateRidden();
		if(this.riddenByEntity != null && this.kickoff) {
			this.riddenByEntity.mountEntity(this);
			this.kickoff = false;
		}

	}

	public void updateRiderPosition() {
		this.riddenByEntity.setPosition(this.posX, this.boundingBox.minY - (double)0.3F + (double)this.riddenByEntity.yOffset, this.posZ);
	}

	public void onUpdate() {
		if(this.playerToAttack != null) {
			for(int list = 0; list < 3; ++list) {
				float j = 0.01745278F;
				double entity = (double)((float)this.posX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F);
				double d1 = (double)((float)this.posY + this.height);
				double d2 = (double)((float)this.posZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F);
				this.worldObj.spawnParticle("splash", entity, d1 - 0.25D, d2, 0.0D, 0.0D, 0.0D);
			}
		}

		super.onUpdate();
		if(this.gotrider) {
			if(this.riddenByEntity != null) {
				return;
			}

			List list9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D));
			byte b10 = 0;
			if(b10 < list9.size()) {
				Entity entity11 = (Entity)list9.get(b10);
				this.capturePrey(entity11);
			}

			this.gotrider = false;
		}

		if(this.handleWaterMovement()) {
			this.dissolve();
		}

	}

	protected boolean canDespawn() {
		return true;
	}

	public void fall(float f) {
		if(!this.friendly) {
			super.fall(f);
			if(this.hops >= 3 && this.health > 0) {
				this.dissolve();
			}

		}
	}

	public void knockBack(Entity entity, int i, double d, double d1) {
		if(this.riddenByEntity == null || entity != this.riddenByEntity) {
			super.knockBack(entity, i, d, d1);
		}
	}

	public void dissolve() {
		for(int i = 0; i < 50; ++i) {
			float f = this.rand.nextFloat() * 3.141593F * 2.0F;
			float f1 = this.rand.nextFloat() * 0.5F + 0.25F;
			float f2 = MathHelper.sin(f) * f1;
			float f3 = MathHelper.cos(f) * f1;
			this.worldObj.spawnParticle("splash", this.posX + (double)f2, this.boundingBox.minY + 1.25D, this.posZ + (double)f3, (double)f2 * 1.5D + this.motionX, 4.0D, (double)f3 * 1.5D + this.motionZ);
		}

		if(this.riddenByEntity != null) {
			this.riddenByEntity.posY += (double)(this.riddenByEntity.yOffset - 0.3F);
			this.riddenByEntity.mountEntity(this);
		}

		this.setEntityDead();
	}

	public void capturePrey(Entity entity) {
		this.splorch();
		this.prevPosX = this.posX = entity.posX;
		this.prevPosY = this.posY = entity.posY + (double)0.01F;
		this.prevPosZ = this.posZ = entity.posZ;
		this.prevRotationYaw = this.rotationYaw = entity.rotationYaw;
		this.prevRotationPitch = this.rotationPitch = entity.rotationPitch;
		this.motionX = entity.motionX;
		this.motionY = entity.motionY;
		this.motionZ = entity.motionZ;
		this.setSize(entity.width, entity.height);
		this.setPosition(this.posX, this.posY, this.posZ);
		entity.mountEntity(this);
		this.rotationYaw = this.rand.nextFloat() * 360.0F;
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(this.hops == 3 && entity == null && this.health > 1) {
			this.health = 1;
		}

		boolean flag = super.attackEntityFrom(entity, i);
		if(flag && this.riddenByEntity != null && this.riddenByEntity instanceof EntityLiving) {
			if(entity != null && this.riddenByEntity == entity) {
				if(this.rand.nextInt(3) == 0) {
					this.kickoff = true;
				}
			} else {
				this.riddenByEntity.attackEntityFrom((Entity)null, i);
				if(this.health <= 0) {
					this.kickoff = true;
				}
			}
		}

		if(flag && this.health <= 0) {
			this.dissolve();
		} else if(flag && entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving)entity;
			if(entityliving.health > 0 && (this.riddenByEntity == null || entityliving != this.riddenByEntity)) {
				this.playerToAttack = entity;
				this.faceEntity(entity, 180.0F, 180.0F);
				this.kickoff = true;
			}
		}

		if(this.friendly && this.playerToAttack instanceof EntityPlayer) {
			this.playerToAttack = null;
		}

		return flag;
	}

	public void d_2() {
		if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityLiving) {
			this.moveForward = 0.0F;
			this.moveStrafing = 0.0F;
			this.isJumping = false;
			this.riddenByEntity.fallDistance = 0.0F;
			this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
			this.prevRotationPitch = this.rotationPitch = 0.0F;
			EntityLiving entityliving = (EntityLiving)this.riddenByEntity;
			float f = 3.141593F;
			float f1 = f / 180.0F;
			float f2 = entityliving.rotationYaw * f1;
			if(!this.onGround) {
				if(entityliving.moveForward > 0.1F) {
					if(this.textureNum == 1) {
						this.motionX += (double)entityliving.moveForward * -Math.sin((double)f2) * 0.125D;
						this.motionZ += (double)entityliving.moveForward * Math.cos((double)f2) * 0.125D;
					} else {
						this.motionX += (double)entityliving.moveForward * -Math.sin((double)f2) * 0.325D;
						this.motionZ += (double)entityliving.moveForward * Math.cos((double)f2) * 0.125D;
					}
				} else if(entityliving.moveForward < -0.1F) {
					if(this.textureNum == 1) {
						this.motionX += (double)entityliving.moveForward * -Math.sin((double)f2) * 0.125D;
						this.motionZ += (double)entityliving.moveForward * Math.cos((double)f2) * 0.125D;
					} else {
						this.motionX += (double)entityliving.moveForward * -Math.sin((double)f2) * 0.325D;
						this.motionZ += (double)entityliving.moveForward * Math.cos((double)f2) * 0.125D;
					}
				}

				if(entityliving.moveStrafing > 0.1F) {
					if(this.textureNum == 1) {
						this.motionX += (double)entityliving.moveStrafing * Math.cos((double)f2) * 0.125D;
						this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)f2) * 0.125D;
					} else {
						this.motionX += (double)entityliving.moveStrafing * Math.cos((double)f2) * 0.325D;
						this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)f2) * 0.125D;
					}
				} else if(entityliving.moveStrafing < -0.1F) {
					if(this.textureNum == 1) {
						this.motionX += (double)entityliving.moveStrafing * Math.cos((double)f2) * 0.125D;
						this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)f2) * 0.125D;
					} else {
						this.motionX += (double)entityliving.moveStrafing * Math.cos((double)f2) * 0.325D;
						this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)f2) * 0.125D;
					}
				}

				if(this.motionY < (double)0.05F && this.flutter > 0 && entityliving.isJumping) {
					this.motionY += (double)0.07F;
					--this.flutter;
				}
			} else {
				if(entityliving.isJumping) {
					if(this.hops == 0) {
						this.onGround = false;
						this.motionY = (double)0.85F;
						this.hops = 1;
						this.flutter = 5;
					} else if(this.hops == 1) {
						this.onGround = false;
						this.motionY = 1.0499999523162842D;
						this.hops = 2;
						this.flutter = 5;
					} else if(this.hops == 2) {
						this.onGround = false;
						this.motionY = 1.25D;
						this.flutter = 5;
					}
				} else if(entityliving.moveForward <= 0.125F && entityliving.moveForward >= -0.125F && entityliving.moveStrafing <= 0.125F && entityliving.moveStrafing >= -0.125F) {
					if(this.hops > 0) {
						this.hops = 0;
					}
				} else {
					this.onGround = false;
					this.motionY = (double)0.35F;
					this.hops = 0;
					this.flutter = 0;
				}

				entityliving.moveForward = 0.0F;
				entityliving.moveStrafing = 0.0F;
			}

			double d = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));
			if(d > 0.2750000059604645D) {
				double d1 = 0.275D / d;
				this.motionX *= d1;
				this.motionZ *= d1;
			}
		}

	}

	public void updatePlayerActionState() {
		++this.entityAge;
		this.func_27021_X();
		if(this.friendly && this.riddenByEntity != null) {
			this.d_2();
		} else {
			if(!this.onGround && this.isJumping) {
				this.isJumping = false;
			} else if(this.onGround) {
				if(this.moveForward > 0.05F) {
					this.moveForward *= 0.75F;
				} else {
					this.moveForward = 0.0F;
				}
			}

			if(this.playerToAttack != null && this.riddenByEntity == null && this.health > 0) {
				this.faceEntity(this.playerToAttack, 10.0F, 10.0F);
			}

			if(this.playerToAttack != null && this.playerToAttack.isDead) {
				this.playerToAttack = null;
			}

			if(!this.onGround && this.motionY < (double)0.05F && this.flutter > 0) {
				this.motionY += (double)0.07F;
				--this.flutter;
			}

			if(this.ticker < 4) {
				++this.ticker;
			} else {
				if(this.onGround && this.riddenByEntity == null && this.hops != 0 && this.hops != 3) {
					this.hops = 0;
				}

				if(this.playerToAttack == null && this.riddenByEntity == null) {
					Entity entity = this.getPrey();
					if(entity != null) {
						this.playerToAttack = entity;
					}
				} else if(this.playerToAttack != null && this.riddenByEntity == null) {
					if(this.getDistanceToEntity(this.playerToAttack) <= 9.0F) {
						if(this.onGround && this.canEntityBeSeen(this.playerToAttack)) {
							this.splotch();
							this.flutter = 10;
							this.isJumping = true;
							this.moveForward = 1.0F;
							this.rotationYaw += 5.0F * (this.rand.nextFloat() - this.rand.nextFloat());
						}
					} else {
						this.playerToAttack = null;
						this.isJumping = false;
						this.moveForward = 0.0F;
					}
				} else if(this.riddenByEntity != null && this.onGround) {
					if(this.hops == 0) {
						this.splotch();
						this.onGround = false;
						this.motionY = (double)0.35F;
						this.moveForward = 0.8F;
						this.hops = 1;
						this.flutter = 5;
						this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
					} else if(this.hops == 1) {
						this.splotch();
						this.onGround = false;
						this.motionY = (double)0.45F;
						this.moveForward = 0.9F;
						this.hops = 2;
						this.flutter = 5;
						this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
					} else if(this.hops == 2) {
						this.splotch();
						this.onGround = false;
						this.motionY = 1.25D;
						this.moveForward = 1.25F;
						this.hops = 3;
						this.flutter = 5;
						this.rotationYaw += 20.0F * (this.rand.nextFloat() - this.rand.nextFloat());
					}
				}

				this.ticker = 0;
			}

			if(this.onGround && this.hops >= 3) {
				this.dissolve();
			}

		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("Hops", (short)this.hops);
		nbttagcompound.setShort("Flutter", (short)this.flutter);
		if(this.riddenByEntity != null) {
			this.gotrider = true;
		}

		nbttagcompound.setBoolean("GotRider", this.gotrider);
		nbttagcompound.setBoolean("Friendly", this.friendly);
		nbttagcompound.setBoolean("textureSet", this.textureSet);
		nbttagcompound.setShort("textureNum", (short)this.textureNum);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.hops = nbttagcompound.getShort("Hops");
		this.flutter = nbttagcompound.getShort("Flutter");
		this.gotrider = nbttagcompound.getBoolean("GotRider");
		this.friendly = nbttagcompound.getBoolean("Friendly");
		this.textureSet = nbttagcompound.getBoolean("textureSet");
		this.textureNum = nbttagcompound.getShort("textureNum");
		if(this.textureNum == 1) {
			this.texture = "/aether/mobs/swets.png";
			this.moveSpeed = 1.5F;
		} else {
			this.texture = "/aether/mobs/goldswets.png";
			this.moveSpeed = 3.0F;
		}

	}

	public void splorch() {
		this.worldObj.playSoundAtEntity(this, "mob.slimeattack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
	}

	public void splotch() {
		this.worldObj.playSoundAtEntity(this, "mob.slime", 0.5F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	public void applyEntityCollision(Entity entity) {
		if(this.hops == 0 && this.riddenByEntity == null && this.playerToAttack != null && entity != null && entity == this.playerToAttack && (entity.ridingEntity == null || !(entity.ridingEntity instanceof EntitySwet))) {
			if(entity.riddenByEntity != null) {
				entity.riddenByEntity.mountEntity(entity);
			}

			this.capturePrey(entity);
		}

		super.applyEntityCollision(entity);
	}

	public boolean interact(EntityPlayer entityplayer) {
		if(!this.worldObj.multiplayerWorld) {
			if(!this.friendly) {
				this.friendly = true;
				this.playerToAttack = null;
				return true;
			}

			if(this.friendly && this.riddenByEntity == null || this.riddenByEntity == entityplayer) {
				this.capturePrey(entityplayer);
			}
		}

		return true;
	}

	protected Entity getPrey() {
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(6.0D, 6.0D, 6.0D));
		int i = 0;

		Entity entity;
		while(true) {
			if(i >= list.size()) {
				return null;
			}

			entity = (Entity)list.get(i);
			if(entity instanceof EntityLiving && !(entity instanceof EntitySwet)) {
				if(this.friendly) {
					if(!(entity instanceof EntityPlayer)) {
						break;
					}
				} else if(!(entity instanceof EntityMob)) {
					break;
				}
			}

			++i;
		}

		return entity;
	}

	protected void dropFewItems() {
		ItemStack stack = new ItemStack(this.textureNum == 1 ? AetherBlocks.Aercloud.blockID : Block.glowStone.blockID, 3, this.textureNum == 1 ? 1 : 0);
		if(mod_Aether.equippedSkyrootSword()) {
			stack.stackSize *= 2;
		}

		this.entityDropItem(stack, 0.0F);
	}
}

package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityMoa extends EntityAetherAnimal {
	public static Minecraft mc = ModLoader.getMinecraftInstance();
	public float field_752_b;
	public float destPos;
	public float field_757_d;
	public float field_756_e;
	public float field_755_h;
	public int timeUntilNextEgg;
	public int jrem;
	int petalsEaten;
	boolean wellFed;
	boolean followPlayer;
	public boolean jpress;
	public boolean baby;
	public boolean grown;
	public boolean saddled;
	public MoaColour colour;

	public EntityMoa(World world) {
		this(world, false, false, false);
	}

	public EntityMoa(World world, boolean babyBool, boolean grownBool, boolean saddledBool) {
		this(world, babyBool, grownBool, saddledBool, MoaColour.pickRandomMoa());
	}

	public EntityMoa(World world, boolean babyBool, boolean grownBool, boolean saddledBool, MoaColour moaColour) {
		super(world);
		this.petalsEaten = 0;
		this.wellFed = false;
		this.followPlayer = false;
		this.baby = false;
		this.grown = false;
		this.saddled = false;
		this.destPos = 0.0F;
		this.field_755_h = 1.0F;
		this.stepHeight = 1.0F;
		this.jrem = 0;
		this.baby = babyBool;
		this.grown = grownBool;
		this.saddled = saddledBool;
		if(this.baby) {
			this.setSize(0.4F, 0.5F);
		}

		this.colour = moaColour;
		this.texture = this.colour.getTexture(this.saddled);
		this.setSize(1.0F, 2.0F);
		this.health = 40;
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
	}

	public void onUpdate() {
		super.onUpdate();
		this.ignoreFrustumCheck = this.riddenByEntity == mc.thePlayer;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.field_756_e = this.field_752_b;
		this.field_757_d = this.destPos;
		this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.05D);
		if(this.destPos < 0.01F) {
			this.destPos = 0.01F;
		}

		if(this.destPos > 1.0F) {
			this.destPos = 1.0F;
		}

		if(this.onGround) {
			this.destPos = 0.0F;
			this.jpress = false;
			this.jrem = this.colour.jumps;
		}

		if(!this.onGround && this.field_755_h < 1.0F) {
			this.field_755_h = 1.0F;
		}

		this.field_755_h = (float)((double)this.field_755_h * 0.9D);
		if(!this.onGround && this.motionY < 0.0D) {
			if(this.riddenByEntity == null) {
				this.motionY *= 0.6D;
			} else {
				this.motionY *= 0.6375D;
			}
		}

		this.field_752_b += this.field_755_h * 2.0F;
		if(!this.worldObj.multiplayerWorld && !this.baby && --this.timeUntilNextEgg <= 0) {
			this.worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.entityDropItem(new ItemStack(AetherItems.MoaEgg, 1, this.colour.ID), 0.0F);
			this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		}

		if(this.wellFed && this.rand.nextInt(2000) == 0) {
			this.wellFed = false;
		}

		if(this.saddled && this.riddenByEntity == null) {
			this.moveSpeed = 0.0F;
		} else {
			this.moveSpeed = 0.7F;
		}

	}

	protected void fall(float f) {
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		boolean flag = super.attackEntityFrom(entity, i);
		if(flag && this.riddenByEntity != null && (this.health <= 0 || this.rand.nextInt(3) == 0)) {
			this.riddenByEntity.mountEntity(this);
		}

		return flag;
	}

	public void updatePlayerActionState() {
		if(!this.worldObj.multiplayerWorld) {
			if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityLiving) {
				this.moveForward = 0.0F;
				this.moveStrafing = 0.0F;
				this.isJumping = false;
				this.riddenByEntity.fallDistance = 0.0F;
				this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
				this.prevRotationPitch = this.rotationPitch = this.riddenByEntity.rotationPitch;
				EntityLiving entityliving = (EntityLiving)this.riddenByEntity;
				float f = 3.141593F;
				float f1 = f / 180.0F;
				float d;
				if(entityliving.moveForward > 0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveForward * -Math.sin((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveForward * Math.cos((double)d) * 0.17499999701976776D;
				} else if(entityliving.moveForward < -0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveForward * -Math.sin((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveForward * Math.cos((double)d) * 0.17499999701976776D;
				}

				if(entityliving.moveStrafing > 0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveStrafing * Math.cos((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)d) * 0.17499999701976776D;
				} else if(entityliving.moveStrafing < -0.1F) {
					d = entityliving.rotationYaw * f1;
					this.motionX += (double)entityliving.moveStrafing * Math.cos((double)d) * 0.17499999701976776D;
					this.motionZ += (double)entityliving.moveStrafing * Math.sin((double)d) * 0.17499999701976776D;
				}

				if(this.onGround && entityliving.isJumping) {
					this.onGround = false;
					this.motionY = 0.875D;
					this.jpress = true;
					--this.jrem;
				} else if(this.handleWaterMovement() && entityliving.isJumping) {
					this.motionY = 0.5D;
					this.jpress = true;
					--this.jrem;
				} else if(this.jrem > 0 && !this.jpress && entityliving.isJumping) {
					this.motionY = 0.75D;
					this.jpress = true;
					--this.jrem;
				}

				if(this.jpress && !entityliving.isJumping) {
					this.jpress = false;
				}

				double d8 = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));
				if(d8 > 0.375D) {
					double d1 = 0.375D / d8;
					this.motionX *= d1;
					this.motionZ *= d1;
				}

			} else {
				super.updatePlayerActionState();
			}
		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("Remaining", (short)this.jrem);
		nbttagcompound.setShort("ColourNumber", (short)this.colour.ID);
		nbttagcompound.setBoolean("Baby", this.baby);
		nbttagcompound.setBoolean("Grown", this.grown);
		nbttagcompound.setBoolean("Saddled", this.saddled);
		nbttagcompound.setBoolean("wellFed", this.wellFed);
		nbttagcompound.setInteger("petalsEaten", this.petalsEaten);
		nbttagcompound.setBoolean("followPlayer", this.followPlayer);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.jrem = nbttagcompound.getShort("Remaining");
		this.colour = MoaColour.getColour(nbttagcompound.getShort("ColourNumber"));
		this.baby = nbttagcompound.getBoolean("Baby");
		this.grown = nbttagcompound.getBoolean("Grown");
		this.saddled = nbttagcompound.getBoolean("Saddled");
		this.wellFed = nbttagcompound.getBoolean("wellFed");
		this.petalsEaten = nbttagcompound.getInteger("petalsEaten");
		this.followPlayer = nbttagcompound.getBoolean("followPlayer");
		if(this.baby) {
			this.grown = false;
			this.saddled = false;
		}

		if(this.grown) {
			this.baby = false;
			this.saddled = false;
		}

		if(this.saddled) {
			this.baby = false;
			this.grown = false;
		}

		this.texture = this.colour.getTexture(this.saddled);
	}

	protected String getLivingSound() {
		return "aether.sound.mobs.moa.idleCall";
	}

	protected String getHurtSound() {
		return "aether.sound.mobs.moa.idleCall";
	}

	protected String getDeathSound() {
		return "aether.sound.mobs.moa.idleCall";
	}

	public boolean interact(EntityPlayer entityplayer) {
		if(!this.saddled && this.grown && !this.baby && entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Item.saddle.shiftedIndex) {
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
			this.saddled = true;
			this.grown = false;
			this.texture = this.colour.getTexture(this.saddled);
			return true;
		} else if(this.saddled && !this.worldObj.multiplayerWorld && (this.riddenByEntity == null || this.riddenByEntity == entityplayer)) {
			entityplayer.mountEntity(this);
			entityplayer.prevRotationYaw = entityplayer.rotationYaw = this.rotationYaw;
			return true;
		} else if(!this.wellFed && !this.saddled && this.baby && !this.grown) {
			ItemStack itemstack = entityplayer.inventory.getCurrentItem();
			if(itemstack != null && itemstack.itemID == AetherItems.AechorPetal.shiftedIndex) {
				++this.petalsEaten;
				entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
				if(this.petalsEaten > this.colour.jumps) {
					this.grown = true;
					this.baby = false;
				}

				this.wellFed = true;
			}

			return true;
		} else {
			if(!this.saddled && (this.baby || this.grown)) {
				if(!this.followPlayer) {
					this.followPlayer = true;
					this.playerToAttack = entityplayer;
				} else {
					this.followPlayer = false;
					this.playerToAttack = null;
				}
			}

			return true;
		}
	}

	public boolean canDespawn() {
		return !this.baby && !this.grown && !this.saddled;
	}

	protected boolean canTriggerWalking() {
		return this.onGround;
	}

	protected void dropFewItems() {
		this.dropItem(Item.feather.shiftedIndex, 3 * (mod_Aether.equippedSkyrootSword() ? 2 : 1));
	}
}

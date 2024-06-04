package net.minecraft.src;

import java.util.List;

import net.minecraft.client.Minecraft;

public class EntityCockatrice extends EntityMob {
	public static Minecraft mc = ModLoader.getMinecraftInstance();
	public float field_752_b;
	public float destPos = 0.0F;
	public float field_757_d;
	public float field_756_e;
	public float field_755_h = 1.0F;
	public int timeUntilNextEgg;
	public int jumps;
	public int jrem;
	public boolean jpress;
	public boolean gotrider;

	public EntityCockatrice(World world) {
		super(world);
		this.stepHeight = 1.0F;
		this.jrem = 0;
		this.jumps = 3;
		this.texture = "/aether/mobs/Cockatrice.png";
		this.setSize(1.0F, 2.0F);
		this.health = 20;
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.rand.nextInt(25) == 0 && this.getBlockPathWeight(i, j, k) >= 0.0F && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.DungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedLightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0;
	}

	public void onUpdate() {
		super.onUpdate();
		this.ignoreFrustumCheck = this.riddenByEntity == mc.thePlayer;
		if(!this.worldObj.multiplayerWorld && this.gotrider) {
			if(this.riddenByEntity != null) {
				return;
			}

			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5D, 0.75D, 0.5D));
			byte i = 0;
			if(i < list.size()) {
				Entity entity = (Entity)list.get(i);
				entity.mountEntity(this);
			}

			this.gotrider = false;
		}

		if(!this.worldObj.multiplayerWorld && this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	protected void attackEntity(Entity entity, float f) {
		if(f < 10.0F) {
			double d = entity.posX - this.posX;
			double d1 = entity.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityPoisonNeedle entityarrow = new EntityPoisonNeedle(this.worldObj, this);
				++entityarrow.posY;
				double d2 = entity.posY + (double)entity.getEyeHeight() - (double)0.2F - entityarrow.posY;
				float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "mob.aether.dartshoot", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.entityJoinedWorld(entityarrow);
				entityarrow.setArrowHeading(d, d2 + (double)f1, d1, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2(d1, d) * 180.0D / (double)(float)Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

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
			this.jrem = this.jumps;
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
		if(!this.worldObj.multiplayerWorld && --this.timeUntilNextEgg <= 0) {
			this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		}

	}

	protected void fall(float f) {
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(entity != null && this.riddenByEntity != null && entity == this.riddenByEntity) {
			return false;
		} else {
			boolean flag = super.attackEntityFrom(entity, i);
			if(flag && this.riddenByEntity != null && (this.health <= 0 || this.rand.nextInt(3) == 0)) {
				this.riddenByEntity.mountEntity(this);
			}

			return flag;
		}
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
		nbttagcompound.setShort("Jumps", (short)this.jumps);
		nbttagcompound.setShort("Remaining", (short)this.jrem);
		if(this.riddenByEntity != null) {
			this.gotrider = true;
		}

		nbttagcompound.setBoolean("GotRider", this.gotrider);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.jumps = nbttagcompound.getShort("Jumps");
		this.jrem = nbttagcompound.getShort("Remaining");
		this.gotrider = nbttagcompound.getBoolean("GotRider");
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
		return true;
	}

	protected void dropFewItems() {
		this.dropItem(Item.feather.shiftedIndex, 3 * (mod_Aether.equippedSkyrootSword() ? 2 : 1));
	}
}

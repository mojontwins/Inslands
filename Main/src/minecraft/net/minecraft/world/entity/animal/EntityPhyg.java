package net.minecraft.world.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.src.AchievementList;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;

public class EntityPhyg extends EntityAetherAnimal {
	public boolean getSaddled = false;
	public float wingFold;
	public float wingAngle;
	private float aimingForFold;
	public int jumps;
	public int jrem;
	private boolean jpress;
	private int ticks;

	public EntityPhyg(World world) {
		super(world);
		this.texture = "/mob/Mob_FlyingPigBase.png";
		this.setSize(0.9F, 1.3F);
		this.jrem = 0;
		this.jumps = 1;
		this.ticks = 0;
		this.stepHeight = 1.0F;
		this.ignoreFrustumCheck = true;
	}

	protected boolean canDespawn() {
		return !this.getSaddled;
	}

	protected boolean canTriggerWalking() {
		return this.onGround;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(16, (byte)0);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("Jumps", (short)this.jumps);
		nbttagcompound.setShort("Remaining", (short)this.jrem);
		nbttagcompound.setBoolean("getSaddled", this.getSaddled);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.jumps = nbttagcompound.getShort("Jumps");
		this.jrem = nbttagcompound.getShort("Remaining");
		this.getSaddled = nbttagcompound.getBoolean("getSaddled");
		if(this.getSaddled) {
			this.texture = "/mob/Mob_FlyingPigSaddle.png";
		}

	}

	protected void jump() {
		this.motionY = 0.6D;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.onGround) {
			this.wingAngle *= 0.8F;
			this.aimingForFold = 0.1F;
			this.jpress = false;
			this.jrem = this.jumps;
		} else {
			this.aimingForFold = 1.0F;
		}

		++this.ticks;
		this.wingAngle = this.wingFold * (float)Math.sin((double)((float)this.ticks / 31.830988F));
		this.wingFold += (this.aimingForFold - this.wingFold) / 5.0F;
		this.fallDistance = 0.0F;
		if(this.motionY < -0.2D) {
			this.motionY = -0.2D;
		}

	}

	@Override
	public void updateEntityActionState() {
		if(!this.worldObj.isRemote) {
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
					this.motionY = 1.4D;
					this.jpress = true;
					--this.jrem;
				} else if(this.handleWaterMovement() && entityliving.isJumping) {
					this.motionY = 0.5D;
					this.jpress = true;
					--this.jrem;
				} else if(this.jrem > 0 && !this.jpress && entityliving.isJumping) {
					this.motionY = 1.2D;
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
				super.updateEntityActionState();
			}
		}
	}

	protected String getLivingSound() {
		return "mob.pig";
	}

	protected String getHurtSound() {
		return "mob.pig";
	}

	protected String getDeathSound() {
		return "mob.pigdeath";
	}

	public boolean interact(EntityPlayer entityplayer) {
		if(!this.getSaddled && entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().itemID == Item.saddle.shiftedIndex) {
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
			this.getSaddled = true;
			this.texture = "/mob/Mob_FlyingPigSaddle.png";
			return true;
		} else if(!this.getSaddled || this.worldObj.isRemote || this.riddenByEntity != null && this.riddenByEntity != entityplayer) {
			return false;
		} else {
			entityplayer.mountEntity(this);
			((EntityPlayer)entityplayer).triggerAchievement(AchievementList.flyingPig);
			return true;
		}
	}

	protected void dropFewItems() {
		this.dropItem(this.rand.nextBoolean() ? Item.feather.shiftedIndex : Item.porkRaw.shiftedIndex, 1);
	}
}

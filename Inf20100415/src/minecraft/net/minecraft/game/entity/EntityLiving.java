package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;

import java.util.List;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;

import util.MathHelper;

public class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	private float rotationYawHead;
	private float prevRotationYawHead;
	protected String texture = "/char.png";
	private int scoreValue = 0;
	public int health;
	public int prevHealth;
	private int livingSoundTime;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	public float newPosZ;
	public float newRotationYaw;
	public float newRotationPitch;
	protected int entityAge;
	protected float moveStrafing;
	protected float moveForward;
	private float randomYawVelocity;
	protected boolean isJumping;
	private float defaultPitch;
	protected float moveSpeed;

	public EntityLiving(World world1) {
		super(world1);
		Math.random();
		this.entityAge = 0;
		this.isJumping = false;
		this.defaultPitch = 0.0F;
		this.moveSpeed = 0.7F;
		this.health = 10;
		this.preventEntitySpawning = true;
		Math.random();
		this.setPosition(this.posX, this.posY, this.posZ);
		Math.random();
		this.rotationYaw = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.stepHeight = 0.5F;
	}

	public final String getEntityTexture() {
		return this.texture;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final boolean canBePushed() {
		return !this.isDead;
	}

	protected float getEyeHeight() {
		return this.height * 0.85F;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -80;
			String string1;
			if((string1 = this.getLivingSound()) != null) {
				this.worldObj.playSoundAtEntity(this, string1, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}

		if(this.isEntityAlive() && this.isInsideOfMaterial()) {
			--this.air;
			if(this.air == -20) {
				this.air = 0;

				for(int i9 = 0; i9 < 8; ++i9) {
					float f2 = this.rand.nextFloat() - this.rand.nextFloat();
					float f3 = this.rand.nextFloat() - this.rand.nextFloat();
					float f4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)f2, this.posY + (double)f3, this.posZ + (double)f4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom((Entity)null, 2);
			}

			this.fire = 0;
		} else {
			this.air = this.maxAir;
		}

		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.heartsLife > 0) {
			--this.heartsLife;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				super.isDead = true;
			}
		}

		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.onLivingUpdate();
		double d10 = this.posX - this.prevPosX;
		double d13 = this.posZ - this.prevPosZ;
		float f5 = MathHelper.sqrt_double(d10 * d10 + d13 * d13);
		float f6 = this.renderYawOffset;
		float f7 = 0.0F;
		float f8 = 0.0F;
		if(f5 > 0.05F) {
			f8 = 1.0F;
			f7 = f5 * 3.0F;
			f6 = (float)Math.atan2(d13, d10) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(!this.onGround) {
			f8 = 0.0F;
		}

		this.rotationYawHead += (f8 - this.rotationYawHead) * 0.3F;

		float f11;
		for(f11 = f6 - this.renderYawOffset; f11 < -180.0F; f11 += 360.0F) {
		}

		while(f11 >= 180.0F) {
			f11 -= 360.0F;
		}

		this.renderYawOffset += f11 * 0.1F;

		for(f11 = this.rotationYaw - this.renderYawOffset; f11 < -180.0F; f11 += 360.0F) {
		}

		while(f11 >= 180.0F) {
			f11 -= 360.0F;
		}

		boolean z12 = f11 < -90.0F || f11 >= 90.0F;
		if(f11 < -75.0F) {
			f11 = -75.0F;
		}

		if(f11 >= 75.0F) {
			f11 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - f11;
		this.renderYawOffset += f11 * 0.1F;
		if(z12) {
			f7 = -f7;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.prevRotationYawHead += f7;
	}

	protected final void setSize(float f1, float f2) {
		super.setSize(f1, f2);
	}

	public final void heal(int i1) {
		if(this.health > 0) {
			this.health += i1;
			if(this.health > 20) {
				this.health = 20;
			}

			this.heartsLife = this.heartsHalvesLife / 2;
		}
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.entityAge = 0;
		if(this.health <= 0) {
			return false;
		} else {
			this.newRotationYaw = 1.5F;
			if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
				if(this.prevHealth - i2 >= this.health) {
					return false;
				}

				this.health = this.prevHealth - i2;
			} else {
				this.prevHealth = this.health;
				this.heartsLife = this.heartsHalvesLife;
				this.health -= i2;
				this.hurtTime = this.maxHurtTime = 10;
			}

			this.attackedAtYaw = 0.0F;
			if(entity1 != null) {
				double d3 = entity1.posX - this.posX;
				double d5 = entity1.posZ - this.posZ;
				this.attackedAtYaw = (float)(Math.atan2(d5, d3) * 180.0D / (double)(float)Math.PI) - this.rotationYaw;
				double d8 = d3;
				float f12 = MathHelper.sqrt_double(d3 * d3 + d5 * d5);
				this.motionX /= 2.0D;
				this.motionY /= 2.0D;
				this.motionZ /= 2.0D;
				this.motionX -= d8 / (double)f12 * (double)0.4F;
				this.motionY += (double)0.4F;
				this.motionZ -= d5 / (double)f12 * (double)0.4F;
				if(this.motionY > (double)0.4F) {
					this.motionY = (double)0.4F;
				}
			} else {
				this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
			}

			if(this.health <= 0) {
				this.worldObj.playSoundAtEntity(this, this.getDeathSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				this.onDeath(entity1);
			} else {
				this.worldObj.playSoundAtEntity(this, this.getHurtSound(), 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			return true;
		}
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void onDeath(Entity entity1) {
		int i4;
		if((i4 = this.getDroppedItem()) > 0) {
			int i2 = this.rand.nextInt(3);

			for(int i3 = 0; i3 < i2; ++i3) {
				this.dropItemWithOffset(i4, 1);
			}
		}

	}

	protected int getDroppedItem() {
		return 0;
	}

	protected final void fall(float f1) {
		int i3;
		if((i3 = (int)Math.ceil((double)(f1 - 3.0F))) > 0) {
			this.attackEntityFrom((Entity)null, i3);
			if((i3 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - (double)0.2F - (double)this.yOffset), (int)this.posZ)) > 0) {
				StepSound stepSound4 = Block.blocksList[i3].stepSound;
				this.worldObj.playSoundAtEntity(this, stepSound4.getStepSound(), stepSound4.stepSoundVolume * 0.5F, stepSound4.stepSoundPitch * 0.75F);
			}
		}

	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("Health", (short)this.health);
		nBTTagCompound1.setShort("HurtTime", (short)this.hurtTime);
		nBTTagCompound1.setShort("DeathTime", (short)this.deathTime);
		nBTTagCompound1.setShort("AttackTime", (short)this.attackTime);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.health = nBTTagCompound1.getShort("Health");
		if(!nBTTagCompound1.hasKey("Health")) {
			this.health = 10;
		}

		this.hurtTime = nBTTagCompound1.getShort("HurtTime");
		this.deathTime = nBTTagCompound1.getShort("DeathTime");
		this.attackTime = nBTTagCompound1.getShort("AttackTime");
	}

	public final boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public void onLivingUpdate() {
		++this.entityAge;
		Entity entity1;
		if((entity1 = this.worldObj.getPlayerEntity()) != null) {
			double d2 = entity1.posX - this.posX;
			double d4 = entity1.posY - this.posY;
			double d6 = entity1.posZ - this.posZ;
			double d8;
			if((d8 = d2 * d2 + d4 * d4 + d6 * d6) > 16384.0D) {
				super.isDead = true;
			}

			if(this.entityAge > 600 && this.rand.nextInt(800) == 0) {
				if(d8 < 1024.0D) {
					this.entityAge = 0;
				} else {
					super.isDead = true;
				}
			}
		}

		if(this.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else {
			this.updatePlayerActionState();
		}

		boolean z17 = this.handleWaterMovement();
		boolean z3 = this.handleLavaMovement();
		if(this.isJumping) {
			if(z17) {
				this.motionY += (double)0.04F;
			} else if(z3) {
				this.motionY += (double)0.04F;
			} else if(this.onGround) {
				this.motionY = (double)0.42F;
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		float f19 = this.moveForward;
		float f18 = this.moveStrafing;
		double d13;
		if(this.handleWaterMovement()) {
			d13 = this.posY;
			this.moveFlying(f18, f19, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.8F;
			this.motionY *= (double)0.8F;
			this.motionZ *= (double)0.8F;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d13, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else if(this.handleLavaMovement()) {
			d13 = this.posY;
			this.moveFlying(f18, f19, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d13, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else {
			this.moveFlying(f18, f19, this.onGround ? 0.1F : 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.91F;
			this.motionY *= (double)0.98F;
			this.motionZ *= (double)0.91F;
			this.motionY -= 0.08D;
			if(this.onGround) {
				this.motionX *= (double)0.6F;
				this.motionZ *= (double)0.6F;
			}
		}

		this.newPosZ = this.newRotationYaw;
		d13 = this.posX - this.prevPosX;
		double d15 = this.posZ - this.prevPosZ;
		if((f18 = MathHelper.sqrt_double(d13 * d13 + d15 * d15) * 4.0F) > 1.0F) {
			f18 = 1.0F;
		}

		this.newRotationYaw += (f18 - this.newRotationYaw) * 0.4F;
		this.newRotationPitch += this.newRotationYaw;
		List list20;
		if((list20 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F))) != null && list20.size() > 0) {
			for(int i5 = 0; i5 < list20.size(); ++i5) {
				Entity entity21;
				if((entity21 = (Entity)list20.get(i5)).canBePushed()) {
					entity21.applyEntityCollision(this);
				}
			}
		}

	}

	protected void updatePlayerActionState() {
		if(this.rand.nextFloat() < 0.07F) {
			this.moveStrafing = (this.rand.nextFloat() - 0.5F) * this.moveSpeed;
			this.moveForward = this.rand.nextFloat() * this.moveSpeed;
		}

		this.isJumping = this.rand.nextFloat() < 0.01F;
		if(this.rand.nextFloat() < 0.04F) {
			this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 60.0F;
		}

		this.rotationYaw += this.randomYawVelocity;
		this.rotationPitch = 0.0F;
		boolean z1 = this.handleWaterMovement();
		boolean z2 = this.handleLavaMovement();
		if(z1 || z2) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	public boolean getCanSpawnHere(float f1, float f2, float f3) {
		this.setPosition((double)f1, (double)(f2 + this.height / 2.0F), (double)f3);
		return this.worldObj.checkIfAABBIsClear1(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}
}
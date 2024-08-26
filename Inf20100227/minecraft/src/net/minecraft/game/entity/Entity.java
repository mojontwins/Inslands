package net.minecraft.game.entity;

import java.util.List;
import java.util.Random;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;
import net.minecraft.game.world.material.Material;
import util.MathHelper;

public abstract class Entity {
	protected World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public AxisAlignedBB boundingBox;
	public boolean onGround = false;
	public boolean isCollided = false;
	private boolean surfaceCollision = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	public float width = 0.6F;
	public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	protected boolean canTriggerWalking = true;
	private float fallDistance = 0.0F;
	private int nextStepDistance = 1;
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	private float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public boolean noClip = false;
	private float entityCollisionReduction = 0.0F;
	protected Random rand = new Random();
	public int ticksExisted = 0;
	public int fireResistance = 1;
	public int fire = 0;
	protected int maxAir = 300;
	private boolean inWater = false;
	public int heartsLife = 0;
	public int air = 300;
	private boolean firstUpdate = true;
	public String skinUrl;

	public Entity(World var1) {
		this.worldObj = var1;
		this.setPosition(0.0D, 0.0D, 0.0D);
	}

	protected void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			while(this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);
				if(this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0) {
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}
	}

	public void setEntityDead() {
		this.isDead = true;
	}

	protected void setSize(float var1, float var2) {
		this.width = var1;
		this.height = var2;
	}

	protected final void setPosition(double var1, double var3, double var5) {
		this.posX = var1;
		this.posY = var3;
		this.posZ = var5;
		float var7 = this.width / 2.0F;
		float var8 = this.height / 2.0F;
		this.boundingBox = new AxisAlignedBB(var1 - (double)var7, var3 - (double)var8, var5 - (double)var7, var1 + (double)var7, var3 + (double)var8, var5 + (double)var7);
	}

	public void onUpdate() {
		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		if(this.handleWaterMovement()) {
			if(!this.inWater && !this.firstUpdate) {
				float var1 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2F) * 0.2F;
				if(var1 > 1.0F) {
					var1 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", var1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				var1 = (float)MathHelper.floor_double(this.boundingBox.minY);

				int var2;
				float var3;
				float var4;
				for(var2 = 0; (float)var2 < 1.0F + this.width * 20.0F; ++var2) {
					var3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + (double)var3, (double)(var1 + 1.0F), this.posZ + (double)var4, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
				}

				for(var2 = 0; (float)var2 < 1.0F + this.width * 20.0F; ++var2) {
					var3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + (double)var3, (double)(var1 + 1.0F), this.posZ + (double)var4, this.motionX, this.motionY, this.motionZ);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if(this.fire > 0) {
			if(this.fire % 20 == 0) {
				this.attackEntityFrom((Entity)null, 1);
			}

			--this.fire;
		}

		if(this.handleLavaMovement()) {
			this.attackEntityFrom((Entity)null, 10);
			this.fire = 600;
		}

		this.firstUpdate = false;
	}

	public final boolean isOffsetPositionInLiquid(double var1, double var3, double var5) {
		double var10 = var1;
		AxisAlignedBB var16 = this.boundingBox;
		var16 = new AxisAlignedBB(var16.minX + var5, var16.minY + var3, var16.minZ + var5, var16.maxX + var10, var16.maxY + var3, var16.maxZ + var5);
		List var2 = this.worldObj.getCollidingBoundingBoxes(var16);
		return var2.size() > 0 ? false : !this.worldObj.getIsAnyLiquid(var16);
	}

	public final void moveEntity(double var1, double var3, double var5) {
		if(this.noClip) {
			this.boundingBox.offset(var1, var3, var5);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			double var7 = this.posX;
			double var9 = this.posZ;
			double var11 = var1;
			double var13 = var3;
			double var15 = var5;
			AxisAlignedBB var17 = this.boundingBox.copy();
			List var18 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(var1, var3, var5));

			int var19;
			for(var19 = 0; var19 < var18.size(); ++var19) {
				var3 = ((AxisAlignedBB)var18.get(var19)).calculateYOffset(this.boundingBox, var3);
			}

			this.boundingBox.offset(0.0D, var3, 0.0D);
			if(!this.surfaceCollision && var13 != var3) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			boolean var28 = this.onGround || var13 != var3 && var13 < 0.0D;

			int var20;
			for(var20 = 0; var20 < var18.size(); ++var20) {
				var1 = ((AxisAlignedBB)var18.get(var20)).calculateXOffset(this.boundingBox, var1);
			}

			this.boundingBox.offset(var1, 0.0D, 0.0D);
			if(!this.surfaceCollision && var11 != var1) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			for(var20 = 0; var20 < var18.size(); ++var20) {
				var5 = ((AxisAlignedBB)var18.get(var20)).calculateZOffset(this.boundingBox, var5);
			}

			this.boundingBox.offset(0.0D, 0.0D, var5);
			if(!this.surfaceCollision && var15 != var5) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			double var22;
			int var27;
			double var30;
			if(this.stepHeight > 0.0F && var28 && this.ySize < 0.05F && (var11 != var1 || var15 != var5)) {
				var30 = var1;
				var22 = var3;
				double var24 = var5;
				var1 = var11;
				var3 = (double)this.stepHeight;
				var5 = var15;
				AxisAlignedBB var29 = this.boundingBox.copy();
				this.boundingBox = var17.copy();
				var18 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(var11, var3, var15));

				for(var27 = 0; var27 < var18.size(); ++var27) {
					var3 = ((AxisAlignedBB)var18.get(var27)).calculateYOffset(this.boundingBox, var3);
				}

				this.boundingBox.offset(0.0D, var3, 0.0D);
				if(!this.surfaceCollision && var13 != var3) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				for(var27 = 0; var27 < var18.size(); ++var27) {
					var1 = ((AxisAlignedBB)var18.get(var27)).calculateXOffset(this.boundingBox, var1);
				}

				this.boundingBox.offset(var1, 0.0D, 0.0D);
				if(!this.surfaceCollision && var11 != var1) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				for(var27 = 0; var27 < var18.size(); ++var27) {
					var5 = ((AxisAlignedBB)var18.get(var27)).calculateZOffset(this.boundingBox, var5);
				}

				this.boundingBox.offset(0.0D, 0.0D, var5);
				if(!this.surfaceCollision && var15 != var5) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				if(var30 * var30 + var24 * var24 >= var1 * var1 + var5 * var5) {
					var1 = var30;
					var3 = var22;
					var5 = var24;
					this.boundingBox = var29.copy();
				} else {
					this.ySize = (float)((double)this.ySize + 0.5D);
				}
			}

			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			this.isCollided = var11 != var1 || var15 != var5;
			this.onGround = var13 != var3 && var13 < 0.0D;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.fall(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(var3 < 0.0D) {
				this.fallDistance = (float)((double)this.fallDistance - var3);
			}

			if(var11 != var1) {
				this.motionX = 0.0D;
			}

			if(var13 != var3) {
				this.motionY = 0.0D;
			}

			if(var15 != var5) {
				this.motionZ = 0.0D;
			}

			var30 = this.posX - var7;
			var22 = this.posZ - var9;
			this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(var30 * var30 + var22 * var22) * 0.6D);
			if(this.canTriggerWalking) {
				int var31 = MathHelper.floor_double(this.posX);
				int var25 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
				var19 = MathHelper.floor_double(this.posZ);
				var27 = this.worldObj.getBlockId(var31, var25, var19);
				if(this.distanceWalkedModified > (float)this.nextStepDistance && var27 > 0) {
					++this.nextStepDistance;
					StepSound var26 = Block.blocksList[var27].stepSound;
					if(!Block.blocksList[var27].material.getIsSolid()) {
						this.worldObj.playSoundAtEntity(this, var26.getStepSound(), var26.stepSoundVolume * 0.15F, var26.stepSoundPitch);
					}

					Block.blocksList[var27].onEntityWalking(this.worldObj, var31, var25, var19);
				}
			}

			this.ySize *= 0.4F;
			boolean var32 = this.handleWaterMovement();
			if(this.worldObj.c(this.boundingBox)) {
				this.dealFireDamage(1);
				if(!var32) {
					++this.fire;
					if(this.fire == 0) {
						this.fire = 300;
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(var32 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

		}
	}

	protected void dealFireDamage(int var1) {
		this.attackEntityFrom((Entity)null, 1);
	}

	protected void fall(float var1) {
	}

	public final boolean handleWaterMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(0.0D, (double)-0.4F, 0.0D), Material.water);
	}

	public final boolean isInsideOfWater() {
		int var1 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + (double)this.getEyeHeight()), MathHelper.floor_double(this.posZ));
		return var1 != 0 ? Block.blocksList[var1].material == Material.water : false;
	}

	protected float getEyeHeight() {
		return 0.0F;
	}

	public final boolean handleLavaMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(0.0D, (double)-0.4F, 0.0D), Material.lava);
	}

	public final void addVelocity(float var1, float var2, float var3) {
		float var4 = MathHelper.sqrt_float(var1 * var1 + var2 * var2);
		if(var4 >= 0.01F) {
			if(var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = var3 / var4;
			var1 *= var4;
			var2 *= var4;
			var3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			var4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += (double)(var1 * var4 - var2 * var3);
			this.motionZ += (double)(var2 * var4 + var1 * var3);
		}
	}

	public float getBrightness(float var1) {
		int var4 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.posY + (double)(this.yOffset / 2.0F));
		int var3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBrightness(var4, var2, var3);
	}

	public final void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
		this.prevPosX = this.posX = var1;
		this.prevPosY = this.posY = var3 + (double)this.yOffset;
		this.prevPosZ = this.posZ = var5;
		this.rotationYaw = var7;
		this.rotationPitch = var8;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public final double getDistanceToEntity(Entity var1) {
		double var2 = this.posX - var1.posX;
		double var4 = this.posY - var1.posY;
		double var6 = this.posZ - var1.posZ;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}

	public boolean attackEntityFrom(Entity var1, int var2) {
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public String getTexture() {
		return null;
	}

	public float getShadowSize() {
		return this.height / 2.0F;
	}

	public final EntityItem dropItemWithOffset(int var1, int var2) {
		return this.entityDropItem(var1, 1, 0.0F);
	}

	public final EntityItem entityDropItem(int var1, int var2, float var3) {
		EntityItem var4 = new EntityItem(this.worldObj, this.posX, this.posY + (double)var3, this.posZ, new ItemStack(var1, var2));
		var4.delayBeforeCanPickup = 10;
		return var4;
	}

	public boolean canBePushed() {
		return !this.isDead;
	}
}

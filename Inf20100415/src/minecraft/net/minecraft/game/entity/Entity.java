package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagDouble;
import com.mojang.nbt.NBTTagFloat;
import com.mojang.nbt.NBTTagList;

import java.util.List;
import java.util.Random;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.block.StepSound;
import net.minecraft.game.world.material.Material;

import util.MathHelper;

public abstract class Entity {
	public boolean preventEntitySpawning = false;
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
	public AxisAlignedBB boundingBox = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	public boolean onGround = false;
	public boolean isCollidedHorizontally = false;
	private boolean unknownBool = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	public float width = 0.6F;
	public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	protected boolean entityWalks = true;
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
	private boolean isFirstUpdate = true;
	public String skinUrl;

	public Entity(World world1) {
		this.worldObj = world1;
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

	protected void setSize(float f1, float f2) {
		this.width = f1;
		this.height = f2;
	}

	protected final void setPosition(double d1, double d3, double d5) {
		this.posX = d1;
		this.posY = d3;
		this.posZ = d5;
		float f7 = this.width / 2.0F;
		float f8 = this.height / 2.0F;
		double d10001 = d1 - (double)f7;
		double d10002 = d3 - (double)f8;
		double d10003 = d5 - (double)f7;
		double d10004 = d1 + (double)f7;
		double d10005 = d3 + (double)f8;
		double d20 = d5 + (double)f7;
		double d18 = d10005;
		double d16 = d10004;
		double d14 = d10003;
		double d12 = d10002;
		double d10 = d10001;
		AxisAlignedBB axisAlignedBB22 = this.boundingBox;
		this.boundingBox.minX = d10;
		axisAlignedBB22.minY = d12;
		axisAlignedBB22.minZ = d14;
		axisAlignedBB22.maxX = d16;
		axisAlignedBB22.maxY = d18;
		axisAlignedBB22.maxZ = d20;
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
			if(!this.inWater && !this.isFirstUpdate) {
				float f1;
				if((f1 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2F) * 0.2F) > 1.0F) {
					f1 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				f1 = (float)MathHelper.floor_double(this.boundingBox.minY);

				int i2;
				float f3;
				float f4;
				for(i2 = 0; (float)i2 < 1.0F + this.width * 20.0F; ++i2) {
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + (double)f3, (double)(f1 + 1.0F), this.posZ + (double)f4, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
				}

				for(i2 = 0; (float)i2 < 1.0F + this.width * 20.0F; ++i2) {
					f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + (double)f3, (double)(f1 + 1.0F), this.posZ + (double)f4, this.motionX, this.motionY, this.motionZ);
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

		this.isFirstUpdate = false;
	}

	public final boolean isOffsetPositionInLiquid(double d1, double d3, double d5) {
		AxisAlignedBB axisAlignedBB7 = this.boundingBox.offsetCopy(d1, d3, d5);
		return this.worldObj.getCollidingBoundingBoxes(axisAlignedBB7).size() > 0 ? false : !this.worldObj.getIsAnyLiquid(axisAlignedBB7);
	}

	public final void moveEntity(double d1, double d3, double d5) {
		if(this.noClip) {
			this.boundingBox.offset(d1, d3, d5);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			double d7 = this.posX;
			double d9 = this.posZ;
			double d11 = d1;
			double d13 = d3;
			double d15 = d5;
			AxisAlignedBB axisAlignedBB17 = this.boundingBox.copy();
			List list18 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(d1, d3, d5));

			int i19;
			for(i19 = 0; i19 < list18.size(); ++i19) {
				d3 = ((AxisAlignedBB)list18.get(i19)).calculateYOffset(this.boundingBox, d3);
			}

			this.boundingBox.offset(0.0D, d3, 0.0D);
			if(!this.unknownBool && d13 != d3) {
				d5 = 0.0D;
				d3 = 0.0D;
				d1 = 0.0D;
			}

			boolean z28 = this.onGround || d13 != d3 && d13 < 0.0D;

			int i20;
			for(i20 = 0; i20 < list18.size(); ++i20) {
				d1 = ((AxisAlignedBB)list18.get(i20)).calculateXOffset(this.boundingBox, d1);
			}

			this.boundingBox.offset(d1, 0.0D, 0.0D);
			if(!this.unknownBool && d11 != d1) {
				d5 = 0.0D;
				d3 = 0.0D;
				d1 = 0.0D;
			}

			for(i20 = 0; i20 < list18.size(); ++i20) {
				d5 = ((AxisAlignedBB)list18.get(i20)).calculateZOffset(this.boundingBox, d5);
			}

			this.boundingBox.offset(0.0D, 0.0D, d5);
			if(!this.unknownBool && d15 != d5) {
				d5 = 0.0D;
				d3 = 0.0D;
				d1 = 0.0D;
			}

			double d22;
			int i27;
			double d30;
			if(this.stepHeight > 0.0F && z28 && this.ySize < 0.05F && (d11 != d1 || d15 != d5)) {
				d30 = d1;
				d22 = d3;
				double d24 = d5;
				d1 = d11;
				d3 = (double)this.stepHeight;
				d5 = d15;
				AxisAlignedBB axisAlignedBB29 = this.boundingBox.copy();
				this.boundingBox = axisAlignedBB17.copy();
				list18 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(d11, d3, d15));

				for(i27 = 0; i27 < list18.size(); ++i27) {
					d3 = ((AxisAlignedBB)list18.get(i27)).calculateYOffset(this.boundingBox, d3);
				}

				this.boundingBox.offset(0.0D, d3, 0.0D);
				if(!this.unknownBool && d13 != d3) {
					d5 = 0.0D;
					d3 = 0.0D;
					d1 = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					d1 = ((AxisAlignedBB)list18.get(i27)).calculateXOffset(this.boundingBox, d1);
				}

				this.boundingBox.offset(d1, 0.0D, 0.0D);
				if(!this.unknownBool && d11 != d1) {
					d5 = 0.0D;
					d3 = 0.0D;
					d1 = 0.0D;
				}

				for(i27 = 0; i27 < list18.size(); ++i27) {
					d5 = ((AxisAlignedBB)list18.get(i27)).calculateZOffset(this.boundingBox, d5);
				}

				this.boundingBox.offset(0.0D, 0.0D, d5);
				if(!this.unknownBool && d15 != d5) {
					d5 = 0.0D;
					d3 = 0.0D;
					d1 = 0.0D;
				}

				if(d30 * d30 + d24 * d24 >= d1 * d1 + d5 * d5) {
					d1 = d30;
					d3 = d22;
					d5 = d24;
					this.boundingBox = axisAlignedBB29.copy();
				} else {
					this.ySize = (float)((double)this.ySize + 0.5D);
				}
			}

			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			this.isCollidedHorizontally = d11 != d1 || d15 != d5;
			this.onGround = d13 != d3 && d13 < 0.0D;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.fall(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(d3 < 0.0D) {
				this.fallDistance = (float)((double)this.fallDistance - d3);
			}

			if(d11 != d1) {
				this.motionX = 0.0D;
			}

			if(d13 != d3) {
				this.motionY = 0.0D;
			}

			if(d15 != d5) {
				this.motionZ = 0.0D;
			}

			d30 = this.posX - d7;
			d22 = this.posZ - d9;
			this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d30 * d30 + d22 * d22) * 0.6D);
			if(this.entityWalks) {
				int i31 = MathHelper.floor_double(this.posX);
				int i25 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
				i19 = MathHelper.floor_double(this.posZ);
				i27 = this.worldObj.getBlockId(i31, i25, i19);
				if(this.distanceWalkedModified > (float)this.nextStepDistance && i27 > 0) {
					++this.nextStepDistance;
					StepSound stepSound26 = Block.blocksList[i27].stepSound;
					if(!Block.blocksList[i27].blockMaterial.getIsLiquid()) {
						this.worldObj.playSoundAtEntity(this, stepSound26.getStepSound(), stepSound26.stepSoundVolume * 0.15F, stepSound26.stepSoundPitch);
					}

					Block.blocksList[i27].onEntityWalking(this.worldObj, i31, i25, i19);
				}
			}

			this.ySize *= 0.4F;
			boolean z32 = this.handleWaterMovement();
			if(this.worldObj.isBoundingBoxBurning(this.boundingBox)) {
				this.dealFireDamage(1);
				if(!z32) {
					++this.fire;
					if(this.fire == 0) {
						this.fire = 300;
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(z32 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

		}
	}

	protected void dealFireDamage(int i1) {
		this.attackEntityFrom((Entity)null, 1);
	}

	protected void fall(float f1) {
	}

	public final boolean handleWaterMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D), Material.water);
	}

	public final boolean isInsideOfMaterial() {
		int i1;
		return (i1 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + (double)this.getEyeHeight()), MathHelper.floor_double(this.posZ))) != 0 ? Block.blocksList[i1].blockMaterial == Material.water : false;
	}

	protected float getEyeHeight() {
		return 0.0F;
	}

	public final boolean handleLavaMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D), Material.lava);
	}

	public final void moveFlying(float f1, float f2, float f3) {
		float f4;
		if((f4 = MathHelper.sqrt_float(f1 * f1 + f2 * f2)) >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = f3 / f4;
			f1 *= f4;
			f2 *= f4;
			f3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			f4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += (double)(f1 * f4 - f2 * f3);
			this.motionZ += (double)(f2 * f4 + f1 * f3);
		}
	}

	public float getEntityBrightness(float f1) {
		int i4 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.posY + (double)(this.yOffset / 2.0F));
		int i3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBrightness(i4, i2, i3);
	}

	public final void setLocationAndAngles(double d1, double d3, double d5, float f7, float f8) {
		this.prevPosX = this.posX = d1;
		this.prevPosY = this.posY = d3 + (double)this.yOffset;
		this.prevPosZ = this.posZ = d5;
		this.rotationYaw = f7;
		this.rotationPitch = f8;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public final double getDistanceSqToEntity(Entity entity1) {
		double d2 = this.posX - entity1.posX;
		double d4 = this.posY - entity1.posY;
		double d6 = this.posZ - entity1.posZ;
		return d2 * d2 + d4 * d4 + d6 * d6;
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer1) {
	}

	public final void applyEntityCollision(Entity entity1) {
		double d2 = entity1.posX - this.posX;
		double d4 = entity1.posZ - this.posZ;
		double d6;
		if((d6 = d2 * d2 + d4 * d4) >= (double)0.01F) {
			d6 = (double)MathHelper.sqrt_double(d6);
			d2 /= d6;
			d4 /= d6;
			d2 /= d6;
			d4 /= d6;
			d2 *= (double)0.05F;
			d4 *= (double)0.05F;
			this.addVelocity(-d2, 0.0D, -d4);
			entity1.addVelocity(d2, 0.0D, d4);
		}

	}

	private void addVelocity(double d1, double d3, double d5) {
		this.motionX += d1;
		this.motionY = this.motionY;
		this.motionZ += d5;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public String getEntityTexture() {
		return null;
	}

	public final boolean addEntityID(NBTTagCompound nBTTagCompound1) {
		String string2 = EntityList.getEntityString(this);
		if(!this.isDead && string2 != null) {
			nBTTagCompound1.setString("id", string2);
			this.writeToNBT(nBTTagCompound1);
			return true;
		} else {
			return false;
		}
	}

	public final void writeToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setTag("Pos", newDoubleNBTList(new double[]{this.posX, this.posY, this.posZ}));
		nBTTagCompound1.setTag("Motion", newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
		float[] f2 = new float[]{this.rotationYaw, this.rotationPitch};
		NBTTagList nBTTagList3 = new NBTTagList();
		int i4 = (f2 = f2).length;

		for(int i5 = 0; i5 < i4; ++i5) {
			float f6 = f2[i5];
			nBTTagList3.setTag(new NBTTagFloat(f6));
		}

		nBTTagCompound1.setTag("Rotation", nBTTagList3);
		nBTTagCompound1.setFloat("FallDistance", this.fallDistance);
		nBTTagCompound1.setShort("Fire", (short)this.fire);
		nBTTagCompound1.setShort("Air", (short)this.air);
		this.writeEntityToNBT(nBTTagCompound1);
	}

	public final void readFromNBT(NBTTagCompound nBTTagCompound1) {
		NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("Pos");
		NBTTagList nBTTagList3 = nBTTagCompound1.getTagList("Motion");
		NBTTagList nBTTagList4 = nBTTagCompound1.getTagList("Rotation");
		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)nBTTagList2.tagAt(0)).doubleValue;
		this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)nBTTagList2.tagAt(1)).doubleValue;
		this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)nBTTagList2.tagAt(2)).doubleValue;
		this.motionX = ((NBTTagDouble)nBTTagList3.tagAt(0)).doubleValue;
		this.motionY = ((NBTTagDouble)nBTTagList3.tagAt(1)).doubleValue;
		this.motionZ = ((NBTTagDouble)nBTTagList3.tagAt(2)).doubleValue;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)nBTTagList4.tagAt(0)).floatValue;
		this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)nBTTagList4.tagAt(1)).floatValue;
		this.fallDistance = nBTTagCompound1.getFloat("FallDistance");
		this.fire = nBTTagCompound1.getShort("Fire");
		this.air = nBTTagCompound1.getShort("Air");
		this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		this.readEntityFromNBT(nBTTagCompound1);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound nBTTagCompound1);

	protected abstract void writeEntityToNBT(NBTTagCompound nBTTagCompound1);

	private static NBTTagList newDoubleNBTList(double... d0) {
		NBTTagList nBTTagList1 = new NBTTagList();
		int i2 = (d0 = d0).length;

		for(int i3 = 0; i3 < i2; ++i3) {
			double d5 = d0[i3];
			nBTTagList1.setTag(new NBTTagDouble(d5));
		}

		return nBTTagList1;
	}

	public final EntityItem dropItemWithOffset(int i1, int i2) {
		return this.entityDropItem(i1, 1, 0.0F);
	}

	public final EntityItem entityDropItem(int i1, int i2, float f3) {
		EntityItem entityItem4;
		(entityItem4 = new EntityItem(this.worldObj, this.posX, this.posY + (double)f3, this.posZ, new ItemStack(i1, i2))).delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(entityItem4);
		return entityItem4;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}
}
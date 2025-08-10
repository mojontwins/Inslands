package net.minecraft.world.entity.item;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;

public class EntityBoat extends Entity {
	public int boatCurrentDamage;
	public int boatTimeSinceHit;
	public int boatRockDirection;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatZ;
	private double boatYaw;
	private double boatPitch;
	private double prevMotionX;
	private double prevMotionY;
	private double prevMotionZ;
	public boolean fireResistant = false;

	public EntityBoat(World world1) {
		super(world1);
		this.boatCurrentDamage = 0;
		this.boatTimeSinceHit = 0;
		this.boatRockDirection = 1;
		this.preventEntitySpawning = true;
		this.setSize(1.5F, 0.6F);
		this.yOffset = this.height / 2.0F;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
	}

	public AxisAlignedBB getCollisionBox(Entity entity1) {
		return entity1.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	public boolean canBePushed() {
		return true;
	}

	public EntityBoat(World world1, double d2, double d4, double d6, boolean fireResistant) {
		this(world1);
		this.setPosition(d2, d4 + (double)this.yOffset, d6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = d2;
		this.prevPosY = d4;
		this.prevPosZ = d6;
		this.fireResistant = this.isImmuneToFire = fireResistant;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.0D - (double)0.3F;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		if(!this.worldObj.isRemote && !this.isDead) {
			this.boatRockDirection = -this.boatRockDirection;
			this.boatTimeSinceHit = 10;
			this.boatCurrentDamage += i2 * 10;
			this.setBeenAttacked();
			if(this.boatCurrentDamage > 40) {
				if(this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}

				/*
				int i3;
				for(i3 = 0; i3 < 3; ++i3) {
					this.dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
				}

				for(i3 = 0; i3 < 2; ++i3) {
					this.dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
				}
				*/
				if(this.fireResistant) {
					this.dropItemWithOffset(Item.boat_iron.shiftedIndex, 1, 0.0F);
				} else {
					this.dropItemWithOffset(Item.boat.shiftedIndex, 1, 0.0F);
				}

				this.setEntityDead();
			}

			return true;
		} else {
			return true;
		}
	}

	public void performHurtAnimation() {
		this.boatRockDirection = -this.boatRockDirection;
		this.boatTimeSinceHit = 10;
		this.boatCurrentDamage += this.boatCurrentDamage * 10;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void setPositionAndRotation2(double d1, double d3, double d5, float f7, float f8, int i9) {
		this.boatX = d1;
		this.boatY = d3;
		this.boatZ = d5;
		this.boatYaw = (double)f7;
		this.boatPitch = (double)f8;
		this.boatPosRotationIncrements = i9 + 4;
		this.motionX = this.prevMotionX;
		this.motionY = this.prevMotionY;
		this.motionZ = this.prevMotionZ;
	}

	public void setVelocity(double d1, double d3, double d5) {
		this.prevMotionX = this.motionX = d1;
		this.prevMotionY = this.motionY = d3;
		this.prevMotionZ = this.motionZ = d5;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.boatTimeSinceHit > 0) {
			--this.boatTimeSinceHit;
		}

		if(this.boatCurrentDamage > 0) {
			--this.boatCurrentDamage;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		byte b1 = 5;
		double d2 = 0.0D;

		for(int i4 = 0; i4 < b1; ++i4) {
			double d5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i4 + 0) / (double)b1 - 0.125D;
			double d7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(i4 + 1) / (double)b1 - 0.125D;
			AxisAlignedBB axisAlignedBB9 = AxisAlignedBB.getBoundingBoxFromPool(this.boundingBox.minX, d5, this.boundingBox.minZ, this.boundingBox.maxX, d7, this.boundingBox.maxZ);
			if(
					this.worldObj.isAABBInMaterial(axisAlignedBB9, Material.water) || 
					(this.fireResistant && (this.worldObj.isAABBInMaterial(axisAlignedBB9, Material.lava) || 
					this.worldObj.isAABBInMaterial(axisAlignedBB9, Material.acid)))) {
				d2 += 1.0D / (double)b1;
			}
		}

		double d6;
		double d8;
		double d10;
		double d21;
		if(this.worldObj.isRemote) {
			if(this.boatPosRotationIncrements > 0) {
				d21 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
				d6 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
				d8 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;

				for(d10 = this.boatYaw - (double)this.rotationYaw; d10 < -180.0D; d10 += 360.0D) {
				}

				while(d10 >= 180.0D) {
					d10 -= 360.0D;
				}

				this.rotationYaw = (float)((double)this.rotationYaw + d10 / (double)this.boatPosRotationIncrements);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(d21, d6, d8);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				d21 = this.posX + this.motionX;
				d6 = this.posY + this.motionY;
				d8 = this.posZ + this.motionZ;
				this.setPosition(d21, d6, d8);
				if(this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.motionX *= (double)0.99F;
				this.motionY *= (double)0.95F;
				this.motionZ *= (double)0.99F;
			}

		} else {
			if(d2 < 1.0D) {
				d21 = d2 * 2.0D - 1.0D;
				this.motionY += (double)0.04F * d21;
			} else {
				if(this.motionY < 0.0D) {
					this.motionY /= 2.0D;
				}

				this.motionY += 0.007000000216066837D;
			}

			if(this.riddenByEntity != null) {
				this.motionX += this.riddenByEntity.motionX * 0.2D;
				this.motionZ += this.riddenByEntity.motionZ * 0.2D;
			}

			d21 = 0.5D;
			if(this.motionX < -d21) {
				this.motionX = -d21;
			}

			if(this.motionX > d21) {
				this.motionX = d21;
			}

			if(this.motionZ < -d21) {
				this.motionZ = -d21;
			}

			if(this.motionZ > d21) {
				this.motionZ = d21;
			}

			if(this.onGround) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			if(d6 > 0.15D) {
				d8 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D);
				d10 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D);

				for(int i12 = 0; (double)i12 < 1.0D + d6 * 60.0D; ++i12) {
					double d13 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
					double d15 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
					double d17;
					double d19;
					if(this.rand.nextBoolean()) {
						d17 = this.posX - d8 * d13 * 0.8D + d10 * d15;
						d19 = this.posZ - d10 * d13 * 0.8D - d8 * d15;
						this.worldObj.spawnParticle("splash", d17, this.posY - 0.125D, d19, this.motionX, this.motionY, this.motionZ);
					} else {
						d17 = this.posX + d8 + d10 * d13 * 0.7D;
						d19 = this.posZ + d10 - d8 * d13 * 0.7D;
						this.worldObj.spawnParticle("splash", d17, this.posY - 0.125D, d19, this.motionX, this.motionY, this.motionZ);
					}
				}
			}

			if(this.isCollidedHorizontally && d6 > 0.2D) {
				if(!this.worldObj.isRemote) {
					this.setEntityDead();

					/*
					int i22;
					for(i22 = 0; i22 < 3; ++i22) {
						this.dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
					}

					for(i22 = 0; i22 < 2; ++i22) {
						this.dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
					}
					*/
					this.dropItemWithOffset(Item.boat.shiftedIndex, 1, 0.0F);
				}
			} else {
				this.motionX *= (double)0.99F;
				this.motionY *= (double)0.95F;
				this.motionZ *= (double)0.99F;
			}

			this.rotationPitch = 0.0F;
			d8 = (double)this.rotationYaw;
			d10 = this.prevPosX - this.posX;
			double d23 = this.prevPosZ - this.posZ;
			if(d10 * d10 + d23 * d23 > 0.001D) {
				d8 = (double)((float)(Math.atan2(d23, d10) * 180.0D / Math.PI));
			}

			double d14;
			for(d14 = d8 - (double)this.rotationYaw; d14 >= 180.0D; d14 -= 360.0D) {
			}

			while(d14 < -180.0D) {
				d14 += 360.0D;
			}

			if(d14 > 20.0D) {
				d14 = 20.0D;
			}

			if(d14 < -20.0D) {
				d14 = -20.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + d14);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			List<Entity> list16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
			int i24;
			if(list16 != null && list16.size() > 0) {
				for(i24 = 0; i24 < list16.size(); ++i24) {
					Entity entity18 = (Entity)list16.get(i24);
					if(entity18 != this.riddenByEntity && entity18.canBePushed() && entity18 instanceof EntityBoat) {
						entity18.applyEntityCollision(this);
					}
				}
			}

			for(int xz = 0; xz < 4; ++xz) {
				int x = MathHelper.floor_double(this.posX + ((double)(xz % 2) - 0.5D) * 0.8D);
				int z = MathHelper.floor_double(this.posZ + ((double)(xz / 2) - 0.5D) * 0.8D);
				
				for(int y0 = 0; y0 < 2; y0 ++) {
					int y = MathHelper.floor_double(this.posY) + y0;
					
					int blockID = this.worldObj.getBlockId(x, y, z);
					int meta = this.worldObj.getBlockMetadata(x, y, z);
					
					if(blockID == Block.snow.blockID) {
						this.worldObj.setBlockWithNotify(x, y, z, 0);
					} else if(blockID == Block.lilyPad.blockID) { 
						this.worldObj.playAuxSFX(2001, x, y, z, blockID);
						Block.blocksList[blockID].dropBlockAsItem(this.worldObj, x, y, z, meta);
						this.worldObj.setBlockWithNotify(x, y, z, 0);
					}
				}
			}

			if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
				this.riddenByEntity = null;
			}

		}
	}

	public void updateRiderPosition() {
		if(this.riddenByEntity != null) {
			double d1 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			double d3 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			this.riddenByEntity.setPosition(this.posX + d1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d3);
		}
	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setBoolean("FireResistant", this.fireResistant);
	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.fireResistant = nBTTagCompound1.getBoolean("FireResistant");
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public boolean interact(EntityPlayer entityPlayer1) {
		if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != entityPlayer1) {
			return true;
		} else {
			if(!this.worldObj.isRemote) {
				entityPlayer1.mountEntity(this);
			}

			return true;
		}
	}
}

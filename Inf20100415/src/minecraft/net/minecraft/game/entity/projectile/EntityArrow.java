package net.minecraft.game.entity.projectile;

import com.mojang.nbt.NBTTagCompound;

import java.util.List;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;

import util.MathHelper;

public class EntityArrow extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private boolean inGround = false;
	public int arrowShake = 0;
	private EntityLiving shootingEntity;
	private int ticksInGround;
	private int ticksInAir = 0;

	public EntityArrow(World world1, EntityLiving entityLiving2) {
		super(world1);
		this.shootingEntity = entityLiving2;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entityLiving2.posX, entityLiving2.posY, entityLiving2.posZ, entityLiving2.rotationYaw, entityLiving2.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= (double)0.1F;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	public final void setArrowHeading(double d1, double d3, double d5, float f7, float f8) {
		float f9 = MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
		d1 /= (double)f9;
		d3 /= (double)f9;
		d5 /= (double)f9;
		d1 += this.rand.nextGaussian() * (double)0.0075F * (double)f8;
		d3 += this.rand.nextGaussian() * (double)0.0075F * (double)f8;
		d5 += this.rand.nextGaussian() * (double)0.0075F * (double)f8;
		d1 *= (double)f7;
		d3 *= (double)f7;
		d5 *= (double)f7;
		this.motionX = d1;
		this.motionY = d3;
		this.motionZ = d5;
		f7 = MathHelper.sqrt_double(d1 * d1 + d5 * d5);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d1, d5) * 180.0D / (double)(float)Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d3, (double)f7) * 180.0D / (double)(float)Math.PI);
		this.ticksInGround = 0;
	}

	public final void onUpdate() {
		super.onUpdate();
		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inGround) {
			if(this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile) == this.inTile) {
				++this.ticksInGround;
				if(this.ticksInGround == 1200) {
					super.isDead = true;
				}

				return;
			}

			this.inGround = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
		}

		Vec3D vec3D1 = new Vec3D(this.posX, this.posY, this.posZ);
		Vec3D vec3D2 = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingObjectPosition3 = this.worldObj.rayTraceBlocks(vec3D1, vec3D2);
		vec3D1 = new Vec3D(this.posX, this.posY, this.posZ);
		vec3D2 = new Vec3D(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(movingObjectPosition3 != null) {
			vec3D2 = new Vec3D(movingObjectPosition3.hitVec.xCoord, movingObjectPosition3.hitVec.yCoord, movingObjectPosition3.hitVec.zCoord);
		}

		Entity entity4 = null;
		List list5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d6 = 0.0D;

		for(int i8 = 0; i8 < list5.size(); ++i8) {
			Entity entity9;
			MovingObjectPosition movingObjectPosition10;
			double d12;
			if((entity9 = (Entity)list5.get(i8)).canBeCollidedWith() && (entity9 != this.shootingEntity || this.ticksInAir >= 5) && (movingObjectPosition10 = entity9.boundingBox.expand((double)0.3F, (double)0.3F, (double)0.3F).calculateIntercept(vec3D1, vec3D2)) != null && ((d12 = vec3D1.distance(movingObjectPosition10.hitVec)) < d6 || d6 == 0.0D)) {
				entity4 = entity9;
				d6 = d12;
			}
		}

		if(entity4 != null) {
			movingObjectPosition3 = new MovingObjectPosition(entity4);
		}

		float f14;
		if(movingObjectPosition3 != null) {
			if(movingObjectPosition3.entityHit != null) {
				if(movingObjectPosition3.entityHit.attackEntityFrom(this, 4)) {
					this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					super.isDead = true;
				} else {
					this.motionX *= -0.10000000149011612D;
					this.motionY *= -0.10000000149011612D;
					this.motionZ *= -0.10000000149011612D;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.ticksInAir = 0;
				}
			} else {
				this.xTile = movingObjectPosition3.blockX;
				this.yTile = movingObjectPosition3.blockY;
				this.zTile = movingObjectPosition3.blockZ;
				this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
				this.motionX = (double)((float)(movingObjectPosition3.hitVec.xCoord - this.posX));
				this.motionY = (double)((float)(movingObjectPosition3.hitVec.yCoord - this.posY));
				this.motionZ = (double)((float)(movingObjectPosition3.hitVec.zCoord - this.posZ));
				f14 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / (double)f14 * (double)0.05F;
				this.posY -= this.motionY / (double)f14 * (double)0.05F;
				this.posZ -= this.motionZ / (double)f14 * (double)0.05F;
				this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				this.inGround = true;
				this.arrowShake = 7;
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		f14 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f14) * 180.0D / (double)(float)Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f15 = 0.99F;
		if(this.handleWaterMovement()) {
			for(int i16 = 0; i16 < 4; ++i16) {
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
			}

			f15 = 0.8F;
		}

		this.motionX *= (double)f15;
		this.motionY *= (double)f15;
		this.motionZ *= (double)f15;
		this.motionY -= (double)0.03F;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("xTile", (short)this.xTile);
		nBTTagCompound1.setShort("yTile", (short)this.yTile);
		nBTTagCompound1.setShort("zTile", (short)this.zTile);
		nBTTagCompound1.setByte("inTile", (byte)this.inTile);
		nBTTagCompound1.setByte("shake", (byte)this.arrowShake);
		nBTTagCompound1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
	}

	public final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.xTile = nBTTagCompound1.getShort("xTile");
		this.yTile = nBTTagCompound1.getShort("yTile");
		this.zTile = nBTTagCompound1.getShort("zTile");
		this.inTile = nBTTagCompound1.getByte("inTile") & 255;
		this.arrowShake = nBTTagCompound1.getByte("shake") & 255;
		this.inGround = nBTTagCompound1.getByte("inGround") == 1;
	}

	public final void onCollideWithPlayer(EntityPlayer entityPlayer1) {
		if(this.inGround && this.shootingEntity == entityPlayer1 && this.arrowShake <= 0 && entityPlayer1.inventory.storePartialItemStack(new ItemStack(Item.arrow.shiftedIndex, 1))) {
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			entityPlayer1.onItemPickup(this);
			super.isDead = true;
		}

	}
}
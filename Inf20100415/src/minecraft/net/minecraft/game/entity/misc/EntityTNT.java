package net.minecraft.game.entity.misc;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;

import util.MathHelper;

public class EntityTNT extends Entity {
	public int fuse = 0;

	public EntityTNT(World world1, float f2, float f3, float f4) {
		super(world1);
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition((double)f2, (double)f3, (double)f4);
		float f5 = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.motionX = (double)(-MathHelper.sin(f5 * (float)Math.PI / 180.0F) * 0.02F);
		this.motionY = (double)0.2F;
		this.motionZ = (double)(-MathHelper.cos(f5 * (float)Math.PI / 180.0F) * 0.02F);
		this.entityWalks = false;
		this.fuse = 80;
		this.prevPosX = (double)f2;
		this.prevPosY = (double)f3;
		this.prevPosZ = (double)f4;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
			this.motionY *= -0.5D;
		}

		if(this.fuse-- <= 0) {
			super.isDead = true;
			this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 4.0F);
		} else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}
	}

	protected final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setByte("Fuse", (byte)this.fuse);
	}

	protected final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.fuse = nBTTagCompound1.getByte("Fuse");
	}
}
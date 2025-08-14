package net.minecraft.world.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.World;

public class EntityMob extends EntityCreature {
	protected int attackStrength = 2;
	Vec3D homingTo = null;
	
	public EntityMob(World world1) {
		super(world1);
		this.health = 20;
	}

	public void onLivingUpdate() {
		float f1 = this.getEntityBrightness(1.0F);
		if(f1 > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public void onUpdate() {
		super.onUpdate();
		if(!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return entityPlayer1 != null && !entityPlayer1.isCreative && this.canEntityBeSeen(entityPlayer1) ? entityPlayer1 : null;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		if(super.attackEntityFrom(entity1, i2)) {
			if(this.riddenByEntity != entity1 && this.ridingEntity != entity1) {
				if (entity1 instanceof EntityPlayer && ((EntityPlayer) entity1).isCreative) return true;
	
				if(entity1 != this) {
					this.entityToAttack = entity1;
				}

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity entity1, float f2) {
		if(this.attackTime <= 0 && f2 < 2.0F && entity1.boundingBox.maxY > this.boundingBox.minY && entity1.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			entity1.attackEntityFrom(this, this.attackStrength);
		}

	}

	public float getBlockPathWeight(int i1, int i2, int i3) {
		return 0.5F - this.worldObj.getLightBrightness(i1, i2, i3);
	}
	
	public boolean attackEntityAsMob(Entity entity1) {
		return entity1.attackEntityFrom(this, this.attackStrength);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	public boolean getCanSpawnHere() {
		if(this.needsLightCheck()) {
			int i1 = MathHelper.floor_double(this.posX);
			int i2 = MathHelper.floor_double(this.boundingBox.minY);
			int i3 = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i1, i2, i3) > this.rand.nextInt(32)) {
				return false;
			} else {
				int i4 = this.worldObj.getBlockLightValue(i1, i2, i3);
				if(this.worldObj.thundering()) {
					int i5 = this.worldObj.skylightSubtracted;
					this.worldObj.skylightSubtracted = 10;
					i4 = this.worldObj.getBlockLightValue(i1, i2, i3);
					this.worldObj.skylightSubtracted = i5;
				}
	
				return i4 <= this.rand.nextInt(8) && super.getCanSpawnHere();
			}
		} else {
			return super.getCanSpawnHere();
		}
	}
	
	public boolean needsLightCheck() {
		return true;
	}
	
	public int getFullHealth() {
		return 20;
	}
}

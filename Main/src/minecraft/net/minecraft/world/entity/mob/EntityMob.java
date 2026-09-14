package net.minecraft.world.entity.mob;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

public class EntityMob extends EntityCreature {
	protected int attackStrength = 2;
	protected Vec3D homingTo = null;
	
	public EntityMob(World world) {
		super(world);
		this.health = 20;
	}

	public void onLivingUpdate() {
		float brightness = this.getEntityBrightness(1.0F);
		if(brightness > 0.5F) {
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
		EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return player != null && !player.isCreative && this.canEntityBeSeen(player) ? player : null;
	}

	public boolean attackEntityFrom(Entity source, int damage) {
		if(super.attackEntityFrom(source, damage)) {
			if(this.riddenByEntity != source && this.ridingEntity != source) {
				if (source instanceof EntityPlayer && ((EntityPlayer) source).isCreative) return true;

				if(source != this) {
					this.entityToAttack = source;
				}

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity target, float distance) {
		if(this.attackTime <= 0 && distance < 2.0F && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			target.attackEntityFrom(this, this.attackStrength);
		}

	}

	public float getBlockPathWeight(int x, int y, int z) {
		return 0.5F - this.worldObj.getLightBrightness(x, y, z);
	}
	
	public boolean attackEntityAsMob(Entity target) {
		return target.attackEntityFrom(this, this.attackStrength);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	public boolean getCanSpawnHere() {
		if(this.needsLightCheck()) {
			int x = MathHelper.floor_double(this.posX);
			int y = MathHelper.floor_double(this.boundingBox.minY);
			int z = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) > this.rand.nextInt(32)) {
				return false;
			} else {
				int blockLight = this.worldObj.getBlockLightValue(x, y, z);
				if(this.worldObj.thundering()) {
					int savedSkylight = this.worldObj.skylightSubtracted;
					this.worldObj.skylightSubtracted = 10;
					blockLight = this.worldObj.getBlockLightValue(x, y, z);
					this.worldObj.skylightSubtracted = savedSkylight;
				}
	
				return blockLight <= this.rand.nextInt(8) && super.getCanSpawnHere();
			}
		} else {
			return super.getCanSpawnHere();
		}
	}
	
	public boolean needsLightCheck() {
		return true;
	}
	
	public boolean isValidTarget(EntityLiving target) {
		if(target == null) return false;
		
		if(target == this) return false;
		
		if(!target.isEntityAlive()) return false;
		
		if(target.boundingBox.minY >= this.boundingBox.maxY || target.boundingBox.maxY <= this.boundingBox.minY) return false;
		
		return true;
	}
	
	public int getFullHealth() {
		return 20;
	}
}

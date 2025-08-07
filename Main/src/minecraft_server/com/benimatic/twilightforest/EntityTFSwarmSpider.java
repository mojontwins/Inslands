package com.benimatic.twilightforest;

import com.mojontwins.minecraft.entity.status.Status;
import com.mojontwins.minecraft.entity.status.StatusEffect;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySpiderBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityTFSwarmSpider extends EntitySpiderBase {
	public EntityTFSwarmSpider(World world) {
		this(world, true);
	}

	public EntityTFSwarmSpider(World world, boolean spawnMore) {
		super(world);
		this.setSize(0.7F, 0.4F);
		this.attackStrength = 0;
		this.setSpawnMore(spawnMore);
		this.texture = "/mob/swarmspider.png";
		this.health = 3;
	}

	public int getFullHealth() {
		return 3;
	}

	public float spiderScaleAmount() {
		return 0.6F;
	}

	public void onUpdate() {
		if(this.shouldSpawnMore()) {
			if(!this.worldObj.isRemote) {
				int more = 1 + this.rand.nextInt(2);
	
				for(int i = 0; i < more; ++i) {
					if(!this.spawnAnother()) {
						this.spawnAnother();
					}
				}
			}

			this.setSpawnMore(false);
		}

		super.onUpdate();
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(17, (byte)0);
	}

	protected void attackEntity(Entity entity, float f) {
		this.attackStrength = !this.onGround && this.rand.nextInt(4) == 0 ? 1 : 0;
		if(this.attackStrength > 0 && entity instanceof EntityLiving) {
			if(!((EntityLiving)entity).isStatusActive(Status.statusPoisoned)) {
				((EntityLiving)entity).addStatusEffect(new StatusEffect(Status.statusPoisoned.id, 40, 1));
			}
		}
		super.attackEntity(entity, f);
	}

	protected boolean spawnAnother() {
		/// TODO : THIS IS PROBEMATIC !!! FIX
		EntityTFSwarmSpider another = new EntityTFSwarmSpider(this.worldObj, false);
		double sx = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
		double sy = this.posY + (double)this.rand.nextInt(3) - 1.0D;
		double sz = this.posZ + (this.worldObj.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
		another.setLocationAndAngles(sx, sy, sz, this.rand.nextFloat() * 360.0F, 0.0F);
		if(!another.getCanSpawnHere()) {
			return false;
		} else {
			this.worldObj.spawnEntityInWorld(another);
			return true;
		}
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}

	public boolean shouldSpawnMore() {
		return (this.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
	}

	public void setSpawnMore(boolean flag) {
		byte byte0 = this.dataWatcher.getWatchableObjectByte(17);
		if(flag) {
			this.dataWatcher.updateObject(17, (byte)(byte0 | 1));
		} else {
			this.dataWatcher.updateObject(17, (byte)(byte0 & -2));
		}

	}
	
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("SpawnMore", this.shouldSpawnMore());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setSpawnMore(nbttagcompound.getBoolean("SpawnMore"));
	}

}

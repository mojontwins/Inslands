/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.monster.EntityZombie
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.world.World
 */
package com.gw.dm;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mojontwins.minecraft.entity.status.Status;
import com.mojontwins.minecraft.entity.status.StatusEffect;

import net.minecraft.src.AttackableTargetSorter;
import net.minecraft.src.Block;
import net.minecraft.src.BlockDoor;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICaveMob;
import net.minecraft.src.ISentient;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PathEntity;
import net.minecraft.src.PathPoint;
import net.minecraft.src.World;

public class EntityGhoul extends EntityMob implements ICaveMob {
    public boolean ignoreHeight;
	protected int doorPosX;
	protected int doorPosY;
	protected int doorPosZ;
	
	protected float distanceX;
	protected float distanceZ;
	
	protected BlockDoor targetDoor = null;
	protected boolean chasingDoor = false;
	
	protected int doorBreakTime;

    public EntityGhoul(World par1World) {
        super(par1World);
        this.health = this.getFullHealth();
        this.texture = "/mob/ghoul.png";
    }

    protected String getLivingSound() {
        return "mob.g_l";
    }

    protected String getHurtSound() {
        return "mob.g_h";
    }

    protected String getDeathSound() {
        return "mob.g_d";
    }

    public int getAttackStrength(Entity par1Entity) {
        int var3 = 6;
        return var3;
    }

    public boolean attackEntityAsMob(Entity par1) {
        this.getAttackTarget().addStatusEffect(new StatusEffect(Status.statusSlowness.id, 40, 1));
        return super.attackEntityAsMob(par1);
    }

    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere();
    }

    public void setIgnoreHeight(boolean par1) {
        this.ignoreHeight = par1;
    }
    
	@Override
	public void onLivingUpdate() {
		if(this.worldObj.isDaytime() && this.burnsOnDaylight()) {
			float f1 = this.getEntityBrightness(1.0F);
			if(f1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f1 - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		// Door interaction
		if (!this.chasingDoor) {
			if(this.worldObj.worldInfo.isBloodMoon() || rand.nextInt(4) == 0) {
				if(this.isCollidedHorizontally) {
					PathEntity pathEntity = this.pathToEntity;
					if(pathEntity != null && !pathEntity.isFinished()) {
						for (int i = 0; i < Math.min(pathEntity.getCurrentPathIndex() + 2, pathEntity.pathLength); i++) {
							PathPoint pathPoint = pathEntity.getPathPointFromIndex(i);
							int dpx = pathPoint.xCoord;
							int dpy = pathPoint.yCoord;
							int dpz = pathPoint.zCoord;
							
							if (this.getDistanceSq(dpx, dpy, dpz) <= 2.25D) {
								this.targetDoor = this.findDoorBlock(dpx, dpy, dpz);
								if (this.targetDoor != null) {
									this.doorPosX = dpx;
									this.doorPosY = dpy;
									this.doorPosZ = dpz;
									
									this.distanceX = (float)this.doorPosX + 0.5F - (float)this.posX;
									this.distanceZ = (float)this.doorPosZ + 0.5F - (float)this.posZ;
									this.chasingDoor = true;
									this.doorBreakTime = 240;
								}
							}
						}
					}		
				}
			}
		} else {
			float f1 = (float)this.doorPosX + 0.5F - (float)this.posX;
			float f2 = (float)this.doorPosZ + 0.5F - (float)this.posZ;
			float f = this.distanceX * f1 + this.distanceZ * f2;
			
			if (f < 0.0F) {
				this.chasingDoor = false;
			}
			
			if(this.rand.nextInt(20) == 0) {
				worldObj.playSoundAtEntity(this, "mob.zombie.wood", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
			
			if(--this.doorBreakTime == 0) {
				this.worldObj.setBlockWithNotify(doorPosX, doorPosY, doorPosZ, 0);
				worldObj.playSoundAtEntity(this, "mob.zombie.woodbreak", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
		super.onLivingUpdate();
	}
	

	protected BlockDoor findDoorBlock(int x, int y, int z) {
		int blockID = this.worldObj.getBlockId(x, y, z);
		if (blockID != Block.doorWood.blockID) return null;
		BlockDoor blockDoor = (BlockDoor)Block.blocksList[blockID];
		return blockDoor;
	}
	
	@Override
	protected Entity findPlayerToAttack() {
			
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D); 
		if(entityPlayer1 != null && !entityPlayer1.isCreative && this.canEntityBeSeen(entityPlayer1)) return entityPlayer1;
		
		// Find close entities of type EntityAmazon, EntityPigman/EntityCowman, etc
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(ISentient.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		Collections.sort(list, new AttackableTargetSorter(this));
		
		// Return closest
		Iterator<Entity> iterator = list.iterator();		
		while(iterator.hasNext()) {
			EntityLiving entityLiving = (EntityLiving)iterator.next();
			if(this.isValidTarget(entityLiving)) return entityLiving;
		}
		
		return null;
	}
	
	public boolean isValidTarget(EntityLiving entityLiving) {
		if(entityLiving == null) return false;
		
		if(entityLiving == this) return false;
		
		if(!entityLiving.isEntityAlive()) return false;
		
		if(entityLiving.boundingBox.minY >= this.boundingBox.maxY || entityLiving.boundingBox.maxY <= this.boundingBox.minY) return false;
		
		return true;
	}
	
	public boolean burnsOnDaylight() {
		return true;
	}
	
	@Override
	protected int getDropItemId() {
		return this.rand.nextInt(6) == 0 ? Item.rottenFlesh.shiftedIndex : Item.egg.shiftedIndex;
	}
	
	@Override
	public int getFullHealth() {
		return 25;
	}

}


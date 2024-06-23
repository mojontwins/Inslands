package com.bigbang87.deadlymonsters;

import java.util.Iterator;
import java.util.List;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public class EntityHauntedCow extends EntityMob {
	/*
	 * Inspired by bigbang87's Haunted Cow from Deadly Monsters.
	 * Coded from scratch (Not that it was very difficult ;) ). 
	 * It uses the original textures.
	 */

	public EntityHauntedCow(World world1) {
		super(world1);
		this.texture = "/mob/hauntedcow.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
		this.scoreValue = 15;
		this.health = this.getFullHealth();
	}

	@Override
	public int getFullHealth() {
		return 50;
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		return super.attackEntityAsMob(entityIn);
	}
	
	@Override
	protected String getLivingSound() {
		return "mob.hauntedcow.idle";
	}

	@Override
	protected String getHurtSound() {
		return "mob.hauntedcow.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.hauntedcow.death";
	}
	
	@Override
	protected Entity findPlayerToAttack() {
		// My haunted cows also chase cows to turn them into haunted cows!
		
		// Find close entities of type EntityCow
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityCow.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		
		Iterator<Entity> iterator = list.iterator();		
		while(iterator.hasNext()) {
			EntityLiving entityLiving = (EntityLiving)iterator.next();
			if(this.isValidTarget(entityLiving)) return entityLiving;
		}
		
		// Then chase the player
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D); 
		if(entityPlayer1 != null && !entityPlayer1.isCreative && this.canEntityBeSeen(entityPlayer1)) return entityPlayer1;
		
		return null;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.fire = 0;
	}
	
	@Override
	public void onStruckByLightning(EntityLightningBolt entityLightningBolt1) {
		
	}

	public boolean isValidTarget(EntityLiving entityLiving) {
		if(entityLiving == null) return false;
		
		if(entityLiving == this) return false;
		
		if(!entityLiving.isEntityAlive()) return false;
			
		return true;
	}
}

package net.minecraft.world.entity.monster;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityThrowableToxicFungus;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.item.Item;

public class EntityFungalCalamity extends EntityMob implements IMob {

	public EntityFungalCalamity(World world1) {
		super(world1);
		this.texture = "/mob/rotero.png";
	}

	@Override
	protected String getLivingSound() {
		return "mob.zombie";
	}

	@Override
	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.zombiedeath";
	}
	

	@Override
	protected int getDropItemId() {
		return Item.bowlSoup.shiftedIndex;
	}
	
	@Override
	public int getFullHealth() {
		return 20;
	}
	
	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		
		if(this.entityToAttack instanceof EntityLiving) {
			EntityLiving living = (EntityLiving)this.entityToAttack;
			if(
					living != null && 
					living.getDistanceSqToEntity(this) < 64.0D &&
					!living.isStatusActive(Status.statusSlowness)) {
				this.moveForward = -this.moveSpeed;
			}
		}
	}
	
	@Override
	protected void attackEntity(Entity victim, float distance) {
		if(victim instanceof EntityLiving) {
			EntityLiving living = (EntityLiving)victim;
			
			if(distance < 12.0F && distance > 4.0F
					&& !living.isStatusActive(Status.statusSlowness)) {
				double dX = living.posX - this.posX;
				double dZ = living.posZ - this.posZ;
				
				if(this.attackTime == 0) {
					EntityThrowableToxicFungus fungalInfection = new EntityThrowableToxicFungus(this.worldObj, this);
					++fungalInfection.posY;
					
					double dy = living.posY + (double)living.getEyeHeight() - (double)0.2F - fungalInfection.posY;
					float f10 = MathHelper.sqrt_double(dX * dX + dZ * dZ) * 0.2F;
					this.worldObj.playSoundAtEntity(this, "\"mob.witch.throw", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
					this.worldObj.spawnEntityInWorld(fungalInfection);
					fungalInfection.setThrowableHeading(dX, dy + (double)f10, dZ, 0.6F, 12.0F);
					this.attackTime = 30;
				}

				this.rotationYaw = (float)(Math.atan2(dZ, dX) * 180.0D / (double)(float)Math.PI) - 90.0F;
				this.hasAttacked = true;
			} else super.attackEntity(victim, distance);
		}
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
	
	@Override
	public boolean needsLightCheck() {
		return true;
	}
	
	@Override
	public boolean getCanSpawnHere() {
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.boundingBox.minY);
		int i3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(i1, i2 - 1, i3) == Block.mycelium.blockID && super.getCanSpawnHere();
	}

}

package net.minecraft.world.entity.sentient;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.AttackableTargetSorter;
import net.minecraft.world.entity.monster.EntityMob;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.projectile.EntityThrowableBottle;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemBottle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class EntityPoisonWitch extends EntityMob implements IMob {

	private static final int spawnCauldronChance = 3;
	
	private int spawnObjectCounter;
	private boolean preparingToDrop = false;

	private ItemStack speakItemStack;
	
	public EntityPoisonWitch(World world1) {
		super(world1);
		this.texture = "/mob/poison_witch.png";
		this.moveSpeed = 1.1F;
		this.health = this.getFullHealth();
		this.speakItemStack = new ItemStack(Item.diamond);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		// Pick up diamonds and maybe drop cauldron
		if(!this.worldObj.isRemote) {
			if(!this.preparingToDrop && this.rand.nextInt(8) == 0) {
				List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox);
	
				Iterator<Entity> iterator = list.iterator();
				while(iterator.hasNext()) {
					EntityItem entityItem = (EntityItem)iterator.next();

					if(entityItem != null && entityItem.item != null && entityItem.item.getItem().shiftedIndex == Item.diamond.shiftedIndex) {
						entityItem.setEntityDead();
						if(rand.nextInt(spawnCauldronChance) == 0) {
							this.spawnObjectCounter = 20;
							this.preparingToDrop = true;
						}
					}
				}
			} 
			
			if(this.preparingToDrop) {
				
				this.spawnObjectCounter --; if (this.spawnObjectCounter <= 0) {
					this.preparingToDrop = false;
					this.entityToAttack = null;
					
					// drop object
					this.dropItem(Block.cauldron.blockID, 1);
				}
			}
		}		
	}
	
	@Override
	public void onDeath(Entity entity) {
		// HA HA
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	
	@Override
	protected void despawnEntity() {
		
	}
	
	@Override
	public boolean attackEntityFrom(Entity entity, int damage) {
		this.entityToAttack = entity;
		return super.attackEntityFrom(entity, damage != 0 ? 1 : 0);
	}
	
	@Override
	public ItemStack getHeldItem() {
		return new ItemStack(Block.cauldron);
	}


	@Override
	protected String getLivingSound() {
		return "";
	}

	@Override
	protected String getHurtSound() {
		return "mob.amazon.hit";
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
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		
		if(!(this.entityToAttack instanceof EntityItem) &&
				this.rand.nextInt(8) == 0) {
			// First look for diamonds around
			List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
			Iterator<Entity> iterator = list.iterator();
			while(iterator.hasNext()) {
				EntityItem entityItem = (EntityItem)iterator.next();
				if(entityItem != null && entityItem.item != null && entityItem.item.getItem().shiftedIndex == Item.diamond.shiftedIndex) {
					this.entityToAttack = entityItem;
				}
			}
		}
		
		if(this.entityToAttack instanceof EntityLiving) {
			EntityLiving living = (EntityLiving)this.entityToAttack;
			if(
					living != null && 
					living.getDistanceSqToEntity(this) < 36.0D &&
					!living.isStatusActive(Status.statusSlowness)) {
				this.moveForward = -this.moveSpeed;
			}
		}
	}
	
	@Override
	protected void attackEntity(Entity victim, float distance) {
		if(victim instanceof EntityItem) {
		} else if(victim instanceof EntityLiving) {
			EntityLiving living = (EntityLiving)victim;
			
			if(distance < 12.0F && distance > 4.0F
					&& !living.isStatusActive(Status.statusSlowness)) {
				double dX = living.posX - this.posX;
				double dZ = living.posZ - this.posZ;
				
				if(this.attackTime == 0) {
					EntityThrowableBottle bottle = new EntityThrowableBottle(this.worldObj, this, (ItemBottle)Item.bottleAcid);
					++bottle.posY;
					
					double dy = living.posY + (double)living.getEyeHeight() - (double)0.2F - bottle.posY;
					float f10 = MathHelper.sqrt_double(dX * dX + dZ * dZ) * 0.2F;
					this.worldObj.playSoundAtEntity(this, "\"mob.witch.throw", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
					this.worldObj.spawnEntityInWorld(bottle);
					bottle.setThrowableHeading(dX, dy + (double)f10, dZ, 0.6F, 12.0F);
					this.attackTime = 30;
				}

				this.rotationYaw = (float)(Math.atan2(dZ, dX) * 180.0D / (double)(float)Math.PI) - 90.0F;
				this.hasAttacked = true;
			} else super.attackEntity(victim, distance);
		} else super.attackEntity(victim, distance);
	}
	
	@Override
	protected Entity findPlayerToAttack() {

		// First look for diamonds around
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		Iterator<Entity> iterator = list.iterator();
		while(iterator.hasNext()) {
			EntityItem entityItem = (EntityItem)iterator.next();
			if(entityItem != null && entityItem.item != null && entityItem.item.getItem().shiftedIndex == Item.diamond.shiftedIndex) {
				return entityItem;
			}
		}
		
		// Find close entities of type EntityZombie
		list = this.worldObj.getEntitiesWithinAABB(EntityZombie.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		Collections.sort(list, new AttackableTargetSorter(this));
		
		// Return closest
		iterator = list.iterator();		
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
		return false;
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return true;
	}
	
	@Override
	public boolean isSpeaking() {
		return this.attackTarget == null || !(this.attackTarget instanceof EntityCreature);
	}
	
	@Override
	public ItemStack getSpeech() {
		return this.speakItemStack;
	}
}

package net.minecraft.world.entity.monster;

import java.util.Iterator;
import java.util.List;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.player.EntityPlayer;

public class EntityIceSkeleton extends EntitySkeleton implements IMob {
	public EntityIceSkeleton(World world1) {
		super(world1);
		this.texture = "/mob/ice_skeleton.png";
		this.inventory.setArmorItemInSlot(3, new ItemStack(Item.helmetRags));
		this.inventory.setArmorItemInSlot(2, new ItemStack(Item.plateRags));
		this.inventory.setArmorItemInSlot(1, new ItemStack(Item.legsRags));
	}
	
	@Override
	protected int getDropItemId() {
		if(this.rand.nextInt(10) == 0) {
			switch(this.rand.nextInt(4)) {
				case 0: return Item.helmetRags.shiftedIndex;
				case 1: return Item.plateRags.shiftedIndex;
				case 2: return Item.legsRags.shiftedIndex;
			}
		}
		return this.rand.nextInt(3) == 0 ? Item.bone.shiftedIndex : Item.arrow.shiftedIndex;
	}
	
	@Override
	public boolean burnsInWinter() {
		return false;
	}
	
	@Override
	public int getFullHealth() {
		return 30;
	}
	
	@Override
	public void onDeath(Entity entity) {
		if(entity instanceof EntityPlayer) {
			//((EntityPlayer) entity).triggerAchievement(AchievementList.iceKiller);
		}
		super.onDeath(entity);
	}
	
	@Override
	protected Entity findPlayerToAttack() {
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		if(entityPlayer1 != null && !entityPlayer1.isCreative && this.canEntityBeSeen(entityPlayer1)) return entityPlayer1;
		
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
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
}

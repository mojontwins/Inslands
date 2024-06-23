package net.minecraft.src;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EntityHusk extends EntityZombie {
	
	public EntityHusk(World world) {
		super(world);
		this.texture = "/mob/husk1.png";
		this.texturePrefix = "husk";
		this.moveSpeed = 0.6F;
		this.attackStrength = 6;
		this.scoreValue = 25;
	}
	
	protected int getMaxTextureVariations() {
		return 3;
	}

	@Override
	protected Entity findPlayerToAttack() {
		// Find close entities of type EntityMeatBlock
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityMeatBlock.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		Collections.sort(list, new AttackableTargetSorter(this));
		if(list.size() > 0) return list.get(0);
		
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D); 
		if(entityPlayer1 != null && !entityPlayer1.isCreative && this.canEntityBeSeen(entityPlayer1)) return entityPlayer1;
		
		// Find close entities of type EntityAmazon, EntityPigman/EntityCowman, etc
		list = this.worldObj.getEntitiesWithinAABB(ISentient.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		list.addAll(this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D)));
		Collections.sort(list, new AttackableTargetSorter(this));
		
		// Return closest
		Iterator<Entity> iterator = list.iterator();		
		while(iterator.hasNext()) {
			EntityLiving entityLiving = (EntityLiving)iterator.next();
			if(this.isValidTarget(entityLiving)) return entityLiving;
		}
		
		return null;
	}
	
	@Override
	protected int getDropItemId() {
		switch(this.rand.nextInt(8)) {
			case 0:
			case 1: return Item.rottenFlesh.shiftedIndex;
			case 2:
			case 3: 
			case 4:
			case 5: return Block.sand.blockID;
			default: return Item.bread.shiftedIndex;
		}
	}
	
	public boolean burnsOnDaylight() {
		return false;
	}
	
	public int getFullHealth() {
		return 15;
	}
	
}

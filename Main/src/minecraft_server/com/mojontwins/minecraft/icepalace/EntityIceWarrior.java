package com.mojontwins.minecraft.icepalace;

import java.util.Iterator;
import java.util.List;

import com.chocolatin.betterdungeons.EntityHumanBase;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IMobWithLevel;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.player.EntityPlayer;

public class EntityIceWarrior extends EntityHumanBase implements IMobWithLevel, ISentient {

	public EntityIceWarrior(World world) {
		super(world);
		this.texture = "/mob/ice.png";
	}

	@Override
	protected Entity findPlayerToAttack() {
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		if (
				entityPlayer1 != null && 
				!entityPlayer1.isCreative && 
				this.canEntityBeSeen(entityPlayer1)
		) {
			return entityPlayer1;
		}
		
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
	
	@Override
	public ItemStack getHeldItem() {
		switch(this.getLvl()) {
		case 0:
			return new ItemStack(Item.battleWood, 1);
		case 1:
			return new ItemStack(Item.maceSteel, 1);
		case 2:
			return new ItemStack(Item.hammerSteel, 1);
		case 3:
			return new ItemStack(Item.maceGold, 1);
		case 4:
			return new ItemStack(Item.maceDiamond, 1);
		default:
			return new ItemStack(Item.axeGold, 1);
		}
	}
	
	public void setArmorTexture(int lvl) {
		switch(lvl) {
		case 1:
			this.armorTexture = "/armor/chain";
			break;
		case 2:
			this.armorTexture = "/armor/ice_iron";
			break;
		case 3:
			this.armorTexture = "/armor/ice_gold";
			break;
		case 4:
			this.armorTexture = "/armor/ice_diamond";
			break;
		default:
			this.armorTexture = "/armor/ice_cloth";
		}

	}
	
	@Override
	protected String getHurtSound() {
		return "mob.ice.hurt";
	}
	
	@Override
	protected String getDeathSound() {
		return "mob.ice.hurt";
	}
	
	@Override
	protected String getLivingSound() {
		return "mob.ice.idle";
	}
}

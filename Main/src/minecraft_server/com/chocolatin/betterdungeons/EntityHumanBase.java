package com.chocolatin.betterdungeons;

import net.minecraft.src.EntityMobWithLevel;
import net.minecraft.src.IMob;
import net.minecraft.src.IMobWithLevel;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityHumanBase extends EntityMobWithLevel implements IMob, IMobWithLevel {
	protected int armor = 4;
	protected int armorIndex = 1;
	public String armorTexture = "/armor/cloth_1.png";
	public int origLvl = 0xff;
	public boolean despawn = false;
	public boolean renderEars = false;
	
	public boolean isSwinging = false;
	public int swingProgressInt = 0;
	
	public EntityHumanBase(World world) {
		super(world);
		this.attackStrength = 4;
	}

	@Override
	public void setLevel(int level) {
		this.setLvl(level);
		this.configureAttributesBasedOnLevel();
	}
	
	@Override
	public int getMaxLevel() {
		return 5;
	}
	
	public void configureAttributesBasedOnLevel() {
		this.setArmorTexture(this.getLvl());
	
		if(this.worldObj != null) {
			this.armorIndex = this.getLvl() + this.armorIndex + this.worldObj.difficultySetting;
			if(this.worldObj.difficultySetting == 2) {
				++this.attackStrength;
			}
	
			if(this.worldObj.difficultySetting == 3) {
				this.attackStrength += 3;
			}
	
			this.attackStrength += this.getLvl();
		}
		
		this.origLvl = this.getLvl();
		this.health = this.getFullHealth();
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public void onUpdate() {
		// Level will come up later if we are in SMP
		if(this.origLvl != this.getLvl()) this.configureAttributesBasedOnLevel();
		
		super.onUpdate();		
	}
	
	public void setArmorTexture(int lvl) {
		switch(lvl) {
		case 1:
			this.armorTexture = "/armor/chain";
			break;
		case 2:
			this.armorTexture = "/armor/iron";
			break;
		case 3:
			this.armorTexture = "/armor/gold";
			break;
		case 4:
			this.armorTexture = "/armor/diamond";
			break;
		default:
			this.armorTexture = "/armor/cloth";
		}

	}

	@Override
	public int getFullHealth() {
		return 20 + this.armorIndex * 4;
	}
	
	public void updateArmor() {
		if(this.health < this.getFullHealth() - this.armorIndex * 4) {
			this.armor = 0;
		} else if(this.health < this.getFullHealth() - this.armorIndex * 3) {
			this.armor = 1;
		} else if(this.health < this.getFullHealth() - this.armorIndex * 2) {
			this.armor = 2;
		} else if(this.health < this.getFullHealth() - this.armorIndex) {
			this.armor = 3;
		}
	}
	
	@Override
	public void onLivingUpdate() {
		this.updateArmor();

		super.onLivingUpdate();
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
	public void dropFewItems() {
		int j = this.rand.nextInt(3);

		int l;
		for(l = 0; l < j; ++l) {
			this.dropItem(Item.appleRed.shiftedIndex, 1);
		}

		if(this.getLvl() == 2) {
			j = this.rand.nextInt(3);

			for(l = 0; l < j; ++l) {
				this.dropItem(Item.ingotIron.shiftedIndex, 1);
			}
		}

		if(this.getLvl() == 3) {
			j = this.rand.nextInt(3);

			for(l = 0; l < j; ++l) {
				this.dropItem(Item.ingotGold.shiftedIndex, 1);
			}
		}

		if(this.getLvl() == 4) {
			j = this.rand.nextInt(3);

			for(l = 0; l < j; ++l) {
				this.dropItem(Item.diamond.shiftedIndex, 1);
			}
		}

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("level", this.getLvl());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setLvl(nbttagcompound.getInteger("level"));
		this.configureAttributesBasedOnLevel();
	}

	@Override
	protected void despawnEntity() {
		if(!this.despawn) {
			if(this.isDead) {
				super.despawnEntity();
			}
		} else {
			super.despawnEntity();
		}

	}

	@Override
	protected int getDropItemId() {
		return Item.feather.shiftedIndex;
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		
		if(!this.isSwinging) {
			this.swingProgress = 0;
			if(this.entityToAttack != null && this.entityToAttack.getDistanceToEntity(this) < 3.0F) {
				this.isSwinging = true;
			}
		} else {
			++this.swingProgressInt;
			if(this.swingProgressInt >= 8) {
				this.swingProgressInt = 0;
				this.isSwinging = false;
			}
		} 
		this.swingProgress = (float)this.swingProgressInt / 8.0F;
		
		if(this.entityToAttack != null && !this.entityToAttack.isEntityAlive()) {
			this.entityToAttack = null;
		}
	}

	@Override
	public ItemStack getHeldItem() {
		switch(this.getLvl()) {
		case 0:
			return new ItemStack(Item.swordWood, 1);
		case 1:
			return new ItemStack(Item.swordStone, 1);
		case 2:
			return new ItemStack(Item.swordSteel, 1);
		case 3:
			return new ItemStack(Item.swordGold, 1);
		case 4:
			return new ItemStack(Item.swordDiamond, 1);
		default:
			return new ItemStack(Item.swordGold, 1);
		}
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}
}

package net.minecraft.world.entity.mob;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.world.inventory.IInventory;
import net.minecraft.world.inventory.InventoryMob;
import net.minecraft.world.level.World;

public class EntityArmoredMob extends EntityMob {
	// Added a proper inventory to manage actual armor and stuff
	public InventoryMob inventory;
	protected int carryoverDamage = 0;
		
	public EntityArmoredMob(World world) {
		super(world);
		this.inventory = new InventoryMob(this, this.getSecondaryInventorySize()); 
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		NBTTagCompound inventoryTag = compound.getCompoundTag("Inventory");
		this.inventory.readFromNBT(inventoryTag);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public IInventory getIInventory() {
		return this.inventory;
	}
	
	public void setInventory(IInventory inventory) {
		this.inventory = (InventoryMob) inventory;
	}
	
	@Override
	protected void damageEntity(int damage) {
		int armorFactor = 25 - this.inventory.getTotalArmorValue();
		int weightedDamage = damage * armorFactor + this.carryoverDamage;
		int strength = weightedDamage / 25;
		this.carryoverDamage = weightedDamage % 25;
		super.damageEntity(strength);
	}
	
	public int getSecondaryInventorySize() {
		return 9;
	}

}

package net.minecraft.world.entity.animal;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.IWaterMob;
import net.minecraft.world.level.World;

public class EntityWaterMob extends EntityCreature implements IAnimals, IWaterMob {
	public EntityWaterMob(World world1) {
		super(world1);
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox);
	}

	public int getTalkInterval() {
		return 120;
	}
}

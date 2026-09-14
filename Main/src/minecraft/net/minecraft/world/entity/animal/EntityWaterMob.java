package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.IWaterMob;
import net.minecraft.world.level.World;

public class EntityWaterMob extends EntityCreature implements IAnimals, IWaterMob {
	public EntityWaterMob(World world) {
		super(world);
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox);
	}

	public int getTalkInterval() {
		return 120;
	}
}

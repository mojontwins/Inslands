package net.minecraft.world.entity.animal;

import net.minecraft.src.World;

public class EntityTFHostileWolf extends EntityWolf {
	public EntityTFHostileWolf(World world) {
		super(world);
		this.setAngry(true);
		this.health = 10;
	}

	public EntityTFHostileWolf(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	public void onUpdate() {
		super.onUpdate();
		if(!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	public boolean getCanSpawnHere() {
		return 
				this.worldObj.checkIfAABBIsClear(this.boundingBox) && 
				this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}
}

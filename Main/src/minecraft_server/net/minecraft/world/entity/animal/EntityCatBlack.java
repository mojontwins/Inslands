package net.minecraft.world.entity.animal;

import net.minecraft.world.level.World;

public class EntityCatBlack extends EntityBetaOcelot {
	private boolean witchCat = false;

	public EntityCatBlack(World world1) {
		super(world1);
		this.texture = "/mob/cat_black.png";
		this.setSize(0.8F, 0.8F);
		this.moveSpeed = 1.1F;
		this.health = this.getFullHealth();
	}
	
	public boolean isWet() {
		return false;
	}
	
	// Only spawns on city chunks
	public boolean isUrban() {
		return true;
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}
	
	@Override
	public boolean canDespawn() {
		return !this.isWitchCat();
	}

	public boolean isWitchCat() {
		return witchCat;
	}

	public void setWitchCat(boolean witchCat) {
		this.witchCat = witchCat;
	}
}

package net.minecraft.world.entity.animal;

import net.minecraft.util.MathHelper;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.World;

public abstract class EntityAetherAnimal extends EntityAnimal {
	public EntityAetherAnimal(World world) {
		super(world);
	}

	public float getBlockPathWeight(int x, int y, int z) {
		return this.worldObj.getBlockID(x, y - 1, z) == Block.grass.blockID ? 10.0F : this.worldObj.getLightBrightness(x, y, z) - 0.5F;
	}

	public boolean getCanSpawnHere() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox) && this.worldObj.getBlockID(x, y - 1, z) == Block.grass.blockID && this.worldObj.getFullBlockLightValue(x, y, z) > 8 && this.getBlockPathWeight(x, y, z) >= 0.0F;
	}

	public int getTalkInterval() {
		return 120;
	}
}

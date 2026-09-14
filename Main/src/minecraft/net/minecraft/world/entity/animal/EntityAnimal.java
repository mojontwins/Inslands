package net.minecraft.world.entity.animal;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.World;

public abstract class EntityAnimal extends EntityCreature implements IAnimals {
	public EntityAnimal(World world) {
		super(world);
	}

	public float getBlockPathWeight(int x, int y, int z) {
		return this.worldObj.getBlockID(x, y - 1, z) == Block.grass.blockID ? 10.0F : this.worldObj.getLightBrightness(x, y, z) - 0.5F;
	}

	public boolean getCanSpawnHere() {
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.boundingBox.minY);
		int z = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockID(x, y - 1, z) == Block.grass.blockID && (this.worldObj.getFullBlockLightValue(x, y, z) > 8 || !this.needsLitBlockToSpawn()) && super.getCanSpawnHere();
	}

	public int getTalkInterval() {
		return 120;
	}
	
	public boolean needsLitBlockToSpawn() {
		return true;
	}
}

package net.minecraft.src;

public abstract class EntityAetherAnimal extends EntityAnimal {
	public EntityAetherAnimal(World world) {
		super(world);
	}

	protected float getBlockPathWeight(int i, int j, int k) {
		return this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.Grass.blockID ? 10.0F : this.worldObj.getLightBrightness(i, j, k) - 0.5F;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.Grass.blockID && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && this.getBlockPathWeight(i, j, k) >= 0.0F;
	}

	public int getTalkInterval() {
		return 120;
	}
}

package net.minecraft.src;

import java.util.List;

public class EntityFloatingBlock extends Entity {
	public int blockID;
	public int metadata;
	public int flytime;

	public EntityFloatingBlock(World world) {
		super(world);
		this.flytime = 0;
	}

	public EntityFloatingBlock(World world, double d, double d1, double d2, int i, int j) {
		super(world);
		this.flytime = 0;
		this.blockID = i;
		this.metadata = j;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(d, d1, d2);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = d;
		this.prevPosY = d1;
		this.prevPosZ = d2;
	}

	public EntityFloatingBlock(World world, double d, double d1, double d2, int i) {
		this(world, d, d1, d2, i, 0);
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void onUpdate() {
		if(this.blockID == 0) {
			this.setEntityDead();
		} else {
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			++this.flytime;
			this.motionY += 0.04D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.98F;
			this.motionY *= (double)0.98F;
			this.motionZ *= (double)0.98F;
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY);
			int k = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getBlockId(i, j, k) == this.blockID || this.worldObj.getBlockId(i, j, k) == AetherBlocks.Grass.blockID && this.blockID == AetherBlocks.Dirt.blockID) {
				this.worldObj.setBlockWithNotify(i, j, k, 0);
			}

			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0D, 1.0D, 0.0D));

			for(int n = 0; n < list.size(); ++n) {
				if(list.get(n) instanceof EntityFallingSand && this.worldObj.canBlockBePlacedAt(this.blockID, i, j, k, true, 1)) {
					this.worldObj.setBlockAndMetadataWithNotify(i, j, k, this.blockID, this.metadata);
					this.setEntityDead();
				}
			}

			if(this.isCollidedVertically && !this.onGround) {
				this.motionX *= (double)0.7F;
				this.motionZ *= (double)0.7F;
				this.motionY *= -0.5D;
				this.setEntityDead();
				if((!this.worldObj.canBlockBePlacedAt(this.blockID, i, j, k, true, 1) || BlockFloating.canFallAbove(this.worldObj, i, j + 1, k) || !this.worldObj.setBlockAndMetadataWithNotify(i, j, k, this.blockID, this.metadata)) && !this.worldObj.multiplayerWorld) {
					;
				}
			} else if(this.flytime > 100 && !this.worldObj.multiplayerWorld) {
				this.setEntityDead();
			}

		}
	}

	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setByte("Tile", (byte)this.blockID);
	}

	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.blockID = nbttagcompound.getByte("Tile") & 255;
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public World getWorld() {
		return this.worldObj;
	}
}

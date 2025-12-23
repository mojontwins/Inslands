package net.minecraft.world.entity;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;

public class EntityTemporarySeat extends Entity {
	public int tileX, tileY, tileZ;
	
	/* 
	 * This entity will be created & spawned when the user right-clicks on a block that can act as a seat.
	 * The player will "ride" this invisible entity. When the player dismounts, this entity dies.
	 */
	
	public EntityTemporarySeat(World world) {
		super(world);
	}
	
	public void setTilePos(int x, int y, int z) {
		this.tileX = x; this.tileY = y; this.tileZ = z;
	}

	public boolean at(int x, int y, int z) {
		return this.tileX == x && this.tileY == y && this.tileZ == z;
	}
	
	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.tileX = nbt.getInteger(string1)
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
	}

	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
	
	@Override
	public boolean attackEntityFrom(Entity e, int damage) {
		return true;
	}
	
	@Override
	public double getMountedYOffset() {
		return 0.5D;
	}
	
	@Override
	public void updateRiderPosition() {
		if(this.riddenByEntity != null) {
			this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
		}
	}
	
	@Override
	public float getShadowSize() {
		return 0.0F;
	}
	
	@Override
	public boolean interact(EntityPlayer e) {
		if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity == e) {
			// Player has clicked me...
			e.ridingEntity = null;
			System.out.println ("Killing seat " + this);
			this.setEntityDead();
		}
		
		return true;
	}
}

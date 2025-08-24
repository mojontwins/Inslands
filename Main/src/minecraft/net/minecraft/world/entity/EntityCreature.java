package net.minecraft.world.entity;

import net.minecraft.util.MathHelper;
import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.ChunkCoordinates;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.Vec3D;

public class EntityCreature extends EntityLiving {
	protected PathEntity activePath;
	protected Entity entityToAttack;
	protected boolean hasAttacked = false;

	public EntityCreature(World world1) {
		super(world1);
	}

	protected boolean isMovementCeased() {
		return false;
	}

	protected void updateEntityActionState() {
		this.hasAttacked = this.isMovementCeased();
		
		float f1 = 16.0F;
		
		// Entity to attack 
		
		if(this.entityToAttack == null) {
			this.entityToAttack = this.findPlayerToAttack();
			if(this.entityToAttack != null) {
				this.activePath = this.worldObj.getPathToEntity(this, this.entityToAttack, f1);
			}
		
		} else if(!this.entityToAttack.isEntityAlive()) {
			this.entityToAttack = null;
		
		} else {
			float f2 = this.entityToAttack.getDistanceToEntity(this);
			if(this.canEntityBeSeen(this.entityToAttack)) {
				this.attackEntity(this.entityToAttack, f2);
			} else {
				this.attackBlockedEntity(this.entityToAttack, f2);
			}
		
		}

		if(this.hasAttacked || this.entityToAttack == null || this.activePath != null && this.rand.nextInt(20) != 0) {
			if(!this.hasAttacked && (this.activePath == null && this.rand.nextInt(80) == 0 || this.rand.nextInt(80) == 0)) {
				if(this.hasHome() && !this.isWithinHomeDistanceCurrentPosition()) {
					this.getBackHome();
				} else {
					this.getNewRandomPath();
				}
			}
			
		} else {
			this.activePath = this.worldObj.getPathToEntity(this, this.entityToAttack, f1);
			
		}

		int i21 = MathHelper.floor_double(this.boundingBox.minY + 0.5D);
		
		boolean needsFloat = this.isInWater() && this.triesToFloat();
		boolean isInLava = this.handleLavaMovement();
		
		this.rotationPitch = 0.0F;
		
		// If a path to an entity exists...
		
		if(this.activePath != null && this.rand.nextInt(100) != 0) {
			Vec3D vec3D5 = this.activePath.getPosition(this);
			double d6 = (double)(this.width * 2.0F);

			while(vec3D5 != null && vec3D5.squareDistanceTo(this.posX, vec3D5.yCoord, this.posZ) < d6 * d6) {
				this.activePath.incrementPathIndex();
				if(this.activePath.isFinished()) {
					vec3D5 = null;
					this.activePath = null;
				} else {
					vec3D5 = this.activePath.getPosition(this);
				}
			}

			this.isJumping = false;
			if(vec3D5 != null) {
				double d8 = vec3D5.xCoord - this.posX;
				double d10 = vec3D5.zCoord - this.posZ;
				double d12 = vec3D5.yCoord - (double)i21;
				float f14 = (float)(Math.atan2(d10, d8) * 180.0D / (double)(float)Math.PI) - 90.0F;
				float f15 = f14 - this.rotationYaw;

				for(this.moveForward = this.moveSpeed; f15 < -180.0F; f15 += 360.0F) {
				}

				while(f15 >= 180.0F) {
					f15 -= 360.0F;
				}

				if(f15 > 30.0F) {
					f15 = 30.0F;
				}

				if(f15 < -30.0F) {
					f15 = -30.0F;
				}

				this.rotationYaw += f15;
				if(this.hasAttacked && this.entityToAttack != null) {
					double d16 = this.entityToAttack.posX - this.posX;
					double d18 = this.entityToAttack.posZ - this.posZ;
					float f20 = this.rotationYaw;
					this.rotationYaw = (float)(Math.atan2(d18, d16) * 180.0D / (double)(float)Math.PI) - 90.0F;
					f15 = (f20 - this.rotationYaw + 90.0F) * (float)Math.PI / 180.0F;
					this.moveStrafing = -MathHelper.sin(f15) * this.moveForward * 1.0F;
					this.moveForward = MathHelper.cos(f15) * this.moveForward * 1.0F;
				}

				if(d12 > 0.0D) {
					this.isJumping = true;
				}
			}

			if(this.entityToAttack != null) {
				this.faceEntity(this.entityToAttack, 30.0F, 30.0F);
			}

			if(this.isCollidedHorizontally && !this.hasPath()) {
				this.isJumping = true;
			}

			if(this.rand.nextFloat() < 0.8F && (needsFloat || isInLava)) {
				this.isJumping = true;
			}

		} else {
			// If a path to an entity does not exist, do the normal thing.
			
			super.updateEntityActionState();
			this.activePath = null;
		}
	}

	// Here is the awesome WANDER AI
	protected void getNewRandomPath() {
		boolean found = false;
		int x = -1;
		int y = -1;
		int z = -1;
		float maxPathWeight = -99999.0F;

		for(int i = 0; i < 10; ++i) {
			int rx = MathHelper.floor_double(this.posX + (double)this.rand.nextInt(13) - 6.0D);
			int ry = MathHelper.floor_double(this.posY + (double)this.rand.nextInt(7) - 3.0D);
			int rz = MathHelper.floor_double(this.posZ + (double)this.rand.nextInt(13) - 6.0D);
			float pathWeight = this.getBlockPathWeight(rx, ry, rz);
			if(pathWeight > maxPathWeight) {
				maxPathWeight = pathWeight;
				x = rx;
				y = ry;
				z = rz;
				found = true;
			}
		}

		if(found) {
			this.activePath = this.worldObj.getEntityPathToXYZ(this, x, y, z, 10.0F);
		}

	}
	
	// New: Back to home
	protected void getBackHome() {
		ChunkCoordinates homePosition = this.getHomePosition();
		this.activePath = this.worldObj.getEntityPathToXYZ(this, homePosition.posX, homePosition.posY, homePosition.posZ, 16.0F);
	}

	protected void attackEntity(Entity entity1, float f2) {
	}

	protected void attackBlockedEntity(Entity entity1, float f2) {
	}

	public float getBlockPathWeight(int i1, int i2, int i3) {
		return 0.0F;
	}

	protected Entity findPlayerToAttack() {
		return null;
	}

	public boolean getCanSpawnHere() {
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.boundingBox.minY);
		int i3 = MathHelper.floor_double(this.posZ);
		return super.getCanSpawnHere() && this.getBlockPathWeight(i1, i2, i3) >= 0.0F;
	}

	public boolean hasPath() {
		return this.activePath != null;
	}

	public void setPathToEntity(PathEntity pathEntity1) {
		this.activePath = pathEntity1;
	}

	public Entity getTarget() {
		return this.entityToAttack;
	}

	public void setTarget(Entity entity1) {
		this.entityToAttack = entity1;
	}
}

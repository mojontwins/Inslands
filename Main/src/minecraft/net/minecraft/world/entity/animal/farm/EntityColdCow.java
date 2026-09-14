package net.minecraft.world.entity.animal.farm;

import com.mojang.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

public class EntityColdCow extends EntityCow {
	private int angerLevel = 0;
	protected int attackStrength = 4;

	public EntityColdCow(World world) {
		super(world);
		this.texture = "/mob/cow2.png";
		this.setSize(0.9F, 1.3F);
		this.health = this.getFullHealth();
	}

	protected void dropFewItems() {
		this.entityDropItem(new ItemStack(Item.leather, 1, 0), 0.0F);
		this.entityDropItem(new ItemStack(Item.beefRaw, 1, 0), 0.0F);
	}

	// Make angerable!

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("Anger", (short) this.angerLevel);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.angerLevel = nbttagcompound.getShort("Anger");
	}

	protected Entity findPlayerToAttack() {
		if (this.angerLevel == 0) return null;

		EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return player != null && !player.isCreative && this.canEntityBeSeen(player) ? player : null;
	}

	public boolean attackEntityFrom(Entity source, int damage) {
		if (source instanceof EntityPlayer && ((EntityPlayer) source).isCreative) {
			return false;
		}

		List<Entity> nearbyCows = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));

		for (int i = 0; i < nearbyCows.size(); ++i) {
			Entity nearby = (Entity) nearbyCows.get(i);
			if (nearby instanceof EntityColdCow) {
				EntityColdCow cowToAnger = (EntityColdCow) nearby;
				cowToAnger.becomeAngryAt(source);
			}
		}

		this.becomeAngryAt(source);

		return super.attackEntityFrom(source, damage);
	}

	private void becomeAngryAt(Entity source) {
		this.entityToAttack = source;
		this.angerLevel = 400 + this.rand.nextInt(400);
	}

	//


	protected void updateEntityActionState() {
		this.hasAttacked = this.isMovementCeased();
		float targetRange = 16.0F;
		if (this.entityToAttack == null) {
			this.entityToAttack = this.findPlayerToAttack();
			if (this.entityToAttack != null) {
				this.activePath = this.worldObj.getPathToEntity(this, this.entityToAttack, targetRange);
			}
		} else if (!this.entityToAttack.isEntityAlive()) {
			this.entityToAttack = null;
		} else {
			float distance = this.entityToAttack.getDistanceToEntity(this);
			if (this.canEntityBeSeen(this.entityToAttack)) {
				this.attackEntity(this.entityToAttack, distance);
			} else {
				this.attackBlockedEntity(this.entityToAttack, distance);
			}
		}

		if (this.hasAttacked || this.entityToAttack == null || this.activePath != null && this.rand.nextInt(20) != 0) {
			if (!this.hasAttacked && (this.activePath == null && this.rand.nextInt(80) == 0 || this.rand.nextInt(80) == 0)) {
				this.getNewRandomPath();
			}
		} else {
			this.activePath = this.worldObj.getPathToEntity(this, this.entityToAttack, targetRange);
		}

		int baseY = MathHelper.floor_double(this.boundingBox.minY + 0.5D);
		boolean inWater = this.isInWater();
		boolean inLava = this.handleLavaMovement();
		this.rotationPitch = 0.0F;
		if (this.activePath != null && this.rand.nextInt(100) != 0) {
			Vec3D pathTarget = this.activePath.getPosition(this);
			double maxDistance = (double) (this.width * 2.0F);

			while (pathTarget != null && pathTarget.squareDistanceTo(this.posX, pathTarget.yCoord, this.posZ) < maxDistance * maxDistance) {
				this.activePath.incrementPathIndex();
				if (this.activePath.isFinished()) {
					pathTarget = null;
					this.activePath = null;
				} else {
					pathTarget = this.activePath.getPosition(this);
				}
			}

			this.isJumping = false;
			if (pathTarget != null) {
				double dx = pathTarget.xCoord - this.posX;
				double dz = pathTarget.zCoord - this.posZ;
				double dy = pathTarget.yCoord - (double) baseY;
				float targetYaw = (float) (Math.atan2(dz, dx) * 180.0D / (double) (float) Math.PI) - 90.0F;
				float yaw = targetYaw - this.rotationYaw;

				for (this.moveForward = this.moveSpeed; yaw < -180.0F; yaw += 360.0F) {
				}

				while (yaw >= 180.0F) {
					yaw -= 360.0F;
				}

				if (yaw > 30.0F) {
					yaw = 30.0F;
				}

				if (yaw < -30.0F) {
					yaw = -30.0F;
				}

				this.rotationYaw += yaw;
				if (this.hasAttacked && this.entityToAttack != null) {
					double dxTarget = this.entityToAttack.posX - this.posX;
					double dzTarget = this.entityToAttack.posZ - this.posZ;
					float prevYaw = this.rotationYaw;
					this.rotationYaw = (float) (Math.atan2(dzTarget, dxTarget) * 180.0D / (double) (float) Math.PI) - 90.0F;
					yaw = (prevYaw - this.rotationYaw + 90.0F) * (float) Math.PI / 180.0F;
					this.moveStrafing = -MathHelper.sin(yaw) * this.moveForward * 1.0F;
					this.moveForward = MathHelper.cos(yaw) * this.moveForward * 1.0F;
				}

				if (dy > 0.0D) {
					this.isJumping = true;
				}
			}

			if (this.entityToAttack != null) {
				this.faceEntity(this.entityToAttack, 30.0F, 30.0F);
			}

			if (this.isCollidedHorizontally && !this.hasPath()) {
				this.isJumping = true;
			}

			if (this.rand.nextFloat() < 0.8F && (inWater || inLava)) {
				this.isJumping = true;
			}

		} else {
			super.updateEntityActionState();
			this.activePath = null;
		}
	}

	protected void getNewRandomPath() {
		boolean foundValid = false;
		int bestX = -1;
		int bestY = -1;
		int bestZ = -1;
		float bestScore = -99999.0F;

		for (int i = 0; i < 10; ++i) {
			int x = MathHelper.floor_double(this.posX + (double) this.rand.nextInt(13) - 6.0D);
			int y = MathHelper.floor_double(this.posY + (double) this.rand.nextInt(7) - 3.0D);
			int z = MathHelper.floor_double(this.posZ + (double) this.rand.nextInt(13) - 6.0D);
			float score = this.getBlockPathWeight(x, y, z);
			if (score > bestScore) {
				bestScore = score;
				bestX = x;
				bestY = y;
				bestZ = z;
				foundValid = true;
			}
		}

		if (foundValid) {
			this.activePath = this.worldObj.getEntityPathToXYZ(this, bestX, bestY, bestZ, 10.0F);
		}
	}

	protected void attackEntity(Entity target, float distance) {
		if (this.attackTime <= 0 && distance < 2.0F && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			target.attackEntityFrom(this, this.attackStrength);
		}
	}

	public boolean hasPath() {
		return this.activePath != null;
	}

	public void setPathToEntity(PathEntity path) {
		this.activePath = path;
	}

	public Entity getTarget() {
		return this.entityToAttack;
	}

	public void setTarget(Entity target) {
		this.entityToAttack = target;
	}

	public int getFullHealth() {
		return 15;
	}

	public void setInSnow() {
	}
}

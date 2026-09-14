package net.minecraft.world.entity.animal.farm;

import com.mojang.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

public class EntityChickenBlack extends EntityChicken {
	private int angerLevel = 0;
	protected int attackStrength = 2;

	public EntityChickenBlack(World world) {
		super(world);
		this.texture = "/mob/chicken_black.png";
		this.setSize(0.3F, 0.4F);
		this.health = 10;
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
	}

	protected int getDropItemId() {
		return this.rand.nextBoolean() ? Item.chickenRaw.shiftedIndex : (this.rand.nextBoolean() ? Item.chickenRaw.shiftedIndex : Item.feather.shiftedIndex);
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
		if (source instanceof EntityPlayer && !((EntityPlayer) source).isCreative) {

			List<Entity> nearbyChickens = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(16.0D, 16.0D, 16.0D));

			for (int i = 0; i < nearbyChickens.size(); ++i) {
				Entity nearby = (Entity) nearbyChickens.get(i);
				if (nearby instanceof EntityChickenBlack) {
					EntityChickenBlack chickenToAnger = (EntityChickenBlack) nearby;
					chickenToAnger.becomeAngryAt(source);
				}
			}

			this.becomeAngryAt(source);
		}

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
		return 10;
	}
}

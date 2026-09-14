package net.minecraft.world.entity.animal.wild;

import java.util.List;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.animal.farm.EntitySheep;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFood;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.phys.AxisAlignedBB;

public class EntityWolf extends EntityAnimal {
	private boolean looksWithInterest = false;
	private float headRoll;
	private float prevHeadRoll;
	private boolean isWolfShaking;
	private boolean isCuqui;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	public EntityWolf(World world) {
		super(world);
		this.texture = "/mob/wolf.png";
		this.setSize(0.8F, 0.8F);
		this.moveSpeed = 1.1F;
		this.health = 8;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, (byte) 0);
		this.dataWatcher.addObject(17, "");
		this.dataWatcher.addObject(18, this.health);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public String getEntityTexture() {
		return this.isTamed() ? "/mob/wolf_tame.png" : (this.isWolfAngry() ? "/mob/wolf_angry.png" : super.getEntityTexture());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Angry", this.isWolfAngry());
		nbttagcompound.setBoolean("Sitting", this.getIsSitting());
		if (this.getOwner() == null) {
			nbttagcompound.setString("Owner", "");
		} else {
			nbttagcompound.setString("Owner", this.getOwner());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setAngry(nbttagcompound.getBoolean("Angry"));
		this.setIsSitting(nbttagcompound.getBoolean("Sitting"));
		String owner = nbttagcompound.getString("Owner");
		if (owner.length() > 0) {
			this.setWolfOwner(owner);
			this.setWolfTamed(true);
		}
	}

	@Override
	protected boolean canDespawn() {
		return !this.isTamed();
	}

	@Override
	protected String getLivingSound() {
		return this.isWolfAngry() ? "mob.wolf.growl" : (this.rand.nextInt(3) == 0 ? (this.isTamed() && this.dataWatcher.getWatchableObjectInt(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
	}

	@Override
	protected String getHurtSound() {
		return "mob.wolf.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.wolf.death";
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	protected int getDropItemId() {
		return -1;
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		if (!this.hasAttacked && !this.hasPath() && this.isTamed() && this.ridingEntity == null) {
			EntityPlayer ownerPlayer = this.worldObj.getPlayerEntityByName(this.getOwner());
			if (ownerPlayer != null) {
				float distance = ownerPlayer.getDistanceToEntity(this);
				if (distance > 5.0F) {
					this.getPathOrWalkableBlock(ownerPlayer, distance);
				}
			} else if (!this.isInWater()) {
				this.setIsSitting(true);
			}
		} else if (this.entityToAttack == null && !this.hasPath() && !this.isTamed() && this.worldObj.rand.nextInt(100) == 0) {
			List<Entity> nearbySheep = this.worldObj.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
			if (!nearbySheep.isEmpty()) {
				this.setTarget(nearbySheep.get(this.worldObj.rand.nextInt(nearbySheep.size())));
			}
		}

		if (this.isInWater()) {
			this.setIsSitting(false);
		}

		if (!this.worldObj.isRemote) {
			this.dataWatcher.updateObject(18, this.health);
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.looksWithInterest = false;
		if (this.hasCurrentTarget() && !this.hasPath() && !this.isWolfAngry()) {
			Entity target = this.getCurrentTarget();
			if (target instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) target;
				ItemStack heldItem = player.inventory.getCurrentItem();
				if (heldItem != null) {
					if (!this.isTamed() && heldItem.itemID == Item.bone.shiftedIndex) {
						this.looksWithInterest = true;
					} else if (this.isTamed() && Item.itemsList[heldItem.itemID] instanceof ItemFood) {
						this.looksWithInterest = ((ItemFood) Item.itemsList[heldItem.itemID]).getIsWolfsFavoriteMeat();
					}
				}
			}
		}

		if (!this.isMultiplayerEntity && this.isWolfShaking && !this.isCuqui && !this.hasPath() && this.onGround) {
			this.isCuqui = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.worldObj.setEntityState(this, (byte) 8);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.prevHeadRoll = this.headRoll;
		if (this.looksWithInterest) {
			this.headRoll += (1.0F - this.headRoll) * 0.4F;
		} else {
			this.headRoll += (0.0F - this.headRoll) * 0.4F;
		}

		if (this.looksWithInterest) {
			this.numTicksToChaseTarget = 10;
		}

		if (this.isWet()) {
			this.isWolfShaking = true;
			this.isCuqui = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if ((this.isWolfShaking || this.isCuqui) && this.isCuqui) {
			if (this.timeWolfIsShaking == 0.0F) {
				this.worldObj.playSoundAtEntity(this, "mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;
			if (this.prevTimeWolfIsShaking >= 2.0F) {
				this.isWolfShaking = false;
				this.isCuqui = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}

			if (this.timeWolfIsShaking > 0.4F) {
				float feetY = (float) this.boundingBox.minY;
				int particleCount = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);
				for (int i = 0; i < particleCount; ++i) {
					float offsetX = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float offsetZ = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					this.worldObj.spawnParticle("splash", this.posX + (double) offsetX, (double) (feetY + 0.8F), this.posZ + (double) offsetZ, this.motionX, this.motionY, this.motionZ);
				}
			}
		}
	}

	public boolean getWolfShaking() {
		return this.isWolfShaking;
	}

	public float getShadingWhileShaking(float partialTick) {
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * partialTick) / 2.0F * 0.25F;
	}

	public float getShakeAngle(float partialTick, float offsetValue) {
		float angle = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * partialTick + offsetValue) / 1.8F;
		if (angle < 0.0F) {
			angle = 0.0F;
		} else if (angle > 1.0F) {
			angle = 1.0F;
		}
		return MathHelper.sin(angle * (float) Math.PI) * MathHelper.sin(angle * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
	}

	public float getInterestedAngle(float partialTick) {
		return (this.prevHeadRoll + (this.headRoll - this.prevHeadRoll) * partialTick) * 0.15F * (float) Math.PI;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.8F;
	}

	@Override
	public int getVerticalFaceSpeed() {
		return this.getIsSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	private void getPathOrWalkableBlock(Entity entity, float distance) {
		PathEntity path = this.worldObj.getPathToEntity(this, entity, 16.0F);
		if (path == null && distance > 12.0F) {
			int xBase = MathHelper.floor_double(entity.posX) - 2;
			int zBase = MathHelper.floor_double(entity.posZ) - 2;
			int y = MathHelper.floor_double(entity.boundingBox.minY);

			for (int searchX = 0; searchX <= 4; ++searchX) {
				for (int searchZ = 0; searchZ <= 4; ++searchZ) {
					if ((searchX < 1 || searchZ < 1 || searchX > 3 || searchZ > 3) && this.worldObj.isBlockNormalCube(xBase + searchX, y - 1, zBase + searchZ) && !this.worldObj.isBlockNormalCube(xBase + searchX, y, zBase + searchZ) && !this.worldObj.isBlockNormalCube(xBase + searchX, y + 1, zBase + searchZ)) {
						this.setLocationAndAngles((double) ((float) (xBase + searchX) + 0.5F), (double) y, (double) ((float) (zBase + searchZ) + 0.5F), this.rotationYaw, this.rotationPitch);
						return;
					}
				}
			}
		} else {
			this.setPathToEntity(path);
		}
	}

	@Override
	protected boolean isMovementCeased() {
		return this.getIsSitting() || this.isCuqui;
	}

	@Override
	public boolean attackEntityFrom(Entity source, int damage) {
		this.setIsSitting(false);
		if (source != null && !(source instanceof EntityPlayer) && !(source instanceof EntityArrow)) {
			damage = (damage + 1) / 2;
		}

		if (!super.attackEntityFrom(source, damage)) {
			return false;
		} else {
			if (source instanceof EntityPlayer && ((EntityPlayer) source).isCreative) {
				return true;
			}

			if (!this.isTamed() && !this.isWolfAngry()) {
				if (source instanceof EntityPlayer) {
					this.setAngry(true);
					this.entityToAttack = source;
				}

				if (source instanceof EntityArrow && ((EntityArrow) source).shootingEntity != null) {
					source = ((EntityArrow) source).shootingEntity;
				}

				if (source instanceof EntityLiving) {
					List<Entity> nearbyWolves = this.worldObj.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
					for (Entity nearby : nearbyWolves) {
						EntityWolf wolf = (EntityWolf) nearby;
						if (!wolf.isTamed() && wolf.entityToAttack == null) {
							wolf.entityToAttack = source;
							if (source instanceof EntityPlayer) {
								wolf.setAngry(true);
							}
						}
					}
				}
			} else if (source != this && source != null) {
				if (this.isTamed() && source instanceof EntityPlayer && ((EntityPlayer) source).username.equalsIgnoreCase(this.getOwner())) {
					return true;
				}
				this.entityToAttack = source;
			}

			return true;
		}
	}

	@Override
	protected Entity findPlayerToAttack() {
		return this.isWolfAngry() ? this.worldObj.getClosestPlayerToEntity(this, 16.0D) : null;
	}

	@Override
	protected void attackEntity(Entity entity, float distance) {
		if (distance > 2.0F && distance < 6.0F && this.rand.nextInt(10) == 0) {
			if (this.onGround) {
				double deltaX = entity.posX - this.posX;
				double deltaZ = entity.posZ - this.posZ;
				float horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
				this.motionX = deltaX / (double) horizontalDistance * 0.5D * (double) 0.8F + this.motionX * (double) 0.2F;
				this.motionZ = deltaZ / (double) horizontalDistance * 0.5D * (double) 0.8F + this.motionZ * (double) 0.2F;
				this.motionY = (double) 0.4F;
			}
		} else if ((double) distance < 1.5D && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			int damage = this.isTamed() ? 4 : 2;
			entity.attackEntityFrom(this, damage);
		}
	}

	@Override
	public boolean interact(EntityPlayer player) {
		if (super.interact(player)) {
			return false;
		}

		ItemStack heldItem = player.inventory.getCurrentItem();
		if (!this.isTamed()) {
			if (heldItem != null && heldItem.itemID == Item.bone.shiftedIndex && !this.isWolfAngry()) {
				--heldItem.stackSize;
				if (heldItem.stackSize <= 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}

				if (!this.worldObj.isRemote) {
					if (this.rand.nextInt(3) == 0) {
						this.setWolfTamed(true);
						this.setPathToEntity(null);
						this.setIsSitting(true);
						this.health = 20;
						this.setWolfOwner(player.username);
						this.showHeartsOrSmokeFX(true);
						this.worldObj.setEntityState(this, (byte) 7);
					} else {
						this.showHeartsOrSmokeFX(false);
						this.worldObj.setEntityState(this, (byte) 6);
					}
				}

				return true;
			}
		} else {
			if (heldItem != null && Item.itemsList[heldItem.itemID] instanceof ItemFood) {
				ItemFood food = (ItemFood) Item.itemsList[heldItem.itemID];
				if (food.getIsWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectInt(18) < 20) {
					--heldItem.stackSize;
					if (heldItem.stackSize <= 0) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}

					this.heal(((ItemFood) Item.porkRaw).getHealAmount());
					return true;
				}
			}

			if (player.username.equalsIgnoreCase(this.getOwner())) {
				if (!this.worldObj.isRemote) {
					this.setIsSitting(!this.getIsSitting());
					this.isJumping = false;
					this.setPathToEntity(null);
				}

				return true;
			}
		}

		return false;
	}

	void showHeartsOrSmokeFX(boolean tamed) {
		String particleName = tamed ? "heart" : "smoke";
		for (int i = 0; i < 7; ++i) {
			double velX = this.rand.nextGaussian() * 0.02D;
			double velY = this.rand.nextGaussian() * 0.02D;
			double velZ = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(particleName, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, velX, velY, velZ);
		}
	}

	@Override
	public void handleHealthUpdate(byte entityState) {
		if (entityState == 7) {
			this.showHeartsOrSmokeFX(true);
		} else if (entityState == 6) {
			this.showHeartsOrSmokeFX(false);
		} else if (entityState == 8) {
			this.isCuqui = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else {
			super.handleHealthUpdate(entityState);
		}
	}

	public float setTailRotation() {
		return this.isWolfAngry() ? 1.5393804F : (this.isTamed() ? (0.55F - (float) (20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02F) * (float) Math.PI : 0.62831855F);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	public String getOwner() {
		return this.dataWatcher.getWatchableObjectString(17);
	}

	public void setWolfOwner(String owner) {
		this.dataWatcher.updateObject(17, owner);
	}

	public boolean getIsSitting() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setIsSitting(boolean sitting) {
		byte flags = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, (byte) (sitting ? (flags | 1) : (flags & -2)));
	}

	public boolean isWolfAngry() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	public void setAngry(boolean angry) {
		byte flags = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, (byte) (angry ? (flags | 2) : (flags & -3)));
	}

	public boolean isTamed() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
	}

	public void setWolfTamed(boolean tamed) {
		byte flags = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, (byte) (tamed ? (flags | 4) : (flags & -5)));
	}

	public int getFullHealth() {
		return 8;
	}
}

package net.minecraft.world.entity.animal.wild;

import com.mojang.nbt.NBTTagCompound;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.animal.farm.EntityChicken;
import net.minecraft.world.entity.animal.farm.EntityChickenBlack;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFood;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.AxisAlignedBB;

public class EntityBetaOcelot extends EntityAnimal {
	private boolean looksWithInterest = false;
	private float headRoll;
	private float prevHeadRoll;
	private boolean isCatShaking;
	private boolean isCuqui;
	private float timeCatIsShaking;
	private float prevtimeCatIsShaking;

	public EntityBetaOcelot(World world) {
		super(world);
		this.texture = "/mob/ozelot.png";
		this.setSize(0.8F, 0.8F);
		this.moveSpeed = 1.1F;
		this.health = 8;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, (byte) 0);
		this.dataWatcher.addObject(17, "");
		this.dataWatcher.addObject(18, Integer.valueOf(this.health));
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Angry", this.isCatAngry());
		nbttagcompound.setBoolean("Sitting", this.getIsSitting());
		if (this.getOwner() == null) {
			nbttagcompound.setString("Owner", "");
		} else {
			nbttagcompound.setString("Owner", this.getOwner());
		}
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setCatAngry(nbttagcompound.getBoolean("Angry"));
		this.setIsSitting(nbttagcompound.getBoolean("Sitting"));
		String owner = nbttagcompound.getString("Owner");
		if (owner.length() > 0) {
			this.setWolfOwner(owner);
			this.setCatTamed(true);
		}
	}

	protected boolean canDespawn() {
		return !this.isCatTamed();
	}

	protected String getLivingSound() {
		if (this.isCatAngry()) return "mob.cat.hiss";
		if (this.rand.nextBoolean()) return null;

		if (this.isCatTamed() && this.rand.nextInt(3) == 0) {
			int health = this.dataWatcher.getWatchableObjectInt(18);
			if (health >= this.getFullHealth() - 2) {
				return "mob.cat.purr";
			} else if (health > this.getFullHealth() / 2) {
				return "mob.cat.purreow";
			}
		}

		return "mob.cat.meow";
	}

	protected String getHurtSound() {
		return "mob.cat.hitt";
	}

	protected String getDeathSound() {
		return "mob.cat.hitt";
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		return -1;
	}

	protected void updateEntityActionState() {
		super.updateEntityActionState();
		if (!this.hasAttacked && !this.hasPath() && this.isCatTamed() && this.ridingEntity == null) {
			EntityPlayer ownerPlayer = this.worldObj.getPlayerEntityByName(this.getOwner());
			if (ownerPlayer != null) {
				float distance = ownerPlayer.getDistanceToEntity(this);
				if (distance > 5.0F) {
					this.getPathOrWalkableBlock(ownerPlayer, distance);
				}
			} else if (!this.isInWater()) {
				this.setIsSitting(true);
			}
		} else if (this.entityToAttack == null && !this.hasPath() && /*!this.isCatTamed() &&*/ this.worldObj.rand.nextInt(100) == 0) {
			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D);
			List<Entity> chickens = this.worldObj.getEntitiesWithinAABB(EntityChicken.class, aabb);
			chickens.addAll(this.worldObj.getEntitiesWithinAABB(EntityChickenBlack.class, aabb));
			if (!chickens.isEmpty()) {
				this.setTarget((Entity) chickens.get(this.worldObj.rand.nextInt(chickens.size())));
			}
		}

		if (this.isInWater()) {
			this.setIsSitting(false);
		}

		if (!this.worldObj.isRemote) {
			this.dataWatcher.updateObject(18, this.health);
		}
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.looksWithInterest = false;
		if (this.hasCurrentTarget() && !this.hasPath() && !this.isCatAngry()) {
			Entity target = this.getCurrentTarget();
			if (target instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) target;
				ItemStack heldItem = player.inventory.getCurrentItem();
				if (heldItem != null) {
					if (!this.isCatTamed() && heldItem.itemID == Item.fishRaw.shiftedIndex) {
						this.looksWithInterest = true;
					} else if (this.isCatTamed() && Item.itemsList[heldItem.itemID] instanceof ItemFood) {
						this.looksWithInterest = ((ItemFood) Item.itemsList[heldItem.itemID]).getIsCatsFavoriteMeat();
					}
				}
			}
		}

		if (!this.isMultiplayerEntity && this.isCatShaking && !this.isCuqui && !this.hasPath() && this.onGround) {
			this.isCuqui = true;
			this.timeCatIsShaking = 0.0F;
			this.prevtimeCatIsShaking = 0.0F;
			this.worldObj.setEntityState(this, (byte) 8);
		}
	}

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

		/*
		if(this.isWet()) {
			this.isCatShaking = true;
			this.isCuqui = false;
			this.timeCatIsShaking = 0.0F;
			this.prevtimeCatIsShaking = 0.0F;
		} else if((this.isCatShaking || this.isCuqui) && this.isCuqui) {
			if(this.timeCatIsShaking == 0.0F) {
				this.worldObj.playSoundAtEntity(this, "mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevtimeCatIsShaking = this.timeCatIsShaking;
			this.timeCatIsShaking += 0.05F;
			if(this.prevtimeCatIsShaking >= 2.0F) {
				this.isCatShaking = false;
				this.isCuqui = false;
				this.prevtimeCatIsShaking = 0.0F;
				this.timeCatIsShaking = 0.0F;
			}

			if(this.timeCatIsShaking > 0.4F) {
				float f1 = (float)this.boundingBox.minY;
				int i2 = (int)(MathHelper.sin((this.timeCatIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

				for(int i3 = 0; i3 < i2; ++i3) {
					float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					this.worldObj.spawnParticle("splash", this.posX + (double)f4, (double)(f1 + 0.8F), this.posZ + (double)f5, this.motionX, this.motionY, this.motionZ);
				}
			}
		}
		*/
	}

	public boolean getWolfShaking() {
		return this.isCatShaking;
	}

	public float getShadingWhileShaking(float partialTick) {
		return 0.75F + (this.prevtimeCatIsShaking + (this.timeCatIsShaking - this.prevtimeCatIsShaking) * partialTick) / 2.0F * 0.25F;
	}

	public float getShakeAngle(float partialTick, float offset) {
		float progress = (this.prevtimeCatIsShaking + (this.timeCatIsShaking - this.prevtimeCatIsShaking) * partialTick + offset) / 1.8F;
		if (progress < 0.0F) {
			progress = 0.0F;
		} else if (progress > 1.0F) {
			progress = 1.0F;
		}

		return MathHelper.sin(progress * (float) Math.PI) * MathHelper.sin(progress * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
	}

	public float getInterestedAngle(float partialTick) {
		return (this.prevHeadRoll + (this.headRoll - this.prevHeadRoll) * partialTick) * 0.15F * (float) Math.PI;
	}

	public float getEyeHeight() {
		return this.height * 0.8F;
	}

	public int getVerticalFaceSpeed() {
		return this.getIsSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	private void getPathOrWalkableBlock(Entity target, float distance) {
		PathEntity path = this.worldObj.getPathToEntity(this, target, 16.0F);
		if (path == null && distance > 12.0F) {
			int baseX = MathHelper.floor_double(target.posX) - 2;
			int baseZ = MathHelper.floor_double(target.posZ) - 2;
			int targetY = MathHelper.floor_double(target.boundingBox.minY);

			for (int dx = 0; dx <= 4; ++dx) {
				for (int dz = 0; dz <= 4; ++dz) {
					if ((dx < 1 || dz < 1 || dx > 3 || dz > 3) && this.worldObj.isBlockNormalCube(baseX + dx, targetY - 1, baseZ + dz) && !this.worldObj.isBlockNormalCube(baseX + dx, targetY, baseZ + dz) && !this.worldObj.isBlockNormalCube(baseX + dx, targetY + 1, baseZ + dz)) {
						this.setLocationAndAngles((double) ((float) (baseX + dx) + 0.5F), (double) targetY, (double) ((float) (baseZ + dz) + 0.5F), this.rotationYaw, this.rotationPitch);
						return;
					}
				}
			}
		} else {
			this.setPathToEntity(path);
		}
	}

	protected boolean isMovementCeased() {
		return this.getIsSitting() || this.isCuqui;
	}

	public boolean attackEntityFrom(Entity source, int damage) {
		this.setIsSitting(false);
		if (source != null && !(source instanceof EntityPlayer) && !(source instanceof EntityArrow)) {
			damage = (damage + 1) / 2;
		}

		if (!super.attackEntityFrom((Entity) source, damage)) {
			return false;
		} else {
			if (source instanceof EntityPlayer && ((EntityPlayer) source).isCreative) return true;

			if (!this.isCatTamed() && !this.isCatAngry()) {
				if (source instanceof EntityPlayer) {
					this.setCatAngry(true);
					this.entityToAttack = (Entity) source;
				}

				if (source instanceof EntityArrow && ((EntityArrow) source).shootingEntity != null) {
					source = ((EntityArrow) source).shootingEntity;
				}

				if (source instanceof EntityLiving) {
					List<Entity> nearbyCats = this.worldObj.getEntitiesWithinAABB(EntityBetaOcelot.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
					Iterator<Entity> iterator = nearbyCats.iterator();

					while (iterator.hasNext()) {
						Entity entity = (Entity) iterator.next();
						EntityBetaOcelot otherCat = (EntityBetaOcelot) entity;
						if (!otherCat.isCatTamed() && otherCat.entityToAttack == null) {
							otherCat.entityToAttack = (Entity) source;
							if (source instanceof EntityPlayer) {
								otherCat.setCatAngry(true);
							}
						}
					}
				}
			} else if (source != this && source != null) {
				if (this.isCatTamed() && source instanceof EntityPlayer && ((EntityPlayer) source).username.equalsIgnoreCase(this.getOwner())) {
					return true;
				}

				this.entityToAttack = (Entity) source;
			}

			return true;
		}
	}

	protected Entity findPlayerToAttack() {
		return this.isCatAngry() ? this.worldObj.getClosestPlayerToEntity(this, 16.0D) : null;
	}

	protected void attackEntity(Entity target, float distance) {
		if (distance > 2.0F && distance < 6.0F && this.rand.nextInt(10) == 0) {
			if (this.onGround) {
				double dx = target.posX - this.posX;
				double dz = target.posZ - this.posZ;
				float horizontalDistance = MathHelper.sqrt_double(dx * dx + dz * dz);
				this.motionX = dx / (double) horizontalDistance * 0.5D * (double) 0.8F + this.motionX * (double) 0.2F;
				this.motionZ = dz / (double) horizontalDistance * 0.5D * (double) 0.8F + this.motionZ * (double) 0.2F;
				this.motionY = (double) 0.4F;
			}
		} else if ((double) distance < 1.5D && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			byte damage = 2;
			if (this.isCatTamed()) {
				damage = 4;
			}

			target.attackEntityFrom(this, damage);
		}
	}

	public boolean interact(EntityPlayer player) {
		if (super.interact(player)) {
			return false;
		}

		ItemStack heldItem = player.inventory.getCurrentItem();
		if (!this.isCatTamed()) {
			if (heldItem != null && heldItem.itemID == Item.fishRaw.shiftedIndex && !this.isCatAngry()) {
				if (!player.isCreative) --heldItem.stackSize;
				if (heldItem.stackSize <= 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
				}

				if (!this.worldObj.isRemote) {
					if (this.rand.nextInt(3) == 0) {
						this.setCatTamed(true);
						this.setPathToEntity((PathEntity) null);
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
				ItemFood itemFood = (ItemFood) Item.itemsList[heldItem.itemID];
				if (itemFood.getIsCatsFavoriteMeat() && this.dataWatcher.getWatchableObjectInt(18) < 20) {
					if (!player.isCreative) --heldItem.stackSize;
					if (heldItem.stackSize <= 0) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
					}

					this.heal(((ItemFood) Item.porkRaw).getHealAmount());
					return true;
				}
			}

			if (player.username.equalsIgnoreCase(this.getOwner())) {
				if (!this.worldObj.isRemote) {
					this.setIsSitting(!this.getIsSitting());
					this.isJumping = false;
					this.setPathToEntity((PathEntity) null);
				}

				return true;
			}
		}

		return false;
	}

	void showHeartsOrSmokeFX(boolean showHearts) {
		String particleName = "heart";
		if (!showHearts) {
			particleName = "smoke";
		}

		for (int i = 0; i < 7; ++i) {
			double vx = this.rand.nextGaussian() * 0.02D;
			double vy = this.rand.nextGaussian() * 0.02D;
			double vz = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(particleName, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, vx, vy, vz);
		}
	}

	public void handleHealthUpdate(byte data) {
		if (data == 7) {
			this.showHeartsOrSmokeFX(true);
		} else if (data == 6) {
			this.showHeartsOrSmokeFX(false);
		} else if (data == 8) {
			this.isCuqui = true;
			this.timeCatIsShaking = 0.0F;
			this.prevtimeCatIsShaking = 0.0F;
		} else {
			super.handleHealthUpdate(data);
		}
	}

	public float setTailRotation() {
		return this.isCatAngry() ? 1.5393804F : (this.isCatTamed() ? (0.55F - (float) (20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02F) * (float) Math.PI : 0.62831855F);
	}

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
		byte dataByte = this.dataWatcher.getWatchableObjectByte(16);
		if (sitting) {
			this.dataWatcher.updateObject(16, (byte) (dataByte | 1));
		} else {
			this.dataWatcher.updateObject(16, (byte) (dataByte & -2));
		}
	}

	public boolean isCatAngry() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	public void setCatAngry(boolean angry) {
		byte dataByte = this.dataWatcher.getWatchableObjectByte(16);
		if (angry) {
			this.dataWatcher.updateObject(16, (byte) (dataByte | 2));
		} else {
			this.dataWatcher.updateObject(16, (byte) (dataByte & -3));
		}
	}

	public boolean isCatTamed() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
	}

	public void setCatTamed(boolean tamed) {
		byte dataByte = this.dataWatcher.getWatchableObjectByte(16);
		if (tamed) {
			this.dataWatcher.updateObject(16, (byte) (dataByte | 4));
		} else {
			this.dataWatcher.updateObject(16, (byte) (dataByte & -5));
		}
	}

	public int getFullHealth() {
		return 12;
	}
}

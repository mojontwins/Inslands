package net.minecraft.world.entity.mob.humanoid;

import com.mojang.nbt.NBTTagCompound;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.mob.EntityArmoredMob;
import net.minecraft.world.entity.mob.undead.EntityZombie;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFood;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.stats.AchievementList;

public class EntityAmazon extends EntityArmoredMob implements ISentient {
	private static final int DATA_FLAGS = 16;
	private static final int DATA_OWNER = 17;
	private static final int DATA_HEALTH = 18;
	private static final int DATA_TEXTURE_VARIATION = 19;

	// DataWatcher flag bits
	private static final int FLAG_SITTING = 1;
	private static final int FLAG_ANGRY = 2;
	private static final int FLAG_TAMED = 4;

	private boolean looksWithInterest = false;
	private float headRoll;
	private float prevHeadRoll;
	protected String texturePrefix;
	
	public boolean isSwinging = false;
	public int swingProgressInt = 0;
	public int timeBeforeLosingInterest = 0;
	
	public EntityAmazon(World world) {
		super(world);
		this.texture = "/mob/amazon1.png";
		this.texturePrefix="amazon";
		this.moveSpeed = 1.1F;
		this.health = this.getFullHealth();
		this.inventory.setInventorySlotContents(0, rand.nextInt(32) == 0 ? new ItemStack(Item.maceGold) : new ItemStack(Item.swordGold));
	}
	
	public void setEntityToAttack(Entity entity) {
		if(this.entityToAttack != entity) {
			this.timeBeforeLosingInterest = 600;
		}	
		this.entityToAttack = entity;
	}
	
	public void swingItem() {
		this.swingProgressInt = -1;
		this.isSwinging = true;
	}
	
	protected int getMaxTextureVariations() {
		return 4;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(DATA_FLAGS, (byte)0);
		this.dataWatcher.addObject(DATA_OWNER, "");
		this.dataWatcher.addObject(DATA_HEALTH, Integer.valueOf(this.health));
		this.dataWatcher.addObject(DATA_TEXTURE_VARIATION, (byte)0);
		int variation = 4;
		if(rand.nextInt(9) != 0) {
			variation = 1 + rand.nextInt(3);
		}
		this.setTextureVariation((byte) variation);		
	}
	
	public void setTextureVariation(byte variation) {
		this.dataWatcher.updateObject(DATA_TEXTURE_VARIATION, variation);
	}
	
	public byte getTextureVariation() {
		return this.dataWatcher.getWatchableObjectByte(DATA_TEXTURE_VARIATION);
	}

	@Override
	protected boolean canTriggerWalking() {
		return true;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("Angry", this.isAmazonAngry());
		compound.setBoolean("Sitting", this.getIsSitting());
		compound.setString("Owner", this.getOwner() == null ? "" : this.getOwner());
		compound.setByte("TextureVariation", this.getTextureVariation());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setAmazonAngry(compound.getBoolean("Angry"));
		this.setIsSitting(compound.getBoolean("Sitting"));
		String owner = compound.getString("Owner");
		if(owner.length() > 0) {
			this.setAmazonOwner(owner);
			this.setAmazonTamed(true);
		}
		if(compound.hasKey("TextureVariation")) {
			this.setTextureVariation(compound.getByte("TextureVariation"));
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected String getLivingSound() {
		return "";
	}

	protected String getHurtSoundByGender() {
		if(this.getTextureVariation() == 3) {
			return "random.hurt";
		} else {
			return "mob.amazon.hit";
		}
	}
	
	@Override
	protected String getHurtSound() {
		return this.getHurtSoundByGender();
	}

	@Override
	protected String getDeathSound() {
		return this.getHurtSoundByGender();
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
		if(!this.hasAttacked && !this.hasPath() && this.isAmazonTamed() && this.ridingEntity == null) {
			EntityPlayer owner = this.worldObj.getPlayerEntityByName(this.getOwner());
			if(owner != null) {
				float distance = owner.getDistanceToEntity(this);
				if(distance > 5.0F) {
					this.getPathOrWalkableBlock(owner, distance);
				}
			} else if(!this.isInWater()) {
				this.setIsSitting(true);
			}
		} else if(this.entityToAttack == null && !this.hasPath() && !this.isAmazonTamed() && this.worldObj.rand.nextInt(100) == 0) {
			AxisAlignedBB aabb = this.areaAroundMe(16.0D, 4.0D, 16.0D);
			List<Entity> zombies = this.worldObj.getEntitiesWithinAABB(EntityZombie.class, aabb);
			if(!zombies.isEmpty()) {
				this.setTarget(zombies.get(this.worldObj.rand.nextInt(zombies.size())));
			}
		}

		if(this.isInWater()) {
			this.setIsSitting(false);
		}

		if(!this.worldObj.isRemote) {
			this.dataWatcher.updateObject(DATA_HEALTH, this.health);
		}
		
		if(this.entityToAttack != null) {
			if(this.entityToAttack.getDistanceToEntity(this) < 3.0F) {
				this.swingItem();
			}
			
			if(!this.entityToAttack.isEntityAlive()) {
				this.entityToAttack = null;
				this.setAmazonAngry(false);
			}
			
			if(this.timeBeforeLosingInterest > 0) {
				this.timeBeforeLosingInterest --;
				if(this.timeBeforeLosingInterest == 0) {
					this.entityToAttack = null;
					this.setAmazonAngry(false);
				}
			}
		}
		
		if(this.isSwinging) {
			++this.swingProgressInt;
			if(this.swingProgressInt >= 8) {
				this.swingProgressInt = 0;
				this.isSwinging = false;
			}
		} else {
			this.swingProgressInt = 0;
		}

		this.swingProgress = (float)this.swingProgressInt / 8.0F;
	}
	
	public boolean interestingItem(int itemID) {
		return (itemID == Item.ruby.shiftedIndex || itemID == Item.emerald.shiftedIndex || itemID == Item.diamond.shiftedIndex || itemID == Block.blockGold.blockID);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.looksWithInterest = false;
		if(this.hasCurrentTarget() && !this.hasPath() && !this.isAmazonAngry()) {
			Entity target = this.getCurrentTarget();
			if(target instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)target;
				ItemStack heldItem = player.inventory.getCurrentItem();
				if(heldItem != null) {
					if(!this.isAmazonTamed() && this.interestingItem(heldItem.itemID)) {
						this.looksWithInterest = true;
					} else if(this.isAmazonTamed() && Item.itemsList[heldItem.itemID] instanceof ItemFood) {
						this.looksWithInterest = true;
					}
				}
			}
		}
		
		// Defend owner
		if(this.isAmazonTamed()) {
			Optional.ofNullable(this.worldObj.getPlayerEntityByName(this.getOwner()))
				.map(owner -> owner.lastAttackingEntity)
				.filter(attacker -> attacker instanceof EntityLiving && this.isValidTarget((EntityLiving)attacker))
				.ifPresent(attacker -> this.entityToAttack = attacker);
		}

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.prevHeadRoll = this.headRoll;
		if(this.looksWithInterest) {
			this.headRoll += (1.0F - this.headRoll) * 0.4F;
		} else {
			this.headRoll -= this.headRoll * 0.4F;
		}

		if(this.looksWithInterest) {
			this.numTicksToChaseTarget = 10;
		}
	}
	
	@Override
	public String getEntityTexture() {
		if(this.getMaxTextureVariations() > 1) {
			return "/mob/" + this.texturePrefix + this.dataWatcher.getWatchableObjectByte(DATA_TEXTURE_VARIATION) + ".png";		
		} else return this.texture;
	}

	public float getInterestedAngle(float deltaTicks) {
		return (this.prevHeadRoll + (this.headRoll - this.prevHeadRoll) * deltaTicks) * 0.15F * (float)Math.PI;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.8F;
	}

	@Override
	public int getVerticalFaceSpeed() {
		return this.getIsSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	private void getPathOrWalkableBlock(Entity target, float distance) {
		PathEntity path = this.worldObj.getPathToEntity(this, target, 16.0F);
		if(path == null && distance > 12.0F) {
			int startX = MathHelper.floor_double(target.posX) - 2;
			int startZ = MathHelper.floor_double(target.posZ) - 2;
			int floorY = MathHelper.floor_double(target.boundingBox.minY);

			for(int dx = 0; dx <= 4; ++dx) {
				for(int dz = 0; dz <= 4; ++dz) {
					if((dx < 1 || dz < 1 || dx > 3 || dz > 3) && this.worldObj.isBlockNormalCube(startX + dx, floorY - 1, startZ + dz) && !this.worldObj.isBlockNormalCube(startX + dx, floorY, startZ + dz) && !this.worldObj.isBlockNormalCube(startX + dx, floorY + 1, startZ + dz)) {
						this.setLocationAndAngles((double)((float)(startX + dx) + 0.5F), (double)floorY, (double)((float)(startZ + dz) + 0.5F), this.rotationYaw, this.rotationPitch);
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
		return this.getIsSitting();
	}

	@Override
	public boolean attackEntityFrom(Entity source, int damage) {
		this.setIsSitting(false);
		if(source != null && !(source instanceof EntityPlayer) && !(source instanceof EntityArrow)) {
			damage = (damage + 1) / 2;
		}
		
		// They can fight among them endlessly
		if(source instanceof EntityAmazon) damage = 0;

		boolean damaged = super.attackEntityFrom(source, damage);

		if(source instanceof EntityPlayer && ((EntityPlayer)source).isCreative) return true;
			
		if(!this.isAmazonTamed() && !this.isAmazonAngry()) {
			if(source instanceof EntityPlayer) {
				this.setAmazonAngry(true);
			}

			Entity attacker = (source instanceof EntityArrow && ((EntityArrow)source).shootingEntity != null)
				? ((EntityArrow)source).shootingEntity
				: source;

			if(attacker instanceof EntityLiving) {
				this.setEntityToAttack(attacker);
				
				AxisAlignedBB aabb = this.areaAroundMe(16.0D, 4.0D, 16.0D);
				this.worldObj.getEntitiesWithinAABB(EntityAmazon.class, aabb)
					.stream()
					.map(e -> (EntityAmazon)e)
					.filter(a -> !a.isAmazonTamed() && a.entityToAttack == null)
					.forEach(a -> {
						a.setEntityToAttack(attacker);
						if(attacker instanceof EntityPlayer) {
							a.setAmazonAngry(true);
						}
					});
			}
		} else if(source != this && source != null) {
			if(this.isAmazonTamed() && source instanceof EntityPlayer && ((EntityPlayer)source).username.equalsIgnoreCase(this.getOwner())) {
				return true;
			}

			this.setEntityToAttack(source);
		}

		return damaged;

	}

	@Override
	protected Entity findPlayerToAttack() {
		return this.isAmazonAngry() ? this.worldObj.getClosestPlayerToEntity(this, 16.0D) : null;
	}

	@Override
	protected void attackEntity(Entity target, float distance) {
		if(distance > 2.0F && distance < 6.0F && this.rand.nextInt(10) == 0) {
			if(this.onGround) {
				double dx = target.posX - this.posX;
				double dz = target.posZ - this.posZ;
				float hDist = MathHelper.sqrt_double(dx * dx + dz * dz);
				this.motionX = dx / (double)hDist * 0.5D * 0.8D + this.motionX * 0.2D;
				this.motionZ = dz / (double)hDist * 0.5D * 0.8D + this.motionZ * 0.2D;
				this.motionY = 0.4D;
			}
		} else if(distance < 1.5D && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			
			byte damage = this.isAmazonTamed() ? (byte)5 : (byte)4;

			target.attackEntityFrom(this, damage);
		}

	}

	@Override
	public boolean interact(EntityPlayer player) {
		if (super.interact(player)) {
			return false;
		}
		
		ItemStack heldItem = player.inventory.getCurrentItem();
		if(!this.isAmazonTamed()) {
			if(heldItem != null && this.interestingItem(heldItem.itemID)) {
				if (!player.isCreative) --heldItem.stackSize;
				if(heldItem.stackSize <= 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
				}

				if(!this.worldObj.isRemote) {
					if(this.rand.nextInt(8) == 0) {
						this.setAmazonTamed(true);
						this.setPathToEntity((PathEntity)null);
						this.health = 20;
						this.setAmazonOwner(player.username);
						this.showHeartsOrSmokeFX(true);
						this.worldObj.setEntityState(this, (byte)7);
						player.triggerAchievement(AchievementList.currency);
					} else {
						this.showHeartsOrSmokeFX(false);
						this.worldObj.setEntityState(this, (byte)6);
					}
				}

				return true;
			}
		} else {
			if(heldItem != null && Item.itemsList[heldItem.itemID] instanceof ItemFood) {
				if(this.dataWatcher.getWatchableObjectInt(DATA_HEALTH) < this.getFullHealth()) {
					if(!player.isCreative) --heldItem.stackSize;
					if(heldItem.stackSize <= 0) {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
					}

					this.heal(((ItemFood)heldItem.getItem()).getHealAmount());
					this.showHeartsOrSmokeFX(true);
					return true;
				}
			}

			if(player.username.equalsIgnoreCase(this.getOwner())) {
				if(!this.worldObj.isRemote) {
					this.setIsSitting(!this.getIsSitting());
					this.isJumping = false;
					this.setPathToEntity((PathEntity)null);
				}

				return true;
			}
		}

		return false;
	}

	private void showHeartsOrSmokeFX(boolean showHearts) {
		String particle = showHearts ? "heart" : "smoke";

		for(int i = 0; i < 7; ++i) {
			double vx = this.rand.nextGaussian() * 0.02D;
			double vy = this.rand.nextGaussian() * 0.02D;
			double vz = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(particle, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, vx, vy, vz);
		}

	}

	@Override
	public void handleHealthUpdate(byte state) {
		if(state == 7) {
			this.showHeartsOrSmokeFX(true);
		} else if(state == 6) {
			this.showHeartsOrSmokeFX(false);
		} else {
			super.handleHealthUpdate(state);
		}

	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	public String getOwner() {
		return this.dataWatcher.getWatchableObjectString(DATA_OWNER);
	}

	public void setAmazonOwner(String owner) {
		this.dataWatcher.updateObject(DATA_OWNER, owner);
	}

	public boolean getIsSitting() {
		return (this.getFlags() & FLAG_SITTING) != 0;
	}

	public void setIsSitting(boolean sitting) {
		this.setFlag(FLAG_SITTING, sitting);
	}

	public boolean isAmazonAngry() {
		return (this.getFlags() & FLAG_ANGRY) != 0;
	}

	public void setAmazonAngry(boolean angry) {
		this.setFlag(FLAG_ANGRY, angry);
	}

	public boolean isAmazonTamed() {
		return (this.getFlags() & FLAG_TAMED) != 0;
	}

	public void setAmazonTamed(boolean tamed) {
		this.setFlag(FLAG_TAMED, tamed);
	}

	private byte getFlags() {
		return this.dataWatcher.getWatchableObjectByte(DATA_FLAGS);
	}

	private void setFlag(int bit, boolean value) {
		byte flags = this.getFlags();
		this.dataWatcher.updateObject(DATA_FLAGS, value ? (byte)(flags | bit) : (byte)(flags & ~bit));
	}

	private AxisAlignedBB areaAroundMe(double x, double y, double z) {
		return AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(x, y, z);
	}
	
	@Override
	public int getFullHealth() {
		return 30;
	}
	
	@Override
	public void somebodyOpenedMyChest(EntityPlayer player) {
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBoxFromPool(player.posX, player.posY, player.posZ, player.posX + 1.0D, player.posY + 1.0D, player.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D);
		this.worldObj.getEntitiesWithinAABB(EntityAmazon.class, aabb)
			.stream()
			.map(e -> (EntityAmazon)e)
			.filter(a -> !a.isAmazonTamed() && a.entityToAttack == null)
			.forEach(a -> {
				a.setEntityToAttack(player);
				a.setAmazonAngry(true);
			});
	}
}

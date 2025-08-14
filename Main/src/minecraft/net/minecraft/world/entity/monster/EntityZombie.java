package net.minecraft.world.entity.monster;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.src.AchievementList;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.ISentient;
import net.minecraft.world.entity.block.EntityMeatBlock;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.pathfinder.PathPoint;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockDoor;

public class EntityZombie extends EntityArmoredMob implements IMob {
	
	protected int doorPosX;
	protected int doorPosY;
	protected int doorPosZ;
	
	protected float distanceX;
	protected float distanceZ;
	
	protected BlockDoor targetDoor = null;
	protected boolean chasingDoor = false;
	
	protected int doorBreakTime;
	protected String texturePrefix;
	
	public EntityZombie(World world1) {
		super(world1);
		this.texture = "/mob/zombie1.png";
		this.texturePrefix = "zombie";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
		this.scoreValue = 15;
		this.health = this.getFullHealth();
	}
	
	protected int getMaxTextureVariations() {
		return 5;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, (byte)0);
		this.setTextureVariation((byte) (1 + rand.nextInt(this.getMaxTextureVariations())));
	}
	
	public void setTextureVariation(byte variation) {
		this.dataWatcher.updateObject(16, variation);
	}
	
	public byte getTextureVariation() {
		return this.dataWatcher.getWatchableObjectByte(16);
	}
		
	@Override
	public String getEntityTexture() {
		if(this.getMaxTextureVariations() > 1) {
			return "/mob/" + this.texturePrefix + this.dataWatcher.getWatchableObjectByte(16) + ".png";		
		} else return this.texture;
	}
	
	@Override
	public void onLivingUpdate() {
		if(this.worldObj.isDaytime() && this.burnsOnDaylight()) {
			float f1 = this.getEntityBrightness(1.0F);
			if(f1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f1 - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		// Door interaction
		if (!this.chasingDoor) {
			if(this.worldObj.worldInfo.isBloodMoon() || rand.nextInt(4) == 0) {
				if(this.isCollidedHorizontally) {
					PathEntity pathEntity = this.pathToEntity;
					if(pathEntity != null && !pathEntity.isFinished()) {
						for (int i = 0; i < Math.min(pathEntity.getCurrentPathIndex() + 2, pathEntity.pathLength); i++) {
							PathPoint pathPoint = pathEntity.getPathPointFromIndex(i);
							int dpx = pathPoint.xCoord;
							int dpy = pathPoint.yCoord;
							int dpz = pathPoint.zCoord;
							
							if (this.getDistanceSq(dpx, dpy, dpz) <= 2.25D) {
								this.targetDoor = this.findDoorBlock(dpx, dpy, dpz);
								if (this.targetDoor != null) {
									this.doorPosX = dpx;
									this.doorPosY = dpy;
									this.doorPosZ = dpz;
									
									this.distanceX = (float)this.doorPosX + 0.5F - (float)this.posX;
									this.distanceZ = (float)this.doorPosZ + 0.5F - (float)this.posZ;
									this.chasingDoor = true;
									this.doorBreakTime = 240;
								}
							}
						}
					}		
				}
			}
		} else {
			float f1 = (float)this.doorPosX + 0.5F - (float)this.posX;
			float f2 = (float)this.doorPosZ + 0.5F - (float)this.posZ;
			float f = this.distanceX * f1 + this.distanceZ * f2;
			
			if (f < 0.0F) {
				this.chasingDoor = false;
			}
			
			if(this.rand.nextInt(20) == 0) {
				worldObj.playSoundAtEntity(this, "mob.zombie.wood", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
			
			if(--this.doorBreakTime == 0) {
				this.worldObj.setBlockWithNotify(doorPosX, doorPosY, doorPosZ, 0);
				worldObj.playSoundAtEntity(this, "mob.zombie.woodbreak", 2.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
		super.onLivingUpdate();
	}

	protected BlockDoor findDoorBlock(int x, int y, int z) {
		int blockID = this.worldObj.getBlockId(x, y, z);
		if (blockID != Block.doorWood.blockID) return null;
		BlockDoor blockDoor = (BlockDoor)Block.blocksList[blockID];
		return blockDoor;
	}
	
	@Override
	protected Entity findPlayerToAttack() {
		// Find close entities of type EntityMeatBlock
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityMeatBlock.class, this.boundingBox.expand(24.0D, 6.0D, 24.0D));
		Collections.sort(list, new AttackableTargetSorter(this));
		if(list.size() > 0) return list.get(0);
		
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D); 
		if(entityPlayer1 != null && !entityPlayer1.isCreative && this.canEntityBeSeen(entityPlayer1)) return entityPlayer1;
		
		// Find close entities of type EntityAmazon, EntityPigman/EntityCowman, etc
		list = this.worldObj.getEntitiesWithinAABB(ISentient.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		Collections.sort(list, new AttackableTargetSorter(this));
		
		// Return closest
		Iterator<Entity> iterator = list.iterator();		
		while(iterator.hasNext()) {
			EntityLiving entityLiving = (EntityLiving)iterator.next();
			if(this.isValidTarget(entityLiving)) return entityLiving;
		}
		
		return null;
	}
	
	public boolean isValidTarget(EntityLiving entityLiving) {
		if(entityLiving == null) return false;
		
		if(entityLiving == this) return false;
		
		if(!entityLiving.isEntityAlive()) return false;
		
		if(entityLiving.boundingBox.minY >= this.boundingBox.maxY || entityLiving.boundingBox.maxY <= this.boundingBox.minY) return false;
		
		return true;
	}
	
	public boolean burnsOnDaylight() {
		return true;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		byte textureVariation = 1;
		if(var1.hasKey("TextureVariation")) {
			textureVariation = var1.getByte("TextureVariation");
		} else {
			System.out.println("Importing old level, overrode texture variation for " + this.getClass() + " with default.");
		}
		this.setTextureVariation(textureVariation);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setByte("TextureVariation", this.getTextureVariation());
	}
	
	@Override
	protected String getLivingSound() {
		return "mob.zombie";
	}

	@Override
	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	@Override
	protected int getDropItemId() {
		return this.rand.nextInt(6) == 0 ? Item.rottenFlesh.shiftedIndex : Item.feather.shiftedIndex;
	}
	
	@Override
	public int getFullHealth() {
		return 20;
	}
	
	// Zombies take half damage during blood moons
	@Override
	public boolean attackEntityFrom(Entity entity, int damage) {
		if(this.worldObj.worldInfo.isBloodMoon() && entity instanceof EntityPlayer) damage >>= 1;
		return super.attackEntityFrom(entity, damage);
	}
	
	// Will fetch you if you are hiding!
	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();

		if(this.worldObj.getWorldInfo().isBloodMoon()) {
			if(this.entityToAttack == null) {
				if(this.homingTo != null) {
					this.pathToEntity = this.worldObj.getEntityPathToXYZ(this, (int)this.homingTo.xCoord, (int)this.homingTo.yCoord, (int)this.homingTo.zCoord, 32.0F);
				} else {
					EntityPlayer closestPlayer = this.worldObj.getClosestPlayerUnderRoof(this.posX, this.posY, this.posZ, 64.0D); 
					if(closestPlayer != null) {
						this.homingTo = Vec3D.createVectorHelper(closestPlayer.posX, closestPlayer.posY, closestPlayer.posZ);
					}
				}
			}
		} else {
			this.homingTo = null;
		}
	}
	
	@Override
	public void onDeath(Entity entity) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			entityPlayer.triggerAchievement(AchievementList.zombie);
		}
		
		super.onDeath(entity);
	}
}

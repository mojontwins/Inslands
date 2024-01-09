package net.minecraft.src;

import java.util.Collections;
import java.util.List;

public class EntityZombie extends EntityArmoredMob {
	
	protected int doorPosX;
	protected int doorPosY;
	protected int doorPosZ;
	
	protected float distanceX;
	protected float distanceZ;
	
	protected BlockDoor targetDoor = null;
	protected boolean chasingDoor = false;
	
	protected int doorBreakTime;
	public EntityZombie(World world1) {
		super(world1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime() && this.burnsOnDaylight()) {
			float f1 = this.getEntityBrightness(1.0F);
			if(f1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f1 - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		// Door interaction
		if (!this.chasingDoor) {
			if(this.worldObj.difficultySetting >= 2 || rand.nextInt(4) == 0) {
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
				System.out.println("CRACK!");
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
	
	protected Entity findPlayerToAttack() {
		// My zombies will also attack pigmen.
		// But first of all they will be attracted by EntityMeatBlock
		
		// Find close entities of type EntityMeatBlock
		List<Entity> list = this.worldObj.getEntitiesWithinAABB(EntityMeatBlock.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		Collections.sort(list, new AttackableTargetSorter(this));
		if(list.size() > 0) return list.get(0);
		
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		if(entityPlayer1 != null && !entityPlayer1.isCreative && this.canEntityBeSeen(entityPlayer1)) return entityPlayer1;
		
		// Find close entities of type EntityAmazon
		//list = this.worldObj.getEntitiesWithinAABB(EntityAmazon.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D));
		
		// Find close entities of type EntityPigman
		/*
		list.addAll(this.worldObj.getEntitiesWithinAABB(EntityPigman.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D)));
		list.addAll(this.worldObj.getEntitiesWithinAABB(EntityCowman.class, this.boundingBox.expand(16.0D, 4.0D, 16.0D)));
		Collections.sort(list, new AttackableTargetSorter(this));
		*/
		/*
		Iterator<Entity> iterator = list.iterator();		
		while(iterator.hasNext()) {
			EntityLiving entityLiving = (EntityLiving)iterator.next();
			if(this.isValidTarget(entityLiving)) return entityLiving;
		}
		*/
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

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		//byte textureVariation = 1;
		if(var1.hasKey("TextureVariation")) {
			//textureVariation = var1.getByte("TextureVariation");
		} else {
			//System.out.println("Importing old level, overrode texture variation for " + this.getClass() + " with default.");
		}
		//this.setTextureVariation(textureVariation);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		//var1.setByte("TextureVariation", this.getTextureVariation());
	}
	
	protected String getLivingSound() {
		return "mob.zombie";
	}

	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	protected int getDropItemId() {
		return Item.feather.shiftedIndex;
	}
	
	public int getFullHealth() {
		return 20;
	}
}

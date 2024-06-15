package net.minecraft.src;

import com.bigbang87.deadlymonsters.EntityHauntedCow;

public class EntityCow extends EntityAnimal {
	public EntityCow(World world1) {
		super(world1);
		this.texture = "/mob/cow.png";
		this.setSize(0.9F, 1.3F);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	protected String getLivingSound() {
		return "mob.cow";
	}

	protected String getHurtSound() {
		return "mob.cowhurt";
	}

	protected String getDeathSound() {
		return "mob.cowhurt";
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		return Item.leather.shiftedIndex;
	}

	public boolean interact(EntityPlayer entityPlayer1) {
		ItemStack itemStack2 = entityPlayer1.inventory.getCurrentItem();
		if(itemStack2 != null && itemStack2.itemID == Item.bucketEmpty.shiftedIndex) {
			entityPlayer1.inventory.setInventorySlotContents(entityPlayer1.inventory.currentItem, new ItemStack(Item.bucketMilk));
			return true;
		} else {
			return false;
		}
	}
	
	public boolean attackEntityFrom(Entity entity1, int i2) {
		if(super.attackEntityFrom(entity1, i2) == false) {
			return false;
		}
		
		if(!this.worldObj.isRemote) {
			
			boolean hauntMe = false;
			
			if(entity1 instanceof EntityPlayer) {
				EntityPlayer entityPlayer = (EntityPlayer)entity1;
				ItemStack heldItemStack = entityPlayer.getCurrentEquippedItem();
				
				// Turn into haunted cow?
				if(heldItemStack == null || (heldItemStack.getItem() instanceof ItemSword) || (heldItemStack.getItem() instanceof ItemBow)) {
					return true;
				}
			
				if(this.rand.nextInt(50) != 0) {
					return true;
				}
				
				// Knock player back a bit
				entityPlayer.addVelocity(
						(double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) *  0.5F), 
						0.1D, 
						(double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) *  0.5F)
					);
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
				
				hauntMe = true;
			}
			
			if(entity1 instanceof EntityHauntedCow) {
				hauntMe = true;
			}
			
			if(hauntMe) {
				// Add lighting
				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ));
				
				// Create haunted cow!
				this.setEntityDead();
				EntityHauntedCow entityHauntedCow = new EntityHauntedCow(this.worldObj);
				entityHauntedCow.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				this.worldObj.entityJoinedWorld(entityHauntedCow);
				
				// Night time!
				long worldTime = this.worldObj.getWorldTime();
				long dayTime = worldTime % 24000;
				if(dayTime < 18000) {
					this.worldObj.setWorldTime((worldTime / 24000) * 24000 + 18000);
				}
			}
		}
		
		return true;
	}
}

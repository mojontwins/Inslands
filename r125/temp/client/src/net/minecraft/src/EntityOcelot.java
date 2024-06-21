package net.minecraft.src;

public class EntityOcelot extends EntityTameable {
	private EntityAITempt aiTempt;

	public EntityOcelot(World world1) {
		super(world1);
		this.texture = "/mob/ozelot.png";
		this.setSize(0.6F, 0.8F);
		this.getNavigator().func_48664_a(true);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, this.aiTempt = new EntityAITempt(this, 0.18F, Item.fishRaw.shiftedIndex, true));
		this.tasks.addTask(4, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.23F, 0.4F));
		this.tasks.addTask(5, new EntityAIFollowOwner(this, 0.3F, 10.0F, 5.0F));
		this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.4F));
		this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
		this.tasks.addTask(8, new EntityAIOcelotAttack(this));
		this.tasks.addTask(9, new EntityAIMate(this, 0.23F));
		this.tasks.addTask(10, new EntityAIWander(this, 0.23F));
		this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
		this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, 14.0F, 750, false));
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(18, (byte)0);
	}

	public void updateAITick() {
		if(!this.getMoveHelper().func_48186_a()) {
			this.setSneaking(false);
			this.setSprinting(false);
		} else {
			float f1 = this.getMoveHelper().getSpeed();
			if(f1 == 0.18F) {
				this.setSneaking(true);
				this.setSprinting(false);
			} else if(f1 == 0.4F) {
				this.setSneaking(false);
				this.setSprinting(true);
			} else {
				this.setSneaking(false);
				this.setSprinting(false);
			}
		}

	}

	protected boolean canDespawn() {
		return !this.isTamed();
	}

	public String getTexture() {
		switch(this.func_48148_ad()) {
		case 0:
			return "/mob/ozelot.png";
		case 1:
			return "/mob/cat_black.png";
		case 2:
			return "/mob/cat_red.png";
		case 3:
			return "/mob/cat_siamese.png";
		default:
			return super.getTexture();
		}
	}

	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 10;
	}

	protected void fall(float f1) {
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setInteger("CatType", this.func_48148_ad());
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.func_48147_c(nBTTagCompound1.getInteger("CatType"));
	}

	protected String getLivingSound() {
		return this.isTamed() ? (this.isInLove() ? "mob.cat.purr" : (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
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
		return Item.leather.shiftedIndex;
	}

	public boolean attackEntityAsMob(Entity entity1) {
		return entity1.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		this.aiSit.func_48407_a(false);
		return super.attackEntityFrom(damageSource1, i2);
	}

	protected void dropFewItems(boolean z1, int i2) {
	}

	public boolean interact(EntityPlayer entityPlayer1) {
		ItemStack itemStack2 = entityPlayer1.inventory.getCurrentItem();
		if(!this.isTamed()) {
			if(this.aiTempt.func_48270_h() && itemStack2 != null && itemStack2.itemID == Item.fishRaw.shiftedIndex && entityPlayer1.getDistanceSqToEntity(this) < 9.0D) {
				--itemStack2.stackSize;
				if(itemStack2.stackSize <= 0) {
					entityPlayer1.inventory.setInventorySlotContents(entityPlayer1.inventory.currentItem, (ItemStack)null);
				}

				if(!this.worldObj.isRemote) {
					if(this.rand.nextInt(3) == 0) {
						this.setTamed(true);
						this.func_48147_c(1 + this.worldObj.rand.nextInt(3));
						this.setOwner(entityPlayer1.username);
						this.func_48142_a(true);
						this.aiSit.func_48407_a(true);
						this.worldObj.setEntityState(this, (byte)7);
					} else {
						this.func_48142_a(false);
						this.worldObj.setEntityState(this, (byte)6);
					}
				}
			}

			return true;
		} else {
			if(entityPlayer1.username.equalsIgnoreCase(this.getOwnerName()) && !this.worldObj.isRemote && !this.isWheat(itemStack2)) {
				this.aiSit.func_48407_a(!this.isSitting());
			}

			return super.interact(entityPlayer1);
		}
	}

	public EntityAnimal spawnBabyAnimal(EntityAnimal entityAnimal1) {
		EntityOcelot entityOcelot2 = new EntityOcelot(this.worldObj);
		if(this.isTamed()) {
			entityOcelot2.setOwner(this.getOwnerName());
			entityOcelot2.setTamed(true);
			entityOcelot2.func_48147_c(this.func_48148_ad());
		}

		return entityOcelot2;
	}

	public boolean isWheat(ItemStack itemStack1) {
		return itemStack1 != null && itemStack1.itemID == Item.fishRaw.shiftedIndex;
	}

	public boolean func_48135_b(EntityAnimal entityAnimal1) {
		if(entityAnimal1 == this) {
			return false;
		} else if(!this.isTamed()) {
			return false;
		} else if(!(entityAnimal1 instanceof EntityOcelot)) {
			return false;
		} else {
			EntityOcelot entityOcelot2 = (EntityOcelot)entityAnimal1;
			return !entityOcelot2.isTamed() ? false : this.isInLove() && entityOcelot2.isInLove();
		}
	}

	public int func_48148_ad() {
		return this.dataWatcher.getWatchableObjectByte(18);
	}

	public void func_48147_c(int i1) {
		this.dataWatcher.updateObject(18, (byte)i1);
	}

	public boolean getCanSpawnHere() {
		if(this.worldObj.rand.nextInt(3) == 0) {
			return false;
		} else {
			if(this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox)) {
				int i1 = MathHelper.floor_double(this.posX);
				int i2 = MathHelper.floor_double(this.boundingBox.minY);
				int i3 = MathHelper.floor_double(this.posZ);
				if(i2 < 63) {
					return false;
				}

				int i4 = this.worldObj.getBlockId(i1, i2 - 1, i3);
				if(i4 == Block.grass.blockID || i4 == Block.leaves.blockID) {
					return true;
				}
			}

			return false;
		}
	}
}

package net.minecraft.src;

import java.util.List;

public class EntityPigZombie extends EntityZombie {
	private int angerLevel = 0;
	private int randomSoundDelay = 0;
	private static final ItemStack defaultHeldItem = new ItemStack(Item.swordGold, 1);

	public EntityPigZombie(World world1) {
		super(world1);
		this.texture = "/mob/pigzombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
		this.isImmuneToFire = true;
	}

	protected boolean isAIEnabled() {
		return false;
	}

	public void onUpdate() {
		this.moveSpeed = this.entityToAttack != null ? 0.95F : 0.5F;
		if(this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
			this.worldObj.playSoundAtEntity(this, "mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		super.onUpdate();
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.difficultySetting > 0 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setShort("Anger", (short)this.angerLevel);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.angerLevel = nBTTagCompound1.getShort("Anger");
	}

	protected Entity findPlayerToAttack() {
		return this.angerLevel == 0 ? null : super.findPlayerToAttack();
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		Entity entity3 = damageSource1.getEntity();
		if(entity3 instanceof EntityPlayer) {
			List list4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));

			for(int i5 = 0; i5 < list4.size(); ++i5) {
				Entity entity6 = (Entity)list4.get(i5);
				if(entity6 instanceof EntityPigZombie) {
					EntityPigZombie entityPigZombie7 = (EntityPigZombie)entity6;
					entityPigZombie7.becomeAngryAt(entity3);
				}
			}

			this.becomeAngryAt(entity3);
		}

		return super.attackEntityFrom(damageSource1, i2);
	}

	private void becomeAngryAt(Entity entity1) {
		this.entityToAttack = entity1;
		this.angerLevel = 400 + this.rand.nextInt(400);
		this.randomSoundDelay = this.rand.nextInt(40);
	}

	protected String getLivingSound() {
		return "mob.zombiepig.zpig";
	}

	protected String getHurtSound() {
		return "mob.zombiepig.zpighurt";
	}

	protected String getDeathSound() {
		return "mob.zombiepig.zpigdeath";
	}

	protected void dropFewItems(boolean z1, int i2) {
		int i3 = this.rand.nextInt(2 + i2);

		int i4;
		for(i4 = 0; i4 < i3; ++i4) {
			this.dropItem(Item.rottenFlesh.shiftedIndex, 1);
		}

		i3 = this.rand.nextInt(2 + i2);

		for(i4 = 0; i4 < i3; ++i4) {
			this.dropItem(Item.goldNugget.shiftedIndex, 1);
		}

	}

	protected void dropRareDrop(int i1) {
		if(i1 > 0) {
			ItemStack itemStack2 = new ItemStack(Item.swordGold);
			EnchantmentHelper.func_48622_a(this.rand, itemStack2, 5);
			this.entityDropItem(itemStack2, 0.0F);
		} else {
			int i3 = this.rand.nextInt(3);
			if(i3 == 0) {
				this.dropItem(Item.ingotGold.shiftedIndex, 1);
			} else if(i3 == 1) {
				this.dropItem(Item.swordGold.shiftedIndex, 1);
			} else if(i3 == 2) {
				this.dropItem(Item.helmetGold.shiftedIndex, 1);
			}
		}

	}

	protected int getDropItemId() {
		return Item.rottenFlesh.shiftedIndex;
	}
}

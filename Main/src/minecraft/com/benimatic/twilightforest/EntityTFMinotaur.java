package com.benimatic.twilightforest;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.world.entity.ai.EntityAIHurtByTarget;
import net.minecraft.world.entity.ai.EntityAILookIdle;
import net.minecraft.world.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.world.entity.ai.EntityAISwimming;
import net.minecraft.world.entity.ai.EntityAIWander;
import net.minecraft.world.entity.ai.EntityAIWatchClosest;
import net.minecraft.world.entity.monster.EntityMob;
import net.minecraft.world.entity.player.EntityPlayer;

public class EntityTFMinotaur extends EntityMob implements IMob {
	private static final ItemStack defaultHeldItem = new ItemStack(Item.axeSteel, 1);

	public EntityTFMinotaur(World var1) {
		super(var1);
		this.texture = "/mob/minotaur.png";
		this.moveSpeed = 0.25F;
		this.attackStrength = 7;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAITFChargeAttack(this, 0.55F));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
		this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, false));
	}

	public int getMaxHealth() {
		return 30;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(17, Byte.valueOf((byte) 0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}

	public boolean isCharging() {
		return this.dataWatcher.getWatchableObjectByte(17) != 0;
	}

	public void setCharging(boolean var1) {
		if (var1) {
			this.dataWatcher.updateObject(17, Byte.valueOf((byte) 127));
		} else {
			this.dataWatcher.updateObject(17, Byte.valueOf((byte) 0));
		}
	}

	public boolean attackEntityAsMob(Entity var1) {
		boolean var2 = super.attackEntityAsMob(var1);

		if (var2 && this.isCharging()) {
			var1.motionY += 0.4000000059604645D;
			this.worldObj.playSoundAtEntity(this, "mob.irongolem.throw", 1.0F, 1.0F);
		}

		return var2;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.isCharging()) {
			this.limbYaw = (float) ((double) this.limbYaw + 0.6D);
		}
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.cow";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.cowhurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.cowhurt";
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.beefRaw.shiftedIndex;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems() {
		int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + 1);

		for (int var4 = 0; var4 < var3; ++var4) {
			if (this.isBurning()) {
				this.dropItem(Item.beefCooked.shiftedIndex, 1);
			} else {
				this.dropItem(Item.beefRaw.shiftedIndex, 1);
			}
		}
	}

	protected void dropRareDrop(int var1) {
		this.dropItem(Item.axeGold.shiftedIndex, 1);
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(Entity var1) {
		super.onDeath(var1);

		if (var1 instanceof EntityPlayer) {
			// TODO: ACHIEVEMENT!!!!!
			//((EntityPlayer) var1).triggerAchievement(AchievementList.twilightHunter);
		}
	}
}

package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.ICaveMob;
import net.minecraft.world.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.world.entity.ai.EntityAIHurtByTarget;
import net.minecraft.world.entity.ai.EntityAILeapAtTarget;
import net.minecraft.world.entity.ai.EntityAILookIdle;
import net.minecraft.world.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.world.entity.ai.EntityAISwimming;
import net.minecraft.world.entity.ai.EntityAITFFlockToSameKind;
import net.minecraft.world.entity.ai.EntityAITFPanicOnFlockDeath;
import net.minecraft.world.entity.ai.EntityAIWander;
import net.minecraft.world.entity.ai.EntityAIWatchClosest;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;

public class EntityTFKobold extends EntityMob implements ICaveMob {
	private boolean shy;

	public EntityTFKobold(World var1) {
		super(var1);
		this.texture = "/mob/kobold.png";
		this.moveSpeed = 0.28F;
		this.setSize(0.8F, 1.1F);
		this.attackStrength = 3;
		this.shy = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAITFPanicOnFlockDeath(this, 0.38F));
		this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.28F));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
		this.tasks.addTask(4, new EntityAITFFlockToSameKind(this, this.moveSpeed));
		this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
	}

	public EntityTFKobold(World var1, double var2, double var4, double var6) {
		this(var1);
		this.setPosition(var2, var4, var6);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(17, Byte.valueOf((byte) 0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 13;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.tf.kobold.kobold";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.tf.kobold.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.tf.kobold.die";
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.wheat.shiftedIndex;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems() {
		super.dropFewItems();

		if (this.rand.nextInt(2) == 0) {
			this.dropItem(Item.ingotGold.shiftedIndex, 1 + 1);
		}
	}

	public boolean isShy() {
		return this.shy && this.recentlyHit <= 0;
	}

	public boolean isPanicked() {
		return this.dataWatcher.getWatchableObjectByte(17) != 0;
	}

	public void setPanicked(boolean var1) {
		if (var1) {
			this.dataWatcher.updateObject(17, Byte.valueOf((byte) 127));
		} else {
			this.dataWatcher.updateObject(17, Byte.valueOf((byte) 0));
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.isPanicked()) {
			for (int var1 = 0; var1 < 2; ++var1) {
				this.worldObj.spawnParticle("splash",
						this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width * 0.5D,
						this.posY + (double) this.getEyeHeight(),
						this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width * 0.5D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 8;
	}
}

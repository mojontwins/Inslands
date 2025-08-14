package net.minecraft.world.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.src.AchievementList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.IMob;
import net.minecraft.world.entity.ai.EntityAIFleeSun;
import net.minecraft.world.entity.ai.EntityAIHurtByTarget;
import net.minecraft.world.entity.ai.EntityAILookIdle;
import net.minecraft.world.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.world.entity.ai.EntityAIRestrictSun;
import net.minecraft.world.entity.ai.EntityAISwimming;
import net.minecraft.world.entity.ai.EntityAITFMagicAttack;
import net.minecraft.world.entity.ai.EntityAIWander;
import net.minecraft.world.entity.ai.EntityAIWatchClosest;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;

public class EntityTFSkeletonDruid extends EntityMob implements IMob {
	private static final ItemStack defaultHeldItem = new ItemStack(Item.hoeGold, 1);

	public EntityTFSkeletonDruid(World world) {
		super(world);
		this.texture = "/mob/skeletonwitch.png";
		this.moveSpeed = 0.25F;
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, this.moveSpeed));
		this.tasks.addTask(4, new EntityAITFMagicAttack(this, this.moveSpeed, 1, 60));
		this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
	}

	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 30;
	}

	protected String getLivingSound() {
		return "mob.skeleton";
	}

	protected String getHurtSound() {
		return "mob.skeletonhurt";
	}

	protected String getDeathSound() {
		return "mob.skeletonhurt";
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	protected int getDropItemId() {
		return Item.hoeGold.shiftedIndex;
	}

	protected void dropFewItems() {
		if(this.rand.nextInt(3) == 0) {
			this.dropItem(Item.stick.shiftedIndex, 1);
		}

		int i = this.rand.nextInt(3);

		for(int k = 0; k < i; ++k) {
			this.dropItem(Item.bone.shiftedIndex, 1);
		}

	}

	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}

	@Override
	public void onDeath(Entity entity) {
		super.onDeath(entity);
		if(entity instanceof EntityPlayer) {
			((EntityPlayer)entity).triggerAchievement(AchievementList.twilightHunter);
		}

	}
	
	@Override
	public boolean needsLightCheck() {
		return false;
	}
}

package net.minecraft.world.entity.monster;

import net.minecraft.src.AchievementList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;

public class EntityTFMinoshroom extends EntityTFMinotaur {
	private static final ItemStack defaultHeldItem = new ItemStack(Item.axeGold, 1);

	public EntityTFMinoshroom(World var1) {
		super(var1);
		this.texture = "/mob/minoshroomtaur.png";
		this.setSize(1.49F, 2.9F);
	}

	public int getMaxHealth() {
		return 200;
	}

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.beefCooked.shiftedIndex;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems() {
		int var3 = this.rand.nextInt(4) + 2 + this.rand.nextInt(1 + 1);

		for (int var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.beefCooked.shiftedIndex, 1);
		}

		this.dropItem(Item.superAxe.shiftedIndex, 1);
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public void onDeath(Entity entity) {
		if(entity instanceof EntityPlayer) {
			((EntityPlayer)entity).triggerAchievement(AchievementList.minoshroom);
		}
	}
}

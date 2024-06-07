package com.benimatic.twilightforest;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class EntityTFMinoshroom extends EntityTFMinotaur {
	private static final ItemStack defaultHeldItem = new ItemStack(Item.axeGold, 1);

	public EntityTFMinoshroom(World var1) {
		super(var1);
		this.texture = "/mob/twilightForest/minoshroomtaur.png";
		this.setSize(1.49F, 2.9F);
	}

	public int getMaxHealth() {
		return 120;
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
	protected void dropFewItems(boolean var1, int var2) {
		int var3 = this.rand.nextInt(4) + 2 + this.rand.nextInt(1 + var2);

		for (int var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.beefCooked.shiftedIndex, 1);
		}

		this.dropItem(Item.axeGold.shiftedIndex, 1); // TODO : CHANGE TO SUPER AXE
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return false;
	}
}

package net.minecraft.world.entity.animal.farm;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLightningBolt;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.mob.undead.EntityHauntedCow;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemBow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemSword;
import net.minecraft.world.level.World;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class EntityCow extends EntityAnimal {
	public EntityCow(World world) {
		super(world);
		this.texture = "/mob/cow.png";
		this.setSize(0.9F, 1.3F);
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

	public boolean interact(EntityPlayer player) {
		if (super.interact(player)) {
			return false;
		}

		ItemStack heldItem = player.inventory.getCurrentItem();
		if (heldItem != null && heldItem.itemID == Item.bucketEmpty.shiftedIndex) {
			player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Item.bucketMilk));
			return true;
		} else {
			return false;
		}
	}

	public boolean attackEntityFrom(Entity source, int damage) {
		if (!super.attackEntityFrom(source, damage)) {
			return false;
		}

		if (!this.worldObj.isRemote && LevelThemeGlobalSettings.hauntCows) {
			boolean hauntMe = false;

			if (source instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) source;
				ItemStack heldItem = player.getCurrentEquippedItem();

				// Turn into haunted cow?
				if (heldItem == null || heldItem.getItem() instanceof ItemSword || heldItem.getItem() instanceof ItemBow) {
					return true;
				}

				// Knock the player back a bit
				player.addVelocity(
						(double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * 0.5F),
						0.1D,
						(double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;

				hauntMe = true;
			}

			if (source instanceof EntityHauntedCow) {
				hauntMe = true;
			}

			if (hauntMe) {
				// Add lightning
				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ));

				// Create the haunted cow!
				this.setEntityDead();
				EntityHauntedCow hauntedCow = new EntityHauntedCow(this.worldObj);
				hauntedCow.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				this.worldObj.spawnEntityInWorld(hauntedCow);

				// Set to night time!
				long worldTime = this.worldObj.getWorldTime();
				long dayTime = worldTime % 24000;
				if (dayTime < 18000) {
					this.worldObj.setWorldTime((worldTime / 24000) * 24000 + 18000);
				}
			}
		}

		return true;
	}
}

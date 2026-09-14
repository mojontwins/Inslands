package net.minecraft.world.entity.animal.farm;

import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemSword;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.World;

public class EntityMooshroom extends EntityCow {
	public EntityMooshroom(World world) {
		super(world);
		this.texture = "/mob/redcow.png";
		this.setSize(0.9F, 1.3F);
	}

	@Override
	public boolean interact(EntityPlayer player) {
		if (super.interact(player)) {
			return false;
		}

		ItemStack heldItem = player.inventory.getCurrentItem();

		if (heldItem != null && heldItem.itemID == Item.bowlEmpty.shiftedIndex) {
			if (heldItem.stackSize == 1) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Item.bowlSoup));
				return true;
			}

			if (player.inventory.addItemStackToInventory(new ItemStack(Item.bowlSoup)) && !player.isCreative) {
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
				return true;
			}
		}

		if (heldItem != null && heldItem.getItem() instanceof ItemSword) {
			this.setEntityDead();
			this.worldObj.spawnParticle("largeexplode", this.posX, this.posY + (double) (this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D);

			if (!this.worldObj.isRemote) {
				EntityCow cow = new EntityCow(this.worldObj);
				cow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				cow.health = this.health;
				cow.renderYawOffset = this.renderYawOffset;
				this.worldObj.spawnEntityInWorld(cow);

				for (int i = 0; i < 5; i++) {
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + (double) this.height, this.posZ, new ItemStack(Block.mushroomRed)));
				}
			}

			return true;
		} else {
			return super.interact(player);
		}
	}
}

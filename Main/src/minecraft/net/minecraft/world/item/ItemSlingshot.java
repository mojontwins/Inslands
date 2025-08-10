package net.minecraft.world.item;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.src.World;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityPebble;

public class ItemSlingshot extends Item {
	public ItemSlingshot(int i1) {
		super(i1);
		this.maxStackSize = 1;
		
		this.displayOnCreativeTab = CreativeTabs.tabCombat;
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(entityPlayer.isCreative || entityPlayer.inventory.consumeInventoryItem(Item.pebble.shiftedIndex)) {
			world.playSoundAtEntity(entityPlayer, "random.bow", 1.0F, 1.0F / (Item.rand.nextFloat() * 0.4F + 0.8F));
			if(!world.isRemote) {
				world.spawnEntityInWorld(new EntityPebble(world, entityPlayer));
			}
		}

		return itemStack;
	}
}

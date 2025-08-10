package net.minecraft.world.item;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.src.World;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityArrow;

public class ItemBow extends Item {
	public ItemBow(int i1) {
		super(i1);
		this.maxStackSize = 1;
		this.setMaxDamage(384);
		
		this.displayOnCreativeTab = CreativeTabs.tabCombat;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(entityPlayer3.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			world2.playSoundAtEntity(entityPlayer3, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			if(!world2.isRemote) {
				world2.spawnEntityInWorld(new EntityArrow(world2, entityPlayer3));
			}
		}

		return itemStack1;
	}
}

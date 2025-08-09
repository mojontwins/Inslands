package net.minecraft.src;

import com.mojang.minecraft.creative.CreativeTabs;

import net.minecraft.world.entity.monster.EntitySnowball;
import net.minecraft.world.entity.player.EntityPlayer;

public class ItemSnowball extends Item {
	public ItemSnowball(int i1) {
		super(i1);
		this.maxStackSize = 16;
		
		this.displayOnCreativeTab = CreativeTabs.tabMisc;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(!entityPlayer3.isCreative) --itemStack1.stackSize;
		world2.playSoundAtEntity(entityPlayer3, "random.bow", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
		if(!world2.isRemote) {
			world2.spawnEntityInWorld(new EntitySnowball(world2, entityPlayer3));
		}

		return itemStack1;
	}
}

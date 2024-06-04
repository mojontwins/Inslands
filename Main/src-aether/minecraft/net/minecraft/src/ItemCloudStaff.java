package net.minecraft.src;

import java.util.List;

public class ItemCloudStaff extends Item {
	public ItemCloudStaff(int i) {
		super(i);
		this.maxStackSize = 1;
		this.setMaxDamage(60);
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(!this.cloudsExist(world, entityplayer)) {
			EntityMiniCloud c1 = new EntityMiniCloud(world, entityplayer, false);
			EntityMiniCloud c2 = new EntityMiniCloud(world, entityplayer, true);
			world.entityJoinedWorld(c1);
			world.entityJoinedWorld(c2);
			itemstack.damageItem(1, (Entity)null);
		}

		return itemstack;
	}

	private boolean cloudsExist(World world, EntityPlayer entityplayer) {
		List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.expand(128.0D, 128.0D, 128.0D));

		for(int j = 0; j < list.size(); ++j) {
			Entity entity1 = (Entity)list.get(j);
			if(entity1 instanceof EntityMiniCloud) {
				return true;
			}
		}

		return false;
	}
}

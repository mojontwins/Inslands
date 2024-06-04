package net.minecraft.src;

public class ItemLightningKnife extends Item {
	public ItemLightningKnife(int i) {
		super(i);
		this.maxStackSize = 16;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		--itemstack.stackSize;
		world.playSoundAtEntity(entityplayer, "mob.aether.dartshoot", 2.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if(!world.multiplayerWorld) {
			world.entityJoinedWorld(new EntityLightningKnife(world, entityplayer));
		}

		return itemstack;
	}
}

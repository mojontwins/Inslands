package net.minecraft.src;

public class ItemPhoenixBow extends Item {
	public ItemPhoenixBow(int i) {
		super(i);
		this.maxStackSize = 1;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(entityplayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex)) {
			world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if(!world.multiplayerWorld) {
				world.entityJoinedWorld(new EntityFlamingArrow(world, entityplayer));
			}
		}

		return itemstack;
	}
}

package net.minecraft.src;

public class ItemCloudParachute extends Item {
	private static int tex = ModLoader.addOverride("/gui/items.png", "/aether/items/CloudParachute.png");

	public ItemCloudParachute(int i) {
		super(i);
		this.setIconIndex(tex);
		this.maxStackSize = 1;
		this.setMaxDamage(i == mod_Aether.idItemCloudParachuteGold ? 20 : 0);
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(EntityCloudParachute.entityHasRoomForCloud(world, entityplayer)) {
			for(int i = 0; i < 32; ++i) {
				EntityCloudParachute.doCloudSmoke(world, entityplayer);
			}

			if(this.shiftedIndex == AetherItems.CloudParachuteGold.shiftedIndex) {
				itemstack.damageItem(1, entityplayer);
			} else {
				--itemstack.stackSize;
			}

			world.playSoundAtEntity(entityplayer, "cloud", 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F + 0.95F));
			if(!world.multiplayerWorld) {
				world.entityJoinedWorld(new EntityCloudParachute(world, entityplayer, this.shiftedIndex == AetherItems.CloudParachuteGold.shiftedIndex));
			}
		}

		return itemstack;
	}

	public int getColorFromDamage(int i) {
		return this.shiftedIndex == AetherItems.CloudParachuteGold.shiftedIndex ? 16777087 : 0xFFFFFF;
	}
}

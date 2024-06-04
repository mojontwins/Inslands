package net.minecraft.src;

public class ItemNotchHammer extends Item {
	private int weaponDamage;

	public ItemNotchHammer(int i) {
		super(i);
		this.maxStackSize = 1;
		this.setMaxDamage(EnumToolMaterial.IRON.getMaxUses());
		this.weaponDamage = 4 + EnumToolMaterial.IRON.getDamageVsEntity() * 2;
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return 1.5F;
	}

	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		itemstack.damageItem(1, entityliving1);
		return true;
	}

	public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
		itemstack.damageItem(2, entityliving);
		return true;
	}

	public int getDamageVsEntity(Entity entity) {
		return this.weaponDamage;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		itemstack.damageItem(1, entityplayer);
		world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if(!world.multiplayerWorld) {
			EntityNotchWave notchwave = new EntityNotchWave(world, entityplayer);
			world.entityJoinedWorld(notchwave);
		}

		return itemstack;
	}

	public boolean isFull3D() {
		return true;
	}
}

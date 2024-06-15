package net.minecraft.src;

import java.util.Random;

public class ItemVampireBlade extends Item {
	private int weaponDamage;
	private static Random random = new Random();

	public ItemVampireBlade(int i) {
		super(i);
		this.maxStackSize = 1;
		this.setMaxDamage(EnumToolMaterial.EMERALD.getMaxUses());
		this.weaponDamage = 4 + EnumToolMaterial.EMERALD.getDamageVsEntity() * 2;
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return 1.5F;
	}

	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		EntityPlayer player = (EntityPlayer)entityliving1;
		if(player.health < mod_Aether.getPlayer(player).maxHealth) {
			++player.health;
		}

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

	public boolean isFull3D() {
		return true;
	}
}

package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;

public final class ItemSword extends Item {
	private int weaponDamage;

	public ItemSword(int i1, int i2) {
		super(i1);
		this.maxStackSize = 1;
		this.maxDamage = 32 << i2;
		this.weaponDamage = 4 + (i2 << 1);
	}

	public final float getStrVsBlock(Block block1) {
		return 1.5F;
	}

	public final void hitEntity(ItemStack itemStack1) {
		itemStack1.damageItem(1);
	}

	public final void onBlockDestroyed(ItemStack itemStack1) {
		itemStack1.damageItem(2);
	}

	public final int getDamageVsEntity() {
		return this.weaponDamage;
	}
}
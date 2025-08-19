package net.minecraft.game.item;

import net.minecraft.game.world.block.Block;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;

	public ItemTool(int i1, int i2, int i3, Block[] block4) {
		super(i1);
		this.blocksEffectiveAgainst = block4;
		this.maxStackSize = 1;
		this.maxDamage = 32 << i3;
		if(i3 == 3) {
			this.maxDamage <<= 1;
		}

		this.efficiencyOnProperMaterial = (float)(i3 + 1 << 1);
		this.damageVsEntity = i2 + i3;
	}

	public final float getStrVsBlock(Block block1) {
		for(int i2 = 0; i2 < this.blocksEffectiveAgainst.length; ++i2) {
			if(this.blocksEffectiveAgainst[i2] == block1) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public final void hitEntity(ItemStack itemStack1) {
		itemStack1.damageItem(2);
	}

	public final void onBlockDestroyed(ItemStack itemStack1) {
		itemStack1.damageItem(1);
	}

	public final int getDamageVsEntity() {
		return this.damageVsEntity;
	}
}
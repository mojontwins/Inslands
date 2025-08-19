package net.minecraft.game.item;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class ItemFlintAndSteel extends Item {
	public ItemFlintAndSteel(int i1) {
		super(3);
		this.maxStackSize = 1;
		this.maxDamage = 64;
	}

	public final boolean onItemUse(ItemStack itemStack1, World world2, int i3, int i4, int i5, int i6) {
		if(i6 == 0) {
			--i4;
		}

		if(i6 == 1) {
			++i4;
		}

		if(i6 == 2) {
			--i5;
		}

		if(i6 == 3) {
			++i5;
		}

		if(i6 == 4) {
			--i3;
		}

		if(i6 == 5) {
			++i3;
		}

		if(world2.getBlockId(i3, i4, i5) == 0) {
			world2.playSoundEffect((double)i3 + 0.5D, (double)i4 + 0.5D, (double)i5 + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
			world2.setBlockWithNotify(i3, i4, i5, Block.fire.blockID);
		}

		itemStack1.damageItem(1);
		return true;
	}
}
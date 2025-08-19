package net.minecraft.game.item;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class ItemSeeds extends Item {
	private int blockType;

	public ItemSeeds(int i1, int i2) {
		super(39);
		this.blockType = i2;
	}

	public final boolean onItemUse(ItemStack itemStack1, World world2, int i3, int i4, int i5, int i6) {
		if(i6 != 1) {
			return false;
		} else if(world2.getBlockId(i3, i4, i5) == Block.tilledField.blockID) {
			world2.setBlockWithNotify(i3, i4 + 1, i5, this.blockType);
			--itemStack1.stackSize;
			return true;
		} else {
			return false;
		}
	}
}
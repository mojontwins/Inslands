package net.minecraft.game.world.block;

import net.minecraft.game.world.World;

public final class BlockMushroom extends BlockFlower {
	protected BlockMushroom(int i1, int i2) {
		super(i1, i2);
		this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F);
	}

	protected final boolean canThisPlantGrowOnThisBlockID(int i1) {
		return Block.opaqueCubeLookup[i1];
	}

	public final boolean canBlockStay(World world1, int i2, int i3, int i4) {
		if(world1.getBlockLightValue(i2, i3, i4) <= 13) {
			i2 = world1.getBlockId(i2, i3 - 1, i4);
			if(Block.opaqueCubeLookup[i2]) {
				return true;
			}
		}

		return false;
	}
}
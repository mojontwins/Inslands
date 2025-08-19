package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockLeavesBase extends Block {
	private boolean graphicsLevel = true;

	protected BlockLeavesBase(int i1, int i2, Material material3, boolean z4) {
		super(i1, i2, material3);
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockId(i2, i3, i4);
		return !this.graphicsLevel && i6 == this.blockID ? false : super.shouldSideBeRendered(world1, i2, i3, i4, i5);
	}
}
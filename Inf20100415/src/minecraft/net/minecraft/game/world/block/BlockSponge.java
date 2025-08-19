package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockSponge extends Block {
	protected BlockSponge(int i1) {
		super(19, Material.sponge);
		this.blockIndexInTexture = 48;
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		for(int i5 = i2 - 2; i5 <= i2 + 2; ++i5) {
			for(int i6 = i3 - 2; i6 <= i3 + 2; ++i6) {
				for(int i7 = i4 - 2; i7 <= i4 + 2; ++i7) {
					world1.getBlockMaterial(i5, i6, i7);
				}
			}
		}

	}

	public final void onBlockRemoval(World world1, int i2, int i3, int i4) {
		for(int i5 = i2 - 2; i5 <= i2 + 2; ++i5) {
			for(int i6 = i3 - 2; i6 <= i3 + 2; ++i6) {
				for(int i7 = i4 - 2; i7 <= i4 + 2; ++i7) {
					world1.notifyBlocksOfNeighborChange(i5, i6, i7, world1.getBlockId(i5, i6, i7));
				}
			}
		}

	}
}
package net.minecraft.game.world.block;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockSand extends Block {
	public BlockSand(int i1, int i2) {
		super(i1, i2, Material.sand);
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		this.scheduleBlockUpdate(world1, i2, i3, i4);
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		this.scheduleBlockUpdate(world1, i2, i3, i4);
	}

	private void scheduleBlockUpdate(World world1, int i2, int i3, int i4) {
		int i5 = i3;

		while(true) {
			int i8 = i5 - 1;
			int i6;
			Material material10;
			if(!((i6 = world1.getBlockId(i2, i8, i4)) == 0 ? true : (i6 == Block.fire.blockID ? true : ((material10 = Block.blocksList[i6].blockMaterial) == Material.water ? true : material10 == Material.lava))) || i5 < 0) {
				if(i5 < 0) {
					world1.setTileNoUpdate(i2, i3, i4, 0);
				}

				if(i5 != i3) {
					if((i6 = world1.getBlockId(i2, i5, i4)) > 0 && Block.blocksList[i6].blockMaterial != Material.air) {
						world1.setTileNoUpdate(i2, i5, i4, 0);
					}

					world1.swap(i2, i3, i4, i2, i5, i4);
				}

				return;
			}

			--i5;
			if(world1.getBlockId(i2, i5, i4) == Block.fire.blockID) {
				world1.setTileNoUpdate(i2, i5, i4, 0);
			}
		}
	}
}
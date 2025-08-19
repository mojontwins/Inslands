package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.terrain.generate.WorldGenTrees;

public final class BlockSapling extends BlockFlower {
	protected BlockSapling(int i1, int i2) {
		super(6, 15);
		this.setBlockBounds(0.099999994F, 0.0F, 0.099999994F, 0.9F, 0.8F, 0.9F);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		super.updateTick(world1, i2, i3, i4, random5);
		if(world1.getBlockLightValue(i2, i3 + 1, i4) >= 9 && random5.nextInt(5) == 0) {
			int i6;
			if((i6 = world1.getBlockMetadata(i2, i3, i4)) < 15) {
				world1.setBlockMetadataWithNotify(i2, i3, i4, i6 + 1);
				return;
			}

			world1.setTileNoUpdate(i2, i3, i4, 0);
			if(!(new WorldGenTrees()).generate(world1, random5, i2, i3, i4)) {
				world1.setTileNoUpdate(i2, i3, i4, this.blockID);
			}
		}

	}
}
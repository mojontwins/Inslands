package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockGrass extends Block {
	protected BlockGrass(int i1) {
		super(2, Material.ground);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public final int getBlockTextureFromSide(int i1) {
		return i1 == 1 ? 0 : (i1 == 0 ? 2 : 3);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(world1.getBlockLightValue(i2, i3 + 1, i4) < 4 && world1.getBlockMaterial(i2, i3 + 1, i4).getCanBlockGrass()) {
			if(random5.nextInt(4) == 0) {
				world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
			}
		} else {
			if(world1.getBlockLightValue(i2, i3 + 1, i4) >= 9) {
				i2 = i2 + random5.nextInt(3) - 1;
				i3 = i3 + random5.nextInt(5) - 3;
				i4 = i4 + random5.nextInt(3) - 1;
				if(world1.getBlockId(i2, i3, i4) == Block.dirt.blockID && world1.getBlockLightValue(i2, i3 + 1, i4) >= 4 && !world1.getBlockMaterial(i2, i3 + 1, i4).getCanBlockGrass()) {
					world1.setBlockWithNotify(i2, i3, i4, Block.grass.blockID);
				}
			}

		}
	}

	public final int idDropped(int i1, Random random2) {
		return Block.dirt.idDropped(0, random2);
	}
}
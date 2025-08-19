package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockStationary extends BlockFluid {
	protected BlockStationary(int i1, Material material2) {
		super(i1, material2);
		this.movingId = i1 - 1;
		this.stillId = i1;
		this.setTickOnLoad(false);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		boolean z6 = false;
		if(this.canFlow(world1, i2, i3 - 1, i4)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world1, i2 - 1, i3, i4)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world1, i2 + 1, i3, i4)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world1, i2, i3, i4 - 1)) {
			z6 = true;
		}

		if(!z6 && this.canFlow(world1, i2, i3, i4 + 1)) {
			z6 = true;
		}

		if(i5 != 0) {
			Material material7 = Block.blocksList[i5].blockMaterial;
			if(this.blockMaterial == Material.water && material7 == Material.lava || material7 == Material.water && this.blockMaterial == Material.lava) {
				world1.setBlockWithNotify(i2, i3, i4, Block.stone.blockID);
				return;
			}
		}

		if(Block.fire.getChanceOfNeighborsEncouragingFire(i5)) {
			z6 = true;
		}

		if(z6) {
			world1.setTileNoUpdate(i2, i3, i4, this.movingId);
			world1.scheduleBlockUpdate(i2, i3, i4, this.movingId);
		}

	}
}
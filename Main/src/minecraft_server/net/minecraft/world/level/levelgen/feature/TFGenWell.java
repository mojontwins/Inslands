package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class TFGenWell extends TFGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		return rand.nextInt(4) == 0 ? this.generate4x4Well(world, rand, x, y, z) : this.generate3x3Well(world, rand, x, y, z);
	}

	public boolean generate3x3Well(World world, Random rand, int x, int y, int z) {
		this.worldObj = world;
		if(!this.isAreaMostlyClear(world, rand, x, y, z, 3, 4, 3, 90)) {
			return false;
		} else {
			this.putBlock(x + 0, y, z + 0, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 1, y, z + 0, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 2, y, z + 0, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 0, y, z + 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 1, y, z + 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 2, y, z + 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 0, y, z + 1, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 2, y, z + 1, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 1, y, z + 1, Block.waterStill.blockID, true);
			this.putBlock(x + 0, y + 1, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 2, y + 1, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 0, y + 1, z + 2, Block.fence.blockID, true);
			this.putBlock(x + 2, y + 1, z + 2, Block.fence.blockID, true);
			this.putBlock(x + 0, y + 2, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 2, y + 2, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 0, y + 2, z + 2, Block.fence.blockID, true);
			this.putBlock(x + 2, y + 2, z + 2, Block.fence.blockID, true);
			this.putBlockAndMetadata(x + 0, y + 3, z + 0, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 0, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 3, z + 0, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 3, z + 2, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 2, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 3, z + 2, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 3, z + 1, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 3, z + 1, Block.stairSingle.blockID, 2, true);
			this.putBlock(x + 1, y + 3, z + 1, Block.planks.blockID, true);

			for(int dy = -1; dy >= -20; --dy) {
				int dblock = world.getblockID(x + 1, y + dy, z + 1);
				if(dblock != Block.dirt.blockID && dblock != Block.grass.blockID && dblock != Block.gravel.blockID && dblock != Block.stone.blockID || !world.getBlockMaterial(x + 1, y + dy - 1, z + 1).isSolid()) {
					break;
				}

				this.putBlock(x + 1, y + dy, z + 1, Block.waterStill.blockID, true);
			}

			return true;
		}
	}

	public boolean generate4x4Well(World world, Random rand, int x, int y, int z) {
		this.worldObj = world;
		if(!this.isAreaClear(world, rand, x, y, z, 4, 4, 4)) {
			return false;
		} else {
			this.putBlock(x + 0, y, z + 0, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 1, y, z + 0, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 2, y, z + 0, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 3, y, z + 0, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 0, y, z + 3, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 1, y, z + 3, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 2, y, z + 3, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 3, y, z + 3, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 0, y, z + 1, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 0, y, z + 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 3, y, z + 1, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 3, y, z + 2, Block.cobblestoneMossy.blockID, true);
			this.putBlock(x + 1, y, z + 1, Block.waterStill.blockID, true);
			this.putBlock(x + 2, y, z + 1, Block.waterStill.blockID, true);
			this.putBlock(x + 1, y, z + 2, Block.waterStill.blockID, true);
			this.putBlock(x + 2, y, z + 2, Block.waterStill.blockID, true);
			this.putBlock(x + 0, y + 1, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 3, y + 1, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 0, y + 1, z + 3, Block.fence.blockID, true);
			this.putBlock(x + 3, y + 1, z + 3, Block.fence.blockID, true);
			this.putBlock(x + 0, y + 2, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 3, y + 2, z + 0, Block.fence.blockID, true);
			this.putBlock(x + 0, y + 2, z + 3, Block.fence.blockID, true);
			this.putBlock(x + 3, y + 2, z + 3, Block.fence.blockID, true);
			this.putBlockAndMetadata(x + 0, y + 3, z + 0, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 0, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 3, z + 0, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 3, z + 0, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 3, z + 3, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 3, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 3, z + 3, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 3, z + 3, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 3, z + 1, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 3, z + 2, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 3, z + 1, Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 3, z + 2, Block.stairSingle.blockID, 2, true);
			this.putBlock(x + 1, y + 3, z + 1, Block.planks.blockID, true);
			this.putBlock(x + 2, y + 3, z + 1, Block.planks.blockID, true);
			this.putBlock(x + 1, y + 3, z + 2, Block.planks.blockID, true);
			this.putBlock(x + 2, y + 3, z + 2, Block.planks.blockID, true);

			for(int dx = 1; dx <= 2; ++dx) {
				for(int dz = 1; dz <= 2; ++dz) {
					for(int dy = -1; dy >= -20; --dy) {
						int dblock = world.getblockID(x + dx, y + dy, z + dz);
						if(dblock != Block.dirt.blockID && dblock != Block.grass.blockID && dblock != Block.gravel.blockID && dblock != Block.stone.blockID || !world.getBlockMaterial(x + dx, y + dy - 1, z + dz).isSolid()) {
							break;
						}

						this.putBlock(x + dx, y + dy, z + dz, Block.waterStill.blockID, true);
					}
				}
			}

			return true;
		}
	}
}

package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public abstract class WorldGenBop extends WorldGenerator {
	// Shared methods for all Bop generators
	
	protected void growVines(World world, int x, int y, int z, int meta) {
		world.setBlockAndMetadata(x, y, z, Block.vine.blockID, meta);
		int var6 = 4;

		while (true) {
			--y;

			if (world.getBlockId(x, y, z) != 0 || var6 <= 0) {
				return;
			}

			world.setBlockAndMetadata(x, y, z, Block.vine.blockID, meta);
			--var6;
		}
	}
}

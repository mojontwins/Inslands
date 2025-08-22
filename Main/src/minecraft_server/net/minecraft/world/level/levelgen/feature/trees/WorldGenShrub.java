package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.tile.Block;

public class WorldGenShrub extends WorldGenerator {
	EnumTreeType tree = EnumTreeType.SHRUB;
	
	private final int leavesID = tree.leaves.getBlock().blockID;
	private final int leavesMeta = tree.leaves.getMetadata();
	private final int trunkID = tree.wood.getBlock().blockID;
	private final int trunkMeta = tree.wood.getMetadata();
	
	public boolean generate(World world, Random rand, int x, int y, int z) {
		
		int blockId;
		for(; ((blockId = world.getBlockId(x, y, z)) == 0 || blockId == Block.leaves.blockID) && y > 0; --y) {
		}

		blockId = world.getBlockId(x, y, z);

		if(blockId == Block.dirt.blockID || blockId == Block.grass.blockID || blockId == Block.sand.blockID || blockId == Block.stone.blockID || blockId == Block.snow.blockID || blockId == Block.terracotta.blockID || blockId == Block.stainedTerracotta.blockID) {
			++y;
			world.setBlockAndMetadata(x, y, z, this.trunkID, this.trunkMeta);

			for(int yy = y; yy <= y + 2; ++yy) {
				int dy = yy - y;
				int radius = 2 - dy;

				for(int xx = x - radius; xx <= x + radius; ++xx) {
					int dx = xx - x;

					for(int zz = z - radius; zz <= z + radius; ++zz) {
						int dz = zz - z;
						if((Math.abs(dx) != radius || Math.abs(dz) != radius || rand.nextInt(2) != 0) && !Block.opaqueCubeLookup[world.getBlockId(xx, yy, zz)]) {
							world.setBlockAndMetadata(xx, yy, zz, this.leavesID, this.leavesMeta);
						}
					}
				}
			}
		}

		return true;
	}
}

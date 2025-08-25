package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.Block;

public class WorldGenTrees extends WorldGenerator {
	
	EnumTreeType tree = EnumTreeType.OAK;
	
	private final int leavesID = tree.leaves.getBlock().blockID;
	private final int leavesMeta = tree.leaves.getMetadata();
	private final int trunkID = tree.wood.getBlock().blockID;
	private final int trunkMeta = tree.wood.getMetadata();
	
	public int addedHeight = 4;

	public boolean generate(World world, Random rand, int x0, int y0, int z0) {
		int height = rand.nextInt(3) + this.addedHeight;

		// Check if it fits in the world

		if (y0 < 1 || y0 + height + 1 >= 256) return false;

		// Check if valid soil

		Block block = world.getBlock(x0, y0 - 1, z0);
		if (block == null || !block.canGrowPlants ()) return false;

		// Check if it fits

		for(int y = y0; y <= y0 + 1 + height; ++y) {

			byte radius = (byte) (y == y0 ? 0 : (y >= y0 + 1 + height - 2 ? 2 : 1));

			for(int x = x0 - radius; x <= x0 + radius; ++x) {
				for(int z = z0 - radius; z <= z0 + radius; ++z) {
					Material m = world.getBlockMaterial(x, y, z);
					if(m != Material.air && m != Material.leaves) {
						return false;
					}
				}
			}
		}

		world.setBlock(x0, y0 - 1, z0, Block.dirt.blockID);
		int canopy = 3;
		byte radiusBase = 0;

		int radius;
		for(int y = y0 - canopy + height; y <= y0 + height; ++y) {
			int yy = y - (y0 + height);
			radius = radiusBase + 1 - yy / 2;

			for(int x = x0 - radius; x <= x0 + radius; ++x) {
				int dx = Math.abs(x - x0);

				for(int z = z0 - radius; z <= z0 + radius; ++z) {
					int dz = Math.abs(z - z0);
					if((dx != radius || Math.abs(dz) != radius || rand.nextInt(2) != 0 && yy != 0) && !Block.opaqueCubeLookup[world.getblockID(x, y, z)]) {
						world.setBlockAndMetadata(x, y, z, this.leavesID, this.leavesMeta);
					}
				}
			}
		}

		for(int y = 0; y < height; ++y) {
			Material m = world.getBlockMaterial(x0, y0 + y, z0);
			if(m == Material.air || m == Material.leaves) {
				world.setBlockAndMetadata(x0, y0 + y, z0, this.trunkID, this.trunkMeta);
			}
		}
		
		return true;
	}
}

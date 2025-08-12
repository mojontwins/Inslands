package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.material.Material;

public class WorldGenPineTree extends WorldGenerator {
	private final int leavesBlock = Block.leaves.blockID;
	private final int leavesMeta = 0;
	private final int woodBlock = Block.wood.blockID;
	private final int woodMeta = 0;
	
	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		while (var1.isAirBlock(var3, var4, var5) && var4 > 2) {
			--var4;
		}

		int var6 = var1.getBlockId(var3, var4, var5);
		Block block = Block.blocksList[var6];
		if (block == null)
			return false;

		if (!block.canGrowPlants()) {
			return false;
		} else {
			for (int var7 = -2; var7 <= 2; ++var7) {
				for (int var8 = -2; var8 <= 2; ++var8) {
					if (var1.isAirBlock(var3 + var7, var4 - 1, var5 + var8)
							&& var1.isAirBlock(var3 + var7, var4 - 2, var5 + var8)
							&& !var1.isAirBlock(var3 + var7, var4, var5 + var8)) {
						return false;
					}
				}
			}

			// settings========
			int baselength = 4 + var2.nextInt(6);
			int branches = 2 + var2.nextInt(4);
			// ================

			int h = 1;
			buildBlock(var1, var3, var4, var5, Block.dirt.blockID, 0);
			for (int b = 0; b < baselength; b++) {
				buildBlock(var1, var3, var4 + h, var5, this.woodBlock, this.woodMeta);
				h++;
			}

			int c = 1;
			for (int r = 0; r < branches; r++) {
				generateBranch(var1, var2, var3, var4 + h, var5, c);
				c++;
				h += 2;
			}

			generateTop(var1, var3, var4 + h, var5);
			return true;
		}
	}

	public void generateTop(World world, int x, int y, int z) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				buildBlock(world, x + i, y, z + j, this.leavesBlock, this.leavesMeta);
			}
		}
		buildBlock(world, x, y, z, this.woodBlock, this.woodMeta);
		buildBlock(world, x + 1, y + 1, z, this.leavesBlock, this.leavesMeta);
		buildBlock(world, x, y + 1, z - 1, this.leavesBlock, this.leavesMeta);
		buildBlock(world, x, y + 1, z + 1, this.leavesBlock, this.leavesMeta);
		buildBlock(world, x - 1, y + 1, z, this.leavesBlock, this.leavesMeta);
		buildBlock(world, x, y + 2, z, this.leavesBlock, this.leavesMeta);
	}

	public void generateBranch(World world, Random rand, int x, int y, int z, int n) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				buildBlock(world, x + i, y, z + j, this.leavesBlock, this.leavesMeta);
			}
		}

		int var99999 = rand.nextInt(2);
		int var99998 = rand.nextInt(2);
		int var99997 = rand.nextInt(2);
		int var99996 = rand.nextInt(2);

		if (n % 2 == 0) {
			// 1
			if (var99998 == 0) {
				buildBlock(world, x + 1, y - 1, z - 2, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x + 2, y - 1, z - 1, this.leavesBlock, this.leavesMeta);
				if (var99999 == 0) {
					buildBlock(world, x + 2, y - 2, z - 2, this.leavesBlock, this.leavesMeta);
				} else {
					buildBlock(world, x + 2, y - 1, z - 2, this.leavesBlock, this.leavesMeta);
				}
			} else {
				buildBlock(world, x + 1, y, z - 2, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x + 2, y, z - 1, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x + 2, y, z - 2, this.leavesBlock, this.leavesMeta);
			}

			// 2
			if (var99997 == 0) {
				buildBlock(world, x - 2, y - 1, z + 1, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x - 1, y - 1, z + 2, this.leavesBlock, this.leavesMeta);
				if (var99996 == 0) {
					buildBlock(world, x - 2, y - 2, z + 2, this.leavesBlock, this.leavesMeta);
				} else {
					buildBlock(world, x - 2, y - 1, z + 2, this.leavesBlock, this.leavesMeta);
				}
			} else {
				buildBlock(world, x - 2, y, z + 1, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x - 1, y, z + 2, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x - 2, y, z + 2, this.leavesBlock, this.leavesMeta);
			}
		} else {
			// 1
			if (var99998 == 0) {
				buildBlock(world, x + 2, y - 1, z + 1, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x + 1, y - 1, z + 2, this.leavesBlock, this.leavesMeta);
				if (var99999 == 0) {
					buildBlock(world, x + 2, y - 2, z + 2, this.leavesBlock, this.leavesMeta);
				} else {
					buildBlock(world, x + 2, y - 1, z + 2, this.leavesBlock, this.leavesMeta);
				}
			} else {
				buildBlock(world, x + 2, y, z + 1, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x + 1, y, z + 2, this.leavesBlock, this.leavesMeta);
				if (var99999 == 0) {
					buildBlock(world, x + 2, y - 1, z + 2, this.leavesBlock, this.leavesMeta);
				} else {
					buildBlock(world, x + 2, y, z + 2, this.leavesBlock, this.leavesMeta);
				}
			}

			// 2
			if (var99997 == 0) {
				buildBlock(world, x - 1, y - 1, z - 2, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x - 2, y - 1, z - 1, this.leavesBlock, this.leavesMeta);
				if (var99996 == 0) {
					buildBlock(world, x - 2, y - 2, z - 2, this.leavesBlock, this.leavesMeta);
				} else {
					buildBlock(world, x - 2, y - 1, z - 2, this.leavesBlock, this.leavesMeta);
				}
			} else {
				buildBlock(world, x - 1, y, z - 2, this.leavesBlock, this.leavesMeta);
				buildBlock(world, x - 2, y, z - 1, this.leavesBlock, this.leavesMeta);
				if (var99996 == 0) {
					buildBlock(world, x - 2, y - 1, z - 2, this.leavesBlock, this.leavesMeta);
				} else {
					buildBlock(world, x - 2, y, z - 2, this.leavesBlock, this.leavesMeta);
				}
			}
		}

		buildBlock(world, x, y, z, this.woodBlock, this.woodMeta);
		buildBlock(world, x, y + 1, z, this.woodBlock, this.woodMeta);
	}

	public void buildBlock(World world, int x, int y, int z, int id, int meta) {
		Material m = world.getBlockMaterial(x, y, z);
		if (m == Material.air || m == Material.leaves) {
			world.setBlockAndMetadata(x, y, z, id, meta);
		}
	}
}

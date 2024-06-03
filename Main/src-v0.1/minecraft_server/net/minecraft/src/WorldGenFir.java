package net.minecraft.src;

import java.util.Random;

public class WorldGenFir extends WorldGenerator {

	// Code adapted from Enhanced Biomes mod
	// so it works in a1.2.6 with vanilla blocks!
	// https://github.com/SMEZ1234/EnhancedBiomes/
	
	private static final int leavesId = Block.leaves.blockID;
	private static final int trunkId = Block.wood.blockID;
	
	private int height;
	private boolean largeTree;
	
	public WorldGenFir (int height, boolean largeTree) {
		this.height = height;
		this.largeTree = largeTree;
	}
	
	public void setBlockIfEmpty (World world, int x, int y, int z, int blockID) {
		if (0 == world.getBlockId(x, y, z)) world.setBlock(x, y, z, blockID);
	}
	
	public void setBlockIfEmpty (World world, int x, int y, int z, int blockID, int meta) {
		if (0 == world.getBlockId(x, y, z)) world.setBlockAndMetadata(x, y, z, blockID, meta);
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if (y + height > 126) return false;
		
		if(this.largeTree == true) {
			for(int yy = 0; yy < this.height + 18; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						if(world.getBlockId(x + zz, y + yy, z + xx) != 0) {
							return false;
						}
					}
				}
			}

			for(int yy = 0; yy < 18; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 12; zz++) {
						if(world.getBlockId(x + zz - 5, y + this.height + yy, z + xx) != 0) {
							return false;
						}
					}
				}

				for(int xx = 0; xx < 12; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						if(world.getBlockId(x + zz, y + this.height + yy, z + xx - 5) != 0) {
							return false;
						}
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 10; zz++) {
						if(world.getBlockId(x + zz - 4, y + this.height + yy, z + xx - 1) != 0) {
							return false;
						}
					}
				}

				for(int xx = 0; xx < 10; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						if(world.getBlockId(x + zz - 1, y + this.height + yy, z + xx - 4) != 0) {
							return false;
						}
					}
				}

				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						if(world.getBlockId(x + zz - 2, y + this.height + yy, z + xx - 3) != 0) {
							return false;
						}
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						if(world.getBlockId(x + zz - 3, y + this.height + yy, z + xx - 2) != 0) {
							return false;
						}
					}
				}
			}

			Block soilBlock = Block.blocksList[world.getBlockId(x, y - 1, z)];
			if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;

			soilBlock = Block.blocksList[world.getBlockId(x, y - 1, z + 1)];
			if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;

			soilBlock = Block.blocksList[world.getBlockId(x + 1, y - 1, z)];
			if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;
			
			soilBlock = Block.blocksList[world.getBlockId(x + 1, y - 1, z + 1)];
			if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;

			//Layer 1
			for(int zz = 0; zz < 2; zz++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int yy = 0; yy < 3; yy++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 18 + yy, z + xx, leavesId);
					}
				}
			}

			//Layer 2
			for(int yy = 0; yy < 3; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					setBlockIfEmpty(world, x + 2, y + this.height + 17 - yy, z + xx, leavesId);
					setBlockIfEmpty(world, x - 1, y + this.height + 17 - yy, z + xx, leavesId);
				}

				for(int zz = 0; zz < 2; zz++) {
					setBlockIfEmpty(world, x + zz, y + this.height + 17 - yy, z + 2, leavesId);
					setBlockIfEmpty(world, x + zz, y + this.height + 17 - yy, z - 1, leavesId);
				}
			}

			//Layer 3
			for(int yy = 0; yy < 3; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height + 14 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 14 - yy, z + xx - 2, leavesId);
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 14 - yy, z + xx - 1, leavesId);
					}
				}
			}

			//Platform 1:1
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height + 9 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 9 - yy, z + xx - 2, leavesId);
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 9 - yy, z + xx - 1, leavesId);
					}
				}
			}

			//Platform 1:2
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						setBlockIfEmpty(world, x + zz - 3, y + this.height + 8 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 8 - yy, z + xx - 3, leavesId);
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height + 8 - yy, z + xx - 1, leavesId);
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 8 - yy, z + xx - 2, leavesId);
					}
				}
			}

			//Platform 2:1
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						setBlockIfEmpty(world, x + zz - 3, y + this.height + 5 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 5 - yy, z + xx - 3, leavesId);
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height + 5 - yy, z + xx - 1, leavesId);
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 5 - yy, z + xx - 2, leavesId);
					}
				}
			}

			//Platform 2:2
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 10; zz++) {
						setBlockIfEmpty(world, x + zz - 4, y + this.height + 4 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 10; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 4 - yy, z + xx - 4, leavesId);
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						setBlockIfEmpty(world, x + zz - 3, y + this.height + 4 - yy, z + xx - 1, leavesId);
					}
				}

				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 4 - yy, z + xx - 3, leavesId);
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height + 4 - yy, z + xx - 2, leavesId);
					}
				}
			}

			//Platform 3:1
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 10; zz++) {
						setBlockIfEmpty(world, x + zz - 4, y + this.height + 1 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 10; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 1 - yy, z + xx - 4, leavesId);
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						setBlockIfEmpty(world, x + zz - 3, y + this.height + 1 - yy, z + xx - 1, leavesId);
					}
				}

				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 1 - yy, z + xx - 3, leavesId);
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height + 1 - yy, z + xx - 2, leavesId);
					}
				}
			}

			//Platform 3:2
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 2; xx++) {
					for(int zz = 0; zz < 12; zz++) {
						setBlockIfEmpty(world, x + zz - 5, y + this.height - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 12; xx++) {
					for(int zz = 0; zz < 2; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height - yy, z + xx - 5, leavesId);
					}
				}

				for(int xx = 0; xx < 4; xx++) {
					for(int zz = 0; zz < 10; zz++) {
						setBlockIfEmpty(world, x + zz - 4, y + this.height - yy, z + xx - 1, leavesId);
					}
				}

				for(int xx = 0; xx < 10; xx++) {
					for(int zz = 0; zz < 4; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height - yy, z + xx - 4, leavesId);
					}
				}

				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 6; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height - yy, z + xx - 3, leavesId);
					}
				}

				for(int xx = 0; xx < 6; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						setBlockIfEmpty(world, x + zz - 3, y + this.height - yy, z + xx - 2, leavesId);
					}
				}
			}

			//Trunk
			for(int i = 0; i < this.height + 18; i++) {
				world.setBlock(x, y + i, z, trunkId);
				world.setBlock(x + 1, y + i, z, trunkId);
				world.setBlock(x, y + i, z + 1, trunkId);
				world.setBlock(x + 1, y + i, z + 1, trunkId);
			}

			return true;
		} else {
			//Small Tree
			for(int i = 0; i < this.height + 5; i++) {
				if(world.getBlockId(x, y + i, z) != 0) {
					return false;
				}
			}

			Block soilBlock = Block.blocksList[world.getBlockId(x, y - 1, z)];
			if (!(soilBlock != null && soilBlock.canGrowPlants())) return false;

			for(int yy = 0; yy < 5; yy++) {
				for(int xx = 0; xx < 1; xx++) {
					for(int zz = 0; zz < 5; zz++) {
						if(world.getBlockId(x + zz - 2, y + this.height + yy, z + xx) != 0) {
							return false;
						}
					}
				}

				for(int xx = 0; xx < 5; xx++) {
					for(int zz = 0; zz < 1; zz++) {
						if(world.getBlockId(x + zz, y + this.height + yy, z + xx - 2) != 0) {
							return false;
						}
					}
				}

				for(int xx = 0; xx < 3; xx++) {
					for(int zz = 0; zz < 3; zz++) {
						if(world.getBlockId(x + zz - 1, y + this.height + yy, z + xx - 1) != 0) {
							return false;
						}
					}
				}
			}

			//Layer 1
			for(int zz = 0; zz < 1; zz++) {
				for(int xx = 0; xx < 1; xx++) {
					for(int yy = 0; yy < 2; yy++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 5 + yy, z + xx, leavesId);
					}
				}
			}

			//Layer 2
			for(int yy = 0; yy < 2; yy++) {
				for(int xx = 0; xx < 1; xx++) {
					for(int zz = 0; zz < 3; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 4 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 3; xx++) {
					for(int zz = 0; zz < 1; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 4 - yy, z + xx - 1, leavesId);
					}
				}
			}

			//Platform 1:1
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 1; xx++) {
					for(int zz = 0; zz < 3; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height + 1 - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 3; xx++) {
					for(int zz = 0; zz < 1; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height + 1 - yy, z + xx - 1, leavesId);
					}
				}
			}

			//Platform 1:2
			for(int yy = 0; yy < 1; yy++) {
				for(int xx = 0; xx < 1; xx++) {
					for(int zz = 0; zz < 5; zz++) {
						setBlockIfEmpty(world, x + zz - 2, y + this.height - yy, z + xx, leavesId);
					}
				}

				for(int xx = 0; xx < 5; xx++) {
					for(int zz = 0; zz < 1; zz++) {
						setBlockIfEmpty(world, x + zz, y + this.height - yy, z + xx - 2, leavesId);
					}
				}

				for(int xx = 0; xx < 3; xx++) {
					for(int zz = 0; zz < 3; zz++) {
						setBlockIfEmpty(world, x + zz - 1, y + this.height - yy, z + xx - 1, leavesId);
					}
				}
			}

			//Trunk
			for(int i = 0; i < this.height + 5; i++) {
				world.setBlock(x, y + i, z, trunkId);
			}

			return true;
		}
	}

}

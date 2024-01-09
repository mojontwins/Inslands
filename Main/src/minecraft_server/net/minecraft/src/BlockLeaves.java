package net.minecraft.src;

import java.util.Random;

public class BlockLeaves extends BlockLeavesBase {
	// Alpha version
		private int leafTexIndex;
	int[] adjacentTreeBlocks;
	// Change to fancy colors for fancy trees and use metadata 1..7 (0 means "biome controlled")
	int[] fixedColors = { 
		0x5BFB3B, // Normal neon green
		0xF6F535, // Yellower for arid biomes
		0x5BFB3B, 
		0x5BFB3B, 
		0x5BFB3B, 
		0x5BFB3B, 
		0x5BFB3B, 
		0x5BFB3B  // This will NEVER be used, it means "Seasonal colorizer"
	};

	protected BlockLeaves(int id, int blockIndex) {
		super(id, blockIndex, Material.leaves, false);
		this.leafTexIndex = blockIndex;
		this.setTickOnLoad(true);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int bh) {
		this.onBlockRemoval(world, x, y, z);
	}

	public int quantityDropped(Random Random) {
		return Random.nextInt(20) != 0 ? 0 : 1;
	}

	public int idDropped(int i, Random Random) {
		return Random.nextInt(50) == 0 ? Item.appleRed.shiftedIndex : Block.sapling.blockID;
	}
	
	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	public void setGraphicsLevel(boolean flag) {
		this.graphicsLevel = flag;
		this.blockIndexInTexture = this.leafTexIndex + (flag ? 0 : 1);
	}

	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		super.onEntityWalking(world, i, j, k, entity);
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		byte radius = 1;
		int range = radius + 1;
		
		if(world.checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range)) {
			for(int xx = -radius; xx <= radius; ++xx) {
				for(int yy = -radius; yy <= radius; ++yy) {
					for(int zz = -radius; zz <= radius; ++zz) {
						int i2 = world.getBlockId(x + xx, y + yy, z + zz);
						if(i2 == this.blockID) {
							int j2 = world.getBlockMetadata(x + xx, y + yy, z + zz);
							world.setBlockMetadata(x + xx, y + yy, z + zz, j2 | 8);
						}
					}
				}
			}
		}
	}
	
	public void updateTick(World world, int x, int y, int z, Random Random) {
		if(!world.multiplayerWorld) {
			int blockMeta = world.getBlockMetadata(x, y, z);
			if((blockMeta & 8) != 0) {
				byte byte0 = 6;
				int i1 = byte0 + 1;
				byte byte1 = 32;
				int j1 = byte1 * byte1;
				int k1 = byte1 / 2;
				if(this.adjacentTreeBlocks == null) {
					this.adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
				}

				int j2;
				if(world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
					j2 = -byte0;

					label112:
					while(true) {
						int l2;
						int j3;
						int l3;
						if(j2 > byte0) {
							j2 = 1;

							while(true) {
								if(j2 > 4) {
									break label112;
								}

								for(l2 = -byte0; l2 <= byte0; ++l2) {
									for(j3 = -byte0; j3 <= byte0; ++j3) {
										for(l3 = -byte0; l3 <= byte0; ++l3) {
											if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1] == j2 - 1) {
												if(this.adjacentTreeBlocks[(l2 + k1 - 1) * j1 + (j3 + k1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1 - 1) * j1 + (j3 + k1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 - 1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 - 1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + l3 + k1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + l3 + k1] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 - 1)] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 - 1)] = j2;
												}

												if(this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1 + 1] == -2) {
													this.adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + l3 + k1 + 1] = j2;
												}
											}
										}
									}
								}

								++j2;
							}
						}

						for(l2 = -byte0; l2 <= byte0; ++l2) {
							for(j3 = -byte0; j3 <= byte0; ++j3) {
								l3 = world.getBlockId(x + j2, y + l2, z + j3);
								Block block = Block.blocksList[l3];
								if(/*l3 == Block.wood.blockID*/ block != null && block instanceof BlockLog) {
									this.adjacentTreeBlocks[(j2 + k1) * j1 + (l2 + k1) * byte1 + j3 + k1] = 0;
								} else if(l3 == this.blockID) {
									this.adjacentTreeBlocks[(j2 + k1) * j1 + (l2 + k1) * byte1 + j3 + k1] = -2;
								} else {
									this.adjacentTreeBlocks[(j2 + k1) * j1 + (l2 + k1) * byte1 + j3 + k1] = -1;
								}
							}
						}

						++j2;
					}
				}

				j2 = this.adjacentTreeBlocks[k1 * j1 + k1 * byte1 + k1];
				if(j2 >= 0) {
					world.setBlockMetadata(x, y, z, blockMeta & -9);	// Clear bit 3
				} else {
					this.removeLeaves(world, x, y, z);
				}
			}
		}
	}

	private void removeLeaves(World world, int i, int j, int k) {
		this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k));
		world.setBlockWithNotify(i, j, k, 0);
	}
	
	// Not as complex as a colorizer, but allows for some freedom!
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		int meta = blockAccess.getBlockMetadata(x, y, z) & 7;
		if(meta == 7) return Seasons.getLeavesColorForToday();
		return this.fixedColors[meta];
	}
	
	public int getRenderColor(int meta) {
		return this.fixedColors[meta];
	}
	
	// Beta version:
	/*
	private int baseIndexInPNG;
	int[] adjacentTreeBlocks;

	protected BlockLeaves(int i1, int i2) {
		super(i1, i2, Material.leaves, false);
		this.baseIndexInPNG = i2;
		this.setTickOnLoad(true);
	}

	public int getRenderColor(int i1) {
		return (i1 & 1) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((i1 & 2) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic());
	}

	public int colorMultiplier(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		int i5 = iBlockAccess1.getBlockMetadata(i2, i3, i4);
		if((i5 & 1) == 1) {
			return ColorizerFoliage.getFoliageColorPine();
		} else if((i5 & 2) == 2) {
			return ColorizerFoliage.getFoliageColorBirch();
		} else {
			iBlockAccess1.getWorldChunkManager().getBiomesForGeneration(i2, i4, 1, 1);
			double d6 = iBlockAccess1.getWorldChunkManager().temperature[0];
			double d8 = iBlockAccess1.getWorldChunkManager().humidity[0];
			return ColorizerFoliage.getFoliageColor(d6, d8);
		}
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
		byte b5 = 1;
		int i6 = b5 + 1;
		if(world1.checkChunksExist(i2 - i6, i3 - i6, i4 - i6, i2 + i6, i3 + i6, i4 + i6)) {
			for(int i7 = -b5; i7 <= b5; ++i7) {
				for(int i8 = -b5; i8 <= b5; ++i8) {
					for(int i9 = -b5; i9 <= b5; ++i9) {
						int i10 = world1.getBlockId(i2 + i7, i3 + i8, i4 + i9);
						if(i10 == Block.leaves.blockID) {
							int i11 = world1.getBlockMetadata(i2 + i7, i3 + i8, i4 + i9);
							world1.setBlockMetadata(i2 + i7, i3 + i8, i4 + i9, i11 | 8);
						}
					}
				}
			}
		}

	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(!world1.multiplayerWorld) {
			int i6 = world1.getBlockMetadata(i2, i3, i4);
			if((i6 & 8) != 0) {
				byte b7 = 4;
				int i8 = b7 + 1;
				byte b9 = 32;
				int i10 = b9 * b9;
				int i11 = b9 / 2;
				if(this.adjacentTreeBlocks == null) {
					this.adjacentTreeBlocks = new int[b9 * b9 * b9];
				}

				int i12;
				if(world1.checkChunksExist(i2 - i8, i3 - i8, i4 - i8, i2 + i8, i3 + i8, i4 + i8)) {
					i12 = -b7;

					label111:
					while(true) {
						int i13;
						int i14;
						int i15;
						if(i12 > b7) {
							i12 = 1;

							while(true) {
								if(i12 > 4) {
									break label111;
								}

								for(i13 = -b7; i13 <= b7; ++i13) {
									for(i14 = -b7; i14 <= b7; ++i14) {
										for(i15 = -b7; i15 <= b7; ++i15) {
											if(this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11) * b9 + i15 + i11] == i12 - 1) {
												if(this.adjacentTreeBlocks[(i13 + i11 - 1) * i10 + (i14 + i11) * b9 + i15 + i11] == -2) {
													this.adjacentTreeBlocks[(i13 + i11 - 1) * i10 + (i14 + i11) * b9 + i15 + i11] = i12;
												}

												if(this.adjacentTreeBlocks[(i13 + i11 + 1) * i10 + (i14 + i11) * b9 + i15 + i11] == -2) {
													this.adjacentTreeBlocks[(i13 + i11 + 1) * i10 + (i14 + i11) * b9 + i15 + i11] = i12;
												}

												if(this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11 - 1) * b9 + i15 + i11] == -2) {
													this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11 - 1) * b9 + i15 + i11] = i12;
												}

												if(this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11 + 1) * b9 + i15 + i11] == -2) {
													this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11 + 1) * b9 + i15 + i11] = i12;
												}

												if(this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11) * b9 + (i15 + i11 - 1)] == -2) {
													this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11) * b9 + (i15 + i11 - 1)] = i12;
												}

												if(this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11) * b9 + i15 + i11 + 1] == -2) {
													this.adjacentTreeBlocks[(i13 + i11) * i10 + (i14 + i11) * b9 + i15 + i11 + 1] = i12;
												}
											}
										}
									}
								}

								++i12;
							}
						}

						for(i13 = -b7; i13 <= b7; ++i13) {
							for(i14 = -b7; i14 <= b7; ++i14) {
								i15 = world1.getBlockId(i2 + i12, i3 + i13, i4 + i14);
								if(i15 == Block.wood.blockID) {
									this.adjacentTreeBlocks[(i12 + i11) * i10 + (i13 + i11) * b9 + i14 + i11] = 0;
								} else if(i15 == Block.leaves.blockID) {
									this.adjacentTreeBlocks[(i12 + i11) * i10 + (i13 + i11) * b9 + i14 + i11] = -2;
								} else {
									this.adjacentTreeBlocks[(i12 + i11) * i10 + (i13 + i11) * b9 + i14 + i11] = -1;
								}
							}
						}

						++i12;
					}
				}

				i12 = this.adjacentTreeBlocks[i11 * i10 + i11 * b9 + i11];
				if(i12 >= 0) {
					world1.setBlockMetadata(i2, i3, i4, i6 & -9);
				} else {
					this.removeLeaves(world1, i2, i3, i4);
				}
			}

		}
	}

	private void removeLeaves(World world1, int i2, int i3, int i4) {
		this.dropBlockAsItem(world1, i2, i3, i4, world1.getBlockMetadata(i2, i3, i4));
		world1.setBlockWithNotify(i2, i3, i4, 0);
	}

	public int quantityDropped(Random random1) {
		return random1.nextInt(20) == 0 ? 1 : 0;
	}

	public int idDropped(int i1, Random random2) {
		return Block.sapling.blockID;
	}

	public void harvestBlock(World world1, EntityPlayer entityPlayer2, int i3, int i4, int i5, int i6) {
		if(!world1.multiplayerWorld && entityPlayer2.getCurrentEquippedItem() != null && entityPlayer2.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			entityPlayer2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(world1, i3, i4, i5, new ItemStack(Block.leaves.blockID, 1, i6 & 3));
		} else {
			super.harvestBlock(world1, entityPlayer2, i3, i4, i5, i6);
		}

	}

	protected int damageDropped(int i1) {
		return i1 & 3;
	}

	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return (i2 & 3) == 1 ? this.blockIndexInTexture + 80 : this.blockIndexInTexture;
	}

	public void setGraphicsLevel(boolean z1) {
		this.graphicsLevel = z1;
		this.blockIndexInTexture = this.baseIndexInPNG + (z1 ? 0 : 1);
	}

	public void onEntityWalking(World world1, int i2, int i3, int i4, Entity entity5) {
		super.onEntityWalking(world1, i2, i3, i4, entity5);
	}
	*/
}

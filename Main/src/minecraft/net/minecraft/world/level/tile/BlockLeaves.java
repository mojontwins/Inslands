package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.levelgen.feature.trees.EnumTreeType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class BlockLeaves extends BlockLeavesBase implements IBlockWithSubtypes {
	// New version.
	// Leaves meta >> 4 is leaves type. It's transferred directly to sapling type (meta) when dropping.
	// Leaves will be rendered using 256+type for fancy, 272+type for fast.
	// Colourizer will only be used for meta 0.
	
	private int leafTexOffset = 256;
	public static byte canopyDiameter = 32;
	public static int canopyRadius = canopyDiameter / 2;

	int[] surroundings;

	protected BlockLeaves(int id, int blockIndex) {
		super(id, blockIndex, Material.leaves, false);
		this.setTickOnLoad(true);
		
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
		// Small optimization: When replaced with leaves or wood, surrounding leaves are
		// NOT affected
		if (blockID == Block.wood.blockID || blockID == Block.leaves.blockID)
			return;
		
		this.onBlockRemovalDo(world, x, y, z);
	}

	public int quantityDropped(Random rand) {
		return rand.nextInt(20) != 0 ? 0 : 1;
	}

	// I'm using this new, more convenient feature.
	@Override
	public ItemStack itemStackDropped(int meta, Random rand) {
		switch(rand.nextInt(50)) {
		case 0:
			return new ItemStack(Item.appleRed);
		case 1:
			if(meta == 0) return new ItemStack(Item.acornSeed);
			return new ItemStack(Item.stick);
		default:
			EnumTreeType tree = EnumTreeType.findTreeTypeFromLeaves(new BlockState(this, meta));
			return new ItemStack(tree.sapling.getBlock().blockID, 1, tree.sapling.getMetadata() & 0xf0);
		}
	}
	
	public boolean isOpaqueCube() {
		return false;
	}

	public void setGraphicsLevel(boolean flag) {
		this.graphicsLevel = flag;
		this.leafTexOffset = flag ? 256 : 272;
	}

	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		super.onEntityWalking(world, i, j, k, entity);
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		int blockID = world.getblockID(x, y, z);

		// Small optimization: When replaced with leaves or wood, surrounding leaves are
		// NOT affected
		if (blockID == Block.wood.blockID || blockID == Block.leaves.blockID)
			return;
		
		this.onBlockRemovalDo(world, x, y, z);
	}

	public void onBlockRemovalDo(World world, int x, int y, int z) {
		byte radius = 1;
		int range = radius + 1;

		if (world.checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range)) {
			for (int xx = -radius; xx <= radius; ++xx) {
				for (int yy = -radius; yy <= radius; ++yy) {
					for (int zz = -radius; zz <= radius; ++zz) {
						int i2 = world.getblockID(x + xx, y + yy, z + zz);
						if (i2 == this.blockID) {
							int j2 = world.getBlockMetadata(x + xx, y + yy, z + zz);
							world.setBlockMetadata(x + xx, y + yy, z + zz, j2 | 8);
						}
					}
				}
			}
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		this.updateTick17(world, x, y, z, random);
	}

	public void updateTick17(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			int metadata = world.getBlockMetadata(x, y, z);

			// Is leaf marked to be checked?
			if ((metadata & 8) != 0) {
				if (this.surroundings == null) {
					this.surroundings = new int[32768];
				}

				if (world.checkChunksExist(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5)) {

					for (int xx = -4; xx <= 4; ++xx) {
						for (int yy = -4; yy <= 4; ++yy) {
							for (int zz = -4; zz <= 4; ++zz) {
								Block block = Block.blocksList[world.getblockID(x + xx, y + yy, z + zz)];

								if (block == null || block.blockMaterial != Material.wood) {
									if (block instanceof BlockLeaves) {
										this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5) + (zz + 16)] = -2;
									} else {
										this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5) + (zz + 16)] = -1;
									}
								} else {
									this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5) + (zz + 16)] = 0;
								}
							}
						}
					}

					for (int density = 1; density <= 4; ++density) {
						for (int xx = -4; xx <= 4; ++xx) {
							for (int yy = -4; yy <= 4; ++yy) {
								for (int zz = -4; zz <= 4; ++zz) {
									if (this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5) + zz + 16] == density
											- 1) {
										if (this.surroundings[((xx + 16 - 1) << 10) + ((yy + 16) << 5) + zz	+ 16] == -2) {
											this.surroundings[((xx + 16 - 1) << 10) + ((yy + 16) << 5) + zz	+ 16] = density;
										}

										if (this.surroundings[((xx + 16 + 1) << 10) + ((yy + 16) << 5) + zz + 16] == -2) {
											this.surroundings[((xx + 16 + 1) << 10) + ((yy + 16) << 5) + zz	+ 16] = density;
										}

										if (this.surroundings[((xx + 16) << 10) + ((yy + 16 - 1) << 5) + zz	+ 16] == -2) {
											this.surroundings[((xx + 16) << 10) + ((yy + 16 - 1) << 5) + zz	+ 16] = density;
										}

										if (this.surroundings[((xx + 16) << 10) + ((yy + 16 + 1) << 5) + zz	+ 16] == -2) {
											this.surroundings[((xx + 16) << 10) + ((yy + 16 + 1) << 5) + zz	+ 16] = density;
										}

										if (this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5)
												+ (zz + 16 - 1)] == -2) {
											this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5)
													+ (zz + 16 - 1)] = density;
										}

										if (this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5) + zz + 16 + 1] == -2) {
											this.surroundings[((xx + 16) << 10) + ((yy + 16) << 5) + zz + 16 + 1] = density;
										}
									}
								}
							}
						}
					}
				}

				int l2 = this.surroundings[16912];

				if (l2 >= 0) {
					world.setBlockMetadata(x, y, z, metadata & -9); // Clear bit 3
				} else {
					this.removeLeaves(world, x, y, z);
				}
			}
		}
	}

	public void updateTickBeta(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			int metadata = world.getBlockMetadata(x, y, z);

			// Is leaf marked to be checked?
			if ((metadata & 8) != 0) {
				byte radius = 6;
				int range = radius + 1;

				if (this.surroundings == null) {
					this.surroundings = new int[canopyDiameter * canopyDiameter * canopyDiameter];
				}

				int density;
				if (world.checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range)) {
					density = -radius;

					label112: while (true) {
						int xx;
						int yy;
						int zz;
						if (density > radius) {
							density = 1;

							while (true) {
								if (density > 4) {
									break label112;
								}

								for (xx = -radius; xx <= radius; ++xx) {
									for (yy = -radius; yy <= radius; ++yy) {
										for (zz = -radius; zz <= radius; ++zz) {
											if (this.surroundings[((xx + canopyRadius) << 10)
													+ ((yy + canopyRadius) << 5) + zz + canopyRadius] == density - 1) {
												if (this.surroundings[((xx + canopyRadius - 1) << 10)
														+ ((yy + canopyRadius) << 5) + zz + canopyRadius] == -2) {
													this.surroundings[((xx + canopyRadius - 1) << 10)
															+ ((yy + canopyRadius) << 5) + zz + canopyRadius] = density;
												}

												if (this.surroundings[((xx + canopyRadius + 1) << 10)
														+ ((yy + canopyRadius) << 5) + zz + canopyRadius] == -2) {
													this.surroundings[((xx + canopyRadius + 1) << 10)
															+ ((yy + canopyRadius) << 5) + zz + canopyRadius] = density;
												}

												if (this.surroundings[((xx + canopyRadius) << 10)
														+ ((yy + canopyRadius - 1) << 5) + zz + canopyRadius] == -2) {
													this.surroundings[((xx + canopyRadius) << 10)
															+ ((yy + canopyRadius - 1) << 5) + zz
															+ canopyRadius] = density;
												}

												if (this.surroundings[((xx + canopyRadius) << 10)
														+ ((yy + canopyRadius + 1) << 5) + zz + canopyRadius] == -2) {
													this.surroundings[((xx + canopyRadius) << 10)
															+ ((yy + canopyRadius + 1) << 5) + zz
															+ canopyRadius] = density;
												}

												if (this.surroundings[((xx + canopyRadius) << 10)
														+ ((yy + canopyRadius) << 5) + (zz + canopyRadius - 1)] == -2) {
													this.surroundings[((xx + canopyRadius) << 10)
															+ ((yy + canopyRadius) << 5)
															+ (zz + canopyRadius - 1)] = density;
												}

												if (this.surroundings[((xx + canopyRadius) << 10)
														+ ((yy + canopyRadius) << 5) + zz + canopyRadius + 1] == -2) {
													this.surroundings[((xx + canopyRadius) << 10)
															+ ((yy + canopyRadius) << 5) + zz + canopyRadius
															+ 1] = density;
												}
											}
										}
									}
								}

								++density;
							}
						}

						for (xx = -radius; xx <= radius; ++xx) {
							for (yy = -radius; yy <= radius; ++yy) {
								zz = world.getblockID(x + density, y + xx, z + yy);
								Block block = Block.blocksList[zz];
								if (block != null && block instanceof BlockLog) {
									this.surroundings[((density + canopyRadius) << 10) + ((xx + canopyRadius) << 5) + yy
											+ canopyRadius] = 0;
								} else if (zz == this.blockID) {
									this.surroundings[((density + canopyRadius) << 10) + ((xx + canopyRadius) << 5) + yy
											+ canopyRadius] = -2;
								} else {
									this.surroundings[((density + canopyRadius) << 10) + ((xx + canopyRadius) << 5) + yy
											+ canopyRadius] = -1;
								}
							}
						}

						++density;
					}
				}

				density = this.surroundings[canopyRadius << 10 | canopyRadius << 5 | canopyRadius];
				if (density >= 0) {
					world.setBlockMetadata(x, y, z, metadata & -9); // Clear bit 3
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
	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		if((world.getBlockMetadata(x, y, z) & 0xf0) == 0) {
			if(LevelThemeGlobalSettings.colorizedPlants) {
				return world.getFoliageColorFromCache(x, z);
			} else {
				return 0x5BFB3B;
			}
		} else return 0xFFFFFF;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return this.leafTexOffset + (meta >> 4);
	}

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 15; i ++) {
			par3List.add(new ItemStack(par1, 1, i<<4));
		}
	}

	@Override
	public int getItemblockID() {
		return this.blockID - 256;
	}

	@Override
	public String getNameFromMeta(int meta) {
		return "leaves." + EnumTreeType.values()[meta >> 4].name;
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return 2;
	}
}
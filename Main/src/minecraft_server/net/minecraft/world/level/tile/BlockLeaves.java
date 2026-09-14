package net.minecraft.world.level.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.colorizer.ColorizerFoliage;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.levelgen.feature.trees.EnumTreeType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class BlockLeaves extends BlockLeavesBase implements IBlockWithSubtypes, IBigPlants {
	public static boolean lockTextures = false;	// If true, meta 0 (default alpha) textures are used.
	
	// New version.
	// Leaves meta >> 4 is leaves type. It's transferred directly to sapling type (meta) when dropping.
	// Leaves will be rendered using 256+type for fancy, 272+type for fast.
	// Colourizer will only be used for meta 0.
	
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
		Block block = Block.blocksList[blockID];
		if((block instanceof BlockLog) || (block instanceof BlockLeaves)) return;
		
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
			EnumTreeType tree = EnumTreeType.findTreeTypeFromLeaves(new BlockState(this, meta & 0xf0));
			return new ItemStack(tree.sapling.getBlock().blockID, 1, tree.sapling.getMetadata() & 0xf0);
		}
	}
	
	public boolean isOpaqueCube() {
		return false;
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		int blockID = world.getBlockID(x, y, z);

		// Small optimization: When replaced with leaves or wood, surrounding leaves are
		// NOT affected
		if (blockID == Block.wood.blockID || blockID == Block.leaves.blockID)
			return;
		
		this.onBlockRemovalDo(world, x, y, z);
	}

	public void onBlockRemovalDo(World world, int x, int y, int z) {
		byte radius = 1;

		for (int xx = -radius; xx <= radius; ++xx) {
			for (int yy = -radius; yy <= radius; ++yy) {
				for (int zz = -radius; zz <= radius; ++zz) {
					int i2 = world.getBlockID(x + xx, y + yy, z + zz);
					if (i2 == this.blockID) {
						int j2 = world.getBlockMetadata(x + xx, y + yy, z + zz);
						world.setBlockMetadata(x + xx, y + yy, z + zz, j2 | 8);
					}
				}
			}
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			int metadata = world.getBlockMetadata(x, y, z);

			// Is leaf marked to be checked?
			if ((metadata & 8) != 0) {
				if (this.surroundings == null) {
					this.surroundings = new int[32768];
				}

				for (int xx = -4; xx <= 4; ++xx) {
					for (int yy = -4; yy <= 4; ++yy) {
						for (int zz = -4; zz <= 4; ++zz) {
							Block block = Block.blocksList[world.getBlockID(x + xx, y + yy, z + zz)];

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

				int l2 = this.surroundings[16912];

				if (l2 >= 0) {
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

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		if(LevelThemeGlobalSettings.colorizedPlants && (meta & 0xf0) == 0) {
			return world.getFoliageColorFromCache(x, z);
		} else return getRenderColor(meta);
	}
	
	@Override
	public int getRenderColor(int meta) {
		if((meta & 0xf0) == 0) {
			if(LevelThemeGlobalSettings.colorizedPlants) {
				return ColorizerFoliage.getFoliageColorBasic();
			} else {
				return 0x5BFB3B;
			}
		} else return 0xFFFFFF;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		if(lockTextures) meta = 0;
		return (BlockLeavesBase.graphicsLevel ? 256 : 272) + (meta >> 4);
	}

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 15; i ++) {
			par3List.add(new ItemStack(par1, 1, i<<4));
		}
	}

	@Override
	public String getNameFromMeta(int meta) {
		return "leaves." + EnumTreeType.values()[meta >> 4].name;
	}

	@Override
	public int getIndexInTextureFromMeta(int meta) {
		return 2;
	}
	
    @Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
    	if(lockTextures) meta = 0;
    	meta &= 0xf0;
		if(!world.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			this.dropBlockAsItem_do(world, x, y, z, new ItemStack(this, 1, meta));
		} else {
			super.harvestBlock(world, player, x, y, z, meta);
		}

	}
}
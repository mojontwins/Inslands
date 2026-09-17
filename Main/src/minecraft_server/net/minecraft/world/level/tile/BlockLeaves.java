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
	
	public static final int DECAY_CHECK_BIT = 8;

	// --- Decay search geometry -------------------------------------------------
	// The 9x9x9 search box sits inside an 11x11x11 padded grid. The one-cell pad
	// lets every +-1 neighbour stride land on a real slot, so the inner flood-fill
	// loop needs no bounds checks.
	private static final int RADIUS = 4;                 // connectivity distance to a log
	private static final int SPAN = RADIUS * 2 + 1;      // 9 cells per axis
	private static final int GRID = SPAN + 2;            // 11 (padded dimension)
	private static final int GRID_AREA = GRID * GRID;    // 121
	private static final int HALF = GRID >> 1;           // 5 (centre offset)
	private static final int CENTER = HALF * GRID_AREA + HALF * GRID + HALF; // 665
	private static final int CELL_COUNT = SPAN * SPAN * SPAN; // 729

	/** The six +-1 neighbour strides, in x/y/z pairs. */
	private static final int[] NEIGHBOR_STRIDES = { GRID_AREA, -GRID_AREA, GRID, -GRID, 1, -1 };

	// Precomputed per-probe-cell tables: the block offset (dx/dy/dz) and the flat
	// grid cursor for each of the 729 cells, in scan order (x-major, z-minor).
	private static final int[] PROBE_DX = new int[CELL_COUNT];
	private static final int[] PROBE_DY = new int[CELL_COUNT];
	private static final int[] PROBE_DZ = new int[CELL_COUNT];
	private static final int[] PROBE_CURSOR = new int[CELL_COUNT];

	static {
		int i = 0;
		for(int dx = -RADIUS; dx <= RADIUS; ++dx) {
			for(int dy = -RADIUS; dy <= RADIUS; ++dy) {
				for(int dz = -RADIUS; dz <= RADIUS; ++dz) {
					PROBE_DX[i] = dx;
					PROBE_DY[i] = dy;
					PROBE_DZ[i] = dz;
					PROBE_CURSOR[i] = (dx + HALF) * GRID_AREA + (dy + HALF) * GRID + (dz + HALF);
					++i;
				}
			}
		}
	}

	/** Scratch grid reused across ticks (the block is a singleton, so one array serves). */
	private final int[] adjacency = new int[GRID * GRID * GRID];

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
		// Small optimization: When replaced with leaves or wood, surrounding leaves are
		// NOT affected
		Block block =  world.getBlock(x, y, z);
		if((block instanceof BlockLog) || (block instanceof BlockLeaves)) return;
		
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

			// Only a leaf a removed log has flagged needs re-checking; the rest stay put.
			if ((metadata & DECAY_CHECK_BIT) == 0) {
				return;
				}

			// Classify the neighbourhood: 0 = log, -2 = leaves, -1 = anything else.
			// Any wood-material block counts as a log (hollow/chipped logs included),
			// and every BlockLeaves subtype counts as leaves.
			for (int i = 0; i < CELL_COUNT; ++i) {
				Block block = Block.blocksList[world.getBlockID(x + PROBE_DX[i], y + PROBE_DY[i], z + PROBE_DZ[i])];
				int cursor = PROBE_CURSOR[i];

				if (block != null && block.blockMaterial == Material.wood) {
					adjacency[cursor] = 0;
				} else if (block instanceof BlockLeaves) {
					adjacency[cursor] = -2;
							} else {
					adjacency[cursor] = -1;
					}
				}

			// Flood from each log (distance 0) outward through leaves, at most RADIUS steps.
			for (int distance = 1; distance <= RADIUS; ++distance) {
				for (int i = 0; i < CELL_COUNT; ++i) {
					int cursor = PROBE_CURSOR[i];
					if (adjacency[cursor] == distance - 1) {
						for (int stride : NEIGHBOR_STRIDES) {
							int neighbor = cursor + stride;
							if (adjacency[neighbor] == -2) {
								adjacency[neighbor] = distance;
									}
									}
									}
									}
									}

			if (adjacency[CENTER] >= 0) {
				// Still tethered to a log: keep the leaves and clear the re-check mark.
				world.setBlockMetadata(x, y, z, metadata & ~DECAY_CHECK_BIT);
				} else {
					this.removeLeaves(world, x, y, z);
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
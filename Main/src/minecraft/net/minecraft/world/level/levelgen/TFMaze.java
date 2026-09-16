package net.minecraft.world.level.levelgen;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.IGroundSubstitute;

/**
 * A cell-based maze held on a "raw" grid, plus helpers that stamp the maze
 * into a {@link World}.
 *
 * <h3>The raw grid</h3>
 * The maze is generated on a grid of {@code cellsWide x cellsDeep} cells
 * (see {@link #generateRecursiveBacktracker}). Before it is drawn into the
 * world, the cell grid is mapped onto a finer "raw" grid that keeps one entry
 * per strip, where strips are either a whole cell or a cell divider:
 *
 * <pre>
 *   raw index    meaning                   world thickness
 *   -----------------------------------------------------------
 *   odd  (2k+1)  the passage cell k        {@link #cellStripWidth} blocks
 *   even (2k)    the divider wall at cell k {@link #wallStripWidth} blocks
 * </pre>
 *
 * The raw grid therefore holds {@code 2*cellsWide+1} by {@code 2*cellsDeep+1}
 * entries. Cell {@code (cx, cz)} lives at raw {@code (cx*2+1, cz*2+1)} and the
 * divider wall between neighbouring cells {@code (a,z)} and {@code (b,z)} sits
 * at raw {@code (a*2+2, z*2+1)} (one of the even indices).
 *
 * <h3>Raw-grid values</h3>
 * <ul>
 *   <li>{@link #WALL_RAW} (0)      - closed wall / not-yet-carved cell</li>
 *   <li>{@link #PASSAGE_RAW} (1)   - carved open passage cell</li>
 *   <li>{@link #OPEN_WALL_RAW} (2) - a divider wall knocked open by the carver</li>
 *   <li>{@link #ROOM} (5)          - part of a carved dungeon room</li>
 *   <li>{@link #OUT_OF_BOUNDS}     - reads that land outside the grid</li>
 * </ul>
 * When the maze is stamped into the world, every non-zero value is treated as
 * "open space"; value 0 is drawn as a wall.
 */
public class TFMaze {

	// ---------------------------------------------------------------------
	// Raw-grid values / sentinels
	// ---------------------------------------------------------------------

	/** Value returned by raw-grid reads that fall outside the grid. */
	public static final int OUT_OF_BOUNDS = Integer.MIN_VALUE;

	/** Raw-grid value marking the blocks of a carved room. */
	public static final int ROOM = 5;

	/** Raw-grid value for a closed wall (or a not-yet-carved cell). */
	public static final int WALL_RAW = 0;

	/** Raw-grid value for a carved open passage cell. */
	public static final int PASSAGE_RAW = 1;

	/** Raw-grid value for a divider wall that has been knocked open. */
	public static final int OPEN_WALL_RAW = 2;

	// ---------------------------------------------------------------------
	// Maze dimensions (measured in cells)
	// ---------------------------------------------------------------------

	/** Number of maze cells along the X axis. */
	public int cellsWide;

	/** Number of maze cells along the Z axis. */
	public int cellsDeep;

	// ---------------------------------------------------------------------
	// Strip thickness (controls how thick the maze walls / passages are)
	// ---------------------------------------------------------------------

	/**
	 * World thickness of the raw strips at even raw indices: the 1-block wide
	 * divider walls between cells.
	 */
	public int wallStripWidth = 1;

	/**
	 * World thickness of the raw strips at odd raw indices: the cell passages.
	 * Defaults to 3 (the hill maze); the hedge maze overrides this to 2.
	 */
	public int cellStripWidth = 3;

	// ---------------------------------------------------------------------
	// Stamping parameters
	// ---------------------------------------------------------------------

	/** How many blocks high every wall is built. */
	public int wallHeight = 3;

	/** How far below the maze floor the wall "roots" are extended. */
	public int rootDepth = 0;

	/**
	 * Origin (minimum corner) block coordinate of the maze, recorded by
	 * {@link #copyToWorld} / {@link #carveToWorld} and used by the
	 * decoration helpers.
	 */
	public int originX;
	public int originY;
	public int originZ;

	/**
	 * Placement policy used by the block writers:
	 * {@code 0} (default) only replaces {@link IGroundSubstitute} blocks,
	 * {@code 4} (set by the hedge maze) replaces anything.
	 */
	public int forceOverwrite = 0;

	/** Block + metadata used for walls. */
	public int wallBlockID = Block.mazeStone.blockID;
	public int wallBlockMeta = 0;

	/** Block + metadata used for the wall roots. */
	public int rootBlockID = Block.mazeStone.blockID;
	public int rootBlockMeta = 1;

	/** Block + metadata used for torches placed by {@link #placeTorches}. */
	public int torchBlockID = Block.torchWood.blockID;
	public int torchBlockMeta = 0;

	// ---------------------------------------------------------------------
	// Raw grid
	// ---------------------------------------------------------------------

	/** Raw-grid dimensions: {@code 2*cellsWide+1} and {@code 2*cellsDeep+1}. */
	public int gridWidth;
	public int gridDepth;

	/** Flat array holding the raw grid (one int per raw cell). */
	public int[] grid;

	/** Random source used by the maze generation. */
	public Random random = new Random();

	// ---------------------------------------------------------------------
	// Construction
	// ---------------------------------------------------------------------

	public TFMaze(int cellsWide, int cellsDeep) {
		this.cellsWide = cellsWide;
		this.cellsDeep = cellsDeep;
		this.gridWidth = cellsWide * 2 + 1;
		this.gridDepth = cellsDeep * 2 + 1;
		this.grid = new int[this.gridWidth * this.gridDepth];
	}

	// ---------------------------------------------------------------------
	// Cell-level access (coordinates are in maze cells)
	// ---------------------------------------------------------------------

	/** Returns the raw-grid value stored in cell {@code (cellX, cellZ)}. */
	public int getCell(int cellX, int cellZ) {
		return this.getRawCell(cellX * 2 + 1, cellZ * 2 + 1);
	}

	/** Writes a raw-grid value into cell {@code (cellX, cellZ)}. */
	public void putCell(int cellX, int cellZ, int value) {
		this.putRawCell(cellX * 2 + 1, cellZ * 2 + 1, value);
	}

	/** True if cell {@code (cellX, cellZ)} currently holds the given value. */
	public boolean cellEquals(int cellX, int cellZ, int value) {
		return this.getCell(cellX, cellZ) == value;
	}

	/**
	 * Returns the raw-grid value of the divider wall between cell
	 * {@code (fromCellX, fromCellZ)} and its neighbour {@code (toCellX, toCellZ)}.
	 * The two cells must be cardinally adjacent.
	 */
	public int getWall(int fromCellX, int fromCellZ, int toCellX, int toCellZ) {
		if(toCellX == fromCellX + 1 && toCellZ == fromCellZ) {
			// wall on the +X side of the cell
			return this.getRawCell(fromCellX * 2 + 2, fromCellZ * 2 + 1);
		} else if(toCellX == fromCellX - 1 && toCellZ == fromCellZ) {
			// wall on the -X side
			return this.getRawCell(fromCellX * 2 + 0, fromCellZ * 2 + 1);
		} else if(toCellX == fromCellX && toCellZ == fromCellZ + 1) {
			// wall on the +Z side
			return this.getRawCell(fromCellX * 2 + 1, fromCellZ * 2 + 2);
		} else if(toCellX == fromCellX && toCellZ == fromCellZ - 1) {
			// wall on the -Z side
			return this.getRawCell(fromCellX * 2 + 1, fromCellZ * 2 + 0);
		} else {
			System.out.println("Wall check out of bounds; s = " + fromCellX + ", " + fromCellZ + "; d = " + toCellX + ", " + toCellZ);
			return OUT_OF_BOUNDS;
		}
	}

	/**
	 * Writes a raw-grid value into the divider wall between two adjacent cells.
	 */
	public void putWall(int fromCellX, int fromCellZ, int toCellX, int toCellZ, int value) {
		if(toCellX == fromCellX + 1 && toCellZ == fromCellZ) {
			this.putRawCell(fromCellX * 2 + 2, fromCellZ * 2 + 1, value);
		}

		if(toCellX == fromCellX - 1 && toCellZ == fromCellZ) {
			this.putRawCell(fromCellX * 2 + 0, fromCellZ * 2 + 1, value);
		}

		if(toCellX == fromCellX && toCellZ == fromCellZ + 1) {
			this.putRawCell(fromCellX * 2 + 1, fromCellZ * 2 + 2, value);
		}

		if(toCellX == fromCellX && toCellZ == fromCellZ - 1) {
			this.putRawCell(fromCellX * 2 + 1, fromCellZ * 2 + 0, value);
		}

	}

	/**
	 * True if the divider wall between the two given cells is still standing
	 * (it has not been knocked open by the carver). Out-of-grid neighbours
	 * report "not a wall", i.e. the maze border counts as open.
	 */
	public boolean isWall(int fromCellX, int fromCellZ, int toCellX, int toCellZ) {
		return this.getWall(fromCellX, fromCellZ, toCellX, toCellZ) == WALL_RAW;
	}

	// ---------------------------------------------------------------------
	// Raw-grid access (coordinates are raw grid indices)
	// ---------------------------------------------------------------------

	/** Index into the flat {@link #grid} array for the given raw coords. */
	private int gridIndex(int rawX, int rawZ) {
		return rawZ * this.gridWidth + rawX;
	}

	protected void putRawCell(int rawX, int rawZ, int value) {
		if(rawX >= 0 && rawX < this.gridWidth && rawZ >= 0 && rawZ < this.gridDepth) {
			this.grid[this.gridIndex(rawX, rawZ)] = value;
		}

	}

	protected int getRawCell(int rawX, int rawZ) {
		return rawX >= 0 && rawX < this.gridWidth && rawZ >= 0 && rawZ < this.gridDepth ? this.grid[this.gridIndex(rawX, rawZ)] : OUT_OF_BOUNDS;
	}

	public boolean isEven(int n) {
		return n % 2 == 0;
	}

	// ---------------------------------------------------------------------
	// Strip footprint tables
	// ---------------------------------------------------------------------

	/**
	 * Builds the world coordinate at which every raw strip on one axis
	 * starts: raw index {@code raw} begins at {@code origin + raw/2*stride}.
	 */
	private int[] buildStripBase(int origin, int gridSize, int stripStride) {
		int[] base = new int[gridSize];
		for(int raw = 0; raw < gridSize; raw++) {
			base[raw] = origin + raw / 2 * stripStride;
		}

		return base;
	}

	/**
	 * Builds the full world-footprint tables for one maze axis: strip base,
	 * thickness (wallStripWidth for even/divider strips, cellStripWidth for
	 * odd/passage strips) and in-strip offset (0 for dividers, 1 for
	 * passages). Built per stamping call because the strip widths are
	 * configurable after construction.
	 */
	private int[][] buildStripTables(int origin, int gridSize, int stripStride) {
		int[] base = this.buildStripBase(origin, gridSize, stripStride);
		int[] thickness = new int[gridSize];
		int[] offset = new int[gridSize];
		for(int raw = 0; raw < gridSize; raw++) {
			boolean even = raw % 2 == 0;
			thickness[raw] = even ? this.wallStripWidth : this.cellStripWidth;
			offset[raw] = even ? 0 : 1;
		}

		return new int[][] {base, thickness, offset};
	}

	// ---------------------------------------------------------------------
	// Stamping into the world
	// ---------------------------------------------------------------------

	/**
	 * Draws the maze into the world with its minimum corner at
	 * {@code (minX, originY=minY, minZ)}.
	 *
	 * <p>Each raw entry is expanded to a {@link #wallStripWidth}/{@link #cellStripWidth}
	 * block wide strip: odd raw indices are the wide passages, even raw
	 * indices are the thin divider walls. A wall value ({@link #WALL_RAW})
	 * builds a wall column plus its roots; any other value carves the same
	 * footprint out of the terrain.</p>
	 */
	public void copyToWorld(World world, int minX, int minY, int minZ) {
		this.originX = minX;
		this.originY = minY;
		this.originZ = minZ;

		int stripStride = this.wallStripWidth + this.cellStripWidth;

		// Precompute the world coordinate/width/offset of every raw strip once
		// instead of recalculating them inside the rawX/rawZ loop.
		int[][] stripTablesX = this.buildStripTables(minX, this.gridWidth, stripStride);
		int[][] stripTablesZ = this.buildStripTables(minZ, this.gridDepth, stripStride);
		int[] stripBaseX = stripTablesX[0];
		int[] stripWidthX = stripTablesX[1];
		int[] stripOffsetX = stripTablesX[2];
		int[] stripBaseZ = stripTablesZ[0];
		int[] stripWidthZ = stripTablesZ[1];
		int[] stripOffsetZ = stripTablesZ[2];

		for(int rawX = 0; rawX < this.gridWidth; rawX++) {
			for(int rawZ = 0; rawZ < this.gridDepth; rawZ++) {
				int baseX = stripBaseX[rawX];
				int baseZ = stripBaseZ[rawZ];
				int widthX = stripWidthX[rawX];
				int widthZ = stripWidthZ[rawZ];
				int offsetX = stripOffsetX[rawX];
				int offsetZ = stripOffsetZ[rawZ];

				if(this.getRawCell(rawX, rawZ) == WALL_RAW) {
					// Closed wall: raise the wall column and sink its roots.
					for(int colX = 0; colX < widthX; colX++) {
						for(int colZ = 0; colZ < widthZ; colZ++) {
							for(int up = 0; up < this.wallHeight; up++) {
								this.putWallBlock(world, baseX + offsetX + colX, minY + up, baseZ + offsetZ + colZ);
							}

							for(int down = 0; down <= this.rootDepth; down++) {
								this.putRootBlock(world, baseX + offsetX + colX, minY - down, baseZ + offsetZ + colZ);
							}
						}
					}
				} else {
					// Open space: carve out the corresponding footprint.
					for(int colX = 0; colX < widthX; colX++) {
						for(int colZ = 0; colZ < widthZ; colZ++) {
							for(int up = 0; up < this.wallHeight; up++) {
								this.carveBlock(world, baseX + offsetX + colX, minY + up, baseZ + offsetZ + colZ);
							}
						}
					}
				}
			}
		}

		this.placeTorches(world);
	}

	/**
	 * Like {@link #copyToWorld} but only carves (opens) the passages,
	 * leaving the untouched wall strips in place. Used to punch a maze into
	 * already-generated terrain.
	 */
	public void carveToWorld(World world, int originX, int originY, int originZ) {
		this.originX = originX;
		this.originY = originY;
		this.originZ = originZ;

		int stripStride = this.wallStripWidth + this.cellStripWidth;

		// Precompute the world coordinate of every raw strip once instead of
		// recalculating them inside the rawX/rawZ loop.
		int[] stripBaseX = this.buildStripBase(originX, this.gridWidth, stripStride);
		int[] stripBaseZ = this.buildStripBase(originZ, this.gridDepth, stripStride);

		for(int rawX = 0; rawX < this.gridWidth; rawX++) {
			for(int rawZ = 0; rawZ < this.gridDepth; rawZ++) {
				if(this.getRawCell(rawX, rawZ) != WALL_RAW) {
					int baseX = stripBaseX[rawX];
					int baseZ = stripBaseZ[rawZ];

					if(this.isEven(rawX) && this.isEven(rawZ)) {
						// Intersection of two divider walls: single tall column.
						for(int up = 0; up < this.wallHeight; up++) {
							this.carveBlock(world, baseX, originY + up, baseZ);
						}
					} else {
						if(this.isEven(rawX) && !this.isEven(rawZ)) {
							// X wall strip crossing a Z passage strip.
							for(int passage = 1; passage <= this.cellStripWidth; passage++) {
								for(int up = 0; up < this.wallHeight; up++) {
									this.carveBlock(world, baseX, originY + up, baseZ + passage);
								}
							}
						} else if(!this.isEven(rawX) && this.isEven(rawZ)) {
							// X passage strip crossing a Z wall strip.
							for(int passage = 1; passage <= this.cellStripWidth; passage++) {
								for(int up = 0; up < this.wallHeight; up++) {
									this.carveBlock(world, baseX + passage, originY + up, baseZ);
								}
							}
						} else if(!this.isEven(rawX) && !this.isEven(rawZ)) {
							// Passage interior.
							for(int offX = 1; offX <= this.cellStripWidth; offX++) {
								for(int offZ = 1; offZ <= this.cellStripWidth; offZ++) {
									for(int up = 0; up < this.wallHeight; up++) {
										this.carveBlock(world, baseX + offX, originY + up, baseZ + offZ);
									}
								}
							}
						}
					}
				}
			}
		}

		this.placeTorches(world);
	}

	/**
	 * Places a wall block. Unless {@link #forceOverwrite} is non-zero, the
	 * block is only placed over {@link IGroundSubstitute} terrain.
	 */
	protected void putWallBlock(World world, int x, int y, int z) {
		if(this.forceOverwrite == 4 || world.getBlock(x, y, z) instanceof IGroundSubstitute)
			world.setBlockAndMetadataWithNotify(x, y, z, this.wallBlockID, this.wallBlockMeta);
	}

	/**
	 * Carves a block to air (same ground-substitute policy as walls).
	 */
	protected void carveBlock(World world, int x, int y, int z) {
		if(this.forceOverwrite == 4 || world.getBlock(x, y, z) instanceof IGroundSubstitute)
			world.setBlockAndMetadataWithNotify(x, y, z, 0, 0);
	}

	/**
	 * Places a "root" block below the maze floor (same policy as walls).
	 */
	protected void putRootBlock(World world, int x, int y, int z) {
		if(this.forceOverwrite == 4 || world.getBlock(x, y, z) instanceof IGroundSubstitute)
			world.setBlockAndMetadataWithNotify(x, y, z, this.rootBlockID, this.rootBlockMeta);
	}

	// ---------------------------------------------------------------------
	// Torches and tree decoration
	// ---------------------------------------------------------------------

	/**
	 * Walks the raw grid and drops torches onto selected wall corners. Only
	 * even/even raw positions (divider-wall intersections) are considered.
	 */
	public void placeTorches(World world) {
		byte torchHeight = 1;

		int stripStride = this.wallStripWidth + this.cellStripWidth;

		// Precompute the world coordinate of every raw strip once instead of
		// recalculating them inside the rawX/rawZ loop.
		int[] stripBaseX = this.buildStripBase(this.originX, this.gridWidth, stripStride);
		int[] stripBaseZ = this.buildStripBase(this.originZ, this.gridDepth, stripStride);

		for(int rawX = 0; rawX < this.gridWidth; rawX++) {
			for(int rawZ = 0; rawZ < this.gridDepth; rawZ++) {
				if(this.getRawCell(rawX, rawZ) == WALL_RAW) {
					int wallX = stripBaseX[rawX];
					int wallY = this.originY + torchHeight;
					int wallZ = stripBaseZ[rawZ];
					if(this.isEven(rawX) && this.isEven(rawZ) && this.shouldTorch(rawX, rawZ) && world.getBlockID(wallX, wallY, wallZ) == this.wallBlockID) {
						world.setBlockAndMetadataWithNotify(wallX, wallY, wallZ, this.torchBlockID, this.torchBlockMeta);
					}
				}
			}
		}

	}

	/**
	 * Decides whether a torch should be placed at wall corner {@code (rawX, rawZ)}.
	 * Torches only land in the middle of the maze (not on the grid border)
	 * and only where the surrounding wall geometry forms a bend (not a
	 * straight corridor along X or along Z).
	 */
	public boolean shouldTorch(int rawX, int rawZ) {
		boolean eastOutOfBounds = this.getRawCell(rawX + 1, rawZ) == OUT_OF_BOUNDS;
		boolean westOutOfBounds = this.getRawCell(rawX - 1, rawZ) == OUT_OF_BOUNDS;
		boolean southOutOfBounds = this.getRawCell(rawX, rawZ + 1) == OUT_OF_BOUNDS;
		boolean northOutOfBounds = this.getRawCell(rawX, rawZ - 1) == OUT_OF_BOUNDS;

		if(eastOutOfBounds || westOutOfBounds || southOutOfBounds || northOutOfBounds) return false;

		boolean straightAlongX = this.getRawCell(rawX + 1, rawZ) == WALL_RAW && this.getRawCell(rawX - 1, rawZ) == WALL_RAW;
		boolean straightAlongZ = this.getRawCell(rawX, rawZ + 1) == WALL_RAW && this.getRawCell(rawX, rawZ - 1) == WALL_RAW;

		// No torches in straight corridors - only around bends.
		if(straightAlongX || straightAlongZ) return false;

		return this.random.nextInt(2) == 0;
	}

	/**
	 * Decides whether a tree should be spawned on wall corner
	 * {@code (rawX, rawZ)} (used by subclass stampers that scatter trees).
	 * Cells on the maze border that are open along one axis always get one;
	 * otherwise trees are rare (1 in 50).
	 */
	public boolean shouldTree(int rawX, int rawZ) {
		boolean interiorAlongX = rawX != 0 && rawX != this.gridWidth - 1;
		boolean sealedNorthSouth = this.getRawCell(rawX, rawZ + 1) == WALL_RAW && this.getRawCell(rawX, rawZ - 1) == WALL_RAW;

		if(!interiorAlongX && !sealedNorthSouth) return true;

		boolean onZBorder = rawZ == 0 || rawZ == this.gridDepth - 1;
		boolean openEastWest = this.getRawCell(rawX + 1, rawZ) != WALL_RAW || this.getRawCell(rawX - 1, rawZ) != WALL_RAW;

		if(onZBorder && openEastWest) return true;

		return this.random.nextInt(50) == 0;
	}

	/**
	 * World X coordinate of the centre line of cell {@code cellX} (i.e. the
	 * start of the maze plus the cell offset, plus one block for the leading
	 * divider wall).
	 */
	public int getWorldX(int cellX) {
		return this.originX + cellX * (this.wallStripWidth + this.cellStripWidth) + 1;
	}

	/** World Z coordinate of the centre line of cell {@code cellZ}. */
	public int getWorldZ(int cellZ) {
		return this.originZ + cellZ * (this.wallStripWidth + this.cellStripWidth) + 1;
	}

	// ---------------------------------------------------------------------
	// Room carving
	// ---------------------------------------------------------------------

	/**
	 * Carves a 3-cell-wide "cross" room around cell {@code (cellX, cellZ)}
	 * (a plus sign centred on the cell).
	 */
	public void carveRoom0(int cellX, int cellZ) {
		this.putCell(cellX, cellZ, ROOM);
		this.putCell(cellX + 1, cellZ, ROOM);
		this.putWall(cellX, cellZ, cellX + 1, cellZ, ROOM);
		this.putCell(cellX - 1, cellZ, ROOM);
		this.putWall(cellX, cellZ, cellX - 1, cellZ, ROOM);
		this.putCell(cellX, cellZ + 1, ROOM);
		this.putWall(cellX, cellZ, cellX, cellZ + 1, ROOM);
		this.putCell(cellX, cellZ - 1, ROOM);
		this.putWall(cellX, cellZ, cellX, cellZ - 1, ROOM);
	}

	/**
	 * Carves a 5x5 raw square room around cell {@code (cellX, cellZ)}, then
	 * re-seals the four cardinal raw cells next to the centre so the room ends
	 * up as a plus/cross shape (open diagonals, blocked N/E/S/W), and finally
	 * widens each of the four exits one raw cell further out.
	 */
	public void carveRoom1(int cellX, int cellZ) {
		int centreRawX = cellX * 2 + 1;
		int centreRawZ = cellZ * 2 + 1;

		for(int offsetX = -2; offsetX <= 2; offsetX++) {
			for(int offsetZ = -2; offsetZ <= 2; offsetZ++) {
				this.putRawCell(centreRawX + offsetX, centreRawZ + offsetZ, ROOM);
			}
		}

		// Re-seal the four cardinal raw neighbours (opening the diagonals).
		// NOTE: these used to be fed through putCell(), which treats its
		// arguments as cell coordinates and multiplied into raw space, so the
		// write landed on unrelated cells. Fixed to operate in raw space.
		this.putRawCell(centreRawX, centreRawZ + 1, WALL_RAW);
		this.putRawCell(centreRawX, centreRawZ - 1, WALL_RAW);
		this.putRawCell(centreRawX + 1, centreRawZ, WALL_RAW);
		this.putRawCell(centreRawX - 1, centreRawZ, WALL_RAW);

		// Widen the four exits by one raw cell when there is room.
		if(this.getRawCell(centreRawX, centreRawZ + 4) != OUT_OF_BOUNDS) {
			this.putRawCell(centreRawX, centreRawZ + 3, ROOM);
		}

		if(this.getRawCell(centreRawX, centreRawZ - 4) != OUT_OF_BOUNDS) {
			this.putRawCell(centreRawX, centreRawZ - 3, ROOM);
		}

		if(this.getRawCell(centreRawX + 4, centreRawZ) != OUT_OF_BOUNDS) {
			this.putRawCell(centreRawX + 3, centreRawZ, ROOM);
		}

		if(this.getRawCell(centreRawX - 4, centreRawZ) != OUT_OF_BOUNDS) {
			this.putRawCell(centreRawX - 3, centreRawZ, ROOM);
		}

	}

	/**
	 * Knocks open the four mid-edge wall cells so the maze connects to the
	 * outside on every side.
	 */
	public void add4Exits() {
		int midRawX = this.gridWidth / 2 + 1;
		int midRawZ = this.gridDepth / 2 + 1;
		this.putRawCell(midRawX, 0, ROOM);
		this.putRawCell(midRawX, this.gridDepth - 1, ROOM);
		this.putRawCell(0, midRawZ, ROOM);
		this.putRawCell(this.gridWidth - 1, midRawZ, ROOM);
	}

	// ---------------------------------------------------------------------
	// Maze generation (recursive backtracker)
	// ---------------------------------------------------------------------

	/**
	 * Entry point for the recursive-backtracker maze generation, starting at
	 * cell {@code (startCellX, startCellZ)}.
	 */
	public void generateRecursiveBacktracker(int startCellX, int startCellZ) {
		this.growMazeFrom(startCellX, startCellZ);
	}

	/**
	 * Recursive-backtracker maze generation starting at cell
	 * {@code (startCellX, startCellZ)}.
	 *
	 * <p>Implemented with an explicit LIFO frame stack instead of recursion so
	 * the call depth is bounded by the cell count (no stack-overflow risk on
	 * large mazes). The stack replays the exact frame order of the old
	 * recursion &mdash; after punching through to a target, the target frame is
	 * pushed and processed first, then the current cell is re-enqueued twice
	 * &mdash; so the RNG draw order and the resulting maze are unchanged.</p>
	 */
	public void growMazeFrom(int startCellX, int startCellZ) {
		// Each frame that finds an unvisited neighbour punches one wall and
		// pushes three frames (target + current cell twice), so the stack can
		// never exceed three times the number of cells.
		int maxFrames = this.cellsWide * this.cellsDeep * 3;
		int[] stackX = new int[maxFrames];
		int[] stackZ = new int[maxFrames];
		int stackSize = 1;
		stackX[0] = startCellX;
		stackZ[0] = startCellZ;

		while(stackSize > 0) {
			int cellX = stackX[--stackSize];
			int cellZ = stackZ[stackSize];

			this.putCell(cellX, cellZ, PASSAGE_RAW);

			int unvisitedNeighbours = 0;
			if(this.cellEquals(cellX + 1, cellZ, WALL_RAW)) unvisitedNeighbours++;
			if(this.cellEquals(cellX - 1, cellZ, WALL_RAW)) unvisitedNeighbours++;
			if(this.cellEquals(cellX, cellZ + 1, WALL_RAW)) unvisitedNeighbours++;
			if(this.cellEquals(cellX, cellZ - 1, WALL_RAW)) unvisitedNeighbours++;

			if(unvisitedNeighbours == 0) continue;

			// Pick the target neighbour by "counting down" the random pick
			// across the four cardinal directions.
			int pick = this.random.nextInt(unvisitedNeighbours);
			int targetCellX = 0;
			int targetCellZ = 0;

			if(this.cellEquals(cellX + 1, cellZ, WALL_RAW)) {
				if(pick == 0) {
					targetCellX = cellX + 1;
					targetCellZ = cellZ;
				}

				pick--;
			}

			if(this.cellEquals(cellX - 1, cellZ, WALL_RAW)) {
				if(pick == 0) {
					targetCellX = cellX - 1;
					targetCellZ = cellZ;
				}

				pick--;
			}

			if(this.cellEquals(cellX, cellZ + 1, WALL_RAW)) {
				if(pick == 0) {
					targetCellX = cellX;
					targetCellZ = cellZ + 1;
				}

				pick--;
			}

			if(this.cellEquals(cellX, cellZ - 1, WALL_RAW) && pick == 0) {
				targetCellX = cellX;
				targetCellZ = cellZ - 1;
			}

			// Knock the chosen divider wall open and continue. The target is
			// pushed last so it is popped (and explored) first, exactly like
			// the original recursion, and the current cell is re-enqueued
			// twice so it can punch further connections to remaining
			// unvisited neighbours.
			this.putWall(cellX, cellZ, targetCellX, targetCellZ, OPEN_WALL_RAW);

			stackX[stackSize] = cellX;
			stackZ[stackSize] = cellZ;
			stackSize++;
			stackX[stackSize] = cellX;
			stackZ[stackSize] = cellZ;
			stackSize++;
			stackX[stackSize] = targetCellX;
			stackZ[stackSize] = targetCellZ;
			stackSize++;
		}
	}

	/** Seeds the internal random source so a maze can be reproduced. */
	public void setSeed(long newSeed) {
		this.random.setSeed(newSeed);
	}
}
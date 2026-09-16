package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.TFMaze;
import net.minecraft.world.level.levelgen.TFTreasure;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockBigMushroom;
import net.minecraft.world.level.tile.BlockLog;
import net.minecraft.world.level.tile.IBigPlants;
import net.minecraft.world.level.tile.entity.TileEntityMobSpawner;

/**
 * Generates a hedge maze on a mostly flat grass island.
 *
 * <p>The maze is built on top of already-generated chunks. It first checks
 * that the terrain roughly matches a flat platform (height variance &lt;= 16
 * blocks, no point below {@code minY}), then clears the area, lays a grass
 * floor, stamps the maze (hedge walls driven straight through whatever is in
 * the way, {@code forceOverwrite = 4}), and decorates dead ends and the
 * carved rooms.</p>
 *
 * <p>This generator rewrote the original so that {@code size} controls the
 * number of maze cells instead of the original hardcoded 16x16 maze with the
 * same footprint for every size.</p>
 */
public class TFGenHedgeMaze extends TFGenerator {
	/** Number of maze cells per side (the maze is always square). */
	int mazeCells;

	/** Lowest y the terrain may drop to before the maze candidacy is rejected. */
	int minY;

	/** The cell maze being built/stamped. */
	TFMaze maze;

	/** Random source for both generation and decoration. */
	Random rand;

	boolean debug = false;

	public TFGenHedgeMaze(int size) {
		this(size, 64);
	}

	public TFGenHedgeMaze(int size, int minY) {
		this.mazeCells = size;
		this.minY = minY;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		// The original code had some hardcoded stuff that meant
		// that it only really worked for size 3 mazes. That's
		// why I've rewriten some of it plus added a different
		// carver based upon the notion that we are operating on
		// chunks that have been generated already.

		this.worldObj = world;
		this.rand = rand;

		// The original maze structure was 16x16 cells with each cell being
		// 3x3 blocks (oddBias). That gave rendered mazes 16*3+1 blocks per
		// side. The original code also placed the maze 7+48 blocks into the
		// negative X/Z direction, hoping those chunks were already generated.
		// Here, mazeCells controls the amount of cells and (x, z) is the
		// centre of the maze.

		TFMaze maze = new TFMaze(this.mazeCells, this.mazeCells);
		this.maze = maze;

		// Hedge passages are only 2 blocks wide (the default is 3).
		maze.cellStripWidth = 2;

		// Maze metrics: each raw strip pair (wall + passage) advances one
		// stripStride in world space, and the border wall adds one block.
		int stripStride = maze.cellStripWidth + maze.wallStripWidth;
		int mazeSizeBlocks = this.mazeCells * stripStride + 1;
		int minX = x - mazeSizeBlocks / 2;
		int minZ = z - mazeSizeBlocks / 2;

		// To decide if we are generating the maze here, let's calculate
		// the min and max land surface height in the area. Note that we'll
		// be leaving a border of 3 blocks around the maze.
		int minHeight = 128;
		int maxHeight = 0;
		for(int xx = minX - 3; xx < minX + mazeSizeBlocks + 3; xx++) {
			for(int zz = minZ - 3; zz < minZ + mazeSizeBlocks + 3; zz++) {
				int height = world.getLandSurfaceHeightValue(xx, zz);
				if(height < this.minY) return false;
				if(height < minHeight) minHeight = height;
				if(height > maxHeight) maxHeight = height;
			}
		}

		if(debug) System.out.println("Attempting hedge @ " + x + " " + z + " (" + mazeSizeBlocks + ")");
		if(debug) System.out.println("minH " + minHeight + ", maxH " + maxHeight);

		// Too much land variance.
		if(maxHeight - minHeight > 16) return false;

		// The higher the terrain (i.e. the smaller maxH - minH), the more the
		// maze sinks into the ground; sink height is clamped to 4 blocks.
		int sink = (maxHeight - minHeight - 3) / 2;
		if(sink > 4) sink = 4;

		y = minHeight + sink;
		if(y < 64) y = 64;

		if(debug) System.out.println("Succeeded @ " + x + " " + y + " " + z);

		// At this point, we can say that the maze gen has succeeded.
		maze.torchBlockID = Block.torchWood.blockID;
		maze.wallBlockID = Block.hedge.blockID;
		maze.forceOverwrite = 4;
		maze.wallHeight = 3;
		maze.rootDepth = 3;

		// Carve

		// The space occupied by the actual labyrinth must be blank. Three
		// shrinking layers guarantee a flat working area.
		for(int i = 1; i < 4; i++) {
			for(int xx = minX - i; xx < minX + mazeSizeBlocks + i; xx++) {
				for(int zz = minZ - i; zz < minZ + mazeSizeBlocks + i; zz++) {
					world.setBlock(xx, y + i - 1, zz, 0);
				}
			}
		}

		// Carve up to delete everything but trunks, leaves and mushrooms,
		// and extend vertical logs down in case we overwrote a hill.
		for(int i = y + 3; i <= maxHeight; i++) {
			for(int xx = minX - 3; xx < minX + mazeSizeBlocks + 3; xx++) {
				for(int zz = minZ - 3; zz < minZ + mazeSizeBlocks + 3; zz++) {
					// Replace with air or with a potential vertical log above if not a plant.
					Block block = world.getBlock(xx, i, zz);
					if(!(block instanceof IBigPlants)) {
						BlockState blockStateUp = world.getBlockStateAt(xx, i + 1, zz);
						if(this.verticalBranch(blockStateUp)) {
							for(int j = i; j >= y + 3; j--) {
								world.setBlockState(xx, j, zz, blockStateUp);
							}
						} else {
							world.setBlock(xx, i, zz, 0);
						}
					}
				}
			}
		}

		// Grass ground. NOTE: the floor uses mazeCells*3 (not mazeSizeBlocks)
		// so its extent is one block short on each side - kept for parity.
		this.fill(minX, y - 1, minZ, this.mazeCells * 3, 1, this.mazeCells * 3, Block.grass.blockID, 0);

		// Decorations: eight jack-o'-lanterns lining the four sides.
		this.putBlockAndMetadata(minX - 1, y, minZ + 23, Block.pumpkinLantern.blockID, 1, true);
		this.putBlockAndMetadata(minX - 1, y, minZ + 28, Block.pumpkinLantern.blockID, 1, true);
		this.putBlockAndMetadata(minX + mazeSizeBlocks, y, minZ + 23, Block.pumpkinLantern.blockID, 3, true);
		this.putBlockAndMetadata(minX + mazeSizeBlocks, y, minZ + 28, Block.pumpkinLantern.blockID, 3, true);
		this.putBlockAndMetadata(minX + 23, y, minZ - 1, Block.pumpkinLantern.blockID, 2, true);
		this.putBlockAndMetadata(minX + 28, y, minZ - 1, Block.pumpkinLantern.blockID, 2, true);
		this.putBlockAndMetadata(minX + 23, y, minZ + mazeSizeBlocks, Block.pumpkinLantern.blockID, 0, true);
		this.putBlockAndMetadata(minX + 28, y, minZ + mazeSizeBlocks, Block.pumpkinLantern.blockID, 0, true);

		// Main rooms: one carved room per 3 cells, spaced apart.
		int roomCount = this.mazeCells / 3;
		int[] roomCoords = new int[roomCount * 2];

		for(int i = 0; i < roomCount; ++i) {
			int roomX;
			int roomZ;
			do {
				roomX = rand.nextInt(this.mazeCells - 2) + 1;
				roomZ = rand.nextInt(this.mazeCells - 2) + 1;
			} while(this.isNearRoom(roomX, roomZ, roomCoords));

			maze.carveRoom1(roomX, roomZ);
			roomCoords[i * 2] = roomX;
			roomCoords[i * 2 + 1] = roomZ;
		}

		// Calculate maze structure
		maze.generateRecursiveBacktracker(0, 0);
		maze.add4Exits();

		// Draw maze
		maze.copyToWorld(this.worldObj, minX, y, minZ);
		this.decorate3x3Rooms(roomCoords);

		// Now, the layer @ y + 3 may contain partial trees. Hopefully most
		// will have a trunk, otherwise we can only leave the leaves to
		// naturally decay.
		for(int xx = minX - 3; xx < minX + mazeSizeBlocks + 3; xx++) {
			for(int zz = minZ - 3; zz < minZ + mazeSizeBlocks + 3; zz++) {
				BlockState blockState = world.getBlockStateAt(xx, y + 3, zz);

				if(this.verticalBranch(blockState)) {
					int yy = y + 2;
					while(world.isAirBlock(xx, yy, zz)) {
						world.setBlockState(xx, yy, zz, blockState);
						yy--;
					}
				}
			}
		}

		System.out.println("Hedge maze @ " + x + " " + z);

		return true;
	}

	/**
	 * True if the given block state is a "vertical branch" that must be
	 * extended downwards until it meets solid ground: an upright log, or a
	 * mushroom stem (metadata 15).
	 */
	private boolean verticalBranch(BlockState blockState) {
		Block block = blockState.getBlock();
		int meta = blockState.getMetadata();

		return (
				(block instanceof BlockLog && (meta & 4) == 0) ||
				(block instanceof BlockBigMushroom && meta == 15)
		);
	}

	/** True if cell (dx, dz) sits within 3 cells of any already-picked room. */
	protected boolean isNearRoom(int dx, int dz, int[] roomCoords) {
		for(int i = 0; i < roomCoords.length / 2; ++i) {
			int roomX = roomCoords[i * 2];
			int roomZ = roomCoords[i * 2 + 1];
			if((roomX != 0 || roomZ != 0) && Math.abs(dx - roomX) < 3 && Math.abs(dz - roomZ) < 3) {
				return true;
			}
		}

		return false;
	}

	void decorate3x3Rooms(int[] roomCoords) {
		for(int i = 0; i < roomCoords.length / 2; ++i) {
			int roomCellX = roomCoords[i * 2];
			int roomCellZ = roomCoords[i * 2 + 1];
			this.decorate3x3Room(roomCellX, roomCellZ);
		}
	}

	void decorate3x3Room(int roomCellX, int roomCellZ) {
		int dx = this.maze.getWorldX(roomCellX) + 1;
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(roomCellZ) + 1;
		this.roomSpawner(dx, dy, dz, 8);
		if(!this.roomTreasure(dx, dy, dz, 8)) {
			this.roomTreasure(dx, dy, dz, 8);
		}

		if(!this.roomJackO(dx, dy, dz, 8) || this.rand.nextInt(4) == 0) {
			this.roomJackO(dx, dy, dz, 8);
		}
	}

	private boolean roomSpawner(int dx, int dy, int dz, int diameter) {
		int rx = this.rand.nextInt(diameter) + dx - diameter / 2;
		int rz = this.rand.nextInt(diameter) + dz - diameter / 2;
		String mobID;
		switch(this.rand.nextInt(3)) {
		case 0:
		default:
			mobID = "HedgeSpider";
			break;
		case 1:
			mobID = "SwarmSpider";
			break;
		case 2:
			mobID = "HostileWolf";
		}

		return this.placeMobSpawner(rx, dy, rz, mobID);
	}

	private boolean roomTreasure(int dx, int dy, int dz, int diameter) {
		int rx = this.rand.nextInt(diameter) + dx - diameter / 2;
		int rz = this.rand.nextInt(diameter) + dz - diameter / 2;
		return this.worldObj.getBlockID(rx, dy, rz) != 0 ? false : TFTreasure.hedgemaze.generate(this.worldObj, this.rand, rx, dy, rz);
	}

	protected boolean placeMobSpawner(int dx, int dy, int dz, String mobID) {
		this.worldObj.setBlockWithNotify(dx, dy, dz, Block.mobSpawner.blockID);
		TileEntityMobSpawner spawner = (TileEntityMobSpawner)this.worldObj.getBlockTileEntity(dx, dy, dz);
		if(spawner != null) {
			spawner.setMobID(mobID);
			return true;
		} else {
			return false;
		}
	}

	private boolean roomJackO(int dx, int dy, int dz, int diameter) {
		int rx = this.rand.nextInt(diameter) + dx - diameter / 2;
		int rz = this.rand.nextInt(diameter) + dz - diameter / 2;
		if(this.worldObj.getBlockID(rx, dy, rz) != 0) {
			return false;
		} else {
			this.worldObj.setBlockAndMetadataWithNotify(rx, dy, rz, Block.pumpkinLantern.blockID, this.rand.nextInt(4));
			return true;
		}
	}
}
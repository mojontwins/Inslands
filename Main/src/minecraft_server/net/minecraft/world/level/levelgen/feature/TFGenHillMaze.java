package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.entity.EntityPainting;
import net.minecraft.world.entity.EnumArt;
import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.TFMaze;
import net.minecraft.world.level.levelgen.TFTreasure;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.entity.TileEntityMobSpawner;
import net.minecraft.world.level.tile.entity.TileEntityMobSpawnerOneshot;

/**
 * Generates a stone maze inside a hill-shaped terrain mound.
 *
 * <p>Unlike the hedge maze this does not level the ground: it turns the
 * {@code y - 1} floor into maze stone, caps the whole area at {@code y + 3},
 * stamps the maze, then decorates every dead end (spiders, webs, treasures,
 * paintings, traps, doors, fountains...) and the carved 3x3 rooms.</p>
 */
public class TFGenHillMaze extends TFGenerator {
	/** How small/large the maze is: 1 -> 11, 2 -> 19, 3 -> 27 cells per side. */
	int mazeScale;

	/** The cell maze being built/stamped. */
	TFMaze maze;

	/** Random source for both generation and decoration. */
	Random rand;

	/** When true, candidacy requires the fill area to be mostly solid. */
	boolean checkSolid;

	/** Required solidity percentage used by the {@link #checkSolid} test. */
	int solidPercent = 60;

	boolean debug = false;

	/**
	 * All painting artworks, cached once instead of re-enumerated for every
	 * {@link #deadEndPainting} decoration.
	 */
	static final EnumArt[] ART_VALUES = EnumArt.values();

	/** Compass faces (also used as the "f" decoration parameter): opening faces -Z. */
	static final int FACE_NORTH = 0;
	/** Opening faces +X. */
	static final int FACE_EAST = 1;
	/** Opening faces +Z. */
	static final int FACE_SOUTH = 2;
	/** Opening faces -X. */
	static final int FACE_WEST = 3;

	public TFGenHillMaze(int size, boolean checksolid, int solidPercent) {
		this.mazeScale = size;
		this.checkSolid = checksolid;
		this.solidPercent = solidPercent;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(this.debug) System.out.println("Attempting hill maze @ " + x + " " + y + " " + z);

		this.worldObj = world;
		this.rand = rand;

		// The maze is offset 7+scale*16 blocks into the negative X/Z so it
		// slides under an already-generated hill (x, z is the hill centre).
		int minX = x - 7 - this.mazeScale * 16;
		int minZ = z - 7 - this.mazeScale * 16;

		byte cellsPerSide = 11;
		if(this.mazeScale == 2) {
			cellsPerSide = 19;
		} else if(this.mazeScale == 3) {
			cellsPerSide = 27;
		}

		if(this.checkSolid) {
			if(!this.checkMostlySolid(minX, y - 1, minZ, cellsPerSide * 4, 5, cellsPerSide * 4, this.solidPercent)) {
				if(this.debug) System.out.println("Hill maze failed, not " + this.solidPercent + " solid.");
				return false;
			}
		}

		// Lay the stone floor and cap the labyrinth roof over the fill area.
		this.fillIfGround(minX, y - 1, minZ, cellsPerSide * 4, 1, cellsPerSide * 4, Block.mazeStone.blockID, 1);
		//this.fill(minX, y, minZ, cellsPerSide * 4, 3, cellsPerSide * 4, 0, 0);
		this.fillIfGround(minX, y + 3, minZ, cellsPerSide * 4, 1, cellsPerSide * 4, Block.mazeStone.blockID, 2);

		this.maze = new TFMaze(cellsPerSide, cellsPerSide);
		int roomCount = cellsPerSide / 3;
		int[] roomCoords = new int[roomCount * 2];

		for(int i = 0; i < roomCount; ++i) {
			int roomX;
			int roomZ;
			do {
				roomX = rand.nextInt(cellsPerSide - 2) + 1;
				roomZ = rand.nextInt(cellsPerSide - 2) + 1;
			} while(this.isNearRoom(roomX, roomZ, roomCoords));

			this.maze.carveRoom1(roomX, roomZ);
			roomCoords[i * 2] = roomX;
			roomCoords[i * 2 + 1] = roomZ;
		}

		this.maze.generateRecursiveBacktracker(0, 0);
		this.maze.copyToWorld(this.worldObj, minX, y, minZ);
		this.decorateDeadEnds();
		this.decorate3x3Rooms(roomCoords);

		System.out.println("Hill maze @ " + x + " " + y + " " + z);

		return true;
	}

	/** True if cell (roomX, roomZ) sits within 3 cells of any already-picked room. */
	protected boolean isNearRoom(int roomX, int roomZ, int[] roomCoords) {
		for(int i = 0; i < roomCoords.length / 2; ++i) {
			int otherX = roomCoords[i * 2];
			int otherZ = roomCoords[i * 2 + 1];
			if((otherX != 0 || otherZ != 0) && Math.abs(roomX - otherX) < 3 && Math.abs(roomZ - otherZ) < 3) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Walks every cell and decorates the dead ends: cells that have exactly
	 * one open neighbour. {@code f} records which neighbour that is, using the
	 * {@link #FACE_NORTH}/{@link #FACE_EAST}/{@link #FACE_SOUTH}/{@link #FACE_WEST}
	 * ordinals expected by the {@code deadEnd*} helpers.
	 */
	public void decorateDeadEnds() {
		for(int x = 0; x < this.maze.cellsWide; ++x) {
			for(int z = 0; z < this.maze.cellsDeep; ++z) {
				boolean westOpen = !this.maze.isWall(x, z, x - 1, z);
				boolean eastOpen = !this.maze.isWall(x, z, x + 1, z);
				boolean northOpen = !this.maze.isWall(x, z, x, z - 1);
				boolean southOpen = !this.maze.isWall(x, z, x, z + 1);
				boolean southSidesClosed = this.maze.isWall(x, z, x, z + 1);

				if(westOpen && this.maze.isWall(x, z, x + 1, z) && this.maze.isWall(x, z, x, z - 1) && southSidesClosed) {
					this.decorateDeadEnd(x, z, FACE_WEST);
				}

				if(this.maze.isWall(x, z, x - 1, z) && eastOpen && this.maze.isWall(x, z, x, z - 1) && southSidesClosed) {
					this.decorateDeadEnd(x, z, FACE_EAST);
				}

				if(this.maze.isWall(x, z, x - 1, z) && this.maze.isWall(x, z, x + 1, z) && northOpen && southSidesClosed) {
					this.decorateDeadEnd(x, z, FACE_NORTH);
				}

				if(this.maze.isWall(x, z, x - 1, z) && this.maze.isWall(x, z, x + 1, z) && this.maze.isWall(x, z, x, z - 1) && southOpen) {
					this.decorateDeadEnd(x, z, FACE_SOUTH);
				}
			}
		}
	}

	/** Picks a random decoration for the dead end at cell (x, z) with the given opening face. */
	public void decorateDeadEnd(int x, int z, int face) {
		int roll = this.rand.nextInt(17);
		switch(roll) {
		case 0:
			this.deadEndSpiderSpawner(x, z, face);
			break;
		case 1:
			this.deadEndWebs(x, z, face);
			break;
		case 2:
			this.deadEndTreasure(x, z, face);
			break;
		case 3:
			this.deadEndSpawner(x, z, face);
			break;
		case 4:
			this.deadEndPainting(x, z, face);
			break;
		case 5:
			this.deadEndTrap(x, z, face);
			break;
		case 6:
			this.deadEndTrappedChest(x, z, face);
			break;
		case 7:
			this.deadEndTorch(x, z, face);
			break;
		case 8:
			this.deadEndTorchRedstone(x, z, face);
			break;
		case 9:
			this.deadEndFountain(x, z, face);
			break;
		case 10:
			this.deadEndLavaFountain(x, z, face);
			break;
		case 11:
			this.deadEndDoorway(x, z, face);
			break;
		case 12:
			this.deadEndDoor(x, z, face);
			break;
		case 13:
			this.deadEndDoorSteel(x, z, face);
			break;
		case 14:
			this.deadEndDoorTreasure(x, z, face);
		}
	}

	void deadEndSpiderSpawner(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		this.deadEndWebs(x, z, face);
		String spiderType = this.rand.nextBoolean() ? "Spider" : "SwarmSpider";
		if(face == FACE_NORTH) {
			this.placeMobSpawner(dx + 1, dy + 0, dz + 2, spiderType);
		} else if(face == FACE_EAST) {
			this.placeMobSpawner(dx + 0, dy + 0, dz + 1, spiderType);
		} else if(face == FACE_SOUTH) {
			this.placeMobSpawner(dx + 1, dy + 0, dz + 0, spiderType);
		} else if(face == FACE_WEST) {
			this.placeMobSpawner(dx + 2, dy + 0, dz + 1, spiderType);
		}
	}

	/** True if at least 6 of the 9 blocks in the 3x3 floor around (dx, dz) are opaque. */
	boolean mostFloor(int dx, int dy, int dz) {
		int blocks = 0;
		for(int x = dx; x <= dx + 2; x++) {
			for(int z = dz; z <= dz + 2; z++) {
				if(this.worldObj.isBlockOpaqueCube(x, dy, z)) blocks++;
			}
		}

		return blocks > 5;
	}

	/** Cobweb infestation centred just inside the dead-end opening. */
	void deadEndWebs(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);

		if(!this.mostFloor(dx, dy - 1, dz)) return;

		if(face == FACE_NORTH) {
			this.setWeb(this.worldObj, dx + 1, dy + 0, dz + 1);
			this.setWeb(this.worldObj, dx + 0, dy + 1, dz + 1);
			this.setWeb(this.worldObj, dx + 2, dy + 1, dz + 1);
			this.setWeb(this.worldObj, dx + 1, dy + 2, dz + 1);
			this.setWeb(this.worldObj, dx + 0, dy + 0, dz + 2);
			this.setWeb(this.worldObj, dx + 2, dy + 0, dz + 2);
			this.setWeb(this.worldObj, dx + 1, dy + 1, dz + 2);
		} else if(face == FACE_EAST) {
			this.setWeb(this.worldObj, dx + 1, dy + 0, dz + 1);
			this.setWeb(this.worldObj, dx + 1, dy + 1, dz + 0);
			this.setWeb(this.worldObj, dx + 1, dy + 1, dz + 2);
			this.setWeb(this.worldObj, dx + 1, dy + 2, dz + 1);
			this.setWeb(this.worldObj, dx + 0, dy + 0, dz + 0);
			this.setWeb(this.worldObj, dx + 0, dy + 0, dz + 2);
			this.setWeb(this.worldObj, dx + 0, dy + 1, dz + 1);
		} else if(face == FACE_SOUTH) {
			this.setWeb(this.worldObj, dx + 1, dy + 0, dz + 1);
			this.setWeb(this.worldObj, dx + 0, dy + 1, dz + 1);
			this.setWeb(this.worldObj, dx + 2, dy + 1, dz + 1);
			this.setWeb(this.worldObj, dx + 1, dy + 2, dz + 1);
			this.setWeb(this.worldObj, dx + 0, dy + 0, dz + 0);
			this.setWeb(this.worldObj, dx + 2, dy + 0, dz + 0);
			this.setWeb(this.worldObj, dx + 1, dy + 1, dz + 0);
		} else if(face == FACE_WEST) {
			this.setWeb(this.worldObj, dx + 1, dy + 0, dz + 1);
			this.setWeb(this.worldObj, dx + 1, dy + 1, dz + 0);
			this.setWeb(this.worldObj, dx + 1, dy + 1, dz + 2);
			this.setWeb(this.worldObj, dx + 1, dy + 2, dz + 1);
			this.setWeb(this.worldObj, dx + 2, dy + 0, dz + 0);
			this.setWeb(this.worldObj, dx + 2, dy + 0, dz + 2);
			this.setWeb(this.worldObj, dx + 2, dy + 1, dz + 1);
		}
	}

	/** Places a web only where the block is under a canopy (not open sky). */
	void setWeb(World world, int x, int y, int z) {
		if(!world.canBlockSeeTheSkyThruCanopy(x, y, z)) {
			world.setBlockWithNotify(x, y, z, Block.web.blockID);
		}
	}

	void deadEndTreasure(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		if(face == FACE_NORTH) {
			TFTreasure.underhill_deadend.generate(this.worldObj, this.rand, dx + 1, dy + 0, dz + 2);
		} else if(face == FACE_EAST) {
			TFTreasure.underhill_deadend.generate(this.worldObj, this.rand, dx + 0, dy + 0, dz + 1);
		} else if(face == FACE_SOUTH) {
			TFTreasure.underhill_deadend.generate(this.worldObj, this.rand, dx + 1, dy + 0, dz + 0);
		} else if(face == FACE_WEST) {
			TFTreasure.underhill_deadend.generate(this.worldObj, this.rand, dx + 2, dy + 0, dz + 1);
		}
	}

	void deadEndSpawner(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		String mobID = this.rand.nextInt(3) == 0 ? "Skeleton" : "Redcap";
		if(face == FACE_NORTH) {
			this.placeMobSpawner(dx + 1, dy + 0, dz + 2, mobID);
		} else if(face == FACE_EAST) {
			this.placeMobSpawner(dx + 0, dy + 0, dz + 1, mobID);
		} else if(face == FACE_SOUTH) {
			this.placeMobSpawner(dx + 1, dy + 0, dz + 0, mobID);
		} else if(face == FACE_WEST) {
			this.placeMobSpawner(dx + 2, dy + 0, dz + 1, mobID);
		}
	}

	/** Torch-lit picture frame on the far wall of the dead end. */
	void deadEndPainting(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		int artNum = this.rand.nextInt(ART_VALUES.length);
		String artID = ART_VALUES[artNum].title;
		EntityPainting painting = null;
		if(face == FACE_NORTH) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 2, Block.torchWood.blockID);
			painting = new EntityPainting(this.worldObj, dx + 1, dy + 1, dz + 3, 0, artID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 2, Block.torchWood.blockID);
		} else if(face == FACE_EAST) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 0, Block.torchWood.blockID);
			painting = new EntityPainting(this.worldObj, dx - 1, dy + 1, dz + 1, 3, artID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 2, Block.torchWood.blockID);
		} else if(face == FACE_SOUTH) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 0, Block.torchWood.blockID);
			painting = new EntityPainting(this.worldObj, dx + 1, dy + 1, dz - 1, 2, artID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 0, Block.torchWood.blockID);
		} else if(face == FACE_WEST) {
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 0, Block.torchWood.blockID);
			painting = new EntityPainting(this.worldObj, dx + 3, dy + 1, dz + 1, 1, artID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 2, Block.torchWood.blockID);
		}

		if(painting != null && painting.onValidSurface()) {
			if(!this.worldObj.isRemote) {
				this.worldObj.spawnEntityInWorld(painting);
			}
		} else {
			//System.out.println("Painting fail!! " + painting.art.title + " at " + painting.xPosition + " , " + painting.yPosition + ", " + painting.zPosition + " : " + painting.direction);
		}
	}

	/** Stone pressure plate over a TNT charge buried at the dead-end opening. */
	void deadEndTrap(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);

		if(!this.mostFloor(dx, dy - 1, dz)) return;

		this.worldObj.setBlockWithNotify(dx + 1, dy + 0, dz + 1, Block.pressurePlateStone.blockID);
		if(face == FACE_NORTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy - 1, dz + 2, Block.tnt.blockID);
		} else if(face == FACE_EAST) {
			this.worldObj.setBlockWithNotify(dx + 0, dy - 1, dz + 1, Block.tnt.blockID);
		} else if(face == FACE_SOUTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy - 1, dz + 0, Block.tnt.blockID);
		} else if(face == FACE_WEST) {
			this.worldObj.setBlockWithNotify(dx + 2, dy - 1, dz + 1, Block.tnt.blockID);
		}
	}

	void deadEndTrappedChest(int x, int z, int face) {
		this.deadEndTrap(x, z, face);
		this.deadEndTreasure(x, z, face);
	}

	void deadEndTorch(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		if(face == FACE_NORTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 2, Block.torchWood.blockID);
		} else if(face == FACE_EAST) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 1, Block.torchWood.blockID);
		} else if(face == FACE_SOUTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 0, Block.torchWood.blockID);
		} else if(face == FACE_WEST) {
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 1, Block.torchWood.blockID);
		}
	}

	void deadEndTorchRedstone(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		if(face == FACE_NORTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 2, Block.torchRedstoneActive.blockID);
		} else if(face == FACE_EAST) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 1, Block.torchRedstoneActive.blockID);
		} else if(face == FACE_SOUTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 0, Block.torchRedstoneActive.blockID);
		} else if(face == FACE_WEST) {
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 1, Block.torchRedstoneActive.blockID);
		}
	}

	/**
	 * Seals the dead end with a three-block-high stone wall (the "nook" cell).
	 * {@code floorOk} must be the already-computed {@link #mostFloor} result
	 * for the same footprint, so the callers do not scan the 3x3 twice.
	 */
	void deadEndNook(int x, int z, int face, boolean floorOk) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);

		if(!floorOk) return;

		if(face == FACE_NORTH) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 0, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 1, dy + 0, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 0, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 2, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 1, dy + 2, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 2, dz + 2, Block.stone.blockID);
		} else if(face == FACE_EAST) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 0, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 0, dz + 1, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 0, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 2, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 2, dz + 1, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 2, dz + 2, Block.stone.blockID);
		} else if(face == FACE_SOUTH) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 0, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 1, dy + 0, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 0, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 0, dy + 2, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 1, dy + 2, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 2, dz + 0, Block.stone.blockID);
		} else if(face == FACE_WEST) {
			this.worldObj.setBlockWithNotify(dx + 2, dy + 0, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 0, dz + 1, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 0, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 2, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 2, dz + 0, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 2, dz + 1, Block.stone.blockID);
			this.worldObj.setBlockWithNotify(dx + 2, dy + 2, dz + 2, Block.stone.blockID);
		}
	}

	/** Seals the dead end with a nook and fills it with a small fountain. */
	void deadEndFountain(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);

		if(!this.mostFloor(dx, dy - 1, dz)) return;

		this.deadEndNook(x, z, face, true);
		this.worldObj.setBlockWithNotify(dx + 1, dy - 1, dz + 1, 0);
		if(face == FACE_NORTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 2, Block.waterMoving.blockID);
		} else if(face == FACE_EAST) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 1, Block.waterMoving.blockID);
		} else if(face == FACE_SOUTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 0, Block.waterMoving.blockID);
		} else if(face == FACE_WEST) {
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 1, Block.waterMoving.blockID);
		}
	}

	/** Seals the dead end with a nook and fills it with a small lava fountain. */
	void deadEndLavaFountain(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);

		if(!this.mostFloor(dx, dy - 1, dz)) return;

		this.deadEndNook(x, z, face, true);
		this.worldObj.setBlockWithNotify(dx + 1, dy - 1, dz + 1, 0);
		if(face == FACE_NORTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 2, Block.lavaMoving.blockID);
		} else if(face == FACE_EAST) {
			this.worldObj.setBlockWithNotify(dx + 0, dy + 1, dz + 1, Block.lavaMoving.blockID);
		} else if(face == FACE_SOUTH) {
			this.worldObj.setBlockWithNotify(dx + 1, dy + 1, dz + 0, Block.lavaMoving.blockID);
		} else if(face == FACE_WEST) {
			this.worldObj.setBlockWithNotify(dx + 2, dy + 1, dz + 1, Block.lavaMoving.blockID);
		}
	}

	/** Builds a doorway frame (open passage) through the sealed wall. */
	void deadEndDoorway(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		int wallID = this.maze.wallBlockID;
		int wallMeta = this.maze.wallBlockMeta;
		if(face == FACE_NORTH) {
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 0, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 0, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 1, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 1, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 2, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 1, dy + 2, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 2, dz + 0, wallID, wallMeta);
		} else if(face == FACE_EAST) {
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 0, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 0, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 1, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 1, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 2, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 2, dz + 1, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 2, dz + 2, wallID, wallMeta);
		} else if(face == FACE_SOUTH) {
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 0, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 0, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 1, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 1, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 2, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 1, dy + 2, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 2, dy + 2, dz + 2, wallID, wallMeta);
		} else if(face == FACE_WEST) {
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 0, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 0, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 1, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 1, dz + 2, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 2, dz + 0, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 2, dz + 1, wallID, wallMeta);
			this.putBlockAndMetadataIfSolid(dx + 0, dy + 2, dz + 2, wallID, wallMeta);
		}
	}

	/** Places both halves of a door on a solid block below (the top half meta carries +8). */
	void setDoor(int x, int y, int z, int doorId, int baseMeta) {
		if(this.worldObj.isBlockOpaqueCube(x, y - 1, z)) {
			this.worldObj.setBlockAndMetadata(x, y + 0, z, doorId, baseMeta);
			this.worldObj.setBlockAndMetadata(x, y + 1, z, doorId, baseMeta + 8);
		}
	}

	void deadEndDoor(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		this.deadEndDoorway(x, z, face);
		if(face == FACE_NORTH) {
			this.setDoor(dx + 1, dy, dz, Block.doorWood.blockID, 1);
		} else if(face == FACE_EAST) {
			this.setDoor(dx + 2, dy, dz + 1, Block.doorWood.blockID, 2);
		} else if(face == FACE_SOUTH) {
			this.setDoor(dx + 1, dy, dz + 2, Block.doorWood.blockID, 3);
		} else if(face == FACE_WEST) {
			this.setDoor(dx, dy, dz + 1, Block.doorWood.blockID, 0);
		}
	}

	void deadEndDoorSteel(int x, int z, int face) {
		int dx = this.maze.getWorldX(x);
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(z);
		this.deadEndDoorway(x, z, face);
		if(face == FACE_NORTH) {
			this.setDoor(dx + 1, dy, dz, Block.doorSteel.blockID, 1);
		} else if(face == FACE_EAST) {
			this.setDoor(dx + 2, dy, dz + 1, Block.doorSteel.blockID, 2);
		} else if(face == FACE_SOUTH) {
			this.setDoor(dx + 1, dy, dz + 2, Block.doorSteel.blockID, 3);
		} else if(face == FACE_WEST) {
			this.setDoor(dx, dy, dz + 1, Block.doorSteel.blockID, 0);
		}
	}

	void deadEndDoorTreasure(int x, int z, int face) {
		this.deadEndDoor(x, z, face);
		this.deadEndTreasure(x, z, face);
	}

	protected boolean placeMobSpawner(int dx, int dy, int dz, String mobID) {
		if(!this.worldObj.isBlockOpaqueCube(dx, dy - 1, dz)) return false;
		this.worldObj.setBlockWithNotify(dx, dy, dz, Block.mobSpawner.blockID);
		TileEntityMobSpawner spawner = (TileEntityMobSpawner)this.worldObj.getBlockTileEntity(dx, dy, dz);
		if(spawner != null) {
			spawner.setMobID(mobID);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Places a one-shot Minoshroom spawner at the centre of a random cell
	 * that actually has a solid floor carved out. The hill maze is dug into
	 * the floating islands, so most cells end up over the void and have no
	 * floor at all; only floored cells are candidates. Used in sky worlds,
	 * where the minotaur maze (and its Minoshroom boss) never generates.
	 */
	public boolean placeMinoshroomSpawner() {
		int[] validCells = new int[this.maze.cellsWide * this.maze.cellsDeep];
		int count = 0;

		for(int cx = 0; cx < this.maze.cellsWide; cx++) {
			for(int cz = 0; cz < this.maze.cellsDeep; cz++) {
				if(this.maze.getCell(cx, cz) != TFMaze.WALL_RAW) {
					int dx = this.maze.getWorldX(cx) + 1;
					int dz = this.maze.getWorldZ(cz) + 1;
					if(this.worldObj.isBlockOpaqueCube(dx, this.maze.originY - 1, dz)) {
						validCells[count++] = cx * this.maze.cellsDeep + cz;
					}
				}
			}
		}

		if(count == 0) return false;

		int pick = validCells[this.rand.nextInt(count)];
		int dx = this.maze.getWorldX(pick / this.maze.cellsDeep) + 1;
		int dy = this.maze.originY;
		int dz = this.maze.getWorldZ(pick % this.maze.cellsDeep) + 1;

		this.worldObj.setBlockWithNotify(dx, dy, dz, Block.mobSpawnerOneshot.blockID);
		TileEntityMobSpawnerOneshot spawner = (TileEntityMobSpawnerOneshot)this.worldObj.getBlockTileEntity(dx, dy, dz);
		if(spawner != null) {
			spawner.setMobID("Minoshroom");
			return true;
		} else {
			return false;
		}
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
		this.roomSpawner(dx, dy, dz, 11);
		if(!this.roomTreasure(dx, dy, dz, 11) || this.rand.nextInt(2) == 0) {
			this.roomTreasure(dx, dy, dz, 11);
		}

		this.roomSpiderwebs(dx, dy, dz, 11);
	}

	private boolean roomSpawner(int dx, int dy, int dz, int diameter) {
		int rx = this.rand.nextInt(diameter) + dx - diameter / 2;
		int rz = this.rand.nextInt(diameter) + dz - diameter / 2;
		return this.placeMobSpawner(rx, dy, rz, this.rand.nextInt(3) == 0 ? "Minotaur" : "Skeleton");
	}

	private boolean roomTreasure(int dx, int dy, int dz, int diameter) {
		int rx = this.rand.nextInt(diameter) + dx - diameter / 2;
		int rz = this.rand.nextInt(diameter) + dz - diameter / 2;
		return this.worldObj.getBlockID(rx, dy, rz) != 0 ? false : TFTreasure.underhill_room.generate(this.worldObj, this.rand, rx, dy, rz);
	}

	private boolean roomSpiderwebs(int dx, int dy, int dz, int diameter) {
		int rx = dx;
		int rz = dz;
		int corner = this.rand.nextInt(4);
		if(corner == 0) {
			rx = dx - 5;
			rz = dz - 5;
		} else if(corner == 1) {
			rx = dx + 5;
			rz = dz - 5;
		} else if(corner == 2) {
			rx = dx - 5;
			rz = dz + 5;
		} else if(corner == 3) {
			rx = dx + 5;
			rz = dz + 5;
		}

		boolean placedWeb = false;
		placedWeb |= this.roomSpiderweb(rx, dy, rz, 3);
		placedWeb |= this.roomSpiderweb(rx, dy, rz, 3);
		placedWeb |= this.roomSpiderweb(rx, dy, rz, 3);
		return placedWeb;
	}

	private boolean roomSpiderweb(int dx, int dy, int dz, int diameter) {
		int rx = this.rand.nextInt(diameter) + dx - diameter / 2;
		int rz = this.rand.nextInt(diameter) + dz - diameter / 2;
		if(this.worldObj.getBlockID(rx, dy + 2, rz) != 0 || this.worldObj.canBlockSeeTheSky(rx, dy + 2, rz)) {
			return false;
		} else {
			this.worldObj.setBlockWithNotify(rx, dy + 2, rz, Block.web.blockID);
			return true;
		}
	}
}
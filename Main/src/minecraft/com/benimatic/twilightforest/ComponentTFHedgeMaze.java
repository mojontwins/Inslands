package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFHedgeMaze extends StructureTFComponent {
	static int MSIZE = 16;
	static int RADIUS = MSIZE / 2 * 3 + 1;
	static int DIAMETER = 2 * RADIUS;
	static int FLOOR_LEVEL = 3;

	public ComponentTFHedgeMaze(World world, Random rand, int i, int x, int y, int z) {
		super(i);
		this.setCoordBaseMode(0);
		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(x, y, z, -RADIUS, -3, -RADIUS, RADIUS * 2, 10, RADIUS * 2, 0);
	}

	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb, boolean notUsed) {
		this.fillWithBlocks(world, sbb, 1, 1, 1, DIAMETER-2, 3, DIAMETER-2, 0, 0, true);
		
		TFMazeNew maze = new TFMazeNew(MSIZE, MSIZE);
		maze.oddBias = 2;
		maze.torchBlockID = Block.torchWood.blockID;
		maze.wallBlockID = Block.hedge.blockID;
		maze.wallBlockMeta = 0;
		maze.type = 4;
		maze.tall = 3;
		maze.roots = 3;
		maze.setSeed(world.getRandomSeed() + (long)(this.boundingBox.minX * this.boundingBox.minZ));

		int nrooms;
		for(nrooms = 0; nrooms <= DIAMETER; ++nrooms) {
			for(int rcoords = 0; rcoords <= DIAMETER; ++rcoords) {
				this.placeBlockAtCurrentPosition(world, Block.grass.blockID, 0, nrooms, FLOOR_LEVEL - 1, rcoords, sbb);
			}
		}

		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 1, 0, FLOOR_LEVEL, 24, sbb);
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 1, 0, FLOOR_LEVEL, 29, sbb);
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 3, 50, FLOOR_LEVEL, 24, sbb);
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 3, 50, FLOOR_LEVEL, 29, sbb);
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 2, 24, FLOOR_LEVEL, 0, sbb);
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 2, 29, FLOOR_LEVEL, 0, sbb);
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 0, 24, FLOOR_LEVEL, 50, sbb);
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, 0, 29, FLOOR_LEVEL, 50, sbb);
		nrooms = MSIZE / 3;
		int[] i10 = new int[nrooms * 2];

		for(int i = 0; i < nrooms; ++i) {
			int rx;
			int rz;
			do {
				rx = maze.rand.nextInt(MSIZE - 2) + 1;
				rz = maze.rand.nextInt(MSIZE - 2) + 1;
			} while(this.isNearRoom(rx, rz, i10));

			maze.carveRoom1(rx, rz);
			i10[i * 2] = rx;
			i10[i * 2 + 1] = rz;
		}

		maze.generateRecursiveBacktracker(0, 0);
		maze.add4Exits();
		maze.copyToStructure(world, rand, 1, FLOOR_LEVEL, 1, this, sbb);
		this.decorate3x3Rooms(world, i10, sbb);
		return true;
	}

	protected boolean isNearRoom(int dx, int dz, int[] rcoords) {
		if(dx == 1 && dz == 1) {
			return true;
		} else {
			for(int i = 0; i < rcoords.length / 2; ++i) {
				int rx = rcoords[i * 2];
				int rz = rcoords[i * 2 + 1];
				if((rx != 0 || rz != 0) && Math.abs(dx - rx) < 3 && Math.abs(dz - rz) < 3) {
					return true;
				}
			}

			return false;
		}
	}

	void decorate3x3Rooms(World world, int[] rcoords, StructureBoundingBox sbb) {
		for(int i = 0; i < rcoords.length / 2; ++i) {
			int dx = rcoords[i * 2];
			int dz = rcoords[i * 2 + 1];
			dx = dx * 3 + 3;
			dz = dz * 3 + 3;
			this.decorate3x3Room(world, dx, dz, sbb);
		}

	}

	void decorate3x3Room(World world, int x, int z, StructureBoundingBox sbb) {
		Random roomRNG = new Random(world.getRandomSeed() ^ (long)(x + z));
		this.roomJackO(world, roomRNG, x, z, 8, sbb);
		if(roomRNG.nextInt(4) == 0) {
			this.roomJackO(world, roomRNG, x, z, 8, sbb);
		}

		this.roomSpawner(world, roomRNG, x, z, 8, sbb);
		this.roomTreasure(world, roomRNG, x, z, 8, sbb);
		if(roomRNG.nextInt(4) == 0) {
			this.roomTreasure(world, roomRNG, x, z, 8, sbb);
		}

	}

	private void roomSpawner(World world, Random rand, int x, int z, int diameter, StructureBoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - diameter / 2;
		int rz = z + rand.nextInt(diameter) - diameter / 2;
		String mobID;
		switch(rand.nextInt(3)) {
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

		this.placeSpawnerAtCurrentPosition(world, rand, rx, FLOOR_LEVEL, rz, mobID, sbb);
	}

	private void roomTreasure(World world, Random rand, int x, int z, int diameter, StructureBoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - diameter / 2;
		int rz = z + rand.nextInt(diameter) - diameter / 2;
		this.placeTreasureAtCurrentPosition(world, rand, rx, FLOOR_LEVEL, rz, TFTreasure.hedgemaze, sbb);
	}

	private void roomJackO(World world, Random rand, int x, int z, int diameter, StructureBoundingBox sbb) {
		int rx = x + rand.nextInt(diameter) - diameter / 2;
		int rz = z + rand.nextInt(diameter) - diameter / 2;
		this.placeBlockAtCurrentPosition(world, Block.pumpkinLantern.blockID, rand.nextInt(4), rx, FLOOR_LEVEL, rz, sbb);
	}
}

package com.benimatic.twilightforest;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.GlobalVars;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;
import net.minecraft.src.WorldSize;

public class TFGenHedgeMaze extends TFGenerator {
	int size;
	TFMaze maze;
	Random rand;

	public TFGenHedgeMaze(int size) {
		this.size = size;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(y < 64) {
			System.out.println ("Attempted hedge maze but too low! @" + y);
			return false;
		}
		
		this.worldObj = world;
		this.rand = rand;
		int sx = x - 7 - this.size * 16;
		int sz = z - 7 - this.size * 16;
		byte msize = 16;
		this.maze = new TFMaze(msize, msize);
		this.maze.oddBias = 2;
		this.maze.torchBlockID = Block.torchWood.blockID;
		this.maze.wallBlockID = Block.hedge.blockID;
		this.maze.type = 4;
		this.maze.tall = 3;
		this.maze.roots = 3;

		// Also check that no more than worldsize / 256 * 2 mazes generate.
		if(GlobalVars.numHedgeMazes > 1 + WorldSize.xChunks * WorldSize.zChunks / 128) {
			System.out.println ("Attempted hedge maze but there are too many (" + GlobalVars.numHedgeMazes + ")");
			return false;
		}
		
		// Check if most that area is uncovered? Canopy shouldn't count.
		int total = 0; 
		int count = 0;
		for(int xx = sx; xx < sx + msize*3; xx += 4) {
			for(int zz = sz; zz < sz + msize*3; zz += 4) {
				if(world.canBlockSeeTheSkyThruCanopy(xx, y + 2, zz)) count ++;
				total ++;
			}
		}
		if(count < (int)((float)total * .2)) {
			System.out.println ("Attempted hedge maze from " + sx + " " + sz + " but more than 80% would be covered.");
			return false;
		}
		
		this.fill(sx, y - 1, sz, msize * 3, 1, msize * 3, Block.grass.blockID, 0);
		this.fill(sx, y, sz, msize * 3, 3, msize * 3, 0, 0);
		

		
		this.putBlockAndMetadata(sx - 1, y, sz + 23, Block.pumpkinLantern.blockID, 1, true);
		this.putBlockAndMetadata(sx - 1, y, sz + 28, Block.pumpkinLantern.blockID, 1, true);
		this.putBlockAndMetadata(sx + 49, y, sz + 23, Block.pumpkinLantern.blockID, 3, true);
		this.putBlockAndMetadata(sx + 49, y, sz + 28, Block.pumpkinLantern.blockID, 3, true);
		this.putBlockAndMetadata(sx + 23, y, sz - 1, Block.pumpkinLantern.blockID, 2, true);
		this.putBlockAndMetadata(sx + 28, y, sz - 1, Block.pumpkinLantern.blockID, 2, true);
		this.putBlockAndMetadata(sx + 23, y, sz + 49, Block.pumpkinLantern.blockID, 0, true);
		this.putBlockAndMetadata(sx + 28, y, sz + 49, Block.pumpkinLantern.blockID, 0, true);
		int nrooms = msize / 3;
		int[] rcoords = new int[nrooms * 2];

		for(int i = 0; i < nrooms; ++i) {
			int rx;
			int rz;
			do {
				rx = rand.nextInt(msize - 2) + 1;
				rz = rand.nextInt(msize - 2) + 1;
			} while(this.isNearRoom(rx, rz, rcoords));

			this.maze.carveRoom1(rx, rz);
			rcoords[i * 2] = rx;
			rcoords[i * 2 + 1] = rz;
		}

		this.maze.generateRecursiveBacktracker(0, 0);
		this.maze.add4Exits();
		this.maze.copyToWorld(this.worldObj, sx, y, sz);
		this.decorate3x3Rooms(rcoords);
		
		System.out.println("Hedge maze @ " + (sx + this.size*8) + ", " + (sz + this.size*8));
		return true;
	}

	protected boolean isNearRoom(int dx, int dz, int[] rcoords) {
		for(int i = 0; i < rcoords.length / 2; ++i) {
			int rx = rcoords[i * 2];
			int rz = rcoords[i * 2 + 1];
			if((rx != 0 || rz != 0) && Math.abs(dx - rx) < 3 && Math.abs(dz - rz) < 3) {
				return true;
			}
		}

		return false;
	}

	void decorate3x3Rooms(int[] rcoords) {
		for(int i = 0; i < rcoords.length / 2; ++i) {
			int dx = rcoords[i * 2];
			int dz = rcoords[i * 2 + 1];
			this.decorate3x3Room(dx, dz);
		}

	}

	void decorate3x3Room(int x, int z) {
		int dx = this.maze.getWorldX(x) + 1;
		int dy = this.maze.worldY;
		int dz = this.maze.getWorldZ(z) + 1;
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
		return this.worldObj.getBlockId(rx, dy, rz) != 0 ? false : TFTreasure.hedgemaze.generate(this.worldObj, this.rand, rx, dy, rz);
	}

	protected boolean placeMobSpawner(int dx, int dy, int dz, String mobID) {
		this.worldObj.setBlockWithNotify(dx, dy, dz, Block.mobSpawner.blockID);
		TileEntityMobSpawner ms = (TileEntityMobSpawner)this.worldObj.getBlockTileEntity(dx, dy, dz);
		if(ms != null) {
			ms.setMobID(mobID);
			return true;
		} else {
			return false;
		}
	}

	private boolean roomJackO(int dx, int dy, int dz, int diameter) {
		int rx = this.rand.nextInt(diameter) + dx - diameter / 2;
		int rz = this.rand.nextInt(diameter) + dz - diameter / 2;
		if(this.worldObj.getBlockId(rx, dy, rz) != 0) {
			return false;
		} else {
			this.worldObj.setBlockAndMetadataWithNotify(rx, dy, rz, Block.pumpkinLantern.blockID, this.rand.nextInt(4));
			return true;
		}
	}
}

package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.TFMaze;
import net.minecraft.world.level.levelgen.TFTreasure;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockBigMushroom;
import net.minecraft.world.level.tile.BlockLeaves;
import net.minecraft.world.level.tile.BlockLog;
import net.minecraft.world.level.tile.entity.TileEntityMobSpawner;

public class TFGenHedgeMaze extends TFGenerator {
	int size;
	int minY;
	TFMaze maze;
	Random rand;
	
	boolean debug = true;

	public TFGenHedgeMaze(int size) {
		this(size, 64);
	}

	public TFGenHedgeMaze(int size, int minY) {
		this.size = size;
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
		
		// Original maze structure size is 16x16 cells. oaddbias in TFMaze
		// is modified here so cells are 3x3 blocks. This would make
		// rendered mazes 16*3+1x16*3+1 cells big.
		
		// Original code also placed the mazes 7+48 blocks in the negative
		// direction of X and Z. I believe this was done in the hopes that
		// those chunks were generated already and overflows were minimised?
		
		// In this version, this.size controls the amount of cells, and (x, z)
		// is the center of the maze.
		
		int msize = this.size;
		this.maze = new TFMaze(msize, msize);
		this.maze.oddBias = 2;
		
		// We are centering the maze in x, z so,
		
		int cellSize = this.maze.oddBias + this.maze.evenBias;
		int sizeBlocks = this.size * cellSize + 1;
		int sx = x - sizeBlocks / 2;
		int sz = z - sizeBlocks / 2;
		
		// To decide if we are generating the maze here, let's calculate
		// the min and max land surface height in the area. Note that we'll
		// be leaving a border of 3 blocks around the maze.
		
		int minH = 128;
		int maxH = 0;
		for(int xx = sx - 3; xx < sx + sizeBlocks + 3; xx ++) {
			for(int zz = sz - 3; zz < sz + sizeBlocks + 3; zz ++) {
				int h = world.getLandSurfaceHeightValue(xx, zz);
				if(h < this.minY) return false;
				if(h < minH) minH = h;
				if(h > maxH) maxH = h;
				
			}
		}
		
		if(debug) System.out.println("Attempting hedge @ " + x + " " + z + " (" + sizeBlocks + ")");
		if(debug) System.out.println("minH " + minH + ", maxH " + maxH); 
		
		// Too much land variance.
		if(maxH - minH > 16) return false;
		
		// Sink maze
		int raise = (maxH - minH - 3) / 2;
		if(raise > 4) raise = 4;
		
		y = minH + raise;
		if(y < 64) y = 64;
		
		if(debug) System.out.println("Succeeded @ " + x + " " + y + " " + z);
		
		// At this point, we can seay that the maze gen has succeeded
		
		this.maze.torchblockID = Block.torchWood.blockID;
		this.maze.wallblockID = Block.hedge.blockID;
		this.maze.type = 4;
		this.maze.tall = 3;
		this.maze.roots = 3;

		// Carve
		
		for(int i = 1; i < 4; i ++) {
			for(int xx = sx - i; xx < sx + sizeBlocks + i; xx ++) {
				for(int zz = sz - i; zz < sz + sizeBlocks + i; zz ++) {
					world.setBlock(xx, y + i - 1, zz, 0);
					
				}
			}
		}
		
		// Carve up to delete everything but trunks, leaves and mushrooms
		for(int i = y + 3; i <= maxH; i ++) {
			for(int xx = sx - 3; xx < sx + sizeBlocks + 3; xx ++) {
				for(int zz = sz - 3; zz < sz + sizeBlocks + 3; zz ++) {
					Block b = world.getBlock(xx, i, zz);
					if(!(
							b instanceof BlockLog || 
							b instanceof BlockLeaves || 
							b instanceof BlockBigMushroom
					)) {
						world.setBlock(xx, i, zz, 0);
					}
				}
			}
		}

		// Grass ground
		
		this.fill(sx, y - 1, sz, msize * 3, 1, msize * 3, Block.grass.blockID, 0);
	
		// Decorations
		
		this.putBlockAndMetadata(sx - 1, y, sz + 23, Block.pumpkinLantern.blockID, 1, true);
		this.putBlockAndMetadata(sx - 1, y, sz + 28, Block.pumpkinLantern.blockID, 1, true);
		this.putBlockAndMetadata(sx + sizeBlocks, y, sz + 23, Block.pumpkinLantern.blockID, 3, true);
		this.putBlockAndMetadata(sx + sizeBlocks, y, sz + 28, Block.pumpkinLantern.blockID, 3, true);
		this.putBlockAndMetadata(sx + 23, y, sz - 1, Block.pumpkinLantern.blockID, 2, true);
		this.putBlockAndMetadata(sx + 28, y, sz - 1, Block.pumpkinLantern.blockID, 2, true);
		this.putBlockAndMetadata(sx + 23, y, sz + sizeBlocks, Block.pumpkinLantern.blockID, 0, true);
		this.putBlockAndMetadata(sx + 28, y, sz + sizeBlocks, Block.pumpkinLantern.blockID, 0, true);
		
		// Main rooms
		
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
		
		// Calculate maze structure

		this.maze.generateRecursiveBacktracker(0, 0);
		this.maze.add4Exits();
		
		// Draw maze
		
		this.maze.copyToWorld(this.worldObj, sx, y, sz);
		this.decorate3x3Rooms(rcoords);
		
		// Now, the layer @ y + 3 may contain partial trees.
		// Hopefully most will have a trunk, otherwise we 
		// can only leave the leaves to naturally decay.
		
		for(int xx = sx - 3; xx < sx + sizeBlocks + 3; xx ++) {
			for(int zz = sz - 3; zz < sz + sizeBlocks + 3; zz ++) {
				BlockState bs = world.getBlockStateAt(xx, y + 4, zz);
				Block block = bs.getBlock();
				if(
						block instanceof BlockLog ||
						(block instanceof BlockBigMushroom && bs.getMetadata() == 15)
				) {
					int yy = y + 3;
					while(world.isAirBlock(xx, yy, zz)) {
						world.setBlockState(xx, yy, zz, bs);
						yy --;
						
					}
				}
			}
		}
		
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
		return this.worldObj.getBlockID(rx, dy, rz) != 0 ? false : TFTreasure.hedgemaze.generate(this.worldObj, this.rand, rx, dy, rz);
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
		if(this.worldObj.getBlockID(rx, dy, rz) != 0) {
			return false;
		} else {
			this.worldObj.setBlockAndMetadataWithNotify(rx, dy, rz, Block.pumpkinLantern.blockID, this.rand.nextInt(4));
			return true;
		}
	}
}

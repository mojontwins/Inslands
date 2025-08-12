package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.feature.TFGenerator;

public class TFGenCanopyMushroom extends TFGenerator {
	private Random treeRNG;
	private int x;
	private int y;
	private int z;
	private int treeHeight;
	private int treeBlock;
	private int treeMeta = 10;
	private int branchMeta = 15;
	private int leafBlock;
	//private int leafMeta = 5;

	public boolean generate(World world, Random random, int treeX, int treeY, int treeZ) {
		this.worldObj = world;
		this.treeRNG = random;
		int blockUnder = world.getBlockId(treeX, treeY - 1, treeZ);
		if((blockUnder == Block.grass.blockID || blockUnder == Block.dirt.blockID || blockUnder == Block.mycelium.blockID) && this.y < 128 - this.treeHeight - 1) {
			this.x = treeX;
			this.y = treeY;
			this.z = treeZ;
			
			if(this.treeRNG.nextInt(25) == 0) {
				this.treeBlock = Block.mushroomCapGreen.blockID;
			} else {
				this.treeBlock = this.treeRNG.nextInt(3) == 0 ? Block.mushroomCapRed.blockID : Block.mushroomCapBrown.blockID;
			}
			
			this.leafBlock = this.treeBlock;
			this.treeHeight = 12;
			if(this.treeRNG.nextInt(3) == 0) {
				this.treeHeight += this.treeRNG.nextInt(5);
				if(this.treeRNG.nextInt(8) == 0) {
					this.treeHeight += this.treeRNG.nextInt(5);
				}
			}

			this.buildBranch(0, (double)this.treeHeight, 0.0D, 0.0D, true);
			int numBranches = 3 + this.treeRNG.nextInt(2);
			double offset = this.treeRNG.nextDouble();

			for(int b = 0; b < numBranches; ++b) {
				this.buildBranch(this.treeHeight - 5 + b, 9.0D, 0.3D * (double)b + offset, 0.2D, false);
			}

			return true;
		} else {
			return false;
		}
	}

	void buildBranch(int height, double length, double angle, double tilt, boolean firefly) {
		int[] src = new int[]{this.x, this.y + height, this.z};
		int[] dest = translate(src[0], src[1], src[2], length, angle, tilt);
		if(src[0] != dest[0] && src[2] != dest[2]) {
			this.drawBresehnam(src[0], src[1], src[2], dest[0], src[1], dest[2], this.treeBlock, this.branchMeta, true);
			this.drawBresehnam(dest[0], src[1] + 1, dest[2], dest[0], dest[1] - 1, dest[2], this.treeBlock, this.treeMeta, true);
		} else {
			this.drawBresehnam(src[0], src[1], src[2], dest[0], dest[1] - 1, dest[2], this.treeBlock, this.treeMeta, true);
		}

		if(firefly) {
			this.addFirefly(3 + this.treeRNG.nextInt(7), this.treeRNG.nextDouble());
		}

		this.drawMushroomCircle(dest[0], dest[1], dest[2], 4, this.leafBlock, true);
	}

	public void drawMushroomCircle(int sx, int sy, int sz, int rad, int blockValue, boolean priority) {
		for(byte dx = 0; dx <= rad; ++dx) {
			for(byte dz = 0; dz <= rad; ++dz) {
				int dist = (int)((double)Math.max(dx, dz) + (double)Math.min(dx, dz) * 0.5D);
				if(dx == 3 && dz == 3) {
					dist = 6;
				}

				if(dx == 0) {
					if(dz < rad) {
						this.putBlockAndMetadata(sx + 0, sy, sz + dz, blockValue, 5, priority);
						this.putBlockAndMetadata(sx + 0, sy, sz - dz, blockValue, 5, priority);
					} else {
						this.putBlockAndMetadata(sx + 0, sy, sz + dz, blockValue, 8, priority);
						this.putBlockAndMetadata(sx + 0, sy, sz - dz, blockValue, 2, priority);
					}
				} else if(dz == 0) {
					if(dx < rad) {
						this.putBlockAndMetadata(sx + dx, sy, sz + 0, blockValue, 5, priority);
						this.putBlockAndMetadata(sx - dx, sy, sz + 0, blockValue, 5, priority);
					} else {
						this.putBlockAndMetadata(sx + dx, sy, sz + 0, blockValue, 6, priority);
						this.putBlockAndMetadata(sx - dx, sy, sz + 0, blockValue, 4, priority);
					}
				} else if(dist < rad) {
					this.putBlockAndMetadata(sx + dx, sy, sz + dz, blockValue, 5, priority);
					this.putBlockAndMetadata(sx + dx, sy, sz - dz, blockValue, 5, priority);
					this.putBlockAndMetadata(sx - dx, sy, sz + dz, blockValue, 5, priority);
					this.putBlockAndMetadata(sx - dx, sy, sz - dz, blockValue, 5, priority);
				} else if(dist == rad) {
					this.putBlockAndMetadata(sx + dx, sy, sz + dz, blockValue, 9, priority);
					this.putBlockAndMetadata(sx + dx, sy, sz - dz, blockValue, 3, priority);
					this.putBlockAndMetadata(sx - dx, sy, sz + dz, blockValue, 7, priority);
					this.putBlockAndMetadata(sx - dx, sy, sz - dz, blockValue, 1, priority);
				}
			}
		}

	}

	private void addFirefly(int height, double angle) {
		int iAngle = (int)(angle * 4.0D);
		if(iAngle == 0) {
			this.putBlockAndMetadata(this.x + 1, this.y + height, this.z, Block.torchWood.blockID, 0, false);
		} else if(iAngle == 1) {
			this.putBlockAndMetadata(this.x - 1, this.y + height, this.z, Block.torchWood.blockID, 0, false);
		} else if(iAngle == 2) {
			this.putBlockAndMetadata(this.x, this.y + height, this.z + 1, Block.torchWood.blockID, 0, false);
		} else if(iAngle == 3) {
			this.putBlockAndMetadata(this.x, this.y + height, this.z - 1, Block.torchWood.blockID, 0, false);
		}

	}
}

package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.material.Material;

public class TFGenMangroveTree extends TFGenerator {
	private Random treeRNG;
	private int x;
	private int y;
	private int z;
	private int height;
	private byte treeBlock;
	private byte treeMeta;
	private byte leafBlock;
	private byte leafMeta;

	public boolean generate(World world, Random random, int treeX, int treeY, int treeZ) {
		this.worldObj = world;
		this.treeRNG = random;
		this.x = treeX;
		this.y = treeY;
		this.z = treeZ;
		this.treeBlock = (byte)Block.wood.blockID;
		this.treeMeta = 0;
		this.leafBlock = (byte)Block.leaves.blockID;
		this.leafMeta = 2;
		int blockUnder = world.getBlockId(this.x, this.y - 1, this.z);
		int blockOn = world.getBlockId(this.x, this.y, this.z);
		if(
			(blockUnder == Block.waterStill.blockID || blockOn == Block.waterStill.blockID || 
			blockUnder == Block.waterMoving.blockID || blockOn == Block.waterMoving.blockID) 
			&& this.y < 128 - this.height - 1) {
			this.buildBranch(5, (double)(6 + this.treeRNG.nextInt(3)), 0.0D, 0.0D);
			int numBranches = this.treeRNG.nextInt(3);
			double offset = this.treeRNG.nextDouble();

			int numRoots;
			for(numRoots = 0; numRoots < numBranches; ++numRoots) {
				this.buildBranch(7 + numRoots, (double)(6 + this.treeRNG.nextInt(2)), 0.3D * (double)numRoots + offset, 0.25D);
			}

			numRoots = 3 + this.treeRNG.nextInt(2);
			offset = this.treeRNG.nextDouble();

			for(int i = 0; i < numRoots; ++i) {
				double rTilt = 0.75D + this.treeRNG.nextDouble() * 0.1D;
				this.buildRoot(5, 8.0D, 0.4D * (double)i + offset, rTilt);
			}

			this.addFirefly(5 + this.treeRNG.nextInt(5), this.treeRNG.nextDouble());
			return true;
		} else {
			return false;
		}
	}

	void buildBranch(int height, double length, double angle, double tilt) {
		int[] src = new int[]{this.x, this.y + height, this.z};
		int[] dest = this.translate(src[0], src[1], src[2], length, angle, tilt);
		this.drawBresehnam(src[0], src[1], src[2], dest[0], dest[1], dest[2], this.treeBlock, this.treeMeta, true);
		this.putBlockAndMetadata(dest[0] + 1, dest[1], dest[2], this.treeBlock, this.treeMeta, true);
		this.putBlockAndMetadata(dest[0] - 1, dest[1], dest[2], this.treeBlock, this.treeMeta, true);
		this.putBlockAndMetadata(dest[0], dest[1], dest[2] + 1, this.treeBlock, this.treeMeta, true);
		this.putBlockAndMetadata(dest[0], dest[1], dest[2] - 1, this.treeBlock, this.treeMeta, true);
		int bSize = 2 + this.treeRNG.nextInt(3);
		this.drawCircle(dest[0], dest[1] - 1, dest[2], (byte)(bSize - 1), this.leafBlock, this.leafMeta, false);
		this.drawCircle(dest[0], dest[1], dest[2], (byte)bSize, this.leafBlock, this.leafMeta, false);
		this.drawCircle(dest[0], dest[1] + 1, dest[2], (byte)(bSize - 2), this.leafBlock, this.leafMeta, false);
	}

	void buildRoot(int height, double length, double angle, double tilt) {
		int[] src = new int[]{this.x, this.y + height, this.z};
		int[] dest = this.translate(src[0], src[1], src[2], length, angle, tilt);
		this.drawBresehnam(src[0], src[1], src[2], dest[0], dest[1], dest[2], this.treeBlock, this.treeMeta, true);
		
		// expand to ground
		int x = dest[0]; 
		int y = dest[1];
		int z = dest[2];
		while(y > 8) {
			Block block = Block.blocksList[this.worldObj.getBlockId(x, y - 1, z)];
			if(block != null && block.blockMaterial != Material.water) break;
			y --;
			this.putBlockAndMetadata(x, y, z, this.treeBlock, this.treeMeta, true);
		}
	}

	private void addFirefly(int height, double angle) {
		/*
		int iAngle = (int)(angle * 4.0D);
		if(iAngle == 0) {
			this.putBlockAndMetadata(this.x + 1, this.y + height, this.z, Block.torch.blockID, 1, false);
		} else if(iAngle == 1) {
			this.putBlockAndMetadata(this.x - 1, this.y + height, this.z, Block.torch.blockID, 2, false);
		} else if(iAngle == 2) {
			this.putBlockAndMetadata(this.x, this.y + height, this.z + 1, Block.torch.blockID, 3, false);
		} else if(iAngle == 3) {
			this.putBlockAndMetadata(this.x, this.y + height, this.z - 1, Block.torch.blockID, 4, false);
		}
		*/
	}
}

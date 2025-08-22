package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.TFGenerator;
import net.minecraft.world.level.tile.Block;

public class TFGenCanopyTree extends TFGenerator {
	private Random treeRNG;
	private int x;
	private int y;
	private int z;
	private int height;
	
	EnumTreeType tree = EnumTreeType.CANOPY;
	
	private final int leavesID = tree.leaves.getBlock().blockID;
	private final int leavesMeta = tree.leaves.getMetadata();
	private final int trunkID = tree.wood.getBlock().blockID;
	private final int trunkMeta = tree.wood.getMetadata();
	
	public boolean generate(World world, Random random, int treeX, int treeY, int treeZ) {
		this.worldObj = world;
		this.treeRNG = random;
		this.x = treeX;
		this.y = treeY;
		this.z = treeZ;

		int j1 = world.getBlockId(this.x, this.y - 1, this.z);
		if((j1 == Block.grass.blockID || j1 == Block.dirt.blockID) && this.y < 128 - this.height - 1) {
			this.buildBranch(0, 20.0D, 0.0D, 0.0D);
			int numBranches = 3 + this.treeRNG.nextInt(2);
			double offset = this.treeRNG.nextDouble();

			for(int b = 0; b < numBranches; ++b) {
				this.buildBranch(10 + b, 9.0D, 0.3D * (double)b + offset, 0.2D);
			}

			this.addFirefly(3 + this.treeRNG.nextInt(7), this.treeRNG.nextDouble());
			return true;
		} else {
			return false;
		}
	}

	void buildBranch(int height, double length, double angle, double tilt) {
		int[] src = new int[]{this.x, this.y + height, this.z};
		int[] dest = this.translate(src[0], src[1], src[2], length, angle, tilt);
		this.drawBresehnam(src[0], src[1], src[2], dest[0], dest[1], dest[2], this.trunkID, this.trunkMeta, true);
		this.putBlockAndMetadata(dest[0] + 1, dest[1], dest[2], this.trunkID, this.trunkMeta, true);
		this.putBlockAndMetadata(dest[0] - 1, dest[1], dest[2], this.trunkID, this.trunkMeta, true);
		this.putBlockAndMetadata(dest[0], dest[1], dest[2] + 1, this.trunkID, this.trunkMeta, true);
		this.putBlockAndMetadata(dest[0], dest[1], dest[2] - 1, this.trunkID, this.trunkMeta, true);
		this.drawCircle(dest[0], dest[1] - 1, dest[2], (byte)3, (byte) this.leavesID, this.leavesMeta, false);
		this.drawCircle(dest[0], dest[1], dest[2], (byte)4, (byte) this.leavesID, this.leavesMeta, false);
		this.drawCircle(dest[0], dest[1] + 1, dest[2], (byte)2, (byte) this.leavesID, this.leavesMeta, false);
	}

	private void addFirefly(int height, double angle) {
		int iAngle = (int)(angle * 4.0D);
		if(iAngle == 0) {
			this.putBlock(this.x + 1, this.y + height, this.z, Block.torchWood.blockID, false);
		} else if(iAngle == 1) {
			this.putBlock(this.x - 1, this.y + height, this.z, Block.torchWood.blockID, false);
		} else if(iAngle == 2) {
			this.putBlock(this.x, this.y + height, this.z + 1, Block.torchWood.blockID, false);
		} else if(iAngle == 3) {
			this.putBlock(this.x, this.y + height, this.z - 1, Block.torchWood.blockID, false);
		}

	}
}

package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.World;

public class WorldGenCypress extends WorldGenMojon {

	EnumTreeType tree = EnumTreeType.CYPRESS;
	
	private final int leavesID = tree.leaves.getBlock().blockID;
	private final int leavesMeta = tree.leaves.getMetadata();
	private final int trunkID = tree.wood.getBlock().blockID;
	private final int trunkMeta = tree.wood.getMetadata();
	
	public int height = 6;
	
	public WorldGenCypress (int height) {
		this(height, false);
	}

	public WorldGenCypress(int height, boolean withNotify) {
		super(withNotify);
		this.height = height;
	}

	@Override
	public boolean generate(World world, Random rand, int x0, int y0, int z0) {
		// May this tree grow here?
		
		if(y0 + this.height > world.getWorldHeight() - 2) return false;
		if(!this.validGround(world, x0, y0 - 1, z0)) return false;
		
		for(int y = 0; y < this.height * 2; y ++) {
			for(int x = -2; x < 3; x ++) {
				for(int z = -2; z < 3; z ++) {
					if(world.isBlockOpaqueCube(x + x0, y + y0, z + z0)) return false;
				}
			}
		}
		
		// Trunk
	
		for(int y = y0; y < y0 + 3 + (this.height << 1); y ++) {
			this.setBlockWithMetadata(world, x0, y, z0, trunkID, trunkMeta);
		}
		
		// Canopy
		
		int y = y0 + 3;
		for(int i = 0; i < this.height; i ++) {
			this.roundedSquareTreeLayer(world, rand, x0, y ++, z0, 5, leavesID, leavesMeta);
			this.roundedSquareTreeLayer(world, rand, x0, y ++, z0, 3, leavesID, leavesMeta);
		}
		
		return true;
	}
	
	
}

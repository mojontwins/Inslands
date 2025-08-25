package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.Direction;
import net.minecraft.world.level.BlockPos;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class WorldGenPalmTree extends WorldGenMojon {

	EnumTreeType tree = EnumTreeType.PALM;
	
	private final int leavesID = tree.leaves.getBlock().blockID;
	private final int leavesMeta = tree.leaves.getMetadata();
	private final int trunkID = tree.wood.getBlock().blockID;
	private final int trunkMeta = tree.wood.getMetadata();
	
	public WorldGenPalmTree(boolean withNotify) {
		super(withNotify);
	}
	
	@Override
	public boolean validGround(World world, int x, int y, int z) {
		return world.getblockID(x, y, z) == Block.sand.blockID;
	}

	@Override
	public boolean generate(World world, Random rand, int x0, int y0, int z0) {
		// Can a palm tree grow here?
		
		if(!this.validGround(world, x0, y0 - 1, z0)) {
			return false;
		}
		
		for(int y = y0 + 8; y < y0 + 10; y ++) {
			for(int x = x0 - 2; x <= x0 + 2; x ++) {
				for(int z = z0 - 2; z <= z0 + 2; z ++) {
					if(world.isBlockOpaqueCube(x, y, z)) return false;
				}
			}
		}
		
		// Two variations
		
		double arch;
		int height;
		
		if(rand.nextBoolean()) {
			arch = .1D + rand.nextDouble() * .35D;
			height = 10;
		} else {
			arch = .05D + rand.nextDouble() * .25D;
			height = 8;
		}
		
		BlockPos cursor = new BlockPos().set(x0, y0 - 1, z0);
		this.setBlockWithMetadata(world, cursor, Block.dirt.blockID, 0);
		
		int direction = Direction.HORZ_PLANE[rand.nextInt(Direction.HORZ_PLANE.length)];
				
		for(int y = 0 ; y < height; y ++) {
			cursor.x = x0; cursor.z = z0;
			cursor.move(direction, (int)Math.floor(arch));
			this.setBlockWithMetadata(world, cursor.move(Direction.UP), trunkID, trunkMeta);
			arch *= 1.3D;
		}
		
		this.edgesPlusShape(world, cursor.move(Direction.DOWN), 5, leavesID, leavesMeta); 	// Y -1
		this.edgesPlusShape(world, cursor.move(Direction.UP), 5, leavesID, leavesMeta); 	// Y + 2
		
		this.edgesSquareShape(world, cursor.move(Direction.UP), 5, leavesID, leavesMeta);	// Y
		this.edgesPlusShape(world, cursor, 3, leavesID, leavesMeta);
		
		this.setBlockIfEmpty(world, cursor.move(Direction.UP), leavesID, leavesMeta); 		// Y + 1
		this.edgesSquareShape(world, cursor, 3, leavesID, leavesMeta);
		
		
		
		return false;
	}

}

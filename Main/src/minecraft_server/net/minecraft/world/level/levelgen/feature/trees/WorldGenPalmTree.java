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
		return world.getBlockID(x, y, z) == Block.sand.blockID;
	}

	@Override
	public boolean generate(World world, Random rand, int x0, int y0, int z0) {
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
		
		double arch;
		int height;
		
		if(rand.nextBoolean()) {
			arch = .1D + rand.nextDouble() * .35D;
			height = 10;
		} else {
			arch = .05D + rand.nextDouble() * .25D;
			height = 8;
		}
		
		this.setBlockAndMetadata(world, x0, y0 - 1, z0, Block.dirt.blockID, 0);
		
		int direction = Direction.HORZ_PLANE[rand.nextInt(Direction.HORZ_PLANE.length)];
		int offX = Direction.offsetX[direction];
		int offZ = Direction.offsetZ[direction];
				
		int trunkX = x0;
		int trunkY = y0;
		int trunkZ = z0;

		for(int i = 0; i < height; i ++) {
			int archStep = (int)Math.floor(arch);
			trunkX = x0 + offX * archStep;
			trunkZ = z0 + offZ * archStep;
			this.setBlockAndMetadata(world, trunkX, trunkY, trunkZ, trunkID, trunkMeta);
			trunkY ++;
			arch *= 1.3D;
		}
		
		BlockPos cursor = new BlockPos().set(trunkX, trunkY, trunkZ);

		this.edgesPlusShape(world, cursor.move(Direction.DOWN), 5, leavesID, leavesMeta);
		this.edgesPlusShape(world, cursor.move(Direction.UP), 5, leavesID, leavesMeta);
		
		this.edgesSquareShape(world, cursor.move(Direction.UP), 5, leavesID, leavesMeta);
		this.edgesPlusShape(world, cursor, 3, leavesID, leavesMeta);
		
		this.setBlockIfEmpty(world, cursor.move(Direction.UP), leavesID, leavesMeta);
		this.edgesSquareShape(world, cursor, 3, leavesID, leavesMeta);
		
		
		
		return false;
	}

}

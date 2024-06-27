package com.mojontwins.minecraft.worldedit;

import net.minecraft.src.BlockPos;
import net.minecraft.src.Vec3i;
import net.minecraft.src.World;

public class WorldEdit {
	public static BlockPos corner1;
	public static BlockPos corner2;
	public static int clipboard[][][];
	public static Vec3i clipboardDims = Vec3i.NULL_VECTOR.copy();
	
	public static void setCorner1(int x, int y, int z) {
		corner1.x = x;
		corner1.y = y;
		corner1.z = z;
	}
	
	public static void setCorner2(int x, int y, int z) {
		corner1.x = x;
		corner1.y = y;
		corner1.z = z;
	}
	
	public static BlockPos getFrom() {
		// Sorts out corner1 and corner2 to get the top-left-frontmost coordinate (min x, y, z)
		return new BlockPos().set(Math.min(corner1.x, corner2.x), Math.min(corner1.y, corner2.y), Math.min(corner1.z, corner2.z));
	}
	
	public static BlockPos getTo() {
		// Sorts out corner1 and corner2 to get the bottom-right-behindmost coordinate (max x, y, z)
		return new BlockPos().set(Math.max(corner1.x, corner2.x), Math.max(corner1.y, corner2.y), Math.max(corner1.z, corner2.z));
	}
	
	public static void initClipboard(BlockPos from, BlockPos to) {
		clipboardDims.x = to.x - from.x + 1;
		clipboardDims.y = to.y - from.y + 1;
		clipboardDims.z = to.z - from.z + 1;
		
		clipboard = new int[clipboardDims.x][clipboardDims.z][clipboardDims.y];
	}
	
	public static void copy(World world) {
		BlockPos from = getFrom();
		BlockPos to = getTo();
		
		initClipboard(from, to);
		
		for(int x = from.x; x <= to.x; x ++) {
			for(int z = from.z; z <= to.z; z ++) {
				for(int y = from.y; y <= to.y; y ++) {
					clipboard[x][z][y] = world.getBlockId(x, y, z) | world.getBlockMetadata(x, y, z) << 16;
				}
			}
		}
	}
	
	public static void clear(World world) {
		BlockPos from = getFrom();
		BlockPos to = getTo();
		
		for(int x = from.x; x <= to.x; x ++) {
			for(int z = from.z; z <= to.z; z ++) {
				for(int y = from.y; y <= to.y; y ++) {
					world.setBlockAndMetadata(x, y, z, 0, 0);
				}
			}
		}
	}
	
	public static void cut(World world) {
		copy(world);
		clear(world);
	}
	
	public static void paste(World world, int x0, int y0, int z0) {
		for(int x = 0; x < clipboardDims.x; x ++) {
			for(int z = 0; z < clipboardDims.z; z ++) {
				for(int y = 0; y < clipboardDims.y; y ++) {
					int clip = clipboard[x][z][y];
					world.setBlockAndMetadata(x0 + x, y0 + y, z0 + z, clip & 0xFFFF, (clip >> 16) & 0xFF);
				}
			}
		}
	}
	
	public static void rotate_ccw() {
		int aux[][][] = new int[clipboardDims.z][clipboardDims.x][clipboardDims.y];
		for(int x = 0; x < clipboardDims.x; x ++) {
			for(int z = 0; z < clipboardDims.z; z ++) {
				aux[clipboardDims.z - 1 - z][x] = clipboard[x][z];
			}
		}
		
		int t = clipboardDims.x;
		clipboardDims.x = clipboardDims.z;
		clipboardDims.z = t;
		
		clipboard = aux;
	}
	
	public static void rotate_cw() {
		int aux[][][] = new int[clipboardDims.z][clipboardDims.x][clipboardDims.y];
		for(int x = 0; x < clipboardDims.x; x ++) {
			for(int z = 0; z < clipboardDims.z; z ++) {
				aux[z][clipboardDims.x - 1 - x] = clipboard[x][z];
			}
		}
		
		int t = clipboardDims.x;
		clipboardDims.x = clipboardDims.z;
		clipboardDims.z = t;
		
		clipboard = aux;
	}
	
	public static void export(World world, int args, boolean flag) {
		// Make sure it's in the clipboard
		copy(world);
		
		// Now export using method args
		switch (args) {
		
		}
	}
}

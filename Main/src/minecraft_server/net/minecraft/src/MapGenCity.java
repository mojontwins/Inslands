package net.minecraft.src;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MapGenCity extends MapGenBase {
	/* 
	 * Generates chunks with buildings or roads or whatever.
	 * Must be called from provideChunk.
	 */
	
	// TODO :: Massive cleanup. Remove dead / test code or something. Put the 2D bitmaps somewhere else?
	
	public boolean hasBuilding = false;
	public boolean hasRoad = false;
	public int baseHeight;
	public int averageHeight;
	public int roadVariation;
	byte [] heightMap;
	
	private byte floorID;
	private byte wallID;
	//private byte wallMeta; // Not used yet.
	private byte glassID;
	private byte columnsID;

	private int floorIDarray[] = new int[] { Block.cobblestone.blockID, Block.stone.blockID, Block.wood.blockID, Block.cobblestoneMossy.blockID };
	private int wallIDarray[] = new int[] { Block.brick.blockID, Block.stone.blockID, Block.sand.blockID, Block.planks.blockID, Block.cloth.blockID, Block.blockClay.blockID, Block.stoneBricks.blockID, Block.sandStone.blockID };
	private int glassIDarray[] = new int[] { Block.glass.blockID, 0, Block.fence.blockID, Block.glass.blockID };
	private int columnsIDarray[] = new int[] { Block.stairDouble.blockID, Block.wood.blockID, Block.stairDouble.blockID, Block.obsidian.blockID };
	
	private int windowType;
	private int bottomWallType;
	private int columnsType;
	private int b2floorType;
	
	public static final int doorWallType = 5;
	
	public static HashMap<ChunkCoordinates,CityChunkDescriptor> cityChunks = new HashMap<ChunkCoordinates,CityChunkDescriptor>();
	public Chunk thisChunk;
	
	private boolean desertChunk = false;;
	
	private static final int stairOffset = 2;
	
	public static final int EMPTY = 0;
	public static final int ROAD_TYPE_1 = 1;
	public static final int ROAD_TYPE_2 = 2;
	
	private static final int blockIDMappings[][] = new int[][] { 
		{0, 0}, 
		{Block.stairDouble.blockID, 0},               // 1
		{Block.stone.blockID, 0},                     // 2
		{Block.cloth.blockID, 0},                     // 3
		{Block.cobblestone.blockID, 0},               // 4
		{Block.cobblestoneMossy.blockID, 0},          // 5
		{Block.dirt.blockID, 0},                      // 6
		{Block.grass.blockID, 0},                     // 7
		{Block.waterStill.blockID, 0},                // 8
		{Block.fence.blockID, 0},                     // 9
		{Block.torchWood.blockID, 0},                     // 10
		{Block.brick.blockID, 0},                     // 11
		{Block.wood.blockID, 0},                      // 12
		{Block.planks.blockID, 0},                    // 13
		{Block.gravel.blockID, 0},                    // 14
		{Block.stairSingle.blockID, 0},               // 15
		{Block.tilledField.blockID, 0},               // 16
		{Block.crops.blockID, 0},                     // 17
		{Block.stairSingle.blockID, 3}                // 18
	};
	
	private static final byte urbanGardenBlockBitmap[][] = new byte [][] {
		{
			4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
			4,13,13,13,13,13,13,13,13,13,13,13,13,13,13, 4,
			4,13, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,13, 4,
			4,13, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,13, 4,
			4,13, 7, 7,16,16,16,16,16,16,16,16, 7, 7,13, 4,
			4,13, 7, 7,16,16,16,16,16,16,16,16, 7, 7,13, 4,
			4,13, 7, 7,16,16, 8,16,16, 8,16,16, 7, 7,13, 4,
			4,13, 7, 7,16,16, 8,16,16, 8,16,16, 7, 7,13, 4,			
			4,13, 7, 7,16,16, 8,16,16, 8,16,16, 7, 7,13, 4,
			4,13, 7, 7,16,16, 8,16,16, 8,16,16, 7, 7,13, 4,
			4,13, 7, 7,16,16,16,16,16,16,16,16, 7, 7,13, 4,
			4,13, 7, 7,16,16,16,16,16,16,16,16, 7, 7,13, 4,
			4,13, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,13, 4,
			4,13, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,13, 4,
			4,13,13,13,13,13,13, 7, 7,13,13,13,13,13,13, 4,
			4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
		},
		{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0,13,13,13,13,13,13,13,13,13,13,13,13,13,13, 0,
			0,13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,13, 0,
			0,13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,13, 0,
			0,13, 0, 0,17,17,17,17,17,17,17,17, 0, 0,13, 0,
			0,13, 0, 0,17,17,17,17,17,17,17,17, 0, 0,13, 0,
			0,13, 0, 0,17,17, 0,17,17, 0,17,17, 0, 0,13, 0,
			0,13, 0, 0,17,17, 0,17,17, 0,17,17, 0, 0,13, 0,			
			0,13, 0, 0,17,17, 0,17,17, 0,17,17, 0, 0,13, 0,
			0,13, 0, 0,17,17, 0,17,17, 0,17,17, 0, 0,13, 0,
			0,13, 0, 0,17,17,17,17,17,17,17,17, 0, 0,13, 0,
			0,13, 0, 0,17,17,17,17,17,17,17,17, 0, 0,13, 0,
			0,13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,13, 0,
			0,13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,13, 0,
			0,13,13,13,13,13,13, 0, 0,13,13,13,13,13,13, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		},
		{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0,12, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,12, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,			
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0,
			0,12, 9, 9, 9, 9,12, 0, 0,12, 9, 9, 9, 9,12, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		}
	};
	
	private static final byte fountainBigBlockBitmap[][] = new byte [][] {
		{
			7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
			7, 7, 7, 7, 7,14,14,14,14,14,14, 7, 7, 7, 5, 7,
			7, 7, 7,14,14,14,14,14,14,14,14,14,14, 7, 7, 7,
			7 ,5,14,14,14,14, 4, 5, 4, 4,14,14,14,14, 7, 7,
			7, 7,14,14,14, 4, 8, 8, 8, 8, 5,14,14,14, 7, 7,
			7,14,14,14, 5, 8, 8, 8, 8, 8, 8, 4,14,14,14, 7,
			7,14,14, 4, 8, 8, 8, 8, 8, 8, 8, 8, 4,14,14, 7,
			5,14,14, 5, 8, 8, 8, 4, 4, 8, 8, 8, 4,14,14, 7,
			
			7,14,14, 4, 8, 8, 8, 4, 4, 8, 8, 8, 5,14,14, 7,
			7,14,14, 4, 8, 8, 8, 8, 8, 8, 8, 8, 4,14,14, 7,
			7,14,14,14, 4, 8, 8, 8, 8, 8, 8, 4,14,14,14, 7,
			7, 7,14,14,14, 4, 8, 8, 8, 8, 4,14,14,14, 7, 7,
			7 ,7,14,14,14,14, 4, 4, 4, 5,14,14,14,14, 7, 7,
			7, 7, 7,14,14,14,14,14,14,14,14,14,14, 7, 7, 7,
			7, 7, 7, 7, 7,14,14,14,14,14,14, 7, 7, 5, 7, 7,
			7, 7, 5, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
		}, 
		{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,15,15,15,15,15,18, 0, 0, 0, 0, 0,
			0 ,0, 0, 0,15,15, 4, 5, 4, 4,15,15, 0, 0, 0, 0,
			0, 0, 0,15,18, 4, 8, 8, 8, 8, 5,15,15, 0, 0, 0,
			0, 0,15,15, 5, 8, 8, 8, 8, 8, 8, 4,18,15, 0, 0,
			0, 0,15, 4, 8, 8, 8, 8, 8, 8, 8, 8, 4,15, 0, 0,
			0, 0,18, 5, 8, 8, 8, 4, 4, 8, 8, 8, 4,15, 0, 0,
			
			0, 0,15, 4, 8, 8, 8, 4, 4, 8, 8, 8, 5,15, 0, 0,
			0, 0,15, 4, 8, 8, 8, 8, 8, 8, 8, 8, 4,15, 0, 0,
			0, 0,15,15, 4, 8, 8, 8, 8, 8, 8, 4,15,15, 0, 0,
			0, 0, 0,18,15, 4, 8, 8, 8, 8, 4,18,15, 0, 0, 0,
			0 ,0, 0, 0,15,15, 4, 4, 4, 5,15,15, 0, 0, 0, 0,
			0, 0, 0, 0, 0,15,18,15,15,15,15, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		}, 
		{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0 ,0, 0, 0, 0, 0,15,15,15,15, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,15, 0, 0, 0, 0,15, 0, 0, 0, 0, 0,
			0, 0, 0, 0,15, 0, 0, 0, 0, 0, 0,15, 0, 0, 0, 0,
			0, 0, 0,15, 0, 0, 0, 0, 0, 0, 0, 0,15, 0, 0, 0,
			0, 0, 0,15, 0, 0, 0, 4, 5, 0, 0, 0,15, 0, 0, 0,
			
			0, 0, 0,15, 0, 0, 0, 5, 4, 0, 0, 0,15, 0, 0, 0,
			0, 0, 0,15, 0, 0, 0, 0, 0, 0, 0, 0,15, 0, 0, 0,
			0, 0, 0, 0,15, 0, 0, 0, 0, 0, 0,15, 0, 0, 0, 0,
			0, 0, 0, 0, 0,15, 0, 0, 0, 0,15, 0, 0, 0, 0, 0,
			0 ,0, 0, 0, 0, 0,15,15,15,15, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		}
	};
	
	private static final byte streetBlockBitmap[][][] = new byte[][][] {
		{
			{
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1,
				2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
				3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 2,
				2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2,
				1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 2, 2, 2, 2, 1, 4, 4, 4, 4,
			}, 
			{
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				15,15,15,15, 0, 0, 0, 0, 0, 0, 0,15,15,15,15,15,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				15,15,15,15, 0, 0, 0, 0, 0, 0, 0,15,15,15,15,15,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
			}
		}, {
			{
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 1, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 1, 4, 4,
				1, 6, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 6, 1, 4,
				4, 1, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 1, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 2, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 1, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 1, 4, 4,
				1, 6, 1, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 6, 1, 4,
				4, 1, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 1, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 3, 2, 2, 2, 1, 4, 4, 4, 4,
				4, 4, 4, 1, 2, 2, 2, 2, 2, 2, 2, 1, 4, 4, 4, 4
			},
			{
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18, 1,18,15, 0, 0, 0, 0, 0, 0, 0,15,18, 5,18,18,
				 1, 6, 1,15, 0, 0, 0, 0, 0, 0, 0,15, 5, 6, 5,18,
				18, 1,18,15, 0, 0, 0, 0, 0, 0, 0,15,18, 5,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18, 1,18,15, 0, 0, 0, 0, 0, 0, 0,15,18, 1,18,18,
				 1, 6, 1,15, 0, 0, 0, 0, 0, 0, 0,15, 5, 6, 1,18,
				18, 1,18,15, 0, 0, 0, 0, 0, 0, 0,15,18, 1,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18,
				18,18,18,15, 0, 0, 0, 0, 0, 0, 0,15,18,18,18,18
			}
		}, 
		{
			{
				4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4,
				4, 4, 1, 6, 1, 4, 4, 4, 4, 4, 1, 6, 1, 4, 4, 4,
				4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4,
				4, 4, 1, 6, 1, 4, 4, 4, 4, 4, 1, 6, 1, 4, 4, 4,
				4, 4, 4, 1, 4, 4, 4, 4, 4, 4, 4, 1, 4, 4, 4, 4,
				4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 
			},
			{
				18,18,18, 1,18,18,18,18,18,18,18, 1,18,18,18,18,
				18,18, 1, 6, 1,18,18,18,18,18, 1, 6, 1,18,18,18,
				18,18,18, 1,18,18,18,18,18,18,18, 1,18,18,18,18,
				15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,
				18,18,18, 1,18,18,18,18,18,18,18, 1,18,18,18,18,
				18,18, 1, 6, 1,18,18,18,18,18, 1, 6, 1,18,18,18,
				18,18,18, 1,18,18,18,18,18,18,18, 1,18,18,18,18,
				18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18, 
			}
		}
	};

	public void setChunk(Chunk chunk) {
		this.thisChunk = chunk;
		this.heightMap = chunk.landSurfaceHeightMap;
	}
	
	public void setIsDesertChunk(BiomeGenBase biomeGen) {
		this.desertChunk = (biomeGen instanceof BiomeGenDesert) /*&& !(biomeGen instanceof BiomeGenDesertOutskirts)*/;
	}
	
	public void generate(IChunkProvider chunkProviderGenerate, World world, int xChunk, int zChunk, byte[] data, byte[] meta) {
		int chunkMinHeight = 127;
		int chunkMaxHeight = 0;

		this.thisChunk.chestY = -1;
		this.thisChunk.specialY = -1;
		
		Random rand = new Random();
		rand.setSeed((long)xChunk * 341873128712L + (long)zChunk * 132897987541L);
		
		this.hasBuilding = false;
		this.hasRoad = false;
			
		// First of all, get average height
		int sumOfHeights = 0;
		for(int z = 0; z < 16; z ++) {
			for(int x = 0; x < 16; x ++) {
				int h = this.heightMap[z << 4 | x];
				if(h < chunkMinHeight) chunkMinHeight = h;
				if(h > chunkMaxHeight) chunkMaxHeight = h;
				if(h < 65) return;
				sumOfHeights += h;
			}
		}
		this.averageHeight = sumOfHeights >> 8;
		this.baseHeight = this.averageHeight;

		// Set values
		
		this.floorID = (byte) this.floorIDarray[rand.nextInt(this.floorIDarray.length)];
		this.wallID = (byte) this.wallIDarray[rand.nextInt(this.wallIDarray.length)];
		this.glassID = (byte) this.glassIDarray[rand.nextInt(this.glassIDarray.length)];
		this.columnsID = (byte) this.columnsIDarray[rand.nextInt(this.columnsIDarray.length)];

		this.windowType = rand.nextInt(3);
		this.columnsType = rand.nextInt(3);
		this.bottomWallType = rand.nextInt(3) + 2;
		
		// Just moved this here so the average calculation with adjacent chunks is done with the adjusted height
		
		this.baseHeight = 4 + (this.baseHeight & 0xFFFFFFF8);	// Test: multiple of 8 + 4
		
		// This chunk is eligible for city. We'll use cityChunks to find any city chunks adjacent to this
		// To carve or raise to average height.
		int heightSum = this.baseHeight;
		int adjacentCityChunks = 1;
		
		for (int x = xChunk - 1 ; x <= xChunk + 1; x ++) {
			for (int z = zChunk - 1; z <= zChunk + 1; z ++) {
				if ((x != xChunk || z != zChunk) && world.chunkExists(x, z)) {
					Chunk chunk = world.getChunkFromChunkCoords(x, z);
					if (chunk.hasBuilding || chunk.hasRoad) {
						adjacentCityChunks ++;
						heightSum += chunk.baseHeight;
					}
				}
			}
		}
		
		if (adjacentCityChunks > 0) {
			this.baseHeight = heightSum / adjacentCityChunks;
		}
		
		this.baseHeight = 4 + (this.baseHeight & 0xFFFFFFF8);	// Test: multiple of 8 + 4

		// Now height is adjusted...
		if(this.baseHeight < 64 || this.baseHeight > 90) return;
		
		// Chunk main biome for cool desert cities
		BiomeGenBase biomeGen = this.thisChunk.getBiomeGenAt(8, 8);
		this.setIsDesertChunk(biomeGen);
		
		// This is the new condition which I hope works better
		if(chunkMinHeight > this.baseHeight - 9 && (this.desertChunk || chunkMaxHeight < this.baseHeight + 10)) {
			
			// Terraform
			this.raiseTerrain(this.baseHeight, data, meta);
			if(!this.desertChunk) {
				this.flattenTerrain(this.baseHeight, data, meta);
			}
			
			// If a CityChunkDescriptor exists for this chunk, build what we are told
			ChunkCoordinates chunkCoordinates = new ChunkCoordinates(xChunk, zChunk);
			CityChunkDescriptor cityChunkDescriptor = cityChunks.get(chunkCoordinates);
			if (cityChunkDescriptor != null) {
				this.buildPiece (cityChunkDescriptor.forceBuild, data, meta, rand);
				cityChunks.remove(chunkCoordinates);
				return;
			}
			
			if (
				((xChunk & 1) == 1 && (zChunk & 1) == 1) || 
				(
					rand.nextInt(6) == 0 && 
					((xChunk & 1) == 1 || (zChunk & 1) == 1)
				)
			) {
				// First of all: a misaligned building should cause adjacent roads to change
				// If chunks with such roads have been already generated, they are altered and chunks marked for re-populating
				// If not, they are stored for the future. Which is PRETTY CLEVER to work around the out of order chunk generation
				
				if ((xChunk & 0) == 0) {
					this.markOrProcessChunk(world, xChunk, zChunk - 1, ROAD_TYPE_1, rand);
					this.markOrProcessChunk(world, xChunk, zChunk + 1, ROAD_TYPE_1, rand);
				} else if ((zChunk & 0) == 0) {
					this.markOrProcessChunk(world, xChunk - 1, zChunk, ROAD_TYPE_2, rand);
					this.markOrProcessChunk(world, xChunk + 1, zChunk, ROAD_TYPE_2, rand);
				}
				
				// New contents being generated ahead, so
				rand = new Random();
				rand.setSeed((long)xChunk * 341873128712L + (long)zChunk * 132897987541L);
								
				int y = this.baseHeight;
				Building building = null;
				
				switch (rand.nextInt(20)) {
					case 0:
						// Big fountain
						this.generateBigFountain(0, y, 0, data, meta, rand);
						break;
					case 1:
						// Urban garden
						this.generateUrbanGarden(0, y, 0, data, meta, rand);
						break;
					case 2:
						// Burger
						building = new BuildingBurger();
						break;
					case 3:
						// Shops
						building = new BuildingShoppe1(y);
						break;
					case 4:
						// Gas station
						building = new BuildingGas();
						break;
					case 5:
						// Witches palace
						if(rand.nextInt(4) == 0) {
							building = new BuildingWitchMansion(y);
							break;
						}
						// Wonky code! Like the C code I write for 8 bit machines!
						// Note how the "break" is placed inside the IF so it generates the next kind of building if it fails!
					case 6:
						// Graveyard
						if(rand.nextInt(3) == 0) {
							building = new BuildingGraveyard(y);
							break;
						}
						// Wonky code! Like the C code I write for 8 bit machines!
						// Note how the "break" is placed inside the IF so it generates the next kind of building if it fails!
					default:
						// procedural building
						switch (rand.nextInt(2)) {
							case 0:
								this.generateSimpleBuilding1(0, y, 0, data, meta, rand);
								break;
							case 1:
								this.generateSimpleBuilding2(0, y, 0, data, meta, rand);
								break;
							// TODO: Add more procedural buildings
						}
				}
				
				if(building != null) {
					building.generate(y, this.thisChunk);
					this.thisChunk.building = building;
				}
				
				this.hasBuilding = true;
			} else {
				int variation;
				
				if ((xChunk & 1) == 0 && (zChunk & 1) == 0) {
					variation = 0;
				} else if ((zChunk & 1) == 0) {
					variation = 1;
				} else {
					variation = 2;
				}
				
				this.generateStreetFloorSimple(0, this.baseHeight, 0, data, meta, rand, variation);
				
				this.hasRoad = true;
				this.roadVariation = variation;
			}
			
		}		
	}
	
	public void raiseTerrain(int y0, byte[] data, byte[] meta) {
		//byte b = (byte)Block.cobblestone.blockID;
		byte w = (byte)Block.stone.blockID;
		
		for (int y = y0 - 9; y < y0; y ++) {
			// This was hollow, but I've decided against
			/*
			for (int i = 0; i < 16; i ++) {
				data [i << 11 | y] = b;
				data [i << 7 | y] = b;
				data [15 << 11 | i << 7 | y] = b;
				data [i << 11 | 15 << 7 | y ] = b;
			}
			*/
			int idx = y;
			for(int i = 0; i < 256; i ++) {
				if(data[idx] != w) data[idx] = w;
				idx += 128;
			}
		}
		
		// Holes
		/*
		data [7 << 11 | (y0 - 9)] = 0;
		data [7 << 11 | (y0 - 8)] = 0;
		data [7 << 7 | (y0 - 9)] = 0;
		data [7 << 7 | (y0 - 8)] = 0;
		data [7 << 11 | 15 << 7 | (y0 - 9)] = 0;
		data [7 << 11 | 15 << 7 | (y0 - 8)] = 0;
		data [15 << 11 | 7 << 7 | (y0 - 9)] = 0;
		data [15 << 11 | 7 << 7 | (y0 - 8)] = 0;
		*/
	}
	
	public void flattenTerrain(int y0, byte[] data, byte meta[]) {
		for (int y = y0 + 1; y < y0 + 10; y ++) {
			int idx = y;
			for(int i = 0; i < 256; i ++) {
				data[idx] = 0; idx += 128;
			}
		}
	}
	
	public void generateSimpleBuilding1(int x0, int y0, int z0, byte[] data, byte[] meta, Random rand) {
		// Get max number of floors & random number of floors
		int maxFloors = (120 - y0 - 5) / 5;					
		int floors = 2 + rand.nextInt(maxFloors - 2);

		int offsetX = x0; // rand.nextInt(2);
		int offsetZ = z0; // rand.nextInt(2);
		
		int y = y0;
		
		boolean orientation = rand.nextBoolean();
		
		// Base floor
		this.drawBaseFloor(offsetX, y, offsetZ, data, meta, rand);
		y += 5;
		
		int yFloors = y;
		
		// Common floors
		for (int i = 0; i < floors; i ++) {
			this.drawFloorType1(offsetX, y, offsetZ, data, meta, rand, orientation);
			y += 5;
		}
		
		// Roof
		this.drawRoofType1(offsetX, y, offsetZ, data, meta, rand, orientation);
		
		// Furniture
		y = yFloors;
		for(int i = 0; i < floors; i ++) {
			int numPieces = rand.nextInt(3);
			for(int j = 0; j < numPieces; j ++) this.drawFurniturePiece(rand.nextInt(FurniturePieces.NUM_FURNITURE_PIECES), x0 + 2, y + 1, z0 + 2, data, meta, rand);
			y += 5;
		}
	}
	
	public void generateSimpleBuilding2(int x0, int y0, int z0, byte[]data, byte[] meta, Random rand) {
		int maxFloors = (120 - y0 - 5) / 4;
		int floors = 2 + rand.nextInt(maxFloors - 2);

		int offsetX = x0 + 1; // rand.nextInt(2);
		int offsetZ = z0 + 1; // rand.nextInt(2);
		
		int y = y0;
		
		this.b2floorType = rand.nextInt(2);
		
		// Base floor
		this.drawBaseFloor(offsetX - 1, y, offsetZ - 1, data, meta, rand);
		int x = offsetX + 2; int z = offsetZ + 2;
		data[(x + 1) << 11 | z << 7 | (y + 1)] = (byte)Block.stairSingle.blockID;
		data[(x + 1) << 11 | (z + 1) << 7 | (y + 1)] = this.wallID;
		data[(x + 2) << 11 | z << 7 | (y + 1)] = (byte)Block.stairDouble.blockID;
		y += 5;	
				
		int yFloors = y;
		
		// Common floors
		for (int i = 0; i < floors; i ++) {
			this.drawFloorType2(offsetX, y, offsetZ, data, meta, rand);
			y += 4;
		}
		
		// Roof
		this.drawRoofType2(offsetX, y, offsetZ, data, meta, rand);
		
		// Furniture
		y = yFloors;
		for(int i = 0; i < floors; i ++) {
			int numPieces = rand.nextInt(3);
			for(int j = 0; j < numPieces; j ++) this.drawFurniturePiece(rand.nextInt(FurniturePieces.NUM_FURNITURE_PIECES), x0 + 2, y + 1, z0 + 2, data, meta, rand);
			y += 4;
		}
	}
	
	public void drawBaseFloor(int x0, int y0, int z0, byte[] data, byte[] meta, Random rand) {
		// Floor
		for (int x = 0; x < 16; x ++)
			for (int z = 0; z < 16; z ++) {
				byte b = (x >= 1 && x <= 14 && z >= 1 && z <= 14) ? this.floorID : (byte)Block.cobblestone.blockID;
				data[(x0 + x) << 11 | (z0 + z) << 7 | y0] = b;
			}
		
		// Columns
		int x = x0 + 1;
		for (int i = 0; i < 4; i ++) {
			int z = z0 + 1;
			for (int j = 0; j < 4; j ++) {
				if (i == 0 || j == 0 || i == 3 || j == 3) {
					for (int y = y0 + 1; y <= y0 + 5; y ++) {
						data[x << 11 | z << 7 | y] = this.columnsID;
						if (i == 0) data[(x - 1) << 11 | z << 7 | y] = this.columnsID;
						if (i == 3) data[(x + 1) << 11 | z << 7 | y] = this.columnsID;
						if (j == 0) data[x << 11 | (z - 1) << 7 | y] = this.columnsID;
						if (j == 3) data[x << 11 | (z + 1) << 7 | y] = this.columnsID;

						if (i == 0 && j == 0) data[(x - 1) << 11 | (z - 1) << 7 | y] = this.columnsID;
						if (i == 3 && j == 0) data[(x + 1) << 11 | (z - 1) << 7 | y] = this.columnsID;
						if (i == 0 && j == 3) data[(x - 1) << 11 | (z + 1) << 7 | y] = this.columnsID;
						if (i == 3 && j == 3) data[(x + 1) << 11 | (z + 1) << 7 | y] = this.columnsID;
					}
					
				}
				z += 4;
			}
			x += 4;
		}
		
		// Walls
		int whichIterationForDoor = rand.nextInt(3);
		for (int i = 0; i < 3; i ++) {
			int selectedWall = rand.nextInt(4);
			this.drawWallType1(x0 + 2 + i * 4, y0 + 1, z0 + 1, data, meta, true, selectedWall == 0 && whichIterationForDoor == i ? MapGenCity.doorWallType : this.bottomWallType);
			this.drawWallType1(x0 + 2 + i * 4, y0 + 1, z0 + 13, data, meta, true, selectedWall == 1 && whichIterationForDoor == i ? MapGenCity.doorWallType : this.bottomWallType);
			
			this.drawWallType1(x0 + 1, y0 + 1, z0 + 2 + i * 4, data, meta, false, selectedWall == 2 && whichIterationForDoor == i ? MapGenCity.doorWallType : this.bottomWallType);
			this.drawWallType1(x0 + 13, y0 + 1, z0 + 2 + i * 4, data, meta, false, selectedWall == 3 && whichIterationForDoor == i ? MapGenCity.doorWallType : this.bottomWallType);
		}
	}
	
	public void drawFloorType1(int x0, int y0, int z0, byte[] data, byte[] meta, Random rand, boolean stairsOrientation) {
		// Floor
		for (int x = 1; x < 14; x ++)
			for (int z = 1; z < 14; z ++) {
				data[(x0 + x) << 11 | (z0 + z) << 7 | y0] = this.floorID;
			}
		
		if(stairsOrientation) {
			// Hole for stairs
			for (int z = 3; z < 10; z ++) {
				data[(x0 + stairOffset) << 11 | (z0 + z) << 7 | y0] = 0;
			}
			
			// Stairs
			int x = x0 + stairOffset, y = y0, z = z0 + 3;
			for (int i = 0; i < 4; i ++) {
				data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;
				data[x << 11 | z << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				data[x << 11 | (z + 1) << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				z += 2; y--;
			}
			data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;
		} else {
			// Hole for stairs
			for (int x = 3; x < 10; x ++) {
				data[(x0 + x) << 11 | (z0 + stairOffset) << 7 | y0] = 0;
			}
			
			// Stairs
			int x = x0 + 3, y = y0, z = z0 + stairOffset;
			for (int i = 0; i < 4; i ++) {
				data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;
				data[x << 11 | z << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				data[x + 1 << 11 | z << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				x += 2; y--;
			}
			data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;			
		}
		
		// Columns
		int x = x0 + 1;
		for (int i = 0; i < 4; i ++) {
			int z = z0 + 1;
			for (int j = 0; j < 4; j ++) {
				if (i == 0 || j == 0 || i == 3 || j == 3) {
					for (int y = y0; y <= y0 + 5; y ++) {
						data[x << 11 | z << 7 | y] = this.columnsID;
						if (this.columnsType > 0) {
							if (i == 0) data[(x - 1) << 11 | z << 7 | y] = this.columnsID;
							if (i == 3) data[(x + 1) << 11 | z << 7 | y] = this.columnsID;
							if (j == 0) data[x << 11 | (z - 1) << 7 | y] = this.columnsID;
							if (j == 3) data[x << 11 | (z + 1) << 7 | y] = this.columnsID;
						}
						if (this.columnsType == 2) {
							if (i == 0 && j == 0) data[(x - 1) << 11 | (z - 1) << 7 | y] = this.columnsID;
							if (i == 3 && j == 0) data[(x + 1) << 11 | (z - 1) << 7 | y] = this.columnsID;
							if (i == 0 && j == 3) data[(x - 1) << 11 | (z + 1) << 7 | y] = this.columnsID;
							if (i == 3 && j == 3) data[(x + 1) << 11 | (z + 1) << 7 | y] = this.columnsID;
						}
					}
					
				}
				z += 4;
			}
			x += 4;
		}
		
		// Walls
		for (int i = 0; i < 3; i ++) {
			this.drawWallType1(x0 + 2 + i * 4, y0 + 1, z0 + 1, data, meta, true, this.windowType);
			this.drawWallType1(x0 + 2 + i * 4, y0 + 1, z0 + 13, data, meta, true, this.windowType);
			
			this.drawWallType1(x0 + 1, y0 + 1, z0 + 2 + i * 4, data, meta, false, this.windowType);
			this.drawWallType1(x0 + 13, y0 + 1, z0 + 2 + i * 4, data, meta, false, this.windowType);
		}
	}
	
	public void drawFloorType2(int x0, int y0, int z0, byte [] data, byte[] meta, Random rand) {
		// Corners, pillars & windows
		for (int y = y0; y < y0 + 5; y ++) {
			
			// Corners
			data[x0 << 11 | z0 << 7 | y] = this.columnsID;
			data[(x0 + 1) << 11 | z0 << 7 | y] = this.columnsID;
			data[x0 << 11 | (z0 + 1) << 7 | y] = this.columnsID;
			data[(x0 + 1) << 11 | (z0 + 1) << 7 | y] = this.columnsID;
			
			data[(x0 + 11) << 11 | z0 << 7 | y] = this.columnsID;
			data[(x0 + 12) << 11 | z0 << 7 | y] = this.columnsID;
			data[(x0 + 11) << 11 | (z0 + 1) << 7 | y] = this.columnsID;
			data[(x0 + 12) << 11 | (z0 + 1) << 7 | y] = this.columnsID;
			
			data[x0 << 11 | (z0 + 11) << 7 | y] = this.columnsID;
			data[(x0 + 1) << 11 | (z0 + 11) << 7 | y] = this.columnsID;
			data[x0 << 11 | (z0 + 12) << 7 | y] = this.columnsID;
			data[(x0 + 1) << 11 | (z0 + 12) << 7 | y] = this.columnsID;
			
			data[(x0 + 11) << 11 | (z0 + 11) << 7 | y] = this.columnsID;
			data[(x0 + 12) << 11 | (z0 + 11) << 7 | y] = this.columnsID;
			data[(x0 + 11) << 11 | (z0 + 12) << 7 | y] = this.columnsID;
			data[(x0 + 12) << 11 | (z0 + 12) << 7 | y] = this.columnsID;
			
			// Pillars
			for (int i = 0; i < 7; i += 2) {
				data[(x0 + 3 + i) << 11 | z0 << 7 | y] = this.columnsID;
				data[(x0 + 3 + i) << 11 | (z0 + 1) << 7 | y] = this.columnsID;
				
				data[(x0 + 3 + i) << 11 | (z0 + 11) << 7 | y] = this.columnsID;
				data[(x0 + 3 + i) << 11 | (z0 + 12) << 7 | y] = this.columnsID;
				
				data[x0 << 11 | (z0 + 3 + i) << 7 | y] = this.columnsID;
				data[(x0 + 1) << 11 | (z0 + 3 + i) << 7 | y] = this.columnsID;
				data[(x0 + 11) << 11 | (z0 + 3 + i) << 7 | y] = this.columnsID;
				data[(x0 + 12) << 11 | (z0 + 3 + i) << 7 | y] = this.columnsID;
			}
			
			// Windows
			for (int i = 0; i < 9; i += 2) {
				data[(x0 + 2 + i) << 11 | (z0 + 1) << 7 | y] = this.glassID;
				data[(x0 + 2 + i) << 11 | (z0 + 11) << 7 | y] = this.glassID;
				
				data[(x0 + 1) << 11 | (z0 + 2 + i) << 7 | y] = this.glassID;
				data[(x0 + 11) << 11 | (z0 + 2 + i) << 7 | y] = this.glassID;
			}
		}
				
		// Floor
		int extend = this.b2floorType << 1;
		for (int x = x0 + 2 - extend; x < x0 + 11 + extend; x ++) {
			for (int z = z0 + 2 - extend; z < z0 + 11 + extend; z ++) {
				data[x << 11 | z << 7 | y0] = this.floorID;
			}
		}
		
		this.drawStairsType2(x0, y0, z0, data, meta);	
	}
	
	public void drawFurniturePiece(int n, int x0, int y0, int z0, byte[] data, byte[] meta, Random rand) {
		// get dimensions
		byte size[] = new byte[2];
		FurniturePieces.getFeatureSize(n, size);
		
		int dimX = size[0]; 
		int dimZ = size[1];
		
		x0 += rand.nextInt(11 - dimX);
		z0 += rand.nextInt(11 - dimZ);
		
		// check if clear
		for(int x = 0; x < dimX; x ++) {
			for(int z = 0; z < dimZ; z ++) {
				int idx = (x0 + x) << 11 | (z0 + z) << 7 | y0;
				if(data[idx] != 0 || data [idx + 1] != 0) return;
			}
		}

		// Draw selected piece
		FurniturePieces.drawFeature(n, x0, y0, z0, this.thisChunk);
	}
	
	public void drawStairsType2(int x0, int y0, int z0, byte[] data, byte[] meta) {
		// Stairs down (in a corner)
		// Layer 1 with hole
		int x = x0 + 2, y = y0, z = z0 + 2;
		data[x << 11 | z << 7 | y] = 0;
		data[(x + 1) << 11 | z << 7 | y] = (byte)Block.stairSingle.blockID;
		data[(x + 2) << 11 | z << 7 | y] = (byte)Block.stairDouble.blockID;
		data[x << 11 | (z + 1) << 7 | y] = 0;
		data[(x + 1) << 11 | (z + 1) << 7 | y] = this.wallID;
		data[(x + 2) << 11 | (z + 1) << 7 | y] = 0;
		data[x << 11 | (z + 2) << 7 | y] = 0;
		data[(x + 1) << 11 | (z + 2) << 7 | y] = 0;
		data[(x + 2) << 11 | (z + 2) << 7 | y] = 0;
		
		// Layer 2
		y --;
		data[x << 11 | z << 7 | y] = (byte)Block.stairDouble.blockID;
		data[x << 11 | (z + 1) << 7 | y] = (byte)Block.stairSingle.blockID;
		data[(x + 1) << 11 | (z + 1) << 7 | y] = this.wallID;
		
		// Layer 3
		y --;
		data[x << 11 | (z + 2) << 7 | y] = (byte)Block.stairDouble.blockID;
		data[(x + 1) << 11 | (z + 2) << 7 | y] = (byte)Block.stairSingle.blockID;
		data[(x + 1) << 11 | (z + 1) << 7 | y] = this.wallID;
		
		// Layer 4
		y --;
		data[(x + 2) << 11 | (z + 1) << 7 | y] = (byte)Block.stairSingle.blockID;
		data[(x + 2) << 11 | (z + 2) << 7 | y] = (byte)Block.stairDouble.blockID;
		data[(x + 1) << 11 | (z + 1) << 7 | y] = this.wallID;
	}
	
	public void drawRoofType1(int x0, int y0, int z0, byte[] data, byte[] meta, Random rand, boolean stairsOrientation) {
		// Floor
		for (int x = 1; x < 14; x ++)
			for (int z = 1; z < 14; z ++) {
				data[(x0 + x) << 11 | (z0 + z) << 7 | y0] = this.floorID;
			}
		
		// Possible chest
		if(rand.nextInt(8) == 0) {
			this.thisChunk.chestY = y0 + 1;
			this.thisChunk.chestX = 4 + rand.nextInt(8);
			this.thisChunk.chestZ = 4 + rand.nextInt(8);
		}
		
		// Possible spawner
		if(rand.nextInt(4) == 0) {
			this.thisChunk.specialY = y0 - 2;
			this.thisChunk.specialX = 4 + rand.nextInt(8);
			this.thisChunk.specialZ = 4 + rand.nextInt(8);
			data[(thisChunk.specialX) << 11 | (thisChunk.specialZ) << 7 | (y0 - 3)] = (byte)Block.cobblestoneMossy.blockID;
			data[(thisChunk.specialX) << 11 | (thisChunk.specialZ) << 7 | (y0 - 4)] = (byte)Block.cobblestoneMossy.blockID;

		}
		
		if(stairsOrientation) {
			// Hole for stairs
			for (int z = 3; z < 10; z ++) {
				data[(x0 + stairOffset) << 11 | (z0 + z) << 7 | y0] = 0;
			}
			
			// Stairs
			int x = x0 + stairOffset, y = y0, z = z0 + 3;
			for (int i = 0; i < 4; i ++) {
				data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;
				data[x << 11 | z << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				data[x << 11 | (z + 1) << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				z += 2; y--;
			}
			data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;
		} else {
			// Hole for stairs
			for (int x = 3; x < 10; x ++) {
				data[(x0 + x) << 11 | (z0 + stairOffset) << 7 | y0] = 0;
			}
			
			// Stairs
			int x = x0 + 3, y = y0, z = z0 + stairOffset;
			for (int i = 0; i < 4; i ++) {
				data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;
				data[x << 11 | z << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				data[x + 1 << 11 | z << 7 | (y - 1)] = (byte) Block.cobblestone.blockID;
				x += 2; y--;
			}
			data[x << 11 | z << 7 | y] = (byte) Block.stairSingle.blockID;			
		}
		
		// Railing
		int y = y0 + 1;
		for (int i = 1; i < 14; i ++) {
			data[(x0 + i) << 11 | (z0 + 1) << 7 | y] = (byte) this.columnsID;
			data[(x0 + i) << 11 | (z0 + 13) << 7 | y] = (byte) this.columnsID;
			data[(x0 + 1) << 11 | (z0 + i) << 7 | y] = (byte) this.columnsID;
			data[(x0 + 13) << 11 | (z0 + i) << 7 | y] = (byte) this.columnsID;
		}
		
		// Dirt?
		if (rand.nextInt(6) == 0) {
			for (int x = 2; x < 13; x ++)
				for (int z = 2; z < 13; z ++) {
					data[(x0 + x) << 11 | (z0 + z) << 7 | y] = (byte)Block.grass.blockID;
				}		
		}
	}
	
	public void drawRoofType2(int x0, int y0, int z0, byte[] data, byte[] meta, Random rand) {
		for (int x = x0; x < x0 + 13; x ++) {
			for (int z = z0; z < z0 + 13; z ++) {
				if (x != x0 && z != z0 && x != x0 + 12 && z != z0 + 12) {
					data[x << 11 | z << 7 | y0] = this.floorID;
				} else {
					data[x << 11 | z << 7 | y0] = this.wallID;
					data[x << 11 | z << 7 | (y0 + 1)] = this.wallID;
				}
			}
		}
		
		// Possible chest
		if(rand.nextInt(8) == 0) {
			this.thisChunk.chestY = y0 + 1;
			this.thisChunk.chestX = 4 + rand.nextInt(8);
			this.thisChunk.chestZ = 4 + rand.nextInt(8);
		}
		
		// Possible spawner
		// Possible chest
		if(rand.nextInt(4) == 0) {
			this.thisChunk.specialY = y0 - 2;
			this.thisChunk.specialX = 4 + rand.nextInt(8);
			this.thisChunk.specialZ = 4 + rand.nextInt(8);
		}
		
		this.drawStairsType2(x0, y0, z0, data, meta);		
	}
	
	public void drawWallType1(int x0, int y0, int z0, byte[] data, byte meta[], boolean orientation, int windowType) {
		if (orientation) {
			// along the x axis
			int z = z0;
			if (windowType == MapGenCity.doorWallType) {
				for(int x = x0; x < x0 + 3; x ++) {
					for(int y = y0; y < y0 + 4; y ++) {
						if(y > y0 + 1 || x != x0 + 1) data[x << 11 | z << 7 | y] = (byte)Block.stone.blockID;
					}
				}
			} else {
				int y = y0;
				for(int x = x0; x < x0 + 3; x ++) {
					data[x << 11 | z << 7 | y] = this.wallID;
					
					if (windowType < 2) {
						data[x << 11 | z << 7 | (y + 3)] = this.wallID;
					}
					
					if (windowType != 1 && windowType < 4) {
						for (int yy = y + (windowType == 3 ? 0 : 1); yy <= y + (windowType == 0 ? 2 : 3); yy ++) {
							data[x << 11 | z << 7 | yy] = this.glassID;
						}
					} else {
						byte b = x == x0 + 1 ? this.glassID : this.wallID;
						data[x << 11 | z << 7 | (y + 1)] = b;
						data[x << 11 | z << 7 | (y + 2)] = b;
						data[x << 11 | z << 7 | (y + 3)] = this.wallID;
					}
				}
			}
		} else {
			// along the z axis
			int x = x0;
			if (windowType == MapGenCity.doorWallType) {
				for(int z = z0; z < z0 + 3; z ++) {
					for(int y = y0; y < y0 + 4; y ++) {
						if(y > y0 + 1 || z != z0 + 1) data[x << 11 | z << 7 | y] = (byte)Block.stone.blockID;
					}
				}
			} else {
				int y = y0;
				for(int z = z0; z < z0 + 3; z ++) {
					data[x << 11 | z << 7 | y] = this.wallID;
					
					if (windowType < 2) {
						data[x << 11 | z << 7 | (y + 3)] = this.wallID;
					}
					
					if (windowType != 1 && windowType < 4) {
						for (int yy =  y + (windowType == 3 ? 0 : 1); yy <= y + (windowType == 0 ? 2 : 3); yy ++) {
							data[x << 11 | z << 7 | yy] = this.glassID;
						}
					} else {
						byte b = z == z0 + 1 ? this.glassID : this.wallID;
						data[x << 11 | z << 7 | (y + 1)] = b;
						data[x << 11 | z << 7 | (y + 2)] = b;
						data[x << 11 | z << 7 | (y + 3)] = this.wallID;
					}
				}
			}
		}
	}
	
	public void fillWholeLayer(int y0, byte[] data, byte[] meta, byte blockID) {
		for (int x = 0; x < 16; x ++)
			for (int z = 0; z < 16; z ++) 
				data[x << 11 | z << 7 | y0] = blockID;
	}
	
	public void drawBitmap(int y0, byte[] data, byte[] meta, byte[] bitmap) {
		int idx = 0; byte b; 
		for (int x = 0; x < 16; x ++)
			for (int z = 0; z < 16; z ++) {
				b = (byte)blockIDMappings[bitmap[idx]][0]; 
				if(b != 0 || !this.desertChunk) {
					data[x << 11 | z << 7 | y0] = b;
					b = (byte)blockIDMappings[bitmap[idx]][1];
					meta[x << 11 | z << 7 | y0] = b;
				}
				idx ++;
			}
		
		// Fix slabs
		for (int x = 0; x < 16; x ++)
			for (int z = 0; z < 16; z ++) {
				idx = x << 11 | z << 7 | y0;
				if(data[idx] == Block.stairSingle.blockID && data[idx + 1] != 0) {
					data[idx] = (byte)Block.dirt.blockID;
				}
			}
	}
	
	public void drawBitmapRoadHollow(int y0, byte[] data, byte[] meta, byte[] bitmap) {
		int idx = 0;
		for (int x = 0; x < 16; x ++)
			for (int z = 0; z < 16; z ++) {
				byte b = bitmap[idx ++];
				//data[x << 11 | z << 7 | y0] = (b != 2 && b != 3) ? (byte)blockIDMappings[b][0] : 0;
				if(b != 2 && b != 3 && b != 0) data[x << 11 | z << 7 | y0] = (byte)blockIDMappings[b][0];
				else if(!this.desertChunk) data[x << 11 | z << 7 | y0] = 0;
			}	
	}
	
	public void generateStreetFloorSimple(int x0, int y0, int z0, byte[] data, byte[] meta, Random rand, int variation) {
		for (int i = 0; i < 2; i ++)
			this.drawBitmap(y0 - 1 + i, data, meta, streetBlockBitmap[variation][i]);
	}
	
	public void generateBigFountain(int x0, int y0, int z0, byte []data, byte[] meta, Random rand) {
		for (int i = 0; i < 3; i ++) 
			this.drawBitmap(y0 + i, data, meta, fountainBigBlockBitmap[i]);
		
		int y = y0 + 3;
		data[7 << 11 | 7 << 7 | y] = (byte)Block.cobblestone.blockID;
		data[8 << 11 | 7 << 7 | y] = (byte)Block.cobblestone.blockID;
		data[7 << 11 | 8 << 7 | y] = (byte)Block.cobblestoneMossy.blockID;
		data[8 << 11 | 8 << 7 | y] = (byte)Block.cobblestone.blockID;
		y++;
		data[7 << 11 | 7 << 7 | y] = (byte)Block.fence.blockID;
		data[8 << 11 | 7 << 7 | y] = (byte)Block.fence.blockID;
		data[7 << 11 | 8 << 7 | y] = (byte)Block.fence.blockID;
		data[8 << 11 | 8 << 7 | y] = (byte)Block.fence.blockID;
		y++;
		data[7 << 11 | 7 << 7 | y] = (byte)Block.stairSingle.blockID;
		data[8 << 11 | 7 << 7 | y] = (byte)Block.stairSingle.blockID;
		data[7 << 11 | 8 << 7 | y] = (byte)Block.stairSingle.blockID;
		data[8 << 11 | 8 << 7 | y] = (byte)Block.stairSingle.blockID;
	}
	
	public void generateUrbanGarden(int x0, int y0, int z0, byte[] data, byte[] meta, Random rand) {
		for (int i = 0; i < 3; i ++) 
			this.drawBitmap(y0 + i, data, meta, urbanGardenBlockBitmap[i]);		
	}
	
	public void markOrProcessChunk(World world, int xChunk, int zChunk, int cityPiece, Random rand) {
		if (world.chunkExists(xChunk, zChunk)) {
			// Modify if it contains a cross road
			Chunk chunk = world.getChunkFromChunkCoords(xChunk, zChunk);
			if(chunk.hasRoad && chunk.roadVariation == 0) {
				this.raiseTerrain(chunk.baseHeight, chunk.blocks, chunk.data);
				if(!this.desertChunk) {
					this.flattenTerrain(chunk.baseHeight, chunk.blocks, chunk.data);
				}
				this.generateStreetFloorSimple(0, chunk.baseHeight, 0, chunk.blocks, chunk.data, rand, cityPiece);
				chunk.roadVariation = cityPiece;
				chunk.isTerrainPopulated = false;
			}
		} else {
			// Mark for the future
			CityChunkDescriptor cityChunkDescriptor = new CityChunkDescriptor ();
			cityChunkDescriptor.forceBuild = cityPiece;
			cityChunks.put(new ChunkCoordinates(xChunk, zChunk), cityChunkDescriptor);
		}
	}
	
	public void buildPiece(int pieceID, byte[] data, byte[] meta, Random rand) {
		//this.raiseTerrain(this.baseHeight, data);
		//this.flattenTerrain(this.baseHeight, data);
		
		switch (pieceID) {
			case ROAD_TYPE_1:
			case ROAD_TYPE_2:
				this.generateStreetFloorSimple(0, this.baseHeight, 0, data, meta, rand, pieceID);
				this.hasRoad = true;
				this.roadVariation = pieceID;
		}
	}
	
	public void populate(World world, Random rand, int chunkX, int chunkZ, Chunk chunk) {
		int x0 = chunkX * 16;
		int z0 = chunkZ * 16;
		
		BiomeGenBase biomeGen = chunk.getBiomeGenAt(8, 8);
		this.desertChunk = biomeGen instanceof BiomeGenDesert;
		
		int i, x, y, z;
		
		// City related decorations		
		if(chunk.hasRoad) {
			y = chunk.baseHeight + 1;
			WorldGenTrees worldGenTrees = this.desertChunk ? new WorldGenTreesDead() : new WorldGenTrees();

			switch (chunk.roadVariation) {
				case 2:
					worldGenTrees.generate(world, rand, x0 + 1, y, z0 + 3);
					worldGenTrees.generate(world, rand, x0 + 1, y, z0 + 11);
					worldGenTrees.generate(world, rand, x0 + 13, y, z0 + 3);
					worldGenTrees.generate(world, rand, x0 + 13, y, z0 + 11);
					
					(new WorldGenStreetLight(rand.nextInt(3) == 0)).generate(world, rand, x0 + 2, y, z0 + 7);
					(new WorldGenStreetLight(rand.nextInt(3) == 0)).generate(world, rand, x0 + 2, y, z0 + 15);
					(new WorldGenStreetLight(rand.nextInt(3) == 0)).generate(world, rand, x0 + 12, y, z0 + 7);
					(new WorldGenStreetLight(rand.nextInt(3) == 0)).generate(world, rand, x0 + 12, y, z0 + 15);
					break;
				case 1:
					worldGenTrees.generate(world, rand, x0 + 3, y, z0 + 1);
					worldGenTrees.generate(world, rand, x0 + 11, y, z0 + 1);
					worldGenTrees.generate(world, rand, x0 + 3, y, z0 + 13);
					worldGenTrees.generate(world, rand, x0 + 11, y, z0 + 13);
					
					(new WorldGenStreetLight(rand.nextInt(3) != 0)).generate(world, rand, x0 + 7, y, z0 + 2);
					(new WorldGenStreetLight(rand.nextInt(3) != 0)).generate(world, rand, x0 + 15, y, z0 + 2);
					(new WorldGenStreetLight(rand.nextInt(3) != 0)).generate(world, rand, x0 + 7, y, z0 + 12);
					(new WorldGenStreetLight(rand.nextInt(3) != 0)).generate(world, rand, x0 + 15, y, z0 + 12);
					break;
			}
			
			// Railings & stairs
			if (world.chunkExists(chunkX - 1, chunkZ)) {
				Chunk otherChunk = world.getChunkFromChunkCoords(chunkX - 1, chunkZ);
				if(otherChunk.hasRoad || otherChunk.hasBuilding) {
					if(otherChunk.baseHeight < chunk.baseHeight) {
						// Draw railing
						for(i = 0; i < 16; i ++) {
							world.setBlock(x0, y, z0 + i, Block.streetLanternFence.blockID);
							world.setBlock(x0, y - 1, z0 + i, Block.stairDouble.blockID);
						}
						// Hole in railing?
						if(otherChunk.hasRoad) {
							world.setBlock(x0, y, z0 + 5, 0);
							world.setBlock(x0, y, z0 + 6, 0);
						}
					} else if(otherChunk.hasRoad && otherChunk.baseHeight > chunk.baseHeight) {
						// Draw stairs
						(new WorldGenStairs(1)).generate(world, rand, x0, y - 2, z0 + 5);
					}
				}
			}
			
			if (world.chunkExists(chunkX + 1, chunkZ)) {
				Chunk otherChunk = world.getChunkFromChunkCoords(chunkX + 1, chunkZ);
				if(otherChunk.hasRoad || otherChunk.hasBuilding) {
					if(otherChunk.baseHeight < chunk.baseHeight) {
						// Draw railing
						for(i = 0; i < 16; i ++) {
							world.setBlock(x0 + 15, y, z0 + i, Block.streetLanternFence.blockID);
							world.setBlock(x0 + 15, y - 1, z0 + i, Block.stairDouble.blockID);
						}
						// Hole in railing?
						if(otherChunk.hasRoad) {
							world.setBlock(x0 + 15, y, z0 + 9, 0);
							world.setBlock(x0 + 15, y, z0 + 10, 0);
						}
					} else if (otherChunk.hasRoad && otherChunk.baseHeight > chunk.baseHeight) {
						// Draw stairs
						(new WorldGenStairs(3)).generate(world, rand, x0 + 13, y - 2, z0 + 5);
					}
				}
			}
			
			if (world.chunkExists(chunkX, chunkZ - 1)) {
				Chunk otherChunk = world.getChunkFromChunkCoords(chunkX, chunkZ - 1);
				if(otherChunk.hasRoad || otherChunk.hasBuilding) {
					if(otherChunk.baseHeight < chunk.baseHeight) {
						// Draw railing
						for(i = 0; i < 16; i ++) {
							world.setBlock(x0 + i, y, z0, Block.streetLanternFence.blockID);
							world.setBlock(x0 + i, y - 1, z0, Block.stairDouble.blockID);
						}
						// Hole in railing?
						if(otherChunk.hasRoad) {
							world.setBlock(x0 + 9, y, z0, 0);
							world.setBlock(x0 + 10, y, z0, 0);
						}
					} else if(otherChunk.hasRoad && otherChunk.baseHeight > chunk.baseHeight) {
						// Draw stairs
						(new WorldGenStairs(0)).generate(world, rand, x0 + 5, y - 2, z0);
					}
				}
			}
			
			if (world.chunkExists(chunkX, chunkZ + 1)) {
				Chunk otherChunk = world.getChunkFromChunkCoords(chunkX, chunkZ + 1);
				if (otherChunk.hasRoad || otherChunk.hasBuilding) {
					if(otherChunk.baseHeight < chunk.baseHeight) {
						// Draw railing
						for(i = 0; i < 16; i ++) {
							world.setBlock(x0 + i, y, z0 + 15, Block.streetLanternFence.blockID);
							world.setBlock(x0 + i, y - 1, z0 + 15, Block.stairDouble.blockID);
						}
						// Hole in railing?
						if(otherChunk.hasRoad) {
							world.setBlock(x0 + 5, y, z0 + 15, 0);
							world.setBlock(x0 + 6, y, z0 + 15, 0);
						}
					} else if(otherChunk.hasRoad && otherChunk.baseHeight > chunk.baseHeight) {
						// Draw stairs
						(new WorldGenStairs(2)).generate(world, rand, x0 + 5, y - 2, z0 + 13);
					}
				}
			}
			
			if(rand.nextBoolean()) for (i = 0; i < 8; i ++) {
				x = rand.nextInt(16);
				z = rand.nextInt(16);
				if (world.getBlockId(x0 + x, y - 1, z0 + z) == 0 && world.getBlockId(x0 + x, y - 2, z0 + z) != 0) {
					world.setBlock(x0 + x, y - 1, z0 + z, Block.woodenSpikes.blockID);
				}
			}
		}
		
		// Chest 
		if(chunk.hasBuilding) {
			if(chunk.chestY != -1) {
				world.setBlockWithNotify(x0 + chunk.chestX, chunk.chestY, z0 + chunk.chestZ, Block.chest.blockID);
				TileEntityChest tec = (TileEntityChest)world.getBlockTileEntity(x0 + chunk.chestX, chunk.chestY, z0 + chunk.chestZ);
		
				if(tec != null && tec.getSizeInventory() > 0) {
					int ni = rand.nextInt(4) + 1;
					int level = rand.nextInt(10);
					
					for(i = 0; i < ni; ++i) {
						ItemStack itemStack = this.getTreasure(level, rand);
						tec.setInventorySlotContents(i, itemStack);
					}
				}
			}
			
			// Spawner
			if(chunk.specialY != -1) {
				world.setBlockWithNotify(x0 + chunk.specialX, chunk.specialY, z0 + chunk.specialZ, Block.mobSpawner.blockID);
				TileEntityMobSpawner ms = (TileEntityMobSpawner)world.getBlockTileEntity(x0 + chunk.specialX, chunk.specialY, z0 + chunk.specialZ);
				if(ms != null) {
					ms.mobID = this.getMobID(rand);
				}
			}
		}
	}
	

	protected ItemStack getTreasure(int level, Random rand) {
		// level should be rand 0-9 so normal = 70%, middle = 20%, good = 10%.
		
		if(level < 7) {
			switch(rand.nextInt(6)) {
			case 0:
				return new ItemStack(Item.ingotIron, rand.nextInt(4) + 1);
			case 1:
				return new ItemStack(Item.bucketEmpty);
			case 2:
				return new ItemStack(Item.bread);
			case 3:
			case 5:
			default:
				return new ItemStack(Block.torchWood, rand.nextInt(16) + 1);
			case 4:
				return new ItemStack(Item.wheat, rand.nextInt(3) + 1);
			}
		} else if(level < 9) {
			switch(rand.nextInt(8)) {
			case 0:
			case 1:
			case 2:
				return this.getTreasure(1, rand);
			case 3:
			case 7:
			default:
				return new ItemStack(Item.bowlSoup);
			case 4:
				return new ItemStack(Item.ingotGold, rand.nextInt(6) + 1);
			case 5:
				return new ItemStack(Item.saddle);
			case 6:
				return new ItemStack(Item.dyePowder, rand.nextInt(10) + 1, rand.nextInt(16));
			}
		} else {
			switch(rand.nextInt(8)) {
			case 0:
			case 1:
			case 2:
				return this.getTreasure(2, rand);
			case 3:
			case 7:
			default:
				return new ItemStack(Item.diamond);
			case 4:
				return new ItemStack(Item.appleGold);
			case 5:
				return new ItemStack(Block.sponge);
			case 6:
				return new ItemStack(Item.saddle);
			}
		} 
	}
	
	public String getMobID(Random rand) {
		switch(rand.nextInt(11)) {
		case 0:
		case 1:
		case 2:
			return "SwarmSpider";
		case 3:
		case 4:
			return "Spider";
		case 5:
		case 6:
			return "Zombie";
		case 7:
		case 8:
		case 9:
			return "Skeleton";
		default:
			return "ElementalCreeper";
		}
	}
}

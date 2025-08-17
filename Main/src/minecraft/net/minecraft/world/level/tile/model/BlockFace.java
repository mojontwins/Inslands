package net.minecraft.world.level.tile.model;

import net.minecraft.client.renderer.util.TextureAtlasSize;

public class BlockFace {
	public BlockFaceUV uvs;
	public int textureIndex;
	public int facing;
	public int u0 = 0, v0 = 0;

	// I'm using this order so I can rotate vertical faces easily
	public static final int NORTH = 0;
	public static final int WEST= 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int DOWN = 4;
	public static final int UP = 5;
	
	public BlockFace(BlockFaceUV uvs, int textureIndex, int facing) {
		this.uvs = uvs;
		this.textureIndex = textureIndex;
		this.facing = facing;
		
		// Precalc this 
		if(textureIndex >= 0) {
			this.u0 = (textureIndex & 15) << 4;
			this.v0 = textureIndex & 0xff0;
		}
	}

	public String toString() {
		String s = "{";
		
		s += "'" + this.facing + "':" + this.uvs + " (" + this.textureIndex + ")";
		
		s += "}";
		return s;
	}
	
	public static int facing2Index(String facing) {
		if("down".equals(facing)) return DOWN;
		if("up".equals(facing)) return UP;
		if("east".equals(facing)) return EAST;
		if("west".equals(facing)) return WEST;
		if("north".equals(facing)) return NORTH;
		if("south".equals(facing)) return SOUTH;
		
		return 0;
	}
	
	public static int rotatedFace(int rotation, int face) {
		return (face - rotation) & 3;
	}

	public void overrideTexture(int textureIndex) {
		// Precalc this 
		this.u0 = (textureIndex & 15) << 4;
		this.v0 = textureIndex & 0xff0;
	}
	
	/* index is:
	 *   0: u1, v1
	 *   1: u1, v2
	 *   2: u2, v1
	 *   3: u2, v2
	 *   
	 *   0 2
	 *   1 3
	 */
	
	
	public double getRotatedU(int index) {
		return (this.u0 + this.uvs.getRotatedU(index)) / TextureAtlasSize.w;
	}
	
	public double getRotatedV(int index) {
		return (this.v0 + this.uvs.getRotatedV(index)) / TextureAtlasSize.h;
	}

	public static int vanillaFace(int faceIdx) {
		switch(faceIdx) {
		case DOWN: return 0;
		case UP: return 1;
		default:
		case EAST: return 2;
		case WEST: return 3;
		case NORTH: return 4;
		case SOUTH: return 5;
		}
	}
}

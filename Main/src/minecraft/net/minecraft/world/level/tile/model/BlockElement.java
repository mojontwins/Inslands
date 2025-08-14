package net.minecraft.world.level.tile.model;

import net.minecraft.src.Vec3D;

public class BlockElement {

	// We are precalculating from/to for all 4 rotation
	public Vec3D[] from = new Vec3D[4];
	public Vec3D[] to = new Vec3D[4];
	
	public BlockFace[] faces = new BlockFace[6];
	
	public void withFromTo(Vec3D from0, Vec3D to0) {
		// Unrolled
		
		// Angle = 0

		this.from[0] = new Vec3D(
				from0.xCoord,
				from0.yCoord,
				from0.zCoord
			);
		
		this.to[0] = new Vec3D(
				to0.xCoord,
				to0.yCoord,
				to0.zCoord
			);
		
		// Angle = 90º CW
		// x' = z, z' = 1 - x;
		
		this.from[1] = new Vec3D(
				from0.zCoord,
				from0.yCoord,
				1.0D - to0.xCoord
			);
		
		this.to[1] = new Vec3D(
				to0.zCoord,
				to0.yCoord,
				1.0D - from0.xCoord
			);
		
		// Angle = 180º CW
		// x' = 1 - x, z' = 1 - z
		
		this.from[2] = new Vec3D(
				1.0D - to0.xCoord,
				from0.yCoord,
				1.0D - to0.zCoord
			);
		
		this.to[2] = new Vec3D(
				1.0D - from0.xCoord,
				to0.yCoord,
				1.0D - from0.zCoord
			);
		
		// Angle = 270º CW
		// x' = 1 - z, z' = x
		
		this.from[3] = new Vec3D(
				1.0D - to0.zCoord,
				from0.yCoord,
				from0.xCoord
			);
		
		this.to[3] = new Vec3D(
				1.0D - from0.zCoord,
				to0.yCoord,
				to0.xCoord
			);
	}
	
	public void withFace(String faceName, BlockFace face) {
		this.faces[BlockFace.facing2Index(faceName)] = face;
	}

	public String toString() {
		return "[\nfrom: " + this.from[0] + ",\nto:" + this.to[0] + ",\nfaces=[\n" + this.listFaces() + "\n]";
	}

	private String listFaces() {
		String s = "";
		for(int i = 0; i < 6; i ++) {
			if(faces[i] != null) s += "\t"+faces[i].toString()+"\n";
		}
		return s;
	}

	public double getMinX(int angle) {
		return Math.min(this.from[angle].xCoord, this.to[angle].xCoord);
	}
	
	public double getMaxX(int angle) {
		return Math.max(this.from[angle].xCoord, this.to[angle].xCoord);
	}
	

	public double getMinZ(int angle) {
		return Math.min(this.from[angle].zCoord, this.to[angle].zCoord);
	}
	
	public double getMaxZ(int angle) {
		return Math.max(this.from[angle].zCoord, this.to[angle].zCoord);
	}
}

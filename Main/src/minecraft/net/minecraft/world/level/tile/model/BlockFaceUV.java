package net.minecraft.world.level.tile.model;

public class BlockFaceUV {
	
	public int[] uv; 		// u1 v1 u2 v2
	public int rotation; 	// 0=0º 1=90º 2=180º 3=270º

	public BlockFaceUV(int[] uv, int rotation) {
		this.uv = uv;
		this.rotation = rotation;
	}
	
	/*
	 * i is index 0-3, angle is 0-3
	 */
	public int rotate(int i, int angle) {
		return (i + angle) & 3;
	}
	
	/*
	 * index is 0 vertexindex (0 to 3), returns U for this vertex..
	 */
	public int getRotatedU(int index) {
		int i = this.rotate(index, this.rotation);
		return (i != 0 && i != 1) ? this.uv[2] : this.uv[0];
	}

	/*
	 * index is 0 vertexindex (0 to 3), returns U for this vertex..
	 */
	public int getRotatedV(int index) {
		int i = this.rotate(index, this.rotation);
		return (i != 0 && i != 3) ? this.uv[3] : this.uv[1];
	}
	
	public String toString() {
		return "(" + this.uv[0] + " " + this.uv[1] + " " + this.uv[2] + " " + this.uv[3] + " @ " +this.rotation + ")";
	}
}

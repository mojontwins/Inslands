package net.minecraft.util;

public class CoordXZ {
	public int x;
	public int z;
	
	public CoordXZ(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int distSqFrom(CoordXZ c) {
		int dx = this.x - c.x;
		int dz = this.z - c.z;
		
		return dx * dx + dz * dz;
	}
}

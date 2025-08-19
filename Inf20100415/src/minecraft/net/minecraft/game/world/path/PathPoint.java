package net.minecraft.game.world.path;

import util.MathHelper;

public final class PathPoint {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	public final int hash;
	int index = -1;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	PathPoint previous;
	public boolean isFirst = false;

	public PathPoint(int i1, int i2, int i3) {
		this.xCoord = i1;
		this.yCoord = i2;
		this.zCoord = i3;
		this.hash = i1 | i2 << 10 | i3 << 20;
	}

	public final float distanceTo(PathPoint pathPoint1) {
		float f2 = (float)(pathPoint1.xCoord - this.xCoord);
		float f3 = (float)(pathPoint1.yCoord - this.yCoord);
		float f4 = (float)(pathPoint1.zCoord - this.zCoord);
		return MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
	}

	public final boolean equals(Object object1) {
		return ((PathPoint)object1).hash == this.hash;
	}

	public final int hashCode() {
		return this.hash;
	}

	public final boolean isAssigned() {
		return this.index >= 0;
	}

	public final String toString() {
		return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
	}
}
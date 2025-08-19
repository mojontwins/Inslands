package net.minecraft.game.physics;

import util.MathHelper;

public final class Vec3D {
	public double xCoord;
	public double yCoord;
	public double zCoord;

	public Vec3D(double d1, double d3, double d5) {
		this.xCoord = d1;
		this.yCoord = d3;
		this.zCoord = d5;
	}

	public final Vec3D subtract(Vec3D vec3D1) {
		return new Vec3D(this.xCoord - vec3D1.xCoord, this.yCoord - vec3D1.yCoord, this.zCoord - vec3D1.zCoord);
	}

	public final Vec3D normalize() {
		double d1 = (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
		return new Vec3D(this.xCoord / d1, this.yCoord / d1, this.zCoord / d1);
	}

	public final Vec3D addVector(double d1, double d3, double d5) {
		return new Vec3D(this.xCoord + d1, this.yCoord + d3, this.zCoord + d5);
	}

	public final double distance(Vec3D vec3D1) {
		double d2 = vec3D1.xCoord - this.xCoord;
		double d4 = vec3D1.yCoord - this.yCoord;
		double d6 = vec3D1.zCoord - this.zCoord;
		return (double)MathHelper.sqrt_double(d2 * d2 + d4 * d4 + d6 * d6);
	}

	public final double squareDistanceTo(Vec3D vec3D1) {
		double d2 = vec3D1.xCoord - this.xCoord;
		double d4 = vec3D1.yCoord - this.yCoord;
		double d6 = vec3D1.zCoord - this.zCoord;
		return d2 * d2 + d4 * d4 + d6 * d6;
	}

	public final Vec3D getIntermediateWithXValue(Vec3D vec3D1, double d2) {
		double d4 = vec3D1.xCoord - this.xCoord;
		double d6 = vec3D1.yCoord - this.yCoord;
		double d8 = vec3D1.zCoord - this.zCoord;
		double d10;
		return d4 * d4 < 1.0000000116860974E-7D ? null : ((d10 = (d2 - this.xCoord) / d4) >= 0.0D && d10 <= 1.0D ? new Vec3D(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
	}

	public final Vec3D getIntermediateWithYValue(Vec3D vec3D1, double d2) {
		double d4 = vec3D1.xCoord - this.xCoord;
		double d6 = vec3D1.yCoord - this.yCoord;
		double d8 = vec3D1.zCoord - this.zCoord;
		double d10;
		return d6 * d6 < 1.0000000116860974E-7D ? null : ((d10 = (d2 - this.yCoord) / d6) >= 0.0D && d10 <= 1.0D ? new Vec3D(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
	}

	public final Vec3D getIntermediateWithZValue(Vec3D vec3D1, double d2) {
		double d4 = vec3D1.xCoord - this.xCoord;
		double d6 = vec3D1.yCoord - this.yCoord;
		double d8;
		double d10;
		return (d8 = vec3D1.zCoord - this.zCoord) * d8 < 1.0000000116860974E-7D ? null : ((d10 = (d2 - this.zCoord) / d8) >= 0.0D && d10 <= 1.0D ? new Vec3D(this.xCoord + d4 * d10, this.yCoord + d6 * d10, this.zCoord + d8 * d10) : null);
	}

	public final String toString() {
		return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
	}
}
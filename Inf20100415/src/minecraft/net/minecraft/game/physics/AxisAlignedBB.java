package net.minecraft.game.physics;

public final class AxisAlignedBB {
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;

	public AxisAlignedBB(double d1, double d3, double d5, double d7, double d9, double d11) {
		this.minX = d1;
		this.minY = d3;
		this.minZ = d5;
		this.maxX = d7;
		this.maxY = d9;
		this.maxZ = d11;
	}

	public final AxisAlignedBB addCoord(double d1, double d3, double d5) {
		double d7 = this.minX;
		double d9 = this.minY;
		double d11 = this.minZ;
		double d13 = this.maxX;
		double d15 = this.maxY;
		double d17 = this.maxZ;
		if(d1 < 0.0D) {
			d7 += d1;
		}

		if(d1 > 0.0D) {
			d13 += d1;
		}

		if(d3 < 0.0D) {
			d9 += d3;
		}

		if(d3 > 0.0D) {
			d15 += d3;
		}

		if(d5 < 0.0D) {
			d11 += d5;
		}

		if(d5 > 0.0D) {
			d17 += d5;
		}

		return new AxisAlignedBB(d7, d9, d11, d13, d15, d17);
	}

	public final AxisAlignedBB expand(double d1, double d3, double d5) {
		if(this.minY > this.maxY) {
			throw new IllegalArgumentException("NOOOOOO!");
		} else {
			double d7 = this.minX - d1;
			double d9 = this.minY - d3;
			double d11 = this.minZ - d5;
			double d13 = this.maxX + d1;
			double d15 = this.maxY + d3;
			double d17 = this.maxZ + d5;
			return new AxisAlignedBB(d7, d9, d11, d13, d15, d17);
		}
	}

	public final AxisAlignedBB offsetCopy(double d1, double d3, double d5) {
		return new AxisAlignedBB(this.minX + d1, this.minY + d3, this.minZ + d5, this.maxX + d1, this.maxY + d3, this.maxZ + d5);
	}

	public final double calculateXOffset(AxisAlignedBB axisAlignedBB1, double d2) {
		if(axisAlignedBB1.maxY > this.minY && axisAlignedBB1.minY < this.maxY) {
			if(axisAlignedBB1.maxZ > this.minZ && axisAlignedBB1.minZ < this.maxZ) {
				double d4;
				if(d2 > 0.0D && axisAlignedBB1.maxX <= this.minX && (d4 = this.minX - axisAlignedBB1.maxX) < d2) {
					d2 = d4;
				}

				if(d2 < 0.0D && axisAlignedBB1.minX >= this.maxX && (d4 = this.maxX - axisAlignedBB1.minX) > d2) {
					d2 = d4;
				}

				return d2;
			} else {
				return d2;
			}
		} else {
			return d2;
		}
	}

	public final double calculateYOffset(AxisAlignedBB axisAlignedBB1, double d2) {
		if(axisAlignedBB1.maxX > this.minX && axisAlignedBB1.minX < this.maxX) {
			if(axisAlignedBB1.maxZ > this.minZ && axisAlignedBB1.minZ < this.maxZ) {
				double d4;
				if(d2 > 0.0D && axisAlignedBB1.maxY <= this.minY && (d4 = this.minY - axisAlignedBB1.maxY) < d2) {
					d2 = d4;
				}

				if(d2 < 0.0D && axisAlignedBB1.minY >= this.maxY && (d4 = this.maxY - axisAlignedBB1.minY) > d2) {
					d2 = d4;
				}

				return d2;
			} else {
				return d2;
			}
		} else {
			return d2;
		}
	}

	public final double calculateZOffset(AxisAlignedBB axisAlignedBB1, double d2) {
		if(axisAlignedBB1.maxX > this.minX && axisAlignedBB1.minX < this.maxX) {
			if(axisAlignedBB1.maxY > this.minY && axisAlignedBB1.minY < this.maxY) {
				double d4;
				if(d2 > 0.0D && axisAlignedBB1.maxZ <= this.minZ && (d4 = this.minZ - axisAlignedBB1.maxZ) < d2) {
					d2 = d4;
				}

				if(d2 < 0.0D && axisAlignedBB1.minZ >= this.maxZ && (d4 = this.maxZ - axisAlignedBB1.minZ) > d2) {
					d2 = d4;
				}

				return d2;
			} else {
				return d2;
			}
		} else {
			return d2;
		}
	}

	public final boolean intersectsWith(AxisAlignedBB axisAlignedBB1) {
		return axisAlignedBB1.maxX > this.minX && axisAlignedBB1.minX < this.maxX ? (axisAlignedBB1.maxY > this.minY && axisAlignedBB1.minY < this.maxY ? axisAlignedBB1.maxZ > this.minZ && axisAlignedBB1.minZ < this.maxZ : false) : false;
	}

	public final void offset(double d1, double d3, double d5) {
		this.minX += d1;
		this.minY += d3;
		this.minZ += d5;
		this.maxX += d1;
		this.maxY += d3;
		this.maxZ += d5;
	}

	public final AxisAlignedBB copy() {
		return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public final MovingObjectPosition calculateIntercept(Vec3D vec3D1, Vec3D vec3D2) {
		Vec3D vec3D3 = vec3D1.getIntermediateWithXValue(vec3D2, this.minX);
		Vec3D vec3D4 = vec3D1.getIntermediateWithXValue(vec3D2, this.maxX);
		Vec3D vec3D5 = vec3D1.getIntermediateWithYValue(vec3D2, this.minY);
		Vec3D vec3D6 = vec3D1.getIntermediateWithYValue(vec3D2, this.maxY);
		Vec3D vec3D7 = vec3D1.getIntermediateWithZValue(vec3D2, this.minZ);
		vec3D2 = vec3D1.getIntermediateWithZValue(vec3D2, this.maxZ);
		if(!this.isVecInYZ(vec3D3)) {
			vec3D3 = null;
		}

		if(!this.isVecInYZ(vec3D4)) {
			vec3D4 = null;
		}

		if(!this.isVecInXZ(vec3D5)) {
			vec3D5 = null;
		}

		if(!this.isVecInXZ(vec3D6)) {
			vec3D6 = null;
		}

		if(!this.isVecInXY(vec3D7)) {
			vec3D7 = null;
		}

		if(!this.isVecInXY(vec3D2)) {
			vec3D2 = null;
		}

		Vec3D vec3D8 = null;
		if(vec3D3 != null) {
			vec3D8 = vec3D3;
		}

		if(vec3D4 != null && (vec3D8 == null || vec3D1.squareDistanceTo(vec3D4) < vec3D1.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D4;
		}

		if(vec3D5 != null && (vec3D8 == null || vec3D1.squareDistanceTo(vec3D5) < vec3D1.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D5;
		}

		if(vec3D6 != null && (vec3D8 == null || vec3D1.squareDistanceTo(vec3D6) < vec3D1.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D6;
		}

		if(vec3D7 != null && (vec3D8 == null || vec3D1.squareDistanceTo(vec3D7) < vec3D1.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D7;
		}

		if(vec3D2 != null && (vec3D8 == null || vec3D1.squareDistanceTo(vec3D2) < vec3D1.squareDistanceTo(vec3D8))) {
			vec3D8 = vec3D2;
		}

		if(vec3D8 == null) {
			return null;
		} else {
			byte b9 = -1;
			if(vec3D8 == vec3D3) {
				b9 = 4;
			}

			if(vec3D8 == vec3D4) {
				b9 = 5;
			}

			if(vec3D8 == vec3D5) {
				b9 = 0;
			}

			if(vec3D8 == vec3D6) {
				b9 = 1;
			}

			if(vec3D8 == vec3D7) {
				b9 = 2;
			}

			if(vec3D8 == vec3D2) {
				b9 = 3;
			}

			return new MovingObjectPosition(0, 0, 0, b9, vec3D8);
		}
	}

	private boolean isVecInYZ(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.yCoord >= this.minY && vec3D1.yCoord <= this.maxY && vec3D1.zCoord >= this.minZ && vec3D1.zCoord <= this.maxZ;
	}

	private boolean isVecInXZ(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.minX && vec3D1.xCoord <= this.maxX && vec3D1.zCoord >= this.minZ && vec3D1.zCoord <= this.maxZ;
	}

	private boolean isVecInXY(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.minX && vec3D1.xCoord <= this.maxX && vec3D1.yCoord >= this.minY && vec3D1.yCoord <= this.maxY;
	}
}
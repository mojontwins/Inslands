package net.minecraft.game.world.path;

public final class Path {
	private PathPoint[] pathpoints = new PathPoint[1024];
	private int count = 0;

	public final PathPoint addPoint(PathPoint pathPoint1) {
		if(pathPoint1.index >= 0) {
			throw new IllegalStateException("OW KNOWS!");
		} else {
			if(this.count == this.pathpoints.length) {
				PathPoint[] pathPoint2 = new PathPoint[this.count << 1];
				System.arraycopy(this.pathpoints, 0, pathPoint2, 0, this.count);
				this.pathpoints = pathPoint2;
			}

			this.pathpoints[this.count] = pathPoint1;
			pathPoint1.index = this.count;
			this.sortBack(this.count++);
			return pathPoint1;
		}
	}

	public final void clearPath() {
		this.count = 0;
	}

	public final PathPoint dequeue() {
		PathPoint pathPoint1 = this.pathpoints[0];
		this.pathpoints[0] = this.pathpoints[--this.count];
		this.pathpoints[this.count] = null;
		if(this.count > 0) {
			this.sortForward(0);
		}

		pathPoint1.index = -1;
		return pathPoint1;
	}

	public final void changeDistance(PathPoint pathPoint1, float f2) {
		float f3 = pathPoint1.distanceToTarget;
		pathPoint1.distanceToTarget = f2;
		if(f2 < f3) {
			this.sortBack(pathPoint1.index);
		} else {
			this.sortForward(pathPoint1.index);
		}
	}

	private void sortBack(int i1) {
		PathPoint pathPoint2;
		int i4;
		for(float f3 = (pathPoint2 = this.pathpoints[i1]).distanceToTarget; i1 > 0; i1 = i4) {
			i4 = i1 - 1 >> 1;
			PathPoint pathPoint5 = this.pathpoints[i4];
			if(f3 >= pathPoint5.distanceToTarget) {
				break;
			}

			this.pathpoints[i1] = pathPoint5;
			pathPoint5.index = i1;
		}

		this.pathpoints[i1] = pathPoint2;
		pathPoint2.index = i1;
	}

	private void sortForward(int i1) {
		PathPoint pathPoint2;
		float f3 = (pathPoint2 = this.pathpoints[i1]).distanceToTarget;

		while(true) {
			int i4;
			int i5 = (i4 = 1 + (i1 << 1)) + 1;
			if(i4 >= this.count) {
				break;
			}

			PathPoint pathPoint6;
			float f7 = (pathPoint6 = this.pathpoints[i4]).distanceToTarget;
			PathPoint pathPoint8;
			float f9;
			if(i5 >= this.count) {
				pathPoint8 = null;
				f9 = Float.POSITIVE_INFINITY;
			} else {
				f9 = (pathPoint8 = this.pathpoints[i5]).distanceToTarget;
			}

			if(f7 < f9) {
				if(f7 >= f3) {
					break;
				}

				this.pathpoints[i1] = pathPoint6;
				pathPoint6.index = i1;
				i1 = i4;
			} else {
				if(f9 >= f3) {
					break;
				}

				this.pathpoints[i1] = pathPoint8;
				pathPoint8.index = i1;
				i1 = i5;
			}
		}

		this.pathpoints[i1] = pathPoint2;
		pathPoint2.index = i1;
	}

	public final boolean isPathEmpty() {
		return this.count == 0;
	}
}
package net.minecraft.game.world.path;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class Pathfinder {
	private World worldMap;
	private Path path = new Path();
	private Map pointMap = new HashMap();
	private PathPoint[] pathOptions = new PathPoint[32];

	public Pathfinder(World world1) {
		this.worldMap = world1;
	}

	public final PathEntity createEntityPathTo(Entity entity1, Entity entity2, float f3) {
		return this.addToPath(entity1, entity2.posX, entity2.boundingBox.minY, entity2.posZ, 16.0F);
	}

	public final PathEntity createEntityPathToXYZ(Entity entity1, int i2, int i3, int i4, float f5) {
		return this.addToPath(entity1, (double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), 16.0F);
	}

	private PathEntity addToPath(Entity entity1, double d2, double d4, double d6, float f8) {
		this.path.clearPath();
		this.pointMap.clear();
		PathPoint object9 = this.openPoint((int)entity1.boundingBox.minX, (int)entity1.boundingBox.minY, (int)entity1.boundingBox.minZ);
		PathPoint pathPoint21 = this.openPoint((int)(d2 - (double)(entity1.width / 2.0F)), (int)d4, (int)(d6 - (double)(entity1.width / 2.0F)));
		PathPoint pathPoint3 = new PathPoint((int)(entity1.width + 1.0F), (int)(entity1.height + 1.0F), (int)(entity1.width + 1.0F));
		float f24 = f8;
		PathPoint pathPoint5 = pathPoint3;
		PathPoint pathPoint23 = pathPoint21;
		Entity entity22 = entity1;
		Pathfinder pathfinder20 = this;
		((PathPoint)object9).totalPathDistance = 0.0F;
		((PathPoint)object9).distanceToNext = ((PathPoint)object9).distanceTo(pathPoint21);
		((PathPoint)object9).distanceToTarget = ((PathPoint)object9).distanceToNext;
		this.path.clearPath();
		this.path.addPoint((PathPoint)object9);
		Object object7 = object9;

		PathEntity pathEntity10000;
		while(true) {
			if(pathfinder20.path.isPathEmpty()) {
				pathEntity10000 = object7 == object9 ? null : createEntityPath((PathPoint)object7);
				break;
			}

			PathPoint pathPoint25;
			if((pathPoint25 = pathfinder20.path.dequeue()).hash == pathPoint23.hash) {
				pathEntity10000 = createEntityPath(pathPoint23);
				break;
			}

			if(pathPoint25.distanceTo(pathPoint23) < ((PathPoint)object7).distanceTo(pathPoint23)) {
				object7 = pathPoint25;
			}

			pathPoint25.isFirst = true;
			int object15 = 0;
			byte b16 = 0;
			if(pathfinder20.getVerticalOffset(pathPoint25.xCoord, pathPoint25.yCoord + 1, pathPoint25.zCoord, pathPoint5) > 0) {
				b16 = 1;
			}

			PathPoint pathPoint17 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord, pathPoint25.yCoord, pathPoint25.zCoord + 1, pathPoint5, b16);
			PathPoint pathPoint18 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord - 1, pathPoint25.yCoord, pathPoint25.zCoord, pathPoint5, b16);
			PathPoint pathPoint19 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord + 1, pathPoint25.yCoord, pathPoint25.zCoord, pathPoint5, b16);
			PathPoint pathPoint10 = pathfinder20.getSafePoint(entity22, pathPoint25.xCoord, pathPoint25.yCoord, pathPoint25.zCoord - 1, pathPoint5, b16);
			if(pathPoint17 != null && !pathPoint17.isFirst && pathPoint17.distanceTo(pathPoint23) < f24) {
				++object15;
				pathfinder20.pathOptions[0] = pathPoint17;
			}

			if(pathPoint18 != null && !pathPoint18.isFirst && pathPoint18.distanceTo(pathPoint23) < f24) {
				pathfinder20.pathOptions[object15++] = pathPoint18;
			}

			if(pathPoint19 != null && !pathPoint19.isFirst && pathPoint19.distanceTo(pathPoint23) < f24) {
				pathfinder20.pathOptions[object15++] = pathPoint19;
			}

			if(pathPoint10 != null && !pathPoint10.isFirst && pathPoint10.distanceTo(pathPoint23) < f24) {
				pathfinder20.pathOptions[object15++] = pathPoint10;
			}

			for(int i26 = 0; i26 < object15; ++i26) {
				PathPoint pathPoint11 = pathfinder20.pathOptions[i26];
				float f12 = pathPoint25.totalPathDistance + pathPoint25.distanceTo(pathPoint11);
				if(!pathPoint11.isAssigned() || f12 < pathPoint11.totalPathDistance) {
					pathPoint11.previous = pathPoint25;
					pathPoint11.totalPathDistance = f12;
					pathPoint11.distanceToNext = pathPoint11.distanceTo(pathPoint23);
					if(pathPoint11.isAssigned()) {
						pathfinder20.path.changeDistance(pathPoint11, pathPoint11.totalPathDistance + pathPoint11.distanceToNext);
					} else {
						pathPoint11.distanceToTarget = pathPoint11.totalPathDistance + pathPoint11.distanceToNext;
						pathfinder20.path.addPoint(pathPoint11);
					}
				}
			}
		}

		return pathEntity10000;
	}

	private PathPoint getSafePoint(Entity entity1, int i2, int i3, int i4, PathPoint pathPoint5, int i6) {
		PathPoint pathPoint8 = null;
		if(this.getVerticalOffset(i2, i3, i4, pathPoint5) > 0) {
			pathPoint8 = this.openPoint(i2, i3, i4);
		}

		if(pathPoint8 == null && this.getVerticalOffset(i2, i3 + i6, i4, pathPoint5) > 0) {
			pathPoint8 = this.openPoint(i2, i3 + i6, i4);
		}

		if(pathPoint8 != null) {
			i6 = 0;

			while(true) {
				int i7;
				if(i3 <= 0 || (i7 = this.getVerticalOffset(i2, i3 - 1, i4, pathPoint5)) <= 0) {
					Material material9;
					if((material9 = this.worldMap.getBlockMaterial(i2, i3 - 1, i4)) == Material.water || material9 == Material.lava) {
						return null;
					}
					break;
				}

				if(i7 < 0) {
					return null;
				}

				++i6;
				if(i6 >= 4) {
					return null;
				}

				--i3;
				pathPoint8 = this.openPoint(i2, i3, i4);
			}
		}

		return pathPoint8;
	}

	private final PathPoint openPoint(int i1, int i2, int i3) {
		int i4 = i1 | i2 << 10 | i3 << 20;
		PathPoint pathPoint5;
		if((pathPoint5 = (PathPoint)this.pointMap.get(i4)) == null) {
			pathPoint5 = new PathPoint(i1, i2, i3);
			this.pointMap.put(i4, pathPoint5);
		}

		return pathPoint5;
	}

	private int getVerticalOffset(int i1, int i2, int i3, PathPoint pathPoint4) {
		for(int i5 = i1; i5 < i1 + pathPoint4.xCoord; ++i5) {
			for(int i6 = i2; i6 < i2 + pathPoint4.yCoord; ++i6) {
				for(int i7 = i3; i7 < i3 + pathPoint4.zCoord; ++i7) {
					Material material8;
					if((material8 = this.worldMap.getBlockMaterial(i1, i2, i3)).getIsSolid()) {
						return 0;
					}

					if(material8 == Material.water || material8 == Material.lava) {
						return -1;
					}
				}
			}
		}

		return 1;
	}

	private static PathEntity createEntityPath(PathPoint pathPoint0) {
		int i1 = 1;

		PathPoint pathPoint2;
		for(pathPoint2 = pathPoint0; pathPoint2.previous != null; pathPoint2 = pathPoint2.previous) {
			++i1;
		}

		PathPoint[] pathPoint3 = new PathPoint[i1];
		pathPoint2 = pathPoint0;
		--i1;

		for(pathPoint3[i1] = pathPoint0; pathPoint2.previous != null; pathPoint3[i1] = pathPoint2) {
			pathPoint2 = pathPoint2.previous;
			--i1;
		}

		return new PathEntity(pathPoint3);
	}
}

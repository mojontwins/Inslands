package net.minecraft.world.entity.ai;

import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.phys.Vec3D;

public class RandomPositionGenerator {
	private static Vec3D destVector = Vec3D.createVectorHelper(0.0D, 0.0D, 0.0D);

	public static Vec3D findRandomTarget(EntityCreature entityCreature, int maxX, int maxZ) {
		return findRandomTargetBlock(entityCreature, maxX, maxZ, null);
	}

	public static Vec3D findRandomTargetBlockTowards(EntityCreature entityCreature, int maxX, int maxZ, Vec3D target) {
		destVector.xCoord = target.xCoord - entityCreature.posX;
		destVector.yCoord = target.yCoord - entityCreature.posY;
		destVector.zCoord = target.zCoord - entityCreature.posZ;
		return findRandomTargetBlock(entityCreature, maxX, maxZ, destVector);
	}

	public static Vec3D findRandomTargetBlockAwayFrom(EntityCreature entityCreature, int maxX, int maxZ, Vec3D target) {
		destVector.xCoord = entityCreature.posX - target.xCoord;
		destVector.yCoord = entityCreature.posY - target.yCoord;
		destVector.zCoord = entityCreature.posZ - target.zCoord;
		return findRandomTargetBlock(entityCreature, maxX, maxZ, destVector);
	}

	private static Vec3D findRandomTargetBlock(EntityCreature entityCreature, int maxX, int maxZ, Vec3D targetDirection) {
		Random random = entityCreature.rand;
		boolean foundTarget = false;
		int bestX = 0;
		int bestY = 0;
		int bestZ = 0;
		float bestWeight = -99999.0F;
		boolean hasHome;
		/*if(entityCreature.hasHome()) {
			double homeDistance = entityCreature.getHomePosition().getEuclideanDistanceTo(MathHelper.floor_double(entityCreature.posX), MathHelper.floor_double(entityCreature.posY), MathHelper.floor_double(entityCreature.posZ)) + 4.0D;
			hasHome = homeDistance < (double)(entityCreature.getMaximumHomeDistance() + (float)maxX);
		} else*/ {
			hasHome = false;
		}

		for (int attempt = 0; attempt < 10; ++attempt) {
			int x = random.nextInt(2 * maxX) - maxX;
			int y = random.nextInt(2 * maxZ) - maxZ;
			int z = random.nextInt(2 * maxX) - maxX;
			if (targetDirection == null || (double) x * targetDirection.xCoord + (double) z * targetDirection.zCoord >= 0.0D) {
				x += MathHelper.floor_double(entityCreature.posX);
				y += MathHelper.floor_double(entityCreature.posY);
				z += MathHelper.floor_double(entityCreature.posZ);
				if (!hasHome/* || entityCreature.isWithinHomeDistance(x, y, z)*/) {
					float weight = entityCreature.getBlockPathWeight(x, y, z);
					if (weight > bestWeight) {
						bestWeight = weight;
						bestX = x;
						bestY = y;
						bestZ = z;
						foundTarget = true;
					}
				}
			}
		}

		if (foundTarget) {
			return Vec3D.createVector((double) bestX, (double) bestY, (double) bestZ);
		} else {
			return null;
		}
	}
}

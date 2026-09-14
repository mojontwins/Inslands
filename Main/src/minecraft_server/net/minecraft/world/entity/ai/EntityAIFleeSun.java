package net.minecraft.world.entity.ai;

import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

public class EntityAIFleeSun extends EntityAIBase {
	private EntityCreature theCreature;
	private double shelterX;
	private double shelterY;
	private double shelterZ;
	private float movementSpeed;
	private World theWorld;

	public EntityAIFleeSun(EntityCreature entityCreature, float speed) {
		this.theCreature = entityCreature;
		this.movementSpeed = speed;
		this.theWorld = entityCreature.worldObj;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if (!this.theWorld.isDaytime()) {
			return false;
		} else if (!this.theCreature.isBurning()) {
			return false;
		} else if (!this.theWorld.canBlockSeeTheSky(MathHelper.floor_double(this.theCreature.posX), (int) this.theCreature.boundingBox.minY, MathHelper.floor_double(this.theCreature.posZ))) {
			return false;
		} else {
			Vec3D shelter = this.findPossibleShelter();
			if (shelter == null) {
				return false;
			} else {
				this.shelterX = shelter.xCoord;
				this.shelterY = shelter.yCoord;
				this.shelterZ = shelter.zCoord;
				return true;
			}
		}
	}

	public boolean continueExecuting() {
		return !this.theCreature.getNavigator().noPath();
	}

	public void startExecuting() {
		this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
	}

	private Vec3D findPossibleShelter() {
		Random random = this.theCreature.getRNG();

		for (int attempt = 0; attempt < 10; ++attempt) {
			int x = MathHelper.floor_double(this.theCreature.posX + (double) random.nextInt(20) - 10.0D);
			int y = MathHelper.floor_double(this.theCreature.boundingBox.minY + (double) random.nextInt(6) - 3.0D);
			int z = MathHelper.floor_double(this.theCreature.posZ + (double) random.nextInt(20) - 10.0D);
			if (!this.theWorld.canBlockSeeTheSky(x, y, z) && this.theCreature.getBlockPathWeight(x, y, z) < 0.0F) {
				return Vec3D.createVector((double) x, (double) y, (double) z);
			}
		}

		return null;
	}
}

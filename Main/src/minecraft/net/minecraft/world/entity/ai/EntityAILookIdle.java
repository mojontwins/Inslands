package net.minecraft.world.entity.ai;

import net.minecraft.world.entity.EntityLiving;

public class EntityAILookIdle extends EntityAIBase {
	private EntityLiving idleEntity;
	private double lookX;
	private double lookZ;
	private int idleTime = 0;

	public EntityAILookIdle(EntityLiving entityLiving) {
		this.idleEntity = entityLiving;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		return this.idleEntity.getRNG().nextFloat() < 0.02F;
	}

	public boolean continueExecuting() {
		return this.idleTime >= 0;
	}

	public void startExecuting() {
		double theta = Math.PI * 2D * this.idleEntity.getRNG().nextDouble();
		this.lookX = Math.cos(theta);
		this.lookZ = Math.sin(theta);
		this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
	}

	public void updateTask() {
		--this.idleTime;
		this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + (double) this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0F, (float) this.idleEntity.getVerticalFaceSpeed());
	}
}

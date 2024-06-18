package net.minecraft.src;

public class EntityAIWatchClosest extends EntityAIBase {
	private EntityLiving field_46110_a;
	private Entity closestEntity;
	private float field_46106_d;
	private int field_46107_e;
	private float field_48241_e;
	private Class field_48240_f;

	public EntityAIWatchClosest(EntityLiving entityLiving1, Class class2, float f3) {
		this.field_46110_a = entityLiving1;
		this.field_48240_f = class2;
		this.field_46106_d = f3;
		this.field_48241_e = 0.02F;
		this.setMutexBits(2);
	}

	public EntityAIWatchClosest(EntityLiving entityLiving1, Class class2, float f3, float f4) {
		this.field_46110_a = entityLiving1;
		this.field_48240_f = class2;
		this.field_46106_d = f3;
		this.field_48241_e = f4;
		this.setMutexBits(2);
	}

	public boolean shouldExecute() {
		if(this.field_46110_a.getRNG().nextFloat() >= this.field_48241_e) {
			return false;
		} else {
			if(this.field_48240_f == EntityPlayer.class) {
				this.closestEntity = this.field_46110_a.worldObj.getClosestPlayerToEntity(this.field_46110_a, (double)this.field_46106_d);
			} else {
				this.closestEntity = this.field_46110_a.worldObj.findNearestEntityWithinAABB(this.field_48240_f, this.field_46110_a.boundingBox.expand((double)this.field_46106_d, 3.0D, (double)this.field_46106_d), this.field_46110_a);
			}

			return this.closestEntity != null;
		}
	}

	public boolean continueExecuting() {
		return !this.closestEntity.isEntityAlive() ? false : (this.field_46110_a.getDistanceSqToEntity(this.closestEntity) > (double)(this.field_46106_d * this.field_46106_d) ? false : this.field_46107_e > 0);
	}

	public void startExecuting() {
		this.field_46107_e = 40 + this.field_46110_a.getRNG().nextInt(40);
	}

	public void resetTask() {
		this.closestEntity = null;
	}

	public void updateTask() {
		this.field_46110_a.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + (double)this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0F, (float)this.field_46110_a.getVerticalFaceSpeed());
		--this.field_46107_e;
	}
}

package net.minecraft.src;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
	EntityTameable field_48294_a;
	EntityLiving field_48293_b;

	public EntityAIOwnerHurtByTarget(EntityTameable entityTameable1) {
		super(entityTameable1, 32.0F, false);
		this.field_48294_a = entityTameable1;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(!this.field_48294_a.isTamed()) {
			return false;
		} else {
			EntityLiving entityLiving1 = this.field_48294_a.getOwner();
			if(entityLiving1 == null) {
				return false;
			} else {
				this.field_48293_b = entityLiving1.getAITarget();
				return this.func_48284_a(this.field_48293_b, false);
			}
		}
	}

	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.field_48293_b);
		super.startExecuting();
	}
}

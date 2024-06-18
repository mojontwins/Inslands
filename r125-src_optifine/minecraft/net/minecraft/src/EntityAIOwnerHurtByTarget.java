package net.minecraft.src;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
	EntityTameable field_48394_a;
	EntityLiving field_48393_b;

	public EntityAIOwnerHurtByTarget(EntityTameable entityTameable1) {
		super(entityTameable1, 32.0F, false);
		this.field_48394_a = entityTameable1;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(!this.field_48394_a.isTamed()) {
			return false;
		} else {
			EntityLiving entityLiving1 = this.field_48394_a.getOwner();
			if(entityLiving1 == null) {
				return false;
			} else {
				this.field_48393_b = entityLiving1.getAITarget();
				return this.func_48376_a(this.field_48393_b, false);
			}
		}
	}

	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.field_48393_b);
		super.startExecuting();
	}
}

package net.minecraft.src;

public class EntityAIOwnerHurtTarget extends EntityAITarget {
	EntityTameable field_48392_a;
	EntityLiving field_48391_b;

	public EntityAIOwnerHurtTarget(EntityTameable entityTameable1) {
		super(entityTameable1, 32.0F, false);
		this.field_48392_a = entityTameable1;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(!this.field_48392_a.isTamed()) {
			return false;
		} else {
			EntityLiving entityLiving1 = this.field_48392_a.getOwner();
			if(entityLiving1 == null) {
				return false;
			} else {
				this.field_48391_b = entityLiving1.getLastAttackingEntity();
				return this.func_48376_a(this.field_48391_b, false);
			}
		}
	}

	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.field_48391_b);
		super.startExecuting();
	}
}

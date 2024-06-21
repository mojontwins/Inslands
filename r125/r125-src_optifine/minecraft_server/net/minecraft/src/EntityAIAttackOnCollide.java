package net.minecraft.src;

public class EntityAIAttackOnCollide extends EntityAIBase {
	World worldObj;
	EntityLiving field_48156_b;
	EntityLiving entityTarget;
	int field_46095_d;
	float field_48155_e;
	boolean field_48153_f;
	PathEntity field_48154_g;
	Class field_48157_h;
	private int field_48158_i;

	public EntityAIAttackOnCollide(EntityLiving entityLiving1, Class class2, float f3, boolean z4) {
		this(entityLiving1, f3, z4);
		this.field_48157_h = class2;
	}

	public EntityAIAttackOnCollide(EntityLiving entityLiving1, float f2, boolean z3) {
		this.field_46095_d = 0;
		this.field_48156_b = entityLiving1;
		this.worldObj = entityLiving1.worldObj;
		this.field_48155_e = f2;
		this.field_48153_f = z3;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		EntityLiving entityLiving1 = this.field_48156_b.getAttackTarget();
		if(entityLiving1 == null) {
			return false;
		} else if(this.field_48157_h != null && !this.field_48157_h.isAssignableFrom(entityLiving1.getClass())) {
			return false;
		} else {
			this.entityTarget = entityLiving1;
			this.field_48154_g = this.field_48156_b.getNavigator().func_48661_a(this.entityTarget);
			return this.field_48154_g != null;
		}
	}

	public boolean continueExecuting() {
		EntityLiving entityLiving1 = this.field_48156_b.getAttackTarget();
		return entityLiving1 == null ? false : (!this.entityTarget.isEntityAlive() ? false : (!this.field_48153_f ? !this.field_48156_b.getNavigator().noPath() : this.field_48156_b.isWithinHomeDistance(MathHelper.floor_double(this.entityTarget.posX), MathHelper.floor_double(this.entityTarget.posY), MathHelper.floor_double(this.entityTarget.posZ))));
	}

	public void startExecuting() {
		this.field_48156_b.getNavigator().setPath(this.field_48154_g, this.field_48155_e);
		this.field_48158_i = 0;
	}

	public void resetTask() {
		this.entityTarget = null;
		this.field_48156_b.getNavigator().clearPathEntity();
	}

	public void updateTask() {
		this.field_48156_b.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
		if((this.field_48153_f || this.field_48156_b.func_48318_al().canSee(this.entityTarget)) && --this.field_48158_i <= 0) {
			this.field_48158_i = 4 + this.field_48156_b.getRNG().nextInt(7);
			this.field_48156_b.getNavigator().func_48652_a(this.entityTarget, this.field_48155_e);
		}

		this.field_46095_d = Math.max(this.field_46095_d - 1, 0);
		double d1 = (double)(this.field_48156_b.width * 2.0F * this.field_48156_b.width * 2.0F);
		if(this.field_48156_b.getDistanceSq(this.entityTarget.posX, this.entityTarget.boundingBox.minY, this.entityTarget.posZ) <= d1) {
			if(this.field_46095_d <= 0) {
				this.field_46095_d = 20;
				this.field_48156_b.attackEntityAsMob(this.entityTarget);
			}
		}
	}
}

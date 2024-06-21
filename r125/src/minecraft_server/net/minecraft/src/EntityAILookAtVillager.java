package net.minecraft.src;

public class EntityAILookAtVillager extends EntityAIBase {
	private EntityIronGolem theGolem;
	private EntityVillager theVillager;
	private int field_48225_c;

	public EntityAILookAtVillager(EntityIronGolem entityIronGolem1) {
		this.theGolem = entityIronGolem1;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		if(!this.theGolem.worldObj.isDaytime()) {
			return false;
		} else if(this.theGolem.getRNG().nextInt(8000) != 0) {
			return false;
		} else {
			this.theVillager = (EntityVillager)this.theGolem.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.theGolem.boundingBox.expand(6.0D, 2.0D, 6.0D), this.theGolem);
			return this.theVillager != null;
		}
	}

	public boolean continueExecuting() {
		return this.field_48225_c > 0;
	}

	public void startExecuting() {
		this.field_48225_c = 400;
		this.theGolem.func_48383_a(true);
	}

	public void resetTask() {
		this.theGolem.func_48383_a(false);
		this.theVillager = null;
	}

	public void updateTask() {
		this.theGolem.getLookHelper().setLookPositionWithEntity(this.theVillager, 30.0F, 30.0F);
		--this.field_48225_c;
	}
}

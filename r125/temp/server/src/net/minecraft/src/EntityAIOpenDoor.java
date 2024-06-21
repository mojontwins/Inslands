package net.minecraft.src;

public class EntityAIOpenDoor extends EntityAIDoorInteract {
	boolean field_48196_i;
	int field_48195_j;

	public EntityAIOpenDoor(EntityLiving entityLiving1, boolean z2) {
		super(entityLiving1);
		this.theEntity = entityLiving1;
		this.field_48196_i = z2;
	}

	public boolean continueExecuting() {
		return this.field_48196_i && this.field_48195_j > 0 && super.continueExecuting();
	}

	public void startExecuting() {
		this.field_48195_j = 20;
		this.targetDoor.onPoweredBlockChange(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, true);
	}

	public void resetTask() {
		if(this.field_48196_i) {
			this.targetDoor.onPoweredBlockChange(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, false);
		}

	}

	public void updateTask() {
		--this.field_48195_j;
		super.updateTask();
	}
}

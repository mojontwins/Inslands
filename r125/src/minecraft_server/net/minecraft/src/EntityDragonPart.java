package net.minecraft.src;

public class EntityDragonPart extends Entity {
	public final EntityDragonBase entityDragonObj;
	public final String name;

	public EntityDragonPart(EntityDragonBase entityDragonBase1, String string2, float f3, float f4) {
		super(entityDragonBase1.worldObj);
		this.setSize(f3, f4);
		this.entityDragonObj = entityDragonBase1;
		this.name = string2;
	}

	protected void entityInit() {
	}

	protected void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
	}

	protected void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		return this.entityDragonObj.attackEntityFromPart(this, damageSource1, i2);
	}

	public boolean isEntityEqual(Entity entity1) {
		return this == entity1 || this.entityDragonObj == entity1;
	}
}

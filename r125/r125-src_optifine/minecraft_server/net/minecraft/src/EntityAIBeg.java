package net.minecraft.src;

public class EntityAIBeg extends EntityAIBase {
	private EntityWolf theWolf;
	private EntityPlayer field_48145_b;
	private World field_48146_c;
	private float field_48143_d;
	private int field_48144_e;

	public EntityAIBeg(EntityWolf entityWolf1, float f2) {
		this.theWolf = entityWolf1;
		this.field_48146_c = entityWolf1.worldObj;
		this.field_48143_d = f2;
		this.setMutexBits(2);
	}

	public boolean shouldExecute() {
		this.field_48145_b = this.field_48146_c.getClosestPlayerToEntity(this.theWolf, (double)this.field_48143_d);
		return this.field_48145_b == null ? false : this.func_48142_a(this.field_48145_b);
	}

	public boolean continueExecuting() {
		return !this.field_48145_b.isEntityAlive() ? false : (this.theWolf.getDistanceSqToEntity(this.field_48145_b) > (double)(this.field_48143_d * this.field_48143_d) ? false : this.field_48144_e > 0 && this.func_48142_a(this.field_48145_b));
	}

	public void startExecuting() {
		this.theWolf.func_48378_e(true);
		this.field_48144_e = 40 + this.theWolf.getRNG().nextInt(40);
	}

	public void resetTask() {
		this.theWolf.func_48378_e(false);
		this.field_48145_b = null;
	}

	public void updateTask() {
		this.theWolf.getLookHelper().setLookPosition(this.field_48145_b.posX, this.field_48145_b.posY + (double)this.field_48145_b.getEyeHeight(), this.field_48145_b.posZ, 10.0F, (float)this.theWolf.getVerticalFaceSpeed());
		--this.field_48144_e;
	}

	private boolean func_48142_a(EntityPlayer entityPlayer1) {
		ItemStack itemStack2 = entityPlayer1.inventory.getCurrentItem();
		return itemStack2 == null ? false : (!this.theWolf.isTamed() && itemStack2.itemID == Item.bone.shiftedIndex ? true : this.theWolf.isWheat(itemStack2));
	}
}

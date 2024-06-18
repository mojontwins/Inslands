package net.minecraft.src;

public class EntityAITempt extends EntityAIBase {
	private EntityCreature temptedEntity;
	private float field_48266_b;
	private double field_48267_c;
	private double field_48264_d;
	private double field_48265_e;
	private double field_48262_f;
	private double field_48263_g;
	private EntityPlayer temptingPlayer;
	private int delayTemptCounter = 0;
	private boolean field_48271_j;
	private int breedingFood;
	private boolean scaredByPlayerMovement;
	private boolean field_48270_m;

	public EntityAITempt(EntityCreature entityCreature1, float f2, int i3, boolean z4) {
		this.temptedEntity = entityCreature1;
		this.field_48266_b = f2;
		this.breedingFood = i3;
		this.scaredByPlayerMovement = z4;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		if(this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);
			if(this.temptingPlayer == null) {
				return false;
			} else {
				ItemStack itemStack1 = this.temptingPlayer.getCurrentEquippedItem();
				return itemStack1 == null ? false : itemStack1.itemID == this.breedingFood;
			}
		}
	}

	public boolean continueExecuting() {
		if(this.scaredByPlayerMovement) {
			if(this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 36.0D) {
				if(this.temptingPlayer.getDistanceSq(this.field_48267_c, this.field_48264_d, this.field_48265_e) > 0.010000000000000002D) {
					return false;
				}

				if(Math.abs((double)this.temptingPlayer.rotationPitch - this.field_48262_f) > 5.0D || Math.abs((double)this.temptingPlayer.rotationYaw - this.field_48263_g) > 5.0D) {
					return false;
				}
			} else {
				this.field_48267_c = this.temptingPlayer.posX;
				this.field_48264_d = this.temptingPlayer.posY;
				this.field_48265_e = this.temptingPlayer.posZ;
			}

			this.field_48262_f = (double)this.temptingPlayer.rotationPitch;
			this.field_48263_g = (double)this.temptingPlayer.rotationYaw;
		}

		return this.shouldExecute();
	}

	public void startExecuting() {
		this.field_48267_c = this.temptingPlayer.posX;
		this.field_48264_d = this.temptingPlayer.posY;
		this.field_48265_e = this.temptingPlayer.posZ;
		this.field_48271_j = true;
		this.field_48270_m = this.temptedEntity.getNavigator().func_48649_a();
		this.temptedEntity.getNavigator().func_48656_a(false);
	}

	public void resetTask() {
		this.temptingPlayer = null;
		this.temptedEntity.getNavigator().clearPathEntity();
		this.delayTemptCounter = 100;
		this.field_48271_j = false;
		this.temptedEntity.getNavigator().func_48656_a(this.field_48270_m);
	}

	public void updateTask() {
		this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F, (float)this.temptedEntity.getVerticalFaceSpeed());
		if(this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D) {
			this.temptedEntity.getNavigator().clearPathEntity();
		} else {
			this.temptedEntity.getNavigator().func_48652_a(this.temptingPlayer, this.field_48266_b);
		}

	}

	public boolean func_48261_f() {
		return this.field_48271_j;
	}
}

package net.minecraft.world.entity.monster;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.ICaveMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;

public class EntityTFRedcap extends EntityMob implements ICaveMob {
	private static final ItemStack defaultHeldItem = new ItemStack(Item.pickaxeSteel, 1);
	protected boolean lefty;
	protected boolean redirect;
	protected boolean shy;

	public EntityTFRedcap(World world) {
		super(world);
		this.texture = "/mob/redcap.png";
		this.moveSpeed = 0.5F;
		this.setSize(0.9F, 1.4F);
		this.attackStrength = 2;
		this.lefty = this.rand.nextBoolean();
		this.shy = true;
	}

	public EntityTFRedcap(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	public int getFullHealth() {
		return 20;
	}

	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}

	protected String getLivingSound() {
		return "mob.redcap";
	}

	protected String getHurtSound() {
		return "mob.redcaphurt";
	}

	protected String getDeathSound() {
		return "mob.redcapdie";
	}

	protected int getDropItemId() {
		return Item.bootsSteel.shiftedIndex;
	}

	@Override
	protected void dropFewItems() {
		switch(this.rand.nextInt(6)) {
			case 0: this.dropItem(Item.bootsSteel.shiftedIndex, 1); break;
			case 1: this.dropItem(Item.plateSteel.shiftedIndex, 1); break;
			case 2: this.dropItem(Item.legsSteel.shiftedIndex, 1); break;
			case 3: this.dropItem(Item.helmetSteel.shiftedIndex, 1); break;			
		}

		if(this.rand.nextInt(9) == 0) {
			this.dropItem(Item.pickaxeSteel.shiftedIndex, 1);
		}

	}

	protected void updateEntityActionState() {
		super.updateEntityActionState();
		if(this.entityToAttack != null) {
			float enemyDist = this.entityToAttack.getDistanceToEntity(this);
			if(enemyDist >= 4.0F && this.shy) {
				this.moveSpeed = 0.5F;
			} else {
				this.moveSpeed = 0.8F;
			}

			if(enemyDist > 4.0F && enemyDist < 6.0F && this.shy && this.isTargetLookingAtMe()) {
				this.moveStrafing = this.lefty ? this.moveForward : -this.moveForward;
				this.moveForward = 0.0F;
			}
		}

	}

	public boolean isTargetLookingAtMe() {
		double dx = this.posX - this.entityToAttack.posX;
		double dz = this.posZ - this.entityToAttack.posZ;
		float angle = (float)(Math.atan2(dz, dx) * 180.0D / (double)(float)Math.PI) - 90.0F;
		float difference = MathHelper.abs((this.entityToAttack.rotationYaw - angle) % 360.0F);
		return difference < 60.0F || difference > 300.0F;
	}
}

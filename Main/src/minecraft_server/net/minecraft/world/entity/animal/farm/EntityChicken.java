package net.minecraft.world.entity.animal.farm;

import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;

public class EntityChicken extends EntityAnimal {
	public boolean isJumping = false;
	public float wingRotation = 0.0F;
	public float destPos = 0.0F;
	public float prevDestPos;
	public float prevWingRotation;
	public float wingAcceleration = 1.0F;
	public int timeUntilNextEgg;

	public EntityChicken(World world) {
		super(world);
		this.texture = "/mob/chicken.png";
		this.setSize(0.3F, 0.4F);
		this.health = 4;
		this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.prevWingRotation = this.wingRotation;
		this.prevDestPos = this.destPos;
		this.destPos = (float) ((double) this.destPos + (double) (this.onGround ? -1 : 4) * 0.3D);
		if (this.destPos < 0.0F) {
			this.destPos = 0.0F;
		}
		if (this.destPos > 1.0F) {
			this.destPos = 1.0F;
		}
		if (!this.onGround && this.wingAcceleration < 1.0F) {
			this.wingAcceleration = 1.0F;
		}
		this.wingAcceleration = (float) ((double) this.wingAcceleration * 0.9D);
		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}
		this.wingRotation += this.wingAcceleration * 2.0F;
		if (!this.worldObj.isRemote && --this.timeUntilNextEgg <= 0) {
			this.worldObj.playSoundAtEntity(this, "mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.dropItem(Item.egg.shiftedIndex, 1);
			this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
		}
	}

	@Override
	protected boolean fall(float distance) {
		return false;
	}

	@Override
	protected String getLivingSound() {
		return "mob.chicken";
	}

	@Override
	protected String getHurtSound() {
		return "mob.chickenhurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.chickenhurt";
	}

	@Override
	protected int getDropItemId() {
		return this.rand.nextBoolean() ? Item.chickenRaw.shiftedIndex : Item.feather.shiftedIndex;
	}

	public int getFullHealth() {
		return 4;
	}
}

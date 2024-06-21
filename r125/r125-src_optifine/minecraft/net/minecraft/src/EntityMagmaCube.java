package net.minecraft.src;

public class EntityMagmaCube extends EntitySlime {
	public EntityMagmaCube(World world1) {
		super(world1);
		this.texture = "/mob/lava.png";
		this.isImmuneToFire = true;
		this.landMovementFactor = 0.2F;
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.difficultySetting > 0 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	public int getTotalArmorValue() {
		return this.getSlimeSize() * 3;
	}

	public int getBrightnessForRender(float f1) {
		return 15728880;
	}

	public float getBrightness(float f1) {
		return 1.0F;
	}

	protected String getSlimeParticle() {
		return "flame";
	}

	protected EntitySlime createInstance() {
		return new EntityMagmaCube(this.worldObj);
	}

	protected int getDropItemId() {
		return Item.magmaCream.shiftedIndex;
	}

	protected void dropFewItems(boolean z1, int i2) {
		int i3 = this.getDropItemId();
		if(i3 > 0 && this.getSlimeSize() > 1) {
			int i4 = this.rand.nextInt(4) - 2;
			if(i2 > 0) {
				i4 += this.rand.nextInt(i2 + 1);
			}

			for(int i5 = 0; i5 < i4; ++i5) {
				this.dropItem(i3, 1);
			}
		}

	}

	public boolean isBurning() {
		return false;
	}

	protected int func_40131_af() {
		return super.func_40131_af() * 4;
	}

	protected void func_40136_ag() {
		this.field_40139_a *= 0.9F;
	}

	protected void jump() {
		this.motionY = (double)(0.42F + (float)this.getSlimeSize() * 0.1F);
		this.isAirBorne = true;
	}

	protected void fall(float f1) {
	}

	protected boolean func_40137_ah() {
		return true;
	}

	protected int func_40130_ai() {
		return super.func_40130_ai() + 2;
	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	protected String func_40138_aj() {
		return this.getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
	}

	public boolean handleLavaMovement() {
		return false;
	}

	protected boolean func_40134_ak() {
		return true;
	}
}

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

	protected int func_40115_A() {
		return super.func_40115_A() * 4;
	}

	protected void func_40116_B() {
		this.field_40122_a *= 0.9F;
	}

	protected void jump() {
		this.motionY = (double)(0.42F + (float)this.getSlimeSize() * 0.1F);
		this.isAirBorne = true;
	}

	protected void fall(float f1) {
	}

	protected boolean func_40119_C() {
		return true;
	}

	protected int func_40113_D() {
		return super.func_40113_D() + 2;
	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	protected String func_40118_E() {
		return this.getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
	}

	public boolean handleLavaMovement() {
		return false;
	}

	protected boolean func_40121_G() {
		return true;
	}
}

package net.minecraft.src;

public class EntityDartGolden extends EntityProjectileBase {
	public EntityLiving victim;
	public static int texfxindex = 94;

	public EntityDartGolden(World world) {
		super(world);
	}

	public EntityDartGolden(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityDartGolden(World world, EntityLiving ent) {
		super(world, ent);
	}

	public void entityInit() {
		super.entityInit();
		this.item = new ItemStack(AetherItems.Dart, 1, 0);
		this.curvature = 0.0F;
		this.dmg = 4;
		this.speed = 1.5F;
	}

	public boolean handleWaterMovement() {
		return this.victim == null && super.handleWaterMovement();
	}

	public void setEntityDead() {
		this.victim = null;
		super.setEntityDead();
	}

	public boolean onHitBlock() {
		this.curvature = 0.03F;
		this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		return this.victim == null;
	}

	public boolean canBeShot(Entity ent) {
		return super.canBeShot(ent) && this.victim == null;
	}
}

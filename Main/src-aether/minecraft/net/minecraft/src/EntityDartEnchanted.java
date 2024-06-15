package net.minecraft.src;

public class EntityDartEnchanted extends EntityDartGolden {
	public EntityLiving victim;
	public static int texfxindex = 94;

	public EntityDartEnchanted(World world) {
		super(world);
	}

	public EntityDartEnchanted(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityDartEnchanted(World world, EntityLiving ent) {
		super(world, ent);
	}

	public void entityInit() {
		super.entityInit();
		this.item = new ItemStack(AetherItems.Dart, 1, 2);
		this.dmg = 6;
	}
}

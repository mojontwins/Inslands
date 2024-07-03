package net.minecraft.src;

public enum EnumCreatureType {
	monster(IMob.class, 70, Material.air, false, "Monster"),
	creature(EntityAnimal.class, 15, Material.air, true, "Animal"),
	waterCreature(IWaterMob.class, 5, Material.water, true, "Water Creature"),
	caveCreature(ICaveMob.class, 50, Material.air, false, "Cave Creature");

	private final Class<?> creatureClass;
	private final int maxNumberOfCreature;
	private final Material creatureMaterial;
	private final boolean isPeacefulCreature;
	private final String name;
	
	private EnumCreatureType(Class<?> class3, int i4, Material material5, boolean z6, String name) {
		this.creatureClass = class3;
		this.maxNumberOfCreature = i4;
		this.creatureMaterial = material5;
		this.isPeacefulCreature = z6;
		this.name = name;
	}

	public Class<?> getCreatureClass() {
		return this.creatureClass;
	}

	public int getMaxNumberOfCreature() {
		return this.maxNumberOfCreature;
	}

	public Material getCreatureMaterial() {
		return this.creatureMaterial;
	}

	public boolean getPeacefulCreature() {
		return this.isPeacefulCreature;
	}
	
	public String toString() {
		return this.name;
	}
}

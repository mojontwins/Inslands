package net.minecraft.world.item;

public enum EnumToolMaterial {
	WOOD(0, 59, 2.0F, 0),
	STONE(1, 131, 4.0F, 1),
	IRON(2, 256, 6.0F, 2),
	EMERALD(3, 1561, 8.0F, 3),
	GOLD(0, 32, 12.0F, 0), 
	SUPER(3, -1, 20.0F, 3);

	private final int harvestLevel;
	private final int maxUses;
	private final float efficiencyOnProperMaterial;
	private final int damageVsEntity;

	private EnumToolMaterial(int harvestLevel, int maxUses, float efficiencyOnProperMaterial, int damageVsEntity) {
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
		this.damageVsEntity = damageVsEntity;
	}

	public int getMaxUses() {
		return this.maxUses;
	}

	public float getEfficiencyOnProperMaterial() {
		return this.efficiencyOnProperMaterial;
	}

	public int getDamageVsEntity() {
		return this.damageVsEntity;
	}

	public int getHarvestLevel() {
		return this.harvestLevel;
	}
}

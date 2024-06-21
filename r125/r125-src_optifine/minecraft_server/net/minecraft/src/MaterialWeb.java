package net.minecraft.src;

final class MaterialWeb extends Material {
	MaterialWeb(MapColor mapColor1) {
		super(mapColor1);
	}

	public boolean blocksMovement() {
		return false;
	}
}

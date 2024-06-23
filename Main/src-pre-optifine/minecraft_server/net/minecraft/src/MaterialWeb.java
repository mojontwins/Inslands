package net.minecraft.src;

public class MaterialWeb extends Material {

	public MaterialWeb(MapColor mapColor1) {
		super(mapColor1);
	}

	public boolean blocksMovement() {
		return false;
	}
}

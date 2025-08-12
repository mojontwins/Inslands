package net.minecraft.world.level.material;

import net.minecraft.world.item.map.MapColor;

public class MaterialWeb extends Material {

	public MaterialWeb(MapColor mapColor1) {
		super(mapColor1);
	}

	public boolean blocksMovement() {
		return false;
	}
}

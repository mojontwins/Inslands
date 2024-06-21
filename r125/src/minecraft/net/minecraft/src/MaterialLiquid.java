package net.minecraft.src;

public class MaterialLiquid extends Material {
	public MaterialLiquid(MapColor mapColor1) {
		super(mapColor1);
		this.setGroundCover();
		this.setNoPushMobility();
	}

	public boolean isLiquid() {
		return true;
	}

	public boolean blocksMovement() {
		return false;
	}

	public boolean isSolid() {
		return false;
	}
}

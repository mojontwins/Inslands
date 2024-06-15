package net.minecraft.src;

public class BlockPillar extends Block {
	public static int sprTop = ModLoader.addOverride("/terrain.png", "/aether/blocks/PillarTop.png");
	public static int sprSide = ModLoader.addOverride("/terrain.png", "/aether/blocks/PillarSide.png");
	public static int sprTopSide = ModLoader.addOverride("/terrain.png", "/aether/blocks/PillarCarved.png");

	protected BlockPillar(int i) {
		super(i, Material.rock);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return i != 0 && i != 1 ? (j == 0 ? sprSide : sprTopSide) : sprTop;
	}

	protected int damageDropped(int i) {
		return i;
	}
}

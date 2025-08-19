package net.minecraft.game.world;

public final class MetadataChunkBlock {
	public final EnumSkyBlock skyBlock;
	public int x;
	public int y;
	public int z;
	public int maxX;
	public int maxY;
	public int maxZ;

	public MetadataChunkBlock(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5, int i6, int i7) {
		this.skyBlock = enumSkyBlock1;
		this.x = i2;
		this.y = i3;
		this.z = i4;
		this.maxX = i5;
		this.maxY = i6;
		this.maxZ = i7;
	}
}
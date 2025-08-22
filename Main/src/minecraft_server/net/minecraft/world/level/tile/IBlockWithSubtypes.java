package net.minecraft.world.level.tile;

public interface IBlockWithSubtypes {
	public int getItemBlockId();
	public String getNameFromMeta(int meta);
	public int getIndexInTextureFromMeta(int meta);
}

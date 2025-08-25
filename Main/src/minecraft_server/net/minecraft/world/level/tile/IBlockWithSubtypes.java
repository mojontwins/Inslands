package net.minecraft.world.level.tile;

public interface IBlockWithSubtypes {
	public int getItemblockID();
	public String getNameFromMeta(int meta);
	public int getIndexInTextureFromMeta(int meta);
}

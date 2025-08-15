package net.minecraft.world.level;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.entity.TileEntity;

public interface IBlockAccess {
	int getBlockId(int i1, int i2, int i3);

	TileEntity getBlockTileEntity(int i1, int i2, int i3);

	float getBrightness(int i1, int i2, int i3, int i4);

	float getLightBrightness(int i1, int i2, int i3);

	int getBlockMetadata(int i1, int i2, int i3);

	Material getBlockMaterial(int i1, int i2, int i3);

	boolean isBlockOpaqueCube(int i1, int i2, int i3);

	boolean isBlockNormalCube(int i1, int i2, int i3);

	WorldChunkManager getWorldChunkManager();

	int getLightBrightnessForSkyBlocks(int i2, int i3, int i4, int i);

	boolean isAirBlock(int i, int j, int z);

	int getGrassColorFromCache(int x, int z);

	int getFoliageColorFromCache(int x, int z);
}

package net.minecraft.world.level.chunk.storage;

import java.io.File;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.WorldInfo;
import net.minecraft.world.level.chunk.IChunkLoader;
import net.minecraft.world.level.dimension.WorldProvider;

public interface ISaveHandler {
	WorldInfo loadWorldInfo();

	void checkSessionLock();

	IChunkLoader getChunkLoader(WorldProvider worldProvider1);

	void saveWorldInfo(WorldInfo worldInfo1);

	File getMapFileFromName(String string1);

	File getSaveDirectory();
	
	void writePlayerData(EntityPlayer entityPlayer1);

	void readPlayerData(EntityPlayer entityPlayer1);

	ISaveHandler getSaveHandler();

	void s_func_22093_e();
}

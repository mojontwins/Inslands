package net.minecraft.src;

import java.io.File;
import java.util.List;

public interface ISaveHandler {
	WorldInfo loadWorldInfo();

	void checkSessionLock();

	IChunkLoader getChunkLoader(WorldProvider worldProvider1);

	void saveWorldInfoAndPlayer(WorldInfo worldInfo1, List<EntityPlayer> list2);

	void saveWorldInfo(WorldInfo worldInfo1);

	File getMapFileFromName(String string1);
	
	void writePlayerData(EntityPlayer entityPlayer1);

	void readPlayerData(EntityPlayer entityPlayer1);

	ISaveHandler getSaveHandler();

	void s_func_22093_e();
}

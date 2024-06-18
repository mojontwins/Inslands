package net.minecraft.src;

import java.io.File;
import java.util.List;

public interface ISaveHandler {
	WorldInfo loadWorldInfo();

	void checkSessionLock();

	IChunkLoader getChunkLoader(WorldProvider worldProvider1);

	void saveWorldInfoAndPlayer(WorldInfo worldInfo1, List list2);

	void saveWorldInfo(WorldInfo worldInfo1);

	IPlayerFileData getPlayerNBTManager();

	void func_22093_e();

	File getMapFileFromName(String string1);
}

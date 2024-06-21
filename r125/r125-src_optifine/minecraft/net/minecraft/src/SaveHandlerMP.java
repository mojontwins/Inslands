package net.minecraft.src;

import java.io.File;
import java.util.List;

public class SaveHandlerMP implements ISaveHandler {
	public WorldInfo loadWorldInfo() {
		return null;
	}

	public void checkSessionLock() {
	}

	public IChunkLoader getChunkLoader(WorldProvider worldProvider1) {
		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo worldInfo1, List list2) {
	}

	public void saveWorldInfo(WorldInfo worldInfo1) {
	}

	public File getMapFileFromName(String string1) {
		return null;
	}

	public String getSaveDirectoryName() {
		return "none";
	}
}

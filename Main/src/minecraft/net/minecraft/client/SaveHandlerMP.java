package net.minecraft.client;

import java.io.File;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;

public class SaveHandlerMP implements ISaveHandler {
	public WorldInfo loadWorldInfo() {
		return null;
	}

	public void checkSessionLock() {
	}

	public IChunkLoader getChunkLoader(WorldProvider worldProvider1) {
		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo worldInfo1, List<EntityPlayer> list2) {
	}

	public void saveWorldInfo(WorldInfo worldInfo1) {
	}

	public File getMapFileFromName(String string1) {
		return null;
	}

	@Override
	public void writePlayerData(EntityPlayer entityPlayer1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readPlayerData(EntityPlayer entityPlayer1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ISaveHandler getSaveHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void s_func_22093_e() {
		// TODO Auto-generated method stub
		
	}
}

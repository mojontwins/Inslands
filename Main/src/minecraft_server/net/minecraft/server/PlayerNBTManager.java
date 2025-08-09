package net.minecraft.server;

import java.io.File;
import java.util.List;

import net.minecraft.src.IChunkLoader;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;
import net.minecraft.world.entity.player.EntityPlayer;

public class PlayerNBTManager implements ISaveHandler {
	// Dummy. Stays so the reobfuscator doesn't go nuts!

	@Override
	public WorldInfo loadWorldInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkSessionLock() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IChunkLoader getChunkLoader(WorldProvider worldProvider1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveWorldInfoAndPlayer(WorldInfo worldInfo1, List<EntityPlayer> list2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveWorldInfo(WorldInfo worldInfo1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public File getMapFileFromName(String string1) {
		// TODO Auto-generated method stub
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

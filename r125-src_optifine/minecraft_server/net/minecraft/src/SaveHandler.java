package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SaveHandler implements IPlayerFileData, ISaveHandler {
	private static final Logger logger = Logger.getLogger("Minecraft");
	private final File worldDirectory;
	private final File worldFile;
	private final File mapDataDir;
	private final long initializationTime = System.currentTimeMillis();
	private final String saveDirectoryName;

	public SaveHandler(File file1, String string2, boolean z3) {
		this.worldDirectory = new File(file1, string2);
		this.worldDirectory.mkdirs();
		this.worldFile = new File(this.worldDirectory, "players");
		this.mapDataDir = new File(this.worldDirectory, "data");
		this.mapDataDir.mkdirs();
		this.saveDirectoryName = string2;
		if(z3) {
			this.worldFile.mkdirs();
		}

		this.setSessionLock();
	}

	private void setSessionLock() {
		try {
			File file1 = new File(this.worldDirectory, "session.lock");
			DataOutputStream dataOutputStream2 = new DataOutputStream(new FileOutputStream(file1));

			try {
				dataOutputStream2.writeLong(this.initializationTime);
			} finally {
				dataOutputStream2.close();
			}

		} catch (IOException iOException7) {
			iOException7.printStackTrace();
			throw new RuntimeException("Failed to check session lock, aborting");
		}
	}

	protected File getWorldDirectory() {
		return this.worldDirectory;
	}

	public void checkSessionLock() {
		try {
			File file1 = new File(this.worldDirectory, "session.lock");
			DataInputStream dataInputStream2 = new DataInputStream(new FileInputStream(file1));

			try {
				if(dataInputStream2.readLong() != this.initializationTime) {
					throw new MinecraftException("The save is being accessed from another location, aborting");
				}
			} finally {
				dataInputStream2.close();
			}

		} catch (IOException iOException7) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	public IChunkLoader getChunkLoader(WorldProvider worldProvider1) {
		throw new RuntimeException("Old Chunk Storage is no longer supported.");
	}

	public WorldInfo loadWorldInfo() {
		File file1 = new File(this.worldDirectory, "level.dat");
		NBTTagCompound nBTTagCompound2;
		NBTTagCompound nBTTagCompound3;
		if(file1.exists()) {
			try {
				nBTTagCompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file1));
				nBTTagCompound3 = nBTTagCompound2.getCompoundTag("Data");
				return new WorldInfo(nBTTagCompound3);
			} catch (Exception exception5) {
				exception5.printStackTrace();
			}
		}

		file1 = new File(this.worldDirectory, "level.dat_old");
		if(file1.exists()) {
			try {
				nBTTagCompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file1));
				nBTTagCompound3 = nBTTagCompound2.getCompoundTag("Data");
				return new WorldInfo(nBTTagCompound3);
			} catch (Exception exception4) {
				exception4.printStackTrace();
			}
		}

		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo worldInfo1, List list2) {
		NBTTagCompound nBTTagCompound3 = worldInfo1.getNBTTagCompoundWithPlayers(list2);
		NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
		nBTTagCompound4.setTag("Data", nBTTagCompound3);

		try {
			File file5 = new File(this.worldDirectory, "level.dat_new");
			File file6 = new File(this.worldDirectory, "level.dat_old");
			File file7 = new File(this.worldDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(nBTTagCompound4, new FileOutputStream(file5));
			if(file6.exists()) {
				file6.delete();
			}

			file7.renameTo(file6);
			if(file7.exists()) {
				file7.delete();
			}

			file5.renameTo(file7);
			if(file5.exists()) {
				file5.delete();
			}
		} catch (Exception exception8) {
			exception8.printStackTrace();
		}

	}

	public void saveWorldInfo(WorldInfo worldInfo1) {
		NBTTagCompound nBTTagCompound2 = worldInfo1.getNBTTagCompound();
		NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
		nBTTagCompound3.setTag("Data", nBTTagCompound2);

		try {
			File file4 = new File(this.worldDirectory, "level.dat_new");
			File file5 = new File(this.worldDirectory, "level.dat_old");
			File file6 = new File(this.worldDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(nBTTagCompound3, new FileOutputStream(file4));
			if(file5.exists()) {
				file5.delete();
			}

			file6.renameTo(file5);
			if(file6.exists()) {
				file6.delete();
			}

			file4.renameTo(file6);
			if(file4.exists()) {
				file4.delete();
			}
		} catch (Exception exception7) {
			exception7.printStackTrace();
		}

	}

	public void writePlayerData(EntityPlayer entityPlayer1) {
		try {
			NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
			entityPlayer1.writeToNBT(nBTTagCompound2);
			File file3 = new File(this.worldFile, "_tmp_.dat");
			File file4 = new File(this.worldFile, entityPlayer1.username + ".dat");
			CompressedStreamTools.writeCompressed(nBTTagCompound2, new FileOutputStream(file3));
			if(file4.exists()) {
				file4.delete();
			}

			file3.renameTo(file4);
		} catch (Exception exception5) {
			logger.warning("Failed to save player data for " + entityPlayer1.username);
		}

	}

	public void readPlayerData(EntityPlayer entityPlayer1) {
		NBTTagCompound nBTTagCompound2 = this.getPlayerData(entityPlayer1.username);
		if(nBTTagCompound2 != null) {
			entityPlayer1.readFromNBT(nBTTagCompound2);
		}

	}

	public NBTTagCompound getPlayerData(String string1) {
		try {
			File file2 = new File(this.worldFile, string1 + ".dat");
			if(file2.exists()) {
				return CompressedStreamTools.readCompressed(new FileInputStream(file2));
			}
		} catch (Exception exception3) {
			logger.warning("Failed to load player data for " + string1);
		}

		return null;
	}

	public IPlayerFileData getPlayerNBTManager() {
		return this;
	}

	public String[] func_52007_g() {
		return this.worldFile.list();
	}

	public void func_22093_e() {
	}

	public File getMapFileFromName(String string1) {
		return new File(this.mapDataDir, string1 + ".dat");
	}
}

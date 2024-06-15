package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SaveHandler implements ISaveHandler {
	private static final Logger logger = Logger.getLogger("Minecraft");
	private final File saveDirectory;
	private final File playersDirectory;
	private final File field_28114_d;
	private final long now = System.currentTimeMillis();

	public SaveHandler(File paramFile, String paramString, boolean paramBoolean) {
		this.saveDirectory = new File(paramFile, paramString);
		this.saveDirectory.mkdirs();
		this.playersDirectory = new File(this.saveDirectory, "players");
		this.field_28114_d = new File(this.saveDirectory, "data");
		this.field_28114_d.mkdirs();
		if(paramBoolean) {
			this.playersDirectory.mkdirs();
		}

		this.func_22154_d();
	}

	private void func_22154_d() {
		try {
			File localIOException = new File(this.saveDirectory, "session.lock");
			DataOutputStream localDataOutputStream = new DataOutputStream(new FileOutputStream(localIOException));

			try {
				localDataOutputStream.writeLong(this.now);
			} finally {
				localDataOutputStream.close();
			}

		} catch (IOException iOException7) {
			iOException7.printStackTrace();
			throw new RuntimeException("Failed to check session lock, aborting");
		}
	}

	protected File getSaveDirectory() {
		return this.saveDirectory;
	}

	public void func_22150_b() {
		try {
			File localIOException = new File(this.saveDirectory, "session.lock");
			DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(localIOException));

			try {
				if(localDataInputStream.readLong() != this.now) {
					throw new MinecraftException("The save is being accessed from another location, aborting");
				}
			} finally {
				localDataInputStream.close();
			}

		} catch (IOException iOException7) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	public IChunkLoader getChunkLoader(WorldProvider paramxa) {
		DimensionBase localDimensionBase = DimensionBase.getDimByProvider(paramxa.getClass());
		if(localDimensionBase.number != 0) {
			File localFile = new File(this.saveDirectory, "DIM" + localDimensionBase.number);
			localFile.mkdirs();
			return new ChunkLoader(localFile, true);
		} else {
			return new ChunkLoader(this.saveDirectory, true);
		}
	}

	public WorldInfo loadWorldInfo() {
		File localFile = new File(this.saveDirectory, "level.dat");
		NBTTagCompound localnu3;
		NBTTagCompound localException2;
		if(localFile.exists()) {
			try {
				localException2 = CompressedStreamTools.func_1138_a(new FileInputStream(localFile));
				localnu3 = localException2.getCompoundTag("Data");
				return new WorldInfo(localnu3);
			} catch (Exception exception5) {
				exception5.printStackTrace();
			}
		}

		localFile = new File(this.saveDirectory, "level.dat_old");
		if(localFile.exists()) {
			try {
				localException2 = CompressedStreamTools.func_1138_a(new FileInputStream(localFile));
				localnu3 = localException2.getCompoundTag("Data");
				return new WorldInfo(localnu3);
			} catch (Exception exception4) {
				exception4.printStackTrace();
			}
		}

		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo paramei, List paramList) {
		NBTTagCompound localnu1 = paramei.getNBTTagCompoundWithPlayer(paramList);
		NBTTagCompound localnu2 = new NBTTagCompound();
		localnu2.setCompoundTag("Data", localnu1);

		try {
			File localException = new File(this.saveDirectory, "level.dat_new");
			File localFile2 = new File(this.saveDirectory, "level.dat_old");
			File localFile3 = new File(this.saveDirectory, "level.dat");
			CompressedStreamTools.writeGzippedCompoundToOutputStream(localnu2, new FileOutputStream(localException));
			if(localFile2.exists()) {
				localFile2.delete();
			}

			localFile3.renameTo(localFile2);
			if(localFile3.exists()) {
				localFile3.delete();
			}

			localException.renameTo(localFile3);
			if(localException.exists()) {
				localException.delete();
			}
		} catch (Exception exception8) {
			exception8.printStackTrace();
		}

	}

	public void saveWorldInfo(WorldInfo paramei) {
		NBTTagCompound localnu1 = paramei.getNBTTagCompound();
		NBTTagCompound localnu2 = new NBTTagCompound();
		localnu2.setCompoundTag("Data", localnu1);

		try {
			File localException = new File(this.saveDirectory, "level.dat_new");
			File localFile2 = new File(this.saveDirectory, "level.dat_old");
			File localFile3 = new File(this.saveDirectory, "level.dat");
			CompressedStreamTools.writeGzippedCompoundToOutputStream(localnu2, new FileOutputStream(localException));
			if(localFile2.exists()) {
				localFile2.delete();
			}

			localFile3.renameTo(localFile2);
			if(localFile3.exists()) {
				localFile3.delete();
			}

			localException.renameTo(localFile3);
			if(localException.exists()) {
				localException.delete();
			}
		} catch (Exception exception7) {
			exception7.printStackTrace();
		}

	}

	public File func_28113_a(String paramString) {
		return new File(this.field_28114_d, paramString + ".dat");
	}
}

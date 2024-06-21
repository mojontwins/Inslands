package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;

public class SaveFormatOld implements ISaveFormat {
	protected final File savesDirectory;

	public SaveFormatOld(File file1) {
		if(!file1.exists()) {
			file1.mkdirs();
		}

		this.savesDirectory = file1;
	}

	public WorldInfo getWorldInfo(String string1) {
		File file2 = new File(this.savesDirectory, string1);
		if(!file2.exists()) {
			return null;
		} else {
			File file3 = new File(file2, "level.dat");
			NBTTagCompound nBTTagCompound4;
			NBTTagCompound nBTTagCompound5;
			if(file3.exists()) {
				try {
					nBTTagCompound4 = CompressedStreamTools.readCompressed(new FileInputStream(file3));
					nBTTagCompound5 = nBTTagCompound4.getCompoundTag("Data");
					return new WorldInfo(nBTTagCompound5);
				} catch (Exception exception7) {
					exception7.printStackTrace();
				}
			}

			file3 = new File(file2, "level.dat_old");
			if(file3.exists()) {
				try {
					nBTTagCompound4 = CompressedStreamTools.readCompressed(new FileInputStream(file3));
					nBTTagCompound5 = nBTTagCompound4.getCompoundTag("Data");
					return new WorldInfo(nBTTagCompound5);
				} catch (Exception exception6) {
					exception6.printStackTrace();
				}
			}

			return null;
		}
	}

	public ISaveHandler getSaveLoader(String string1, boolean z2) {
		return new SaveHandler(this.savesDirectory, string1, z2);
	}

	public boolean isOldMapFormat(String string1) {
		return false;
	}

	public boolean convertMapFormat(String string1, IProgressUpdate iProgressUpdate2) {
		return false;
	}
}

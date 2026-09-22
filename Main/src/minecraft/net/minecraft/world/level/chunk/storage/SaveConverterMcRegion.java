package net.minecraft.world.level.chunk.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.mojang.nbt.CompressedStreamTools;
import com.mojang.nbt.NBTTagCompound;

import net.minecraft.util.MathHelper;
import net.minecraft.world.level.WorldInfo;

public class SaveConverterMcRegion implements ISaveFormat {
	protected final File savesDirectory;

	public SaveConverterMcRegion(File file1) {
		if(!file1.exists()) {
			file1.mkdirs();
		}

		this.savesDirectory = file1;
	}

	public String getFormatName() {
		return "Scaevolus\' McRegion";
	}

	public List<SaveFormatComparator> getSaveList() {
		ArrayList<SaveFormatComparator> arrayList1 = new ArrayList<SaveFormatComparator>();
		File[] file2 = this.savesDirectory.listFiles();
		File[] file3 = file2;
		int i4 = file2.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			File file6 = file3[i5];
			if(file6.isDirectory()) {
				String string7 = file6.getName();
				WorldInfo worldInfo8 = this.getWorldInfo(string7);
				if(worldInfo8 != null) {
					boolean z9 = worldInfo8.getSaveVersion() != 19132;
					String string10 = worldInfo8.getWorldName();
					if(string10 == null || MathHelper.stringNullOrLengthZero(string10)) {
						string10 = string7;
					}

					arrayList1.add(new SaveFormatComparator(string7, string10, worldInfo8.getLastTimePlayed(), worldInfo8.getSizeOnDisk(), z9));
				}
			}
		}

		return arrayList1;
	}

	public void flushCache() {
		RegionFileCache.closeRegionFiles();
	}

	public ISaveHandler getSaveLoader(String string1, boolean z2) {
		return new SaveOldDir(this.savesDirectory, string1, z2);
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

	public void renameWorld(String string1, String string2) {
		File file3 = new File(this.savesDirectory, string1);
		if(file3.exists()) {
			File file4 = new File(file3, "level.dat");
			if(file4.exists()) {
				try {
					NBTTagCompound nBTTagCompound5 = CompressedStreamTools.readCompressed(new FileInputStream(file4));
					NBTTagCompound nBTTagCompound6 = nBTTagCompound5.getCompoundTag("Data");
					nBTTagCompound6.setString("LevelName", string2);
					CompressedStreamTools.writeCompressed(nBTTagCompound5, new FileOutputStream(file4));
				} catch (Exception exception7) {
					exception7.printStackTrace();
				}
			}

		}
	}

	public void deleteWorldDirectory(String string1) {
		File file2 = new File(this.savesDirectory, string1);
		if(file2.exists()) {
			deleteRecursively(file2.listFiles());
			file2.delete();
		}
	}

	protected static void deleteRecursively(File[] file0) {
		for(int i1 = 0; i1 < file0.length; ++i1) {
			if(file0[i1].isDirectory()) {
				deleteRecursively(file0[i1].listFiles());
			}

			file0[i1].delete();
		}

	}
}
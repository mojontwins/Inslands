package net.minecraft.world.level.chunk.storage;

import java.io.File;

import net.minecraft.world.level.WorldInfo;
import net.minecraft.world.level.chunk.IChunkLoader;
import net.minecraft.world.level.dimension.WorldProvider;

public class SaveOldDir extends SaveHandler {
	public SaveOldDir(File file1, String string2, boolean z3) {
		super(file1, string2, z3);
	}

	public IChunkLoader getChunkLoader(WorldProvider worldProvider1) {
		String folder = worldProvider1.getSaveFolderName();
		// Linked worlds live in their own "DIM-<id>" save directory which already IS
		// the world folder; when the provider reports that same folder name, do not
		// nest a second time under the save directory.
		if(folder != null && folder.equals(this.getSaveDirectory().getName())) {
			folder = null;
		}
		File file2 = folder == null ? this.getSaveDirectory() : new File(this.getSaveDirectory(), folder);
		file2.mkdirs();
		return new McRegionChunkLoader(file2);
	}

	public void saveWorldInfo(WorldInfo worldInfo1) {
		worldInfo1.setSaveVersion(19132);
		super.saveWorldInfo(worldInfo1);
	}

	public void s_func_22093_e() {
		RegionFileCache.closeRegionFiles();
	}
}

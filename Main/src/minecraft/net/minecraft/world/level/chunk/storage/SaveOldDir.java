package net.minecraft.world.level.chunk.storage;

import java.io.File;
import java.util.List;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.WorldInfo;
import net.minecraft.world.level.chunk.IChunkLoader;
import net.minecraft.world.level.dimension.WorldProvider;

public class SaveOldDir extends SaveHandler {
	public SaveOldDir(File file1, String string2, boolean z3) {
		super(file1, string2, z3);
	}

	public IChunkLoader getChunkLoader(WorldProvider worldProvider1) {
		String folder = worldProvider1.getSaveFolderName();
		File file2 = folder == null ? this.getSaveDirectory() : new File(this.getSaveDirectory(), folder);
		file2.mkdirs();
		return new McRegionChunkLoader(file2);
	}

	public void saveWorldInfoAndPlayer(WorldInfo worldInfo1, List<EntityPlayer> list2) {
		worldInfo1.setSaveVersion(19132);
		super.saveWorldInfoAndPlayer(worldInfo1, list2);
	}

	public void s_func_22093_e() {
		RegionFileCache.closeRegionFiles();
	}
}

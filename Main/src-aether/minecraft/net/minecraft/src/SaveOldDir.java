package net.minecraft.src;

import java.io.File;
import java.util.List;

public class SaveOldDir extends SaveHandler {
	public SaveOldDir(File paramFile, String paramString, boolean paramBoolean) {
		super(paramFile, paramString, paramBoolean);
	}

	public IChunkLoader getChunkLoader(WorldProvider paramxa) {
		File localFile1 = this.getSaveDirectory();
		DimensionBase localDimensionBase = DimensionBase.getDimByProvider(paramxa.getClass());
		if(localDimensionBase.number != 0) {
			File localFile2 = new File(localFile1, "DIM" + localDimensionBase.number);
			localFile2.mkdirs();
			return new McRegionChunkLoader(localFile2);
		} else {
			return new McRegionChunkLoader(localFile1);
		}
	}

	public void saveWorldInfoAndPlayer(WorldInfo paramei, List paramList) {
		paramei.setSaveVersion(19132);
		super.saveWorldInfoAndPlayer(paramei, paramList);
	}
}

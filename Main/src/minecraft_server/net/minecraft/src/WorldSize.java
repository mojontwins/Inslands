package net.minecraft.src;

public class WorldSize {
	public static int xChunks;
	public static int zChunks;
	
	public static int width;
	public static int length;
	
	public static int sizeID;
	
	public static final String sizeNames[] = new String[] { "small", "normal", "big", "huge" };
	
	public static void setSize(int xChunks, int zChunks) {
		WorldSize.xChunks = xChunks; 
		WorldSize.zChunks = zChunks;
		WorldSize.width = xChunks * 16;
		WorldSize.length = zChunks * 16;
		
		System.out.println ("Size set: " + xChunks + " x " + zChunks);
	}
	
	public static int getXChunks(IChunkProvider chunkProvider) {
		return xChunks;
	}
	
	public static int getZChunks(IChunkProvider chunkProvider) {
		return zChunks;
	}
	
	public static int getXChunkMinForReal(IChunkProvider chunkProvider) {
		if(chunkProvider instanceof ChunkProviderHell) {
			if(WorldSize.xChunks < 16 || WorldSize.zChunks < 16) return 1;
			return WorldSize.xChunks / 4;
		} else return 0;
	}
	
	public static int getXChunkMinForRealBlocks(IChunkProvider chunkProvider) {
		return getXChunkMinForReal(chunkProvider) << 4;
	}
	
	public static int getZChunkMinForReal(IChunkProvider chunkProvider) {
		if(chunkProvider instanceof ChunkProviderHell) {
			if(WorldSize.xChunks < 16 || WorldSize.zChunks < 16) return 1;
			return WorldSize.zChunks / 4;
		} else return 0;
	}

	public static int getZChunkMinForRealBlocks(IChunkProvider chunkProvider) {
		return getZChunkMinForReal(chunkProvider) << 4;
	}
	
	public static int getXChunkMaxForReal(IChunkProvider chunkProvider) {
		if(chunkProvider instanceof ChunkProviderHell) {
			if(WorldSize.xChunks < 16 || WorldSize.zChunks < 16) return xChunks - 1;
			return 3 * WorldSize.xChunks / 4;
		} else return xChunks;
	}
	
	public static int getXChunkMaxForRealBlocks(IChunkProvider chunkProvider) {
		return getXChunkMaxForReal(chunkProvider) << 4;
	}
	
	public static int getZChunkMaxForReal(IChunkProvider chunkProvider) {
		if(chunkProvider instanceof ChunkProviderHell) {
			if(WorldSize.xChunks < 16 || WorldSize.zChunks < 16) return zChunks - 1;
			return 3 * WorldSize.xChunks / 4;
		} else return zChunks;
	}

	public static int getZChunkMaxForRealBlocks(IChunkProvider chunkProvider) {
		return getZChunkMaxForReal(chunkProvider) << 4;
	}
	
	public static boolean inRange(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
		return chunkX >= WorldSize.getXChunkMinForReal(chunkProvider) &&
				chunkZ >= WorldSize.getZChunkMinForReal(chunkProvider) &&
				chunkX < WorldSize.getXChunkMaxForReal(chunkProvider) &&
				chunkZ < WorldSize.getZChunkMaxForReal(chunkProvider);
	}
	
	public static int coords2hash(int x, int z) {
		return x + z * xChunks;
	}
	
	public double getDistanceFromCenter(int x, int z) {
		double xx = (double)(width / 2 - x);
		double zz = (double)(length / 2 - z);
		return Math.sqrt(xx * xx + zz * zz);
	}
	
	public static int getTotalChunks() {
		return xChunks * zChunks;
	}

	public static void setSizeById(int sizeId) {
		switch(sizeId) {
		case 0: setSize(8, 8); break;
		case 1: setSize(16, 16); break;
		case 2: setSize(32, 32); break; 	// World is 1 whole region
		case 3: setSize(64, 64); break; 	// World is 4 whole regions
		}
		
		WorldSize.sizeID = sizeId;
	}

	public static void setSizeByName(String stringProperty) {
		if(stringProperty == null || "".equals(stringProperty)) {
			setSizeById(1);
			return;
		} 

		for(int i = 0; i < sizeNames.length; i ++) {
			if(stringProperty.equals(sizeNames[i])) {
				setSizeById(i);
				return;
			}
		}
		
		setSizeById(1);
	}
}

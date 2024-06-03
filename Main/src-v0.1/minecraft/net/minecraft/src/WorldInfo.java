package net.minecraft.src;

import java.util.List;

public class WorldInfo {
	private long randomSeed;
	private WorldType terrainType = WorldType.DEFAULT;
	private int spawnX;
	private int spawnY;
	private int spawnZ;
	private long worldTime;
	private long lastTimePlayed;
	private long sizeOnDisk;
	private NBTTagCompound playerTag;
	private int dimension;
	private String levelName;
	private int saveVersion;
	
	private boolean raining;
	private int rainTime;
	
	private boolean thundering;
	private int thunderTime;

	private boolean snowing;
	private int snowingTime;
	
	private boolean mapFeaturesEnabled;
	
	private int themeId;
	
	public WorldInfo(NBTTagCompound nBTTagCompound1) {
		this.randomSeed = nBTTagCompound1.getLong("RandomSeed");
		
		String generatorName = "";
		if(nBTTagCompound1.hasKey("generatorName")) {
			generatorName = nBTTagCompound1.getString("generatorName");
			this.terrainType = WorldType.parseWorldType(generatorName);
			if(this.terrainType == null) {
				this.terrainType = WorldType.DEFAULT;
			} 
		}

		if(nBTTagCompound1.hasKey("MapFeatures")) {
			this.mapFeaturesEnabled = nBTTagCompound1.getBoolean("MapFeatures");
		} else {
			this.mapFeaturesEnabled = true;
		}

		this.spawnX = nBTTagCompound1.getInteger("SpawnX");
		this.spawnY = nBTTagCompound1.getInteger("SpawnY");
		this.spawnZ = nBTTagCompound1.getInteger("SpawnZ");
		this.worldTime = nBTTagCompound1.getLong("Time");
		this.lastTimePlayed = nBTTagCompound1.getLong("LastPlayed");
		this.sizeOnDisk = nBTTagCompound1.getLong("SizeOnDisk");
		this.levelName = nBTTagCompound1.getString("LevelName");
		this.saveVersion = nBTTagCompound1.getInteger("version");
		this.rainTime = nBTTagCompound1.getInteger("rainTime");
		this.raining = nBTTagCompound1.getBoolean("raining");
		this.thunderTime = nBTTagCompound1.getInteger("thunderTime");
		this.thundering = nBTTagCompound1.getBoolean("thundering");
		this.snowingTime = nBTTagCompound1.getInteger("snowingTime");
		this.snowing = nBTTagCompound1.getBoolean("snowing");
		Seasons.dayOfTheYear = nBTTagCompound1.getInteger("DayOfTheYear");
		if(nBTTagCompound1.hasKey("Player")) {
			this.playerTag = nBTTagCompound1.getCompoundTag("Player");
			this.dimension = this.playerTag.getInteger("Dimension");
		}

		this.themeId = nBTTagCompound1.getInteger("ThemeId");
		LevelThemeGlobalSettings.loadThemeById(this.themeId);
		LevelThemeGlobalSettings.worldTypeID = WorldType.getIdByName(generatorName);
	System.out.println ("Generator name = " + generatorName + ", worldTypeID = " + LevelThemeGlobalSettings.worldTypeID);
		
		int xChunks = nBTTagCompound1.getInteger("WidthInChunks");
		int zChunks = nBTTagCompound1.getInteger("LengthInChunks");
		
		if(xChunks == 0 || zChunks == 0) {
			System.out.println ("Zero dimensions read. setting 8x8");
			xChunks = zChunks = 8;
		}
	
		WorldSize.setSize(xChunks, zChunks);
		
	}

	public WorldInfo(WorldSettings worldSettings1, String string2) {
		this.randomSeed = worldSettings1.getSeed();
		this.mapFeaturesEnabled = worldSettings1.isMapFeaturesEnabled();
		this.levelName = string2;
		this.terrainType = worldSettings1.getTerrainType();
		if(this.terrainType == WorldType.SKY) this.dimension = 1;
	}

	public WorldInfo(WorldInfo worldInfo1) {
		this.randomSeed = worldInfo1.randomSeed;
		this.mapFeaturesEnabled = worldInfo1.mapFeaturesEnabled;
		this.spawnX = worldInfo1.spawnX;
		this.spawnY = worldInfo1.spawnY;
		this.spawnZ = worldInfo1.spawnZ;
		this.worldTime = worldInfo1.worldTime;
		this.lastTimePlayed = worldInfo1.lastTimePlayed;
		this.sizeOnDisk = worldInfo1.sizeOnDisk;
		this.playerTag = worldInfo1.playerTag;
		this.dimension = worldInfo1.dimension;
		this.levelName = worldInfo1.levelName;
		this.saveVersion = worldInfo1.saveVersion;
		this.rainTime = worldInfo1.rainTime;
		this.raining = worldInfo1.raining;
		this.thunderTime = worldInfo1.thunderTime;
		this.thundering = worldInfo1.thundering;
		this.snowingTime = worldInfo1.snowingTime;
		this.snowing = worldInfo1.snowing;
		this.themeId = worldInfo1.themeId;
	}

	public NBTTagCompound getNBTTagCompound() {
		NBTTagCompound nBTTagCompound1 = new NBTTagCompound();
		this.updateTagCompound(nBTTagCompound1, this.playerTag);
		return nBTTagCompound1;
	}

	public NBTTagCompound getNBTTagCompoundWithPlayer(List<EntityPlayer> list1) {
		NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
		EntityPlayer entityPlayer3 = null;
		NBTTagCompound nBTTagCompound4 = null;
		if(list1.size() > 0) {
			entityPlayer3 = (EntityPlayer)list1.get(0);
		}

		if(entityPlayer3 != null) {
			nBTTagCompound4 = new NBTTagCompound();
			entityPlayer3.writeToNBT(nBTTagCompound4);
		}

		this.updateTagCompound(nBTTagCompound2, nBTTagCompound4);
		return nBTTagCompound2;
	}

	private void updateTagCompound(NBTTagCompound nBTTagCompound1, NBTTagCompound nBTTagCompound2) {
		nBTTagCompound1.setLong("RandomSeed", this.randomSeed);
		nBTTagCompound1.setString("generatorName", this.terrainType.getWorldType());
		nBTTagCompound1.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
		nBTTagCompound1.setBoolean("MapFeatures", this.mapFeaturesEnabled);
		nBTTagCompound1.setInteger("SpawnX", this.spawnX);
		nBTTagCompound1.setInteger("SpawnY", this.spawnY);
		nBTTagCompound1.setInteger("SpawnZ", this.spawnZ);
		nBTTagCompound1.setLong("Time", this.worldTime);
		nBTTagCompound1.setLong("SizeOnDisk", this.sizeOnDisk);
		nBTTagCompound1.setLong("LastPlayed", System.currentTimeMillis());
		nBTTagCompound1.setString("LevelName", this.levelName);
		nBTTagCompound1.setInteger("version", this.saveVersion);
		nBTTagCompound1.setInteger("rainTime", this.rainTime);
		nBTTagCompound1.setBoolean("raining", this.raining);
		nBTTagCompound1.setInteger("thunderTime", this.thunderTime);
		nBTTagCompound1.setBoolean("thundering", this.thundering);
		nBTTagCompound1.setInteger("snowingTime", this.snowingTime);
		nBTTagCompound1.setBoolean("snowing", this.snowing);
		nBTTagCompound1.setInteger("DayOfTheYear", Seasons.dayOfTheYear);
		if(nBTTagCompound2 != null) {
			nBTTagCompound1.setCompoundTag("Player", nBTTagCompound2);
		}
		nBTTagCompound1.setInteger("ThemeId", LevelThemeGlobalSettings.themeID);
		nBTTagCompound1.setInteger("WidthInChunks", WorldSize.xChunks);
		nBTTagCompound1.setInteger("LengthInChunks", WorldSize.zChunks);

	}

	public long getRandomSeed() {
		return this.randomSeed;
	}

	public int getSpawnX() {
		return this.spawnX;
	}

	public int getSpawnY() {
		return this.spawnY;
	}

	public int getSpawnZ() {
		return this.spawnZ;
	}

	public long getWorldTime() {
		return this.worldTime;
	}

	public long getSizeOnDisk() {
		return this.sizeOnDisk;
	}

	public NBTTagCompound getPlayerNBTTagCompound() {
		return this.playerTag;
	}

	public int getDimension() {
		return this.dimension;
	}

	public void setSpawnX(int i1) {
		this.spawnX = i1;
	}

	public void setSpawnY(int i1) {
		this.spawnY = i1;
	}

	public void setSpawnZ(int i1) {
		this.spawnZ = i1;
	}

	public void setWorldTime(long j1) {
		this.worldTime = j1;
	}

	public void setSizeOnDisk(long j1) {
		this.sizeOnDisk = j1;
	}

	public void setPlayerNBTTagCompound(NBTTagCompound nBTTagCompound1) {
		this.playerTag = nBTTagCompound1;
	}

	public void setSpawn(int i1, int i2, int i3) {
		this.spawnX = i1;
		this.spawnY = i2;
		this.spawnZ = i3;
	}

	public String getWorldName() {
		return this.levelName;
	}

	public void setWorldName(String string1) {
		this.levelName = string1;
	}

	public int getSaveVersion() {
		return this.saveVersion;
	}

	public void setSaveVersion(int i1) {
		this.saveVersion = i1;
	}

	public long getLastTimePlayed() {
		return this.lastTimePlayed;
	}

	public boolean getThundering() {
		return this.thundering;
	}

	public void setThundering(boolean z1) {
		this.thundering = z1;
	}

	public int getThunderTime() {
		return this.thunderTime;
	}

	public void setThunderTime(int i1) {
		this.thunderTime = i1;
	}

	public boolean getRaining() {
		return this.raining;
	}

	public void setRaining(boolean z1) {
		this.raining = z1;
	}

	public int getRainTime() {
		return this.rainTime;
	}

	public void setRainTime(int i1) {
		this.rainTime = i1;
	}

	public boolean getSnowing() {
		return this.snowing;
	}

	public void setSnowing(boolean z1) {
		this.snowing = z1;
	}

	public int getSnowingTime() {
		return this.snowingTime;
	}

	public void setSnowingTime(int i1) {
		this.snowingTime = i1;
	}

	public boolean isMapFeaturesEnabled() {
		return this.mapFeaturesEnabled;
	}
	
	public WorldType getTerrainType() {
		return this.terrainType;
	}

	public void setTerrainType(WorldType worldType1) {
		this.terrainType = worldType1;
	}

	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}
}

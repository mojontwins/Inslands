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
	private int gameType;
	private boolean mapFeaturesEnabled;
	private boolean hardcore = false;

	public WorldInfo(NBTTagCompound nBTTagCompound1) {
		this.randomSeed = nBTTagCompound1.getLong("RandomSeed");
		if(nBTTagCompound1.hasKey("generatorName")) {
			String string2 = nBTTagCompound1.getString("generatorName");
			this.terrainType = WorldType.parseWorldType(string2);
			if(this.terrainType == null) {
				this.terrainType = WorldType.DEFAULT;
			} else if(this.terrainType.func_48626_e()) {
				int i3 = 0;
				if(nBTTagCompound1.hasKey("generatorVersion")) {
					i3 = nBTTagCompound1.getInteger("generatorVersion");
				}

				this.terrainType = this.terrainType.func_48629_a(i3);
			}
		}

		this.gameType = nBTTagCompound1.getInteger("GameType");
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
		this.hardcore = nBTTagCompound1.getBoolean("hardcore");
		if(nBTTagCompound1.hasKey("Player")) {
			this.playerTag = nBTTagCompound1.getCompoundTag("Player");
			this.dimension = this.playerTag.getInteger("Dimension");
		}

	}

	public WorldInfo(WorldSettings worldSettings1, String string2) {
		this.randomSeed = worldSettings1.getSeed();
		this.gameType = worldSettings1.getGameType();
		this.mapFeaturesEnabled = worldSettings1.isMapFeaturesEnabled();
		this.levelName = string2;
		this.hardcore = worldSettings1.getHardcoreEnabled();
		this.terrainType = worldSettings1.getTerrainType();
	}

	public WorldInfo(WorldInfo worldInfo1) {
		this.randomSeed = worldInfo1.randomSeed;
		this.terrainType = worldInfo1.terrainType;
		this.gameType = worldInfo1.gameType;
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
		this.hardcore = worldInfo1.hardcore;
	}

	public NBTTagCompound getNBTTagCompound() {
		NBTTagCompound nBTTagCompound1 = new NBTTagCompound();
		this.updateTagCompound(nBTTagCompound1, this.playerTag);
		return nBTTagCompound1;
	}

	public NBTTagCompound getNBTTagCompoundWithPlayers(List list1) {
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
		nBTTagCompound1.setString("generatorName", this.terrainType.func_48628_a());
		nBTTagCompound1.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
		nBTTagCompound1.setInteger("GameType", this.gameType);
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
		nBTTagCompound1.setBoolean("hardcore", this.hardcore);
		if(nBTTagCompound2 != null) {
			nBTTagCompound1.setCompoundTag("Player", nBTTagCompound2);
		}

	}

	public long getSeed() {
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

	public void setPlayerNBTTagCompound(NBTTagCompound nBTTagCompound1) {
		this.playerTag = nBTTagCompound1;
	}

	public void setSpawnPosition(int i1, int i2, int i3) {
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

	public boolean isThundering() {
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

	public boolean isRaining() {
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

	public int getGameType() {
		return this.gameType;
	}

	public boolean isMapFeaturesEnabled() {
		return this.mapFeaturesEnabled;
	}

	public boolean isHardcoreModeEnabled() {
		return this.hardcore;
	}

	public WorldType getTerrainType() {
		return this.terrainType;
	}

	public void setTerrainType(WorldType worldType1) {
		this.terrainType = worldType1;
	}
}

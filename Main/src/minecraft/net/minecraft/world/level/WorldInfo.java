package net.minecraft.world.level;

import java.util.List;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.world.GlobalVars;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

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
	private boolean generateCities;
	private boolean layeredSand;
	
	private int themeId;
	private boolean bloodMoon;
	private boolean meltBuild;
		
	public WorldInfo(NBTTagCompound nbt) {
		this.randomSeed = nbt.getLong("RandomSeed");
		
		String generatorName = "";
		if(nbt.hasKey("generatorName")) {
			generatorName = nbt.getString("generatorName");
			this.terrainType = WorldType.parseWorldType(generatorName);
			if(this.terrainType == null) {
				this.terrainType = WorldType.DEFAULT;
			} 
		}

		if(nbt.hasKey("MapFeatures")) {
			this.mapFeaturesEnabled = nbt.getBoolean("MapFeatures");
		} else {
			this.mapFeaturesEnabled = true;
		}

		if(nbt.hasKey("GenerateCities")) {
			this.generateCities = nbt.getBoolean("GenerateCities");
		} else {
			this.generateCities = true;
		}
		
		if(nbt.hasKey("layeredSand")) {
			this.layeredSand = nbt.getBoolean("layeredSand");
		} else {
			this.layeredSand = true;
		}

		this.spawnX = nbt.getInteger("SpawnX");
		this.spawnY = nbt.getInteger("SpawnY");
		this.spawnZ = nbt.getInteger("SpawnZ");
		this.worldTime = nbt.getLong("Time");
		this.lastTimePlayed = nbt.getLong("LastPlayed");
		this.sizeOnDisk = nbt.getLong("SizeOnDisk");
		this.levelName = nbt.getString("LevelName");
		this.saveVersion = nbt.getInteger("version");
		this.rainTime = nbt.getInteger("rainTime");
		this.raining = nbt.getBoolean("raining");
		this.thunderTime = nbt.getInteger("thunderTime");
		this.thundering = nbt.getBoolean("thundering");
		this.snowingTime = nbt.getInteger("snowingTime");
		this.snowing = nbt.getBoolean("snowing");
		this.bloodMoon = nbt.getBoolean("BloodMoon");
		Seasons.dayOfTheYear = nbt.getInteger("DayOfTheYear");
		if(nbt.hasKey("Player")) {
			this.playerTag = nbt.getCompoundTag("Player");
			this.dimension = this.playerTag.getInteger("Dimension");
		}

		this.themeId = nbt.getInteger("ThemeId");
		LevelThemeGlobalSettings.loadThemeById(this.themeId);
		LevelThemeGlobalSettings.worldTypeID = WorldType.getIdByName(generatorName);
	
		//System.out.println ("Generator name = " + generatorName + ", worldTypeID = " + LevelThemeGlobalSettings.worldTypeID);
		
		int xChunks = nbt.getInteger("WidthInChunks");
		int zChunks = nbt.getInteger("LengthInChunks");
		
		if(xChunks == 0 || zChunks == 0) {
			System.out.println ("Zero dimensions read. setting 8x8");
			xChunks = zChunks = 8;
		}
	
		WorldSize.setSize(xChunks, zChunks);
		
		GlobalVars.noiseOffsetX = nbt.getInteger("noiseOffsetX");
		GlobalVars.noiseOffsetZ = nbt.getInteger("noiseOffsetZ");
	}

	public WorldInfo(WorldSettings settings, String string2) {
		this.randomSeed = settings.getSeed();
		this.mapFeaturesEnabled = settings.isMapFeaturesEnabled();
		this.layeredSand = settings.isLayeredSand();
		this.generateCities = settings.isGenerateCities();
		this.levelName = string2;
		this.terrainType = settings.getTerrainType();
		if(this.terrainType == WorldType.SKY) this.dimension = 1;
	}

	public WorldInfo(WorldInfo info) {
		this.randomSeed = info.randomSeed;
		this.mapFeaturesEnabled = info.mapFeaturesEnabled;
		this.generateCities = info.generateCities;
		this.spawnX = info.spawnX;
		this.spawnY = info.spawnY;
		this.spawnZ = info.spawnZ;
		this.worldTime = info.worldTime;
		this.lastTimePlayed = info.lastTimePlayed;
		this.sizeOnDisk = info.sizeOnDisk;
		this.playerTag = info.playerTag;
		this.dimension = info.dimension;
		this.levelName = info.levelName;
		this.saveVersion = info.saveVersion;
		this.rainTime = info.rainTime;
		this.raining = info.raining;
		this.thunderTime = info.thunderTime;
		this.thundering = info.thundering;
		this.snowingTime = info.snowingTime;
		this.snowing = info.snowing;
		this.bloodMoon = info.bloodMoon;
		this.layeredSand = info.layeredSand;
	}

	public NBTTagCompound getNBTTagCompound() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.updateTagCompound(nbt, this.playerTag);
		return nbt;
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

	private void updateTagCompound(NBTTagCompound nbt, NBTTagCompound nBTTagCompound2) {
		nbt.setLong("RandomSeed", this.randomSeed);
		nbt.setString("generatorName", this.terrainType.getWorldType());
		nbt.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
		nbt.setBoolean("MapFeatures", this.mapFeaturesEnabled);
		nbt.setBoolean("GenerateCities", this.generateCities);
		nbt.setBoolean("layeredSand", this.layeredSand);
		nbt.setInteger("SpawnX", this.spawnX);
		nbt.setInteger("SpawnY", this.spawnY);
		nbt.setInteger("SpawnZ", this.spawnZ);
		nbt.setLong("Time", this.worldTime);
		nbt.setLong("SizeOnDisk", this.sizeOnDisk);
		nbt.setLong("LastPlayed", System.currentTimeMillis());
		nbt.setString("LevelName", this.levelName);
		nbt.setInteger("version", this.saveVersion);
		nbt.setInteger("rainTime", this.rainTime);
		nbt.setBoolean("raining", this.raining);
		nbt.setInteger("thunderTime", this.thunderTime);
		nbt.setBoolean("thundering", this.thundering);
		nbt.setInteger("snowingTime", this.snowingTime);
		nbt.setBoolean("snowing", this.snowing);
		nbt.setBoolean("BloodMoon", this.bloodMoon);
		nbt.setInteger("DayOfTheYear", Seasons.dayOfTheYear);
		if(nBTTagCompound2 != null) {
			nbt.setCompoundTag("Player", nBTTagCompound2);
		}
		nbt.setInteger("ThemeId", LevelThemeGlobalSettings.themeID);
		nbt.setInteger("WidthInChunks", WorldSize.xChunks);
		nbt.setInteger("LengthInChunks", WorldSize.zChunks);
		nbt.setInteger("noiseOffsetX", GlobalVars.noiseOffsetX);
		nbt.setInteger("noiseOffsetZ", GlobalVars.noiseOffsetZ);

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

	public void setSpawnX(int x) {
		this.spawnX = x;
	}

	public void setSpawnY(int y) {
		this.spawnY = y;
	}

	public void setSpawnZ(int z) {
		this.spawnZ = z;
	}

	public void setWorldTime(long t) {
		this.worldTime = t;
	}

	public void setSizeOnDisk(long d) {
		this.sizeOnDisk = d;
	}

	public void setPlayerNBTTagCompound(NBTTagCompound nbt) {
		this.playerTag = nbt;
	}

	public void setSpawn(int x, int y, int z) {
		this.spawnX = x;
		this.spawnY = y;
		this.spawnZ = z;
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
	
	public boolean getGenerateCities() {
		return this.generateCities;
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

	public boolean isBloodMoon() {
		return this.bloodMoon;
	}

	public void setBloodMoon(boolean bloodMoon) {
		this.bloodMoon = bloodMoon;
	}

	public boolean isMeltBuild() {
		return meltBuild;
	}

	public void setMeltBuild(boolean meltBuild) {
		this.meltBuild = meltBuild;
	}

	public boolean isLayeredSand() {
		return this.layeredSand;
	}
}

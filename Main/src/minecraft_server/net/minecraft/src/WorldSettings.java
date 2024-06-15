package net.minecraft.src;

public final class WorldSettings {
	private final long seed;
	private final int gameType;
	private final boolean mapFeaturesEnabled;
	private final boolean generateCities;
	private final boolean hardcoreEnabled;
	private final WorldType terrainType;

	public WorldSettings(long seed, int gameType, boolean mapFeaturesEnabled, boolean hardcoreEnabled, boolean generateCities, WorldType worldType6) {
		this.seed = seed;
		this.gameType = gameType;
		this.mapFeaturesEnabled = mapFeaturesEnabled;
		this.hardcoreEnabled = hardcoreEnabled;
		this.generateCities = generateCities;
		this.terrainType = worldType6;
	}

	public long getSeed() {
		return this.seed;
	}

	public int getGameType() {
		return this.gameType;
	}

	public boolean getHardcoreEnabled() {
		return this.hardcoreEnabled;
	}

	public boolean isMapFeaturesEnabled() {
		return this.mapFeaturesEnabled;
	}

	public boolean isGenerateCities() {
		return this.generateCities;
	}
	
	public WorldType getTerrainType() {
		return this.terrainType;
	}

	public static int validGameType(int i0) {
		switch(i0) {
		case 0:
		case 1:
			return i0;
		default:
			return 0;
		}
	}

}

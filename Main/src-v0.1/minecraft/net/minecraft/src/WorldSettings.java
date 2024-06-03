package net.minecraft.src;

public final class WorldSettings {
	private final long seed;
	private final int gameType;
	private final boolean mapFeaturesEnabled;
	private final boolean hardcoreEnabled;
	private final WorldType terrainType;

	public WorldSettings(long j1, int i3, boolean z4, boolean z5, WorldType worldType6) {
		this.seed = j1;
		this.gameType = i3;
		this.mapFeaturesEnabled = z4;
		this.hardcoreEnabled = z5;
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

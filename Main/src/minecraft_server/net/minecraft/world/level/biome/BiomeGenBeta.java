package net.minecraft.world.level.biome;

public class BiomeGenBeta extends BiomeGenBase {
	public static BiomeGenBase iceDesert = new BiomeGenIceDesert(32).setBiomeName("Ice Desert");
	public static BiomeGenBase betaTundra = new BiomeGenBetaTundra(33).setBiomeName("Tundra");
	public static BiomeGenBase arctic = new BiomeGenArctic(34).setBiomeName("Arctic");
	public static BiomeGenBase savanna = new BiomeGenSavanna(35).setBiomeName("Savanna");
	public static BiomeGenBase betaDesert = new BiomeGenBetaDesert(36).setBiomeName("Desert");
	public static BiomeGenBase swamp = new BiomeGenSwampLand(37).setBiomeName("Swamp");
	public static BiomeGenBase betaTaiga = new BiomeGenBetaTaiga(38).setBiomeName("Taiga");
	public static BiomeGenBase shrubland = new BiomeGenShrubland(39).setBiomeName("Shrubland");
	public static BiomeGenBase betaForest = new BiomeGenBetaForest(40).setBiomeName("Forest");
	public static BiomeGenBase betaPlains = new BiomeGenBetaPlains(41).setBiomeName("Plains");
	public static BiomeGenBase rainForest = new BiomeGenRainForest(42).setBiomeName("Rainforest");
	public static BiomeGenBase seasonalForest = new BiomeGenSeasonalForest(43).setBiomeName("Seasonal Forest");
	
	public BiomeGenBeta(int id) {
		super(id);
	}
	
	public static BiomeGenBase getBiome(float temperature, float humidity) {
		humidity *= temperature;
		//return temperature < 0.1F ? tundra : (humidity < 0.2F ? (temperature < 0.5F ? tundra : (temperature < 0.95F ? savanna : desert)) : (humidity > 0.5F && temperature < 0.7F ? swampland : (temperature < 0.5F ? taiga : (temperature < 0.97F ? (humidity < 0.35F ? shrubland : forest) : (humidity < 0.45F ? plains : (humidity < 0.9F ? seasonalForest : rainforest))))));
		
		// This will be more easily tuneable / patchable:
		
		if (temperature < 0.1F) {
			if (humidity < 0.5F) return iceDesert;
			if (humidity < 0.8F) return betaTundra;
			return arctic;
		} else {
			if (humidity < 0.2F) {
				if (temperature < 0.5F) {
					return betaTundra;
				} else {
					if (temperature < 0.95F) {
						return savanna;
					} else {
						return betaDesert;
					}
				}
			} else {
				if (humidity > 0.5F && temperature < 0.7F) {
					return swamp;
				} else {
					if (temperature < 0.5F) {
						return betaTaiga;
					} else {
						if (temperature < 0.8F) {
							if (humidity < 0.35F) {
								return shrubland;
							} else {
								return betaForest;
							}
						} else {
							if (humidity < 0.35F) {
								return betaPlains;
							} else {
								if (humidity < 0.7F) {
									return seasonalForest;
								} else {
									return rainForest;
								}
							}
						}
					}
				}
			}
		}
	}


}

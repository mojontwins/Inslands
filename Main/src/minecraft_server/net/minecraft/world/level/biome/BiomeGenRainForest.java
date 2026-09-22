package net.minecraft.world.level.biome;

public class BiomeGenRainForest extends BiomeGenBetaForest {
	public BiomeGenRainForest(int id) {
		super(id);
		
		this.biomeColor = 588432;
		
		this.bigTreesEach10Trees = 8;
		this.redFlowersAttempts = 2;
		this.yellowFlowersAttempts = 6;
		this.tallGrassAttempts = 64;
	}
}

package net.minecraft.world.level.biome;

public class BiomeGenSeasonalForest extends BiomeGenBetaForest {

	public BiomeGenSeasonalForest(int id) {
		super(id);
		
		this.biomeColor = 10215459;
		this.redFlowersAttempts = 4;
		this.yellowFlowersAttempts = 8;
		this.treeBaseAttemptsModifier = 2;
		this.bigTreesEach10Trees = 5;
		this.tallGrassAttempts = 16;
	}

}

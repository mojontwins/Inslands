package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenForest;

public class BiomeGenTheneWhiteForest extends BiomeGenBase {
	public BiomeGenTheneWhiteForest() {
		super();
		this.overrideSkyColor = 0xCCCCCC;
		this.overrideFogColor = 0xEEEEEE;
		this.overrideCloudColor = 0xEEEEFF;
		
		this.bigTreesEach10Trees = 5;
		this.treeBaseAttemptsModifier = 20;
		this.tallGrassAttempts = 128;
		this.redFlowersAttempts = 16;
		this.yellowFlowersAttempts = 24;
		
		this.gravelLumpAttempts = 20;
		this.waterFallAttempts = 100;
	}
	
	// Todo - add white trees here.
	
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenForest();
	};
	
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenForest();
	};
	
}

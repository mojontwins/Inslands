package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenForest;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenTrees;

public class BiomeGenBetaForest extends BiomeGenBeta {

	public BiomeGenBetaForest() {
		super ();
		
		this.biomeColor = 353825;
		this.redFlowersAttempts = 4;
		this.yellowFlowersAttempts = 8;
		this.treeBaseAttemptsModifier = 7;
		this.bigTreesEach10Trees = 5;
		this.tallGrassAttempts = 32;
	}

	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return rand.nextInt(5) == 0 ? new WorldGenForest() : new WorldGenTrees();
	}
}

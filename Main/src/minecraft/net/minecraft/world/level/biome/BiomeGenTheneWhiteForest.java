package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenRuinedHouse;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenAlder;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenAspen;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenEucalyptusBig;
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
	
	@Override
	public void prePopulate(World world, Random rand, int x0, int z0) {
		if(rand.nextInt(8) == 0) {
			(new WorldGenRuinedHouse()).generate(world, rand, x0 + rand.nextInt(16), 64, z0 + rand.nextInt(16));
		}
	}
	
	@Override
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		switch(rand.nextInt(4)) {
		case 0:	return new WorldGenAlder(3 + rand.nextInt(3), 4, 3).setRelaxedBoundaries();
		case 1: return new WorldGenAspen(3 + rand.nextInt(3)).setRelaxedBoundaries();
		default: return new WorldGenForest().setRelaxedBoundaries();
		}
	};
	
	@Override
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		switch(rand.nextInt(3)) {
		case 0:	return new WorldGenAlder(6 + rand.nextInt(3), 5, 4).setRelaxedBoundaries();
		case 1: return new WorldGenAspen(8 + rand.nextInt(4)).setRelaxedBoundaries();
		default: return new WorldGenEucalyptusBig(true).setRelaxedBoundaries();
		}
	};
	
}

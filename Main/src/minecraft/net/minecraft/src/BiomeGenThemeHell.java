package net.minecraft.src;

import java.util.Random;

public class BiomeGenThemeHell extends BiomeGenBase {

	public BiomeGenThemeHell() {
		super();
		this.overrideSkyColor = 0x100400;
		this.overrideFogColor = 0x100400;
		this.overrideCloudColor = 0x210800;
		
		this.bigTreesEach10Trees = 2;
		this.mushroomBrownChance = 1;
		this.mushroomRedChance = 2;
		this.lavaAttempts = 50;
		this.waterFallAttempts = 0;
		this.mainLiquid = Block.lavaStill.blockID;
		this.deadBushAttempts = 8;
		this.topBlock = (byte)Block.dirt.blockID;
		
		this.weather = Weather.desert;
		this.foliageColorizer = 1;
	}
	
	public WorldGenerator getTreeGen(Random rand) {
		return new WorldGenTreesDead();
	}
	
	public WorldGenerator getBigTreeGen(Random rand) {
		return new WorldGenTrees();
	}
	
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z, i;
		
		int amount = rand.nextInt(3);
		for(i = 0; i < amount; i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.getHeightValue(x, z);
			(new WorldGenBigMushroom(rand.nextInt(3) == 0 ? 0 : 1)).generate(world, rand, x, y, z);
		}
		
		if (rand.nextInt(32) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			WorldGenRockBoulder worldGenRockBoulder = new WorldGenRockBoulder();
			worldGenRockBoulder.generate(world, rand, x, world.getHeightValue(x, z) + 1, z);
		}
		
		for(i = 0; i < 10; ++i) {
			x = chunkX + rand.nextInt(16);
			y = rand.nextInt(64) + 48;
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.regolith.blockID, 32)).generate(world, rand, x, y, z);
		}	
		
		if(rand.nextInt(16) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(rand.nextInt(120) + 8);
			z = chunkZ + rand.nextInt(16) + 8;
			if(y < 64 || rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.lavaMoving.blockID)).generate(world, rand, x, y, z);
			}
		}
	}
}

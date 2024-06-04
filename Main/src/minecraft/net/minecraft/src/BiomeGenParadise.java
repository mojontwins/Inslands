package net.minecraft.src;

import java.util.Random;

import com.misc.aether.AetherGenClouds;

public class BiomeGenParadise extends BiomeGenBase {

	public BiomeGenParadise() {
		this.overrideSkyColor = 0xC6DEFF;
		this.overrideFogColor = 0xC6DEFF;
		this.overrideCloudColor = 0xEEEEFF;
		
		this.redFlowersAttempts = 16;
		this.yellowFlowersAttempts = 24;
		
		this.foliageColorizer = 0;
	}

	public int getAlgaeAmount() {
		return 64;
	}
	
	public int getCoralAmount() {
		return 32;
	}
	
	public void populate(World world, Random rand, int chunkX, int chunkZ) {	
		super.populate(world, rand, chunkX, chunkZ);
		int x, y, z;
		
		// Generate algae
		int algae = rand.nextInt (this.getAlgaeAmount());
		 
		for (int i = 0; i < algae; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			y = world.getHeightValueUnderWater (x, z);
			(new WorldGenSeaweed()).generate(world,  rand, x, y, z);
		}
		
		// Generate coral
		for (int i = 0; i < this.getCoralAmount(); i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.getHeightValueUnderWater (x, z) + 1;

			if(Block.coral.canBlockStay(world, x, y, z)) {
				world.setBlockAndMetadataWithNotify(x, y, z, Block.coral.blockID, rand.nextInt(3));
			}
		}
		
		// Generate Lilypads
		for(int i = 0; i < 8; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			
			for(y = rand.nextInt(128); y > 0 && world.getBlockId(x, y - 1, z) == 0; y --) {}
			
			(new WorldGenLilypad()).generate(world, rand, x, y, z);
		}
		
		// Paeonias
		for(int i = 0; i < 12; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.paeonia.blockID)).generate(world, rand, x, y, z);
		}
		
		// Blue Flowers
		for(int i = 0; i < 10; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.blueFlower.blockID)).generate(world, rand, x, y, z);
		}
		
		// Aether stuff
		
		// Aerclouds
		
		if (world.worldProvider instanceof WorldProviderSky) {
			if(rand.nextInt(50) == 0) {
				x = chunkX + rand.nextInt(16);
				y = rand.nextInt(32) + 96;
				z = chunkZ + rand.nextInt(16);
				(new AetherGenClouds(Block.aercloud.blockID, 2, 4, false)).generate(world, rand, x, y, z);
			}
	
			if(rand.nextInt(13) == 0) {
				x = chunkX + rand.nextInt(16);
				y = rand.nextInt(64) + 32;
				z = chunkZ + rand.nextInt(16);
				(new AetherGenClouds(Block.aercloud.blockID, 1, 8, false)).generate(world, rand, x, y, z);
			}
	
			if(rand.nextInt(7) == 0) {
				x = chunkX + rand.nextInt(16);
				y = rand.nextInt(64) + 32;
				z = chunkZ + rand.nextInt(16);
				(new AetherGenClouds(Block.aercloud.blockID, 0, 16, false)).generate(world, rand, x, y, z);
			}
	
			if(rand.nextInt(50) == 0) {
				x = chunkX + rand.nextInt(16);
				y = rand.nextInt(32);
				z = chunkZ + rand.nextInt(16);
				(new AetherGenClouds(Block.aercloud.blockID, 0, 64, true)).generate(world, rand, x, y, z);
			}
		} else {
			if(rand.nextInt(32) == 0) {
				x = chunkX + rand.nextInt(16);
				y = rand.nextInt(96) + 32;
				z = chunkZ + rand.nextInt(16);
				(new AetherGenClouds(Block.aercloud.blockID, 2, 4, false)).generate(world, rand, x, y, z);
			}
	
			if(rand.nextInt(13) == 0) {
				x = chunkX + rand.nextInt(16);
				y = rand.nextInt(112) + 16;
				z = chunkZ + rand.nextInt(16);
				(new AetherGenClouds(Block.aercloud.blockID, 1, 8, false)).generate(world, rand, x, y, z);
			}
			
			if(rand.nextInt(32) == 0) {
				x = chunkX + rand.nextInt(16);
				y = rand.nextInt(96) + 32;
				z = chunkZ + rand.nextInt(16);
				(new AetherGenClouds(Block.aercloud.blockID, 0, 64, true)).generate(world, rand, x, y, z);
			}
		}

	}
}

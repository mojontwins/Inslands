package net.minecraft.src;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
	public BiomeGenForest() {
		super();
	}
	
	public int getAlgaeAmount() {
		return 64;
	}
	
	public int getCoralAmount() {
		return 32;
	}
	
	public void prePopulate(World world, Random rand, int x0, int z0) {
		int x, y, z;
		
		// Generate lakes
		if(rand.nextInt(4) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = z0 + rand.nextInt(16) + 8;
			(new WorldGenLakes(Block.waterMoving.blockID)).generate(world, rand, x, y, z);
		}
		
		// Generate lava lakes
		if(rand.nextInt(8) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(rand.nextInt(120) + 8);
			z = z0 + rand.nextInt(16) + 8;
			if(y < 64 || rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.lavaMoving.blockID)).generate(world, rand, x, y, z);
			}
		}
	}
	
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z;
		
		for(int i = 0; i < rand.nextInt(4); i ++) {
			x = chunkX + rand.nextInt(16);
			z = chunkZ + rand.nextInt(16);
			y = world.getHeightValue(x, z);
			(new WorldGenHollowLogs ()).generate(world, rand, x, y, z);
		}
		
		for(int i = 0; i < 4; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(64);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.glowshroom.blockID)).generate(world, rand, x, y, z);
		}
		
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
	}
}

package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.tile.Block;

public class WorldGenHellWillow extends WorldGenerator {

	// Code adapted from Enhanced Biomes mod
	// so it works in a1.2.6 with vanilla blocks!
	// https://github.com/SMEZ1234/EnhancedBiomes/
	
	private static int leavesId = Block.leaves2.blockID;
	private static int leavesMeta = 1;
	private static int woodId = Block.wood2.blockID;
	
	private int height;
	
	public WorldGenHellWillow (int height) {
		this.height = height;
	}
	
	public static double increaseMagnitude(double input, double increase) {
		return input + (input > 0 ? increase : -increase);
	}
	
	public void setBlockIfEmpty (World world, int x, int y, int z, int blockID) {
		if (0 == world.getblockID(x, y, z)) world.setBlock(x, y, z, blockID);
	}
	
	public void setBlockAndMetadataIfEmpty (World world, int x, int y, int z, int blockID, int meta) {
		if (0 == world.getblockID(x, y, z)) world.setBlockAndMetadata(x, y, z, blockID, meta);
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		int soilblockID = world.getblockID(x, y - 1, z);

		//if(!Block.opaqueCubeLookup[soilblockID]) return false;
		if(soilblockID != Block.bloodStone.blockID) return false;

		if (y + height > 126) return false;
		
		// wood
		int span = 3;
		
		int disX = (rand.nextInt((span * 2) + 1)) - span, 
			disY = height, 
			disZ = (rand.nextInt((span * 2) + 1)) - span;

		int posX = x + disX, 
			posY = y + height, 
			posZ = z + disZ;

		for(int b = 0; b < height; b++) {
			int xx = (int) increaseMagnitude((double) (disX) * (b + 1) / height, 0.5D),
				yy = disY * b / height,
				zz = (int) increaseMagnitude((double) (disZ) * (b + 1) / height, 0.5D);

			world.setBlock(x + xx, y + yy, z + zz, woodId);
		}
		
		//Crown
		world.setBlockAndMetadata(posX, posY, posZ, leavesId, leavesMeta);
		
		for(int xx = -1; xx <= 1; xx++) for(int yy = -1; yy <= 0; yy++) for(int zz = -1; zz <= 1; zz++) 
			this.setBlockAndMetadataIfEmpty(world, posX + xx, posY + yy, posZ + zz, leavesId, leavesMeta);
		
		//Branches
		for(int branches = 0; branches < 16; branches++) {
			int pos = branches % 8 + 1;
			
			int[] mods = new int[2];
			
			switch(pos) {
				case 2:
					mods[0] = -1;
					break;
				case 4:
					mods[1] = -1;
					break;
				case 5:
					mods[1] =  1;
					break;
				case 7:
					mods[0] =  1;
					break;
				case 1:
					mods[0] = -1;
					mods[1] = -1;
					break;
				case 3:
					mods[0] = -1;
					mods[1] =  1;
					break;
				case 6:
					mods[0] =  1;
					mods[1] = -1;
					break;
				case 8:
					mods[0] =  1;
					mods[1] =  1;
					break;
			}

			if(pos == 2 || pos == 4 || pos == 5 || pos == 7) {
				this.setBlockAndMetadataIfEmpty(world, posX + 2*mods[0], posY - 1, posZ + 2*mods[1], leavesId, leavesMeta);
				this.setBlockAndMetadataIfEmpty(world, posX + 2*mods[0], posY - 2, posZ + 2*mods[1], leavesId, leavesMeta);
				this.setBlockAndMetadataIfEmpty(world, posX + (2+rand.nextInt(2))*mods[0], posY - 3, posZ + (2+rand.nextInt(2))*mods[1], leavesId, leavesMeta);
				
				int variationX = mods[0] == 0 ? rand.nextInt(3) - 1 : 0,
					variationZ = mods[1] == 0 ? rand.nextInt(3) - 1 : 0;
				
				int variation2X = variationX * (1 + rand.nextInt(2)),
					variation2Z = variationZ * (1 + rand.nextInt(2));
									
				this.setBlockAndMetadataIfEmpty(world, posX + 3*mods[0] + variationX, posY - 4, posZ + 3*mods[1] + variationZ, leavesId, leavesMeta);
				this.setBlockAndMetadataIfEmpty(world, posX + 3*mods[0] + variationX, posY - 5, posZ + 3*mods[1] + variationZ, leavesId, leavesMeta);
				
				for(int yy = -6; yy > -8 - rand.nextInt(3); yy--) {
					this.setBlockAndMetadataIfEmpty(world, posX + 3*mods[0] + variation2X, posY + yy, posZ + 3*mods[1] + variation2Z, leavesId, leavesMeta);
				}
			}
			else {
				int mulX = rand.nextInt(2),
					mulZ = 1 - mulX;
				
				mulX++; mulZ++;
				
				this.setBlockAndMetadataIfEmpty(world, posX + mulX*mods[0], posY - 1, posZ + mulZ*mods[1], leavesId, leavesMeta);
				this.setBlockAndMetadataIfEmpty(world, posX + mulX*mods[0], posY - 2, posZ + mulZ*mods[1], leavesId, leavesMeta);
				this.setBlockAndMetadataIfEmpty(world, posX + (mulX + rand.nextInt(2))*mods[0], posY - 3, posZ + (mulZ + rand.nextInt(2))*mods[1], leavesId, leavesMeta);
				
				mulX++; mulZ++;
				
				this.setBlockAndMetadataIfEmpty(world, posX + mulX*mods[0], posY - 4, posZ + mulZ*mods[1], leavesId, leavesMeta);
				this.setBlockAndMetadataIfEmpty(world, posX + mulX*mods[0], posY - 5, posZ + mulZ*mods[1], leavesId, leavesMeta);
				
				mulX += rand.nextInt(2); mulZ += rand.nextInt(2);
				
				for(int yy = -6; yy > -8 - rand.nextInt(3); yy--) {
					this.setBlockAndMetadataIfEmpty(world, posX + mulX*mods[0], posY + yy, posZ + mulZ*mods[1], leavesId, leavesMeta);
				}
			}
		}
		
		return true;
	}
}

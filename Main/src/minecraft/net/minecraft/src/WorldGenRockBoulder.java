package net.minecraft.src;

import java.util.Random;

public class WorldGenRockBoulder extends WorldGenerator {
	private boolean bigBoulder = false;
	
	public WorldGenRockBoulder() {
	}
	
	public WorldGenRockBoulder(boolean b) {
		this.bigBoulder = b;
	}

	private void raiseColumn (World world, Random rand, int x, int y, int z, int l) {
		int blockID;
		int y0 = y - 1;
		for (; l > 0 && y < 127; l--) {
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			if(block != null && block.isOpaqueCube()) break;
			switch(rand.nextInt(30)) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
					blockID = Block.cobblestone.blockID;
					break;
				case 7:
				case 8:
				case 9:
				case 10:
					blockID = Block.cobblestoneMossy.blockID;
					break;
				case 11:
				case 12:
					blockID = Block.oreCoal.blockID;
					break;
				case 13:
					blockID = Block.oreIron.blockID;
					break;
				default:
					blockID = Block.stone.blockID;
			}
			world.setBlock(x, y ++, z, blockID);
		}
		
		Block block;
		while (y0 > 0 && (block = Block.blocksList[world.getBlockId(x, y0, z)]) != null && !block.isOpaqueCube() && !block.blockMaterial.isSolid()) {
			world.setBlock(x, y0, z, Block.stone.blockID);
			y0 --;
		}
	}
	
	private void spawnColumn (World world, Random rand, int x, int y, int z, int height) {
		int blockId = world.getBlockId(x, y - 1, z);
		while (!Block.opaqueCubeLookup[blockId] && y > 1) {
			y --; blockId = world.getBlockId(x, y - 1, z);
		}
		
		if(y <= 1) return;
				
		// Raise this column
		raiseColumn (world, rand, x, y, z, height);
		
		// Spawn new columns
		if(height > 5) {
			if (!Block.opaqueCubeLookup[world.getBlockId (x - 1, y, z)]) spawnColumn (world, rand, x - 1, y, z, height - rand.nextInt(2) - 1);
			if (!Block.opaqueCubeLookup[world.getBlockId (x + 1, y, z)]) spawnColumn (world, rand, x + 1, y, z, height - rand.nextInt(2) - 1);
			if (!Block.opaqueCubeLookup[world.getBlockId (x, y, z - 1)]) spawnColumn (world, rand, x, y, z - 1, height - rand.nextInt(2) - 1);
			if (!Block.opaqueCubeLookup[world.getBlockId (x, y, z + 1)]) spawnColumn (world, rand, x, y, z + 1, height - rand.nextInt(2) - 1);
		}
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		int h = this.bigBoulder ? 16:8;
		this.spawnColumn (world, rand, x, y, z, h + rand.nextInt(h));
		return true;
	}

}

package net.minecraft.src;

import java.util.Random;

public class BlockGrass extends Block {
	protected BlockGrass(int blockID) {
		super(blockID, Material.grass);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		switch(side) {
			case 0: return 2;
			case 1: return 0;
			default: 
				Block block = Block.blocksList[blockAccess.getBlockId(x, y + 1, z)];
				
				if(block == null) return 3;
				
				if(block.blockMaterial == Material.snow || block.blockMaterial == Material.builtSnow) return 68;
				if(block.getRenderType() == 111 && blockAccess.getBlockMetadata(x, y + 1, z) > 0) return 68;
				
				return 3;
		}
	}

	public int getBlockTextureFromSide(int side) {
		switch(side) {
			case 0: return 2;
			case 1: return 0;
			default: return 3;
		}
	}
	
	// Softlocked for a1.1.2
	/*
	public int colorMultiplier(IBlockAccess iBlockAccess1, int i2, int i3, int i4) {
		iBlockAccess1.getWorldChunkManager().getBiomesForGeneration(i2, i4, 1, 1);
		double d5 = iBlockAccess1.getWorldChunkManager().temperature[0];
		double d7 = iBlockAccess1.getWorldChunkManager().humidity[0];
		return ColorizerGrass.getGrassColor(d5, d7);
	}
	*/

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if(!world.multiplayerWorld) {
			if(world.getBlockLightValue(x, y + 1, z) < 4 && Block.lightOpacity[world.getBlockId(x, y + 1, z)] > 2) {
				if(rand.nextInt(4) != 0) {
					return;
				}

				world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
			} else if(world.getBlockLightValue(x, y + 1, z) >= 9) {
				int xx = x + rand.nextInt(3) - 1;
				int yy = y + rand.nextInt(5) - 3;
				int zz = z + rand.nextInt(3) - 1;
				int blockID = world.getBlockId(xx, yy + 1, zz);
				if(world.getBlockId(xx, yy, zz) == Block.dirt.blockID && world.getBlockLightValue(xx, yy + 1, zz) >= 4 && Block.lightOpacity[blockID] <= 2) {
					world.setBlockWithNotify(xx, yy, zz, Block.grass.blockID);
				}
			}

			if(Seasons.currentSeason == Seasons.AUTUMN && world.isAirBlock(x, y + 1, z) && world.isUnderLeaves(x, y + 1, z)) {
				world.setBlock(x, y + 1, z, Block.leafPile.blockID);
			}
		}
	}

	public int idDropped(int i1, Random random2) {
		return Block.dirt.idDropped(0, random2);
	}
	
	public boolean canGrowPlants() {
		return true;
	}
}

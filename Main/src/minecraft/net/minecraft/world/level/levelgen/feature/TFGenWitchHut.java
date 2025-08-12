package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.TileEntityMobSpawnerOneshot;
import net.minecraft.src.World;

public class TFGenWitchHut extends TFGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		return this.generateTinyHut(world, rand, x, y, z);
	}

	public boolean generateTinyHut(World world, Random rand, int x, int y, int z) {
		this.worldObj = world;
		if(!this.isAreaMostlyClear(world, rand, x, y, z, 5, 7, 6, 85)) {
			return false;
		} else {
			// Make hollow
			this.fill(x, y, z, 5, 7, 6, 0, 0);
			
			this.putBlock(x + 1, y + 0, z + 1, this.randStone(rand, 1), true);
			this.putBlock(x + 2, y + 0, z + 1, this.randStone(rand, 1), true);
			this.putBlock(x + 3, y + 0, z + 1, this.randStone(rand, 1), true);
			this.putBlock(x + 5, y + 0, z + 1, this.randStone(rand, 1), true);
			this.putBlock(x + 0, y + 0, z + 2, (byte)Block.brick.blockID, true);
			this.putBlock(x + 1, y + 0, z + 2, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 0, z + 2, this.randStone(rand, 1), true);
			this.putBlock(x + 0, y + 0, z + 3, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 0, z + 3, this.randStone(rand, 1), true);
			this.putBlock(x + 0, y + 0, z + 4, (byte)Block.brick.blockID, true);
			this.putBlock(x + 1, y + 0, z + 4, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 0, z + 4, this.randStone(rand, 1), true);
			this.putBlock(x + 1, y + 0, z + 5, this.randStone(rand, 1), true);
			this.putBlock(x + 2, y + 0, z + 5, this.randStone(rand, 1), true);
			this.putBlock(x + 3, y + 0, z + 5, this.randStone(rand, 1), true);
			this.putBlock(x + 5, y + 0, z + 5, this.randStone(rand, 1), true);
			this.putBlock(x + 1, y + 1, z + 1, this.randStone(rand, 2), true);
			this.putBlock(x + 3, y + 1, z + 1, this.randStone(rand, 2), true);
			this.putBlock(x + 5, y + 1, z + 1, this.randStone(rand, 2), true);
			this.putBlock(x + 0, y + 1, z + 2, (byte)Block.brick.blockID, true);
			this.putBlock(x + 1, y + 1, z + 2, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 1, z + 2, this.randStone(rand, 2), true);
			this.putBlock(x + 0, y + 1, z + 3, (byte)Block.brick.blockID, true);
			this.putBlock(x + 0, y + 1, z + 4, (byte)Block.brick.blockID, true);
			this.putBlock(x + 1, y + 1, z + 4, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 1, z + 4, this.randStone(rand, 2), true);
			this.putBlock(x + 1, y + 1, z + 5, this.randStone(rand, 2), true);
			this.putBlock(x + 3, y + 1, z + 5, this.randStone(rand, 2), true);
			this.putBlock(x + 5, y + 1, z + 5, this.randStone(rand, 2), true);
			this.putBlock(x + 1, y + 2, z + 1, this.randStone(rand, 3), true);
			this.putBlock(x + 2, y + 2, z + 1, this.randStone(rand, 3), true);
			this.putBlock(x + 3, y + 2, z + 1, this.randStone(rand, 3), true);
			this.putBlock(x + 4, y + 2, z + 1, this.randStone(rand, 3), true);
			this.putBlock(x + 5, y + 2, z + 1, this.randStone(rand, 3), true);
			this.putBlock(x + 0, y + 2, z + 2, (byte)Block.brick.blockID, true);
			this.putBlock(x + 1, y + 2, z + 2, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 2, z + 2, this.randStone(rand, 3), true);
			this.putBlock(x + 0, y + 2, z + 3, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 2, z + 3, this.randStone(rand, 3), true);
			this.putBlock(x + 0, y + 2, z + 4, (byte)Block.brick.blockID, true);
			this.putBlock(x + 1, y + 2, z + 4, (byte)Block.brick.blockID, true);
			this.putBlock(x + 5, y + 2, z + 4, this.randStone(rand, 1), true);
			this.putBlock(x + 1, y + 2, z + 5, this.randStone(rand, 3), true);
			this.putBlock(x + 2, y + 2, z + 5, this.randStone(rand, 3), true);
			this.putBlock(x + 3, y + 2, z + 5, this.randStone(rand, 3), true);
			this.putBlock(x + 4, y + 2, z + 5, this.randStone(rand, 3), true);
			this.putBlock(x + 5, y + 2, z + 5, this.randStone(rand, 3), true);
			this.putBlock(x + 0, y + 3, z + 2, (byte)Block.brick.blockID, true);
			this.putBlock(x + 0, y + 3, z + 3, (byte)Block.brick.blockID, true);
			this.putBlock(x + 0, y + 3, z + 4, (byte)Block.brick.blockID, true);
			this.putBlock(x + 2, y + 3, z + 1, this.randStone(rand, 4), true);
			this.putBlock(x + 3, y + 3, z + 1, this.randStone(rand, 4), true);
			this.putBlock(x + 4, y + 3, z + 1, this.randStone(rand, 4), true);
			this.putBlock(x + 2, y + 3, z + 5, this.randStone(rand, 4), true);
			this.putBlock(x + 3, y + 3, z + 5, this.randStone(rand, 4), true);
			this.putBlock(x + 4, y + 3, z + 5, this.randStone(rand, 4), true);
			this.putBlock(x + 0, y + 4, z + 3, (byte)Block.brick.blockID, true);
			this.putBlock(x + 3, y + 4, z + 1, this.randStone(rand, 5), true);
			this.putBlock(x + 3, y + 4, z + 5, this.randStone(rand, 5), true);
			this.putBlock(x + 0, y + 5, z + 3, (byte)Block.brick.blockID, true);
			this.putBlock(x + 0, y + 6, z + 3, (byte)Block.brick.blockID, true);
			this.putBlockAndMetadata(x + 0, y + 2, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 2, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 2, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 0, y + 2, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 6, y + 2, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 6, y + 2, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 6, y + 2, z + 2, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 6, y + 2, z + 3, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 6, y + 2, z + 4, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 6, y + 2, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 6, y + 2, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 2, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 4, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 3, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 3, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 3, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 3, z + 2, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 3, z + 3, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 3, z + 4, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 3, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 3, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 4, z + 0, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 4, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 4, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 4, z + 2, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 4, z + 3, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 4, z + 4, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 4, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 4, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 1, y + 4, z + 6, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 4, z + 0, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 4, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 4, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 4, z + 2, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 4, z + 3, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 4, z + 4, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 4, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 4, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 5, y + 4, z + 6, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 5, z + 0, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 5, z + 1, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 5, z + 0, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 5, z + 1, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 5, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 5, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 5, z + 2, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 5, z + 3, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 5, z + 4, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 5, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 5, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 5, z + 5, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 2, y + 5, z + 6, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 5, z + 5, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 4, y + 5, z + 6, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 6, z + 0, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 6, z + 1, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 6, z + 2, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 6, z + 4, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 6, z + 5, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 6, z + 6, (byte)Block.stairDouble.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 7, z + 0, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlockAndMetadata(x + 3, y + 7, z + 6, (byte)Block.stairSingle.blockID, 2, true);
			this.putBlock(x + 1, y - 1, z + 3, Block.blockCoal.blockID, true);
			this.putBlock(x + 1, y + 0, z + 3, (byte)Block.fire.blockID, true);
			this.worldObj.setBlockWithNotify(x + 3, y + 1, z + 3, Block.mobSpawner.blockID);
			TileEntityMobSpawner ms = (TileEntityMobSpawner)world.getBlockTileEntity(x + 3, y + 1, z + 3);
			ms.setMobID("Skeleton");
			
			this.worldObj.setBlockWithNotify(x + 3, y + 2, z + 3, Block.mobSpawnerOneshot.blockID);
			TileEntityMobSpawnerOneshot msos = (TileEntityMobSpawnerOneshot)world.getBlockTileEntity(x + 3, y + 2, z + 3);
			msos.setMobID("SkeletonWitch");
			return true;
		}
	}
}

package net.minecraft.world.level.levelgen.feature;

import java.util.Random;

import net.minecraft.world.Direction;
import net.minecraft.world.Facing;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class WorldGenVines extends WorldGenerator {
    public WorldGenVines() {
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
        int i = par3;
        int j = par5;

        for (; par4 < 128; par4++) {
            if (par1World.getBlockId(par3, par4, par5) == 0) {
                int k = 2;

                while (k <= 5) {                        
                    if (Block.vine.canPlaceBlockOnSide(par1World, par3, par4, par5, k)) {
                        par1World.setBlockAndMetadata(par3, par4, par5, Block.vine.blockID, 1 << Direction.vineGrowth[Facing.faceToSide[k]]);
                        break;
                    }

                    k++;
                }
            } else {
            	par3 = (i + par2Random.nextInt(4)) - par2Random.nextInt(4);
            	par5 = (j + par2Random.nextInt(4)) - par2Random.nextInt(4);
            }
        }

        return true;
    }
}

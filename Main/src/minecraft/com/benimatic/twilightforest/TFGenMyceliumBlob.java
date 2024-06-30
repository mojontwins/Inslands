package com.benimatic.twilightforest;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class TFGenMyceliumBlob extends WorldGenerator {
	private int myceliumBlockId = Block.mycelium.blockID;
	private int numberOfBlocks;

	public TFGenMyceliumBlob(int i) {
		this.numberOfBlocks = i;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		int l = random.nextInt(this.numberOfBlocks - 2) + 2;
		byte i1 = 1;

		for(int j1 = i - l; j1 <= i + l; ++j1) {
			for(int k1 = k - l; k1 <= k + l; ++k1) {
				int l1 = j1 - i;
				int i2 = k1 - k;
				if(l1 * l1 + i2 * i2 <= l * l) {
					for(int j2 = j - i1; j2 <= j + i1; ++j2) {
						int k2 = world.getBlockId(j1, j2, k1);
						if(k2 == Block.dirt.blockID || k2 == Block.grass.blockID) {
							world.setBlock(j1, j2, k1, this.myceliumBlockId);
						}
					}
				}
			}
		}

		return true;
	}
}

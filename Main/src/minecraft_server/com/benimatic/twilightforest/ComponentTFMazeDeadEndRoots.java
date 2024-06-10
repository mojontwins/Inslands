package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeDeadEndRoots extends ComponentTFMazeDeadEnd {
	public ComponentTFMazeDeadEndRoots(int var1, int var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		for (int var4 = 1; var4 < 5; ++var4) {
			for (int var5 = 0; var5 < 5; ++var5) {
				if (var2.nextInt(var5 + 2) > 0) {
					int var6 = var2.nextInt(6);
					this.placeBlockAtCurrentPosition(var1, Block.dirt.blockID, 0, var4, 6, var5, var3);

					for (int var7 = 6 - var6; var7 < 6; ++var7) {
						this.placeBlockAtCurrentPosition(var1, Block.wood.blockID, 0, var4, var7, var5, var3);
					}

					if (var2.nextInt(var5 + 1) > 1) {
						this.placeBlockAtCurrentPosition(var1, Block.gravel.blockID, 0, var4, 1, var5, var3);

						if (var2.nextInt(var5 + 1) > 1) {
							this.placeBlockAtCurrentPosition(var1, Block.gravel.blockID, 0, var4, 2, var5, var3);
						}
					}
				}
			}
		}

		return true;
	}
}

package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeCorridorShrooms extends ComponentTFMazeCorridor {
	public ComponentTFMazeCorridorShrooms(int var1, int var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		int var4;
		int var5;

		for (var4 = 1; var4 < 5; ++var4) {
			for (var5 = 0; var5 < 5; ++var5) {
				if (var2.nextInt(2) > 0) {
					this.placeBlockAtCurrentPosition(var1, Block.mycelium.blockID, 0, var4, 0, var5, var3);
				}

				if (var2.nextInt(2) > 0) {
					this.placeBlockAtCurrentPosition(var1,
							var2.nextBoolean() ? Block.mushroomRed.blockID : Block.mushroomBrown.blockID, 0, var4, 1,
							var5, var3);
				}
			}
		}

		var4 = var2.nextBoolean() ? Block.mushroomCapRed.blockID : Block.mushroomCapBrown.blockID;
		var5 = var2.nextInt(4) + 1;
		int var6 = var2.nextInt(4) + 1;
		this.placeBlockAtCurrentPosition(var1, var4, 15, 1, var5 - 1, var6, var3);
		this.func_74872_a(var1, var3, 1, 1, var6, 1, var5, var6, var4, 10, 0, 0, false);
		this.func_74872_a(var1, var3, 1, var5, var6 - 1, 2, var5, var6 + 1, var4, 14, 0, 0, false);
		var4 = var4 == Block.mushroomCapBrown.blockID ? Block.mushroomCapRed.blockID : Block.mushroomCapBrown.blockID;
		var5 = var2.nextInt(4) + 1;
		var6 = var2.nextInt(4) + 1;
		this.func_74872_a(var1, var3, 4, 1, var6, 4, var5, var6, var4, 10, 0, 0, false);
		this.func_74872_a(var1, var3, 3, var5, var6 - 1, 4, var5, var6 + 1, var4, 14, 0, 0, false);
		return true;
	}
}

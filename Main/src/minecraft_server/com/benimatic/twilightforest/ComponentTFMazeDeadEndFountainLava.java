package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeDeadEndFountainLava extends ComponentTFMazeDeadEndFountain {
	public ComponentTFMazeDeadEndFountainLava(int var1, int var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		super.addComponentParts(var1, var2, var3, mostlySolid);
		this.placeBlockAtCurrentPosition(var1, 0, 0, 2, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, 0, 0, 3, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.lavaMoving.blockID, 0, 2, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.lavaMoving.blockID, 0, 3, 3, 4, var3);
		return true;
	}
}

package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeRoomVault extends ComponentTFMazeRoom {
	public ComponentTFMazeRoomVault(int var1, Random var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		this.func_74872_a(var1, var3, 0, 1, 0, 15, 4, 15, Block.mazeStone2.blockID, 3, 0, 0, false);
		this.func_74872_a(var1, var3, 0, 2, 0, 15, 3, 15, Block.mazeStone2.blockID, 1, 0, 0, false);
		this.func_74878_a(var1, var3, 6, 2, 6, 9, 3, 9);
		this.func_74872_a(var1, var3, 6, 2, 5, 9, 2, 5, Block.pressurePlatePlanks.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 6, 2, 10, 9, 2, 10, Block.pressurePlatePlanks.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 5, 2, 6, 5, 2, 9, Block.pressurePlatePlanks.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 10, 2, 6, 10, 2, 9, Block.pressurePlatePlanks.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 6, 4, 5, 9, 4, 5, Block.sand.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 6, 4, 10, 9, 4, 10, Block.sand.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 5, 4, 6, 5, 4, 9, Block.sand.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 10, 4, 6, 10, 4, 9, Block.sand.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 6, 0, 5, 9, 0, 5, Block.tnt.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 6, 0, 10, 9, 0, 10, Block.tnt.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 5, 0, 6, 5, 0, 9, Block.tnt.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 10, 0, 6, 10, 0, 9, Block.tnt.blockID, 0, 0, 0, false);
		this.placeBlockAtCurrentPosition(var1, Block.chest.blockID, 0, 7, 2, 6, var3);
		this.placeTreasureAtCurrentPosition(var1, var2, 8, 2, 6, TFTreasure.underhill_room, var3);
		this.placeBlockAtCurrentPosition(var1, Block.chest.blockID, 0, 8, 2, 9, var3);
		this.placeTreasureAtCurrentPosition(var1, var2, 7, 2, 9, TFTreasure.underhill_room, var3);
		this.placeBlockAtCurrentPosition(var1, Block.chest.blockID, 0, 6, 2, 7, var3);
		this.placeTreasureAtCurrentPosition(var1, var2, 6, 2, 8, TFTreasure.underhill_room, var3);
		this.placeBlockAtCurrentPosition(var1, Block.chest.blockID, 0, 9, 2, 8, var3);
		this.placeTreasureAtCurrentPosition(var1, var2, 9, 2, 7, TFTreasure.underhill_room, var3);
		return true;
	}
}

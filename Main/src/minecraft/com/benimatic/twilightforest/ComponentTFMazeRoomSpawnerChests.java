package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeRoomSpawnerChests extends ComponentTFMazeRoom {
	public ComponentTFMazeRoomSpawnerChests(int var1, Random var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		super.addComponentParts(var1, var2, var3, mostlySolid);
		this.placePillarEnclosure(var1, var3, 3, 3);
		this.placePillarEnclosure(var1, var3, 10, 3);
		this.placePillarEnclosure(var1, var3, 3, 10);
		this.placePillarEnclosure(var1, var3, 10, 10);
		this.placeSpawnerAtCurrentPosition(var1, var2, 4, 2, 4, "Minotaur", var3);
		this.placeTreasureAtCurrentPosition(var1, var2, 4, 2, 11, TFTreasure.underhill_room, var3);
		this.placeTreasureAtCurrentPosition(var1, var2, 11, 2, 4, TFTreasure.underhill_room, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlatePlanks.blockID, 0, 11, 1, 11, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 10, 0, 11, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 11, 0, 10, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 11, 0, 12, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 12, 0, 11, var3);
		return true;
	}

	private void placePillarEnclosure(World var1, StructureBoundingBox var2, int var3, int var4) {
		for (int var5 = 1; var5 < 5; ++var5) {
			this.placeBlockAtCurrentPosition(var1, Block.mazeStone.blockID, 2, var3 + 0, var5, var4 + 0, var2);
			this.placeBlockAtCurrentPosition(var1, Block.mazeStone.blockID, 2, var3 + 2, var5, var4 + 0, var2);
			this.placeBlockAtCurrentPosition(var1, Block.mazeStone.blockID, 2, var3 + 0, var5, var4 + 2, var2);
			this.placeBlockAtCurrentPosition(var1, Block.mazeStone.blockID, 2, var3 + 2, var5, var4 + 2, var2);
		}

		this.placeBlockAtCurrentPosition(var1, Block.planks.blockID, 0, var3 + 1, 1, var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.planks.blockID, 0, var3 + 1, 4, var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(1), var3 + 1, 1,
				var4 + 0, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(0), var3 + 0, 1,
				var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(2), var3 + 2, 1,
				var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(3), var3 + 1, 1,
				var4 + 2, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(1) + 4, var3 + 1, 4,
				var4 + 0, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(0) + 4, var3 + 0, 4,
				var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(2) + 4, var3 + 2, 4,
				var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(3) + 4, var3 + 1, 4,
				var4 + 2, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 1, 2, var4 + 0, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 0, 2, var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 2, 2, var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 1, 2, var4 + 2, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 1, 3, var4 + 0, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 0, 3, var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 2, 3, var4 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, Block.fenceIron.blockID, 0, var3 + 1, 3, var4 + 2, var2);
	}
}

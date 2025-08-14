package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.TFTreasure;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.tile.Block;

public class ComponentTFMazeDeadEndChest extends ComponentTFMazeDeadEnd {
	public ComponentTFMazeDeadEndChest(int var1, int var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		this.placeBlockAtCurrentPosition(var1, Block.planks.blockID, 0, 2, 1, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.planks.blockID, 0, 3, 1, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(1), 2, 1, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.stairCompactPlanks.blockID, this.getStairMeta(1), 3, 1, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.chest.blockID, 0, 2, 2, 4, var3);
		this.placeTreasureAtCurrentPosition(var1, var2, 3, 2, 4, TFTreasure.underhill_deadend, var3);
		this.func_74872_a(var1, var3, 1, 1, 0, 4, 3, 1, Block.mazeStone2.blockID, 2, 0, 0, false);
		this.func_74872_a(var1, var3, 1, 4, 0, 4, 4, 1, Block.mazeStone2.blockID, 3, 0, 0, false);
		this.fillWithBlocks(var1, var3, 2, 1, 0, 3, 3, 1, Block.fenceIron.blockID, 0, false);
		return true;
	}
}

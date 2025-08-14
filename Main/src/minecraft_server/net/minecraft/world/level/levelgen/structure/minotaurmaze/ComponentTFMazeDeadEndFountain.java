package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.tile.Block;

public class ComponentTFMazeDeadEndFountain extends ComponentTFMazeDeadEnd {
	public ComponentTFMazeDeadEndFountain(int var1, int var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		super.addComponentParts(var1, var2, var3, mostlySolid);
		this.func_74872_a(var1, var3, 1, 1, 4, 4, 4, 4, Block.mazeStone2.blockID, 1, 0, 0, false);
		this.placeBlockAtCurrentPosition(var1, Block.waterMoving.blockID, 0, 2, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.waterMoving.blockID, 0, 3, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, 0, 0, 2, 0, 3, var3);
		this.placeBlockAtCurrentPosition(var1, 0, 0, 3, 0, 3, var3);
		return true;
	}
}

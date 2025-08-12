package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;

public class ComponentTFMazeRoomExit extends ComponentTFMazeRoom {
	public ComponentTFMazeRoomExit(int var1, Random var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		super.addComponentParts(var1, var2, var3, mostlySolid);
		this.func_74872_a(var1, var3, 5, -5, 5, 10, 0, 10, Block.mazeStone2.blockID, 1, 0, 0, false);
		this.func_74872_a(var1, var3, 5, 1, 5, 10, 1, 10, Block.mazeStone2.blockID, 3, 0, 0, false);
		this.func_74872_a(var1, var3, 5, 2, 5, 10, 3, 10, Block.fenceIron.blockID, 0, 0, 0, false);
		this.func_74872_a(var1, var3, 5, 4, 5, 10, 4, 10, Block.mazeStone2.blockID, 3, 0, 0, false);
		this.func_74878_a(var1, var3, 6, -5, 6, 9, 4, 9);
		this.getXWithOffset(0, 0);
		this.getYWithOffset(0);
		this.getZWithOffset(0, 0);
		return true;
	}
}

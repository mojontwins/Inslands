package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;

public class ComponentTFMazeDeadEnd extends StructureTFComponent {
	public ComponentTFMazeDeadEnd(int var1, int var2, int var3, int var4, int var5) {
		super(var1);
		this.coordBaseMode = var5;
		this.boundingBox = new StructureBoundingBox(var2, var3, var4, var2 + 5, var3 + 5, var4 + 5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		this.func_74872_a(var1, var3, 1, 1, 0, 4, 4, 0, Block.fence.blockID, 0, 0, 0, false);
		this.func_74878_a(var1, var3, 2, 1, 0, 3, 3, 0);
		return true;
	}
}

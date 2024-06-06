package com.benimatic.twilightforest;

import java.util.List;
import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;
import com.mojang.minecraft.structure.StructureComponent;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeRoom extends StructureTFComponent {
	public ComponentTFMazeRoom(int var1, Random var2, int var3, int var4, int var5) {
		super(var1);
		this.coordBaseMode = var2.nextInt(4);
		this.boundingBox = new StructureBoundingBox(var3, var4, var5, var3 + 15, var4 + 4, var5 + 15);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current
	 * Location of StructGen
	 */
	public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		this.func_74872_a(var1, var3, 1, 0, 1, 14, 0, 14, Block.mazeStone.blockID, 7, 0, 0, true);
		this.func_74872_a(var1, var3, 2, 0, 2, 13, 0, 13, Block.mazeStone.blockID, 6, 0, 0, true);

		if (this.getBlockIdAtCurrentPosition(var1, 7, 1, 0, var3) == 0) {
			this.fillWithBlocks(var1, var3, 6, 1, 0, 9, 4, 0, Block.fence.blockID, 0, false);
			this.func_74878_a(var1, var3, 7, 1, 0, 8, 3, 0);
		}

		if (this.getBlockIdAtCurrentPosition(var1, 7, 1, 15, var3) == 0) {
			this.fillWithBlocks(var1, var3, 6, 1, 15, 9, 4, 15, Block.fence.blockID, 0, false);
			this.func_74878_a(var1, var3, 7, 1, 15, 8, 3, 15);
		}

		if (this.getBlockIdAtCurrentPosition(var1, 0, 1, 7, var3) == 0) {
			this.fillWithBlocks(var1, var3, 0, 1, 6, 0, 4, 9, Block.fence.blockID, 0, false);
			this.func_74878_a(var1, var3, 0, 1, 7, 0, 3, 8);
		}

		if (this.getBlockIdAtCurrentPosition(var1, 15, 1, 7, var3) == 0) {
			this.fillWithBlocks(var1, var3, 15, 1, 6, 15, 4, 9, Block.fence.blockID, 0, false);
			this.func_74878_a(var1, var3, 15, 1, 7, 15, 3, 8);
		}

		return true;
	}
}

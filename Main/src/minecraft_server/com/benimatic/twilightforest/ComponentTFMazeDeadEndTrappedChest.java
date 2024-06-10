package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeDeadEndTrappedChest extends ComponentTFMazeDeadEndChest {
	public ComponentTFMazeDeadEndTrappedChest(int var1, int var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		super.addComponentParts(var1, var2, var3, mostlySolid);
		/*
		this.placeBlockAtCurrentPosition(var1, Block.presurePlateStone.blockID, this.getHookMeta(3), 1, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.presurePlateStone.blockID, this.getHookMeta(1), 4, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.presurePlateStone.blockID, this.getHookMeta(3), 1, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.presurePlateStone.blockID, this.getHookMeta(1), 4, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.presurePlateStone.blockID, this.getHookMeta(3), 1, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.presurePlateStone.blockID, this.getHookMeta(1), 4, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 1, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 1, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 1, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 1, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 2, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 2, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 3, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 3, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 3, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 3, 3, var3);
		*/
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, this.getHookMeta(3), 1, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, this.getHookMeta(1), 4, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, this.getHookMeta(3), 1, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, this.getHookMeta(1), 4, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, this.getHookMeta(3), 1, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, this.getHookMeta(1), 4, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 2, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 1, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 1, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 1, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 1, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 2, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 2, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 2, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 3, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 4, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 3, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 1, 3, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 2, 3, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.pressurePlateStone.blockID, 0, 3, 3, 3, var3);
		
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 0, 0, 2, var3);
		this.placeBlockAtCurrentPosition(var1, 0, 0, 0, -1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, 0, 0, 1, -1, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 2, 0, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 3, 0, 4, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 2, 0, 3, var3);
		this.placeBlockAtCurrentPosition(var1, Block.tnt.blockID, 0, 3, 0, 3, var3);
		return true;
	}

	protected int getHookMeta(int var1) {
		return (this.getCoordBaseMode() + var1) % 4;
	}
}

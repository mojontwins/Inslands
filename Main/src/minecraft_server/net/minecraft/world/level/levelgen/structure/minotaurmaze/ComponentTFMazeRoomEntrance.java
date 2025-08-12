package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureComponent;

public class ComponentTFMazeRoomEntrance extends StructureTFComponent {
	public ComponentTFMazeRoomEntrance(int var1, Random var2, int var3, int var4, int var5) {
		super(var1);
		this.coordBaseMode = var2.nextInt(4);

		switch (this.coordBaseMode) {
		case 0:
		case 2:
			this.boundingBox = new StructureBoundingBox(var3, var4, var5, var3 + 6 - 1, var4 + 14, var5 + 6 - 1);
			break;

		default:
			this.boundingBox = new StructureBoundingBox(var3, var4, var5, var3 + 6 - 1, var4 + 14, var5 + 6 - 1);
		}
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
		this.fillWithBlocks(var1, var3, 0, -10, 0, 5, 64, 5, Block.stoneBricks.blockID, 0, true);
		this.fillWithBlocks(var1, var3, 1, -10, 1, 4, 64, 4, 0, 0, true);
		return true;
	}
}

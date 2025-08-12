package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;

public class ComponentTFMazeRoomCollapse extends ComponentTFMazeRoom {
	public ComponentTFMazeRoomCollapse(int var1, Random var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		super.addComponentParts(var1, var2, var3, mostlySolid);

		for (int var4 = 1; var4 < 14; ++var4) {
			for (int var5 = 1; var5 < 14; ++var5) {
				int var6 = (int) Math.round(7.0D / Math.sqrt((7.5D - (double) var4) * (7.5D - (double) var4)
						+ (7.5D - (double) var5) * (7.5D - (double) var5)));
				int var7 = var2.nextInt(var6);
				int var8 = var2.nextInt(var6);

				if (var7 > 0) {
					++var7;
					this.fillWithBlocks(var1, var3, var4, 1, var5, var4, var7, var5, Block.gravel.blockID, 0, false);
					this.func_74878_a(var1, var3, var4, var7, var5, var4, var7 + 5, var5);
				} else if (var8 > 0) {
					this.fillWithBlocks(var1, var3, var4, 5, var5, var4, 5 + var8, var5, Block.dirt.blockID, 0, true);
					this.func_74872_a(var1, var3, var4, 5 - var2.nextInt(5), var5, var4, 5, var5, Block.wood.blockID,
							0, 0, 0, false);
				} else if (var2.nextInt(var6 + 1) > 0) {
					this.func_74878_a(var1, var3, var4, 5, var5, var4, 5, var5);
				}
			}
		}

		return true;
	}
}

package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;

public class ComponentTFMazeMushRoom extends ComponentTFMazeRoom {
	public ComponentTFMazeMushRoom(int var1, Random var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
		this.coordBaseMode = 0;
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

				if (var2.nextInt(var6 + 1) > 0) {
					this.placeBlockAtCurrentPosition(var1, Block.mycelium.blockID, 0, var4, 0, var5, var3);
				}

				if (var2.nextInt(var6) > 0) {
					this.placeBlockAtCurrentPosition(var1,
							var2.nextBoolean() ? Block.mushroomRed.blockID : Block.mushroomBrown.blockID, 0, var4, 1,
							var5, var3);
				}
			}
		}

		this.makeMediumMushroom(var1, var3, 5, 2, 9, Block.mushroomCapRed.blockID);
		this.makeMediumMushroom(var1, var3, 5, 3, 9, Block.mushroomCapRed.blockID);
		this.makeMediumMushroom(var1, var3, 9, 2, 5, Block.mushroomCapRed.blockID);
		this.makeMediumMushroom(var1, var3, 6, 3, 4, Block.mushroomCapBrown.blockID);
		this.makeMediumMushroom(var1, var3, 10, 1, 9, Block.mushroomCapBrown.blockID);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapRed.blockID, 15, 1, 2, 1, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapRed.blockID, 5, 1, 3, 1, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapRed.blockID, 9, 2, 3, 1, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapRed.blockID, 9, 1, 3, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 15, 14, 3, 1, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 5, 14, 4, 1, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 7, 13, 4, 1, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 7, 14, 4, 2, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 15, 1, 1, 14, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 5, 1, 2, 14, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 3, 2, 2, 14, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 3, 1, 2, 13, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 5, 14, 1, 14, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 1, 13, 1, 14, var3);
		this.placeBlockAtCurrentPosition(var1, Block.mushroomCapBrown.blockID, 1, 14, 1, 13, var3);
		return true;
	}

	private void makeMediumMushroom(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6) {
		this.placeBlockAtCurrentPosition(var1, var6, 5, var3 + 0, var4, var5 + 0, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 6, var3 + 1, var4, var5 + 0, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 9, var3 + 1, var4, var5 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 8, var3 + 0, var4, var5 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 7, var3 - 1, var4, var5 + 1, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 4, var3 - 1, var4, var5 + 0, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 1, var3 - 1, var4, var5 - 1, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 2, var3 + 0, var4, var5 - 1, var2);
		this.placeBlockAtCurrentPosition(var1, var6, 3, var3 + 1, var4, var5 - 1, var2);

		for (int var7 = 1; var7 < var4; ++var7) {
			this.placeBlockAtCurrentPosition(var1, var6, 10, var3 + 0, var7, var5 + 0, var2);
		}
	}
}

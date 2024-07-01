package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMazeRoomBoss extends ComponentTFMazeRoom {
	private boolean taurPlaced = false;

	public ComponentTFMazeRoomBoss(int var1, Random var2, int var3, int var4, int var5) {
		super(var1, var2, var3, var4, var5);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		if (this.getBlockIdAtCurrentPosition(var1, 7, 1, 0, var3) == 0) {
			this.fillWithBlocks(var1, var3, 6, 1, 0, 9, 4, 0, Block.fence.blockID, 0, false);
		}

		if (this.getBlockIdAtCurrentPosition(var1, 7, 1, 15, var3) == 0) {
			this.fillWithBlocks(var1, var3, 6, 1, 15, 9, 4, 15, Block.fence.blockID, 0, false);
		}

		if (this.getBlockIdAtCurrentPosition(var1, 0, 1, 7, var3) == 0) {
			this.fillWithBlocks(var1, var3, 0, 1, 6, 0, 4, 9, Block.fence.blockID, 0, false);
		}

		if (this.getBlockIdAtCurrentPosition(var1, 15, 1, 7, var3) == 0) {
			this.fillWithBlocks(var1, var3, 15, 1, 6, 15, 4, 9, Block.fence.blockID, 0, false);
		}

		int var4;
		int var5;
		int var6;

		for (var4 = 1; var4 < 14; ++var4) {
			for (var5 = 1; var5 < 14; ++var5) {
				var6 = (int) Math.round(7.0D / Math.sqrt((7.5D - (double) var4) * (7.5D - (double) var4)
						+ (7.5D - (double) var5) * (7.5D - (double) var5)));
				boolean var7 = var2.nextInt(var6 + 1) > 0;
				boolean var8 = var2.nextInt(var6) > 0;
				boolean var9 = var2.nextBoolean();

				if (var7) {
					this.placeBlockAtCurrentPosition(var1, Block.mycelium.blockID, 0, var4, 0, var5, var3);
				}

				if (var8) {
					this.placeBlockAtCurrentPosition(var1,
							var9 ? Block.mushroomRed.blockID : Block.mushroomBrown.blockID, 0, var4, 1, var5, var3);
				}
			}
		}

		this.func_74872_a(var1, var3, 1, 1, 1, 3, 1, 3, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 1, 2, 1, 1, 3, 4, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 2, 2, 1, 4, 3, 1, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 1, 4, 1, 3, 4, 3, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.placeTreasureAtCurrentPosition(var1, var2, 3, 2, 3, TFTreasure.underhill_room, var3);
		this.func_74872_a(var1, var3, 12, 1, 12, 14, 1, 14, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 14, 2, 11, 14, 3, 14, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 11, 2, 14, 14, 3, 14, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 12, 4, 12, 14, 4, 14, Block.mushroomCapRed.blockID, 14, 0, 0, false);
		this.placeTreasureAtCurrentPosition(var1, var2, 12, 2, 12, TFTreasure.underhill_room, var3);
		this.func_74872_a(var1, var3, 1, 1, 12, 3, 1, 14, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 1, 2, 11, 1, 3, 14, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 2, 2, 14, 4, 3, 14, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 1, 4, 12, 3, 4, 14, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.placeTreasureAtCurrentPosition(var1, var2, 3, 2, 12, TFTreasure.underhill_room, var3);
		this.func_74872_a(var1, var3, 12, 1, 1, 14, 1, 3, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 11, 2, 1, 14, 3, 1, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 14, 2, 2, 14, 3, 4, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 12, 4, 1, 14, 4, 3, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.placeTreasureAtCurrentPosition(var1, var2, 12, 2, 3, TFTreasure.underhill_room, var3);
		this.func_74872_a(var1, var3, 5, 4, 5, 7, 5, 7, Block.mushroomCapBrown.blockID, 14, 0, 0, false);
		this.func_74872_a(var1, var3, 8, 4, 8, 10, 5, 10, Block.mushroomCapRed.blockID, 14, 0, 0, false);

		if (!this.taurPlaced) {
			var4 = this.getXWithOffset(7, 7);
			var5 = this.getYWithOffset(1);
			var6 = this.getZWithOffset(7, 7);

			if (var3.isVecInside(var4, var5, var6)) {
				this.taurPlaced = true;
				
				EntityTFMinoshroom var10 = new EntityTFMinoshroom(var1);
				var10.setPosition((double) var4, (double) var5, (double) var6);
				var10.setHomeArea(var4, var5, var6, 7);
				var1.spawnEntityInWorld(var10);
			}
		}

		return true;
	}
}

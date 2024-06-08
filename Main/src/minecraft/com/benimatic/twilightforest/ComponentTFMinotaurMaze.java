package com.benimatic.twilightforest;

import java.util.List;
import java.util.Random;

import com.mojang.minecraft.structure.StructureBoundingBox;
import com.mojang.minecraft.structure.StructureComponent;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class ComponentTFMinotaurMaze extends StructureTFComponent {
	private static final int FLOOR_LEVEL = 1;
	TFMazeNew maze;
	int[] rcoords;
	private int level;

	public ComponentTFMinotaurMaze(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		super(var1);
		this.setCoordBaseMode(0);
		this.level = var7;
		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(var2, var3, var4, -this.getRadius(), 0,
				-this.getRadius(), this.getRadius() * 2, 5, this.getRadius() * 2, 0);
		this.maze = new TFMazeNew(this.getMazeSize(), this.getMazeSize());
		this.maze.setSeed(
				(long) (this.boundingBox.minX * 90342903 + this.boundingBox.minY * 90342903 ^ this.boundingBox.minZ));
		byte var8 = 7;
		this.rcoords = new int[var8 * 2];
		this.rcoords[0] = var5;
		this.rcoords[1] = var6;
		this.maze.carveRoom1(var5, var6);

		for (int var9 = 1; var9 < var8; ++var9) {
			int var10;
			int var11;

			do {
				var10 = this.maze.rand.nextInt(this.getMazeSize() - 2) + 1;
				var11 = this.maze.rand.nextInt(this.getMazeSize() - 2) + 1;
			} while (this.isNearRoom(var10, var11, this.rcoords, var9 == 1 ? 7 : 4));

			this.maze.carveRoom1(var10, var11);
			this.rcoords[var9 * 2] = var10;
			this.rcoords[var9 * 2 + 1] = var11;
		}

		this.maze.generateRecursiveBacktracker(0, 0);
	}

	public ComponentTFMinotaurMaze(int var1, int var2, int var3, int var4, int var5) {
		this(var1, var2, var3, var4, 11, 11, var5);
	}

	protected ComponentTFMazeRoom makeRoom(Random var1, int var2, int var3, int var4) {
		Object var5 = null;
		int var6 = this.boundingBox.minX + var3 * 5 - 4;
		int var7 = this.boundingBox.minY;
		int var8 = this.boundingBox.minZ + var4 * 5 - 4;

		if (var2 == 0) {
			var5 = new ComponentTFMazeRoom(3 + var2, var1, var6, var7, var8);
		} else if (var2 == 1) {
			if (this.level == FLOOR_LEVEL) {
				var5 = new ComponentTFMazeRoomExit(3 + var2, var1, var6, var7, var8);
			} else {
				var5 = new ComponentTFMazeRoomBoss(3 + var2, var1, var6, var7, var8);
			}
		} else if (var2 != 2 && var2 != 3) {
			if (var2 == 4) {
				if (this.level == FLOOR_LEVEL) {
					var5 = new ComponentTFMazeRoomFountain(3 + var2, var1, var6, var7, var8);
				} else {
					var5 = new ComponentTFMazeRoomVault(3 + var2, var1, var6, var7, var8);
				}
			} else {
				var5 = new ComponentTFMazeRoomSpawnerChests(3 + var2, var1, var6, var7, var8);
			}
		} else if (this.level == FLOOR_LEVEL) {
			var5 = new ComponentTFMazeRoomCollapse(3 + var2, var1, var6, var7, var8);
		} else {
			var5 = new ComponentTFMazeMushRoom(3 + var2, var1, var6, var7, var8);
		}

		return (ComponentTFMazeRoom) var5;
	}

	protected void decorateDeadEndsCorridors(Random var1, List<StructureComponent> var2) {
		for (int var3 = 0; var3 < this.maze.width; ++var3) {
			for (int var4 = 0; var4 < this.maze.depth; ++var4) {
				StructureComponent var5 = null;

				if (!this.maze.isWall(var3, var4, var3 - 1, var4) && this.maze.isWall(var3, var4, var3 + 1, var4)
						&& this.maze.isWall(var3, var4, var3, var4 - 1)
						&& this.maze.isWall(var3, var4, var3, var4 + 1)) {
					var5 = this.makeDeadEnd(var1, var3, var4, 3);
				}

				if (this.maze.isWall(var3, var4, var3 - 1, var4) && !this.maze.isWall(var3, var4, var3 + 1, var4)
						&& this.maze.isWall(var3, var4, var3, var4 - 1)
						&& this.maze.isWall(var3, var4, var3, var4 + 1)) {
					var5 = this.makeDeadEnd(var1, var3, var4, 1);
				}

				if (this.maze.isWall(var3, var4, var3 - 1, var4) && this.maze.isWall(var3, var4, var3 + 1, var4)
						&& !this.maze.isWall(var3, var4, var3, var4 - 1)
						&& this.maze.isWall(var3, var4, var3, var4 + 1)) {
					var5 = this.makeDeadEnd(var1, var3, var4, 0);
				}

				if (this.maze.isWall(var3, var4, var3 - 1, var4) && this.maze.isWall(var3, var4, var3 + 1, var4)
						&& this.maze.isWall(var3, var4, var3, var4 - 1)
						&& !this.maze.isWall(var3, var4, var3, var4 + 1)) {
					var5 = this.makeDeadEnd(var1, var3, var4, 2);
				}

				if (!this.maze.isWall(var3, var4, var3 - 1, var4) && !this.maze.isWall(var3, var4, var3 + 1, var4)
						&& this.maze.isWall(var3, var4, var3, var4 - 1) && this.maze.isWall(var3, var4, var3, var4 + 1)
						&& this.maze.isWall(var3 - 1, var4, var3 - 1, var4 - 1)
						&& this.maze.isWall(var3 - 1, var4, var3 - 1, var4 + 1)
						&& this.maze.isWall(var3 + 1, var4, var3 + 1, var4 - 1)
						&& this.maze.isWall(var3 + 1, var4, var3 + 1, var4 + 1)) {
					var5 = this.makeCorridor(var1, var3, var4, 1);
				}

				if (!this.maze.isWall(var3, var4, var3, var4 - 1) && !this.maze.isWall(var3, var4, var3, var4 + 1)
						&& this.maze.isWall(var3, var4, var3 - 1, var4) && this.maze.isWall(var3, var4, var3 + 1, var4)
						&& this.maze.isWall(var3, var4 - 1, var3 - 1, var4 - 1)
						&& this.maze.isWall(var3, var4 - 1, var3 + 1, var4 - 1)
						&& this.maze.isWall(var3, var4 + 1, var3 - 1, var4 + 1)
						&& this.maze.isWall(var3, var4 + 1, var3 + 1, var4 + 1)) {
					var5 = this.makeCorridor(var1, var3, var4, 0);
				}

				if (var5 != null) {
					var2.add(var5);
					((StructureTFComponent) var5).buildComponent(this, var2, var1);
				}
			}
		}
	}

	protected ComponentTFMazeDeadEnd makeDeadEnd(Random var1, int var2, int var3, int var4) {
		int var5 = this.boundingBox.minX + var2 * 5 + 1;
		int var6 = this.boundingBox.minY;
		int var7 = this.boundingBox.minZ + var3 * 5 + 1;
		int var8 = var1.nextInt(8);

		switch (var8) {
		case 0:
		default:
			return new ComponentTFMazeDeadEnd(4, var5, var6, var7, var4);

		case 1:
			return new ComponentTFMazeDeadEndChest(4, var5, var6, var7, var4);

		case 2:
			return new ComponentTFMazeDeadEndTrappedChest(4, var5, var6, var7, var4);

		case 3:
			return new ComponentTFMazeDeadEndTorches(4, var5, var6, var7, var4);

		case 4:
			return new ComponentTFMazeDeadEndFountain(4, var5, var6, var7, var4);

		case 5:
			return new ComponentTFMazeDeadEndFountainLava(4, var5, var6, var7, var4);

		case 6:
			return new ComponentTFMazeDeadEndPainting(4, var5, var6, var7, var4);

		case 7:
			return (ComponentTFMazeDeadEnd) (this.level == FLOOR_LEVEL ? 
					new ComponentTFMazeDeadEndRoots(4, var5, var6, var7, var4)
				: 
					new ComponentTFMazeDeadEndShrooms(4, var5, var6, var7, var4));
		}
	}

	protected ComponentTFMazeCorridor makeCorridor(Random var1, int var2, int var3, int var4) {
		int var5 = this.boundingBox.minX + var2 * 5 + 1;
		int var6 = this.boundingBox.minY;
		int var7 = this.boundingBox.minZ + var3 * 5 + 1;
		int var8 = var1.nextInt(5);

		switch (var8) {
		case 0:
		default:
			return null;

		case 1:
			return new ComponentTFMazeCorridor(4, var5, var6, var7, var4);

		case 2:
			return new ComponentTFMazeCorridorIronFence(4, var5, var6, var7, var4);

		case 3:
			return null;

		case 4:
			return (ComponentTFMazeCorridor) (this.level == 1
					? new ComponentTFMazeCorridorRoots(4, var5, var6, var7, var4)
					: new ComponentTFMazeCorridorShrooms(4, var5, var6, var7, var4));
		}
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current
	 * Location of StructGen
	 */
	public void buildComponent(StructureComponent var1, List<StructureComponent> var2, Random var3) {
		super.buildComponent(var1, var2, var3);
		int var4;
		int var5;

		if (this.level == FLOOR_LEVEL) {
			var4 = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2;
			var5 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2;
			ComponentTFMinotaurMaze var6 = new ComponentTFMinotaurMaze(1, var4, this.boundingBox.minY - 10, var5,
					this.rcoords[2], this.rcoords[3], 2);
			var2.add(var6);
			var6.buildComponent(this, var2, var3);
		}

		for (var4 = 0; var4 < this.rcoords.length / 2; ++var4) {
			var5 = this.rcoords[var4 * 2];
			int var8 = this.rcoords[var4 * 2 + 1];
			ComponentTFMazeRoom var7 = this.makeRoom(var3, var4, var5, var8);
			var2.add(var7);
			var7.buildComponent(this, var2, var3);
		}

		this.decorateDeadEndsCorridors(var3, var2);
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob
	 * Spawners, it closes Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3, boolean mostlySolid) {
		if (this.level == FLOOR_LEVEL + 1) {
			this.fillWithBlocks(var1, var3, 0, -1, 0, this.getDiameter() + 2, 6, this.getDiameter() + 2,
					Block.bedrock.blockID, 0, false);
		}

		this.func_74878_a(var1, var3, 1, 1, 1, this.getDiameter(), 4, this.getDiameter());
		this.func_74872_a(var1, var3, 1, 5, 1, this.getDiameter(), 5, this.getDiameter(), Block.mazeStone2.blockID, 0,
				Block.stone.blockID, 0, this.level == FLOOR_LEVEL);
		this.func_74872_a(var1, var3, 1, 0, 1, this.getDiameter(), 0, this.getDiameter(), Block.mazeStone2.blockID, 6,
				Block.stone.blockID, 0, false);
		this.maze.headBlockID = Block.mazeStone2.blockID;
		this.maze.headBlockMeta = 3;
		this.maze.wallBlockID = Block.mazeStone2.blockID;
		this.maze.wallBlockMeta = 1;
		this.maze.rootBlockID = Block.mazeStone2.blockID;
		this.maze.rootBlockMeta = 3;
		this.maze.pillarBlockID = Block.mazeStone2.blockID;
		this.maze.pillarBlockMeta = 2;
		this.maze.wallVar0ID = Block.mazeStone2.blockID;
		this.maze.wallVar0Meta = 4;
		this.maze.wallVarRarity = 0.2F;
		this.maze.torchRarity = 0.05F;
		this.maze.tall = 2;
		this.maze.head = 1;
		this.maze.roots = 1;
		this.maze.oddBias = 4;
		this.maze.copyToStructure(var1, var2, 1, 2, 1, this, var3);
		return true;
	}

	public int getMazeSize() {
		return 22;
	}

	public int getRadius() {
		return (int) ((double) this.getMazeSize() * 2.5D);
	}

	public int getDiameter() {
		return this.getMazeSize() * 5;
	}

	protected boolean isNearRoom(int var1, int var2, int[] var3, int var4) {
		if (var1 == 1 && var2 == 1) {
			return true;
		} else {
			for (int var5 = 0; var5 < var3.length / 2; ++var5) {
				int var6 = var3[var5 * 2];
				int var7 = var3[var5 * 2 + 1];

				if ((var6 != 0 || var7 != 0) && Math.abs(var1 - var6) < var4 && Math.abs(var2 - var7) < var4) {
					return true;
				}
			}

			return false;
		}
	}
}

package net.minecraft.world.level.levelgen;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenBigTree;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.minotaurmaze.StructureTFComponent;

public class TFMazeNew extends TFMaze {
	public int width;
	public int depth;
	public int oddBias = 3;
	public int evenBias = 1;
	public int tall = 3;
	public int head = 0;
	public int roots = 0;
	public int worldX;
	public int worldY;
	public int worldZ;
	public int type;
	public int wallBlockID;
	public int wallBlockMeta;
	public int wallVar0ID;
	public int wallVar0Meta;
	public float wallVarRarity;
	public int headBlockID;
	public int headBlockMeta;
	public int rootBlockID;
	public int rootBlockMeta;
	public int pillarBlockID;
	public int pillarBlockMeta;
	public int torchBlockID;
	public int torchBlockMeta;
	public float torchRarity;
	public int rawWidth;
	public int rawDepth;
	public int[] storage;
	public static final int OUT_OF_BOUNDS = Integer.MIN_VALUE;
	public static final int OOB = Integer.MIN_VALUE;
	public static final int ROOM = 5;
	public Random rand;

	public TFMazeNew(int var1, int var2) {
		super(var1, var2);
		this.wallBlockID = Block.mazeStone2.blockID;
		this.wallBlockMeta = 2;
		this.rootBlockID = Block.mazeStone2.blockID;
		this.rootBlockMeta = 0;
		this.torchBlockID = Block.torchWood.blockID;
		this.pillarBlockID = -1;
		this.torchBlockMeta = 0;
		this.torchRarity = 0.75F;
		this.width = var1;
		this.depth = var2;
		this.rawWidth = this.width * 2 + 1;
		this.rawDepth = this.depth * 2 + 1;
		this.storage = new int[this.rawWidth * this.rawDepth];
		this.rand = new Random();
	}

	public int getCell(int var1, int var2) {
		return this.getRaw(var1 * 2 + 1, var2 * 2 + 1);
	}

	public void putCell(int var1, int var2, int var3) {
		this.putRaw(var1 * 2 + 1, var2 * 2 + 1, var3);
	}

	public boolean cellEquals(int var1, int var2, int var3) {
		return this.getCell(var1, var2) == var3;
	}

	public int getWall(int var1, int var2, int var3, int var4) {
		if (var3 == var1 + 1 && var4 == var2) {
			return this.getRaw(var1 * 2 + 2, var2 * 2 + 1);
		} else if (var3 == var1 - 1 && var4 == var2) {
			return this.getRaw(var1 * 2 + 0, var2 * 2 + 1);
		} else if (var3 == var1 && var4 == var2 + 1) {
			return this.getRaw(var1 * 2 + 1, var2 * 2 + 2);
		} else if (var3 == var1 && var4 == var2 - 1) {
			return this.getRaw(var1 * 2 + 1, var2 * 2 + 0);
		} else {
			System.out.println("Wall check out of bounds; s = " + var1 + ", " + var2 + "; d = " + var3 + ", " + var4);
			return Integer.MIN_VALUE;
		}
	}

	public void putWall(int var1, int var2, int var3, int var4, int var5) {
		if (var3 == var1 + 1 && var4 == var2) {
			this.putRaw(var1 * 2 + 2, var2 * 2 + 1, var5);
		}

		if (var3 == var1 - 1 && var4 == var2) {
			this.putRaw(var1 * 2 + 0, var2 * 2 + 1, var5);
		}

		if (var3 == var1 && var4 == var2 + 1) {
			this.putRaw(var1 * 2 + 1, var2 * 2 + 2, var5);
		}

		if (var3 == var1 && var4 == var2 - 1) {
			this.putRaw(var1 * 2 + 1, var2 * 2 + 0, var5);
		}
	}

	public boolean isWall(int var1, int var2, int var3, int var4) {
		return this.getWall(var1, var2, var3, var4) == 0;
	}

	protected void putRaw(int var1, int var2, int var3) {
		if (var1 >= 0 && var1 < this.rawWidth && var2 >= 0 && var2 < this.rawDepth) {
			this.storage[var2 * this.rawWidth + var1] = var3;
		}
	}

	protected int getRaw(int var1, int var2) {
		return var1 >= 0 && var1 < this.rawWidth && var2 >= 0 && var2 < this.rawDepth
				? this.storage[var2 * this.rawWidth + var1]
				: Integer.MIN_VALUE;
	}

	public void setSeed(long var1) {
		this.rand.setSeed(var1);
	}

	public void copyToWorld(World var1, int var2, int var3, int var4) {
		this.worldX = var2;
		this.worldY = var3;
		this.worldZ = var4;

		for (int var5 = 0; var5 < this.rawWidth; ++var5) {
			for (int var6 = 0; var6 < this.rawDepth; ++var6) {
				if (this.getRaw(var5, var6) == 0) {
					int var7 = var2 + var5 / 2 * (this.evenBias + this.oddBias);
					int var8 = var4 + var6 / 2 * (this.evenBias + this.oddBias);
					int var9;

					if (this.isEven(var5) && this.isEven(var6)) {
						if (this.type == 4 && this.shouldTree(var5, var6)) {
							(new WorldGenBigTree()).generate(var1, this.rand, var7, var3, var8);
						} else {
							for (var9 = 0; var9 < this.head; ++var9) {
								this.putHeadBlock(var1, var7, var3 + this.tall + var9, var8);
							}

							for (var9 = 0; var9 < this.tall; ++var9) {
								this.putWallBlock(var1, var7, var3 + var9, var8);
							}

							for (var9 = 1; var9 <= this.roots; ++var9) {
								this.putRootBlock(var1, var7, var3 - var9, var8);
							}
						}
					}

					int var10;
					int var11;

					if (this.isEven(var5) && !this.isEven(var6)) {
						for (var9 = 0; var9 < this.evenBias; ++var9) {
							for (var10 = 1; var10 <= this.oddBias; ++var10) {
								for (var11 = 0; var11 < this.head; ++var11) {
									this.putHeadBlock(var1, var7 + var9, var3 + this.tall + var11, var8 + var10);
								}

								for (var11 = 0; var11 < this.tall; ++var11) {
									this.putWallBlock(var1, var7 + var9, var3 + var11, var8 + var10);
								}

								for (var11 = 1; var11 <= this.roots; ++var11) {
									this.putRootBlock(var1, var7 + var9, var3 - var11, var8 + var10);
								}
							}
						}
					}

					if (!this.isEven(var5) && this.isEven(var6)) {
						for (var9 = 0; var9 < this.evenBias; ++var9) {
							for (var10 = 1; var10 <= this.oddBias; ++var10) {
								for (var11 = 0; var11 < this.head; ++var11) {
									this.putHeadBlock(var1, var7 + var10, var3 + this.tall + var11, var8 + var9);
								}

								for (var11 = 0; var11 < this.tall; ++var11) {
									this.putWallBlock(var1, var7 + var10, var3 + var11, var8 + var9);
								}

								for (var11 = 1; var11 <= this.roots; ++var11) {
									this.putRootBlock(var1, var7 + var10, var3 - var11, var8 + var9);
								}
							}
						}
					}
				}
			}
		}

		this.placeTorches(var1);
	}

	public void carveToWorld(World var1, int var2, int var3, int var4) {
		this.worldX = var2;
		this.worldY = var3;
		this.worldZ = var4;

		for (int var5 = 0; var5 < this.rawWidth; ++var5) {
			for (int var6 = 0; var6 < this.rawDepth; ++var6) {
				if (this.getRaw(var5, var6) != 0) {
					int var7 = var2 + var5 / 2 * (this.evenBias + this.oddBias);
					int var8 = var4 + var6 / 2 * (this.evenBias + this.oddBias);
					int var9;

					if (this.isEven(var5) && this.isEven(var6)) {
						for (var9 = 0; var9 < this.tall; ++var9) {
							this.carveBlock(var1, var7, var3 + var9, var8);
						}
					} else {
						int var10;

						if (this.isEven(var5) && !this.isEven(var6)) {
							for (var9 = 1; var9 <= this.oddBias; ++var9) {
								for (var10 = 0; var10 < this.tall; ++var10) {
									this.carveBlock(var1, var7, var3 + var10, var8 + var9);
								}
							}
						} else if (!this.isEven(var5) && this.isEven(var6)) {
							for (var9 = 1; var9 <= this.oddBias; ++var9) {
								for (var10 = 0; var10 < this.tall; ++var10) {
									this.carveBlock(var1, var7 + var9, var3 + var10, var8);
								}
							}
						} else if (!this.isEven(var5) && !this.isEven(var6)) {
							for (var9 = 1; var9 <= this.oddBias; ++var9) {
								for (var10 = 1; var10 <= this.oddBias; ++var10) {
									for (int var11 = 0; var11 < this.tall; ++var11) {
										this.carveBlock(var1, var7 + var9, var3 + var11, var8 + var10);
									}
								}
							}
						}
					}
				}
			}
		}

		this.placeTorches(var1);
	}

	public void copyToStructure(World var1, Random var2, int var3, int var4, int var5, StructureTFComponent var6,
			StructureBoundingBox var7) {
		int var8;
		int var9;
		int var10;
		int var11;
		int var12;

		for (var8 = 0; var8 < this.rawWidth; ++var8) {
			for (var9 = 0; var9 < this.rawDepth; ++var9) {
				if (this.getRaw(var8, var9) == 0) {
					var10 = var3 + var8 / 2 * (this.evenBias + this.oddBias);
					var11 = var5 + var9 / 2 * (this.evenBias + this.oddBias);

					if (this.evenBias > 1) {
						--var10;
						--var11;
					}

					int var13;
					int var14;

					if (this.isEven(var8) && this.isEven(var9)) {
						if (this.type == 4 && this.shouldTree(var8, var9)) {
							this.putCanopyTree(var1, var10, var4, var11, var6, var7);
						} else {
							for (var12 = 0; var12 < this.evenBias; ++var12) {
								for (var13 = 0; var13 < this.evenBias; ++var13) {
									for (var14 = 0; var14 < this.head; ++var14) {
										this.putHeadBlock(var1, var10 + var12, var4 + this.tall + var14, var11 + var13,
												var6, var7);
									}

									for (var14 = 0; var14 < this.tall; ++var14) {
										if (this.shouldPillar(var8, var9)) {
											this.putPillarBlock(var1, var10 + var12, var4 + var14, var11 + var13, var6,
													var7);
										} else {
											this.putWallBlock(var1, var10 + var12, var4 + var14, var11 + var13, var6,
													var7);
										}
									}

									for (var14 = 1; var14 <= this.roots; ++var14) {
										this.putRootBlock(var1, var10 + var12, var4 - var14, var11 + var13, var6, var7);
									}
								}
							}
						}
					}

					if (this.isEven(var8) && !this.isEven(var9)) {
						for (var12 = 0; var12 < this.evenBias; ++var12) {
							for (var13 = 1; var13 <= this.oddBias; ++var13) {
								for (var14 = 0; var14 < this.head; ++var14) {
									this.putHeadBlock(var1, var10 + var12, var4 + this.tall + var14, var11 + var13,
											var6, var7);
								}

								for (var14 = 0; var14 < this.tall; ++var14) {
									this.putWallBlock(var1, var10 + var12, var4 + var14, var11 + var13, var6, var7);
								}

								for (var14 = 1; var14 <= this.roots; ++var14) {
									this.putRootBlock(var1, var10 + var12, var4 - var14, var11 + var13, var6, var7);
								}
							}
						}
					}

					if (!this.isEven(var8) && this.isEven(var9)) {
						for (var12 = 0; var12 < this.evenBias; ++var12) {
							for (var13 = 1; var13 <= this.oddBias; ++var13) {
								for (var14 = 0; var14 < this.head; ++var14) {
									this.putHeadBlock(var1, var10 + var13, var4 + this.tall + var14, var11 + var12,
											var6, var7);
								}

								for (var14 = 0; var14 < this.tall; ++var14) {
									this.putWallBlock(var1, var10 + var13, var4 + var14, var11 + var12, var6, var7);
								}

								for (var14 = 1; var14 <= this.roots; ++var14) {
									this.putRootBlock(var1, var10 + var13, var4 - var14, var11 + var12, var6, var7);
								}
							}
						}
					}
				}
			}
		}

		for (var8 = 0; var8 < this.rawWidth; ++var8) {
			for (var9 = 0; var9 < this.rawDepth; ++var9) {
				if (this.getRaw(var8, var9) == 0) {
					var10 = var3 + var8 / 2 * (this.evenBias + this.oddBias);
					var11 = var4 + 1;
					var12 = var5 + var9 / 2 * (this.evenBias + this.oddBias);

					if (this.isEven(var8) && this.isEven(var9) && this.shouldTorch(var8, var9)
							&& var6.getBlockIdAtCurrentPosition(var1, var10, var11, var12, var7) == this.wallBlockID) {
						var6.placeBlockAtCurrentPosition(var1, this.torchBlockID, this.torchBlockMeta, var10, var11,
								var12, var7);
					}
				}
			}
		}
	}

	protected void putPillarBlock(World var1, int var2, int var3, int var4, StructureTFComponent var5,
			StructureBoundingBox var6) {
		var5.placeBlockAtCurrentPosition(var1, this.pillarBlockID, this.pillarBlockMeta, var2, var3, var4, var6);
	}

	protected void putWallBlock(World var1, int var2, int var3, int var4) {
		var1.setBlockAndMetadataWithNotify(var2, var3, var4, this.wallBlockID, this.wallBlockMeta);
	}

	protected void putWallBlock(World var1, int var2, int var3, int var4, StructureTFComponent var5,
			StructureBoundingBox var6) {
		if (this.wallVarRarity > 0.0F && this.rand.nextFloat() < this.wallVarRarity) {
			var5.placeBlockAtCurrentPosition(var1, this.wallVar0ID, this.wallVar0Meta, var2, var3, var4, var6);
		} else {
			var5.placeBlockAtCurrentPosition(var1, this.wallBlockID, this.wallBlockMeta, var2, var3, var4, var6);
		}
	}

	protected void carveBlock(World var1, int var2, int var3, int var4) {
		var1.setBlockAndMetadataWithNotify(var2, var3, var4, 0, 0);
	}

	protected void putHeadBlock(World var1, int var2, int var3, int var4) {
		var1.setBlockAndMetadataWithNotify(var2, var3, var4, this.headBlockID, this.headBlockMeta);
	}

	protected void putHeadBlock(World var1, int var2, int var3, int var4, StructureTFComponent var5,
			StructureBoundingBox var6) {
		var5.placeBlockAtCurrentPosition(var1, this.headBlockID, this.headBlockMeta, var2, var3, var4, var6);
	}

	protected void putRootBlock(World var1, int var2, int var3, int var4) {
		var1.setBlockAndMetadataWithNotify(var2, var3, var4, this.rootBlockID, this.rootBlockMeta);
	}

	protected void putRootBlock(World var1, int var2, int var3, int var4, StructureTFComponent var5,
			StructureBoundingBox var6) {
		var5.placeBlockAtCurrentPosition(var1, this.rootBlockID, this.rootBlockMeta, var2, var3, var4, var6);
	}

	protected void putCanopyTree(World var1, int var2, int var3, int var4, StructureTFComponent var5,
			StructureBoundingBox var6) {
		int var7 = var5.getXWithOffset(var2, var4);
		int var8 = var5.getYWithOffset(var3);
		int var9 = var5.getZWithOffset(var2, var4);

		if (var6.isVecInside(var7, var8, var9)) {
			(new WorldGenBigTree()).generate(var1, this.rand, var7, var8, var9);
		}
	}

	public void placeTorches(World var1) {
		byte var2 = 1;

		for (int var3 = 0; var3 < this.rawWidth; ++var3) {
			for (int var4 = 0; var4 < this.rawDepth; ++var4) {
				if (this.getRaw(var3, var4) == 0) {
					int var5 = this.worldX + var3 / 2 * (this.evenBias + this.oddBias);
					int var6 = this.worldY + var2;
					int var7 = this.worldZ + var4 / 2 * (this.evenBias + this.oddBias);

					if (this.isEven(var3) && this.isEven(var4) && this.shouldTorch(var3, var4)
							&& var1.getBlockId(var5, var6, var7) == this.wallBlockID) {
						var1.setBlockAndMetadataWithNotify(var5, var6, var7, this.torchBlockID, this.torchBlockMeta);
					}
				}
			}
		}
	}

	public boolean shouldTorch(int var1, int var2) {
		return this.getRaw(var1 + 1, var2) != Integer.MIN_VALUE && this.getRaw(var1 - 1, var2) != Integer.MIN_VALUE
				&& this.getRaw(var1, var2 + 1) != Integer.MIN_VALUE && this.getRaw(var1, var2 - 1) != Integer.MIN_VALUE
						? ((this.getRaw(var1 + 1, var2) != 0 || this.getRaw(var1 - 1, var2) != 0)
								&& (this.getRaw(var1, var2 + 1) != 0 || this.getRaw(var1, var2 - 1) != 0)
										? this.rand.nextFloat() <= this.torchRarity
										: false)
						: false;
	}

	public boolean shouldPillar(int var1, int var2) {
		return this.pillarBlockID == -1 ? false
				: (this.getRaw(var1 + 1, var2) != Integer.MIN_VALUE && this.getRaw(var1 - 1, var2) != Integer.MIN_VALUE
						&& this.getRaw(var1, var2 + 1) != Integer.MIN_VALUE
						&& this.getRaw(var1, var2 - 1) != Integer.MIN_VALUE
								? (this.getRaw(var1 + 1, var2) != 0 || this.getRaw(var1 - 1, var2) != 0)
										&& (this.getRaw(var1, var2 + 1) != 0 || this.getRaw(var1, var2 - 1) != 0)
								: false);
	}

	public boolean shouldTree(int var1, int var2) {
		return (var1 == 0 || var1 == this.rawWidth - 1)
				&& (this.getRaw(var1, var2 + 1) != 0 || this.getRaw(var1, var2 - 1) != 0)
						? true
						: ((var2 == 0 || var2 == this.rawDepth - 1)
								&& (this.getRaw(var1 + 1, var2) != 0 || this.getRaw(var1 - 1, var2) != 0) ? true
										: this.rand.nextInt(50) == 0);
	}

	public int getWorldX(int var1) {
		return this.worldX + var1 * (this.evenBias + this.oddBias) + 1;
	}

	public int getWorldZ(int var1) {
		return this.worldZ + var1 * (this.evenBias + this.oddBias) + 1;
	}

	public void carveRoom0(int var1, int var2) {
		this.putCell(var1, var2, 5);
		this.putCell(var1 + 1, var2, 5);
		this.putWall(var1, var2, var1 + 1, var2, 5);
		this.putCell(var1 - 1, var2, 5);
		this.putWall(var1, var2, var1 - 1, var2, 5);
		this.putCell(var1, var2 + 1, 5);
		this.putWall(var1, var2, var1, var2 + 1, 5);
		this.putCell(var1, var2 - 1, 5);
		this.putWall(var1, var2, var1, var2 - 1, 5);
	}

	public void carveRoom1(int var1, int var2) {
		int var3 = var1 * 2 + 1;
		int var4 = var2 * 2 + 1;

		for (int var5 = -2; var5 <= 2; ++var5) {
			for (int var6 = -2; var6 <= 2; ++var6) {
				this.putRaw(var3 + var5, var4 + var6, 5);
			}
		}

		this.putCell(var3, var4 + 1, 0);
		this.putCell(var3, var4 - 1, 0);
		this.putCell(var3 + 1, var4, 0);
		this.putCell(var3 - 1, var4, 0);

		if (this.getRaw(var3, var4 + 4) != Integer.MIN_VALUE) {
			this.putRaw(var3, var4 + 3, 5);
		}

		if (this.getRaw(var3, var4 - 4) != Integer.MIN_VALUE) {
			this.putRaw(var3, var4 - 3, 5);
		}

		if (this.getRaw(var3 + 4, var4) != Integer.MIN_VALUE) {
			this.putRaw(var3 + 3, var4, 5);
		}

		if (this.getRaw(var3 - 4, var4) != Integer.MIN_VALUE) {
			this.putRaw(var3 - 3, var4, 5);
		}
	}

	public void add4Exits() {
		int var1 = this.rawWidth / 2 + 1;
		int var2 = this.rawDepth / 2 + 1;
		this.putRaw(var1, 0, 5);
		this.putRaw(var1, this.rawDepth - 1, 5);
		this.putRaw(0, var2, 5);
		this.putRaw(this.rawWidth - 1, var2, 5);
	}

	public void generateRecursiveBacktracker(int var1, int var2) {
		this.rbGen(var1, var2);
	}

	public void rbGen(int var1, int var2) {
		this.putCell(var1, var2, 1);
		int var3 = 0;

		if (this.cellEquals(var1 + 1, var2, 0)) {
			++var3;
		}

		if (this.cellEquals(var1 - 1, var2, 0)) {
			++var3;
		}

		if (this.cellEquals(var1, var2 + 1, 0)) {
			++var3;
		}

		if (this.cellEquals(var1, var2 - 1, 0)) {
			++var3;
		}

		if (var3 != 0) {
			int var4 = this.rand.nextInt(var3);
			int var6 = 0;
			int var5 = 0;

			if (this.cellEquals(var1 + 1, var2, 0)) {
				if (var4 == 0) {
					var5 = var1 + 1;
					var6 = var2;
				}

				--var4;
			}

			if (this.cellEquals(var1 - 1, var2, 0)) {
				if (var4 == 0) {
					var5 = var1 - 1;
					var6 = var2;
				}

				--var4;
			}

			if (this.cellEquals(var1, var2 + 1, 0)) {
				if (var4 == 0) {
					var5 = var1;
					var6 = var2 + 1;
				}

				--var4;
			}

			if (this.cellEquals(var1, var2 - 1, 0) && var4 == 0) {
				var5 = var1;
				var6 = var2 - 1;
			}

			this.putWall(var1, var2, var5, var6, 2);
			this.rbGen(var5, var6);
			this.rbGen(var1, var2);
			this.rbGen(var1, var2);
		}
	}
}

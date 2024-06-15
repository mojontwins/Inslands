package net.minecraft.src;

import java.util.Random;

public class AetherGenDungeonSilver extends AetherGenBuildings {
	private int baseMeta1;
	private int baseMeta2;
	private int lockedBlockID1;
	private int lockedBlockID2;
	private int wallBlockID1;
	private int wallBlockID2;
	private int baseID1;
	private int baseID2;
	private int columnID;
	private int[][][] rooms = new int[3][3][3];

	public AetherGenDungeonSilver(int i, int j, int k, int l, int m, int m1, int o, int o1, int p) {
		this.lockedBlockID1 = i;
		this.lockedBlockID2 = j;
		this.wallBlockID1 = k;
		this.wallBlockID2 = l;
		this.baseID1 = m;
		this.baseMeta1 = m1;
		this.baseID2 = o;
		this.baseMeta2 = o1;
		this.columnID = p;
	}

	public boolean generate(World world, Random random, int i, int j, int k) {
		this.replaceAir = true;
		if(!this.isBoxEmpty(world, i, j, k, 55, 20, 30)) {
			return false;
		} else {
			int row;
			int x;
			int y;
			int z;
			if(world.getBlockId(i, j - 5, k) == 0 || world.getBlockId(i + 55, j - 5, k) == 0 || world.getBlockId(i, j - 5, k + 30) == 0 || world.getBlockId(i + 55, j - 5, k + 30) == 0) {
				for(row = 0; row < 100; ++row) {
					x = i - 11 + random.nextInt(77);
					y = j - 7;
					z = k - 10 + random.nextInt(50);
					(new AetherGenClouds(AetherBlocks.Aercloud.blockID, 0, 10, false)).generate(world, random, x, y, z);
				}
			}

			this.replaceSolid = true;
			this.setBlocks(this.baseID2, this.baseID1, 30);
			this.setMetadata(this.baseMeta2, this.baseMeta1);
			this.addSolidBox(world, random, i, j - 5, k, 55, 5, 30);

			for(row = i; row < i + 55; row += 4) {
				this.addColumn(world, random, row, j, k, 14);
				this.addColumn(world, random, row, j, k + 27, 14);
			}

			for(row = k; row < k + 12; row += 4) {
				this.addColumn(world, random, i, j, row, 14);
				this.addColumn(world, random, i + 52, j, row, 14);
			}

			for(row = k + 19; row < k + 30; row += 4) {
				this.addColumn(world, random, i, j, row, 14);
				this.addColumn(world, random, i + 52, j, row, 14);
			}

			this.setBlocks(this.lockedBlockID1, this.lockedBlockID2, 20);
			this.setMetadata(1, 1);
			this.addHollowBox(world, random, i + 4, j - 1, k + 4, 47, 16, 22);
			this.addPlaneX(world, random, i + 11, j, k + 5, 15, 20);
			this.addPlaneX(world, random, i + 18, j, k + 5, 15, 20);
			this.addPlaneX(world, random, i + 25, j, k + 5, 15, 20);
			this.addPlaneZ(world, random, i + 5, j, k + 11, 20, 15);
			this.addPlaneZ(world, random, i + 5, j, k + 18, 20, 15);
			this.setBlocks(this.lockedBlockID1, AetherBlocks.Trap.blockID, 15);
			this.setMetadata(1, 1);
			this.addPlaneY(world, random, i + 5, j + 4, k + 5, 20, 20);
			this.addPlaneY(world, random, i + 5, j + 9, k + 5, 20, 20);

			for(row = j; row < j + 2; ++row) {
				for(x = k + 14; x < k + 16; ++x) {
					world.setBlock(i + 4, row, x, 0);
				}
			}

			this.setBlocks(0, 0, 1);
			this.addSolidBox(world, random, i, j - 4, k + 14, 1, 4, 2);
			this.addSolidBox(world, random, i + 1, j - 3, k + 14, 1, 3, 2);
			this.addSolidBox(world, random, i + 2, j - 2, k + 14, 1, 2, 2);
			this.addSolidBox(world, random, i + 3, j - 1, k + 14, 1, 1, 2);
			this.setBlocks(this.lockedBlockID1, this.lockedBlockID2, 20);
			this.setMetadata(1, 1);

			for(row = 0; row < 7; ++row) {
				this.addPlaneY(world, random, i - 1, j + 15 + row, k - 1 + 2 * row, 57, 32 - 4 * row);
			}

			row = random.nextInt(3);
			this.addStaircase(world, random, i + 19, j, k + 5 + row * 7, 10);
			this.rooms[2][0][row] = 2;
			this.rooms[2][1][row] = 2;
			this.rooms[2][2][row] = 1;
			x = i + 25;

			for(y = j; y < j + 2; ++y) {
				for(z = k + 7 + 7 * row; z < k + 9 + 7 * row; ++z) {
					world.setBlock(x, y, z, 0);
				}
			}

			row = random.nextInt(3);
			this.addStaircase(world, random, i + 12, j, k + 5 + row * 7, 5);
			this.rooms[1][0][row] = 1;
			this.rooms[1][1][row] = 1;
			row = random.nextInt(3);
			this.addStaircase(world, random, i + 5, j + 5, k + 5 + row * 7, 5);
			this.rooms[0][0][row] = 1;
			this.rooms[0][1][row] = 1;

			int valk;
			int p;
			for(valk = 0; valk < 3; ++valk) {
				for(int chest = 0; chest < 3; ++chest) {
					label298:
					for(p = 0; p < 3; ++p) {
						int item = this.rooms[valk][chest][p];
						int roomType;
						if(valk + 1 < 3 && (item == 0 || item == 1 || random.nextBoolean()) && item != 2) {
							roomType = this.rooms[valk + 1][chest][p];
							if(roomType != 2 && (roomType != 1 || item != 1)) {
								this.rooms[valk][chest][p] = 3;
								item = 3;
								x = i + 11 + 7 * valk;

								for(y = j + 5 * chest; y < j + 2 + 5 * chest; ++y) {
									for(z = k + 7 + 7 * p; z < k + 9 + 7 * p; ++z) {
										world.setBlock(x, y, z, 0);
									}
								}
							}
						}

						if(valk - 1 > 0 && (item == 0 || item == 1 || random.nextBoolean()) && item != 2) {
							roomType = this.rooms[valk - 1][chest][p];
							if(roomType != 2 && (roomType != 1 || item != 1)) {
								this.rooms[valk][chest][p] = 4;
								item = 4;
								x = i + 4 + 7 * valk;

								for(y = j + 5 * chest; y < j + 2 + 5 * chest; ++y) {
									for(z = k + 7 + 7 * p; z < k + 9 + 7 * p; ++z) {
										world.setBlock(x, y, z, 0);
									}
								}
							}
						}

						if(p + 1 < 3 && (item == 0 || item == 1 || random.nextBoolean()) && item != 2) {
							roomType = this.rooms[valk][chest][p + 1];
							if(roomType != 2 && (roomType != 1 || item != 1)) {
								this.rooms[valk][chest][p] = 5;
								item = 5;
								z = k + 11 + 7 * p;

								for(y = j + 5 * chest; y < j + 2 + 5 * chest; ++y) {
									for(x = i + 7 + 7 * valk; x < i + 9 + 7 * valk; ++x) {
										world.setBlock(x, y, z, 0);
									}
								}
							}
						}

						if(p - 1 > 0 && (item == 0 || item == 1 || random.nextBoolean()) && item != 2) {
							roomType = this.rooms[valk][chest][p - 1];
							if(roomType != 2 && (roomType != 1 || item != 1)) {
								this.rooms[valk][chest][p] = 6;
								item = 6;
								z = k + 4 + 7 * p;

								for(y = j + 5 * chest; y < j + 2 + 5 * chest; ++y) {
									for(x = i + 7 + 7 * valk; x < i + 9 + 7 * valk; ++x) {
										world.setBlock(x, y, z, 0);
									}
								}
							}
						}

						roomType = random.nextInt(3);
						if(item >= 3) {
							switch(roomType) {
							case 0:
								world.setBlockAndMetadata(i + 7 + valk * 7, j - 1 + chest * 5, k + 7 + p * 7, AetherBlocks.Trap.blockID, 1);
								break;
							case 1:
								this.addPlaneY(world, random, i + 7 + 7 * valk, j + 5 * chest, k + 7 + 7 * p, 2, 2);
								int u = i + 7 + 7 * valk + random.nextInt(2);
								int v = k + 7 + 7 * p + random.nextInt(2);
								if(world.getBlockId(u, j + 5 * chest + 1, v) != 0) {
									break;
								}

								world.setBlockWithNotify(u, j + 5 * chest + 1, v, Block.chest.blockID);
								TileEntityChest chest1 = (TileEntityChest)world.getBlockTileEntity(u, j + 5 * chest + 1, v);
								u = 0;

								while(true) {
									if(u >= 3 + random.nextInt(3)) {
										continue label298;
									}

									ItemStack item1 = this.getNormalLoot(random);
									chest1.setInventorySlotContents(random.nextInt(chest1.getSizeInventory()), item1);
									++u;
								}
							case 2:
								this.addPlaneY(world, random, i + 7 + 7 * valk, j + 5 * chest, k + 7 + 7 * p, 2, 2);
								world.setBlockWithNotify(i + 7 + 7 * valk + random.nextInt(2), j + 5 * chest + 1, k + 7 + 7 * p + random.nextInt(2), AetherBlocks.ChestMimic.blockID);
								world.setBlockWithNotify(i + 7 + 7 * valk + random.nextInt(2), j + 5 * chest + 1, k + 7 + 7 * p + random.nextInt(2), AetherBlocks.ChestMimic.blockID);
							}
						}
					}
				}
			}

			for(x = 0; x < 24; ++x) {
				for(z = 0; z < 20; ++z) {
					valk = (int)(Math.sqrt((double)(x * x + (z - 7) * (z - 7))) + Math.sqrt((double)(x * x + (z - 12) * (z - 12))));
					if(valk == 21) {
						world.setBlockAndMetadata(i + 26 + x, j, k + 5 + z, this.lockedBlockID2, 1);
					} else if(valk > 21) {
						world.setBlockAndMetadata(i + 26 + x, j, k + 5 + z, this.lockedBlockID1, 1);
					}
				}
			}

			this.setBlocks(this.lockedBlockID1, this.lockedBlockID1, 1);
			this.setMetadata(1, 1);
			this.addPlaneY(world, random, i + 44, j + 1, k + 11, 6, 8);
			this.addSolidBox(world, random, i + 46, j + 2, k + 13, 4, 2, 4);
			this.addLineX(world, random, i + 46, j + 4, k + 13, 4);
			this.addLineX(world, random, i + 46, j + 4, k + 16, 4);
			this.addPlaneX(world, random, i + 49, j + 4, k + 13, 4, 4);
			this.setBlocks(Block.cloth.blockID, Block.cloth.blockID, 1);
			this.setMetadata(11, 11);
			this.addPlaneY(world, random, i + 47, j + 3, k + 14, 2, 2);

			for(x = 0; x < 2; ++x) {
				for(z = 0; z < 2; ++z) {
					world.setBlock(i + 44 + x * 5, j + 2, k + 11 + z * 7, AetherBlocks.AmbrosiumTorch.blockID);
				}
			}

			this.setBlocks(Block.waterStill.blockID, Block.waterStill.blockID, 1);
			this.setMetadata(0, 0);
			this.addPlaneY(world, random, i + 35, j + 1, k + 5, 6, 3);
			this.addPlaneY(world, random, i + 35, j + 1, k + 22, 6, 3);
			this.setBlocks(this.lockedBlockID1, this.lockedBlockID1, 1);
			this.setMetadata(1, 1);
			this.addLineZ(world, random, i + 34, j + 1, k + 5, 2);
			this.addLineZ(world, random, i + 41, j + 1, k + 5, 2);
			this.addLineX(world, random, i + 36, j + 1, k + 8, 4);
			world.setBlockAndMetadata(i + 35, j + 1, k + 7, this.lockedBlockID1, 1);
			world.setBlockAndMetadata(i + 40, j + 1, k + 7, this.lockedBlockID1, 1);
			this.addLineZ(world, random, i + 34, j + 1, k + 23, 2);
			this.addLineZ(world, random, i + 41, j + 1, k + 23, 2);
			this.addLineX(world, random, i + 36, j + 1, k + 21, 4);
			world.setBlockAndMetadata(i + 35, j + 1, k + 22, this.lockedBlockID1, 1);
			world.setBlockAndMetadata(i + 40, j + 1, k + 22, this.lockedBlockID1, 1);

			for(x = i + 36; x < i + 40; x += 3) {
				for(z = k + 8; z < k + 22; z += 13) {
					world.setBlock(x, j + 2, z, AetherBlocks.AmbrosiumTorch.blockID);
				}
			}

			this.addChandelier(world, i + 28, j, k + 10, 8);
			this.addChandelier(world, i + 43, j, k + 10, 8);
			this.addChandelier(world, i + 43, j, k + 19, 8);
			this.addChandelier(world, i + 28, j, k + 19, 8);
			this.addSapling(world, random, i + 45, j + 1, k + 6);
			this.addSapling(world, random, i + 45, j + 1, k + 21);
			EntityValkyrie entityValkyrie19 = new EntityValkyrie(world, (double)i + 40.0D, (double)j + 1.5D, (double)k + 15.0D, true);
			entityValkyrie19.setPosition((double)(i + 40), (double)(j + 2), (double)(k + 15));
			entityValkyrie19.setDungeon(i + 26, j, k + 5);
			world.entityJoinedWorld(entityValkyrie19);
			this.setBlocks(this.lockedBlockID1, this.lockedBlockID1, 1);
			this.setMetadata(1, 1);
			this.addHollowBox(world, random, i + 41, j - 2, k + 13, 4, 4, 4);
			x = i + 42 + random.nextInt(2);
			z = k + 14 + random.nextInt(2);
			world.setBlock(x, j - 1, z, AetherBlocks.TreasureChest.blockID);
			TileEntityChest tileEntityChest20 = (TileEntityChest)((TileEntityChest)world.getBlockTileEntity(x, j - 1, z));

			for(p = 0; p < 3 + random.nextInt(3); ++p) {
				ItemStack itemStack21 = this.getSilverLoot(random);
				tileEntityChest20.setInventorySlotContents(random.nextInt(tileEntityChest20.getSizeInventory()), itemStack21);
			}

			world.setBlockMetadata(x, j - 1, z, 2);
			return true;
		}
	}

	private void addSapling(World world, Random random, int i, int j, int k) {
		this.setBlocks(this.lockedBlockID1, this.lockedBlockID1, 1);
		this.setMetadata(1, 1);
		this.addPlaneY(world, random, i, j, k, 3, 3);
		world.setBlock(i + 1, j, k + 1, AetherBlocks.Dirt.blockID);
		world.setBlock(i + 1, j + 1, k + 1, AetherBlocks.GoldenOakSapling.blockID);

		for(int x = i; x < i + 3; x += 2) {
			for(int z = k; z < k + 3; z += 2) {
				world.setBlock(x, j + 1, z, AetherBlocks.AmbrosiumTorch.blockID);
			}
		}

	}

	private void addChandelier(World world, int i, int j, int k, int h) {
		int z;
		for(z = j + h + 3; z < j + h + 6; ++z) {
			world.setBlock(i, z, k, Block.fence.blockID);
		}

		for(z = i - 1; z < i + 2; ++z) {
			world.setBlock(z, j + h + 1, k, Block.glowStone.blockID);
		}

		for(z = j + h; z < j + h + 3; ++z) {
			world.setBlock(i, z, k, Block.glowStone.blockID);
		}

		for(z = k - 1; z < k + 2; ++z) {
			world.setBlock(i, j + h + 1, z, Block.glowStone.blockID);
		}

	}

	private void addColumn(World world, Random random, int i, int j, int k, int h) {
		this.setBlocks(this.wallBlockID1, this.wallBlockID2, 20);
		this.setMetadata(1, 1);
		this.addPlaneY(world, random, i, j, k, 3, 3);
		this.addPlaneY(world, random, i, j + h, k, 3, 3);
		this.setBlocks(this.columnID, this.columnID, 1);
		this.setMetadata(0, 0);
		this.addLineY(world, random, i + 1, j, k + 1, h - 1);
		world.setBlockAndMetadata(i + 1, j + h - 1, k + 1, this.columnID, 1);
	}

	private void addStaircase(World world, Random random, int i, int j, int k, int h) {
		this.setBlocks(0, 0, 1);
		this.addSolidBox(world, random, i + 1, j, k + 1, 4, h, 4);
		this.setBlocks(this.lockedBlockID1, this.lockedBlockID2, 5);
		this.setMetadata(1, 1);
		this.addSolidBox(world, random, i + 2, j, k + 2, 2, h + 4, 2);
		world.setBlock(i + 1, j + 0, k + 1, Block.stairSingle.blockID);
		world.setBlock(i + 2, j + 0, k + 1, Block.stairDouble.blockID);
		world.setBlock(i + 3, j + 1, k + 1, Block.stairSingle.blockID);
		world.setBlock(i + 4, j + 1, k + 1, Block.stairDouble.blockID);
		world.setBlock(i + 4, j + 2, k + 2, Block.stairSingle.blockID);
		world.setBlock(i + 4, j + 2, k + 3, Block.stairDouble.blockID);
		world.setBlock(i + 4, j + 3, k + 4, Block.stairSingle.blockID);
		world.setBlock(i + 3, j + 3, k + 4, Block.stairDouble.blockID);
		world.setBlock(i + 2, j + 4, k + 4, Block.stairSingle.blockID);
		world.setBlock(i + 1, j + 4, k + 4, Block.stairDouble.blockID);
		if(h != 5) {
			world.setBlock(i + 1, j + 5, k + 3, Block.stairSingle.blockID);
			world.setBlock(i + 1, j + 5, k + 2, Block.stairDouble.blockID);
			world.setBlock(i + 1, j + 6, k + 1, Block.stairSingle.blockID);
			world.setBlock(i + 2, j + 6, k + 1, Block.stairDouble.blockID);
			world.setBlock(i + 3, j + 7, k + 1, Block.stairSingle.blockID);
			world.setBlock(i + 4, j + 7, k + 1, Block.stairDouble.blockID);
			world.setBlock(i + 4, j + 8, k + 2, Block.stairSingle.blockID);
			world.setBlock(i + 4, j + 8, k + 3, Block.stairDouble.blockID);
			world.setBlock(i + 4, j + 9, k + 4, Block.stairSingle.blockID);
			world.setBlock(i + 3, j + 9, k + 4, Block.stairDouble.blockID);
		}
	}

	private ItemStack getNormalLoot(Random random) {
		int item = random.nextInt(15);
		switch(item) {
		case 0:
			return new ItemStack(AetherItems.PickZanite);
		case 1:
			return new ItemStack(AetherItems.Bucket, 1, 2);
		case 2:
			return new ItemStack(AetherItems.DartShooter);
		case 3:
			return new ItemStack(AetherItems.MoaEgg, 1, 0);
		case 4:
			return new ItemStack(AetherItems.AmbrosiumShard, random.nextInt(10) + 1);
		case 5:
			return new ItemStack(AetherItems.Dart, random.nextInt(5) + 1, 0);
		case 6:
			return new ItemStack(AetherItems.Dart, random.nextInt(3) + 1, 1);
		case 7:
			return new ItemStack(AetherItems.Dart, random.nextInt(3) + 1, 2);
		case 8:
			if(random.nextInt(20) == 0) {
				return new ItemStack(AetherItems.BlueMusicDisk);
			}
			break;
		case 9:
			return new ItemStack(AetherItems.Bucket);
		case 10:
			if(random.nextInt(10) == 0) {
				return new ItemStack(Item.itemsList[Item.record13.shiftedIndex + random.nextInt(2)]);
			}
			break;
		case 11:
			if(random.nextInt(2) == 0) {
				return new ItemStack(AetherItems.ZaniteBoots);
			}

			if(random.nextInt(2) == 0) {
				return new ItemStack(AetherItems.ZaniteHelmet);
			}

			if(random.nextInt(2) == 0) {
				return new ItemStack(AetherItems.ZaniteLeggings);
			}

			if(random.nextInt(2) == 0) {
				return new ItemStack(AetherItems.ZaniteChestplate);
			}
			break;
		case 12:
			if(random.nextInt(4) == 0) {
				return new ItemStack(AetherItems.IronPendant);
			}
		case 13:
			if(random.nextInt(10) == 0) {
				return new ItemStack(AetherItems.GoldPendant);
			}
		case 14:
			if(random.nextInt(15) == 0) {
				return new ItemStack(AetherItems.ZaniteRing);
			}
		}

		return new ItemStack(AetherBlocks.AmbrosiumTorch, random.nextInt(5));
	}

	private ItemStack getSilverLoot(Random random) {
		int item = random.nextInt(9);
		switch(item) {
		case 0:
			return new ItemStack(AetherItems.GummieSwet, random.nextInt(16));
		case 1:
			return new ItemStack(AetherItems.SwordLightning);
		case 2:
			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.AxeValkyrie);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.ShovelValkyrie);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.PickValkyrie);
			}
			break;
		case 3:
			return new ItemStack(AetherItems.SwordHoly);
		case 4:
			return new ItemStack(AetherItems.GoldenFeather);
		case 5:
			return new ItemStack(AetherItems.RegenerationStone);
		case 6:
			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.NeptuneHelmet);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.NeptuneLeggings);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.NeptuneChestplate);
			}
			break;
		case 7:
			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.NeptuneBoots);
			}

			return new ItemStack(AetherItems.NeptuneGlove);
		case 8:
			return new ItemStack(AetherItems.InvisibilityCloak);
		}

		return new ItemStack(AetherItems.ZanitePendant);
	}
}

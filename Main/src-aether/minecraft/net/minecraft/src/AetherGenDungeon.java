package net.minecraft.src;

import java.util.Random;

public class AetherGenDungeon extends WorldGenerator {
	public int xoff;
	public int yoff;
	public int zoff;
	public int rad;
	public int truey;

	private int floors() {
		return Block.stairDouble.blockID;
	}

	private int walls() {
		return AetherBlocks.LockedDungeonStone.blockID;
	}

	private int ceilings() {
		return AetherBlocks.LockedLightDungeonStone.blockID;
	}

	private int torches() {
		return 0;
	}

	private int doors() {
		return 0;
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		return this.generate(world, random, x, y, z, 24);
	}

	public boolean generate(World world, Random random, int x, int y, int z, int r) {
		r = (int)Math.floor((double)r * 0.8D);
		int wid = (int)Math.sqrt((double)(r * r / 2));

		int direction;
		int boss;
		int j;
		for(direction = 4; direction > -5; --direction) {
			boss = wid;
			if(direction >= 3 || direction <= -3) {
				boss = wid - 1;
			}

			if(direction == 4 || direction == -4) {
				--boss;
			}

			for(int flag = -boss; flag <= boss; ++flag) {
				for(j = -boss; j <= boss; ++j) {
					if(direction == 4) {
						this.setBlock(world, random, x + flag, y + direction, z + j, this.walls(), 2, this.ceilings(), 2, 10);
					} else if(direction > -4) {
						if(flag != boss && -flag != boss && j != boss && -j != boss) {
							world.setBlock(x + flag, y + direction, z + j, 0);
							if(direction == -2 && (flag == boss - 1 || -flag == boss - 1 || j == boss - 1 || -j == boss - 1) && (flag % 3 == 0 || j % 3 == 0)) {
								world.setBlock(x + flag, y + direction + 2, z + j, this.torches());
							}
						} else {
							this.setBlock(world, random, x + flag, y + direction, z + j, this.walls(), 2, this.ceilings(), 2, 10);
						}
					} else {
						this.setBlock(world, random, x + flag, y + direction, z + j, this.walls(), 2, this.ceilings(), 2, 10);
						if((flag == boss - 2 || -flag == boss - 2) && (j == boss - 2 || -j == boss - 2)) {
							world.setBlock(x + flag, y + direction + 1, z + j, Block.netherrack.blockID);
							world.setBlock(x + flag, y + direction + 2, z + j, Block.fire.blockID);
						}
					}
				}
			}
		}

		direction = random.nextInt(4);

		for(boss = wid; boss < wid + 32; ++boss) {
			boolean z18 = false;

			for(j = -3; j < 2; ++j) {
				for(int k = -3; k < 4; ++k) {
					int a;
					int b;
					if(direction / 2 == 0) {
						a = boss;
						b = k;
					} else {
						a = k;
						b = boss;
					}

					if(direction % 2 == 0) {
						a = -a;
						b = -b;
					}

					if(!AetherBlocks.isGood(world.getBlockId(x + a, y + j, z + b), world.getBlockMetadata(x + a, y + j, z + b))) {
						z18 = true;
						if(j == -3) {
							this.setBlock(world, random, x + a, y + j, z + b, AetherBlocks.Holystone.blockID, 0, AetherBlocks.Holystone.blockID, 2, 5);
						} else if(j < 1) {
							if(boss == wid) {
								if(k < 2 && k > -2 && j < 0) {
									world.setBlock(x + a, y + j, z + b, this.doors());
								} else {
									this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 10);
								}
							} else if(k != 3 && k != -3) {
								world.setBlock(x + a, y + j, z + b, 0);
								if(j == -1 && (k == 2 || k == -2) && (boss - wid - 2) % 3 == 0) {
									world.setBlock(x + a, y + j, z + b, this.torches());
								}
							} else {
								this.setBlock(world, random, x + a, y + j, z + b, AetherBlocks.Holystone.blockID, 0, AetherBlocks.Holystone.blockID, 2, 5);
							}
						} else if(boss == wid) {
							this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 5);
						} else {
							this.setBlock(world, random, x + a, y + j, z + b, AetherBlocks.Holystone.blockID, 0, AetherBlocks.Holystone.blockID, 2, 5);
						}
					}

					a = -a;
					b = -b;
					if(boss < wid + 6) {
						if(j == -3) {
							this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 10);
						} else if(j < 1) {
							if(boss == wid) {
								if(k < 2 && k > -2 && j < 0) {
									this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 10);
								} else {
									this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 10);
								}
							} else if(boss == wid + 5) {
								this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 10);
							} else if(boss == wid + 4 && k == 0 && j == -2) {
								world.setBlockAndMetadata(x + a, y + j, z + b, AetherBlocks.TreasureChest.blockID, 4);
								TileEntityChest chest = (TileEntityChest)((TileEntityChest)world.getBlockTileEntity(x + a, y + j, z + b));

								for(int p = 0; p < 3 + random.nextInt(3); ++p) {
									ItemStack item = this.getGoldLoot(random);
									chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), item);
								}
							} else if(k != 3 && k != -3) {
								world.setBlock(x + a, y + j, z + b, 0);
								if(j == -1 && (k == 2 || k == -2) && (boss - wid - 2) % 3 == 0) {
									world.setBlock(x + a, y + j, z + b, this.torches());
								}
							} else {
								this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 10);
							}
						} else {
							this.setBlock(world, random, x + a, y + j, z + b, this.walls(), 2, this.ceilings(), 2, 10);
						}
					}
				}
			}

			if(!z18) {
				break;
			}
		}

		EntityFireMonster entityFireMonster19 = new EntityFireMonster(world, x, y - 1, z, wid, direction);
		world.entityJoinedWorld(entityFireMonster19);
		return true;
	}

	private void setBlock(World world, Random random, int i, int j, int k, int block1, int meta1, int block2, int meta2, int chance) {
		if(random.nextInt(chance) == 0) {
			world.setBlockAndMetadata(i, j, k, block2, meta2);
		} else {
			world.setBlockAndMetadata(i, j, k, block1, meta1);
		}

	}

	private ItemStack getGoldLoot(Random random) {
		int item = random.nextInt(8);
		switch(item) {
		case 0:
			return new ItemStack(AetherItems.IronBubble);
		case 1:
			return new ItemStack(AetherItems.VampireBlade);
		case 2:
			return new ItemStack(AetherItems.PigSlayer);
		case 3:
			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.PhoenixHelm);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.PhoenixLegs);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.PhoenixBody);
			}
			break;
		case 4:
			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.PhoenixBoots);
			}

			return new ItemStack(AetherItems.PhoenixGlove);
		case 5:
			return new ItemStack(AetherItems.LifeShard);
		case 6:
			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.GravititeHelmet);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.GravititePlatelegs);
			}

			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.GravititeBodyplate);
			}
			break;
		case 7:
			if(random.nextBoolean()) {
				return new ItemStack(AetherItems.GravititeBoots);
			}

			return new ItemStack(AetherItems.GravititeGlove);
		}

		return new ItemStack(AetherItems.ObsidianBody);
	}
}

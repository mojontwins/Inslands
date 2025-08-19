package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public final class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int i1, int i2) {
		super(51, 31, Material.fire);
		this.setBurnRate(Block.planks.blockID, 5, 20);
		this.setBurnRate(Block.wood.blockID, 5, 5);
		this.setBurnRate(Block.leaves.blockID, 30, 60);
		this.setBurnRate(Block.bookshelf.blockID, 30, 20);
		this.setBurnRate(Block.tnt.blockID, 15, 100);

		for(i1 = 0; i1 < 16; ++i1) {
			this.setBurnRate(Block.clothRed.blockID + i1, 30, 60);
		}

		this.setTickOnLoad(true);
	}

	private void setBurnRate(int i1, int i2, int i3) {
		this.chanceToEncourageFire[i1] = i2;
		this.abilityToCatchFire[i1] = i3;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 3;
	}

	public final int quantityDropped(Random random1) {
		return 0;
	}

	public final int tickRate() {
		return 20;
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		int i6;
		if((i6 = world1.getBlockMetadata(i2, i3, i4)) < 15) {
			world1.setBlockMetadataWithNotify(i2, i3, i4, i6 + 1);
			world1.scheduleBlockUpdate(i2, i3, i4, this.blockID);
		}

		if(!this.canNeighborCatchFire(world1, i2, i3, i4)) {
			if(!world1.isSolid(i2, i3 - 1, i4) || i6 > 3) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}

		} else if(!this.canBlockCatchFire(world1, i2, i3 - 1, i4) && i6 == 15 && random5.nextInt(4) == 0) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		} else {
			if(i6 % 5 == 0 && i6 > 5) {
				this.tryToCatchBlockOnFire(world1, i2 + 1, i3, i4, 300, random5);
				this.tryToCatchBlockOnFire(world1, i2 - 1, i3, i4, 300, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3 - 1, i4, 100, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3 + 1, i4, 200, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3, i4 - 1, 300, random5);
				this.tryToCatchBlockOnFire(world1, i2, i3, i4 + 1, 300, random5);

				for(i6 = i2 - 1; i6 <= i2 + 1; ++i6) {
					for(int i7 = i4 - 1; i7 <= i4 + 1; ++i7) {
						for(int i8 = i3 - 1; i8 <= i3 + 4; ++i8) {
							if(i6 != i2 || i8 != i3 || i7 != i4) {
								int i9 = 100;
								if(i8 > i3 + 1) {
									i9 = 100 + (i8 - (i3 + 1)) * 100;
								}

								int i10000;
								if(world1.getBlockId(i6, i8, i7) != 0) {
									i10000 = 0;
								} else {
									int i15 = this.getChanceToEncourageFire(world1, i6 + 1, i8, i7, 0);
									i15 = this.getChanceToEncourageFire(world1, i6 - 1, i8, i7, i15);
									i15 = this.getChanceToEncourageFire(world1, i6, i8 - 1, i7, i15);
									i15 = this.getChanceToEncourageFire(world1, i6, i8 + 1, i7, i15);
									i15 = this.getChanceToEncourageFire(world1, i6, i8, i7 - 1, i15);
									i10000 = this.getChanceToEncourageFire(world1, i6, i8, i7 + 1, i15);
								}

								int i10 = i10000;
								if(i10000 > 0 && random5.nextInt(i9) <= i10) {
									world1.setBlockWithNotify(i6, i8, i7, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World world1, int i2, int i3, int i4, int i5, Random random6) {
		int i7 = this.abilityToCatchFire[world1.getBlockId(i2, i3, i4)];
		if(random6.nextInt(i5) < i7) {
			boolean z8 = world1.getBlockId(i2, i3, i4) == Block.tnt.blockID;
			if(random6.nextInt(2) == 0) {
				world1.setBlockWithNotify(i2, i3, i4, this.blockID);
			} else {
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}

			if(z8) {
				Block.tnt.onBlockDestroyedByPlayer(world1, i2, i3, i4, 0);
			}
		}

	}

	private boolean canNeighborCatchFire(World world1, int i2, int i3, int i4) {
		return this.canBlockCatchFire(world1, i2 + 1, i3, i4) ? true : (this.canBlockCatchFire(world1, i2 - 1, i3, i4) ? true : (this.canBlockCatchFire(world1, i2, i3 - 1, i4) ? true : (this.canBlockCatchFire(world1, i2, i3 + 1, i4) ? true : (this.canBlockCatchFire(world1, i2, i3, i4 - 1) ? true : this.canBlockCatchFire(world1, i2, i3, i4 + 1)))));
	}

	public final boolean isCollidable() {
		return false;
	}

	public final boolean canBlockCatchFire(World world1, int i2, int i3, int i4) {
		return this.chanceToEncourageFire[world1.getBlockId(i2, i3, i4)] > 0;
	}

	private int getChanceToEncourageFire(World world1, int i2, int i3, int i4, int i5) {
		int i6;
		return (i6 = this.chanceToEncourageFire[world1.getBlockId(i2, i3, i4)]) > i5 ? i6 : i5;
	}

	public final boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return world1.isSolid(i2, i3 - 1, i4) || this.canNeighborCatchFire(world1, i2, i3, i4);
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(!world1.isSolid(i2, i3 - 1, i4) && !this.canNeighborCatchFire(world1, i2, i3, i4)) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		if(!world1.isSolid(i2, i3 - 1, i4) && !this.canNeighborCatchFire(world1, i2, i3, i4)) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		} else {
			world1.scheduleBlockUpdate(i2, i3, i4, this.blockID);
		}
	}

	public final boolean getChanceOfNeighborsEncouragingFire(int i1) {
		return this.chanceToEncourageFire[i1] > 0;
	}

	public final void fireSpread(World world1, int i2, int i3, int i4) {
		boolean z5 = false;
		if(!(z5 = fireCheck(world1, i2, i3 + 1, i4))) {
			z5 = fireCheck(world1, i2 - 1, i3, i4);
		}

		if(!z5) {
			z5 = fireCheck(world1, i2 + 1, i3, i4);
		}

		if(!z5) {
			z5 = fireCheck(world1, i2, i3, i4 - 1);
		}

		if(!z5) {
			z5 = fireCheck(world1, i2, i3, i4 + 1);
		}

		if(!z5) {
			z5 = fireCheck(world1, i2, i3 - 1, i4);
		}

		if(!z5) {
			world1.setBlockWithNotify(i2, i3, i4, Block.fire.blockID);
		}

	}

	public final void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		if(random5.nextInt(24) == 0) {
			world1.playSoundEffect((double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), "fire.fire", 1.0F + random5.nextFloat(), random5.nextFloat() * 0.7F + 0.3F);
		}

		int i6;
		float f7;
		float f8;
		float f9;
		if(!world1.isSolid(i2, i3 - 1, i4) && !Block.fire.canBlockCatchFire(world1, i2, i3 - 1, i4)) {
			if(Block.fire.canBlockCatchFire(world1, i2 - 1, i3, i4)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)i2 + random5.nextFloat() * 0.1F;
					f8 = (float)i3 + random5.nextFloat();
					f9 = (float)i4 + random5.nextFloat();
					world1.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world1, i2 + 1, i3, i4)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)(i2 + 1) - random5.nextFloat() * 0.1F;
					f8 = (float)i3 + random5.nextFloat();
					f9 = (float)i4 + random5.nextFloat();
					world1.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world1, i2, i3, i4 - 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)i2 + random5.nextFloat();
					f8 = (float)i3 + random5.nextFloat();
					f9 = (float)i4 + random5.nextFloat() * 0.1F;
					world1.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world1, i2, i3, i4 + 1)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)i2 + random5.nextFloat();
					f8 = (float)i3 + random5.nextFloat();
					f9 = (float)(i4 + 1) - random5.nextFloat() * 0.1F;
					world1.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(world1, i2, i3 + 1, i4)) {
				for(i6 = 0; i6 < 2; ++i6) {
					f7 = (float)i2 + random5.nextFloat();
					f8 = (float)(i3 + 1) - random5.nextFloat() * 0.1F;
					f9 = (float)i4 + random5.nextFloat();
					world1.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
				}
			}

		} else {
			for(i6 = 0; i6 < 3; ++i6) {
				f7 = (float)i2 + random5.nextFloat();
				f8 = (float)i3 + random5.nextFloat() * 0.5F + 0.5F;
				f9 = (float)i4 + random5.nextFloat();
				world1.spawnParticle("largesmoke", (double)f7, (double)f8, (double)f9, 0.0D, 0.0D, 0.0D);
			}

		}
	}

	private static boolean fireCheck(World world0, int i1, int i2, int i3) {
		int i4;
		if((i4 = world0.getBlockId(i1, i2, i3)) == Block.fire.blockID) {
			return true;
		} else if(i4 == 0) {
			world0.setBlockWithNotify(i1, i2, i3, Block.fire.blockID);
			return true;
		} else {
			return false;
		}
	}
}
package net.minecraft.src;

import java.util.Random;

public class TeleporterAether extends Teleporter {
	private Random field_4232_a = new Random();

	public void func_4107_a(World world, Entity entity) {
		if(!this.func_4106_b(world, entity)) {
			this.func_4108_c(world, entity);
			this.func_4106_b(world, entity);
		}
	}

	public boolean func_4106_b(World world, Entity entity) {
		short c = 128;
		double d = -1.0D;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = MathHelper.floor_double(entity.posX);
		int i1 = MathHelper.floor_double(entity.posZ);

		double d6;
		for(int k1 = l - c; k1 <= l + c; ++k1) {
			double l1 = (double)k1 + 0.5D - entity.posX;

			for(int d2 = i1 - c; d2 <= i1 + c; ++d2) {
				double d3 = (double)d2 + 0.5D - entity.posZ;

				for(int k2 = 127; k2 >= 0; --k2) {
					if(world.getBlockId(k1, k2, d2) == AetherBlocks.Portal.blockID) {
						while(world.getBlockId(k1, k2 - 1, d2) == AetherBlocks.Portal.blockID) {
							--k2;
						}

						d6 = (double)k2 + 0.5D - entity.posY;
						double d7 = l1 * l1 + d6 * d6 + d3 * d3;
						if(d < 0.0D || d7 < d) {
							d = d7;
							i = k1;
							j = k2;
							k = d2;
						}
					}
				}
			}
		}

		if(d >= 0.0D) {
			double d22 = (double)i + 0.5D;
			double d4 = (double)j + 0.5D;
			d6 = (double)k + 0.5D;
			if(world.getBlockId(i - 1, j, k) == AetherBlocks.Portal.blockID) {
				d22 -= 0.5D;
			}

			if(world.getBlockId(i + 1, j, k) == AetherBlocks.Portal.blockID) {
				d22 += 0.5D;
			}

			if(world.getBlockId(i, j, k - 1) == AetherBlocks.Portal.blockID) {
				d6 -= 0.5D;
			}

			if(world.getBlockId(i, j, k + 1) == AetherBlocks.Portal.blockID) {
				d6 += 0.5D;
			}

			entity.setLocationAndAngles(d22, d4, d6, entity.rotationYaw, 0.0F);
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;
			return true;
		} else {
			return false;
		}
	}

	public boolean func_4108_c(World world, Entity entity) {
		byte byte0 = 16;
		double d = -1.0D;
		int i = MathHelper.floor_double(entity.posX);
		int j = MathHelper.floor_double(entity.posY);
		int k = MathHelper.floor_double(entity.posZ);
		int l = i;
		int i1 = j;
		int j1 = k;
		int k1 = 0;
		int l1 = this.field_4232_a.nextInt(4);

		int k2;
		double l2;
		int l3;
		double i4;
		int j5;
		int k6;
		int l7;
		int i9;
		int j10;
		int k11;
		int flag1;
		int k12;
		int i13;
		double d32;
		double d33;
		for(k2 = i - byte0; k2 <= i + byte0; ++k2) {
			l2 = (double)k2 + 0.5D - entity.posX;

			for(l3 = k - byte0; l3 <= k + byte0; ++l3) {
				i4 = (double)l3 + 0.5D - entity.posZ;

				label296:
				for(j5 = 127; j5 >= 0; --j5) {
					if(world.isAirBlock(k2, j5, l3)) {
						while(j5 > 0 && world.isAirBlock(k2, j5 - 1, l3)) {
							--j5;
						}

						for(k6 = l1; k6 < l1 + 4; ++k6) {
							l7 = k6 % 2;
							i9 = 1 - l7;
							if(k6 % 4 >= 2) {
								l7 = -l7;
								i9 = -i9;
							}

							for(j10 = 0; j10 < 3; ++j10) {
								for(k11 = 0; k11 < 4; ++k11) {
									for(flag1 = -1; flag1 < 4; ++flag1) {
										k12 = k2 + (k11 - 1) * l7 + j10 * i9;
										i13 = j5 + flag1;
										int j13 = l3 + (k11 - 1) * i9 - j10 * l7;
										if(flag1 < 0 && !this.blockIsGood(world.getBlockId(k12, i13, j13), world.getBlockMetadata(k12, i13, j13)) || flag1 >= 0 && !world.isAirBlock(k12, i13, j13)) {
											continue label296;
										}
									}
								}
							}

							d32 = (double)j5 + 0.5D - entity.posY;
							d33 = l2 * l2 + d32 * d32 + i4 * i4;
							if(d < 0.0D || d33 < d) {
								d = d33;
								l = k2;
								i1 = j5;
								j1 = l3;
								k1 = k6 % 4;
							}
						}
					}
				}
			}
		}

		if(d < 0.0D) {
			for(k2 = i - byte0; k2 <= i + byte0; ++k2) {
				l2 = (double)k2 + 0.5D - entity.posX;

				for(l3 = k - byte0; l3 <= k + byte0; ++l3) {
					i4 = (double)l3 + 0.5D - entity.posZ;

					label234:
					for(j5 = 127; j5 >= 0; --j5) {
						if(world.isAirBlock(k2, j5, l3)) {
							while(world.isAirBlock(k2, j5 - 1, l3) && j5 > 0) {
								--j5;
							}

							for(k6 = l1; k6 < l1 + 2; ++k6) {
								l7 = k6 % 2;
								i9 = 1 - l7;

								for(j10 = 0; j10 < 4; ++j10) {
									for(k11 = -1; k11 < 4; ++k11) {
										flag1 = k2 + (j10 - 1) * l7;
										k12 = j5 + k11;
										i13 = l3 + (j10 - 1) * i9;
										if(k11 < 0 && !this.blockIsGood(world.getBlockId(flag1, k12, i13), world.getBlockMetadata(flag1, k12, i13)) || k11 >= 0 && !world.isAirBlock(flag1, k12, i13)) {
											continue label234;
										}
									}
								}

								d32 = (double)j5 + 0.5D - entity.posY;
								d33 = l2 * l2 + d32 * d32 + i4 * i4;
								if(d < 0.0D || d33 < d) {
									d = d33;
									l = k2;
									i1 = j5;
									j1 = l3;
									k1 = k6 % 2;
								}
							}
						}
					}
				}
			}
		}

		int i30 = l;
		int i3 = i1;
		l3 = j1;
		int i31 = k1 % 2;
		int j4 = 1 - i31;
		if(k1 % 4 >= 2) {
			i31 = -i31;
			j4 = -j4;
		}

		boolean z34;
		if(d < 0.0D) {
			if(i1 < 70) {
				i1 = 70;
			}

			if(i1 > 118) {
				i1 = 118;
			}

			i3 = i1;

			for(j5 = -1; j5 <= 1; ++j5) {
				for(k6 = 1; k6 < 3; ++k6) {
					for(l7 = -1; l7 < 3; ++l7) {
						i9 = i30 + (k6 - 1) * i31 + j5 * j4;
						j10 = i3 + l7;
						k11 = l3 + (k6 - 1) * j4 - j5 * i31;
						z34 = l7 < 0;
						world.setBlockWithNotify(i9, j10, k11, z34 ? Block.glowStone.blockID : 0);
					}
				}
			}
		}

		for(j5 = 0; j5 < 4; ++j5) {
			world.editingBlocks = true;

			for(k6 = 0; k6 < 4; ++k6) {
				for(l7 = -1; l7 < 4; ++l7) {
					i9 = i30 + (k6 - 1) * i31;
					j10 = i3 + l7;
					k11 = l3 + (k6 - 1) * j4;
					z34 = k6 == 0 || k6 == 3 || l7 == -1 || l7 == 3;
					world.setBlockWithNotify(i9, j10, k11, z34 ? Block.glowStone.blockID : AetherBlocks.Portal.blockID);
				}
			}

			world.editingBlocks = false;

			for(k6 = 0; k6 < 4; ++k6) {
				for(l7 = -1; l7 < 4; ++l7) {
					i9 = i30 + (k6 - 1) * i31;
					j10 = i3 + l7;
					k11 = l3 + (k6 - 1) * j4;
					world.notifyBlocksOfNeighborChange(i9, j10, k11, world.getBlockId(i9, j10, k11));
				}
			}
		}

		return true;
	}

	public boolean blockIsGood(int block, int meta) {
		return block == 0 ? false : (!Block.blocksList[block].blockMaterial.isSolid() ? false : block != AetherBlocks.Aercloud.blockID || meta != 0);
	}
}

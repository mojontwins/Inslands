package net.minecraft.game.world.terrain.generate;

import java.util.Random;

import net.minecraft.game.world.World;

import util.MathHelper;

public final class WorldGenBigTree extends WorldGenerator {
	private static byte[] otherCoordPairs = new byte[]{(byte)2, (byte)0, (byte)0, (byte)1, (byte)2, (byte)1};
	private Random rand = new Random();
	private World worldObj;
	private int[] basePos = new int[]{0, 0, 0};
	private int heightLimit = 0;
	private int height;
	private double heightAttenuation = 0.618D;
	private double branchSlope = 0.381D;
	private double scaleWidth = 1.0D;
	private double leafDensity = 1.0D;
	private int trunkSize = 1;
	private int heightLimitLimit = 12;
	private int leafDistanceLimit = 4;
	private int[][] leafNodes;

	private void placeBlockLine(int[] i1, int[] i2, int i3) {
		int[] i15 = new int[]{0, 0, 0};
		byte b4 = 0;

		byte b5;
		for(b5 = 0; b4 < 3; ++b4) {
			i15[b4] = i2[b4] - i1[b4];
			if(Math.abs(i15[b4]) > Math.abs(i15[b5])) {
				b5 = b4;
			}
		}

		if(i15[b5] != 0) {
			byte b14 = otherCoordPairs[b5];
			b4 = otherCoordPairs[b5 + 3];
			byte b6;
			if(i15[b5] > 0) {
				b6 = 1;
			} else {
				b6 = -1;
			}

			double d10 = (double)i15[b14] / (double)i15[b5];
			double d12 = (double)i15[b4] / (double)i15[b5];
			int[] i7 = new int[]{0, 0, 0};
			int i8 = 0;

			for(i3 = i15[b5] + b6; i8 != i3; i8 += b6) {
				i7[b5] = MathHelper.floor_double((double)(i1[b5] + i8) + 0.5D);
				i7[b14] = MathHelper.floor_double((double)i1[b14] + (double)i8 * d10 + 0.5D);
				i7[b4] = MathHelper.floor_double((double)i1[b4] + (double)i8 * d12 + 0.5D);
				this.worldObj.setTileNoUpdate(i7[0], i7[1], i7[2], 17);
			}

		}
	}

	private int checkBlockLine(int[] i1, int[] i2) {
		int[] i3 = new int[]{0, 0, 0};
		byte b4 = 0;

		byte b5;
		for(b5 = 0; b4 < 3; ++b4) {
			i3[b4] = i2[b4] - i1[b4];
			if(Math.abs(i3[b4]) > Math.abs(i3[b5])) {
				b5 = b4;
			}
		}

		if(i3[b5] == 0) {
			return -1;
		} else {
			byte b14 = otherCoordPairs[b5];
			b4 = otherCoordPairs[b5 + 3];
			byte b6;
			if(i3[b5] > 0) {
				b6 = 1;
			} else {
				b6 = -1;
			}

			double d9 = (double)i3[b14] / (double)i3[b5];
			double d11 = (double)i3[b4] / (double)i3[b5];
			int[] i7 = new int[]{0, 0, 0};
			int i8 = 0;

			int i15;
			for(i15 = i3[b5] + b6; i8 != i15; i8 += b6) {
				i7[b5] = i1[b5] + i8;
				i7[b14] = (int)((double)i1[b14] + (double)i8 * d9);
				i7[b4] = (int)((double)i1[b4] + (double)i8 * d11);
				int i13;
				if((i13 = this.worldObj.getBlockId(i7[0], i7[1], i7[2])) != 0 && i13 != 18) {
					break;
				}
			}

			return i8 == i15 ? -1 : Math.abs(i8);
		}
	}

	public final void setScale(double d1, double d3, double d5) {
		this.heightLimitLimit = 12;
		this.leafDistanceLimit = 5;
		this.scaleWidth = 1.0D;
		this.leafDensity = 1.0D;
	}

	public final boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		this.worldObj = world1;
		long j6 = random2.nextLong();
		this.rand.setSeed(j6);
		this.basePos[0] = i3;
		this.basePos[1] = i4;
		this.basePos[2] = i5;
		if(this.heightLimit == 0) {
			this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
		}

		int[] i39 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
		int[] i41 = new int[]{this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2]};
		boolean z10000;
		if((i4 = this.worldObj.getBlockId(this.basePos[0], this.basePos[1] - 1, this.basePos[2])) != 2 && i4 != 3) {
			z10000 = false;
		} else if((i5 = this.checkBlockLine(i39, i41)) == -1) {
			z10000 = true;
		} else if(i5 < 6) {
			z10000 = false;
		} else {
			this.heightLimit = i5;
			z10000 = true;
		}

		if(!z10000) {
			return false;
		} else {
			WorldGenBigTree worldGenBigTree38 = this;
			this.height = (int)((double)this.heightLimit * this.heightAttenuation);
			if(this.height >= this.heightLimit) {
				this.height = this.heightLimit - 1;
			}

			int i40;
			if((i40 = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D))) <= 0) {
				i40 = 1;
			}

			int[][] i42 = new int[i40 * this.heightLimit][4];
			i4 = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
			i5 = 1;
			int i43 = this.basePos[1] + this.height;
			int i7 = i4 - this.basePos[1];
			i42[0][0] = this.basePos[0];
			i42[0][1] = i4;
			i42[0][2] = this.basePos[2];
			i42[0][3] = i43;
			--i4;

			int i8;
			int i11;
			while(i7 >= 0) {
				i8 = 0;
				float f59;
				if((double)i7 < (double)((float)worldGenBigTree38.heightLimit) * 0.3D) {
					f59 = -1.618F;
				} else {
					float f13 = (float)worldGenBigTree38.heightLimit / 2.0F;
					float f14;
					float f36;
					if((f14 = (float)worldGenBigTree38.heightLimit / 2.0F - (float)i7) == 0.0F) {
						f36 = f13;
					} else if(Math.abs(f14) >= f13) {
						f36 = 0.0F;
					} else {
						f36 = (float)Math.sqrt(Math.pow((double)Math.abs(f13), 2.0D) - Math.pow((double)Math.abs(f14), 2.0D));
					}

					f59 = f36 *= 0.5F;
				}

				float f9 = f59;
				if(f59 < 0.0F) {
					--i4;
					--i7;
				} else {
					for(; i8 < i40; ++i8) {
						double d19 = worldGenBigTree38.scaleWidth * (double)f9 * ((double)worldGenBigTree38.rand.nextFloat() + 0.328D);
						double d21 = (double)worldGenBigTree38.rand.nextFloat() * 2.0D * 3.14159D;
						int i10 = (int)(d19 * Math.sin(d21) + (double)worldGenBigTree38.basePos[0] + 0.5D);
						i11 = (int)(d19 * Math.cos(d21) + (double)worldGenBigTree38.basePos[2] + 0.5D);
						int[] i12 = new int[]{i10, i4, i11};
						int[] i52 = new int[]{i10, i4 + worldGenBigTree38.leafDistanceLimit, i11};
						if(worldGenBigTree38.checkBlockLine(i12, i52) == -1) {
							i52 = new int[]{worldGenBigTree38.basePos[0], worldGenBigTree38.basePos[1], worldGenBigTree38.basePos[2]};
							double d30 = Math.sqrt(Math.pow((double)Math.abs(worldGenBigTree38.basePos[0] - i12[0]), 2.0D) + Math.pow((double)Math.abs(worldGenBigTree38.basePos[2] - i12[2]), 2.0D)) * worldGenBigTree38.branchSlope;
							if((double)i12[1] - d30 > (double)i43) {
								i52[1] = i43;
							} else {
								i52[1] = (int)((double)i12[1] - d30);
							}

							if(worldGenBigTree38.checkBlockLine(i52, i12) == -1) {
								i42[i5][0] = i10;
								i42[i5][1] = i4;
								i42[i5][2] = i11;
								i42[i5][3] = i52[1];
								++i5;
							}
						}
					}

					--i4;
					--i7;
				}
			}

			worldGenBigTree38.leafNodes = new int[i5][4];
			System.arraycopy(i42, 0, worldGenBigTree38.leafNodes, 0, i5);
			worldGenBigTree38 = this;
			i40 = 0;

			for(i3 = this.leafNodes.length; i40 < i3; ++i40) {
				i4 = worldGenBigTree38.leafNodes[i40][0];
				i5 = worldGenBigTree38.leafNodes[i40][1];
				i43 = worldGenBigTree38.leafNodes[i40][2];
				int i10001 = i4;
				i4 = i43;
				int i49 = i5;
				i8 = i10001;
				WorldGenBigTree worldGenBigTree47 = worldGenBigTree38;
				i5 = i5;

				for(int i56 = i49 + worldGenBigTree38.leafDistanceLimit; i5 < i56; ++i5) {
					int i22 = i5 - i49;
					float f20 = i22 >= 0 && i22 < worldGenBigTree47.leafDistanceLimit ? (i22 != 0 && i22 != worldGenBigTree47.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
					boolean z53 = true;
					z53 = true;
					float f51 = f20;
					WorldGenBigTree worldGenBigTree57 = worldGenBigTree47;
					int i28 = (int)((double)f20 + 0.618D);
					byte b29 = otherCoordPairs[1];
					byte b58 = otherCoordPairs[4];
					int[] i31 = new int[]{i8, i5, i4};
					int[] i50 = new int[]{0, 0, 0};
					i11 = -i28;

					label134:
					for(i50[1] = i31[1]; i11 <= i28; ++i11) {
						i50[b29] = i31[b29] + i11;
						int i55 = -i28;

						while(true) {
							while(true) {
								if(i55 > i28) {
									continue label134;
								}

								if(Math.sqrt(Math.pow((double)Math.abs(i11) + 0.5D, 2.0D) + Math.pow((double)Math.abs(i55) + 0.5D, 2.0D)) > (double)f51) {
									++i55;
								} else {
									i50[b58] = i31[b58] + i55;
									int i54;
									if((i54 = worldGenBigTree57.worldObj.getBlockId(i50[0], i50[1], i50[2])) != 0 && i54 != 18) {
										++i55;
									} else {
										worldGenBigTree57.worldObj.setTileNoUpdate(i50[0], i50[1], i50[2], 18);
										++i55;
									}
								}
							}
						}
					}
				}
			}

			i40 = this.basePos[0];
			i3 = this.basePos[1];
			i4 = this.basePos[1] + this.height;
			i5 = this.basePos[2];
			int[] i44 = new int[]{i40, i3, i5};
			int[] i48 = new int[]{i40, i4, i5};
			this.placeBlockLine(i44, i48, 17);
			if(this.trunkSize == 2) {
				++i44[0];
				++i48[0];
				this.placeBlockLine(i44, i48, 17);
				++i44[2];
				++i48[2];
				this.placeBlockLine(i44, i48, 17);
				i44[0] += -1;
				i48[0] += -1;
				this.placeBlockLine(i44, i48, 17);
			}

			worldGenBigTree38 = this;
			i40 = 0;
			i3 = this.leafNodes.length;

			for(int[] i45 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]}; i40 < i3; ++i40) {
				int[] i46 = worldGenBigTree38.leafNodes[i40];
				i44 = new int[]{i46[0], i46[1], i46[2]};
				i45[1] = i46[3];
				i7 = i45[1] - worldGenBigTree38.basePos[1];
				if((double)i7 >= (double)worldGenBigTree38.heightLimit * 0.2D) {
					worldGenBigTree38.placeBlockLine(i45, i44, 17);
				}
			}

			return true;
		}
	}
}
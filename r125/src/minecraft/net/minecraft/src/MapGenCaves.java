package net.minecraft.src;

import java.util.Random;

public class MapGenCaves extends MapGenBase {
	protected void generateLargeCaveNode(long j1, int i3, int i4, byte[] b5, double d6, double d8, double d10) {
		this.generateCaveNode(j1, i3, i4, b5, d6, d8, d10, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void generateCaveNode(long j1, int i3, int i4, byte[] b5, double d6, double d8, double d10, float f12, float f13, float f14, int i15, int i16, double d17) {
		double d19 = (double)(i3 * 16 + 8);
		double d21 = (double)(i4 * 16 + 8);
		float f23 = 0.0F;
		float f24 = 0.0F;
		Random random25 = new Random(j1);
		if(i16 <= 0) {
			int i26 = this.range * 16 - 16;
			i16 = i26 - random25.nextInt(i26 / 4);
		}

		boolean z54 = false;
		if(i15 == -1) {
			i15 = i16 / 2;
			z54 = true;
		}

		int i27 = random25.nextInt(i16 / 2) + i16 / 4;

		for(boolean z28 = random25.nextInt(6) == 0; i15 < i16; ++i15) {
			double d29 = 1.5D + (double)(MathHelper.sin((float)i15 * (float)Math.PI / (float)i16) * f12 * 1.0F);
			double d31 = d29 * d17;
			float f33 = MathHelper.cos(f14);
			float f34 = MathHelper.sin(f14);
			d6 += (double)(MathHelper.cos(f13) * f33);
			d8 += (double)f34;
			d10 += (double)(MathHelper.sin(f13) * f33);
			if(z28) {
				f14 *= 0.92F;
			} else {
				f14 *= 0.7F;
			}

			f14 += f24 * 0.1F;
			f13 += f23 * 0.1F;
			f24 *= 0.9F;
			f23 *= 0.75F;
			f24 += (random25.nextFloat() - random25.nextFloat()) * random25.nextFloat() * 2.0F;
			f23 += (random25.nextFloat() - random25.nextFloat()) * random25.nextFloat() * 4.0F;
			if(!z54 && i15 == i27 && f12 > 1.0F && i16 > 0) {
				this.generateCaveNode(random25.nextLong(), i3, i4, b5, d6, d8, d10, random25.nextFloat() * 0.5F + 0.5F, f13 - (float)Math.PI / 2F, f14 / 3.0F, i15, i16, 1.0D);
				this.generateCaveNode(random25.nextLong(), i3, i4, b5, d6, d8, d10, random25.nextFloat() * 0.5F + 0.5F, f13 + (float)Math.PI / 2F, f14 / 3.0F, i15, i16, 1.0D);
				return;
			}

			if(z54 || random25.nextInt(4) != 0) {
				double d35 = d6 - d19;
				double d37 = d10 - d21;
				double d39 = (double)(i16 - i15);
				double d41 = (double)(f12 + 2.0F + 16.0F);
				if(d35 * d35 + d37 * d37 - d39 * d39 > d41 * d41) {
					return;
				}

				if(d6 >= d19 - 16.0D - d29 * 2.0D && d10 >= d21 - 16.0D - d29 * 2.0D && d6 <= d19 + 16.0D + d29 * 2.0D && d10 <= d21 + 16.0D + d29 * 2.0D) {
					int i55 = MathHelper.floor_double(d6 - d29) - i3 * 16 - 1;
					int i36 = MathHelper.floor_double(d6 + d29) - i3 * 16 + 1;
					int i56 = MathHelper.floor_double(d8 - d31) - 1;
					int i38 = MathHelper.floor_double(d8 + d31) + 1;
					int i57 = MathHelper.floor_double(d10 - d29) - i4 * 16 - 1;
					int i40 = MathHelper.floor_double(d10 + d29) - i4 * 16 + 1;
					if(i55 < 0) {
						i55 = 0;
					}

					if(i36 > 16) {
						i36 = 16;
					}

					if(i56 < 1) {
						i56 = 1;
					}

					if(i38 > 120) {
						i38 = 120;
					}

					if(i57 < 0) {
						i57 = 0;
					}

					if(i40 > 16) {
						i40 = 16;
					}

					boolean z58 = false;

					int i42;
					int i45;
					for(i42 = i55; !z58 && i42 < i36; ++i42) {
						for(int i43 = i57; !z58 && i43 < i40; ++i43) {
							for(int i44 = i38 + 1; !z58 && i44 >= i56 - 1; --i44) {
								i45 = (i42 * 16 + i43) * 128 + i44;
								if(i44 >= 0 && i44 < 128) {
									if(b5[i45] == Block.waterMoving.blockID || b5[i45] == Block.waterStill.blockID) {
										z58 = true;
									}

									if(i44 != i56 - 1 && i42 != i55 && i42 != i36 - 1 && i43 != i57 && i43 != i40 - 1) {
										i44 = i56;
									}
								}
							}
						}
					}

					if(!z58) {
						for(i42 = i55; i42 < i36; ++i42) {
							double d59 = ((double)(i42 + i3 * 16) + 0.5D - d6) / d29;

							for(i45 = i57; i45 < i40; ++i45) {
								double d46 = ((double)(i45 + i4 * 16) + 0.5D - d10) / d29;
								int i48 = (i42 * 16 + i45) * 128 + i38;
								boolean z49 = false;
								if(d59 * d59 + d46 * d46 < 1.0D) {
									for(int i50 = i38 - 1; i50 >= i56; --i50) {
										double d51 = ((double)i50 + 0.5D - d8) / d31;
										if(d51 > -0.7D && d59 * d59 + d51 * d51 + d46 * d46 < 1.0D) {
											byte b53 = b5[i48];
											if(b53 == Block.grass.blockID) {
												z49 = true;
											}

											if(b53 == Block.stone.blockID || b53 == Block.dirt.blockID || b53 == Block.grass.blockID) {
												if(i50 < 10) {
													b5[i48] = (byte)Block.lavaMoving.blockID;
												} else {
													b5[i48] = 0;
													if(z49 && b5[i48 - 1] == Block.dirt.blockID) {
														b5[i48 - 1] = this.worldObj.getBiomeGenForCoords(i42 + i3 * 16, i45 + i4 * 16).topBlock;
													}
												}
											}
										}

										--i48;
									}
								}
							}
						}

						if(z54) {
							break;
						}
					}
				}
			}
		}

	}

	protected void recursiveGenerate(World world1, int i2, int i3, int i4, int i5, byte[] b6) {
		int i7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
		if(this.rand.nextInt(15) != 0) {
			i7 = 0;
		}

		for(int i8 = 0; i8 < i7; ++i8) {
			double d9 = (double)(i2 * 16 + this.rand.nextInt(16));
			double d11 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
			double d13 = (double)(i3 * 16 + this.rand.nextInt(16));
			int i15 = 1;
			if(this.rand.nextInt(4) == 0) {
				this.generateLargeCaveNode(this.rand.nextLong(), i4, i5, b6, d9, d11, d13);
				i15 += this.rand.nextInt(4);
			}

			for(int i16 = 0; i16 < i15; ++i16) {
				float f17 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
				if(this.rand.nextInt(10) == 0) {
					f19 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
				}

				this.generateCaveNode(this.rand.nextLong(), i4, i5, b6, d9, d11, d13, f19, f17, f18, 0, 0, 1.0D);
			}
		}

	}
}

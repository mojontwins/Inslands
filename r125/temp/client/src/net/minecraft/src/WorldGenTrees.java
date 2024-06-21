package net.minecraft.src;

import java.util.Random;

public class WorldGenTrees extends WorldGenerator {
	private final int field_48202_a;
	private final boolean field_48200_b;
	private final int field_48201_c;
	private final int field_48199_d;

	public WorldGenTrees(boolean z1) {
		this(z1, 4, 0, 0, false);
	}

	public WorldGenTrees(boolean z1, int i2, int i3, int i4, boolean z5) {
		super(z1);
		this.field_48202_a = i2;
		this.field_48201_c = i3;
		this.field_48199_d = i4;
		this.field_48200_b = z5;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		int i6 = random2.nextInt(3) + this.field_48202_a;
		boolean z7 = true;
		if(i4 >= 1 && i4 + i6 + 1 <= 256) {
			int i8;
			byte b9;
			int i11;
			int i12;
			for(i8 = i4; i8 <= i4 + 1 + i6; ++i8) {
				b9 = 1;
				if(i8 == i4) {
					b9 = 0;
				}

				if(i8 >= i4 + 1 + i6 - 2) {
					b9 = 2;
				}

				for(int i10 = i3 - b9; i10 <= i3 + b9 && z7; ++i10) {
					for(i11 = i5 - b9; i11 <= i5 + b9 && z7; ++i11) {
						if(i8 >= 0 && i8 < 256) {
							i12 = world1.getBlockId(i10, i8, i11);
							if(i12 != 0 && i12 != Block.leaves.blockID && i12 != Block.grass.blockID && i12 != Block.dirt.blockID && i12 != Block.wood.blockID) {
								z7 = false;
							}
						} else {
							z7 = false;
						}
					}
				}
			}

			if(!z7) {
				return false;
			} else {
				i8 = world1.getBlockId(i3, i4 - 1, i5);
				if((i8 == Block.grass.blockID || i8 == Block.dirt.blockID) && i4 < 256 - i6 - 1) {
					this.func_50073_a(world1, i3, i4 - 1, i5, Block.dirt.blockID);
					b9 = 3;
					byte b18 = 0;

					int i13;
					int i14;
					int i15;
					for(i11 = i4 - b9 + i6; i11 <= i4 + i6; ++i11) {
						i12 = i11 - (i4 + i6);
						i13 = b18 + 1 - i12 / 2;

						for(i14 = i3 - i13; i14 <= i3 + i13; ++i14) {
							i15 = i14 - i3;

							for(int i16 = i5 - i13; i16 <= i5 + i13; ++i16) {
								int i17 = i16 - i5;
								if((Math.abs(i15) != i13 || Math.abs(i17) != i13 || random2.nextInt(2) != 0 && i12 != 0) && !Block.opaqueCubeLookup[world1.getBlockId(i14, i11, i16)]) {
									this.setBlockAndMetadata(world1, i14, i11, i16, Block.leaves.blockID, this.field_48199_d);
								}
							}
						}
					}

					for(i11 = 0; i11 < i6; ++i11) {
						i12 = world1.getBlockId(i3, i4 + i11, i5);
						if(i12 == 0 || i12 == Block.leaves.blockID) {
							this.setBlockAndMetadata(world1, i3, i4 + i11, i5, Block.wood.blockID, this.field_48201_c);
							if(this.field_48200_b && i11 > 0) {
								if(random2.nextInt(3) > 0 && world1.isAirBlock(i3 - 1, i4 + i11, i5)) {
									this.setBlockAndMetadata(world1, i3 - 1, i4 + i11, i5, Block.vine.blockID, 8);
								}

								if(random2.nextInt(3) > 0 && world1.isAirBlock(i3 + 1, i4 + i11, i5)) {
									this.setBlockAndMetadata(world1, i3 + 1, i4 + i11, i5, Block.vine.blockID, 2);
								}

								if(random2.nextInt(3) > 0 && world1.isAirBlock(i3, i4 + i11, i5 - 1)) {
									this.setBlockAndMetadata(world1, i3, i4 + i11, i5 - 1, Block.vine.blockID, 1);
								}

								if(random2.nextInt(3) > 0 && world1.isAirBlock(i3, i4 + i11, i5 + 1)) {
									this.setBlockAndMetadata(world1, i3, i4 + i11, i5 + 1, Block.vine.blockID, 4);
								}
							}
						}
					}

					if(this.field_48200_b) {
						for(i11 = i4 - 3 + i6; i11 <= i4 + i6; ++i11) {
							i12 = i11 - (i4 + i6);
							i13 = 2 - i12 / 2;

							for(i14 = i3 - i13; i14 <= i3 + i13; ++i14) {
								for(i15 = i5 - i13; i15 <= i5 + i13; ++i15) {
									if(world1.getBlockId(i14, i11, i15) == Block.leaves.blockID) {
										if(random2.nextInt(4) == 0 && world1.getBlockId(i14 - 1, i11, i15) == 0) {
											this.func_48198_a(world1, i14 - 1, i11, i15, 8);
										}

										if(random2.nextInt(4) == 0 && world1.getBlockId(i14 + 1, i11, i15) == 0) {
											this.func_48198_a(world1, i14 + 1, i11, i15, 2);
										}

										if(random2.nextInt(4) == 0 && world1.getBlockId(i14, i11, i15 - 1) == 0) {
											this.func_48198_a(world1, i14, i11, i15 - 1, 1);
										}

										if(random2.nextInt(4) == 0 && world1.getBlockId(i14, i11, i15 + 1) == 0) {
											this.func_48198_a(world1, i14, i11, i15 + 1, 4);
										}
									}
								}
							}
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	private void func_48198_a(World world1, int i2, int i3, int i4, int i5) {
		this.setBlockAndMetadata(world1, i2, i3, i4, Block.vine.blockID, i5);
		int i6 = 4;

		while(true) {
			--i3;
			if(world1.getBlockId(i2, i3, i4) != 0 || i6 <= 0) {
				return;
			}

			this.setBlockAndMetadata(world1, i2, i3, i4, Block.vine.blockID, i5);
			--i6;
		}
	}
}

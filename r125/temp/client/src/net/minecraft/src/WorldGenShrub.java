package net.minecraft.src;

import java.util.Random;

public class WorldGenShrub extends WorldGenerator {
	private int field_48197_a;
	private int field_48196_b;

	public WorldGenShrub(int i1, int i2) {
		this.field_48196_b = i1;
		this.field_48197_a = i2;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		int i15;
		for(boolean z6 = false; ((i15 = world1.getBlockId(i3, i4, i5)) == 0 || i15 == Block.leaves.blockID) && i4 > 0; --i4) {
		}

		int i7 = world1.getBlockId(i3, i4, i5);
		if(i7 == Block.dirt.blockID || i7 == Block.grass.blockID) {
			++i4;
			this.setBlockAndMetadata(world1, i3, i4, i5, Block.wood.blockID, this.field_48196_b);

			for(int i8 = i4; i8 <= i4 + 2; ++i8) {
				int i9 = i8 - i4;
				int i10 = 2 - i9;

				for(int i11 = i3 - i10; i11 <= i3 + i10; ++i11) {
					int i12 = i11 - i3;

					for(int i13 = i5 - i10; i13 <= i5 + i10; ++i13) {
						int i14 = i13 - i5;
						if((Math.abs(i12) != i10 || Math.abs(i14) != i10 || random2.nextInt(2) != 0) && !Block.opaqueCubeLookup[world1.getBlockId(i11, i8, i13)]) {
							this.setBlockAndMetadata(world1, i11, i8, i13, Block.leaves.blockID, this.field_48197_a);
						}
					}
				}
			}
		}

		return true;
	}
}

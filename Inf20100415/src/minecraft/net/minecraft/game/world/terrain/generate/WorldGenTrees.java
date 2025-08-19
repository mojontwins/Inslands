package net.minecraft.game.world.terrain.generate;

import java.util.Random;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class WorldGenTrees extends WorldGenerator {
	public final boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		int i6 = random2.nextInt(3) + 4;
		boolean z7 = true;
		if(i4 > 0 && i4 + i6 + 1 <= 128) {
			int i8;
			int i10;
			int i11;
			int i12;
			for(i8 = i4; i8 <= i4 + 1 + i6; ++i8) {
				byte b9 = 1;
				if(i8 == i4) {
					b9 = 0;
				}

				if(i8 >= i4 + 1 + i6 - 2) {
					b9 = 2;
				}

				for(i10 = i3 - b9; i10 <= i3 + b9 && z7; ++i10) {
					for(i11 = i5 - b9; i11 <= i5 + b9 && z7; ++i11) {
						if(i8 >= 0 && i8 < 128) {
							if((i12 = world1.getBlockId(i10, i8, i11)) != 0 && i12 != Block.leaves.blockID) {
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
			} else if(((i8 = world1.getBlockId(i3, i4 - 1, i5)) == Block.grass.blockID || i8 == Block.dirt.blockID) && i4 < 128 - i6 - 1) {
				world1.setTileNoUpdate(i3, i4 - 1, i5, Block.dirt.blockID);

				int i15;
				for(i15 = i4 - 3 + i6; i15 <= i4 + i6; ++i15) {
					i10 = i15 - (i4 + i6);
					i11 = 1 - i10 / 2;

					for(i12 = i3 - i11; i12 <= i3 + i11; ++i12) {
						int i14 = i12 - i3;

						for(i8 = i5 - i11; i8 <= i5 + i11; ++i8) {
							int i13 = i8 - i5;
							if((Math.abs(i14) != i11 || Math.abs(i13) != i11 || random2.nextInt(2) != 0 && i10 != 0) && !Block.opaqueCubeLookup[world1.getBlockId(i12, i15, i8)]) {
								world1.setTileNoUpdate(i12, i15, i8, Block.leaves.blockID);
							}
						}
					}
				}

				for(i15 = 0; i15 < i6; ++i15) {
					if(!Block.opaqueCubeLookup[world1.getBlockId(i3, i4 + i15, i5)]) {
						world1.setTileNoUpdate(i3, i4 + i15, i5, Block.wood.blockID);
					}
				}

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
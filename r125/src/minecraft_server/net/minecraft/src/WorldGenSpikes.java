package net.minecraft.src;

import java.util.Random;

public class WorldGenSpikes extends WorldGenerator {
	private int replaceID;

	public WorldGenSpikes(int i1) {
		this.replaceID = i1;
	}

	public boolean generate(World world1, Random random2, int i3, int i4, int i5) {
		if(world1.isAirBlock(i3, i4, i5) && world1.getBlockId(i3, i4 - 1, i5) == this.replaceID) {
			int i6 = random2.nextInt(32) + 6;
			int i7 = random2.nextInt(4) + 1;

			int i8;
			int i9;
			int i10;
			int i11;
			for(i8 = i3 - i7; i8 <= i3 + i7; ++i8) {
				for(i9 = i5 - i7; i9 <= i5 + i7; ++i9) {
					i10 = i8 - i3;
					i11 = i9 - i5;
					if(i10 * i10 + i11 * i11 <= i7 * i7 + 1 && world1.getBlockId(i8, i4 - 1, i9) != this.replaceID) {
						return false;
					}
				}
			}

			for(i8 = i4; i8 < i4 + i6 && i8 < 128; ++i8) {
				for(i9 = i3 - i7; i9 <= i3 + i7; ++i9) {
					for(i10 = i5 - i7; i10 <= i5 + i7; ++i10) {
						i11 = i9 - i3;
						int i12 = i10 - i5;
						if(i11 * i11 + i12 * i12 <= i7 * i7 + 1) {
							world1.setBlockWithNotify(i9, i8, i10, Block.obsidian.blockID);
						}
					}
				}
			}

			EntityEnderCrystal entityEnderCrystal13 = new EntityEnderCrystal(world1);
			entityEnderCrystal13.setLocationAndAngles((double)((float)i3 + 0.5F), (double)(i4 + i6), (double)((float)i5 + 0.5F), random2.nextFloat() * 360.0F, 0.0F);
			world1.spawnEntityInWorld(entityEnderCrystal13);
			world1.setBlockWithNotify(i3, i4 + i6, i5, Block.bedrock.blockID);
			return true;
		} else {
			return false;
		}
	}
}

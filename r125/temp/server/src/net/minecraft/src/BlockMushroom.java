package net.minecraft.src;

import java.util.Random;

public class BlockMushroom extends BlockFlower {
	protected BlockMushroom(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.2F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f3 * 2.0F, 0.5F + f3);
		this.setTickRandomly(true);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(random5.nextInt(25) == 0) {
			byte b6 = 4;
			int i7 = 5;

			int i8;
			int i9;
			int i10;
			for(i8 = i2 - b6; i8 <= i2 + b6; ++i8) {
				for(i9 = i4 - b6; i9 <= i4 + b6; ++i9) {
					for(i10 = i3 - 1; i10 <= i3 + 1; ++i10) {
						if(world1.getBlockId(i8, i10, i9) == this.blockID) {
							--i7;
							if(i7 <= 0) {
								return;
							}
						}
					}
				}
			}

			i8 = i2 + random5.nextInt(3) - 1;
			i9 = i3 + random5.nextInt(2) - random5.nextInt(2);
			i10 = i4 + random5.nextInt(3) - 1;

			for(int i11 = 0; i11 < 4; ++i11) {
				if(world1.isAirBlock(i8, i9, i10) && this.canBlockStay(world1, i8, i9, i10)) {
					i2 = i8;
					i3 = i9;
					i4 = i10;
				}

				i8 = i2 + random5.nextInt(3) - 1;
				i9 = i3 + random5.nextInt(2) - random5.nextInt(2);
				i10 = i4 + random5.nextInt(3) - 1;
			}

			if(world1.isAirBlock(i8, i9, i10) && this.canBlockStay(world1, i8, i9, i10)) {
				world1.setBlockWithNotify(i8, i9, i10, this.blockID);
			}
		}

	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return super.canPlaceBlockAt(world1, i2, i3, i4) && this.canBlockStay(world1, i2, i3, i4);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i1) {
		return Block.opaqueCubeLookup[i1];
	}

	public boolean canBlockStay(World world1, int i2, int i3, int i4) {
		if(i3 >= 0 && i3 < 256) {
			int i5 = world1.getBlockId(i2, i3 - 1, i4);
			return i5 == Block.mycelium.blockID || world1.getFullBlockLightValue(i2, i3, i4) < 13 && this.canThisPlantGrowOnThisBlockID(i5);
		} else {
			return false;
		}
	}

	public boolean fertilizeMushroom(World world1, int i2, int i3, int i4, Random random5) {
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		world1.setBlock(i2, i3, i4, 0);
		WorldGenBigMushroom worldGenBigMushroom7 = null;
		if(this.blockID == Block.mushroomBrown.blockID) {
			worldGenBigMushroom7 = new WorldGenBigMushroom(0);
		} else if(this.blockID == Block.mushroomRed.blockID) {
			worldGenBigMushroom7 = new WorldGenBigMushroom(1);
		}

		if(worldGenBigMushroom7 != null && worldGenBigMushroom7.generate(world1, random5, i2, i3, i4)) {
			return true;
		} else {
			world1.setBlockAndMetadata(i2, i3, i4, this.blockID, i6);
			return false;
		}
	}
}

package net.minecraft.src;

import java.util.Random;

public class BlockMushroomCap extends Block {
	private int mushroomType;

	public BlockMushroomCap(int i1, Material material2, int i3, int i4) {
		super(i1, i3, material2);
		this.mushroomType = i4;
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return i2 == 10 && i1 > 1 ? this.blockIndexInTexture - 1 : (i2 >= 1 && i2 <= 9 && i1 == 1 ? this.blockIndexInTexture - 16 - this.mushroomType : (i2 >= 1 && i2 <= 3 && i1 == 2 ? this.blockIndexInTexture - 16 - this.mushroomType : (i2 >= 7 && i2 <= 9 && i1 == 3 ? this.blockIndexInTexture - 16 - this.mushroomType : ((i2 == 1 || i2 == 4 || i2 == 7) && i1 == 4 ? this.blockIndexInTexture - 16 - this.mushroomType : ((i2 == 3 || i2 == 6 || i2 == 9) && i1 == 5 ? this.blockIndexInTexture - 16 - this.mushroomType : (i2 == 14 ? this.blockIndexInTexture - 16 - this.mushroomType : (i2 == 15 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture)))))));
	}

	public int quantityDropped(Random random1) {
		int i2 = random1.nextInt(10) - 7;
		if(i2 < 0) {
			i2 = 0;
		}

		return i2;
	}

	public int idDropped(int i1, Random random2, int i3) {
		return Block.mushroomBrown.blockID + this.mushroomType;
	}
}

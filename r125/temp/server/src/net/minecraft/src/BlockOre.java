package net.minecraft.src;

import java.util.Random;

public class BlockOre extends Block {
	public BlockOre(int i1, int i2) {
		super(i1, i2, Material.rock);
	}

	public int idDropped(int i1, Random random2, int i3) {
		return this.blockID == Block.oreCoal.blockID ? Item.coal.shiftedIndex : (this.blockID == Block.oreDiamond.blockID ? Item.diamond.shiftedIndex : (this.blockID == Block.oreLapis.blockID ? Item.dyePowder.shiftedIndex : this.blockID));
	}

	public int quantityDropped(Random random1) {
		return this.blockID == Block.oreLapis.blockID ? 4 + random1.nextInt(5) : 1;
	}

	public int quantityDroppedWithBonus(int i1, Random random2) {
		if(i1 > 0 && this.blockID != this.idDropped(0, random2, i1)) {
			int i3 = random2.nextInt(i1 + 2) - 1;
			if(i3 < 0) {
				i3 = 0;
			}

			return this.quantityDropped(random2) * (i3 + 1);
		} else {
			return this.quantityDropped(random2);
		}
	}

	protected int damageDropped(int i1) {
		return this.blockID == Block.oreLapis.blockID ? 4 : 0;
	}
}

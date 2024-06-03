package net.minecraft.src;

import java.util.Random;

public class BlockOre extends Block {
	public BlockOre(int i1, int i2) {
		super(i1, i2, Material.rock);
	}

	public int idDropped(int i1, Random random2) {
		if(this.blockID == Block.oreCoal.blockID) return Item.coal.shiftedIndex;
		if(this.blockID == Block.oreDiamond.blockID) return Item.diamond.shiftedIndex;
		if(this.blockID == Block.oreLapis.blockID) return Item.dyePowder.shiftedIndex;
		return this.blockID;
	}

	public int quantityDropped(Random random1) {
		return this.blockID == Block.oreLapis.blockID ? 4 + random1.nextInt(5) : 1;
	}

	protected int damageDropped(int i1) {
		return this.blockID == Block.oreLapis.blockID ? 4 : 0;
	}
	
	// Softlocked for b1.2
	public boolean softLocked() {
		return this.blockID == Block.oreLapis.blockID;
	}
}

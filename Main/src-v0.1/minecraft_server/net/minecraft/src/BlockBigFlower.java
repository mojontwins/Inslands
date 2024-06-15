package net.minecraft.src;

public class BlockBigFlower extends Block {
	// Uses metadata to represent three different blocks:
	// meta 0 is red petal block
	// meta 1 is yellow petal block
	// meta 2 is stem.

	protected BlockBigFlower(int id, int blockIndexInTexture, Material material) {
		super(id, blockIndexInTexture, material);
	}
	
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		switch(metadata) {
			case 0: return 11 * 16 + 13;
			case 1: return 11 * 16 + 14;
			case 2:
				if(side < 2) return 71;
				return 11 * 16 + 15;
			default: return this.blockIndexInTexture;
		}
	}

	public Material getBlockMaterialBasedOnmetaData(int meta) {
		return meta == 2 ? Material.wood : Material.leaves;
	}
	
	protected int damageDropped(int i1) {
		return i1;
	}
}

package net.minecraft.src;

public class BlockSlime extends Block {

	protected BlockSlime(int id, int blockIndexInTexture, Material material) {
		super(id, blockIndexInTexture, material);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 106;
	}
	
	public int getRenderBlockPass() {
		return 2;
	}
}

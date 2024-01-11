package net.minecraft.src;

public class BlockBone extends BlockLog {

	public BlockBone(int i1) {
		super(i1, Material.bone);
	}

	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		boolean horizontal = (meta & 4) != 0;
		
		int outTextureIndex = 11 * 16 + 15;
		int endTextureIndex = 11 * 16 + 9;
		
		if (!horizontal) {
			// Vanilla logs:
			return side <= 1 ? endTextureIndex : outTextureIndex;
		} else {
			// Horizontal renderer expects this:
			return side == 1 ? endTextureIndex : outTextureIndex;
		}
	}
}

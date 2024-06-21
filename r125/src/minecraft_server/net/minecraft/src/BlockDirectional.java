package net.minecraft.src;

public abstract class BlockDirectional extends Block {
	protected BlockDirectional(int i1, int i2, Material material3) {
		super(i1, i2, material3);
	}

	protected BlockDirectional(int i1, Material material2) {
		super(i1, material2);
	}

	public static int getDirection(int i0) {
		return i0 & 3;
	}
}

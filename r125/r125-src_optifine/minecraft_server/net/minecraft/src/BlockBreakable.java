package net.minecraft.src;

public class BlockBreakable extends Block {
	private boolean localFlag;

	protected BlockBreakable(int i1, int i2, Material material3, boolean z4) {
		super(i1, i2, material3);
		this.localFlag = z4;
	}

	public boolean isOpaqueCube() {
		return false;
	}
}

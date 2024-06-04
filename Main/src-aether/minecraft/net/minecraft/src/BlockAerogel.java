package net.minecraft.src;

public class BlockAerogel extends Block {
	public BlockAerogel(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/Aerogel.png"), Material.rock);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
	}
}

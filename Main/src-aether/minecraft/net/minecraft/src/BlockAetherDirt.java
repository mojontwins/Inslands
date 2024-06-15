package net.minecraft.src;

public class BlockAetherDirt extends Block {
	protected BlockAetherDirt(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/Dirt.png"), Material.ground);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		world.setBlockWithNotify(i, j, k, this.blockID);
		world.setBlockMetadataWithNotify(i, j, k, 1);
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		if(l == 0 && mod_Aether.equippedSkyrootShovel()) {
			this.dropBlockAsItem(world, i, j, k, l);
		}

		this.dropBlockAsItem(world, i, j, k, l);
	}
}

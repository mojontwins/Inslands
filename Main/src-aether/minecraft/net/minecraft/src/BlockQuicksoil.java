package net.minecraft.src;

public class BlockQuicksoil extends Block {
	public BlockQuicksoil(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/Quicksoil.png"), Material.sand);
		this.slipperiness = 1.1F;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		if(l == 0 && mod_Aether.equippedSkyrootShovel()) {
			this.dropBlockAsItem(world, i, j, k, l);
		}

		this.dropBlockAsItem(world, i, j, k, l);
	}
}

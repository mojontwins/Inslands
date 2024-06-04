package net.minecraft.src;

import java.util.Random;

public class BlockAmbrosiumOre extends Block {
	public BlockAmbrosiumOre(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/AmbrosiumOre.png"), Material.rock);
	}

	public int idDropped(int i, Random random) {
		return AetherItems.AmbrosiumShard.shiftedIndex;
	}

	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		world.setBlockWithNotify(i, j, k, this.blockID);
		world.setBlockMetadataWithNotify(i, j, k, 1);
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		if(l == 0 && mod_Aether.equippedSkyrootPick()) {
			this.dropBlockAsItem(world, i, j, k, l);
		}

		this.dropBlockAsItem(world, i, j, k, l);
	}
}

package net.minecraft.src;

public class BlockIcestone extends Block {
	protected BlockIcestone(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/Icestone.png"), Material.rock);
	}

	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		world.setBlockWithNotify(i, j, k, this.blockID);
		world.setBlockMetadataWithNotify(i, j, k, 1);

		for(int x = i - 3; x < i + 4; ++x) {
			for(int y = j - 1; y < j + 2; ++y) {
				for(int z = k - 3; z < k + 4; ++z) {
					if((x - i) * (x - i) + (y - j) * (y - j) + (z - k) * (z - k) < 8 && world.getBlockId(x, y, z) == Block.waterStill.blockID) {
						world.setBlockWithNotify(x, y, z, Block.ice.blockID);
					}

					if((x - i) * (x - i) + (y - j) * (y - j) + (z - k) * (z - k) < 8 && world.getBlockId(x, y, z) == Block.lavaStill.blockID) {
						world.setBlockWithNotify(x, y, z, Block.obsidian.blockID);
					}
				}
			}
		}

	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		if(l == 0 && mod_Aether.equippedSkyrootPick()) {
			this.dropBlockAsItem(world, i, j, k, l);
		}

		this.dropBlockAsItem(world, i, j, k, l);
	}
}

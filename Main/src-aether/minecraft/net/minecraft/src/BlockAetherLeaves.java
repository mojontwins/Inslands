package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class BlockAetherLeaves extends BlockLeavesBase {
	public static int sprSkyroot = ModLoader.addOverride("/terrain.png", "/aether/blocks/SkyrootLeaves.png");
	public static int sprGoldenOak = ModLoader.addOverride("/terrain.png", "/aether/blocks/GoldenOakLeaves.png");

	protected BlockAetherLeaves(int blockID) {
		super(blockID, blockID == mod_Aether.idBlockGoldenOakLeaves ? sprGoldenOak : sprSkyroot, Material.leaves, false);
		this.setTickOnLoad(true);
	}

	public int quantityDropped(Random random) {
		return this.blockID == AetherBlocks.GoldenOakLeaves.blockID ? (random.nextInt(60) != 0 ? 0 : 1) : (random.nextInt(40) != 0 ? 0 : 1);
	}

	public int idDropped(int i, Random random) {
		return this.blockID == AetherBlocks.SkyrootLeaves.blockID ? AetherBlocks.SkyrootSapling.blockID : (random.nextInt(10) == 0 ? Item.appleGold.shiftedIndex : AetherBlocks.GoldenOakSapling.blockID);
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		byte l = 1;
		int i1 = l + 1;
		if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
			for(int j1 = -l; j1 <= l; ++j1) {
				for(int k1 = -l; k1 <= l; ++k1) {
					for(int l1 = -l; l1 <= l; ++l1) {
						int i2 = world.getBlockId(i + j1, j + k1, k + l1);
						if(i2 == this.blockID) {
							int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
							world.setBlockMetadata(i + j1, j + k1, k + l1, j2 | 8);
						}
					}
				}
			}
		}

	}

	public void updateTick(World world, int px, int py, int pz, Random rand) {
		if(!world.multiplayerWorld) {
			if(!this.nearTrunk(world, px, py, pz)) {
				this.removeLeaves(world, px, py, pz);
			}

		}
	}

	private void removeLeaves(World world, int px, int py, int pz) {
		this.dropBlockAsItem(world, px, py, pz, world.getBlockMetadata(px, py, pz));
		world.setBlockWithNotify(px, py, pz, 0);
	}

	private boolean nearTrunk(World world, int px, int py, int pz) {
		Loc startLoc = new Loc(px, py, pz);
		LinkedList toCheck = new LinkedList();
		ArrayList checked = new ArrayList();
		toCheck.offer(new Loc(px, py, pz));
		int bLeaves = this.blockID;

		while(!toCheck.isEmpty()) {
			Loc curLoc = (Loc)toCheck.poll();
			if(!checked.contains(curLoc)) {
				if(curLoc.distSimple(startLoc) <= 4) {
					int block = curLoc.getBlock(world);
					int meta = curLoc.getMeta(world);
					if(block == AetherBlocks.Log.blockID && this.isMyTrunkMeta(meta)) {
						return true;
					}

					if(block == bLeaves) {
						toCheck.addAll(Arrays.asList(curLoc.adjacent()));
					}
				}

				checked.add(curLoc);
			}
		}

		return false;
	}

	private boolean isMyTrunkMeta(int meta) {
		return this.blockID == AetherBlocks.SkyrootLeaves.blockID ? meta <= 1 : meta >= 2;
	}

	protected int damageDropped(int i) {
		return i & 3;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		if(!world.multiplayerWorld && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(world, i, j, k, new ItemStack(this.blockID, 1, l & 3));
		} else {
			super.harvestBlock(world, entityplayer, i, j, k, l);
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		iblockaccess.getBlockId(i, j, k);
		return true;
	}
}

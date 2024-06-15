package net.minecraft.src;

import java.util.Random;

public class BlockFloating extends Block {
	public static boolean fallInstantly = false;
	private boolean enchanted;

	public BlockFloating(int i, int j, boolean bool) {
		super(i, j, Material.rock);
		this.enchanted = bool;
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate());
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate());
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if(!this.enchanted || this.enchanted && world.isBlockGettingPowered(i, j, k)) {
			this.tryToFall(world, i, j, k);
		}

	}

	private void tryToFall(World world, int i, int j, int k) {
		if(canFallAbove(world, i, j + 1, k) && j < 128) {
			byte byte0 = 32;
			if(!fallInstantly && world.checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0)) {
				EntityFloatingBlock floating = new EntityFloatingBlock(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), this.blockID);
				world.entityJoinedWorld(floating);
			} else {
				world.setBlockWithNotify(i, j, k, 0);

				while(canFallAbove(world, i, j + 1, k) && j < 128) {
					++j;
				}

				if(j > 0) {
					world.setBlockWithNotify(i, j, k, this.blockID);
				}
			}
		}

	}

	public int tickRate() {
		return 3;
	}

	public static boolean canFallAbove(World world, int i, int j, int k) {
		int l = world.getBlockId(i, j, k);
		if(l == 0) {
			return true;
		} else if(l == Block.fire.blockID) {
			return true;
		} else {
			Material material = Block.blocksList[l].blockMaterial;
			return material == Material.water ? true : material == Material.lava;
		}
	}
}

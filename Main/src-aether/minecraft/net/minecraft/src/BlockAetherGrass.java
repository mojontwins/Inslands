package net.minecraft.src;

import java.util.Random;

public class BlockAetherGrass extends Block {
	public static int sprTop = ModLoader.addOverride("/terrain.png", "/aether/blocks/GrassTop.png");
	public static int sprSide = ModLoader.addOverride("/terrain.png", "/aether/blocks/GrassSide.png");

	protected BlockAetherGrass(int blockID) {
		super(blockID, Material.ground);
		this.setTickOnLoad(true);
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return l == 1 ? sprTop : (l == 0 ? AetherBlocks.Dirt.blockIndexInTexture : sprSide);
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if(!world.multiplayerWorld) {
			if(world.getBlockLightValue(i, j + 1, k) < 4 && world.getBlockMaterial(i, j + 1, k).getCanBlockGrass()) {
				if(random.nextInt(4) != 0) {
					return;
				}

				world.setBlockWithNotify(i, j, k, AetherBlocks.Dirt.blockID);
			} else if(world.getBlockLightValue(i, j + 1, k) >= 9) {
				int l = i + random.nextInt(3) - 1;
				int i1 = j + random.nextInt(5) - 3;
				int j1 = k + random.nextInt(3) - 1;
				if(world.getBlockId(l, i1, j1) == AetherBlocks.Dirt.blockID && world.getBlockLightValue(l, i1 + 1, j1) >= 4 && !world.getBlockMaterial(l, i1 + 1, j1).getCanBlockGrass()) {
					world.setBlockWithNotify(l, i1, j1, AetherBlocks.Grass.blockID);
				}
			}

		}
	}

	public int idDropped(int i, Random random) {
		return AetherBlocks.Dirt.idDropped(0, random);
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		if(mod_Aether.equippedSkyrootShovel()) {
			this.dropBlockAsItem(world, i, j, k, l);
		}

		this.dropBlockAsItem(world, i, j, k, l);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityPlayer) {
		if(world.multiplayerWorld) {
			return false;
		} else if(entityPlayer == null) {
			return false;
		} else {
			ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
			if(itemStack == null) {
				return false;
			} else if(itemStack.itemID != Item.dyePowder.shiftedIndex) {
				return false;
			} else if(itemStack.getItemDamage() != 15) {
				return false;
			} else {
				--itemStack.stackSize;
				int iSpawned = 0;

				label54:
				for(int var9 = 0; var9 < 64; ++var9) {
					int var10 = i;
					int var11 = j + 1;
					int var12 = k;

					for(int var13 = 0; var13 < var9 / 16; ++var13) {
						var10 += world.rand.nextInt(3) - 1;
						var11 += (world.rand.nextInt(3) - 1) * world.rand.nextInt(3) / 2;
						var12 += world.rand.nextInt(3) - 1;
						if(world.getBlockId(var10, var11 - 1, var12) != this.blockID || world.isBlockNormalCube(var10, var11, var12)) {
							continue label54;
						}
					}

					if(world.getBlockId(var10, var11, var12) == 0) {
						if(world.rand.nextInt(20 + 10 * iSpawned) == 0) {
							world.setBlockWithNotify(var10, var11, var12, mod_Aether.idBlockWhiteFlower);
							++iSpawned;
						} else if(world.rand.nextInt(10 + 2 * iSpawned) <= 2) {
							world.setBlockWithNotify(var10, var11, var12, mod_Aether.idBlockPurpleFlower);
							++iSpawned;
						}
					}
				}

				return true;
			}
		}
	}
}

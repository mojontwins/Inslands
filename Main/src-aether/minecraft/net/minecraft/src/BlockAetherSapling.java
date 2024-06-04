package net.minecraft.src;

import java.util.Random;

public class BlockAetherSapling extends BlockFlower {
	public static int sprSkyroot = ModLoader.addOverride("/terrain.png", "/aether/blocks/SkyrootSapling.png");
	public static int sprGoldenOak = ModLoader.addOverride("/terrain.png", "/aether/blocks/GoldenOakSapling.png");

	protected BlockAetherSapling(int blockID) {
		super(blockID, blockID == mod_Aether.idBlockGoldenOakSapling ? sprGoldenOak : sprSkyroot);
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		if(!world.multiplayerWorld) {
			super.updateTick(world, i, j, k, random);
			if(world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(30) == 0) {
				this.growTree(world, i, j, k, random);
			}

		}
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return this.blockID == AetherBlocks.GoldenOakSapling.blockID ? sprGoldenOak : sprSkyroot;
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return super.canPlaceBlockAt(world, i, j, k) && this.canThisPlantGrowOnThisBlockID(world.getBlockId(i, j - 1, k));
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i) {
		return i == AetherBlocks.Grass.blockID || i == AetherBlocks.Dirt.blockID;
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
				this.growTree(world, i, j, k, world.rand);
				--itemStack.stackSize;
				return true;
			}
		}
	}

	public void growTree(World world, int i, int j, int k, Random random) {
		world.setBlock(i, j, k, 0);
		Object obj = null;
		if(this.blockID == AetherBlocks.GoldenOakSapling.blockID) {
			obj = new AetherGenGoldenOak();
		} else {
			obj = new AetherGenSkyroot();
		}

		if(!((WorldGenerator)((WorldGenerator)obj)).generate(world, random, i, j, k)) {
			world.setBlock(i, j, k, this.blockID);
		}

	}
}

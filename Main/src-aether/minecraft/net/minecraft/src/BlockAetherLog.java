package net.minecraft.src;

import java.util.Random;

public class BlockAetherLog extends Block {
	public static int sprTop = ModLoader.addOverride("/terrain.png", "/aether/blocks/SkyrootLogTop.png");
	public static int sprSide = ModLoader.addOverride("/terrain.png", "/aether/blocks/SkyrootLogSide.png");
	public static int sprGoldenSide = ModLoader.addOverride("/terrain.png", "/aether/blocks/GoldenOak.png");
	private static Random rand = new Random();

	protected BlockAetherLog(int blockID) {
		super(blockID, sprSide, Material.wood);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return i <= 1 && j <= 3 ? sprTop : (j <= 1 ? sprSide : sprGoldenSide);
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta) {
		entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
		ItemStack stack = new ItemStack(this.blockID, 1, 0);
		if(mod_Aether.equippedSkyrootAxe() && meta != 1) {
			stack.stackSize *= 2;
		}

		SAPI.drop(world, new Loc(x, y, z), stack);
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if(itemstack != null && (itemstack.itemID == AetherItems.AxeZanite.shiftedIndex || itemstack.itemID == AetherItems.AxeGravitite.shiftedIndex || meta < 2)) {
			if(meta > 1 && rand.nextBoolean()) {
				stack = new ItemStack(AetherItems.GoldenAmber.shiftedIndex, 2 + rand.nextInt(2), 0);
				SAPI.drop(world, new Loc(x, y, z), stack);
			}

		}
	}
}

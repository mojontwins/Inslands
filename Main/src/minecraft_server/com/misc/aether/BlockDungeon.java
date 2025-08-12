package com.misc.aether;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockDungeon extends Block {
	public static int sprBronze = BlockTrap.sprBronze;
	public static int sprSilver = BlockTrap.sprSilver;
	public static int sprGold = BlockTrap.sprGold;
	public static int sprBronzeLit = 10*16+13;
	public static int sprSilverLit = 16; //Placeholders
	public static int sprGoldLit = 16;

	public BlockDungeon(int i) {
		super(i, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return j == 2 ? (this.isLit() ? sprGoldLit : sprGold) : (j == 1 ? (this.isLit() ? sprSilverLit : sprSilver) : (this.isLit() ? sprBronzeLit : sprBronze));
	}

	private boolean isLit() {
		return this.blockID == Block.lightDungeonStone.blockID || this.blockID == Block.lockedLightDungeonStone.blockID;
	}

	protected int damageDropped(int i) {
		return i;
	}

	public static int func_21034_c(int i) {
		return ~i & 15;
	}

	public static int func_21035_d(int i) {
		return ~i & 15;
	}
}

package net.minecraft.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.world.block.Block;

public final class Session {
	public static List registeredBlocksList;
	public String name;

	public Session(String string1, String string2) {
		this.name = string1;
	}

	static {
		(registeredBlocksList = new ArrayList()).add(Block.stone);
		registeredBlocksList.add(Block.cobblestone);
		registeredBlocksList.add(Block.brick);
		registeredBlocksList.add(Block.dirt);
		registeredBlocksList.add(Block.planks);
		registeredBlocksList.add(Block.wood);
		registeredBlocksList.add(Block.leaves);
		registeredBlocksList.add(Block.torch);
		registeredBlocksList.add(Block.stairSingle);
		registeredBlocksList.add(Block.glass);
		registeredBlocksList.add(Block.cobblestoneMossy);
		registeredBlocksList.add(Block.sapling);
		registeredBlocksList.add(Block.plantYellow);
		registeredBlocksList.add(Block.plantRed);
		registeredBlocksList.add(Block.mushroomBrown);
		registeredBlocksList.add(Block.mushroomRed);
		registeredBlocksList.add(Block.sand);
		registeredBlocksList.add(Block.gravel);
		registeredBlocksList.add(Block.sponge);
		registeredBlocksList.add(Block.clothRed);
		registeredBlocksList.add(Block.clothOrange);
		registeredBlocksList.add(Block.clothYellow);
		registeredBlocksList.add(Block.clothChartreuse);
		registeredBlocksList.add(Block.clothGreen);
		registeredBlocksList.add(Block.clothSpringGreen);
		registeredBlocksList.add(Block.clothCyan);
		registeredBlocksList.add(Block.clothCapri);
		registeredBlocksList.add(Block.clothUltramarine);
		registeredBlocksList.add(Block.clothViolet);
		registeredBlocksList.add(Block.clothPurple);
		registeredBlocksList.add(Block.clothMagenta);
		registeredBlocksList.add(Block.clothRose);
		registeredBlocksList.add(Block.clothDarkGray);
		registeredBlocksList.add(Block.clothGray);
		registeredBlocksList.add(Block.clothWhite);
		registeredBlocksList.add(Block.oreCoal);
		registeredBlocksList.add(Block.oreIron);
		registeredBlocksList.add(Block.oreGold);
		registeredBlocksList.add(Block.blockSteel);
		registeredBlocksList.add(Block.blockGold);
		registeredBlocksList.add(Block.bookshelf);
		registeredBlocksList.add(Block.tnt);
		registeredBlocksList.add(Block.obsidian);
		System.out.println(registeredBlocksList.size());
	}
}
package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class PoisonIsland {

	public static void addRecipes(CraftingManager craftingManager) {
		craftingManager.addRecipe(new ItemStack(Item.bottleEmpty), new Object[] {" # ","# #", "###", '#', Block.thinGlass});	
	}

}

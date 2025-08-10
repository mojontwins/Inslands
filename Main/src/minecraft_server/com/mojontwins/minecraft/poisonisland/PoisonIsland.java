package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingManager;

public class PoisonIsland {

	public static void addRecipes(CraftingManager craftingManager) {
		craftingManager.addRecipe(new ItemStack(Item.bottleEmpty), new Object[] {" # ","# #", "###", '#', Block.thinGlass});	
	}

}

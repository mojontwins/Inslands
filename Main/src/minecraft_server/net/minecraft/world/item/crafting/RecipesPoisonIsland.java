package net.minecraft.world.item.crafting;

import net.minecraft.src.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RecipesPoisonIsland {

	public static void addRecipes(CraftingManager craftingManager) {
		craftingManager.addRecipe(new ItemStack(Item.bottleEmpty), new Object[] {" # ","# #", "###", '#', Block.thinGlass});	
	}

}

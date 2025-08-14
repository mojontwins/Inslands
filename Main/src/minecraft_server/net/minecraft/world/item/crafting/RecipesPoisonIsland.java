package net.minecraft.world.item.crafting;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.tile.Block;

public class RecipesPoisonIsland {

	public static void addRecipes(CraftingManager craftingManager) {
		craftingManager.addRecipe(new ItemStack(Item.bottleEmpty), new Object[] {" # ","# #", "###", '#', Block.thinGlass});	
	}

}

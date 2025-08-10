package net.minecraft.world.item.crafting;

import net.minecraft.src.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RecipesNether {

	public void addRecipes(CraftingManager craftingManager) {
		craftingManager.addRecipe(new ItemStack(Block.planks2, 4, 0), new Object[]{"#", '#', new ItemStack(Block.wood2, 1, 0)});
		craftingManager.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", '#', Block.planks2});
	}

}

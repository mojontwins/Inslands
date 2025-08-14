package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.tile.Block;

public class RecipesCrafting {
	public void addRecipes(CraftingManager craftingManager1) {
		craftingManager1.addRecipe(new ItemStack(Block.chest), new Object[]{"###", "# #", "###", '#', Block.planks});
		craftingManager1.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[]{"###", "# #", "###", '#', Block.cobblestone});
		craftingManager1.addRecipe(new ItemStack(Block.workbench), new Object[]{"##", "##", '#', Block.planks});
		craftingManager1.addRecipe(new ItemStack(Block.sandStone), new Object[]{"##", "##", '#', Block.sand});
	}
}

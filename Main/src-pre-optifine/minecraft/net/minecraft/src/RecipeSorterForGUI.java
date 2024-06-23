package net.minecraft.src;

import java.util.Comparator;

public class RecipeSorterForGUI implements Comparator<Object> {
	public int compareRecipesForGUI(IRecipe craftingRecipe1, IRecipe craftingRecipe2) {
		if(craftingRecipe2.getRecipeOutput().itemID > craftingRecipe1.getRecipeOutput().itemID) return -1;
		if(craftingRecipe2.getRecipeOutput().itemID < craftingRecipe1.getRecipeOutput().itemID) return 1;
		if(craftingRecipe2.getRecipeOutput().itemDamage > craftingRecipe1.getRecipeOutput().itemDamage) return -1;
		if(craftingRecipe2.getRecipeOutput().itemDamage < craftingRecipe1.getRecipeOutput().itemDamage) return 1;
		return 0;
	}
	
	public int compare(Object object1, Object object2) {
		return this.compareRecipesForGUI((IRecipe)object1, (IRecipe)object2);
	}
}

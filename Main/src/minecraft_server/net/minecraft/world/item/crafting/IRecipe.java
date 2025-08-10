package net.minecraft.world.item.crafting;

import net.minecraft.world.inventory.InventoryCrafting;
import net.minecraft.world.item.ItemStack;

public interface IRecipe {
	public boolean matches(InventoryCrafting i1);

	public ItemStack getCraftingResult(InventoryCrafting i1);

	public int getRecipeSize();

	public ItemStack getRecipeOutput();
	
	public ItemStack[] getRecipeItems();
	
	public int getWidth();

	public int getHeight();
}

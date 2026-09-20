package net.minecraft.world.item.crafting;

import net.minecraft.world.inventory.InventoryCrafting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.tile.Block;

public class RecipesWorldPortal implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inventoryCrafting1) {
		return this.getCraftingResult(inventoryCrafting1) != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting1) {
		ItemStack wool = null;
		for(int i = 0; i < 9; ++i) {
			int row = i / 3;
			int col = i % 3;
			ItemStack stack = inventoryCrafting1.getStackInRowAndColumn(row, col);
			boolean edge = row == 0 || row == 2 || col == 0 || col == 2;
			if(edge) {
				if(stack == null || stack.itemID != Block.obsidian.blockID) return null;
			} else {
				if(stack == null || stack.itemID != Block.cloth.blockID) return null;
				if(wool != null) return null;
				wool = stack;
			}
		}
		if(wool == null) return null;
		return new ItemStack(Block.worldPortal, 1, wool.getItemDamage() & 15);
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Block.worldPortal, 1, 0);
	}

	@Override
	public ItemStack[] getRecipeItems() {
		ItemStack[] items = new ItemStack[9];
		for(int i = 0; i < 9; ++i) {
			boolean edge = (i / 3) == 0 || (i / 3) == 2 || (i % 3) == 0 || (i % 3) == 2;
			items[i] = edge ? new ItemStack(Block.obsidian) : new ItemStack(Block.cloth, 1, 0);
		}
		return items;
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}
}
package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;

public class FurnaceRecipe {
	public final int inputId;
	public final ItemStack outputItemStack;
	
	public FurnaceRecipe(int inputId, ItemStack outputItemStack) {
		this.inputId = inputId;
		this.outputItemStack = outputItemStack;
	}

}

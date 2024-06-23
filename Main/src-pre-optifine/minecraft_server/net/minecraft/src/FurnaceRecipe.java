package net.minecraft.src;

public class FurnaceRecipe {
	public final int inputId;
	public final ItemStack outputItemStack;
	
	public FurnaceRecipe(int inputId, ItemStack outputItemStack) {
		this.inputId = inputId;
		this.outputItemStack = outputItemStack;
	}

}

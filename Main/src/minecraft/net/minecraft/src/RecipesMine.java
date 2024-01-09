package net.minecraft.src;

public class RecipesMine {
	public void addRecipes(CraftingManager craftingManager) {
		// Iron wire & derivatives
		craftingManager.addRecipe(new ItemStack(Item.ironWire, 4), new Object[] {"#", "#", '#', Item.ingotIron}); 
		craftingManager.addRecipe(new ItemStack(Block.ironWall, 5), new Object[] {"# #", " # ", "# #", '#', Item.ironWire});
		craftingManager.addRecipe(new ItemStack(Block.woodenSpikes), new Object[] {"# #", " # ", "# #", '#', Item.stick});
		craftingManager.addRecipe(new ItemStack(Block.barbedWire, 8), new Object[] {"###", "# #", "###", '#', Item.ironWire});
		craftingManager.addRecipe(new ItemStack(Block.blockCoal, 1), new Object[] {"###", "###", "###", '#', Item.coal});
		craftingManager.addRecipe(new ItemStack(Item.coal, 9), new Object[] {"#", '#', Block.blockCoal});
		craftingManager.addRecipe(new ItemStack(Item.slingshot, 1), new Object[] {"#-#", " # ", '#', Item.stick, '-', Item.silk});
		craftingManager.addRecipe(new ItemStack(Block.cobblestone, 1), new Object[] {"###", "###", "###", '#', Item.pebble});
		// craftingManager.addRecipe(new ItemStack(Block.glowStone, 1), new Object[]{"##", "##", Character.valueOf('#'), Item.lightStoneDust});
		craftingManager.addRecipe(new ItemStack(Block.driedKelpBlock, 1), new Object[] {"###", "###", "###", '#', Item.driedKelp});
		craftingManager.addRecipe(new ItemStack(Item.driedKelp, 9), new Object[] {"#", '#', Block.driedKelpBlock});
		/*
		craftingManager.addRecipe(new ItemStack(Block.stairSingle, 3, 1), new Object[]{"###", '#', Block.cobblestone});
		craftingManager.addRecipe(new ItemStack(Block.stairSingle, 3, 2), new Object[]{"###", '#', Block.cobblestoneMossy});
		craftingManager.addRecipe(new ItemStack(Block.stairSingle, 3, 3), new Object[]{"###", '#', Block.planks});
		craftingManager.addRecipe(new ItemStack(Block.stairSingle, 3, 4), new Object[]{"###", '#', Block.dirt});
		craftingManager.addRecipe(new ItemStack(Block.stairSingle, 3, 5), new Object[]{"###", '#', Block.glass});
		*/
		craftingManager.addRecipe(new ItemStack(Block.slimeBlock, 1), new Object[] {"###", "###", "###", '#', Item.slimeBall});
		craftingManager.addRecipe(new ItemStack(Item.slimeBall, 9), new Object[] {"#", '#', Block.slimeBlock});
		craftingManager.addRecipe(new ItemStack(Block.fleshBlock, 1),  new Object[] {"###", "###", "###", '#', Item.rottenFlesh});
		craftingManager.addRecipe(new ItemStack(Block.hayStack, 1), new Object[] {"###", "###", "###", '#', Item.wheat});
		craftingManager.addRecipe(new ItemStack(Item.wheat, 9), new Object[] {"#", '#', Block.hayStack});
		craftingManager.addRecipe(new ItemStack(Block.stoneBricks,4), new Object[] {"##", "##", '#', Block.stone});
		craftingManager.addShapelessRecipe(new ItemStack(Block.wood, 1), new Object[]{new ItemStack(Block.chippedWood, 1, -1), new ItemStack(Block.hollowLog, 1, -1)});
		craftingManager.addShapelessRecipe(new ItemStack(Block.planks,3), new Object[] {new ItemStack(Block.chippedWood, 1, -1)});
		craftingManager.addShapelessRecipe(new ItemStack(Block.planks,1), new Object[] {new ItemStack(Block.hollowLog, 1, -1)});
		
		/*
		craftingManager.addRecipe(new ItemStack(Block.classicPistonBase, 1), new Object[]{" X ", "X#X", "XXX", 'X', Item.ingotIron, '#', Item.redstone});
		craftingManager.addRecipe(new ItemStack(Block.classicStickyPistonBase, 1), new Object[]{" X ", "X#X", "XXX", 'X', Item.ingotGold, '#', Item.redstone});
		*/
	}
}
